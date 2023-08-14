package com.github.xingshuangs.iot.protocol.s7.serializer;


import com.github.xingshuangs.iot.exceptions.S7CommException;
import com.github.xingshuangs.iot.protocol.common.buff.ByteReadBuff;
import com.github.xingshuangs.iot.protocol.common.buff.ByteWriteBuff;
import com.github.xingshuangs.iot.protocol.common.enums.EDataType;
import com.github.xingshuangs.iot.protocol.common.serializer.IPLCSerializable;
import com.github.xingshuangs.iot.protocol.s7.enums.EPlcType;
import com.github.xingshuangs.iot.protocol.s7.model.DataItem;
import com.github.xingshuangs.iot.protocol.s7.model.RequestItem;
import com.github.xingshuangs.iot.protocol.s7.service.S7PLC;
import com.github.xingshuangs.iot.protocol.s7.utils.AddressUtil;

import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * S7序列化工具
 *
 * @author xingshuang
 */
public class S7Serializer implements IPLCSerializable {

    private final S7PLC s7PLC;

    public S7Serializer(S7PLC s7PLC) {
        this.s7PLC = s7PLC;
    }

    /**
     * 静态方法实例对象
     *
     * @param s7PLC plc对象
     * @return 对象实例
     */
    public static S7Serializer newInstance(S7PLC s7PLC) {
        return new S7Serializer(s7PLC);
    }

    /**
     * 将类根据S7Variable注解转换为解析数据
     *
     * @param targetClass 目标类型
     * @return 解析数据列表
     */
    private List<S7ParseData> parseBean(final Class<?> targetClass) {
        List<S7ParseData> s7ParseDataList = new ArrayList<>();

        for (final Field field : targetClass.getDeclaredFields()) {
            final S7Variable s7Variable = field.getAnnotation(S7Variable.class);

            if (s7Variable == null) {
                continue;
            }

            S7Parameter parameter = new S7Parameter(s7Variable.address(), s7Variable.type(), s7Variable.count());
            S7ParseData s7ParseData = this.createS7ParseData(parameter, field);

            s7ParseDataList.add(s7ParseData);
        }
        return s7ParseDataList;
    }

    /**
     * 解析参数
     *
     * @param parameters 参数列表
     * @return 解析列表
     */
    private List<S7ParseData> parseBean(final List<S7Parameter> parameters) {
        try {
            List<S7ParseData> s7ParseDataList = new ArrayList<>();
            for (final S7Parameter p : parameters) {
                if (p == null) {
                    throw new S7CommException("parameters列表中存在null");
                }
                S7ParseData s7ParseData = this.createS7ParseData(p, p.getClass().getDeclaredField("value"));
                s7ParseDataList.add(s7ParseData);
            }
            return s7ParseDataList;
        } catch (NoSuchFieldException e) {
            throw new S7CommException(e);
        }
    }

    /**
     * 校验S7Variable的数据是否满足规则要求
     *
     * @param parameter parameter
     */
    private void checkS7Variable(final S7Parameter parameter) {
        if (parameter.getAddress().isEmpty()) {
            throw new S7CommException("S7参数中[address]不能为空");
        }
        if (parameter.getCount() < 0) {
            throw new S7CommException("S7参数中[count]不能为负数");
        }
        if (parameter.getDataType() == EDataType.STRING && parameter.getCount() > 254) {
            throw new S7CommException("S7参数中字符串类型类型数据的[count]不能大于254");
        }
        if (parameter.getDataType() != EDataType.BYTE && parameter.getDataType() != EDataType.STRING && parameter.getCount() > 1) {
            throw new S7CommException("S7参数中只有[type]=字节和字符串类型数据的[count]才能大于1，其他必须等于1");
        }
    }

    /**
     * 根据条件读取数据
     *
     * @param s7ParseDataList 条件
     */
    private void readDataByCondition(List<S7ParseData> s7ParseDataList) {
        if (s7ParseDataList.isEmpty()) {
            throw new S7CommException("解析出的注解数据个数为空，无法读取数据");
        }

        // 读取PLC数据
        List<RequestItem> requestItems = s7ParseDataList.stream().map(S7ParseData::getRequestItem).collect(Collectors.toList());
        List<DataItem> dataItems = this.s7PLC.readS7Data(requestItems);

        if (s7ParseDataList.size() != dataItems.size()) {
            throw new S7CommException("所需的字段解析项个数与返回的数据项数量不一致，错误");
        }

        // 提取数据
        for (int i = 0; i < dataItems.size(); i++) {
            s7ParseDataList.get(i).setDataItem(dataItems.get(i));
        }
    }

    /**
     * 构建S7ParseData数据
     *
     * @param p     S7Parameter数据
     * @param field 对应字段
     * @return S7ParseData数据
     */
    private S7ParseData createS7ParseData(S7Parameter p, Field field) {
        this.checkS7Variable(p);
        // 组装S7解析数据
        S7ParseData s7ParseData = new S7ParseData();
        s7ParseData.setDataType(p.getDataType());
        s7ParseData.setCount(p.getCount());
        s7ParseData.setField(field);
        if (p.getDataType() == EDataType.BOOL) {
            s7ParseData.setRequestItem(AddressUtil.parseBit(p.getAddress()));
        } else if (p.getDataType() == EDataType.STRING) {
            RequestItem requestItem = AddressUtil.parseByte(p.getAddress(), 1 + p.getCount() * p.getDataType().getByteLength());
            // 为什么字节索引+1，为了避免修改PLC中string[60]类型的第一个字节数据，该数据为字符串的允许最大长度
            // S1200（非S200Smart）:数据类型为 string 的操作数可存储多个字符，最多可包括 254 个字符。字符串中的第一个字节为总长度，第二个字节为有效字符数量。
            // S200SMART:字符串由变量存储时，字符串长度为0至254个字符，最长为255个字节，其中第一个字符为长度字节
            int offset = this.s7PLC.getPlcType() == EPlcType.S200_SMART ? 0 : 1;
            requestItem.setByteAddress(requestItem.getByteAddress() + offset);
            s7ParseData.setRequestItem(requestItem);
        } else {
            s7ParseData.setRequestItem(AddressUtil.parseByte(p.getAddress(), p.getCount() * p.getDataType().getByteLength()));
        }
        return s7ParseData;
    }

    // region read

    @Override
    public <T> T read(Class<T> targetClass) {
        List<S7ParseData> s7ParseDataList = this.parseBean(targetClass);
        this.readDataByCondition(s7ParseDataList);
        return this.fillData(targetClass, s7ParseDataList);
    }

    /**
     * 读取数据
     *
     * @param parameters 参数配置列表
     * @return 结果列表
     */
    public List<S7Parameter> read(List<S7Parameter> parameters) {
        List<S7ParseData> s7ParseDataList = this.parseBean(parameters);
        this.readDataByCondition(s7ParseDataList);
        this.fillData(parameters, s7ParseDataList);
        return parameters;
    }

    /**
     * 提取数据
     *
     * @param targetClass     目标类型
     * @param s7ParseDataList S7解析数据列表
     * @param <T>             类型
     * @return 目标类型的实体对象
     */
    private <T> T fillData(Class<T> targetClass, List<S7ParseData> s7ParseDataList) {
        try {
            final T result = targetClass.newInstance();
            for (S7ParseData item : s7ParseDataList) {
                this.fillField(result, item);
            }
            return result;

        } catch (Exception e) {
            throw new S7CommException("序列化提取数据错误:" + e.getMessage(), e);
        }
    }

    /**
     * 填充数据
     *
     * @param parameters      参数列表
     * @param s7ParseDataList 解析列表
     */
    private void fillData(final List<S7Parameter> parameters, final List<S7ParseData> s7ParseDataList) {
        try {
            for (int i = 0; i < s7ParseDataList.size(); i++) {
                S7ParseData item = s7ParseDataList.get(i);
                S7Parameter parameter = parameters.get(i);
                this.fillField(parameter, item);
            }
        } catch (Exception e) {
            throw new S7CommException("序列化提取数据错误:" + e.getMessage(), e);
        }
    }

    /**
     * 填充字段数据
     *
     * @param result 参数对象
     * @param item   数据对象
     * @param <T>    泛型
     * @throws IllegalAccessException 异常
     */
    private <T> void fillField(T result, S7ParseData item) throws IllegalAccessException {
        ByteReadBuff buff = new ByteReadBuff(item.getDataItem().getData());
        item.getField().setAccessible(true);
        switch (item.getDataType()) {
            case BOOL:
                item.getField().set(result, buff.getBoolean(0));
                break;
            case BYTE:
                item.getField().set(result, buff.getBytes(item.getCount()));
                break;
            case UINT16:
                item.getField().set(result, buff.getUInt16());
                break;
            case INT16:
                item.getField().set(result, buff.getInt16());
                break;
            case TIME:
            case UINT32:
                item.getField().set(result, buff.getUInt32());
                break;
            case INT32:
                item.getField().set(result, buff.getInt32());
                break;
            case FLOAT32:
                item.getField().set(result, buff.getFloat32());
                break;
            case FLOAT64:
                item.getField().set(result, buff.getFloat64());
                break;
            case STRING:
                int length = buff.getByteToInt(0);
                item.getField().set(result, buff.getString(1, Math.min(length, item.getCount()), Charset.forName("GB2312")));
                break;
            case DATE:
                LocalDate date = LocalDate.of(1990, 1, 1).plusDays(buff.getUInt16());
                item.getField().set(result, date);
                break;
            case TIME_OF_DAY:
                LocalTime time = LocalTime.ofSecondOfDay(buff.getUInt32() / 1000);
                item.getField().set(result, time);
                break;
            case DTL:
                int year = buff.getUInt16();
                int month = buff.getByteToInt();
                int dayOfMonth = buff.getByteToInt();
                int week = buff.getByteToInt();
                int hour = buff.getByteToInt();
                int minute = buff.getByteToInt();
                int second = buff.getByteToInt();
                long nanoOfSecond = buff.getUInt32();
                LocalDateTime dateTime = LocalDateTime.of(year, month, dayOfMonth, hour, minute, second, (int) nanoOfSecond);
                item.getField().set(result, dateTime);
                break;
            default:
                throw new S7CommException("无法识别数据类型");
        }
    }

    // endregion

    // region write
    @Override
    public <T> void write(T targetBean) {
        // 解析参数
        List<S7ParseData> s7ParseDataList = this.parseBean(targetBean.getClass());

        if (s7ParseDataList.isEmpty()) {
            throw new S7CommException("解析出的注解数据个数为空，无法读取数据");
        }

        // 填充字节数据
        s7ParseDataList = this.extractData(targetBean, s7ParseDataList);

        // 写入PLC
        List<RequestItem> requestItems = s7ParseDataList.stream().map(S7ParseData::getRequestItem).collect(Collectors.toList());
        List<DataItem> dataItems = s7ParseDataList.stream().map(S7ParseData::getDataItem).collect(Collectors.toList());
        this.s7PLC.writeS7Data(requestItems, dataItems);
    }

    /**
     * 写入数据
     *
     * @param parameters 参数列表
     */
    public void write(List<S7Parameter> parameters) {
        // 解析参数
        List<S7ParseData> s7ParseDataList = this.parseBean(parameters);

        if (s7ParseDataList.size() != parameters.size()) {
            throw new S7CommException("解析出的数据个数与传入的数据个数不一致");
        }

        // 填充字节数据
        s7ParseDataList = this.extractData(parameters, s7ParseDataList);

        // 写入PLC
        List<RequestItem> requestItems = s7ParseDataList.stream().map(S7ParseData::getRequestItem).collect(Collectors.toList());
        List<DataItem> dataItems = s7ParseDataList.stream().map(S7ParseData::getDataItem).collect(Collectors.toList());
        this.s7PLC.writeS7Data(requestItems, dataItems);
    }

    /**
     * 提取数据
     *
     * @param targetBean      目标对象
     * @param s7ParseDataList 解析后的数据列表
     * @param <T>             类型
     * @return 有效的解析后的数据列表
     */
    private <T> List<S7ParseData> extractData(T targetBean, List<S7ParseData> s7ParseDataList) {
        try {
            for (S7ParseData item : s7ParseDataList) {
                item.getField().setAccessible(true);
                Object data = item.getField().get(targetBean);
                if (data == null) {
                    continue;
                }
                this.extractField(item, data);
            }
            return s7ParseDataList.stream().filter(x -> x.getDataItem() != null).collect(Collectors.toList());
        } catch (Exception e) {
            throw new S7CommException("序列化填充字节数据错误:" + e.getMessage(), e);
        }
    }

    /**
     * 提取数据
     *
     * @param parameters      参数数据列表
     * @param s7ParseDataList 解析后的数据列表
     * @return 有效的解析后的数据列表
     */
    private List<S7ParseData> extractData(List<S7Parameter> parameters, List<S7ParseData> s7ParseDataList) {
        try {
            for (int i = 0; i < s7ParseDataList.size(); i++) {
                S7ParseData item = s7ParseDataList.get(i);
                S7Parameter parameter = parameters.get(i);
                if (parameter.getValue() == null) {
                    throw new S7CommException("参数的数值不能为null");
                }
                this.extractField(item, parameter.getValue());
            }
            return s7ParseDataList.stream().filter(x -> x.getDataItem() != null).collect(Collectors.toList());
        } catch (Exception e) {
            throw new S7CommException("序列化填充字节数据错误:" + e.getMessage(), e);
        }
    }

    /**
     * 提取字段数
     *
     * @param item S7的解析数据
     * @param data 数据源
     */
    private void extractField(S7ParseData item, Object data) {
        switch (item.getDataType()) {
            case BOOL:
                item.setDataItem(DataItem.createReqByBoolean((Boolean) data));
                break;
            case BYTE:
                item.setDataItem(DataItem.createReqByByte(ByteReadBuff.newInstance((byte[]) data)
                        .getBytes(item.getCount())));
                break;
            case UINT16:
                item.setDataItem(DataItem.createReqByByte(ByteWriteBuff.newInstance(2)
                        .putShort((Integer) data).getData()));
                break;
            case INT16:
                item.setDataItem(DataItem.createReqByByte(ByteWriteBuff.newInstance(2)
                        .putShort((Short) data).getData()));
                break;
            case TIME:
            case UINT32:
                item.setDataItem(DataItem.createReqByByte(ByteWriteBuff.newInstance(4)
                        .putInteger((Long) data).getData()));
                break;
            case INT32:
                item.setDataItem(DataItem.createReqByByte(ByteWriteBuff.newInstance(4)
                        .putInteger((Integer) data).getData()));
                break;
            case FLOAT32:
                item.setDataItem(DataItem.createReqByByte(ByteWriteBuff.newInstance(4)
                        .putFloat((Float) data).getData()));
                break;
            case FLOAT64:
                item.setDataItem(DataItem.createReqByByte(ByteWriteBuff.newInstance(8)
                        .putDouble((Double) data).getData()));
                break;
            case STRING:
                byte[] bytes = ((String) data).getBytes(Charset.forName("GB2312"));
                byte[] targetBytes = new byte[1 + item.getCount()];
                targetBytes[0] = (byte) item.getCount();
                System.arraycopy(bytes, 0, targetBytes, 1, Math.min(bytes.length, item.getCount()));
                item.setDataItem(DataItem.createReqByByte(targetBytes));
                break;
            case DATE:
                // TODO: 后面时间采用工具类
                LocalDate start = LocalDate.of(1990, 1, 1);
                long date = ((LocalDate) data).toEpochDay() - start.toEpochDay();
                item.setDataItem(DataItem.createReqByByte(ByteWriteBuff.newInstance(2)
                        .putShort((short) date).getData()));
                break;
            case TIME_OF_DAY:
                long timeOfDay = ((LocalTime) data).toSecondOfDay() * 1000L;
                item.setDataItem(DataItem.createReqByByte(ByteWriteBuff.newInstance(4)
                        .putInteger(timeOfDay).getData()));
                break;
            case DTL:
                LocalDateTime dateTime = (LocalDateTime) data;
                byte[] dateTimeData = ByteWriteBuff.newInstance(12)
                        .putShort(dateTime.getYear())
                        .putByte(dateTime.getMonthValue())
                        .putByte(dateTime.getDayOfMonth())
                        .putByte(dateTime.getDayOfWeek().getValue())
                        .putByte(dateTime.getHour())
                        .putByte(dateTime.getMinute())
                        .putByte(dateTime.getSecond())
                        .putInteger(dateTime.getNano())
                        .getData();
                item.setDataItem(DataItem.createReqByByte(dateTimeData));
                break;
            default:
                throw new S7CommException("无法识别数据类型");
        }
    }
    // endregion
}

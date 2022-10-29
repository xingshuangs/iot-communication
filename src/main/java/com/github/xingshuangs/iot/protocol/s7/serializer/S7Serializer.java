package com.github.xingshuangs.iot.protocol.s7.serializer;


import com.github.xingshuangs.iot.exceptions.S7CommException;
import com.github.xingshuangs.iot.protocol.common.buff.ByteReadBuff;
import com.github.xingshuangs.iot.protocol.common.enums.EDataType;
import com.github.xingshuangs.iot.protocol.s7.model.DataItem;
import com.github.xingshuangs.iot.protocol.s7.model.RequestItem;
import com.github.xingshuangs.iot.protocol.s7.service.S7PLC;
import com.github.xingshuangs.iot.protocol.s7.utils.AddressUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * S7序列化工具
 *
 * @author xingshuang
 */
public class S7Serializer implements ISerializable {

    private S7PLC s7PLC;

    public S7Serializer(S7PLC s7PLC) {
        this.s7PLC = s7PLC;
    }

    @Override
    public <T> T read(Class<T> targetClass) {
        // 解析参数
        List<S7ParseData> s7ParseDataList = this.parseBean(targetClass);

        if (s7ParseDataList.isEmpty()) {
            throw new S7CommException("解析出的数据个数为空，无法读取数据");
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
        return this.extractData(targetClass, s7ParseDataList);
    }

    @Override
    public <T> void write(T targetBean) {

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
            this.checkS7Variable(s7Variable);

            // 组装S7解析数据
            S7ParseData s7ParseData = new S7ParseData();
            s7ParseData.setDataType(s7Variable.type());
            s7ParseData.setCount(s7Variable.count());
            s7ParseData.setField(field);
            if (s7Variable.type() == EDataType.BOOL) {
                s7ParseData.setRequestItem(AddressUtil.parseBit(s7Variable.address()));
            } else {
                s7ParseData.setRequestItem(AddressUtil.parseByte(s7Variable.address(),
                        s7Variable.count() * s7Variable.type().getByteLength()));
            }

            s7ParseDataList.add(s7ParseData);
        }
        return s7ParseDataList;
    }

    /**
     * 校验S7Variable的数据是否满足规则要求
     *
     * @param s7Variable s7Variable
     */
    private void checkS7Variable(S7Variable s7Variable) {
        if (s7Variable.address().isEmpty()) {
            throw new S7CommException("S7参数注解中[address]不能为空");
        }
        if (s7Variable.count() < 0) {
            throw new S7CommException("S7参数注解中[count]不能为负数");
        }
        if (s7Variable.type() != EDataType.BYTE && s7Variable.count() > 1) {
            throw new S7CommException("S7参数注解中只有[type]=字节类型数据的[count]才能大于1，其他必须等于1");
        }
    }

    /**
     * 提取数据
     *
     * @param targetClass     目标类型
     * @param s7ParseDataList S7解析数据列表
     * @param <T>             类型
     * @return 目标类型的实体对象
     */
    private <T> T extractData(Class<T> targetClass, List<S7ParseData> s7ParseDataList) {

        try {
            final T result = targetClass.newInstance();
            for (S7ParseData item : s7ParseDataList) {
                ByteReadBuff buff = new ByteReadBuff(item.getDataItem().getData());
                item.getField().setAccessible(true);
                switch (item.getDataType()) {
                    case BOOL:
                        item.getField().set(result, buff.getBoolean(0));
                        break;
                    case BYTE:
                        item.getField().set(result, buff.getBytes());
                        break;
                    case UINT16:
                        item.getField().set(result, buff.getUInt16());
                        break;
                    case INT16:
                        item.getField().set(result, buff.getInt16());
                        break;
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
                    default:
                        throw new S7CommException("无法识别数据类型");
                }
            }
            return result;

        } catch (Exception e) {
            throw new S7CommException("序列化提取数据错误:" + e.getMessage(), e);
        }
    }
}

package com.github.xingshuangs.iot.protocol.common.serializer;


import com.github.xingshuangs.iot.exceptions.ByteArrayParseException;
import com.github.xingshuangs.iot.protocol.common.buff.ByteReadBuff;
import com.github.xingshuangs.iot.protocol.common.buff.ByteWriteBuff;
import com.github.xingshuangs.iot.protocol.common.enums.EDataType;
import com.github.xingshuangs.iot.utils.BooleanUtil;

import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 字节数组序列化工具
 *
 * @author xingshuang
 */
public class ByteArraySerializer implements IByteArraySerializable {

    @Override
    public <T> T toObject(final Class<T> targetClass, final byte[] src) {
        try {
            ByteReadBuff buff = new ByteReadBuff(src);
            final T result = targetClass.newInstance();
            for (final Field field : targetClass.getDeclaredFields()) {
                final ByteArrayVariable variable = field.getAnnotation(ByteArrayVariable.class);
                if (variable == null) {
                    continue;
                }

                this.checkByteArrayVariable(variable);
                field.setAccessible(true);
                this.extractData(buff, result, field, variable);
            }
            return result;
        } catch (Exception e) {
            throw new ByteArrayParseException("解析成对象错误，原因：" + e.getMessage(), e);
        }
    }

    private <T> void extractData(ByteReadBuff buff, T result, Field field, ByteArrayVariable variable) throws IllegalAccessException {
        switch (variable.type()) {
            case BOOL:
                List<Boolean> booleans = IntStream.range(0, variable.count()).boxed()
                        .map(x -> {
                            int byteAdd = variable.byteOffset() + (variable.bitOffset() + x) / 8;
                            int bitAdd = (variable.bitOffset() + x) % 8;
                            return buff.getBoolean(byteAdd, bitAdd);
                        }).collect(Collectors.toList());
                field.set(result, variable.count() == 1 ? booleans.get(0) : booleans);
                break;
            case BYTE:
                List<Byte> bytes = IntStream.range(0, variable.count()).boxed()
                        .map(x -> buff.getByte(variable.byteOffset() + x * variable.type().getByteLength()))
                        .collect(Collectors.toList());
                field.set(result, variable.count() == 1 ? bytes.get(0) : bytes);
                break;
            case UINT16:
                List<Integer> uint16s = IntStream.range(0, variable.count()).boxed()
                        .map(x -> buff.getUInt16(variable.byteOffset() + x * variable.type().getByteLength()))
                        .collect(Collectors.toList());
                field.set(result, variable.count() == 1 ? uint16s.get(0) : uint16s);
                break;
            case INT16:
                List<Short> int16s = IntStream.range(0, variable.count()).boxed()
                        .map(x -> buff.getInt16(variable.byteOffset() + x * variable.type().getByteLength()))
                        .collect(Collectors.toList());
                field.set(result, variable.count() == 1 ? int16s.get(0) : int16s);
                break;
            case UINT32:
                List<Long> uint32s = IntStream.range(0, variable.count()).boxed()
                        .map(x -> buff.getUInt32(variable.byteOffset() + x * variable.type().getByteLength()))
                        .collect(Collectors.toList());
                field.set(result, variable.count() == 1 ? uint32s.get(0) : uint32s);
                break;
            case INT32:
                List<Integer> int32s = IntStream.range(0, variable.count()).boxed()
                        .map(x -> buff.getInt32(variable.byteOffset() + x * variable.type().getByteLength()))
                        .collect(Collectors.toList());
                field.set(result, variable.count() == 1 ? int32s.get(0) : int32s);
                break;
            case FLOAT32:
                List<Float> float32s = IntStream.range(0, variable.count()).boxed()
                        .map(x -> buff.getFloat32(variable.byteOffset() + x * variable.type().getByteLength()))
                        .collect(Collectors.toList());
                field.set(result, variable.count() == 1 ? float32s.get(0) : float32s);
                break;
            case FLOAT64:
                List<Double> float64s = IntStream.range(0, variable.count()).boxed()
                        .map(x -> buff.getFloat64(variable.byteOffset() + x * variable.type().getByteLength()))
                        .collect(Collectors.toList());
                field.set(result, variable.count() == 1 ? float64s.get(0) : float64s);
                break;
            case STRING:
                field.set(result, buff.getString(variable.byteOffset(), variable.count()));
                break;
            default:
                throw new ByteArrayParseException("提取数据的时候无法识别数据类型");
        }
    }

    /**
     * 校验字节数组注解的参数
     *
     * @param variable 注解参数
     */
    private void checkByteArrayVariable(ByteArrayVariable variable) {
        if (variable.byteOffset() < 0) {
            throw new ByteArrayParseException("字节偏移量不能为负数");
        }
        if (variable.count() < 0) {
            throw new ByteArrayParseException("数据个数不能为负数");
        }
        if (variable.type() == EDataType.BOOL && (variable.bitOffset() > 7 || variable.bitOffset() < 0)) {
            throw new ByteArrayParseException("当数据类型为bool时，位偏移量只能是[0,7]");
        }
    }

    @Override
    public <T> byte[] toByteArray(final T targetBean) {
        try {
            // 组装数据，同时计算最大的字节长度
            int buffSize = 0;
            List<ByteArrayParseData> parseDataList = new ArrayList<>();
            for (final Field field : targetBean.getClass().getDeclaredFields()) {
                final ByteArrayVariable variable = field.getAnnotation(ByteArrayVariable.class);
                if (variable == null) {
                    continue;
                }
                this.checkByteArrayVariable(variable);
                parseDataList.add(new ByteArrayParseData(variable, field));
                int maxPos = variable.byteOffset() + variable.count() * variable.type().getByteLength();
                if (maxPos > buffSize) {
                    buffSize = maxPos;
                }
            }
            if (buffSize == 0) {
                return new byte[0];
            }
            // 填充字节数组的内容
            ByteWriteBuff buff = ByteWriteBuff.newInstance(buffSize);
            for (ByteArrayParseData item : parseDataList) {
                Field field = item.getField();
                ByteArrayVariable variable = item.getVariable();
                field.setAccessible(true);
                Object data = field.get(targetBean);
                if (data == null) {
                    continue;
                }
                if (variable.count() == 1) {
                    this.fillOneData(variable, data, buff);
                } else {
                    this.fillListData(variable, data, buff);
                }
            }
            return buff.getData();
        } catch (Exception e) {
            throw new ByteArrayParseException("解析成对象错误，原因：" + e.getMessage(), e);
        }
    }

    private void fillOneData(ByteArrayVariable variable, Object data, ByteWriteBuff buff) {
        switch (variable.type()) {
            case BOOL:
                int byteAdd = variable.byteOffset() + variable.bitOffset() / 8;
                int bitAdd = variable.bitOffset() % 8;
                byte newByte = BooleanUtil.setBit(buff.getByte(byteAdd), bitAdd, (Boolean) data);
                buff.putByte(newByte, byteAdd);
                break;
            case BYTE:
                buff.putByte((Byte) data, variable.byteOffset());
                break;
            case UINT16:
                buff.putShort((Integer) data, variable.byteOffset());
                break;
            case INT16:
                buff.putShort((Short) data, variable.byteOffset());
                break;
            case UINT32:
                buff.putInteger((Long) data, variable.byteOffset());
                break;
            case INT32:
                buff.putInteger((Integer) data, variable.byteOffset());
                break;
            case FLOAT32:
                buff.putFloat((Float) data, variable.byteOffset());
                break;
            case FLOAT64:
                buff.putDouble((Double) data, variable.byteOffset());
                break;
            case STRING:
                buff.putString((String) data, StandardCharsets.US_ASCII, variable.byteOffset());
                break;
            default:
                throw new ByteArrayParseException("填充数据的时候无法识别数据类型");
        }
    }

    private void fillListData(ByteArrayVariable variable, Object data, ByteWriteBuff buff) {
        switch (variable.type()) {
            case BOOL:
                List<Boolean> booleanData = ((List<Boolean>) data);
                for (int i = 0; i < booleanData.size(); i++) {
                    int byteAdd = variable.byteOffset() + (variable.bitOffset() + i) / 8;
                    int bitAdd = (variable.bitOffset() + i) % 8;
                    byte newByte = BooleanUtil.setBit(buff.getByte(byteAdd), bitAdd, booleanData.get(i));
                    buff.putByte(newByte, byteAdd);
                }
                break;
            case BYTE:
                List<Byte> byteData = ((List<Byte>) data);
                for (int i = 0; i < byteData.size(); i++) {
                    buff.putByte(byteData.get(i), variable.byteOffset() + i * variable.type().getByteLength());
                }
                break;
            case UINT16:
                List<Integer> uint16 = ((List<Integer>) data);
                for (int i = 0; i < uint16.size(); i++) {
                    buff.putShort(uint16.get(i), variable.byteOffset() + i * variable.type().getByteLength());
                }
                break;
            case INT16:
                List<Short> int16 = ((List<Short>) data);
                for (int i = 0; i < int16.size(); i++) {
                    buff.putShort(int16.get(i), variable.byteOffset() + i * variable.type().getByteLength());
                }
                break;
            case UINT32:
                List<Long> uint32 = ((List<Long>) data);
                for (int i = 0; i < uint32.size(); i++) {
                    buff.putInteger(uint32.get(i), variable.byteOffset() + i * variable.type().getByteLength());
                }
                break;
            case INT32:
                List<Integer> int32 = ((List<Integer>) data);
                for (int i = 0; i < int32.size(); i++) {
                    buff.putInteger(int32.get(i), variable.byteOffset() + i * variable.type().getByteLength());
                }
                break;
            case FLOAT32:
                List<Float> float32 = ((List<Float>) data);
                for (int i = 0; i < float32.size(); i++) {
                    buff.putFloat(float32.get(i), variable.byteOffset() + i * variable.type().getByteLength());
                }
                break;
            case FLOAT64:
                List<Double> float64 = ((List<Double>) data);
                for (int i = 0; i < float64.size(); i++) {
                    buff.putDouble(float64.get(i), variable.byteOffset() + i * variable.type().getByteLength());
                }
                break;
            case STRING:
                buff.putString((String) data, StandardCharsets.US_ASCII, variable.byteOffset());
                break;
            default:
                throw new ByteArrayParseException("填充数据的时候无法识别数据类型");
        }
    }
}

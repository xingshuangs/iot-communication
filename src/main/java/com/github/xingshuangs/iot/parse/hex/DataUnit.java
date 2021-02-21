/*
  Copyright (C), 2009-2021, 江苏汇博机器人技术股份有限公司
  FileName: DataUnit
  Author:   ShuangPC
  Date:     2021/2/18
  Description: 数据单元
  History:
  <author>         <time>          <version>          <desc>
  作者姓名         修改时间           版本号             描述
 */

package com.github.xingshuangs.iot.parse.hex;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 数据单元
 *
 * @author ShuangPC
 */

public class DataUnit<T> {

    /**
     * 字段名
     */
    private String name = "";

    /**
     * 描述
     */
    private String description = "";

    /**
     * 单位
     */
    private String unit = "";

    /**
     * 字节数组
     */
    private byte[] bytes = new byte[]{};

    /**
     * 具体的数值
     */
    private T value;

    /**
     * 字节偏移量，默认0
     */
    private int byteOffset = 0;

    /**
     * 位偏移量，默认0
     */
    private int bitOffset = 0;

    /**
     * 数据个数，默认1个，字节长度 = 数据个数 * 类型占的字节数
     * "bool": 1个字节,
     * "byte": 1个字节,
     * "ushort": 2个字节,
     * "short": 2个字节,
     * "int": 4个字节,
     * "uint": 4个字节,
     * "float": 4个字节,
     * "double": 8个字节,
     * "string": 1个字节
     */
    private int count = 1;

    /**
     * 字符串类型
     */
    private DataTypeEm dataType = DataTypeEm.STRING;

    /**
     * 是否为小端模式，默认false
     */
    private boolean littleEndian = false;

    public DataUnit() {
    }

    public DataUnit(int byteOffset, String dataType) {
        this.byteOffset = byteOffset;
        this.dataType = DataTypeEm.valueFrom(dataType);
    }

    public DataUnit(int byteOffset, int count, String dataType) {
        this.byteOffset = byteOffset;
        this.count = count;
        this.dataType = DataTypeEm.valueFrom(dataType);
    }

    public DataUnit(int byteOffset,int bitOffset, int count, String dataType) {
        this.byteOffset = byteOffset;
        this.bitOffset = bitOffset;
        this.count = count;
        this.dataType = DataTypeEm.valueFrom(dataType);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public int getByteOffset() {
        return byteOffset;
    }

    public void setByteOffset(int byteOffset) {
        this.byteOffset = byteOffset;
    }

    public int getBitOffset() {
        return bitOffset;
    }

    public void setBitOffset(int bitOffset) {
        this.bitOffset = bitOffset;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public DataTypeEm getDataType() {
        return dataType;
    }

    public void setDataType(DataTypeEm dataType) {
        this.dataType = dataType;
    }

    public boolean getLittleEndian() {
        return littleEndian;
    }

    public void setLittleEndian(boolean littleEndian) {
        this.littleEndian = littleEndian;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DataUnit)) return false;
        DataUnit<?> dataUnit = (DataUnit<?>) o;
        return getByteOffset() == dataUnit.getByteOffset() &&
                getBitOffset() == dataUnit.getBitOffset() &&
                getCount() == dataUnit.getCount() &&
                getLittleEndian() == dataUnit.getLittleEndian() &&
                Objects.equals(getName(), dataUnit.getName()) &&
                Objects.equals(getDescription(), dataUnit.getDescription()) &&
                Objects.equals(getUnit(), dataUnit.getUnit()) &&
                Arrays.equals(getBytes(), dataUnit.getBytes()) &&
                Objects.equals(getValue(), dataUnit.getValue()) &&
                getDataType() == dataUnit.getDataType();
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(getName(), getDescription(), getUnit(), getValue(), getByteOffset(), getBitOffset(), getCount(), getDataType(), getLittleEndian());
        result = 31 * result + Arrays.hashCode(getBytes());
        return result;
    }

    @Override
    public String toString() {
        return "DataUnit{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", unit='" + unit + '\'' +
                ", bytes=" + Arrays.toString(bytes) +
                ", value=" + value +
                ", byteOffset=" + byteOffset +
                ", bitOffset=" + bitOffset +
                ", count=" + count +
                ", dataType='" + dataType + '\'' +
                ", littleEndian=" + littleEndian +
                '}';
    }

    /**
     * 获取总字节长度
     *
     * @return 总字节长度
     */
    public Integer getTotalByteLength() {
        switch (this.dataType) {
            case BOOL:
                return (this.bitOffset + this.count) % 8 == 0 ?
                        (this.bitOffset + this.count) / 8 : (this.bitOffset + this.count) / 8 + 1;
            case INT8:
                return this.count;
            case UINT8:
                return this.count;
            case INT16:
                return this.count * 2;
            case UINT16:
                return this.count * 2;
            case INT32:
                return this.count * 4;
            case UINT32:
                return this.count * 4;
            case FLOAT32:
                return this.count * 4;
            case FLOAT64:
                return this.count * 8;
            case STRING:
                return this.count;
            default:
                return this.count;
        }
    }

    public enum DataTypeEm {
        /**
         * 数据类型
         */
        BOOL("bool"),
        INT8("byte"),
        UINT8("ubyte"),
        INT16("short"),
        UINT16("ushort"),
        INT32("int"),
        UINT32("uint"),
        FLOAT32("float"),
        FLOAT64("double"),
        STRING("string");

        private static final Map<String, DataTypeEm> VALUES;

        static {
            // this prevent values to be assigned with the wrong order
            // and ensure valueOf to work fine
            final DataTypeEm[] values = values();
            VALUES = new HashMap<>();
            for (DataTypeEm item : values) {
                final String value = item.value;
                if (VALUES.containsKey(value)) {
                    throw new AssertionError("value already in use: " + value);
                }
                VALUES.put(value, item);
            }
        }

        String value;

        DataTypeEm(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        /**
         * 根据类型名获取枚举对象
         *
         * @param type 类型名
         * @return 枚举对象
         */
        public static DataTypeEm valueFrom(String type) {
            if (type.length() <= 0 || !VALUES.containsKey(type)) {
                throw new IllegalArgumentException("unknown data type: " + type);
            }
            return VALUES.get(type);
        }
    }
}

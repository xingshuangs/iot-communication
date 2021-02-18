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

package com.github.oscura.iot.parse.hex;

import java.util.Arrays;

/**
 * 数据单元
 *
 * @author ShuangPC
 * @date 2021/2/18
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
    private String dataType = "string";

    /**
     * 是否为小端模式，默认false
     */
    private boolean littleEndian = false;


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

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public boolean isLittleEndian() {
        return littleEndian;
    }

    public void setLittleEndian(boolean littleEndian) {
        this.littleEndian = littleEndian;
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
}

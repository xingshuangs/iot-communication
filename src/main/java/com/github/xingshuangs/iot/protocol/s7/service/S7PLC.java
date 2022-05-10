package com.github.xingshuangs.iot.protocol.s7.service;


import com.github.xingshuangs.iot.protocol.s7.enums.EPlcType;
import com.github.xingshuangs.iot.protocol.s7.model.DataItem;
import com.github.xingshuangs.iot.protocol.s7.model.RequestItem;
import com.github.xingshuangs.iot.protocol.s7.utils.AddressUtil;
import com.github.xingshuangs.iot.utils.BooleanUtil;
import com.github.xingshuangs.iot.utils.FloatUtil;
import com.github.xingshuangs.iot.utils.IntegerUtil;
import com.github.xingshuangs.iot.utils.ShortUtil;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author xingshuang
 */
public class S7PLC extends PLCNetwork {

    public static final int PORT = 102;

    public static final String IP = "127.0.0.1";


    public S7PLC() {
        this(EPlcType.S1200, IP, PORT, 0, 0);
    }

    public S7PLC(EPlcType plcType) {
        this(plcType, IP, PORT, 0, 0);
    }

    public S7PLC(EPlcType plcType, String ip) {
        this(plcType, ip, PORT, 0, 0);
    }

    public S7PLC(EPlcType plcType, String ip, int port, int rack, int slot) {
        super(ip, port);
        this.plcType = plcType;
        this.rack = rack;
        this.slot = slot;
    }

    //region 读取数据

    /**
     * 多地址读取字节数据
     *
     * @param addressRead 地址包装列表
     * @return 字节数组列表
     * @throws IOException IO异常
     */
    public List<byte[]> readMultiByte(MultiAddressRead addressRead) throws IOException {
        List<DataItem> dataItems = this.readS7Data(addressRead.getRequestItems());
        return dataItems.stream().map(DataItem::getData).collect(Collectors.toList());
    }

    /**
     * 单地址字节数据读取
     *
     * @param address 地址
     * @param count   字节个数
     * @return 字节数组
     * @throws IOException IO异常
     */
    public byte[] readByte(String address, int count) throws IOException {
        DataItem dataItem = this.readS7Data(AddressUtil.parseByte(address, count));
        return dataItem.getData();
    }

    /**
     * 读取一个字节
     *
     * @param address 地址
     * @return 一个字节
     * @throws IOException IO异常
     */
    public byte readByte(String address) throws IOException {
        return this.readByte(address, 1)[0];
    }

    /**
     * 读取一个boolean
     *
     * @param address 地址
     * @return 一个boolean值
     * @throws IOException IO异常
     */
    public boolean readBoolean(String address) throws IOException {
        DataItem dataItem = this.readS7Data(AddressUtil.parseBit(address));
        return BooleanUtil.getValue(dataItem.getData()[0], 0);
    }

    /**
     * 读取多个个boolean值
     *
     * @param address 地址
     * @return 多个boolean值
     * @throws IOException IO异常
     */
    public List<Boolean> readBoolean(String... address) throws IOException {
        return this.readBoolean(Arrays.asList(address));
    }

    /**
     * 读取boolean列表
     *
     * @param addresses 地址列表
     * @return boolean列表
     * @throws IOException IO异常
     */
    public List<Boolean> readBoolean(List<String> addresses) throws IOException {
        List<RequestItem> requestItems = addresses.stream().map(AddressUtil::parseBit).collect(Collectors.toList());
        List<DataItem> dataItems = this.readS7Data(requestItems);
        return dataItems.stream().map(x -> BooleanUtil.getValue(x.getData()[0], 0)).collect(Collectors.toList());
    }

    /**
     * 读取一个Int16 2字节数据
     *
     * @param address 地址
     * @return 一个Int16 2字节数据
     * @throws IOException IO异常
     */
    public short readInt16(String address) throws IOException {
        DataItem dataItem = this.readS7Data(AddressUtil.parseByte(address, 2));
        return ShortUtil.toInt16(dataItem.getData());
    }

    /**
     * 读取Int16 2字节数据列表
     *
     * @param address 地址
     * @return Int16 2字节数据列表
     * @throws IOException IO异常
     */
    public List<Short> readInt16(String... address) throws IOException {
        return this.readInt16(Arrays.asList(address));
    }

    /**
     * 读取Int16 2字节数据列表
     *
     * @param addresses 地址列表
     * @return Int16 2字节数据列表
     * @throws IOException IO异常
     */
    public List<Short> readInt16(List<String> addresses) throws IOException {
        List<RequestItem> requestItems = addresses.stream().map(x -> AddressUtil.parseByte(x, 2)).collect(Collectors.toList());
        List<DataItem> dataItems = this.readS7Data(requestItems);
        return dataItems.stream().map(x -> ShortUtil.toInt16(x.getData())).collect(Collectors.toList());
    }

    /**
     * 读取一个UInt16 2字节数据
     *
     * @param address 地址
     * @return 一个UInt16 2字节数据
     * @throws IOException IO异常
     */
    public int readUInt16(String address) throws IOException {
        DataItem dataItem = this.readS7Data(AddressUtil.parseByte(address, 2));
        return ShortUtil.toUInt16(dataItem.getData());
    }

    /**
     * 读取UInt16 2字节数据列表
     *
     * @param address 地址
     * @return UInt16 2字节数据列表
     * @throws IOException IO异常
     */
    public List<Integer> readUInt16(String... address) throws IOException {
        return this.readUInt16(Arrays.asList(address));
    }

    /**
     * 读取UInt16 2字节数据列表
     *
     * @param addresses 地址列表
     * @return UInt16 2字节数据列表
     * @throws IOException IO异常
     */
    public List<Integer> readUInt16(List<String> addresses) throws IOException {
        List<RequestItem> requestItems = addresses.stream().map(x -> AddressUtil.parseByte(x, 2)).collect(Collectors.toList());
        List<DataItem> dataItems = this.readS7Data(requestItems);
        return dataItems.stream().map(x -> ShortUtil.toUInt16(x.getData())).collect(Collectors.toList());
    }

    /**
     * 读取一个UInt32 4字节数据
     *
     * @param address 地址
     * @return 一个UInt32 4字节数据
     * @throws IOException IO异常
     */
    public int readInt32(String address) throws IOException {
        DataItem dataItem = this.readS7Data(AddressUtil.parseByte(address, 4));
        return IntegerUtil.toInt32(dataItem.getData());
    }

    /**
     * 读取UInt32 4字节数据列表
     *
     * @param address 地址
     * @return UInt32 4字节数据列表
     * @throws IOException IO异常
     */
    public List<Integer> readInt32(String... address) throws IOException {
        return this.readInt32(Arrays.asList(address));
    }

    /**
     * 读取UInt32 4字节数据列表
     *
     * @param addresses 地址列表
     * @return UInt32 4字节数据列表
     * @throws IOException IO异常
     */
    public List<Integer> readInt32(List<String> addresses) throws IOException {
        List<RequestItem> requestItems = addresses.stream().map(x -> AddressUtil.parseByte(x, 4)).collect(Collectors.toList());
        List<DataItem> dataItems = this.readS7Data(requestItems);
        return dataItems.stream().map(x -> IntegerUtil.toInt32(x.getData())).collect(Collectors.toList());
    }

    /**
     * 读取一个UInt32 4字节数据
     *
     * @param address 地址
     * @return 一个UInt32 4字节数据
     * @throws IOException IO异常
     */
    public long readUInt32(String address) throws IOException {
        DataItem dataItem = this.readS7Data(AddressUtil.parseByte(address, 4));
        return IntegerUtil.toUInt32(dataItem.getData());
    }

    /**
     * 读取UInt32 4字节数据列表
     *
     * @param address 地址
     * @return UInt32 4字节数据列表
     * @throws IOException IO异常
     */
    public List<Long> readUInt32(String... address) throws IOException {
        return this.readUInt32(Arrays.asList(address));
    }

    /**
     * 读取UInt32 4字节数据列表
     *
     * @param addresses 地址列表
     * @return UInt32 4字节数据列表
     * @throws IOException IO异常
     */
    public List<Long> readUInt32(List<String> addresses) throws IOException {
        List<RequestItem> requestItems = addresses.stream().map(x -> AddressUtil.parseByte(x, 4)).collect(Collectors.toList());
        List<DataItem> dataItems = this.readS7Data(requestItems);
        return dataItems.stream().map(x -> IntegerUtil.toUInt32(x.getData())).collect(Collectors.toList());
    }

    /**
     * 读取一个Float32的数据
     *
     * @param address 地址
     * @return 一个Float32的数据
     * @throws IOException IO异常
     */
    public float readFloat32(String address) throws IOException {
        DataItem dataItem = this.readS7Data(AddressUtil.parseByte(address, 4));
        return FloatUtil.toFloat32(dataItem.getData());
    }

    /**
     * 读取多个Float32的数据
     *
     * @param address 地址
     * @return 多个Float32的数据
     * @throws IOException IO异常
     */
    public List<Float> readFloat32(String... address) throws IOException {
        return this.readFloat32(Arrays.asList(address));
    }

    /**
     * 读取多个Float32的数据
     *
     * @param addresses 地址列表
     * @return 多个Float32的数据
     * @throws IOException IO异常
     */
    public List<Float> readFloat32(List<String> addresses) throws IOException {
        List<RequestItem> requestItems = addresses.stream().map(x -> AddressUtil.parseByte(x, 4)).collect(Collectors.toList());
        List<DataItem> dataItems = this.readS7Data(requestItems);
        return dataItems.stream().map(x -> FloatUtil.toFloat32(x.getData())).collect(Collectors.toList());
    }

    /**
     * 读取一个Float32的数据
     *
     * @param address 地址
     * @return 一个Float32的数据
     * @throws IOException
     */
    public double readFloat64(String address) throws IOException {
        DataItem dataItem = this.readS7Data(AddressUtil.parseByte(address, 8));
        return FloatUtil.toFloat64(dataItem.getData());
    }

    /**
     * 读取多个Float32的数据
     *
     * @param address 多个地址
     * @return 多个Float32的数据
     * @throws IOException IO异常
     */
    public List<Double> readFloat64(String... address) throws IOException {
        return this.readFloat64(Arrays.asList(address));
    }

    /**
     * 读取多个Float32的数据
     *
     * @param addresses 地址列表
     * @return 多个Float32的数据
     * @throws IOException IO异常
     */
    public List<Double> readFloat64(List<String> addresses) throws IOException {
        List<RequestItem> requestItems = addresses.stream().map(x -> AddressUtil.parseByte(x, 8)).collect(Collectors.toList());
        List<DataItem> dataItems = this.readS7Data(requestItems);
        return dataItems.stream().map(x -> FloatUtil.toFloat64(x.getData())).collect(Collectors.toList());
    }

    //endregion

    //region 写入数据

    /**
     * 写入boolean数据
     *
     * @param address 地址
     * @param data    boolean数据
     * @throws IOException IO异常
     */
    public void writeBoolean(String address, boolean data) throws IOException {
        this.writeS7Data(AddressUtil.parseBit(address), DataItem.byBoolean(data));
    }

    /**
     * 写入字节数据
     *
     * @param address 地址
     * @param data    字节数据
     * @throws IOException IO异常
     */
    public void writeByte(String address, byte data) throws IOException {
        this.writeS7Data(AddressUtil.parseByte(address, 1), DataItem.byByte(data));
    }

    /**
     * 写入字节列表数据
     *
     * @param address 地址
     * @param data    字节列表数据
     * @throws IOException IO异常
     */
    public void writeByte(String address, byte[] data) throws IOException {
        this.writeS7Data(AddressUtil.parseByte(address, data.length), DataItem.byByte(data));
    }

    /**
     * 写入UInt16数据
     *
     * @param address 地址
     * @param data    UInt16数据
     * @throws IOException IO异常
     */
    public void writeUInt16(String address, int data) throws IOException {
        this.writeByte(address, ShortUtil.toByteArray(data));
    }

    /**
     * 写入Int16数据
     *
     * @param address 地址
     * @param data    Int16数据
     * @throws IOException IO异常
     */
    public void writeInt16(String address, short data) throws IOException {
        this.writeByte(address, ShortUtil.toByteArray(data));
    }

    /**
     * 写入UInt32数据
     *
     * @param address 地址
     * @param data    UInt32数据
     * @throws IOException IO异常
     */
    public void writeUInt32(String address, long data) throws IOException {
        this.writeByte(address, IntegerUtil.toByteArray(data));
    }

    /**
     * 写入Int32数据
     *
     * @param address 地址
     * @param data    Int32数据
     * @throws IOException IO异常
     */
    public void writeInt32(String address, int data) throws IOException {
        this.writeByte(address, IntegerUtil.toByteArray(data));
    }

    /**
     * 写入Float32数据
     *
     * @param address 地址
     * @param data    Float32数据
     * @throws IOException IO异常
     */
    public void writeFloat32(String address, float data) throws IOException {
        this.writeByte(address, FloatUtil.toByteArray(data));
    }

    /**
     * 写入Float64数据
     *
     * @param address 地址
     * @param data    Float64数据
     * @throws IOException IO异常
     */
    public void writeFloat64(String address, double data) throws IOException {
        this.writeByte(address, FloatUtil.toByteArray(data));
    }

    /**
     * 多地址写入数据
     *
     * @param addressWrite 数据
     * @throws IOException IO异常
     */
    public void writeMultiData(MultiAddressWrite addressWrite) throws IOException {
        this.writeS7Data(addressWrite.getRequestItems(), addressWrite.getDataItems());
    }

    //endregion
}

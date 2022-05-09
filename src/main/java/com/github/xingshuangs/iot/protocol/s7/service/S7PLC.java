package com.github.xingshuangs.iot.protocol.s7.service;


import com.github.xingshuangs.iot.protocol.s7.enums.EPlcType;
import com.github.xingshuangs.iot.protocol.s7.model.DataItem;
import com.github.xingshuangs.iot.protocol.s7.model.RequestItem;
import com.github.xingshuangs.iot.protocol.s7.utils.S7AddressUtil;
import com.github.xingshuangs.iot.utils.BooleanUtil;
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

    private EPlcType plcType = EPlcType.S1200;

    public S7PLC() {
        this(EPlcType.S1200, IP, PORT);
    }

    public S7PLC(EPlcType plcType) {
        this(plcType, IP, PORT);
    }

    public S7PLC(EPlcType plcType, String ip) {
        this(plcType, ip, PORT);
    }

    public S7PLC(EPlcType plcType, String ip, int port) {
        super(ip, port);
        this.plcType = plcType;
    }

    /**
     * 多地址读取字节数据
     *
     * @param addressWrappers 地址包装列表
     * @return 字节数组列表
     * @throws IOException IO异常
     */
    public List<byte[]> readByte(List<AddressWrapper> addressWrappers) throws IOException {
        List<RequestItem> requestItems = addressWrappers.stream()
                .map(x -> S7AddressUtil.parseByte(x.getAddress(), x.getCount())).collect(Collectors.toList());
        List<DataItem> dataItems = this.readS7Data(requestItems);
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
        DataItem dataItem = this.readS7Data(S7AddressUtil.parseByte(address, count));
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

    public boolean readBoolean(String address) throws IOException {
        DataItem dataItem = this.readS7Data(S7AddressUtil.parseBit(address));
        return BooleanUtil.getValue(dataItem.getData()[0], 0);
    }

    /**
     * 读取一个boolean值
     *
     * @param address 地址
     * @return 一个boolean
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
        List<RequestItem> requestItems = addresses.stream().map(S7AddressUtil::parseBit).collect(Collectors.toList());
        List<DataItem> dataItems = this.readS7Data(requestItems);
        return dataItems.stream().map(x -> BooleanUtil.getValue(x.getData()[0], 0)).collect(Collectors.toList());
    }

    /**
     * 读取一个UInt16 2字节数据
     *
     * @param address 地址
     * @return 一个UInt16 2字节数据
     * @throws IOException IO异常
     */
    public int readUInt16(String address) throws IOException {
        DataItem dataItem = this.readS7Data(S7AddressUtil.parseByte(address, 2));
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
        List<RequestItem> requestItems = addresses.stream().map(x -> S7AddressUtil.parseByte(x, 2)).collect(Collectors.toList());
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
    public long readUInt32(String address) throws IOException {
        DataItem dataItem = this.readS7Data(S7AddressUtil.parseByte(address, 4));
        return IntegerUtil.toInt32(dataItem.getData());
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
        List<RequestItem> requestItems = addresses.stream().map(x -> S7AddressUtil.parseByte(x, 4)).collect(Collectors.toList());
        List<DataItem> dataItems = this.readS7Data(requestItems);
        return dataItems.stream().map(x -> IntegerUtil.toUInt32(x.getData())).collect(Collectors.toList());
    }
}

package com.github.xingshuangs.iot.protocol.s7.service;


import com.github.xingshuangs.iot.protocol.common.buff.ByteReadBuff;
import com.github.xingshuangs.iot.protocol.common.buff.ByteWriteBuff;
import com.github.xingshuangs.iot.protocol.s7.enums.*;
import com.github.xingshuangs.iot.protocol.s7.model.DataItem;
import com.github.xingshuangs.iot.protocol.s7.model.RequestItem;
import com.github.xingshuangs.iot.protocol.s7.model.RequestNckItem;
import com.github.xingshuangs.iot.protocol.s7.model.S7Data;
import com.github.xingshuangs.iot.protocol.s7.utils.AddressUtil;
import com.github.xingshuangs.iot.utils.*;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author xingshuang
 */
public class S7PLC extends PLCNetwork {

    public static final int PORT = 102;

    public static final int DEFAULT_PDU_LENGTH = 240;

    public static final String IP = "127.0.0.1";

    public S7PLC() {
        this(EPlcType.S1200, IP, PORT, 0, 1, DEFAULT_PDU_LENGTH);
    }

    public S7PLC(EPlcType plcType) {
        this(plcType, IP, PORT, 0, 1, DEFAULT_PDU_LENGTH);
    }

    public S7PLC(EPlcType plcType, String ip) {
        this(plcType, ip, PORT, 0, 1, DEFAULT_PDU_LENGTH);
    }

    public S7PLC(EPlcType plcType, String ip, int port, int rack, int slot) {
        this(plcType, ip, port, rack, slot, DEFAULT_PDU_LENGTH);
    }

    public S7PLC(EPlcType plcType, String ip, int port, int rack, int slot, int pduLength) {
        super(ip, port);
        this.plcType = plcType;
        this.rack = rack;
        this.slot = slot;
        this.pduLength = pduLength;
    }

    //region 读取数据

    /**
     * 最原始的方式读取生数据
     *
     * @param variableType 参数类型
     * @param count        数据个数
     * @param area         区域
     * @param dbNumber     DB块编号
     * @param byteAddress  字节地址
     * @param bitAddress   位地址
     * @return 字节数组
     */
    public byte[] readRaw(EParamVariableType variableType, int count, EArea area, int dbNumber, int byteAddress, int bitAddress) {
        if (count <= 0) {
            throw new IllegalArgumentException("count<=0");
        }
        if (dbNumber < 0) {
            throw new IllegalArgumentException("dbNumber<0");
        }
        if (byteAddress < 0) {
            throw new IllegalArgumentException("byteAddress<0");
        }
        if (bitAddress < 0 || bitAddress > 7) {
            throw new IllegalArgumentException("bitAddress<0||bitAddress>7");
        }
        RequestItem requestItem = RequestItem.createByParams(variableType, count, area, dbNumber, byteAddress, bitAddress);
        DataItem dataItem = this.readS7Data(requestItem);
        return dataItem.getData();
    }

    /**
     * 多地址读取字节数据
     *
     * @param addressRead 地址包装列表
     * @return 字节数组列表
     */
    public List<byte[]> readMultiByte(MultiAddressRead addressRead) {
        List<DataItem> dataItems = this.readS7Data(addressRead.getRequestItems());
        return dataItems.stream().map(DataItem::getData).collect(Collectors.toList());
    }

    /**
     * 单地址字节数据读取
     *
     * @param address 地址
     * @param count   字节个数
     * @return 字节数组
     */
    public byte[] readByte(String address, int count) {
        DataItem dataItem = this.readS7Data(AddressUtil.parseByte(address, count));
        return dataItem.getData();
    }

    /**
     * 读取一个字节
     *
     * @param address 地址
     * @return 一个字节
     */
    public byte readByte(String address) {
        return this.readByte(address, 1)[0];
    }

    /**
     * 读取一个boolean
     *
     * @param address 地址
     * @return 一个boolean值
     */
    public boolean readBoolean(String address) {
        DataItem dataItem = this.readS7Data(AddressUtil.parseBit(address));
        return BooleanUtil.getValue(dataItem.getData()[0], 0);
    }

    /**
     * 读取多个个boolean值
     *
     * @param address 地址
     * @return 多个boolean值
     */
    public List<Boolean> readBoolean(String... address) {
        return this.readBoolean(Arrays.asList(address));
    }

    /**
     * 读取boolean列表
     *
     * @param addresses 地址列表
     * @return boolean列表
     */
    public List<Boolean> readBoolean(List<String> addresses) {
        List<RequestItem> requestItems = addresses.stream().map(AddressUtil::parseBit).collect(Collectors.toList());
        List<DataItem> dataItems = this.readS7Data(requestItems);
        return dataItems.stream().map(x -> BooleanUtil.getValue(x.getData()[0], 0)).collect(Collectors.toList());
    }

    /**
     * 读取一个Int16 2字节数据
     *
     * @param address 地址
     * @return 一个Int16 2字节数据
     */
    public short readInt16(String address) {
        DataItem dataItem = this.readS7Data(AddressUtil.parseByte(address, 2));
        return ShortUtil.toInt16(dataItem.getData());
    }

    /**
     * 读取Int16 2字节数据列表
     *
     * @param address 地址
     * @return Int16 2字节数据列表
     */
    public List<Short> readInt16(String... address) {
        return this.readInt16(Arrays.asList(address));
    }

    /**
     * 读取Int16 2字节数据列表
     *
     * @param addresses 地址列表
     * @return Int16 2字节数据列表
     */
    public List<Short> readInt16(List<String> addresses) {
        List<RequestItem> requestItems = addresses.stream().map(x -> AddressUtil.parseByte(x, 2)).collect(Collectors.toList());
        List<DataItem> dataItems = this.readS7Data(requestItems);
        return dataItems.stream().map(x -> ShortUtil.toInt16(x.getData())).collect(Collectors.toList());
    }

    /**
     * 读取一个UInt16 2字节数据
     *
     * @param address 地址
     * @return 一个UInt16 2字节数据
     */
    public int readUInt16(String address) {
        DataItem dataItem = this.readS7Data(AddressUtil.parseByte(address, 2));
        return ShortUtil.toUInt16(dataItem.getData());
    }

    /**
     * 读取UInt16 2字节数据列表
     *
     * @param address 地址
     * @return UInt16 2字节数据列表
     */
    public List<Integer> readUInt16(String... address) {
        return this.readUInt16(Arrays.asList(address));
    }

    /**
     * 读取UInt16 2字节数据列表
     *
     * @param addresses 地址列表
     * @return UInt16 2字节数据列表
     */
    public List<Integer> readUInt16(List<String> addresses) {
        List<RequestItem> requestItems = addresses.stream().map(x -> AddressUtil.parseByte(x, 2)).collect(Collectors.toList());
        List<DataItem> dataItems = this.readS7Data(requestItems);
        return dataItems.stream().map(x -> ShortUtil.toUInt16(x.getData())).collect(Collectors.toList());
    }

    /**
     * 读取一个UInt32 4字节数据
     *
     * @param address 地址
     * @return 一个UInt32 4字节数据
     */
    public int readInt32(String address) {
        DataItem dataItem = this.readS7Data(AddressUtil.parseByte(address, 4));
        return IntegerUtil.toInt32(dataItem.getData());
    }

    /**
     * 读取UInt32 4字节数据列表
     *
     * @param address 地址
     * @return UInt32 4字节数据列表
     */
    public List<Integer> readInt32(String... address) {
        return this.readInt32(Arrays.asList(address));
    }

    /**
     * 读取UInt32 4字节数据列表
     *
     * @param addresses 地址列表
     * @return UInt32 4字节数据列表
     */
    public List<Integer> readInt32(List<String> addresses) {
        List<RequestItem> requestItems = addresses.stream().map(x -> AddressUtil.parseByte(x, 4)).collect(Collectors.toList());
        List<DataItem> dataItems = this.readS7Data(requestItems);
        return dataItems.stream().map(x -> IntegerUtil.toInt32(x.getData())).collect(Collectors.toList());
    }

    /**
     * 读取一个UInt32 4字节数据
     *
     * @param address 地址
     * @return 一个UInt32 4字节数据
     */
    public long readUInt32(String address) {
        DataItem dataItem = this.readS7Data(AddressUtil.parseByte(address, 4));
        return IntegerUtil.toUInt32(dataItem.getData());
    }

    /**
     * 读取UInt32 4字节数据列表
     *
     * @param address 地址
     * @return UInt32 4字节数据列表
     */
    public List<Long> readUInt32(String... address) {
        return this.readUInt32(Arrays.asList(address));
    }

    /**
     * 读取UInt32 4字节数据列表
     *
     * @param addresses 地址列表
     * @return UInt32 4字节数据列表
     */
    public List<Long> readUInt32(List<String> addresses) {
        List<RequestItem> requestItems = addresses.stream().map(x -> AddressUtil.parseByte(x, 4)).collect(Collectors.toList());
        List<DataItem> dataItems = this.readS7Data(requestItems);
        return dataItems.stream().map(x -> IntegerUtil.toUInt32(x.getData())).collect(Collectors.toList());
    }

    /**
     * 读取一个Float32的数据
     *
     * @param address 地址
     * @return 一个Float32的数据
     */
    public float readFloat32(String address) {
        DataItem dataItem = this.readS7Data(AddressUtil.parseByte(address, 4));
        return FloatUtil.toFloat32(dataItem.getData());
    }

    /**
     * 读取多个Float32的数据
     *
     * @param address 地址
     * @return 多个Float32的数据
     */
    public List<Float> readFloat32(String... address) {
        return this.readFloat32(Arrays.asList(address));
    }

    /**
     * 读取多个Float32的数据
     *
     * @param addresses 地址列表
     * @return 多个Float32的数据
     */
    public List<Float> readFloat32(List<String> addresses) {
        List<RequestItem> requestItems = addresses.stream().map(x -> AddressUtil.parseByte(x, 4)).collect(Collectors.toList());
        List<DataItem> dataItems = this.readS7Data(requestItems);
        return dataItems.stream().map(x -> FloatUtil.toFloat32(x.getData())).collect(Collectors.toList());
    }

    /**
     * 读取一个Float64的数据
     *
     * @param address 地址
     * @return 一个Float64的数据
     */
    public double readFloat64(String address) {
        DataItem dataItem = this.readS7Data(AddressUtil.parseByte(address, 8));
        return FloatUtil.toFloat64(dataItem.getData());
    }

    /**
     * 读取多个Float64的数据
     *
     * @param address 多个地址
     * @return 多个Float64的数据
     */
    public List<Double> readFloat64(String... address) {
        return this.readFloat64(Arrays.asList(address));
    }

    /**
     * 读取多个Float64的数据
     *
     * @param addresses 地址列表
     * @return 多个Float64的数据
     */
    public List<Double> readFloat64(List<String> addresses) {
        List<RequestItem> requestItems = addresses.stream().map(x -> AddressUtil.parseByte(x, 8)).collect(Collectors.toList());
        List<DataItem> dataItems = this.readS7Data(requestItems);
        return dataItems.stream().map(x -> FloatUtil.toFloat64(x.getData())).collect(Collectors.toList());
    }

    /**
     * 读取字符串
     * String（字符串）数据类型存储一串单字节字符，
     * String提供了多大256个字节，前两个字节分别表示字节中最大的字符数和当前的字符数，定义字符串的最大长度可以减少它的占用存储空间
     *
     * @param address 地址
     * @return 字符串
     */
    public String readString(String address) {
        DataItem dataItem = this.readS7Data(AddressUtil.parseByte(address, 2));
//        int total = ByteUtil.toUInt8(dataItem.getData(), 0);
//        if (total == 0 || total == 255) {
//            throw new S7CommException("该地址的值不是字符串类型");
//        }
        int length = ByteUtil.toUInt8(dataItem.getData(), 1);
        dataItem = this.readS7Data(AddressUtil.parseByte(address, 2 + length));
        return ByteUtil.toStr(dataItem.getData(), 2);
    }

    /**
     * 读取字符串
     *
     * @param address 地址
     * @param length  字符串长度
     * @return 字符串
     */
    public String readString(String address, int length) {
        if (length <= 0 || length > 254) {
            throw new IllegalArgumentException("length <= 0 || length > 254");
        }
        DataItem dataItem = this.readS7Data(AddressUtil.parseByte(address, 2 + length));
//        int total = ByteUtil.toUInt8(dataItem.getData(), 0);
//        if (total == 0 || total == 255) {
//            throw new S7CommException("该地址的值不是字符串类型");
//        }
        int actLength = ByteUtil.toUInt8(dataItem.getData(), 1);
        return ByteUtil.toStr(dataItem.getData(), 2, Math.min(actLength, length));
    }

//    /**
//     * 读取字符串
//     * Wsting数据类型与sting数据类型接近，支持单字值的较长字符串，
//     * 第一个字包含最大总字符数，下一个字包含的是当前的总字符数，接下来的字符串可含最多65534个字
//     *
//     * @param address 地址
//     * @return 字符串
//     */
//    public String readWString(String address) {
//        DataItem dataItem = this.readS7Data(AddressUtil.parseByte(address, 4));
//        int type = ShortUtil.toUInt16(dataItem.getData());
//        if (type == 0 || type == 65535) {
//            throw new S7CommException("该地址的值不是字符串WString类型");
//        }
//        int length = ShortUtil.toUInt16(dataItem.getData(), 2);
//        dataItem = this.readS7Data(AddressUtil.parseByte(address, 4 + length * 2));
//        return ByteUtil.toStr(dataItem.getData(), 4);
//    }

    /**
     * 读取时间，时间为毫秒时间，ms，例如1000ms
     *
     * @param address 地址
     * @return 时间，ms
     */
    public long readTime(String address) {
        return this.readUInt32(address);
    }

    /**
     * 读取日期，例如：2023-04-04
     *
     * @param address 地址
     * @return 日期
     */
    public LocalDate readDate(String address) {
        int offset = this.readUInt16(address);
        return LocalDate.of(1990, 1, 1).plusDays(offset);
    }

    /**
     * 读取一天中的时间，例如：23:56:31
     *
     * @param address 地址
     * @return 时间
     */
    public LocalTime readTimeOfDay(String address) {
        long value = this.readUInt32(address);
        return LocalTime.ofSecondOfDay(value / 1000);
    }

    /**
     * 日期和时间的数据类型
     *
     * @param address 地址
     * @return 日期时间
     */
    public LocalDateTime readDTL(String address) {
        byte[] bytes = this.readByte(address, 12);
        ByteReadBuff buff = ByteReadBuff.newInstance(bytes);
        int year = buff.getUInt16();
        int month = buff.getByteToInt();
        int dayOfMonth = buff.getByteToInt();
        int week = buff.getByteToInt();
        int hour = buff.getByteToInt();
        int minute = buff.getByteToInt();
        int second = buff.getByteToInt();
        long nanoOfSecond = buff.getUInt32();
        return LocalDateTime.of(year, month, dayOfMonth, hour, minute, second, (int) nanoOfSecond);
    }

    //endregion

    //region 写入数据

    /**
     * 最原始的方式写入生数据
     *
     * @param variableType     参数类型
     * @param count            数据个数
     * @param area             区域
     * @param dbNumber         DB块编号
     * @param byteAddress      字节地址
     * @param bitAddress       位地址
     * @param dataVariableType 数据变量类型
     * @param data             数据字节数组
     */
    public void writeRaw(EParamVariableType variableType, int count, EArea area, int dbNumber, int byteAddress,
                         int bitAddress, EDataVariableType dataVariableType, byte[] data) {
        if (count <= 0) {
            throw new IllegalArgumentException("count<=0");
        }
        if (dbNumber <= 0) {
            throw new IllegalArgumentException("dbNumber<=0");
        }
        if (byteAddress < 0) {
            throw new IllegalArgumentException("byteAddress<0");
        }
        if (bitAddress < 0 || bitAddress > 7) {
            throw new IllegalArgumentException("bitAddress<0||bitAddress>7");
        }
        if (data == null || data.length == 0) {
            throw new IllegalArgumentException("data");
        }
        RequestItem requestItem = RequestItem.createByParams(variableType, count, area, dbNumber, byteAddress, bitAddress);
        DataItem dataItem = DataItem.createReq(data, dataVariableType);
        this.writeS7Data(requestItem, dataItem);
    }

    /**
     * 写入boolean数据
     *
     * @param address 地址
     * @param data    boolean数据
     */
    public void writeBoolean(String address, boolean data) {
        this.writeS7Data(AddressUtil.parseBit(address), DataItem.createReqByBoolean(data));
    }

    /**
     * 写入字节数据
     *
     * @param address 地址
     * @param data    字节数据
     */
    public void writeByte(String address, byte data) {
        this.writeS7Data(AddressUtil.parseByte(address, 1), DataItem.createReqByByte(data));
    }

    /**
     * 写入字节列表数据
     *
     * @param address 地址
     * @param data    字节列表数据
     */
    public void writeByte(String address, byte[] data) {
        this.writeS7Data(AddressUtil.parseByte(address, data.length), DataItem.createReqByByte(data));
    }

    /**
     * 写入UInt16数据
     *
     * @param address 地址
     * @param data    UInt16数据
     */
    public void writeUInt16(String address, int data) {
        this.writeByte(address, ShortUtil.toByteArray(data));
    }

    /**
     * 写入Int16数据
     *
     * @param address 地址
     * @param data    Int16数据
     */
    public void writeInt16(String address, short data) {
        this.writeByte(address, ShortUtil.toByteArray(data));
    }

    /**
     * 写入UInt32数据
     *
     * @param address 地址
     * @param data    UInt32数据
     */
    public void writeUInt32(String address, long data) {
        this.writeByte(address, IntegerUtil.toByteArray(data));
    }

    /**
     * 写入Int32数据
     *
     * @param address 地址
     * @param data    Int32数据
     */
    public void writeInt32(String address, int data) {
        this.writeByte(address, IntegerUtil.toByteArray(data));
    }

    /**
     * 写入Float32数据
     *
     * @param address 地址
     * @param data    Float32数据
     */
    public void writeFloat32(String address, float data) {
        this.writeByte(address, FloatUtil.toByteArray(data));
    }

    /**
     * 写入Float64数据
     *
     * @param address 地址
     * @param data    Float64数据
     */
    public void writeFloat64(String address, double data) {
        this.writeByte(address, FloatUtil.toByteArray(data));
    }

    /**
     * 多地址写入数据
     *
     * @param addressWrite 数据
     */
    public void writeMultiData(MultiAddressWrite addressWrite) {
        this.writeS7Data(addressWrite.getRequestItems(), addressWrite.getDataItems());
    }

    /**
     * 写入字符串数据
     * String（字符串）数据类型存储一串单字节字符，
     * String提供了多大256个字节，前两个字节分别表示字节中最大的字符数和当前的字符数，定义字符串的最大长度可以减少它的占用存储空间
     *
     * @param address 地址
     * @param data    字符串数据
     */
    public void writeString(String address, String data) {
        if (data.length() == 0) {
            throw new IllegalArgumentException("data字符串参数长度为0");
        }
        // 填充字节长度数据
        byte[] dataBytes = data.getBytes(StandardCharsets.US_ASCII);
        byte[] tmp = new byte[1 + dataBytes.length];
        tmp[0] = ByteUtil.toByte(dataBytes.length);
        System.arraycopy(dataBytes, 0, tmp, 1, dataBytes.length);
        // 字节索引+1
        RequestItem requestItem = AddressUtil.parseByte(address, tmp.length);
        requestItem.setByteAddress(requestItem.getByteAddress() + 1);
        // 通信交互
        this.writeS7Data(requestItem, DataItem.createReqByByte(tmp));
    }

//    /**
//     * 写入字符串数据
//     * Wsting数据类型与sting数据类型接近，支持单字值的较长字符串，
//     * 第一个字包含最大总字符数，下一个字包含的是当前的总字符数，接下来的字符串可含最多65534个字
//     *
//     * @param address 地址
//     * @param data    字符串数据
//     */
//    public void writeWString(String address, String data) {
//        if (data.length() > (65534*2-4)) {
//            throw new IllegalArgumentException("data字符串参数过长");
//        }
//        byte[] dataBytes = data.getBytes(StandardCharsets.US_ASCII);
//        byte[] tmp = new byte[2 + dataBytes.length];
//        byte[] lengthBytes = ShortUtil.toByteArray(dataBytes.length / 2);
//        tmp[0] = (byte) 0xFF;
//        tmp[1] = (byte) 0xFE;
//        tmp[2] = lengthBytes[0];
//        tmp[3] = lengthBytes[1];
//        System.arraycopy(dataBytes, 0, tmp, 4, dataBytes.length);
//        this.writeByte(address, tmp);
//    }

    /**
     * 写入时间，时间为毫秒时间，ms
     *
     * @param address 地址
     * @param time    时间，ms
     */
    public void writeTime(String address, long time) {
        this.writeUInt32(address, time);
    }

    /**
     * 读取日期
     *
     * @param address 地址
     * @param date    日期
     */
    public void writeDate(String address, LocalDate date) {
        LocalDate start = LocalDate.of(1990, 1, 1);
        long value = date.toEpochDay() - start.toEpochDay();
        this.writeUInt16(address, (int) value);
    }

    /**
     * 写入一天中的时间
     *
     * @param address 地址
     * @param time    时间
     */
    public void writeTimeOfDay(String address, LocalTime time) {
        int value = time.toSecondOfDay();
        this.writeUInt32(address, (long) value * 1000);
    }

    /**
     * 写入具体的时间
     *
     * @param address  地址
     * @param dateTime LocalDateTime对象
     */
    public void writeDTL(String address, LocalDateTime dateTime) {
        byte[] data = ByteWriteBuff.newInstance(12)
                .putShort(dateTime.getYear())
                .putByte(dateTime.getMonthValue())
                .putByte(dateTime.getDayOfMonth())
                .putByte(dateTime.getDayOfWeek().getValue())
                .putByte(dateTime.getHour())
                .putByte(dateTime.getMinute())
                .putByte(dateTime.getSecond())
                .putInteger(dateTime.getNano())
                .getData();
        this.writeByte(address, data);
    }

    //endregion

    //region 控制部分

    /**
     * 热重启
     */
    public void hotRestart() {
        this.readFromServerByPersistence(S7Data.createHotRestart());
    }

    /**
     * 冷重启
     */
    public void coldRestart() {
        this.readFromServerByPersistence(S7Data.createColdRestart());
    }

    /**
     * PLC停止
     */
    public void plcStop() {
        this.readFromServerByPersistence(S7Data.createPlcStop());
    }

    /**
     * 将ram复制到rom
     */
    public void copyRamToRom() {
        this.readFromServerByPersistence(S7Data.createCopyRamToRom());
    }

    /**
     * 压缩
     */
    public void compress() {
        this.readFromServerByPersistence(S7Data.createCompress());
    }

    //endregion

    //region NCK

    //    04.08.07.00.020
    //    828D_04.08
    //    16/11/21 20:34:20
    //    828D-ME42
    //    SOC2
    //    machineTool

    /**
     * CNC的ID<br>
     * 发送[29]：03 00 00 1D 02 F0 80 32 01 00 00 00 00 00 0C 00 00 04 01 12 08 82 01 46 6E 00 01 1A 01 <br>
     * 接收[57]：03 00 00 39 02 F0 80 32 03 00 00 00 00 00 02 00 24 00 00 04 01 FF 09 00 20 30 30 30 30 36 30 31 39 33 30 38 38 46 43 30 30 30 30 37 35 00 00 00 00 00 00 00 00 00 00 00 00
     *
     * @return 数据
     */
    public String readCncId() {
        // 12 08 82 01 46 6E 00 01 1A 01
        RequestNckItem requestNckItem = new RequestNckItem(ENckArea.N_NCK, 1, 18030, 1, ENckModule.M, 1);
        DataItem dataItem = this.readS7NckData(requestNckItem);
        return ByteReadBuff.newInstance(dataItem.getData(), true).getString(dataItem.getCount()).trim();
    }

    /**
     * CNC的Version<br>
     * 发送[29]：03 00 00 1D 02 F0 80 32 01 00 00 00 00 00 0C 00 00 04 01 12 08 82 01 46 78 00 01 1A 01 <br>
     * 接收[45]：03 00 00 2D 02 F0 80 32 03 00 00 00 00 00 02 00 18 00 00 04 01 FF 09 00 14 30 34 2E 30 38 2E 30 37 2E 30 30 2E 30 32 30 20 20 20 20 00
     *
     * @return 数据
     */
    public String readCncVersion() {
        // 12 08 82 01 46 78 00 01 1A 01
        RequestNckItem requestNckItem = new RequestNckItem(ENckArea.N_NCK, 1, 18040, 1, ENckModule.M, 1);
        DataItem dataItem = this.readS7NckData(requestNckItem);
        return ByteReadBuff.newInstance(dataItem.getData(), true).getString(dataItem.getCount()).trim();
    }

    /**
     * 类型<br>
     * 发送[29]：03 00 00 1D 02 F0 80 32 01 00 00 00 00 00 0C 00 00 04 01 12 08 82 01 46 78 00 02 1A 01<br>
     * 接收[45]：03 00 00 2D 02 F0 80 32 03 00 00 00 00 00 02 00 18 00 00 04 01 FF 09 00 14 38 32 38 44 5F 30 34 2E 30 38 20 20 20 20 20 00 00 00 00 00
     *
     * @return 数据
     */
    public String readCncType1() {
        // 12 08 82 01 46 78 00 01 1A 01
        RequestNckItem requestNckItem = new RequestNckItem(ENckArea.N_NCK, 1, 18040, 2, ENckModule.M, 1);
        DataItem dataItem = this.readS7NckData(requestNckItem);
        return ByteReadBuff.newInstance(dataItem.getData(), true).getString(dataItem.getCount()).trim();
    }

    /**
     * CNC的Version<br>
     * 发送[29]：03 00 00 1D 02 F0 80 32 01 00 00 00 00 00 0C 00 00 04 01 12 08 82 01 46 78 00 01 1A 01<br>
     * 接收[45]：03 00 00 2D 02 F0 80 32 03 00 00 00 00 00 02 00 18 00 00 04 01 FF 09 00 14 30 34 2E 30 38 2E 30 37 2E 30 30 2E 30 32 30 20 20 20 20 00
     *
     * @return 数据
     */
    public String readCncManufactureDate() {
        // 12 08 82 01 46 78 00 03 1A 01
        RequestNckItem requestNckItem = new RequestNckItem(ENckArea.N_NCK, 1, 18040, 3, ENckModule.M, 1);
        DataItem dataItem = this.readS7NckData(requestNckItem);
        return ByteReadBuff.newInstance(dataItem.getData(), true).getString(dataItem.getCount()).trim();
    }

    /**
     * CNC的Type<br>
     * 发送[29]：03 00 00 1D 02 F0 80 32 01 00 00 00 00 00 0C 00 00 04 01 12 08 82 01 46 78 00 04 1A 01<br>
     * 接收[45]：03 00 00 2D 02 F0 80 32 03 00 00 00 00 00 02 00 18 00 00 04 01 FF 09 00 14 38 32 38 44 2D 4D 45 34 32 00 00 00 00 00 00 00 00 00 00 00
     *
     * @return 数据
     */
    public String readCncType() {
        // 12 08 82 01 46 78 00 04 1A 01
        RequestNckItem requestNckItem = new RequestNckItem(ENckArea.N_NCK, 1, 18040, 4, ENckModule.M, 1);
        DataItem dataItem = this.readS7NckData(requestNckItem);
        return ByteReadBuff.newInstance(dataItem.getData(), true).getString(dataItem.getCount()).trim();
    }

    /**
     * 获取机械坐标系<br>
     * 发送[29]：03 00 00 1D 02 F0 80 32 01 00 00 00 13 00 0C 00 00 04 01 12 08 82 41 00 02 00 01 74 01<br>
     * 接收[33]：03 00 00 21 02 F0 80 32 03 00 00 00 13 00 02 00 0C 00 00 04 01 FF 09 00 08 CD CC CC CC CC 6C 61 40<br>
     * <p>
     * 另一种方式也可以一个request，lineCount=3，结果有3个数据<br>
     * 发送[29]：03 00 00 1D 02 F0 80 32 01 00 00 00 02 00 0C 00 00 04 01 12 08 82 41 00 02 00 01 74 03<br>
     * 接收[49]：03 00 00 31 02 F0 80 32 03 00 00 00 02 00 02 00 1C 00 00 04 01 FF 09 00 18 D8 B6 28 B3 41 26 69 3F 2D 43 1C EB E2 36 3A BF E7 52 5C 55 F6 5D 41 3F
     *
     * @return 数据
     */
    public List<Double> readMachinePosition() {
        // 12 08 82 41 00 02 00 01 74 01
        // 12 08 82 41 00 02 00 02 74 01
        // 12 08 82 41 00 02 00 03 74 01
        List<RequestNckItem> requestNckItems = IntStream.of(1, 2, 3, 4)
                .mapToObj(x -> new RequestNckItem(ENckArea.C_CHANNEL, 1, 2, x, ENckModule.SMA, 1))
                .collect(Collectors.toList());
        List<DataItem> dataItems = this.readS7NckData(requestNckItems);
        return dataItems.stream().map(x -> ByteReadBuff.newInstance(x.getData(), true).getFloat64())
                .collect(Collectors.toList());
    }

    /**
     * 获取相对坐标系<br>
     * 发送[29]：03 00 00 1D 02 F0 80 32 01 00 00 00 13 00 0C 00 00 04 01 12 08 82 41 00 19 00 01 70 01<br>
     * 接收[33]：03 00 00 21 02 F0 80 32 03 00 00 00 13 00 02 00 0C 00 00 04 01 FF 09 00 08 5B B6 D6 17 89 2D C8 40
     *
     * @return 数据
     */
    public List<Double> readRelativePosition() {
        // 12 08 82 41 00 19 00 01 70 01
        // 12 08 82 41 00 19 00 02 70 01
        // 12 08 82 41 00 19 00 03 70 01
        List<RequestNckItem> requestNckItems = IntStream.of(1, 2, 3, 4)
                .mapToObj(x -> new RequestNckItem(ENckArea.C_CHANNEL, 1, 25, x, ENckModule.SEGA, 1))
                .collect(Collectors.toList());
        List<DataItem> dataItems = this.readS7NckData(requestNckItems);
        return dataItems.stream().map(x -> ByteReadBuff.newInstance(x.getData(), true).getFloat64())
                .collect(Collectors.toList());
    }

    /**
     * 获取剩余坐标系<br>
     * 发送[59]：03 00 00 3B 02 F0 80 32 01 00 00 00 00 00 2A 00 00 04 04 12 08 82 41 00 03 00 01 74 01 12 08 82 41 00 03 00 02 74 01 12 08 82 41 00 03 00 03 74 01 12 08 82 41 00 03 00 04 74 01<br>
     * 接收[69]：03 00 00 45 02 F0 80 32 03 00 00 00 00 00 02 00 30 00 00 04 04 FF 09 00 08 00 00 00 00 00 00 00 00 FF 09 00 08 00 00 00 00 00 00 00 00 FF 09 00 08 00 00 00 00 00 00 00 00 FF 09 00 08 00 00 00 00 00 00 00 00
     *
     * @return 数据
     */
    public List<Double> readRemainPosition() {
        // 12 08 82 41 00 03 00 01 74 01
        // 12 08 82 41 00 03 00 02 74 01
        // 12 08 82 41 00 03 00 03 74 01
        List<RequestNckItem> requestNckItems = IntStream.of(1, 2, 3, 4)
                .mapToObj(x -> new RequestNckItem(ENckArea.C_CHANNEL, 1, 3, x, ENckModule.SMA, 1))
                .collect(Collectors.toList());
        List<DataItem> dataItems = this.readS7NckData(requestNckItems);
        return dataItems.stream().map(x -> ByteReadBuff.newInstance(x.getData(), true).getFloat64())
                .collect(Collectors.toList());
    }

    /**
     * T工件坐标<br>
     * 发送[49]：03 00 00 31 02 F0 80 32 01 00 00 00 00 00 20 00 00 04 03 12 08 82 41 00 01 00 04 12 01 12 08 82 41 00 01 00 05 12 01 12 08 82 41 00 01 00 06 12 01<br>
     * 接收[57]：03 00 00 39 02 F0 80 32 03 00 00 00 00 00 02 00 24 00 00 04 03 FF 09 00 08 00 00 00 00 00 00 00 80 FF 09 00 08 00 00 00 00 00 00 00 80 FF 09 00 08 00 00 00 00 00 00 00 80
     *
     * @return 数据
     */
    public List<Double> readTWorkPiecePosition() {
        // 12 08 82 41 00 01 00 04 12 01
        // 12 08 82 41 00 01 00 05 12 01
        // 12 08 82 41 00 01 00 06 12 01
        List<RequestNckItem> requestNckItems = IntStream.of(4, 5, 6)
                .mapToObj(x -> new RequestNckItem(ENckArea.C_CHANNEL, 1, 1, x, ENckModule.FU, 1))
                .collect(Collectors.toList());
        List<DataItem> dataItems = this.readS7NckData(requestNckItems);
        return dataItems.stream().map(x -> ByteReadBuff.newInstance(x.getData(), true).getFloat64())
                .collect(Collectors.toList());
    }

    /**
     * 刀具半径补偿编号<br>
     * 发送[29]：03 00 00 1D 02 F0 80 32 01 00 00 00 13 00 0C 00 00 04 01 12 08 82 41 00 23 00 01 7F 01<br>
     * 接收[57]：03 00 00 39 02 F0 80 32 03 00 00 00 13 00 02 00 24 00 00 04 01 FF 09 00 20 32 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00
     *
     * @return 数据
     */
    public int readToolRadiusCompensationNumber() {
        // 12 08 82 41 00 23 00 01 7F 01
        RequestNckItem requestNckItem = new RequestNckItem(ENckArea.C_CHANNEL, 1, 35, 1, ENckModule.S, 1);
        DataItem dataItem = this.readS7NckData(requestNckItem);
        return ByteReadBuff.newInstance(dataItem.getData(), true).getUInt16();
    }

    /**
     * 刀具编号<br>
     * 发送[29]：03 00 00 1D 02 F0 80 32 01 00 00 00 13 00 0C 00 00 04 01 12 08 82 41 00 17 00 01 7F 01<br>
     * 接收[27]：03 00 00 1B 02 F0 80 32 03 00 00 00 13 00 02 00 06 00 00 04 01 FF 09 00 02 01 00
     *
     * @return 数据
     */
    public int readToolNumber() {
        // 12 08 82 41 00 17 00 01 7F 01
        RequestNckItem requestNckItem = new RequestNckItem(ENckArea.C_CHANNEL, 1, 23, 1, ENckModule.S, 1);
        DataItem dataItem = this.readS7NckData(requestNckItem);
        return ByteReadBuff.newInstance(dataItem.getData(), true).getUInt16();
    }

    /**
     * 实际主轴转速<br>
     * 发送[29]：03 00 00 1D 02 F0 80 32 01 00 00 00 13 00 0C 00 00 04 01 12 08 82 41 00 02 00 01 72 01<br>
     * 接收[33]：03 00 00 21 02 F0 80 32 03 00 00 00 13 00 02 00 0C 00 00 04 01 FF 09 00 08 00 00 00 00 00 00 00 00
     *
     * @return 数据
     */
    public double readActSpindleSpeed() {
        // 12 08 82 41 00 02 00 01 72 01
        RequestNckItem item = new RequestNckItem(ENckArea.C_CHANNEL, 1, 2, 1, ENckModule.SSP, 1);
        DataItem dataItem = this.readS7NckData(item);
        return ByteReadBuff.newInstance(dataItem.getData(), true).getFloat64();
    }

    /**
     * 设定主轴转速<br>
     * 发送[29]：03 00 00 1D 02 F0 80 32 01 00 00 00 00 00 0C 00 00 04 01 12 08 82 41 00 04 00 01 72 01<br>
     * 接收[33]：03 00 00 21 02 F0 80 32 03 00 00 00 00 00 02 00 0C 00 00 04 01 FF 09 00 08 00 00 00 00 00 00 59 40
     *
     * @return 数据
     */
    public double readSetSpindleSpeed() {
        // 12 08 82 01 00 03 00 04 72 01
        RequestNckItem item = new RequestNckItem(ENckArea.N_NCK, 1, 3, 4, ENckModule.SSP, 1);
        DataItem dataItem = this.readS7NckData(item);
        return ByteReadBuff.newInstance(dataItem.getData(), true).getFloat64();
    }

    /**
     * 主轴速率<br>
     * 发送[29]：03 00 00 1D 02 F0 80 32 01 00 00 00 00 00 0C 00 00 04 01 12 08 82 41 00 04 00 01 72 01<br>
     * 接收[33]：03 00 00 21 02 F0 80 32 03 00 00 00 00 00 02 00 0C 00 00 04 01 FF 09 00 08 00 00 00 00 00 00 59 40
     *
     * @return 数据
     */
    public double readSpindleRate() {
        // 12 08 82 41 00 04 00 01 72 01
        RequestNckItem item = new RequestNckItem(ENckArea.C_CHANNEL, 1, 4, 1, ENckModule.SSP, 1);
        DataItem dataItem = this.readS7NckData(item);
        return ByteReadBuff.newInstance(dataItem.getData(), true).getFloat64();
    }

    /**
     * 进给速率<br>
     * 发送[29]：03 00 00 1D 02 F0 80 32 01 00 00 00 13 00 0C 00 00 04 01 12 08 82 41 00 03 00 01 7F 01<br>
     * 接收[33]：03 00 00 21 02 F0 80 32 03 00 00 00 13 00 02 00 0C 00 00 04 01 FF 09 00 08 00 00 00 00 00 00 00 00
     *
     * @return 数据
     */
    public double readFeedRate() {
        // 12 08 82 41 00 03 00 01 7F 01
        RequestNckItem item = new RequestNckItem(ENckArea.C_CHANNEL, 1, 3, 1, ENckModule.S, 1);
        DataItem dataItem = this.readS7NckData(item);
        return ByteReadBuff.newInstance(dataItem.getData(), true).getFloat64();
    }

    /**
     * 获取设定进给速率<br>
     * 发送[29]：03 00 00 1D 02 F0 80 32 01 00 00 00 13 00 0C 00 00 04 01 12 08 82 41 00 02 00 01 7F 01<br>
     * 接收[33]：03 00 00 21 02 F0 80 32 03 00 00 00 13 00 02 00 0C 00 00 04 01 FF 09 00 08 00 00 00 00 00 00 00 00
     *
     * @return 数据
     */
    public double readSetFeedRate() {
        // 12 08 82 41 00 02 00 01 7F 01
        RequestNckItem item = new RequestNckItem(ENckArea.C_CHANNEL, 1, 2, 1, ENckModule.S, 1);
        DataItem dataItem = this.readS7NckData(item);
        return ByteReadBuff.newInstance(dataItem.getData(), true).getFloat64();
    }

    /**
     * 获取实际进给速率<br>
     * 发送[29]：03 00 00 1D 02 F0 80 32 01 00 00 00 13 00 0C 00 00 04 01 12 08 82 41 00 01 00 01 7F 01<br>
     * 接收[33]：03 00 00 21 02 F0 80 32 03 00 00 00 13 00 02 00 0C 00 00 04 01 FF 09 00 08 00 00 00 00 00 00 00 00
     *
     * @return 数据
     */
    public double readActFeedRate() {
        // 12 08 82 41 00 01 00 01 7F 01
        RequestNckItem item = new RequestNckItem(ENckArea.C_CHANNEL, 1, 1, 1, ENckModule.S, 1);
        DataItem dataItem = this.readS7NckData(item);
        return ByteReadBuff.newInstance(dataItem.getData(), true).getFloat64();
    }

    /**
     * 工作模式的请求，0:JOG, 1:MDA, 2:AUTO, 其他<br>
     * 发送[29]：03 00 00 1D 02 F0 80 32 01 00 00 00 00 00 0C 00 00 04 01 12 08 82 21 00 03 00 01 7F 01<br>
     * 接收[27]：03 00 00 1B 02 F0 80 32 03 00 00 00 00 00 02 00 06 00 00 04 01 FF 09 00 02 00 00
     *
     * @return 数据
     */
    public int readWorkMode() {
        // 12 08 82 21 00 03 00 01 7F 01
        RequestNckItem item = new RequestNckItem(ENckArea.B_MODE_GROUP, 1, 3, 1, ENckModule.S, 1);
        DataItem dataItem = this.readS7NckData(item);
        return ByteReadBuff.newInstance(dataItem.getData(), true).getUInt16();
    }

    /**
     * 状态，2:stop, 1:start, 0:reset<br>
     * 发送[29]：03 00 00 1D 02 F0 80 32 01 00 00 00 00 00 0C 00 00 04 01 12 08 82 41 00 0B 00 01 7F 01<br>
     * 接收[27]：03 00 00 1B 02 F0 80 32 03 00 00 00 00 00 02 00 06 00 00 04 01 FF 09 00 02 02 00
     *
     * @return 数据
     */
    public int readStatus() {
        // 12 08 82 41 00 0b 00 01 7F 01
        RequestNckItem item = new RequestNckItem(ENckArea.C_CHANNEL, 1, 11, 1, ENckModule.S, 1);
        DataItem dataItem = this.readS7NckData(item);
        return ByteReadBuff.newInstance(dataItem.getData(), true).getUInt16();
    }

    /**
     * 运行时间<br>
     * 发送[29]：03 00 00 1D 02 F0 80 32 01 00 00 00 00 00 0C 00 00 04 01 12 08 82 41 01 29 00 01 7F 01<br>
     * 接收[33]：03 00 00 21 02 F0 80 32 03 00 00 00 00 00 02 00 0C 00 00 04 01 FF 09 00 08 00 00 00 00 00 00 00 00
     *
     * @return 数据
     */
    public double readRunTime() {
        // 12 08 82 41 01 29 00 01 7F 01
        RequestNckItem item = new RequestNckItem(ENckArea.C_CHANNEL, 1, 297, 1, ENckModule.S, 1);
        DataItem dataItem = this.readS7NckData(item);
        return ByteReadBuff.newInstance(dataItem.getData(), true).getFloat64();
    }

    /**
     * 剩余时间<br>
     * 发送[29]：03 00 00 1D 02 F0 80 32 01 00 00 00 00 00 0C 00 00 04 01 12 08 82 41 01 2A 00 01 7F 01<br>
     * 接收[33]：03 00 00 21 02 F0 80 32 03 00 00 00 00 00 02 00 0C 00 00 04 01 FF 09 00 08 00 00 00 00 00 00 00 00
     *
     * @return 数据
     */
    public double readRemainTime() {
        // 12 08 82 41 01 2A 00 01 7F 01
        RequestNckItem item = new RequestNckItem(ENckArea.C_CHANNEL, 1, 298, 1, ENckModule.S, 1);
        DataItem dataItem = this.readS7NckData(item);
        return ByteReadBuff.newInstance(dataItem.getData(), true).getFloat64();
    }

    /**
     * 程序名<br>
     * 发送[29]：03 00 00 1D 02 F0 80 32 01 00 00 00 00 00 0C 00 00 04 01 12 08 82 41 00 0C 00 01 7A 01<br>
     * 接收[185]：03 00 00 B9 02 F0 80 32 03 00 00 00 00 00 02 00 A4 00 00 04 01 FF 09 00 A0 2F 5F 4E 5F 4D 50 46 30 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00
     *
     * @return 数据
     */
    public String readProgramName() {
        // 12 08 82 41 00 0C 00 01 7A 01
        RequestNckItem item = new RequestNckItem(ENckArea.C_CHANNEL, 1, 12, 1, ENckModule.SPARPP, 1);
        DataItem dataItem = this.readS7NckData(item);
        return ByteReadBuff.newInstance(dataItem.getData(), true).getString(dataItem.getCount()).trim();
    }

    /**
     * 报警数量<br>
     * 发送[29]：03 00 00 1D 02 F0 80 32 01 00 00 00 00 00 0C 00 00 04 01 12 08 82 01 00 07 00 01 7F 01<br>
     * 接收[27]：03 00 00 1B 02 F0 80 32 03 00 00 00 00 00 02 00 06 00 00 04 01 FF 09 00 02 05 00
     *
     * @return 数据
     */
    public int readAlarmNumber() {
        // 12 08 82 01 00 07 00 01 7F 01
        RequestNckItem item = new RequestNckItem(ENckArea.N_NCK, 1, 7, 1, ENckModule.S, 1);
        DataItem dataItem = this.readS7NckData(item);
        return ByteReadBuff.newInstance(dataItem.getData(), true).getUInt16();
    }

    /**
     * 报警信息<br>
     * 发送[29]：03 00 00 1D 02 F0 80 32 01 00 00 00 00 00 0C 00 00 04 01 12 08 82 01 00 07 00 01 7F 01<br>
     * 接收[27]：03 00 00 1B 02 F0 80 32 03 00 00 00 00 00 02 00 06 00 00 04 01 FF 09 00 02 05 00
     *
     * @return 数据
     */
    public long readAlarmInfo() {
        // 12 08 82 01 00 01 00 01 77 01
        // 12 08 82 01 00 04 00 01 77 01
        RequestNckItem item = new RequestNckItem(ENckArea.N_NCK, 1, 1, 1, ENckModule.SALA, 1);
        DataItem dataItem = this.readS7NckData(item);
        return ByteReadBuff.newInstance(dataItem.getData(), true).getUInt32();
    }

    //endregion
}

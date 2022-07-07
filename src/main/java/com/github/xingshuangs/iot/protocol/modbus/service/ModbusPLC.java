package com.github.xingshuangs.iot.protocol.modbus.service;


import com.github.xingshuangs.iot.protocol.modbus.model.*;
import com.github.xingshuangs.iot.utils.BooleanUtil;
import com.github.xingshuangs.iot.utils.ShortUtil;

import java.util.List;

/**
 * modbus 1个寄存器占2个字节
 *
 * @author xingshuang
 */
public class ModbusPLC extends ModbusNetwork {

    public static final int PORT = 502;

    public static final String IP = "127.0.0.1";

    public ModbusPLC() {
        this(0, IP, PORT);
    }

    public ModbusPLC(int unitId) {
        this(unitId, IP, PORT);
    }

    public ModbusPLC(int unitId, String ip) {
        this(unitId, ip, PORT);
    }

    public ModbusPLC(int unitId, String ip, int port) {
        super(unitId, ip, port);
    }


    /**
     * 读取线圈， modbus 1个寄存器占2个字节
     *
     * @param address  地址
     * @param quantity 字节个数
     * @return 字节数组
     */
    public List<Boolean> readCoil(int address, int quantity) {
        if (address < 0) {
            throw new IllegalArgumentException("address<0");
        }
        if (quantity < 1 || quantity > 200) {
            throw new IllegalArgumentException("quantity<1||quantity>200");
        }
        MbReadCoilRequest reqPdu = new MbReadCoilRequest(address, quantity);
        MbReadCoilResponse resPdu = (MbReadCoilResponse) this.readModbusData(reqPdu);
        return BooleanUtil.byteArrayToList(quantity, resPdu.getCoilStatus());
    }

    /**
     * 读取线圈， modbus 1个寄存器占2个字节
     *
     * @param address    地址
     * @param coilStatus 线圈状态列表
     */
    public void writeCoil(int address, boolean coilStatus) {
        if (address < 0) {
            throw new IllegalArgumentException("address<0");
        }

        MbWriteSingleCoilRequest reqPdu = new MbWriteSingleCoilRequest(address, coilStatus);
        this.readModbusData(reqPdu);
    }

    /**
     * 读取线圈， modbus 1个寄存器占2个字节
     *
     * @param address    地址
     * @param coilStatus 线圈状态列表
     */
    public void writeCoil(int address, List<Boolean> coilStatus) {
        if (address < 0) {
            throw new IllegalArgumentException("address<0");
        }
        if (coilStatus.isEmpty()) {
            throw new IllegalArgumentException("coilStatus为空或null");
        }
        byte[] values = BooleanUtil.listToByteArray(coilStatus);
        MbWriteMultipleCoilRequest reqPdu = new MbWriteMultipleCoilRequest(address, coilStatus.size(), values);
        this.readModbusData(reqPdu);
    }

    /**
     * 读取线圈， modbus 1个寄存器占2个字节
     *
     * @param address  地址
     * @param quantity 字节个数
     * @return 字节数组
     */
    public List<Boolean> readDiscreteInput(int address, int quantity) {
        if (address < 0) {
            throw new IllegalArgumentException("address<0");
        }
        if (quantity < 1 || quantity > 200) {
            throw new IllegalArgumentException("quantity<1||quantity>200");
        }
        MbReadDiscreteInputRequest reqPdu = new MbReadDiscreteInputRequest(address, quantity);
        MbReadDiscreteInputResponse resPdu = (MbReadDiscreteInputResponse) this.readModbusData(reqPdu);
        return BooleanUtil.byteArrayToList(quantity, resPdu.getInputStatus());
    }


    /**
     * 读取保持寄存器， modbus 1个寄存器占2个字节
     *
     * @param address  地址
     * @param quantity 字节个数
     * @return 字节数组
     */
    public byte[] readHoldRegister(int address, int quantity) {
        if (address < 0) {
            throw new IllegalArgumentException("address<0");
        }
        if (quantity <= 0 || quantity > 125) {
            throw new IllegalArgumentException("quantity<=0||quantity>125");
        }
        MbReadHoldRegisterRequest reqPdu = new MbReadHoldRegisterRequest(address, quantity);
        MbReadHoldRegisterResponse resPdu = (MbReadHoldRegisterResponse) this.readModbusData(reqPdu);
        return resPdu.getRegister();
    }

    /**
     * 读取保持寄存器， modbus 1个寄存器占2个字节
     *
     * @param address 地址
     * @param value   数值，占2个字节
     */
    public void writeHoldRegister(int address, int value) {
        if (address < 0) {
            throw new IllegalArgumentException("address<0");
        }
        if (value < 0 || value > 65535) {
            throw new IllegalArgumentException("value<0||value>65535");
        }
        MbWriteSingleRegisterRequest reqPdu = new MbWriteSingleRegisterRequest(address, value);
        this.readModbusData(reqPdu);
    }

    /**
     * 读取保持寄存器， modbus 1个寄存器占2个字节
     *
     * @param address 地址
     * @param values  数据列表
     * @return 字节数组
     */
    public void writeHoldRegister(int address, List<Integer> values) {
        if (address < 0) {
            throw new IllegalArgumentException("address<0");
        }
        byte[] data = new byte[values.size() * 2];
        for (int i = 0; i < values.size(); i++) {
            byte[] bytes = ShortUtil.toByteArray(values.get(i));
            data[i * 2] = bytes[0];
            data[i * 2 + 1] = bytes[1];
        }
        MbWriteMultipleRegisterRequest reqPdu = new MbWriteMultipleRegisterRequest(address, values.size(), data);
        this.readModbusData(reqPdu);
    }

    /**
     * 读取输入寄存器， modbus 1个寄存器占2个字节
     *
     * @param address  地址
     * @param quantity 字节个数
     * @return 字节数组
     */
    public byte[] readInputRegister(int address, int quantity) {
        if (address < 0) {
            throw new IllegalArgumentException("address<0");
        }
        if (quantity <= 0 || quantity > 125) {
            throw new IllegalArgumentException("quantity<=0||quantity>125");
        }
        MbReadInputRegisterRequest reqPdu = new MbReadInputRegisterRequest(address, quantity);
        MbReadInputRegisterResponse resPdu = (MbReadInputRegisterResponse) this.readModbusData(reqPdu);
        return resPdu.getRegister();
    }

    /**
     * 读取一个Int16 2字节数据
     *
     * @param address 地址
     * @return 一个Int16 2字节数据
     */
    public short readInt16(int address) {
        byte[] res = this.readHoldRegister(address, 1);
        return ShortUtil.toInt16(res);
    }

    /**
     * 读取一个UInt16 2字节数据
     *
     * @param address 地址
     * @return 一个UInt16 2字节数据
     */
    public int readUInt16(int address) {
        byte[] res = this.readHoldRegister(address, 1);
        return ShortUtil.toUInt16(res);
    }


//    /**
//     * 读取一个UInt32 4字节数据
//     *
//     * @param address 地址
//     * @return 一个UInt32 4字节数据
//     */
//    public long readUInt32(int address) {
//        DataItem dataItem = this.readS7Data(AddressUtil.parseByte(address, 4));
//        return IntegerUtil.toUInt32(dataItem.getData());
//    }
//
//    /**
//     * 读取一个Float32的数据
//     *
//     * @param address 地址
//     * @return 一个Float32的数据
//     */
//    public float readFloat32(String address) {
//        DataItem dataItem = this.readS7Data(AddressUtil.parseByte(address, 4));
//        return FloatUtil.toFloat32(dataItem.getData());
//    }


//    /**
//     * 读取一个Float32的数据
//     *
//     * @param address 地址
//     * @return 一个Float32的数据
//     */
//    public double readFloat64(String address) {
//        DataItem dataItem = this.readS7Data(AddressUtil.parseByte(address, 8));
//        return FloatUtil.toFloat64(dataItem.getData());
//    }

    /**
     * 读取字符串
     * String（字符串）数据类型存储一串单字节字符，
     * String提供了多大256个字节，前两个字节分别表示字节中最大的字符数和当前的字符数，定义字符串的最大长度可以减少它的占用存储空间
     *
     * @param address 地址
     * @return 字符串
     */
    public String readString(String address) {
        return "";
    }
}

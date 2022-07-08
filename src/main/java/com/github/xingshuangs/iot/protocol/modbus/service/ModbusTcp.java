package com.github.xingshuangs.iot.protocol.modbus.service;


import com.github.xingshuangs.iot.protocol.modbus.model.*;
import com.github.xingshuangs.iot.utils.*;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * modbus 1个寄存器占2个字节
 *
 * @author xingshuang
 */
public class ModbusTcp extends ModbusNetwork {

    public static final int PORT = 502;

    public static final String IP = "127.0.0.1";

    public ModbusTcp() {
        this(0, IP, PORT);
    }

    public ModbusTcp(int unitId) {
        this(unitId, IP, PORT);
    }

    public ModbusTcp(int unitId, String ip) {
        this(unitId, ip, PORT);
    }

    public ModbusTcp(int unitId, String ip, int port) {
        super(unitId, ip, port);
    }

    //region 线圈和寄存器的读取

    /**
     * 读取线圈， modbus 1个寄存器占2个字节
     *
     * @param address  地址
     * @param quantity 线圈数量
     * @return boolean列表
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
     * 单写线圈， modbus 1个寄存器占2个字节
     *
     * @param address    地址
     * @param coilStatus 线圈状态
     */
    public void writeCoil(int address, boolean coilStatus) {
        if (address < 0) {
            throw new IllegalArgumentException("address<0");
        }

        MbWriteSingleCoilRequest reqPdu = new MbWriteSingleCoilRequest(address, coilStatus);
        this.readModbusData(reqPdu);
    }

    /**
     * 多写线圈， modbus 1个寄存器占2个字节
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
     * 读取离散输入， modbus 1个寄存器占2个字节
     *
     * @param address  地址
     * @param quantity 线圈数量
     * @return boolean列表
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
     * @param quantity 寄存器数量
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
     * @param values  数据值列表
     */
    public void writeHoldRegister(int address, byte[] values) {
        if (address < 0) {
            throw new IllegalArgumentException("address<0");
        }
        if (values.length % 2 != 0) {
            throw new IllegalArgumentException("values长度必须是偶数");
        }
        MbWriteMultipleRegisterRequest reqPdu = new MbWriteMultipleRegisterRequest(address, values.length / 2, values);
        this.readModbusData(reqPdu);
    }

    /**
     * 读取保持寄存器， modbus 1个寄存器占2个字节
     *
     * @param address 地址
     * @param values  数据值列表
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
     * @param quantity 寄存器数量
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
    //endregion

    //region 通用保持寄存器 读取数据

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

    /**
     * 读取一个UInt32 4字节数据
     *
     * @param address 地址
     * @return 一个UInt32 4字节数据
     */
    public int readInt32(int address) {
        byte[] res = this.readHoldRegister(address, 2);
        return IntegerUtil.toInt32(res);
    }

    /**
     * 读取一个UInt32 4字节数据
     *
     * @param address 地址
     * @return 一个UInt32 4字节数据
     */
    public long readUInt32(int address) {
        byte[] res = this.readHoldRegister(address, 2);
        return IntegerUtil.toUInt32(res);
    }

    /**
     * 读取一个Float32的数据
     *
     * @param address 地址
     * @return 一个Float32的数据
     */
    public float readFloat32(int address) {
        byte[] res = this.readHoldRegister(address, 2);
        return FloatUtil.toFloat32(res);
    }

    /**
     * 读取一个Float32的数据
     *
     * @param address 地址
     * @return 一个Float32的数据
     */
    public double readFloat64(int address) {
        byte[] res = this.readHoldRegister(address, 4);
        return FloatUtil.toFloat64(res);
    }

    /**
     * 读取字符串
     * String（字符串）数据类型存储一串单字节字符
     *
     * @param address 地址
     * @param length  字符串长度
     * @return 字符串
     */
    public String readString(int address, int length) {
        byte[] res = this.readHoldRegister(address, length / 2);
        return ByteUtil.toStr(res);
    }
    //endregion

    //region 通用保持寄存器 写入数据

    /**
     * 写入一个Int16 2字节数据
     *
     * @param address 地址
     * @param data    数据
     */
    public void writeInt16(int address, short data) {
        byte[] bytes = ShortUtil.toByteArray(data);
        this.writeHoldRegister(address, bytes);
    }

    /**
     * 写入一个UInt16 2字节数据
     *
     * @param address 地址
     * @param data    数据
     */
    public void writeUInt16(int address, int data) {
        byte[] bytes = ShortUtil.toByteArray(data);
        this.writeHoldRegister(address, bytes);
    }

    /**
     * 写入一个Int32 4字节数据
     *
     * @param address 地址
     * @param data    数据
     */
    public void writeInt32(int address, int data) {
        byte[] bytes = IntegerUtil.toByteArray(data);
        this.writeHoldRegister(address, bytes);
    }

    /**
     * 写入一个UInt32 4字节数据
     *
     * @param address 地址
     * @param data    数据
     */
    public void writeUInt32(int address, long data) {
        byte[] bytes = IntegerUtil.toByteArray(data);
        this.writeHoldRegister(address, bytes);
    }

    /**
     * 写入一个Float32 4字节数据
     *
     * @param address 地址
     * @param data    数据
     */
    public void writeFloat32(int address, float data) {
        byte[] bytes = FloatUtil.toByteArray(data);
        this.writeHoldRegister(address, bytes);
    }

    /**
     * 写入一个Float64 8字节数据
     *
     * @param address 地址
     * @param data    数据
     */
    public void writeFloat64(int address, double data) {
        byte[] bytes = FloatUtil.toByteArray(data);
        this.writeHoldRegister(address, bytes);
    }

    /**
     * 写入一个String数据
     *
     * @param address 地址
     * @param data    数据字符串
     */
    public void writeString(int address, String data) {
        byte[] bytes = data.getBytes(StandardCharsets.US_ASCII);
        this.writeHoldRegister(address, bytes);
    }
    //endregion
}

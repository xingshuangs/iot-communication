package com.github.xingshuangs.iot.protocol.modbus.service;


import com.github.xingshuangs.iot.exceptions.ModbusCommException;
import com.github.xingshuangs.iot.protocol.common.buff.EByteBuffFormat;
import com.github.xingshuangs.iot.protocol.modbus.model.MbErrorResponse;
import com.github.xingshuangs.iot.protocol.modbus.model.MbPdu;
import com.github.xingshuangs.iot.protocol.modbus.model.MbRtuRequest;
import com.github.xingshuangs.iot.protocol.modbus.model.MbRtuResponse;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * modbus 1个寄存器占2个字节
 *
 * @author xingshuang
 */
@Slf4j
public class ModbusRtuOverTcp extends ModbusNetwork<MbRtuRequest, MbRtuResponse> {

    public ModbusRtuOverTcp() {
        this(1, IP, PORT);
    }

    public ModbusRtuOverTcp(String ip) {
        this(1, ip, PORT);
    }

    public ModbusRtuOverTcp(String ip, int port) {
        this(1, ip, port);
    }

    public ModbusRtuOverTcp(int unitId) {
        this(unitId, IP, PORT);
    }

    public ModbusRtuOverTcp(int unitId, String ip) {
        this(unitId, ip, PORT);
    }

    public ModbusRtuOverTcp(int unitId, String ip, int port) {
        super(unitId, ip, port);
        this.tag = "ModbusRtu";
    }

    @Override
    protected MbRtuResponse readFromServer(MbRtuRequest req) {
        byte[] reqBytes = req.toByteArray();
        if (this.comCallback != null) {
            this.comCallback.accept(reqBytes);
        }
        int len;
        byte[] data = new byte[1024];
        synchronized (this.objLock) {
            this.write(reqBytes);
            len = this.read(data);
        }
        if (len <= 0) {
            throw new ModbusCommException(" Modbus数据读取长度有误");
        }
        byte[] total = new byte[len];
        System.arraycopy(data, 0, total, 0, len);
        if (this.comCallback != null) {
            this.comCallback.accept(total);
        }
        MbRtuResponse ack = MbRtuResponse.fromBytes(total);
        this.checkResult(req, ack);
        return ack;
    }

    @Override
    protected void checkResult(MbRtuRequest req, MbRtuResponse ack) {
        if (!ack.checkCrc()) {
            throw new ModbusCommException("响应数据CRC校验失败");
        }
        if (ack.getPdu() == null) {
            throw new ModbusCommException("PDU数据为null");
        }
        if (ack.getPdu().getFunctionCode().getCode() == (req.getPdu().getFunctionCode().getCode() | (byte) 0x80)) {
            MbErrorResponse response = (MbErrorResponse) ack.getPdu();
            throw new ModbusCommException("响应返回异常，异常码:" + response.getErrorCode().getDescription());
        }
        if (ack.getPdu().getFunctionCode().getCode() != req.getPdu().getFunctionCode().getCode()) {
            MbErrorResponse response = (MbErrorResponse) ack.getPdu();
            throw new ModbusCommException("返回功能码和发送功能码不一致，异常码:" + response.getErrorCode().getDescription());
        }
    }

    @Override
    protected MbPdu readModbusData(int unitId, MbPdu reqPdu) {
        MbRtuRequest request = new MbRtuRequest(unitId, reqPdu);
        try {
            MbRtuResponse response = this.readFromServer(request);
            return response.getPdu();
        } finally {
            if (!this.persistence) {
                log.debug("由于短连接方式，通信完毕触发关闭连接通道，服务端IP[{}]", this.socketAddress);
                this.close();
            }
        }
    }

    //region 线圈和寄存器的读取

    /**
     * 读取线圈， modbus 1个寄存器占2个字节
     *
     * @param unitId   从站编号
     * @param address  地址
     * @param quantity 线圈数量
     * @return boolean列表
     */
    public List<Boolean> readCoil(int unitId, int address, int quantity) {
        this.unitId = unitId;
        return this.readCoil(address, quantity);
    }

    /**
     * 写单线圈， modbus 1个寄存器占2个字节
     *
     * @param unitId     从站编号
     * @param address    地址
     * @param coilStatus 线圈状态
     */
    public void writeCoil(int unitId, int address, boolean coilStatus) {
        this.unitId = unitId;
        this.writeCoil(address, coilStatus);
    }

    /**
     * 写多线圈， modbus 1个寄存器占2个字节
     *
     * @param unitId     从站编号
     * @param address    地址
     * @param coilStatus 线圈状态列表
     */
    public void writeCoil(int unitId, int address, List<Boolean> coilStatus) {
        this.unitId = unitId;
        this.writeCoil(address, coilStatus);
    }

    /**
     * 读取离散输入， modbus 1个寄存器占2个字节
     *
     * @param unitId   从站编号
     * @param address  地址
     * @param quantity 线圈数量
     * @return boolean列表
     */
    public List<Boolean> readDiscreteInput(int unitId, int address, int quantity) {
        this.unitId = unitId;
        return this.readDiscreteInput(address, quantity);
    }

    /**
     * 读取保持寄存器， modbus 1个寄存器占2个字节
     *
     * @param unitId   从站编号
     * @param address  地址
     * @param quantity 寄存器数量
     * @return 字节数组
     */
    public byte[] readHoldRegister(int unitId, int address, int quantity) {
        this.unitId = unitId;
        return this.readHoldRegister(address, quantity);
    }

    /**
     * 以数值形式写入保持寄存器， modbus 1个寄存器占2个字节
     *
     * @param unitId  从站编号
     * @param address 地址
     * @param value   数值，占2个字节
     */
    public void writeHoldRegister(int unitId, int address, int value) {
        this.unitId = unitId;
        this.writeHoldRegister(address, value);
    }

    /**
     * 以字节数组形式写入保持寄存器， modbus 1个寄存器占2个字节
     *
     * @param unitId  从站编号
     * @param address 地址
     * @param values  数据值列表
     */
    public void writeHoldRegister(int unitId, int address, byte[] values) {
        this.unitId = unitId;
        this.writeHoldRegister(address, values);
    }

    /**
     * 以数值数组形式写入保持寄存器， modbus 1个寄存器占2个字节
     *
     * @param unitId  从站编号
     * @param address 地址
     * @param values  数据值列表
     */
    public void writeHoldRegister(int unitId, int address, List<Integer> values) {
        this.unitId = unitId;
        this.writeHoldRegister(address, values);
    }

    /**
     * 读取输入寄存器， modbus 1个寄存器占2个字节
     *
     * @param unitId   从站编号
     * @param address  地址
     * @param quantity 寄存器数量
     * @return 字节数组
     */
    public byte[] readInputRegister(int unitId, int address, int quantity) {
        this.unitId = unitId;
        return this.readInputRegister(address, quantity);
    }
    //endregion

    //region 通用保持寄存器 读取数据

    /**
     * 读取一个boolean类型数据，只有一个地址的数据，位索引[0,15]
     *
     * @param unitId   从站编号
     * @param address  地址
     * @param bitIndex 位索引[0,15]
     * @return true, false
     */
    public boolean readBoolean(int unitId, int address, int bitIndex) {
        this.unitId = unitId;
        return this.readBoolean(address, bitIndex);
    }

    /**
     * 读取一个Int16 2字节数据，默认大端模式
     *
     * @param unitId  从站编号
     * @param address 地址
     * @return 一个Int16 2字节数据
     */
    public short readInt16(int unitId, int address) {
        return this.readInt16(unitId, address, false);
    }

    /**
     * 读取一个Int16 2字节数据
     *
     * @param unitId       从站编号
     * @param address      地址
     * @param littleEndian 是否小端模式，true：小端，false：大端
     * @return 一个Int16 2字节数据
     */
    public short readInt16(int unitId, int address, boolean littleEndian) {
        this.unitId = unitId;
        return this.readInt16(address, littleEndian);
    }

    /**
     * 读取一个UInt16 2字节数据，默认大端模式
     *
     * @param unitId  从站编号
     * @param address 地址
     * @return 一个UInt16 2字节数据
     */
    public int readUInt16(int unitId, int address) {
        return this.readUInt16(unitId, address, false);
    }

    /**
     * 读取一个UInt16 2字节数据
     *
     * @param unitId       从站编号
     * @param address      地址
     * @param littleEndian 是否小端模式，true：小端，false：大端
     * @return 一个UInt16 2字节数据
     */
    public int readUInt16(int unitId, int address, boolean littleEndian) {
        this.unitId = unitId;
        return this.readUInt16(address, littleEndian);
    }

    /**
     * 读取一个Int32 4字节数据，默认BA_DC格式
     *
     * @param unitId  从站编号
     * @param address 地址
     * @return 一个Int32 4字节数据
     */
    public int readInt32(int unitId, int address) {
        return this.readInt32(unitId, address, EByteBuffFormat.BA_DC);
    }

    /**
     * 读取一个Int32 4字节数据
     *
     * @param unitId  从站编号
     * @param address 地址
     * @param format  4字节数据转换格式
     * @return 一个Int32 4字节数据
     */
    public int readInt32(int unitId, int address, EByteBuffFormat format) {
        this.unitId = unitId;
        return this.readInt32(address, format);
    }

    /**
     * 读取一个UInt32 4字节数据，默认BA_DC格式
     *
     * @param unitId  从站编号
     * @param address 地址
     * @return 一个UInt32 4字节数据
     */
    public long readUInt32(int unitId, int address) {
        return this.readUInt32(unitId, address, EByteBuffFormat.BA_DC);
    }

    /**
     * 读取一个UInt32 4字节数据
     *
     * @param unitId  从站编号
     * @param address 地址
     * @param format  4字节数据转换格式
     * @return 一个UInt32 4字节数据
     */
    public long readUInt32(int unitId, int address, EByteBuffFormat format) {
        this.unitId = unitId;
        return this.readUInt32(address, format);
    }

    /**
     * 读取一个Float32的数据，默认BA_DC格式
     *
     * @param unitId  从站编号
     * @param address 地址
     * @return 一个Float32的数据
     */
    public float readFloat32(int unitId, int address) {
        return this.readFloat32(unitId, address, EByteBuffFormat.BA_DC);
    }

    /**
     * 读取一个Float32的数据
     *
     * @param unitId  从站编号
     * @param address 地址
     * @param format  4字节数据转换格式
     * @return 一个Float32的数据
     */
    public float readFloat32(int unitId, int address, EByteBuffFormat format) {
        this.unitId = unitId;
        return this.readFloat32(address, format);
    }

    /**
     * 读取一个Float64的数据，默认BA_DC格式
     *
     * @param unitId  从站编号
     * @param address 地址
     * @return 一个Float64的数据
     */
    public double readFloat64(int unitId, int address) {
        return this.readFloat64(unitId, address, EByteBuffFormat.BA_DC);
    }

    /**
     * 读取一个Float64的数据
     *
     * @param unitId  从站编号
     * @param address 地址
     * @param format  8字节数据转换格式
     * @return 一个Float64的数据
     */
    public double readFloat64(int unitId, int address, EByteBuffFormat format) {
        this.unitId = unitId;
        return this.readFloat64(address, format);
    }

    /**
     * 读取字符串，默认ASCII编码
     * String（字符串）数据类型存储一串单字节字符
     *
     * @param unitId  从站编号
     * @param address 地址
     * @param length  字符串长度
     * @return 字符串
     */
    public String readString(int unitId, int address, int length) {
        return this.readString(unitId, address, length, StandardCharsets.US_ASCII);
    }

    /**
     * 读取字符串
     * String（字符串）数据类型存储一串单字节字符
     *
     * @param unitId  从站编号
     * @param address 地址
     * @param length  字符串长度
     * @param charset 字符编码
     * @return 字符串
     */
    public String readString(int unitId, int address, int length, Charset charset) {
        this.unitId = unitId;
        return this.readString(address, length, charset);
    }
    //endregion

    //region 通用保持寄存器 写入数据

    /**
     * 写入一个Int16 2字节数据，默认大端模式
     *
     * @param unitId  从站编号
     * @param address 地址
     * @param data    数据
     */
    public void writeInt16(int unitId, int address, short data) {
        this.writeInt16(unitId, address, data, false);
    }

    /**
     * 写入一个Int16 2字节数据
     *
     * @param unitId       从站编号
     * @param address      地址
     * @param data         数据
     * @param littleEndian 是否小端模式，true：小端，false：大端
     */
    public void writeInt16(int unitId, int address, short data, boolean littleEndian) {
        this.unitId = unitId;
        this.writeInt16(address, data, littleEndian);
    }

    /**
     * 写入一个UInt16 2字节数据，默认大端模式
     *
     * @param unitId  从站编号
     * @param address 地址
     * @param data    数据
     */
    public void writeUInt16(int unitId, int address, int data) {
        this.writeUInt16(unitId, address, data, false);
    }

    /**
     * 写入一个UInt16 2字节数据，默认大端模式
     *
     * @param unitId       从站编号
     * @param address      地址
     * @param data         数据
     * @param littleEndian 是否小端模式，true：小端，false：大端
     */
    public void writeUInt16(int unitId, int address, int data, boolean littleEndian) {
        this.unitId = unitId;
        this.writeUInt16(address, data, littleEndian);
    }

    /**
     * 写入一个Int32 4字节数据，默认BA_DC格式
     *
     * @param unitId  从站编号
     * @param address 地址
     * @param data    数据
     */
    public void writeInt32(int unitId, int address, int data) {
        this.writeInt32(unitId, address, data, EByteBuffFormat.BA_DC);
    }

    /**
     * 写入一个Int32 4字节数据，默认BA_DC格式
     *
     * @param unitId  从站编号
     * @param address 地址
     * @param data    数据
     * @param format  4字节数据转换格式
     */
    public void writeInt32(int unitId, int address, int data, EByteBuffFormat format) {
        this.unitId = unitId;
        this.writeInt32(address, data, format);
    }

    /**
     * 写入一个UInt32 4字节数据，默认BA_DC格式
     *
     * @param unitId  从站编号
     * @param address 地址
     * @param data    数据
     */
    public void writeUInt32(int unitId, int address, long data) {
        this.writeUInt32(unitId, address, data, EByteBuffFormat.BA_DC);
    }

    /**
     * 写入一个UInt32 4字节数据，默认BA_DC格式
     *
     * @param unitId  从站编号
     * @param address 地址
     * @param data    数据
     * @param format  4字节数据转换格式
     */
    public void writeUInt32(int unitId, int address, long data, EByteBuffFormat format) {
        this.unitId = unitId;
        this.writeUInt32(address, data, format);
    }

    /**
     * 写入一个Float32 4字节数据
     *
     * @param unitId  从站编号
     * @param address 地址
     * @param data    数据
     */
    public void writeFloat32(int unitId, int address, float data) {
        this.writeFloat32(unitId, address, data, EByteBuffFormat.BA_DC);
    }

    /**
     * 写入一个Float32 4字节数据
     *
     * @param unitId  从站编号
     * @param address 地址
     * @param data    数据
     * @param format  4字节数据转换格式
     */
    public void writeFloat32(int unitId, int address, float data, EByteBuffFormat format) {
        this.unitId = unitId;
        this.writeFloat32(address, data, format);
    }

    /**
     * 写入一个Float64 8字节数据，默认BA_DC格式
     *
     * @param unitId  从站编号
     * @param address 地址
     * @param data    数据
     */
    public void writeFloat64(int unitId, int address, double data) {
        this.writeFloat64(unitId, address, data, EByteBuffFormat.BA_DC);
    }

    /**
     * 写入一个Float64 8字节数据
     *
     * @param unitId  从站编号
     * @param address 地址
     * @param data    数据
     * @param format  8字节数据转换格式
     */
    public void writeFloat64(int unitId, int address, double data, EByteBuffFormat format) {
        this.unitId = unitId;
        this.writeFloat64(address, data, format);
    }

    /**
     * 写入一个String数据，默认ASCII编码
     *
     * @param unitId  从站编号
     * @param address 地址
     * @param data    数据字符串
     */
    public void writeString(int unitId, int address, String data) {
        this.writeString(unitId, address, data, StandardCharsets.US_ASCII);
    }

    /**
     * 写入一个String数据
     *
     * @param unitId  从站编号
     * @param address 地址
     * @param data    数据字符串
     * @param charset 字符集
     */
    public void writeString(int unitId, int address, String data, Charset charset) {
        this.unitId = unitId;
        this.writeString(address, data, charset);
    }
    //endregion

}

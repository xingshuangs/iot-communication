/*
 * MIT License
 *
 * Copyright (c) 2021-2099 Oscura (xingshuang) <xingshuang_cool@163.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.xingshuangs.iot.protocol.modbus.service;


import com.github.xingshuangs.iot.common.algorithm.LoopGroupAlg;
import com.github.xingshuangs.iot.common.buff.ByteReadBuff;
import com.github.xingshuangs.iot.common.buff.ByteWriteBuff;
import com.github.xingshuangs.iot.common.buff.EByteBuffFormat;
import com.github.xingshuangs.iot.net.client.TcpClientBasic;
import com.github.xingshuangs.iot.protocol.modbus.model.*;
import com.github.xingshuangs.iot.utils.BooleanUtil;
import com.github.xingshuangs.iot.utils.ByteUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

/**
 * Modbus communication basic skeleton, abstract class.
 * (Modbus网络通信的基础结构，抽象类)
 *
 * @author xingshuang
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Slf4j
public abstract class ModbusSkeletonAbstract<T, R> extends TcpClientBasic {

    /**
     * Unit id, slave id also.
     * (unit id or slave id)
     */
    protected int unitId = 1;

    /**
     * Lock.
     * (锁)
     */
    protected final Object objLock = new Object();

    /**
     * Communication callback, first parameter is tag, second is package content.
     * (通信回调，第一个参数是tag标签，指示该报文含义；第二个参数是具体报文内容)
     */
    protected BiConsumer<String, byte[]> comCallback;

    /**
     * Persistence, true: long connection, false: short connection.
     * (是否持久化，默认是持久化，对应长连接，true：长连接，false：短连接)
     */
    protected boolean persistence = true;

    public ModbusSkeletonAbstract() {
        super();
    }

    public ModbusSkeletonAbstract(int unitId, String host, int port) {
        super(host, port);
        this.unitId = unitId;
    }

    @Override
    public void connect() {
        try {
            super.connect();
        } finally {
            if (!this.persistence) {
                this.close();
            }
        }
    }

    //region 底层数据通信部分

    /**
     * Read data from server.
     * (从服务器读取数据)
     *
     * @param req modbus request
     * @return modbus response.
     */
    protected abstract R readFromServer(T req);

    /**
     * Check result by req and ack.
     * (校验请求数据和响应数据)
     *
     * @param req req data.
     * @param ack ack data.
     */
    protected abstract void checkResult(T req, R ack);


    /**
     * Read modbus data.
     * (读取modbus数据)
     *
     * @param unitId unit id or slave id
     * @param reqPdu request pdu.
     * @return ack result
     */
    protected abstract MbPdu readModbusData(int unitId, MbPdu reqPdu);

    //endregion

    //region 线圈和寄存器的读取

    /**
     * Read coil.
     * (读取线圈)
     *
     * @param address  modbus address
     * @param quantity coil quantity
     * @return boolean list
     */
    public List<Boolean> readCoil(int address, int quantity) {
        return this.readCoil(this.unitId, address, quantity);
    }

    /**
     * Read coil.
     * (读取线圈)
     *
     * @param unitId   unit id or slave id
     * @param address  modbus address
     * @param quantity coil quantity
     * @return boolean list
     */
    public List<Boolean> readCoil(int unitId, int address, int quantity) {
        if (address < 0 || address > 65535) {
            throw new IllegalArgumentException("address < 0 || address > 65535");
        }
        if (quantity < 1) {
            throw new IllegalArgumentException("quantity<1");
        }

        List<Boolean> res = new ArrayList<>();
        LoopGroupAlg.loopExecute(quantity, 2000, (off, len) -> {
            MbReadCoilRequest reqPdu = new MbReadCoilRequest(address + off, len);
            MbReadCoilResponse resPdu = (MbReadCoilResponse) this.readModbusData(unitId, reqPdu);
            List<Boolean> booleans = BooleanUtil.byteArrayToList(len, resPdu.getCoilStatus());
            res.addAll(booleans);
        });
        return res;
    }

    /**
     * Write coil.
     * (写单线圈)
     *
     * @param address    modbus address
     * @param coilStatus coil status
     */
    public void writeCoil(int address, boolean coilStatus) {
        this.writeCoil(this.unitId, address, coilStatus);
    }

    /**
     * Write single coil.
     * (写单线圈)
     *
     * @param unitId     unit id or slave id
     * @param address    modbus address
     * @param coilStatus coil status
     */
    public void writeCoil(int unitId, int address, boolean coilStatus) {
        if (address < 0 || address > 65535) {
            throw new IllegalArgumentException("address < 0 || address > 65535");
        }

        MbWriteSingleCoilRequest reqPdu = new MbWriteSingleCoilRequest(address, coilStatus);
        this.readModbusData(unitId, reqPdu);
    }

    /**
     * Write multiple coils.
     * (写多线圈)
     *
     * @param address    modbus address
     * @param coilStatus coil status list
     */
    public void writeCoil(int address, List<Boolean> coilStatus) {
        this.writeCoil(this.unitId, address, coilStatus);
    }

    /**
     * Write multiple coil.
     * (写多线圈)
     *
     * @param unitId     unit id or slave id
     * @param address    modbus address
     * @param coilStatus coil status list
     */
    public void writeCoil(int unitId, int address, List<Boolean> coilStatus) {
        if (address < 0 || address > 65535) {
            throw new IllegalArgumentException("address < 0 || address > 65535");
        }
        if (coilStatus.isEmpty()) {
            throw new IllegalArgumentException("coilStatus list is empty");
        }

        LoopGroupAlg.loopExecute(coilStatus.size(), 1968, (off, len) -> {
            List<Boolean> booleanList = coilStatus.subList(off, off + len);
            byte[] values = BooleanUtil.listToByteArray(booleanList);
            MbWriteMultipleCoilRequest reqPdu = new MbWriteMultipleCoilRequest(address + off, len, values);
            this.readModbusData(unitId, reqPdu);
        });
    }

    /**
     * Read discrete input.
     * (读取离散输入)
     *
     * @param address  modbus address
     * @param quantity quantity
     * @return boolean list
     */
    public List<Boolean> readDiscreteInput(int address, int quantity) {
        return this.readDiscreteInput(this.unitId, address, quantity);
    }

    /**
     * Read discrete input,
     * (读取离散输入)
     *
     * @param unitId   unit id or slave id
     * @param address  modbus address
     * @param quantity quantity
     * @return boolean list
     */
    public List<Boolean> readDiscreteInput(int unitId, int address, int quantity) {
        if (address < 0 || address > 65535) {
            throw new IllegalArgumentException("address < 0 || address > 65535");
        }
        if (quantity < 1) {
            throw new IllegalArgumentException("quantity<1");
        }

        List<Boolean> res = new ArrayList<>();
        LoopGroupAlg.loopExecute(quantity, 2000, (off, len) -> {
            MbReadDiscreteInputRequest reqPdu = new MbReadDiscreteInputRequest(address + off, len);
            MbReadDiscreteInputResponse resPdu = (MbReadDiscreteInputResponse) this.readModbusData(unitId, reqPdu);
            List<Boolean> booleans = BooleanUtil.byteArrayToList(len, resPdu.getInputStatus());
            res.addAll(booleans);
        });
        return res;
    }

    /**
     * Read multiple hold register.
     * (读取保持寄存器， modbus 1个寄存器占2个字节)
     *
     * @param address  modbus address
     * @param quantity quantity of register
     * @return byte array
     */
    public byte[] readHoldRegister(int address, int quantity) {
        return this.readHoldRegister(this.unitId, address, quantity);
    }

    /**
     * Read multiple hold register.
     * (读取保持寄存器， modbus 1个寄存器占2个字节)
     *
     * @param unitId   unit id or slave id
     * @param address  modbus address
     * @param quantity quantity of register
     * @return byte array
     */
    public byte[] readHoldRegister(int unitId, int address, int quantity) {
        if (address < 0 || address > 65535) {
            throw new IllegalArgumentException("address < 0 || address > 65535");
        }
        if (quantity < 1) {
            throw new IllegalArgumentException("quantity < 1");
        }

        // TODO: 实际在slave中测试，没有125的约束，暂时先这么写着
        ByteWriteBuff buff = ByteWriteBuff.newInstance(quantity * 2);
        LoopGroupAlg.loopExecute(quantity, 125, (off, len) -> {
            MbReadHoldRegisterRequest reqPdu = new MbReadHoldRegisterRequest(address + off, len);
            MbReadHoldRegisterResponse resPdu = (MbReadHoldRegisterResponse) this.readModbusData(unitId, reqPdu);
            buff.putBytes(resPdu.getRegister());
        });
        return buff.getData();

//        MbReadHoldRegisterRequest reqPdu = new MbReadHoldRegisterRequest(address, quantity);
//        MbReadHoldRegisterResponse resPdu = (MbReadHoldRegisterResponse) this.readModbusData(unitId, reqPdu);
//        return resPdu.getRegister();
    }


    /**
     * Write single hold register.
     * (以数值形式写入单个保持寄存器， modbus 1个寄存器占2个字节)
     *
     * @param address modbus address
     * @param value   value, 2 bytes
     */
    public void writeHoldRegister(int address, int value) {
        this.writeHoldRegister(this.unitId, address, value);
    }

    /**
     * Write single hold register.
     * (以数值形式写入单个保持寄存器， modbus 1个寄存器占2个字节)
     *
     * @param unitId  unit id or slave id
     * @param address modbus address
     * @param value   value, 2 bytes
     */
    public void writeHoldRegister(int unitId, int address, int value) {
        if (address < 0 || address > 65535) {
            throw new IllegalArgumentException("address < 0 || address > 65535");
        }
        if (value < 0 || value > 65535) {
            throw new IllegalArgumentException("value < 0 || value > 65535");
        }

        MbWriteSingleRegisterRequest reqPdu = new MbWriteSingleRegisterRequest(address, value);
        this.readModbusData(unitId, reqPdu);
    }

    /**
     * Write multiple hold register.
     * (以byte array形式写入保持寄存器， modbus 1个寄存器占2个字节)
     *
     * @param address modbus address
     * @param values  value list
     */
    public void writeHoldRegister(int address, byte[] values) {
        this.writeHoldRegister(this.unitId, address, values);
    }

    /**
     * Write multiple hold register.
     * (以byte array形式写入保持寄存器， modbus 1个寄存器占2个字节)
     *
     * @param unitId  unit id or slave id
     * @param address modbus address
     * @param values  value list
     */
    public void writeHoldRegister(int unitId, int address, byte[] values) {
        if (address < 0 || address > 65535) {
            throw new IllegalArgumentException("address < 0 || address > 65535");
        }
        if (values.length % 2 != 0) {
            throw new IllegalArgumentException("values must have an even length");
        }

        ByteReadBuff buff = ByteReadBuff.newInstance(values);
        LoopGroupAlg.loopExecute(values.length / 2, 123, (off, len) -> {
            byte[] bytes = buff.getBytes(off * 2, len * 2);
            MbWriteMultipleRegisterRequest reqPdu = new MbWriteMultipleRegisterRequest(address + off, len, bytes);
            this.readModbusData(unitId, reqPdu);
        });
    }

    /**
     * Write multiple hold register.
     * (以数值数组形式写入多个保持寄存器， modbus 1个寄存器占2个字节)
     *
     * @param address modbus address
     * @param values  value list
     */
    public void writeHoldRegister(int address, List<Integer> values) {
        this.writeHoldRegister(this.unitId, address, values);
    }

    /**
     * Write multiple hold register.
     * (以数值数组形式写入多个保持寄存器， modbus 1个寄存器占2个字节)
     *
     * @param unitId  unit id or slave id
     * @param address modbus address
     * @param values  value list
     */
    public void writeHoldRegister(int unitId, int address, List<Integer> values) {
        if (values.isEmpty()) {
            throw new IllegalArgumentException("values is empty");
        }

        ByteWriteBuff buff = ByteWriteBuff.newInstance(values.size() * 2);
        values.forEach(buff::putShort);
        this.writeHoldRegister(unitId, address, buff.getData());
    }

    /**
     * Write multiple input register.
     * (读取输入寄存器， modbus 1个寄存器占2个字节)
     *
     * @param address  modbus address
     * @param quantity register quantity
     * @return byte array
     */
    public byte[] readInputRegister(int address, int quantity) {
        return this.readInputRegister(this.unitId, address, quantity);
    }

    /**
     * Write multiple input register.
     * (读取输入寄存器， modbus 1个寄存器占2个字节)
     *
     * @param unitId   unit id or slave id
     * @param address  modbus address
     * @param quantity register quantity
     * @return byte array
     */
    public byte[] readInputRegister(int unitId, int address, int quantity) {
        if (address < 0 || address > 65535) {
            throw new IllegalArgumentException("address < 0 || address > 65535");
        }
        if (quantity < 1) {
            throw new IllegalArgumentException("quantity < 1");
        }

        ByteWriteBuff buff = ByteWriteBuff.newInstance(quantity * 2);
        LoopGroupAlg.loopExecute(quantity, 125, (off, len) -> {
            MbReadInputRegisterRequest reqPdu = new MbReadInputRegisterRequest(address + off, len);
            MbReadInputRegisterResponse resPdu = (MbReadInputRegisterResponse) this.readModbusData(unitId, reqPdu);
            buff.putBytes(resPdu.getRegister());
        });
        return buff.getData();
    }
    //endregion

    //region 通用保持寄存器 读取数据

    /**
     * Read boolean.
     * (读取一个boolean类型数据，只有一个address的数据，位索引[0,15])
     *
     * @param address  modbus address
     * @param bitIndex bit index [0,15]
     * @return true, false
     */
    public boolean readBoolean(int address, int bitIndex) {
        return this.readBoolean(this.unitId, address, bitIndex);
    }

    /**
     * Read boolean from hold register.
     * (读取一个boolean类型数据，只有一个address的数据，位索引[0,15])
     *
     * @param unitId   unit id or slave id
     * @param address  modbus address
     * @param bitIndex bit index[0,15]
     * @return true, false
     */
    public boolean readBoolean(int unitId, int address, int bitIndex) {
        if (bitIndex < 0 || bitIndex > 15) {
            throw new IllegalArgumentException("bitIndex < 0 || bitIndex > 15");
        }

        byte[] res = this.readHoldRegister(unitId, address, 1);
        int byteOffset = bitIndex / 8;
        int bitOffset = bitIndex % 8;
        return ByteReadBuff.newInstance(res).getBoolean(byteOffset, bitOffset);
    }

    /**
     * Read Int16 from hold register.
     * (读取一个Int16 2字节数据，默认大端模式)
     *
     * @param address modbus address
     * @return int16 data, 2 bytes
     */
    public short readInt16(int address) {
        return this.readInt16(this.unitId, address, false);
    }

    /**
     * Read Int16 from hold register.
     * (读取一个Int16 2字节数据)
     *
     * @param address      address
     * @param littleEndian is little endian, true: yes, false: no.
     * @return int16 data, 2 bytes
     */
    public short readInt16(int address, boolean littleEndian) {
        return this.readInt16(this.unitId, address, littleEndian);
    }

    /**
     * Read Int16 from hold register.
     * (读取一个Int16 2字节数据)
     *
     * @param unitId  unit id or slave id
     * @param address modbus address
     * @return int16 data, 2 bytes
     */
    public short readInt16(int unitId, int address) {
        return this.readInt16(unitId, address, false);
    }

    /**
     * Read Int16 from hold register.
     * (读取一个Int16 2字节数据)
     *
     * @param unitId       unit id or slave id
     * @param address      modbus address
     * @param littleEndian is little endian, true: yes, false: no.
     * @return int16 data, 2 bytes
     */
    public short readInt16(int unitId, int address, boolean littleEndian) {
        byte[] res = this.readHoldRegister(unitId, address, 1);
        return ByteReadBuff.newInstance(res, littleEndian).getInt16();
    }

    /**
     * Read UInt16 from hold register.
     * (读取一个UInt16 2字节数据，默认大端模式)
     *
     * @param address modbus address
     * @return UInt16 data, 2 bytes
     */
    public int readUInt16(int address) {
        return this.readUInt16(this.unitId, address, false);
    }

    /**
     * Read UInt16 from hold register.
     * (读取一个UInt16 2字节数据)
     *
     * @param address      address
     * @param littleEndian is little endian, true: yes, false: no.
     * @return UInt16 data, 2 bytes
     */
    public int readUInt16(int address, boolean littleEndian) {
        return this.readUInt16(this.unitId, address, littleEndian);
    }

    /**
     * Read UInt16 from hold register.
     * (读取一个UInt16 2字节数据)
     *
     * @param unitId  unit id or slave id
     * @param address modbus address
     * @return UInt16 data, 2 bytes
     */
    public int readUInt16(int unitId, int address) {
        return this.readUInt16(unitId, address, false);
    }

    /**
     * Read UInt16 from hold register.
     * (读取一个UInt16 2字节数据)
     *
     * @param unitId       unit id or slave id
     * @param address      modbus address
     * @param littleEndian is little endian, true: yes, false: no.
     * @return UInt16 data, 2 bytes
     */
    public int readUInt16(int unitId, int address, boolean littleEndian) {
        byte[] res = this.readHoldRegister(unitId, address, 1);
        return ByteReadBuff.newInstance(res, littleEndian).getUInt16();
    }

    /**
     * Read Int32 from hold register. BA_DC default.
     * (读取一个Int32 4字节数据，默认BA_DC格式)
     *
     * @param address modbus address
     * @return Int32 data, 4 bytes
     */
    public int readInt32(int address) {
        return this.readInt32(this.unitId, address, EByteBuffFormat.BA_DC);
    }

    /**
     * Read Int32 from hold register.
     * (读取一个Int32 4字节数据)
     *
     * @param address modbus address
     * @param format  format of 4 bytes
     * @return Int32 data, 4 bytes
     */
    public int readInt32(int address, EByteBuffFormat format) {
        return this.readInt32(this.unitId, address, format);
    }

    /**
     * Read Int32 from hold register.
     * (读取一个Int32 4字节数据)
     *
     * @param unitId  unit id or slave id
     * @param address modbus address
     * @return Int32 data, 4 bytes
     */
    public int readInt32(int unitId, int address) {
        return this.readInt32(unitId, address, EByteBuffFormat.BA_DC);
    }

    /**
     * Read Int32 from hold register.
     * (读取一个Int32 4字节数据)
     *
     * @param unitId  unit id or slave id
     * @param address modbus address
     * @param format  format of 4 bytes
     * @return Int32 data, 4 bytes
     */
    public int readInt32(int unitId, int address, EByteBuffFormat format) {
        byte[] res = this.readHoldRegister(unitId, address, 2);
        return ByteReadBuff.newInstance(res, format).getInt32();
    }

    /**
     * Read UInt32 from hold register. BA_DC default.
     * (读取一个UInt32 4字节数据，默认BA_DC格式)
     *
     * @param address modbus address
     * @return UInt32 data, 4 bytes
     */
    public long readUInt32(int address) {
        return this.readUInt32(this.unitId, address, EByteBuffFormat.BA_DC);
    }

    /**
     * Read UInt32 from hold register.
     * (读取一个UInt32 4字节数据)
     *
     * @param address modbus address
     * @param format  format of 4 bytes
     * @return UInt32 data, 4 bytes
     */
    public long readUInt32(int address, EByteBuffFormat format) {
        return this.readUInt32(this.unitId, address, format);
    }

    /**
     * Read UInt32 from hold register.
     * (读取一个UInt32 4字节数据)
     *
     * @param unitId  unit id or slave id
     * @param address modbus address
     * @return UInt32 data, 4 bytes
     */
    public long readUInt32(int unitId, int address) {
        return this.readUInt32(unitId, address, EByteBuffFormat.BA_DC);
    }

    /**
     * Read UInt32 from hold register.
     * (读取一个UInt32 4字节数据)
     *
     * @param unitId  unit id or slave id
     * @param address modbus address
     * @param format  format of 4 bytes
     * @return UInt32 data, 4 bytes
     */
    public long readUInt32(int unitId, int address, EByteBuffFormat format) {
        byte[] res = this.readHoldRegister(unitId, address, 2);
        return ByteReadBuff.newInstance(res, format).getUInt32();
    }

    /**
     * Read Float32 from hold register. BA_DC default.
     * (读取一个Float32的数据，默认BA_DC格式)
     *
     * @param address modbus address
     * @return Float32 data, 4 bytes
     */
    public float readFloat32(int address) {
        return this.readFloat32(this.unitId, address, EByteBuffFormat.BA_DC);
    }

    /**
     * Read Float32 from hold register.
     * (读取一个Float32的数据)
     *
     * @param address modbus address
     * @param format  format of 4 bytes
     * @return Float32 data, 4 bytes
     */
    public float readFloat32(int address, EByteBuffFormat format) {
        return this.readFloat32(this.unitId, address, format);
    }

    /**
     * Read Float32 from hold register.
     * (读取一个Float32的数据)
     *
     * @param unitId  unit id or slave id
     * @param address modbus address
     * @return Float32 data, 4 bytes
     */
    public float readFloat32(int unitId, int address) {
        return this.readFloat32(unitId, address, EByteBuffFormat.BA_DC);
    }

    /**
     * Read Float32 from hold register.
     * (读取一个Float32的数据)
     *
     * @param unitId  unit id or slave id
     * @param address modbus address
     * @param format  format of 4 bytes
     * @return Float32 data, 4 bytes
     */
    public float readFloat32(int unitId, int address, EByteBuffFormat format) {
        byte[] res = this.readHoldRegister(unitId, address, 2);
        return ByteReadBuff.newInstance(res, format).getFloat32();
    }

    /**
     * Read Float64 from hold register.
     * (读取一个Float64的数据)
     *
     * @param address modbus address
     * @return Float64 data, 8 bytes
     */
    public double readFloat64(int address) {
        return this.readFloat64(this.unitId, address, EByteBuffFormat.BA_DC);
    }

    /**
     * Read Float64 from hold register.
     * (读取一个Float64的数据)
     *
     * @param address modbus address
     * @param format  format of 8 bytes
     * @return Float64 data, 8 bytes
     */
    public double readFloat64(int address, EByteBuffFormat format) {
        return this.readFloat64(this.unitId, address, format);
    }

    /**
     * Read Float64 from hold register.
     * (读取一个Float64的数据)
     *
     * @param unitId  unit id or slave id
     * @param address modbus address
     * @return Float64 data, 8 bytes
     */
    public double readFloat64(int unitId, int address) {
        return this.readFloat64(unitId, address, EByteBuffFormat.BA_DC);
    }

    /**
     * Read Float64 from hold register.
     * (读取一个Float64的数据)
     *
     * @param unitId  unit id or slave id
     * @param address modbus address
     * @param format  format of 8 bytes
     * @return Float64 data, 8 bytes
     */
    public double readFloat64(int unitId, int address, EByteBuffFormat format) {
        byte[] res = this.readHoldRegister(unitId, address, 4);
        return ByteReadBuff.newInstance(res, format).getFloat64();
    }

    /**
     * Read string from hold register. ASCII default.
     * (String（字符串）数据类型存储一串单字节字符)
     *
     * @param address modbus address
     * @param length  string length
     * @return string
     */
    public String readString(int address, int length) {
        return this.readString(this.unitId, address, length, StandardCharsets.US_ASCII);
    }

    /**
     * Read string from hold register.
     * (String（字符串）数据类型存储一串单字节字符)
     *
     * @param address modbus address
     * @param length  string length
     * @param charset charset
     * @return string
     */
    public String readString(int address, int length, Charset charset) {
        return this.readString(this.unitId, address, length, charset);
    }

    /**
     * Read string from hold register.
     * (String（字符串）数据类型存储一串单字节字符)
     *
     * @param unitId  unit id or slave id
     * @param address modbus address
     * @param length  string length
     * @return string
     */
    public String readString(int unitId, int address, int length) {
        return this.readString(unitId, address, length, StandardCharsets.US_ASCII);
    }

    /**
     * Read string from hold register.
     * (String（字符串）数据类型存储一串单字节字符)
     *
     * @param unitId  unit id or slave id
     * @param address modbus address
     * @param length  string length
     * @param charset charset
     * @return string
     */
    public String readString(int unitId, int address, int length, Charset charset) {
        byte[] res = this.readHoldRegister(unitId, address, length / 2);
        return ByteUtil.toStr(res, 0, res.length, charset);
    }
    //endregion

    //region 通用保持寄存器 写入数据

    /**
     * Write Int16 to hold register. Big endian default.
     * (写入一个Int16 2字节数据)
     *
     * @param address modbus address
     * @param data    data
     */
    public void writeInt16(int address, short data) {
        this.writeInt16(this.unitId, address, data, false);
    }

    /**
     * Write Int16 to hold register.
     * (写入一个Int16 2字节数据)
     *
     * @param address      modbus address
     * @param data         data
     * @param littleEndian is little endian，true：Yes，false：No
     */
    public void writeInt16(int address, short data, boolean littleEndian) {
        this.writeInt16(this.unitId, address, data, littleEndian);
    }

    /**
     * Write Int16 to hold register.
     * (写入一个Int16 2字节数据)
     *
     * @param unitId  unit id or slave id
     * @param address modbus address
     * @param data    data
     */
    public void writeInt16(int unitId, int address, short data) {
        this.writeInt16(unitId, address, data, false);
    }

    /**
     * Write Int16 to hold register.
     * (写入一个Int16 2字节数据)
     *
     * @param unitId       unit id or slave id
     * @param address      modbus address
     * @param data         data
     * @param littleEndian is little endian，true：Yes，false：No
     */
    public void writeInt16(int unitId, int address, short data, boolean littleEndian) {
        byte[] bytes = ByteWriteBuff.newInstance(2, littleEndian)
                .putShort(data)
                .getData();
        this.writeHoldRegister(unitId, address, bytes);
    }

    /**
     * Write UInt16 to hold register. Big endian default.
     * (写入一个UInt16 2字节数据，默认大端模式)
     *
     * @param address modbus address
     * @param data    data
     */
    public void writeUInt16(int address, int data) {
        this.writeUInt16(this.unitId, address, data, false);
    }

    /**
     * Write UInt16 to hold register. Big endian default.
     * (写入一个UInt16 2字节数据，默认大端模式)
     *
     * @param address      modbus address
     * @param data         data
     * @param littleEndian is little endian, true: yes, false: no.
     */
    public void writeUInt16(int address, int data, boolean littleEndian) {
        this.writeUInt16(this.unitId, address, data, littleEndian);
    }

    /**
     * Write UInt16 to hold register. Big endian default.
     * (写入一个UInt16 2字节数据，默认大端模式)
     *
     * @param unitId  unit id or slave id
     * @param address modbus address
     * @param data    data
     */
    public void writeUInt16(int unitId, int address, int data) {
        this.writeUInt16(unitId, address, data, false);
    }

    /**
     * Write UInt16 to hold register. Big endian default.
     * (写入一个UInt16 2字节数据，默认大端模式)
     *
     * @param unitId       unit id or slave id
     * @param address      address
     * @param data         data
     * @param littleEndian is little endian, true: yes, false: no.
     */
    public void writeUInt16(int unitId, int address, int data, boolean littleEndian) {
        byte[] bytes = ByteWriteBuff.newInstance(2, littleEndian)
                .putShort(data)
                .getData();
        this.writeHoldRegister(unitId, address, bytes);
    }

    /**
     * Write Int32 to hold register. BA_DC format default.
     * (写入一个Int32 4字节数据，默认BA_DC格式)
     *
     * @param address modbus address
     * @param data    data
     */
    public void writeInt32(int address, int data) {
        this.writeInt32(this.unitId, address, data, EByteBuffFormat.BA_DC);
    }

    /**
     * Write Int32 to hold register. BA_DC format default.
     * (写入一个Int32 4字节数据，默认BA_DC格式)
     *
     * @param address modbus address
     * @param data    data
     * @param format  format of 4 bytes
     */
    public void writeInt32(int address, int data, EByteBuffFormat format) {
        this.writeInt32(this.unitId, address, data, format);
    }

    /**
     * Write Int32 to hold register. BA_DC format default.
     * (写入一个Int32 4字节数据，默认BA_DC格式)
     *
     * @param unitId  unit id or slave id
     * @param address modbus address
     * @param data    data
     */
    public void writeInt32(int unitId, int address, int data) {
        this.writeInt32(unitId, address, data, EByteBuffFormat.BA_DC);
    }

    /**
     * Write Int32 to hold register. BA_DC format default.
     * (写入一个Int32 4字节数据，默认BA_DC格式)
     *
     * @param unitId  unit id or slave id
     * @param address modbus address
     * @param data    data
     * @param format  format of 4 bytes
     */
    public void writeInt32(int unitId, int address, int data, EByteBuffFormat format) {
        byte[] bytes = ByteWriteBuff.newInstance(4, format)
                .putInteger(data)
                .getData();
        this.writeHoldRegister(unitId, address, bytes);
    }

    /**
     * Write UInt32 to hold register. BA_DC format default.
     * (写入一个UInt32 4字节数据，默认BA_DC格式)
     *
     * @param address modbus address
     * @param data    data
     */
    public void writeUInt32(int address, long data) {
        this.writeUInt32(this.unitId, address, data, EByteBuffFormat.BA_DC);
    }

    /**
     * Write UInt32 to hold register. BA_DC format default.
     * (写入一个UInt32 4字节数据，默认BA_DC格式)
     *
     * @param address modbus address
     * @param data    data
     * @param format  format of 4 bytes
     */
    public void writeUInt32(int address, long data, EByteBuffFormat format) {
        this.writeUInt32(this.unitId, address, data, format);
    }

    /**
     * Write UInt32 to hold register. BA_DC format default.
     * (写入一个UInt32 4字节数据，默认BA_DC格式)
     *
     * @param unitId  unit id or slave id
     * @param address modbus address
     * @param data    data
     */
    public void writeUInt32(int unitId, int address, long data) {
        this.writeUInt32(unitId, address, data, EByteBuffFormat.BA_DC);
    }

    /**
     * Write UInt32 to hold register. BA_DC format default.
     * (写入一个UInt32 4字节数据，默认BA_DC格式)
     *
     * @param unitId  unit id or slave id
     * @param address modbus address
     * @param data    data
     * @param format  format of 4 bytes
     */
    public void writeUInt32(int unitId, int address, long data, EByteBuffFormat format) {
        byte[] bytes = ByteWriteBuff.newInstance(4, format)
                .putInteger(data)
                .getData();
        this.writeHoldRegister(unitId, address, bytes);
    }

    /**
     * Write float32 to hold register.
     * (写入一个Float32 4字节数据)
     *
     * @param address modbus address
     * @param data    data
     */
    public void writeFloat32(int address, float data) {
        this.writeFloat32(this.unitId, address, data, EByteBuffFormat.BA_DC);
    }

    /**
     * Write float32 to hold register.
     * (写入一个Float32 4字节数据)
     *
     * @param address modbus address
     * @param data    data
     * @param format  format of 4 bytes
     */
    public void writeFloat32(int address, float data, EByteBuffFormat format) {
        this.writeFloat32(this.unitId, address, data, format);
    }

    /**
     * Write float32 to hold register.
     * (写入一个Float32 4字节数据)
     *
     * @param unitId  unit id or slave id
     * @param address modbus address
     * @param data    data
     */
    public void writeFloat32(int unitId, int address, float data) {
        this.writeFloat32(unitId, address, data, EByteBuffFormat.BA_DC);
    }

    /**
     * Write float32 to hold register.
     * (写入一个Float32 4字节数据)
     *
     * @param unitId  unit id or slave id
     * @param address modbus address
     * @param data    data
     * @param format  format of 4 bytes
     */
    public void writeFloat32(int unitId, int address, float data, EByteBuffFormat format) {
        byte[] bytes = ByteWriteBuff.newInstance(4, format)
                .putFloat(data)
                .getData();
        this.writeHoldRegister(unitId, address, bytes);
    }

    /**
     * Write float64 to hold register. BA_DC format.
     * (写入一个Float64 8字节数据，默认BA_DC格式)
     *
     * @param address modbus address
     * @param data    data
     */
    public void writeFloat64(int address, double data) {
        this.writeFloat64(this.unitId, address, data, EByteBuffFormat.BA_DC);
    }

    /**
     * Write float64 to hold register.
     * (写入一个Float64 8字节数据)
     *
     * @param address modbus address
     * @param data    data
     * @param format  format of 8 bytes
     */
    public void writeFloat64(int address, double data, EByteBuffFormat format) {
        this.writeFloat64(this.unitId, address, data, format);
    }

    /**
     * Write float64 to hold register.
     * (写入一个Float64 8字节数据)
     *
     * @param unitId  unit id or slave id
     * @param address modbus address
     * @param data    data
     */
    public void writeFloat64(int unitId, int address, double data) {
        this.writeFloat64(unitId, address, data, EByteBuffFormat.BA_DC);
    }

    /**
     * Write float64 to hold register.
     * (写入一个Float64 8字节数据)
     *
     * @param unitId  unit id or slave id
     * @param address modbus address
     * @param data    data
     * @param format  format of 8 bytes
     */
    public void writeFloat64(int unitId, int address, double data, EByteBuffFormat format) {
        byte[] bytes = ByteWriteBuff.newInstance(8, format)
                .putDouble(data)
                .getData();
        this.writeHoldRegister(unitId, address, bytes);
    }

    /**
     * Write string to hold register. ASCII default.
     * (写入一个String数据，默认ASCII编码)
     *
     * @param address modbus address
     * @param data    data string
     */
    public void writeString(int address, String data) {
        this.writeString(this.unitId, address, data, StandardCharsets.US_ASCII);
    }

    /**
     * Write string to hold register.
     * (写入一个String数据)
     *
     * @param address modbus address
     * @param data    data string
     * @param charset charset
     */
    public void writeString(int address, String data, Charset charset) {
        this.writeString(this.unitId, address, data, charset);
    }

    /**
     * Write string to hold register.
     * (写入一个String数据)
     *
     * @param unitId  unit id or slave id
     * @param address modbus address
     * @param data    data string
     */
    public void writeString(int unitId, int address, String data) {
        this.writeString(unitId, address, data, StandardCharsets.US_ASCII);
    }

    /**
     * Write string to hold register.
     * (写入一个String数据)
     *
     * @param unitId  unit id or slave id
     * @param address modbus address
     * @param data    data string
     * @param charset charset
     */
    public void writeString(int unitId, int address, String data, Charset charset) {
        byte[] bytes = data.getBytes(charset);
        this.writeHoldRegister(unitId, address, bytes);
    }
    //endregion
}

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
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

/**
 * Modbus网络通信的基础结构，抽象类
 *
 * @author xingshuang
 */
@Data
@Slf4j
public abstract class ModbusSkeletonAbstract<T, R> extends TcpClientBasic {

    /**
     * 从站编号
     */
    protected int unitId = 1;

    /**
     * 锁
     */
    protected final Object objLock = new Object();

    /**
     * 通信回调，第一个参数是tag标签，指示该报文含义；第二个参数是具体报文内容
     */
    protected BiConsumer<String, byte[]> comCallback;

    /**
     * 是否持久化，默认是持久化，对应长连接，true：长连接，false：短连接
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
     * 从服务器读取数据
     *
     * @param req modbus协议数据
     * @return modbus协议数据
     */
    protected abstract R readFromServer(T req);

    /**
     * 校验请求数据和响应数据
     *
     * @param req 请求数据
     * @param ack 响应数据
     */
    protected abstract void checkResult(T req, R ack);


    /**
     * 读取modbus数据
     *
     * @param unitId 从站编号
     * @param reqPdu 请求对象
     * @return 响应结果
     */
    protected abstract MbPdu readModbusData(int unitId, MbPdu reqPdu);

    //endregion

    //region 线圈和寄存器的读取

    /**
     * 读取线圈， modbus 1个寄存器占2个字节
     *
     * @param address  地址
     * @param quantity 线圈数量
     * @return boolean列表
     */
    public List<Boolean> readCoil(int address, int quantity) {
        return this.readCoil(this.unitId, address, quantity);
    }

    /**
     * 读取线圈， modbus 1个寄存器占2个字节
     *
     * @param unitId   从站编号
     * @param address  地址
     * @param quantity 线圈数量
     * @return boolean列表
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
     * 写单线圈， modbus 1个寄存器占2个字节
     *
     * @param address    地址
     * @param coilStatus 线圈状态
     */
    public void writeCoil(int address, boolean coilStatus) {
        this.writeCoil(this.unitId, address, coilStatus);
    }

    /**
     * 写单线圈， modbus 1个寄存器占2个字节
     *
     * @param unitId     从站编号
     * @param address    地址
     * @param coilStatus 线圈状态
     */
    public void writeCoil(int unitId, int address, boolean coilStatus) {
        if (address < 0 || address > 65535) {
            throw new IllegalArgumentException("address < 0 || address > 65535");
        }

        MbWriteSingleCoilRequest reqPdu = new MbWriteSingleCoilRequest(address, coilStatus);
        this.readModbusData(unitId, reqPdu);
    }

    /**
     * 写多线圈， modbus 1个寄存器占2个字节
     *
     * @param address    地址
     * @param coilStatus 线圈状态列表
     */
    public void writeCoil(int address, List<Boolean> coilStatus) {
        this.writeCoil(this.unitId, address, coilStatus);
    }

    /**
     * 写多线圈， modbus 1个寄存器占2个字节
     *
     * @param unitId     从站编号
     * @param address    地址
     * @param coilStatus 线圈状态列表
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
     * 读取离散输入， modbus 1个寄存器占2个字节
     *
     * @param address  地址
     * @param quantity 线圈数量
     * @return boolean列表
     */
    public List<Boolean> readDiscreteInput(int address, int quantity) {
        return this.readDiscreteInput(this.unitId, address, quantity);
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
     * 读取保持寄存器， modbus 1个寄存器占2个字节
     *
     * @param address  地址
     * @param quantity 寄存器数量
     * @return 字节数组
     */
    public byte[] readHoldRegister(int address, int quantity) {
        return this.readHoldRegister(this.unitId, address, quantity);
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
     * 以数值形式写入单个保持寄存器， modbus 1个寄存器占2个字节
     *
     * @param address 地址
     * @param value   数值，占2个字节
     */
    public void writeHoldRegister(int address, int value) {
        this.writeHoldRegister(this.unitId, address, value);
    }

    /**
     * 以数值形式写入单个保持寄存器， modbus 1个寄存器占2个字节
     *
     * @param unitId  从站编号
     * @param address 地址
     * @param value   数值，占2个字节
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
     * 以字节数组形式写入保持寄存器， modbus 1个寄存器占2个字节
     *
     * @param address 地址
     * @param values  数据值列表
     */
    public void writeHoldRegister(int address, byte[] values) {
        this.writeHoldRegister(this.unitId, address, values);
    }

    /**
     * 以字节数组形式写入保持寄存器， modbus 1个寄存器占2个字节
     *
     * @param unitId  从站编号
     * @param address 地址
     * @param values  数据值列表
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
     * 以数值数组形式写入多个保持寄存器， modbus 1个寄存器占2个字节
     *
     * @param address 地址
     * @param values  数据值列表
     */
    public void writeHoldRegister(int address, List<Integer> values) {
        this.writeHoldRegister(this.unitId, address, values);
    }

    /**
     * 以数值数组形式写入多个保持寄存器， modbus 1个寄存器占2个字节
     *
     * @param unitId  从站编号
     * @param address 地址
     * @param values  数据值列表
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
     * 读取输入寄存器， modbus 1个寄存器占2个字节
     *
     * @param address  地址
     * @param quantity 寄存器数量
     * @return 字节数组
     */
    public byte[] readInputRegister(int address, int quantity) {
        return this.readInputRegister(this.unitId, address, quantity);
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
     * 读取一个boolean类型数据，只有一个地址的数据，位索引[0,15]
     *
     * @param address  地址
     * @param bitIndex 位索引[0,15]
     * @return true, false
     */
    public boolean readBoolean(int address, int bitIndex) {
        return this.readBoolean(this.unitId, address, bitIndex);
    }

    /**
     * 读取一个boolean类型数据，只有一个地址的数据，位索引[0,15]
     *
     * @param unitId   从站编号
     * @param address  地址
     * @param bitIndex 位索引[0,15]
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
     * 读取一个Int16 2字节数据，默认大端模式
     *
     * @param address 地址
     * @return 一个Int16 2字节数据
     */
    public short readInt16(int address) {
        return this.readInt16(this.unitId, address, false);
    }

    /**
     * 读取一个Int16 2字节数据
     *
     * @param address      地址
     * @param littleEndian 是否小端模式，true：小端，false：大端
     * @return 一个Int16 2字节数据
     */
    public short readInt16(int address, boolean littleEndian) {
        return this.readInt16(this.unitId, address, littleEndian);
    }

    /**
     * 读取一个Int16 2字节数据
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
        byte[] res = this.readHoldRegister(unitId, address, 1);
        return ByteReadBuff.newInstance(res, littleEndian).getInt16();
    }

    /**
     * 读取一个UInt16 2字节数据，默认大端模式
     *
     * @param address 地址
     * @return 一个UInt16 2字节数据
     */
    public int readUInt16(int address) {
        return this.readUInt16(this.unitId, address, false);
    }

    /**
     * 读取一个UInt16 2字节数据
     *
     * @param address      地址
     * @param littleEndian 是否小端模式，true：小端，false：大端
     * @return 一个UInt16 2字节数据
     */
    public int readUInt16(int address, boolean littleEndian) {
        return this.readUInt16(this.unitId, address, littleEndian);
    }

    /**
     * 读取一个UInt16 2字节数据
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
        byte[] res = this.readHoldRegister(unitId, address, 1);
        return ByteReadBuff.newInstance(res, littleEndian).getUInt16();
    }

    /**
     * 读取一个Int32 4字节数据，默认BA_DC格式
     *
     * @param address 地址
     * @return 一个Int32 4字节数据
     */
    public int readInt32(int address) {
        return this.readInt32(this.unitId, address, EByteBuffFormat.BA_DC);
    }

    /**
     * 读取一个Int32 4字节数据
     *
     * @param address 地址
     * @param format  4字节数据转换格式
     * @return 一个Int32 4字节数据
     */
    public int readInt32(int address, EByteBuffFormat format) {
        return this.readInt32(this.unitId, address, format);
    }

    /**
     * 读取一个Int32 4字节数据
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
        byte[] res = this.readHoldRegister(unitId, address, 2);
        return ByteReadBuff.newInstance(res, format).getInt32();
    }

    /**
     * 读取一个UInt32 4字节数据，默认BA_DC格式
     *
     * @param address 地址
     * @return 一个UInt32 4字节数据
     */
    public long readUInt32(int address) {
        return this.readUInt32(this.unitId, address, EByteBuffFormat.BA_DC);
    }

    /**
     * 读取一个UInt32 4字节数据
     *
     * @param address 地址
     * @param format  4字节数据转换格式
     * @return 一个UInt32 4字节数据
     */
    public long readUInt32(int address, EByteBuffFormat format) {
        return this.readUInt32(this.unitId, address, format);
    }

    /**
     * 读取一个UInt32 4字节数据
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
        byte[] res = this.readHoldRegister(unitId, address, 2);
        return ByteReadBuff.newInstance(res, format).getUInt32();
    }

    /**
     * 读取一个Float32的数据，默认BA_DC格式
     *
     * @param address 地址
     * @return 一个Float32的数据
     */
    public float readFloat32(int address) {
        return this.readFloat32(this.unitId, address, EByteBuffFormat.BA_DC);
    }

    /**
     * 读取一个Float32的数据
     *
     * @param address 地址
     * @param format  4字节数据转换格式
     * @return 一个Float32的数据
     */
    public float readFloat32(int address, EByteBuffFormat format) {
        return this.readFloat32(this.unitId, address, format);
    }

    /**
     * 读取一个Float32的数据
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
        byte[] res = this.readHoldRegister(unitId, address, 2);
        return ByteReadBuff.newInstance(res, format).getFloat32();
    }

    /**
     * 读取一个Float64的数据，默认BA_DC格式
     *
     * @param address 地址
     * @return 一个Float64的数据
     */
    public double readFloat64(int address) {
        return this.readFloat64(this.unitId, address, EByteBuffFormat.BA_DC);
    }

    /**
     * 读取一个Float64的数据
     *
     * @param address 地址
     * @param format  8字节数据转换格式
     * @return 一个Float64的数据
     */
    public double readFloat64(int address, EByteBuffFormat format) {
        return this.readFloat64(this.unitId, address, format);
    }

    /**
     * 读取一个Float64的数据
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
        byte[] res = this.readHoldRegister(unitId, address, 4);
        return ByteReadBuff.newInstance(res, format).getFloat64();
    }

    /**
     * 读取字符串，默认ASCII编码
     * String（字符串）数据类型存储一串单字节字符
     *
     * @param address 地址
     * @param length  字符串长度
     * @return 字符串
     */
    public String readString(int address, int length) {
        return this.readString(this.unitId, address, length, StandardCharsets.US_ASCII);
    }

    /**
     * 读取字符串
     * String（字符串）数据类型存储一串单字节字符
     *
     * @param address 地址
     * @param length  字符串长度
     * @param charset 字符编码
     * @return 字符串
     */
    public String readString(int address, int length, Charset charset) {
        return this.readString(this.unitId, address, length, charset);
    }

    /**
     * 读取字符串
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
        byte[] res = this.readHoldRegister(unitId, address, length / 2);
        return ByteUtil.toStr(res, 0, res.length, charset);
    }
    //endregion

    //region 通用保持寄存器 写入数据

    /**
     * 写入一个Int16 2字节数据，默认大端模式
     *
     * @param address 地址
     * @param data    数据
     */
    public void writeInt16(int address, short data) {
        this.writeInt16(this.unitId, address, data, false);
    }

    /**
     * 写入一个Int16 2字节数据
     *
     * @param address      地址
     * @param data         数据
     * @param littleEndian 是否小端模式，true：小端，false：大端
     */
    public void writeInt16(int address, short data, boolean littleEndian) {
        this.writeInt16(this.unitId, address, data, littleEndian);
    }

    /**
     * 写入一个Int16 2字节数据
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
        byte[] bytes = ByteWriteBuff.newInstance(2, littleEndian)
                .putShort(data)
                .getData();
        this.writeHoldRegister(unitId, address, bytes);
    }

    /**
     * 写入一个UInt16 2字节数据，默认大端模式
     *
     * @param address 地址
     * @param data    数据
     */
    public void writeUInt16(int address, int data) {
        this.writeUInt16(this.unitId, address, data, false);
    }

    /**
     * 写入一个UInt16 2字节数据，默认大端模式
     *
     * @param address      地址
     * @param data         数据
     * @param littleEndian 是否小端模式，true：小端，false：大端
     */
    public void writeUInt16(int address, int data, boolean littleEndian) {
        this.writeUInt16(this.unitId, address, data, littleEndian);
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
        byte[] bytes = ByteWriteBuff.newInstance(2, littleEndian)
                .putShort(data)
                .getData();
        this.writeHoldRegister(unitId, address, bytes);
    }

    /**
     * 写入一个Int32 4字节数据，默认BA_DC格式
     *
     * @param address 地址
     * @param data    数据
     */
    public void writeInt32(int address, int data) {
        this.writeInt32(this.unitId, address, data, EByteBuffFormat.BA_DC);
    }

    /**
     * 写入一个Int32 4字节数据，默认BA_DC格式
     *
     * @param address 地址
     * @param data    数据
     * @param format  4字节数据转换格式
     */
    public void writeInt32(int address, int data, EByteBuffFormat format) {
        this.writeInt32(this.unitId, address, data, format);
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
        byte[] bytes = ByteWriteBuff.newInstance(4, format)
                .putInteger(data)
                .getData();
        this.writeHoldRegister(unitId, address, bytes);
    }

    /**
     * 写入一个UInt32 4字节数据，默认BA_DC格式
     *
     * @param address 地址
     * @param data    数据
     */
    public void writeUInt32(int address, long data) {
        this.writeUInt32(this.unitId, address, data, EByteBuffFormat.BA_DC);
    }

    /**
     * 写入一个UInt32 4字节数据，默认BA_DC格式
     *
     * @param address 地址
     * @param data    数据
     * @param format  4字节数据转换格式
     */
    public void writeUInt32(int address, long data, EByteBuffFormat format) {
        this.writeUInt32(this.unitId, address, data, format);
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
        byte[] bytes = ByteWriteBuff.newInstance(4, format)
                .putInteger(data)
                .getData();
        this.writeHoldRegister(unitId, address, bytes);
    }

    /**
     * 写入一个Float32 4字节数据
     *
     * @param address 地址
     * @param data    数据
     */
    public void writeFloat32(int address, float data) {
        this.writeFloat32(this.unitId, address, data, EByteBuffFormat.BA_DC);
    }

    /**
     * 写入一个Float32 4字节数据
     *
     * @param address 地址
     * @param data    数据
     * @param format  4字节数据转换格式
     */
    public void writeFloat32(int address, float data, EByteBuffFormat format) {
        this.writeFloat32(this.unitId, address, data, format);
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
        byte[] bytes = ByteWriteBuff.newInstance(4, format)
                .putFloat(data)
                .getData();
        this.writeHoldRegister(unitId, address, bytes);
    }

    /**
     * 写入一个Float64 8字节数据，默认BA_DC格式
     *
     * @param address 地址
     * @param data    数据
     */
    public void writeFloat64(int address, double data) {
        this.writeFloat64(this.unitId, address, data, EByteBuffFormat.BA_DC);
    }

    /**
     * 写入一个Float64 8字节数据
     *
     * @param address 地址
     * @param data    数据
     * @param format  8字节数据转换格式
     */
    public void writeFloat64(int address, double data, EByteBuffFormat format) {
        this.writeFloat64(this.unitId, address, data, format);
    }

    /**
     * 写入一个Float64 8字节数据
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
        byte[] bytes = ByteWriteBuff.newInstance(8, format)
                .putDouble(data)
                .getData();
        this.writeHoldRegister(unitId, address, bytes);
    }

    /**
     * 写入一个String数据，默认ASCII编码
     *
     * @param address 地址
     * @param data    数据字符串
     */
    public void writeString(int address, String data) {
        this.writeString(this.unitId, address, data, StandardCharsets.US_ASCII);
    }

    /**
     * 写入一个String数据
     *
     * @param address 地址
     * @param data    数据字符串
     * @param charset 字符集
     */
    public void writeString(int address, String data, Charset charset) {
        this.writeString(this.unitId, address, data, charset);
    }

    /**
     * 写入一个String数据
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
        byte[] bytes = data.getBytes(charset);
        this.writeHoldRegister(unitId, address, bytes);
    }
    //endregion
}

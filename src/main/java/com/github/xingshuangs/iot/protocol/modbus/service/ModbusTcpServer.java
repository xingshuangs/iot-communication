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


import com.github.xingshuangs.iot.common.buff.ByteReadBuff;
import com.github.xingshuangs.iot.common.buff.EByteBuffFormat;
import com.github.xingshuangs.iot.exceptions.SocketRuntimeException;
import com.github.xingshuangs.iot.net.SocketUtils;
import com.github.xingshuangs.iot.net.server.TcpServerBasic;
import com.github.xingshuangs.iot.protocol.modbus.enums.EMbExceptionCode;
import com.github.xingshuangs.iot.protocol.modbus.model.*;
import com.github.xingshuangs.iot.utils.BooleanUtil;
import com.github.xingshuangs.iot.utils.ShortUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * ModbusTcp服务端
 *
 * @author xingshuang
 */
@Data
@Slf4j
public class ModbusTcpServer extends TcpServerBasic {

    /**
     * 读写锁
     */
    private ReadWriteLock rwLock = new ReentrantReadWriteLock();

    /**
     * 线圈
     */
    private List<Boolean> coils = new ArrayList<>();

    /**
     * 离散量输入
     */
    private List<Boolean> discreteInputs = new ArrayList<>();


    /**
     * 输入寄存器
     */
    private byte[] inputRegisters;

    /**
     * 保持寄存器
     */
    private byte[] holdRegisters;

    /**
     * 编码方式
     */
    private EByteBuffFormat buffFormat = EByteBuffFormat.BA_DC;

    /**
     * 客户端当前的连接数量
     */
    private AtomicInteger connectedNumber = new AtomicInteger();

    /**
     * 客户端连接的最大允许数量
     */
    private Integer maxAvailableNumber;

    public ModbusTcpServer() {
        this(502, 2000);
    }

    public ModbusTcpServer(int port) {
        this(port, 2000);
    }

    public ModbusTcpServer(int port, int size) {
        this.port = port;
        for (int i = 0; i < size; i++) {
            this.coils.add(false);
            this.discreteInputs.add(false);
        }
        this.inputRegisters = new byte[size * 2];
        this.holdRegisters = new byte[size * 2];
        this.maxAvailableNumber = Runtime.getRuntime().availableProcessors();
    }

    /**
     * 校验客户端是否允许连入
     *
     * @param client 客户端
     * @return true:验证成功，false：验证失败
     */
    @Override
    protected boolean checkClientValid(Socket client) {
        return this.connectedNumber.get() < this.maxAvailableNumber;
    }

    /**
     * 客户端连入
     *
     * @param socket 客户端
     */
    @Override
    protected void clientConnected(Socket socket) {
        this.connectedNumber.getAndIncrement();
    }

    /**
     * 客户端断开
     *
     * @param socket 客户端
     */
    @Override
    protected void clientDisconnected(Socket socket) {
        this.connectedNumber.getAndDecrement();
    }

    @Override
    protected void doClientHandle(Socket socket) {
        MbTcpRequest request = this.readS7DataFromClient(socket);
        MbTcpResponse response;
        try {
            switch (request.getPdu().getFunctionCode()) {
                case READ_COIL:
                    response = this.readCoil(request);
                    break;
                case READ_DISCRETE_INPUT:
                    response = this.readDiscreteInput(request);
                    break;
                case READ_HOLD_REGISTER:
                    response = this.readHoldRegister(request);
                    break;
                case READ_INPUT_REGISTER:
                    response = this.readInputRegister(request);
                    break;
                case WRITE_SINGLE_COIL:
                    response = this.writeSingleCoil(request);
                    break;
                case WRITE_SINGLE_REGISTER:
                    response = this.writeSingleRegister(request);
                    break;
                case WRITE_MULTIPLE_COIL:
                    // TODO: 待修改
                    response = this.writeSingleRegister(request);
                    break;
                case WRITE_MULTIPLE_REGISTER:
                    // TODO: 待修改
                    response = this.writeSingleRegister(request);
                    break;
                default:
                    response = new MbTcpResponse(request.getHeader(), new MbErrorResponse(EMbExceptionCode.ILLEGAL_FUNCTION));
                    break;
            }
            this.write(socket, response.toByteArray());
        } catch (Exception e) {
            response = new MbTcpResponse(request.getHeader(), new MbErrorResponse(EMbExceptionCode.SLAVE_DEVICE_FAILURE));
            this.write(socket, response.toByteArray());
        }
    }

    /**
     * 读取S7协议的数据
     *
     * @param socket socket对象
     * @return S7Data
     */
    private MbTcpRequest readS7DataFromClient(Socket socket) {
        byte[] data = this.readClientData(socket);
        return MbTcpRequest.fromBytes(data);
    }

    /**
     * 重写读取客户端数据，针对粘包粘包的数据处理
     *
     * @param socket 客户端socket对象
     * @return 字节数据
     */
    @Override
    protected byte[] readClientData(Socket socket) {
        try {
            InputStream in = socket.getInputStream();
            int firstByte = in.read();
            if (firstByte == -1) {
                SocketUtils.close(socket);
                throw new SocketRuntimeException("The client is disconnected.");
            }

            byte[] data = new byte[MbapHeader.BYTE_LENGTH];
            data[0] = (byte) firstByte;
            this.read(socket, data, 1, MbapHeader.BYTE_LENGTH, 1024, 0, true);
            MbapHeader header = MbapHeader.fromBytes(data);
            byte[] total = new byte[data.length + header.getLength() - 1];
            System.arraycopy(data, 0, total, 0, data.length);
            this.read(socket, total, data.length, header.getLength() - 1, 1024, 0, true);
            return data;
        } catch (IOException e) {
            throw new SocketRuntimeException(e);
        }
    }

    private MbTcpResponse readCoil(MbTcpRequest request) {
        MbReadCoilRequest reqPdu = (MbReadCoilRequest) request.getPdu();
        List<Boolean> booleans = this.coils.subList(reqPdu.getAddress(), reqPdu.getAddress() + reqPdu.getQuantity());
        byte[] bytes = BooleanUtil.listToByteArray(booleans);
        MbReadCoilResponse ackPdu = new MbReadCoilResponse();
        ackPdu.setCount(bytes.length);
        ackPdu.setCoilStatus(bytes);
        return new MbTcpResponse(request.getHeader(), ackPdu);
    }

    private MbTcpResponse readDiscreteInput(MbTcpRequest request) {
        MbReadDiscreteInputRequest reqPdu = (MbReadDiscreteInputRequest) request.getPdu();
        List<Boolean> booleans = this.discreteInputs.subList(reqPdu.getAddress(), reqPdu.getAddress() + reqPdu.getQuantity());
        byte[] bytes = BooleanUtil.listToByteArray(booleans);
        MbReadDiscreteInputResponse ackPdu = new MbReadDiscreteInputResponse();
        ackPdu.setCount(bytes.length);
        ackPdu.setInputStatus(bytes);
        return new MbTcpResponse(request.getHeader(), ackPdu);
    }

    private MbTcpResponse readHoldRegister(MbTcpRequest request) {
        MbReadHoldRegisterRequest reqPdu = (MbReadHoldRegisterRequest) request.getPdu();
        ByteReadBuff buff = ByteReadBuff.newInstance(this.holdRegisters);
        byte[] bytes = buff.getBytes(reqPdu.getAddress() * 2, reqPdu.getQuantity() * 2);
        MbReadHoldRegisterResponse ackPdu = new MbReadHoldRegisterResponse();
        ackPdu.setCount(bytes.length);
        ackPdu.setRegister(bytes);
        return new MbTcpResponse(request.getHeader(), ackPdu);
    }

    private MbTcpResponse readInputRegister(MbTcpRequest request) {
        MbReadInputRegisterRequest reqPdu = (MbReadInputRegisterRequest) request.getPdu();
        ByteReadBuff buff = ByteReadBuff.newInstance(this.inputRegisters);
        byte[] bytes = buff.getBytes(reqPdu.getAddress() * 2, reqPdu.getQuantity() * 2);
        MbReadInputRegisterResponse ackPdu = new MbReadInputRegisterResponse();
        ackPdu.setCount(bytes.length);
        ackPdu.setRegister(bytes);
        return new MbTcpResponse(request.getHeader(), ackPdu);
    }

    private MbTcpResponse writeSingleCoil(MbTcpRequest request) {
        MbWriteSingleCoilRequest reqPdu = (MbWriteSingleCoilRequest) request.getPdu();
        this.coils.set(reqPdu.getAddress(), reqPdu.isValue());
        MbWriteSingleCoilResponse ackPdu = new MbWriteSingleCoilResponse();
        ackPdu.setAddress(reqPdu.getAddress());
        ackPdu.setValue(new byte[]{(byte) 0xFF, 0x00});
        return new MbTcpResponse(request.getHeader(), ackPdu);
    }

    private MbTcpResponse writeSingleRegister(MbTcpRequest request) {
        MbWriteSingleRegisterRequest reqPdu = (MbWriteSingleRegisterRequest) request.getPdu();
        byte[] bytes = ShortUtil.toByteArray(reqPdu.getValue());
        this.holdRegisters[reqPdu.getAddress() * 2] = bytes[0];
        this.holdRegisters[reqPdu.getAddress() * 2 + 1] = bytes[1];
        MbWriteSingleRegisterResponse ackPdu = new MbWriteSingleRegisterResponse();
        ackPdu.setAddress(reqPdu.getAddress());
        ackPdu.setValue(new byte[]{(byte) 0xFF, 0x00});
        return new MbTcpResponse(request.getHeader(), ackPdu);
    }
}

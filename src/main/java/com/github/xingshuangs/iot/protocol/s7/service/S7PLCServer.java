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

package com.github.xingshuangs.iot.protocol.s7.service;


import com.github.xingshuangs.iot.common.buff.ByteReadBuff;
import com.github.xingshuangs.iot.exceptions.SocketRuntimeException;
import com.github.xingshuangs.iot.net.SocketUtils;
import com.github.xingshuangs.iot.net.server.TcpServerBasic;
import com.github.xingshuangs.iot.protocol.s7.enums.*;
import com.github.xingshuangs.iot.protocol.s7.model.*;
import com.github.xingshuangs.iot.protocol.s7.utils.AddressUtil;
import com.github.xingshuangs.iot.utils.BooleanUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

/**
 * S7的PLC服务端
 *
 * @author xingshuang
 */
@Slf4j
public class S7PLCServer extends TcpServerBasic {

    /**
     * 数据Map操作锁
     */
    private final Object objLock = new Object();

    /**
     * 读写锁
     */
    private final ReadWriteLock rwLock = new ReentrantReadWriteLock();

    /**
     * 所有数据
     */
    protected final HashMap<String, byte[]> dataMap = new HashMap<>();

    public S7PLCServer() {
        this(102);
    }

    public S7PLCServer(int port) {
        this.port = port;
        this.dataMap.put("DB1", new byte[65536]);
        this.dataMap.put("M", new byte[65536]);
        this.dataMap.put("I", new byte[65536]);
        this.dataMap.put("Q", new byte[65536]);
        this.dataMap.put("T", new byte[65536]);
        this.dataMap.put("C", new byte[65536]);
    }

    /**
     * 获取目前可用的区域
     *
     * @return 数据区
     */
    public Set<String> getAvailableAreas() {
        synchronized (this.objLock) {
            return this.dataMap.keySet();
        }
    }

    /**
     * 添加所需的DB块
     *
     * @param dbNumbers db块编号
     */
    public void addDBArea(int... dbNumbers) {
        log.debug("服务端数据区添加DB{}", dbNumbers);
        synchronized (this.objLock) {
            for (int x : dbNumbers) {
                String name = String.format("DB%s", x);
                this.dataMap.computeIfAbsent(name, key -> new byte[65536]);
            }
        }
    }


    @Override
    protected boolean checkHandshake(Socket socket) {
        // 校验connect request
        S7Data s7Data = this.readS7DataFromClient(socket);
        if (!(s7Data.getCotp() instanceof COTPConnection)
                || s7Data.getCotp().getPduType() != EPduType.CONNECT_REQUEST) {
            log.error("客户端[{}]握手失败，不是连接请求", socket.getRemoteSocketAddress());
            return false;
        }
        S7Data connectConfirm = S7Data.createConnectConfirm(s7Data);
        this.write(socket, connectConfirm.toByteArray());

        // 校验setup
        s7Data = this.readS7DataFromClient(socket);
        if (!(s7Data.getCotp() instanceof COTPData)
                || s7Data.getCotp().getPduType() != EPduType.DT_DATA) {
            log.error("客户端[{}]握手失败，不是参数设置", socket.getRemoteSocketAddress());
            return false;
        }
        S7Data connectAckDtData = S7Data.createConnectAckDtData(s7Data);
        this.write(socket, connectAckDtData.toByteArray());
        log.debug("客户端[{}]握手成功", socket.getRemoteSocketAddress());
        return true;
    }

    @Override
    protected void doClientHandle(Socket socket) {
        S7Data req = this.readS7DataFromClient(socket);
        if (!(req.getCotp() instanceof COTPData)
                || req.getCotp().getPduType() != EPduType.DT_DATA
                || req.getHeader().getMessageType() != EMessageType.JOB) {
            S7Data response = S7Data.createErrorResponse(req, EErrorClass.ERROR_ON_SUPPLIES, 0x8500);
            this.write(socket, response.toByteArray());
        }

        try {
            switch (req.getParameter().getFunctionCode()) {
                case READ_VARIABLE:
                    this.readVariableHandle(socket, req);
                    return;
                case WRITE_VARIABLE:
                    this.writeVariableHandle(socket, req);
                    return;
                default:
                    S7Data response = S7Data.createErrorResponse(req, EErrorClass.ERROR_ON_SUPPLIES, 0x8500);
                    this.write(socket, response.toByteArray());
            }
        } catch (Exception e) {
            S7Data response = S7Data.createErrorResponse(req, EErrorClass.ERROR_ON_SERVICE_PROCESSING, 0x8404);
            this.write(socket, response.toByteArray());
        }
    }

    /**
     * 读数据处理
     *
     * @param socket socket对象
     * @param req    请求数据
     */
    private void readVariableHandle(Socket socket, S7Data req) {
        ReadWriteParameter parameter = (ReadWriteParameter) req.getParameter();
        List<ReturnItem> returnItems = new ArrayList<>();
        try {
            this.rwLock.readLock().lock();
            parameter.getRequestItems().forEach(p1 -> {
                RequestItem p = (RequestItem) p1;
                // 判定该区域的数据是否存在
                String area = AddressUtil.parseArea(p);
                if (!this.dataMap.containsKey(area)) {
                    log.error("客户端[{}]读取[{}]数据，区域[{}]，字节索引[{}]，位索引[{}]，长度[{}]，无该区域地址数据",
                            socket.getRemoteSocketAddress(), p.getVariableType(), area, p.getByteAddress(), p.getBitAddress(), p.getCount());
                    returnItems.add(ReturnItem.createDefault(EReturnCode.OBJECT_DOES_NOT_EXIST));
                    return;
                }
                // 提取指定地址的字节数据
                byte[] bytes = this.dataMap.get(area);
                ByteReadBuff buff = new ByteReadBuff(bytes);
                byte[] data;
                if (p.getVariableType() == EParamVariableType.BYTE) {
                    data = buff.getBytes(p.getByteAddress(), p.getCount());
                } else {
                    byte oldData = buff.getByte(p.getByteAddress());
                    data = BooleanUtil.getValue(oldData, p.getBitAddress()) ? new byte[]{(byte) 0x01} : new byte[]{(byte) 0x00};
                }
                log.debug("客户端[{}]读取[{}]数据，区域[{}]，字节索引[{}]，位索引[{}]，长度[{}]，区域地址数据{}",
                        socket.getRemoteSocketAddress(), p.getVariableType(), area, p.getByteAddress(), p.getBitAddress(), p.getCount(), data);
                DataItem dataItem = DataItem.createAckBy(data, p.getVariableType() == EParamVariableType.BYTE ? EDataVariableType.BYTE_WORD_DWORD : EDataVariableType.BIT);
                returnItems.add(dataItem);
            });
        } finally {
            this.rwLock.readLock().unlock();
        }
        S7Data ack = S7Data.createReadWriteResponse(req, returnItems);
        this.write(socket, ack.toByteArray());
    }

    /**
     * 写入数据处理
     *
     * @param socket socket对象
     * @param req    请求数据
     */
    private void writeVariableHandle(Socket socket, S7Data req) {
        ReadWriteParameter parameter = (ReadWriteParameter) req.getParameter();
        ReadWriteDatum datum = (ReadWriteDatum) req.getDatum();
        List<DataItem> dataItems = datum.getReturnItems().stream().map(DataItem.class::cast).collect(Collectors.toList());
        List<ReturnItem> returnItems = new ArrayList<>();
        try {
            this.rwLock.writeLock().lock();
            for (int i = 0; i < parameter.getItemCount(); i++) {
                RequestItem p = (RequestItem) (parameter.getRequestItems().get(i));
                DataItem d = dataItems.get(i);
                // 判定该区域的数据是否存在
                String area = AddressUtil.parseArea(p);
                if (!this.dataMap.containsKey(area)) {
                    log.error("客户端[{}]写入[{}]数据，区域[{}]，字节索引[{}]，位索引[{}]，长度[{}]，无该区域地址",
                            socket.getRemoteSocketAddress(), p.getVariableType(), area, p.getByteAddress(), p.getBitAddress(), p.getCount());
                    returnItems.add(ReturnItem.createDefault(EReturnCode.OBJECT_DOES_NOT_EXIST));
                    continue;
                }
                // 写入指定地址的数据
                byte[] bytes = this.dataMap.get(area);
                if (p.getVariableType() == EParamVariableType.BYTE) {
                    System.arraycopy(d.getData(), 0, bytes, p.getByteAddress(), d.getData().length);
                } else {
                    byte newData = BooleanUtil.setBit(bytes[p.getByteAddress()], p.getBitAddress(), d.getData()[0] == 1);
                    System.arraycopy(new byte[]{newData}, 0, bytes, p.getByteAddress(), 1);
                }
                log.debug("客户端[{}]写入[{}]数据，区域[{}]，字节索引[{}]，位索引[{}]，长度[{}]，区域地址数据{}",
                        socket.getRemoteSocketAddress(), p.getVariableType(), area, p.getByteAddress(), p.getBitAddress(), p.getCount(), d.getData());
                returnItems.add(ReturnItem.createDefault(EReturnCode.SUCCESS));
            }
        } finally {
            this.rwLock.writeLock().unlock();
        }

        S7Data ack = S7Data.createReadWriteResponse(req, returnItems);
        this.write(socket, ack.toByteArray());
    }

    /**
     * 读取S7协议的数据
     *
     * @param socket socket对象
     * @return S7Data
     */
    private S7Data readS7DataFromClient(Socket socket) {
        byte[] data = this.readClientData(socket);
        return S7Data.fromBytes(data);
    }

    /**
     * 重写读取客户端数据，针对粘包粘包的数据处理
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
                throw new SocketRuntimeException("客户端主动断开");
            }
            // 先获取TPKT
            byte[] tpktData = new byte[4];
            tpktData[0] = (byte) firstByte;
            this.read(socket, tpktData, 1, 3, 1024, 0, true);
            TPKT tpkt = TPKT.fromBytes(tpktData);
            // 根据内部的长度获取剩余部分内容
            byte[] data = new byte[tpkt.getLength()];
            System.arraycopy(tpktData, 0, data, 0, tpktData.length);
            this.read(socket, data, 4, data.length - 4, 1024, 0, true);
            return data;
        } catch (IOException e) {
            throw new SocketRuntimeException(e);
        }
    }
}

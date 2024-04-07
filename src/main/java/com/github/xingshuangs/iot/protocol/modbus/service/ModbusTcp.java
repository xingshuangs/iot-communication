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


import com.github.xingshuangs.iot.common.constant.GeneralConst;
import com.github.xingshuangs.iot.exceptions.ModbusCommException;
import com.github.xingshuangs.iot.protocol.modbus.model.*;
import lombok.extern.slf4j.Slf4j;

import static com.github.xingshuangs.iot.common.constant.GeneralConst.LOCALHOST;
import static com.github.xingshuangs.iot.common.constant.GeneralConst.MODBUS_PORT;

/**
 * modbus 1个寄存器占2个字节
 *
 * @author xingshuang
 */
@Slf4j
public class ModbusTcp extends ModbusSkeletonAbstract<MbTcpRequest, MbTcpResponse> {

    public ModbusTcp() {
        this(1, LOCALHOST, MODBUS_PORT);
    }

    public ModbusTcp(String ip) {
        this(1, ip, MODBUS_PORT);
    }

    public ModbusTcp(String ip, int port) {
        this(1, ip, port);
    }

    public ModbusTcp(int unitId) {
        this(unitId, LOCALHOST, MODBUS_PORT);
    }

    public ModbusTcp(int unitId, String ip) {
        this(unitId, ip, MODBUS_PORT);
    }

    public ModbusTcp(int unitId, String ip, int port) {
        super(unitId, ip, port);
        this.tag = "ModbusTcp";
    }

    //region 底层数据通信部分

    /**
     * 从服务器读取数据
     *
     * @param req modbus协议数据
     * @return modbus协议数据
     */
    @Override
    protected MbTcpResponse readFromServer(MbTcpRequest req) {
        if (this.comCallback != null) {
            this.comCallback.accept(req.toByteArray());
        }
        MbapHeader header;
        int len;
        byte[] total;
        synchronized (this.objLock) {
            this.write(req.toByteArray());

            byte[] data = new byte[MbapHeader.BYTE_LENGTH];
            len = this.read(data);
            if (len < MbapHeader.BYTE_LENGTH) {
                // MbapHeader 无效，读取长度不一致
                throw new ModbusCommException("MbapHeader is invalid, the read length is inconsistent");
            }
            header = MbapHeader.fromBytes(data);
            total = new byte[data.length + header.getLength() - 1];
            System.arraycopy(data, 0, total, 0, data.length);
            len = this.read(total, data.length, header.getLength() - 1);
        }
        if (len < header.getLength() - 1) {
            //  MbapHeader后面的数据长度不一致
            throw new ModbusCommException("The length of the data after MbapHeader is inconsistent");
        }
        if (this.comCallback != null) {
            this.comCallback.accept(total);
        }
        MbTcpResponse ack = MbTcpResponse.fromBytes(total);
        this.checkResult(req, ack);
        return ack;
    }

    /**
     * 校验请求数据和响应数据
     *
     * @param req 请求数据
     * @param ack 响应数据
     */
    @Override
    protected void checkResult(MbTcpRequest req, MbTcpResponse ack) {
        if (ack.getPdu() == null) {
            throw new ModbusCommException("PDU is null");
        }
        if (req.getHeader().getTransactionId() != ack.getHeader().getTransactionId()) {
            // 事务元标识符Id不一致
            throw new ModbusCommException("The transaction meta identifier Id is inconsistent");
        }
        if (ack.getPdu().getFunctionCode().getCode() == (req.getPdu().getFunctionCode().getCode() | (byte) 0x80)) {
            MbErrorResponse response = (MbErrorResponse) ack.getPdu();
            // 响应返回异常，异常码:
            throw new ModbusCommException("The response returns an exception, the exception code:" + response.getErrorCode().getDescription());
        }
        if (ack.getPdu().getFunctionCode().getCode() != req.getPdu().getFunctionCode().getCode()) {
            MbErrorResponse response = (MbErrorResponse) ack.getPdu();
            // 返回功能码和发送功能码不一致，异常码:
            throw new ModbusCommException("The return function code is inconsistent with the send function code. The exception code is: " + response.getErrorCode().getDescription());
        }
    }

    //endregion

    /**
     * 读取modbus数据
     *
     * @param reqPdu 请求对象
     * @return 响应结果
     */
    @Override
    protected MbPdu readModbusData(int unitId, MbPdu reqPdu) {
        MbTcpRequest request = MbTcpRequest.createDefault();
        request.getHeader().setUnitId(unitId);
        request.setPdu(reqPdu);
        request.selfCheck();
        try {
            MbTcpResponse response = this.readFromServer(request);
            return response.getPdu();
        } finally {
            if (!this.persistence) {
                log.debug("Due to the short connection mode, the communication is triggered to close the connection channel, and the server IP[{}]", this.socketAddress);
                this.close();
            }
        }
    }
}

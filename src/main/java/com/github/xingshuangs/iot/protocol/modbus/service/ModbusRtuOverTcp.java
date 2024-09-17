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
import com.github.xingshuangs.iot.protocol.modbus.model.MbErrorResponse;
import com.github.xingshuangs.iot.protocol.modbus.model.MbPdu;
import com.github.xingshuangs.iot.protocol.modbus.model.MbRtuRequest;
import com.github.xingshuangs.iot.protocol.modbus.model.MbRtuResponse;
import lombok.extern.slf4j.Slf4j;

import static com.github.xingshuangs.iot.common.constant.GeneralConst.LOCALHOST;
import static com.github.xingshuangs.iot.common.constant.GeneralConst.MODBUS_PORT;

/**
 * Modbus rtu over tcp.
 * (modbus 1个寄存器占2个字节)
 *
 * @author xingshuang
 */
@Slf4j
public class ModbusRtuOverTcp extends ModbusSkeletonAbstract<MbRtuRequest, MbRtuResponse> {

    public ModbusRtuOverTcp() {
        this(1, LOCALHOST, MODBUS_PORT);
    }

    public ModbusRtuOverTcp(String ip) {
        this(1, ip, MODBUS_PORT);
    }

    public ModbusRtuOverTcp(String ip, int port) {
        this(1, ip, port);
    }

    public ModbusRtuOverTcp(int unitId) {
        this(unitId, LOCALHOST, MODBUS_PORT);
    }

    public ModbusRtuOverTcp(int unitId, String ip) {
        this(unitId, ip, MODBUS_PORT);
    }

    public ModbusRtuOverTcp(int unitId, String ip, int port) {
        super(unitId, ip, port);
        this.tag = "ModbusRtu";
    }

    @Override
    protected MbRtuResponse readFromServer(MbRtuRequest req) {
        byte[] reqBytes = req.toByteArray();
        if (this.comCallback != null) {
            this.comCallback.accept(GeneralConst.PACKAGE_REQ, reqBytes);
        }
        int len;
        byte[] data = new byte[1024];
        synchronized (this.objLock) {
            this.write(reqBytes);
            len = this.read(data);
        }
        if (len <= 0) {
            // Modbus数据读取长度有误
            throw new ModbusCommException("The length of Modbus data read is incorrect");
        }
        byte[] total = new byte[len];
        System.arraycopy(data, 0, total, 0, len);
        if (this.comCallback != null) {
            this.comCallback.accept(GeneralConst.PACKAGE_ACK, total);
        }
        MbRtuResponse ack = MbRtuResponse.fromBytes(total);
        this.checkResult(req, ack);
        return ack;
    }

    @Override
    protected void checkResult(MbRtuRequest req, MbRtuResponse ack) {
        if (!ack.checkCrc()) {
            // 响应数据CRC校验失败
            throw new ModbusCommException("Response data CRC check failed");
        }
        if (ack.getPdu() == null) {
            throw new ModbusCommException("PDU is null");
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

    @Override
    protected MbPdu readModbusData(int unitId, MbPdu reqPdu) {
        MbRtuRequest request = new MbRtuRequest(unitId, reqPdu);
        try {
            MbRtuResponse response = this.readFromServer(request);
            return response.getPdu();
        } finally {
            if (!this.persistence) {
                log.debug("Due to the short connection mode, the communication is triggered to close the connection channel, and the server IP[{}]", this.socketAddress);
                this.close();
            }
        }
    }
}

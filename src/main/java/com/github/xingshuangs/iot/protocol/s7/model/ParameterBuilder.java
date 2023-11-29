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

package com.github.xingshuangs.iot.protocol.s7.model;


import com.github.xingshuangs.iot.exceptions.S7CommException;
import com.github.xingshuangs.iot.protocol.s7.enums.EFunctionCode;
import com.github.xingshuangs.iot.protocol.s7.enums.EMessageType;

/**
 * @author xingshuang
 */
public class ParameterBuilder {

    private ParameterBuilder() {
        // NOOP
    }

    /**
     * 字节数组数据解析
     *
     * @param data        字节数组数据
     * @param messageType 消息类型
     * @return Parameter
     */
    public static Parameter fromBytes(final byte[] data, EMessageType messageType) {
        EFunctionCode functionCode = EFunctionCode.from(data[0]);

        switch (functionCode) {
            case CPU_SERVICES:
                return null;
            case READ_VARIABLE:
            case WRITE_VARIABLE:
                return ReadWriteParameter.fromBytes(data);
            case START_DOWNLOAD:
                return messageType == EMessageType.ACK_DATA ? new Parameter(EFunctionCode.START_DOWNLOAD) : StartDownloadParameter.fromBytes(data);
            case DOWNLOAD:
                return messageType == EMessageType.ACK_DATA ? new Parameter(EFunctionCode.DOWNLOAD) : DownloadParameter.fromBytes(data);
            case END_DOWNLOAD:
                return messageType == EMessageType.ACK_DATA ? new Parameter(EFunctionCode.END_DOWNLOAD) : EndDownloadParameter.fromBytes(data);
            case START_UPLOAD:
                return messageType == EMessageType.ACK_DATA ? StartUploadAckParameter.fromBytes(data) : StartUploadParameter.fromBytes(data);
            case UPLOAD:
                return messageType == EMessageType.ACK_DATA ? UploadAckParameter.fromBytes(data) : UploadParameter.fromBytes(data);
            case END_UPLOAD:
                return messageType == EMessageType.ACK_DATA ? new Parameter(EFunctionCode.END_UPLOAD) : EndUploadParameter.fromBytes(data);
            case PLC_CONTROL:
                if (messageType == EMessageType.ACK) {
                    return PlcControlAckParameter.fromBytes(data);
                }
                return messageType == EMessageType.ACK_DATA ? new Parameter(EFunctionCode.PLC_CONTROL) : PlcControlParameter.fromBytes(data);
            case PLC_STOP:
                return messageType == EMessageType.ACK_DATA ? new Parameter(EFunctionCode.PLC_STOP) : PlcStopParameter.fromBytes(data);
            case SETUP_COMMUNICATION:
                return SetupComParameter.fromBytes(data);
            default:
                throw new S7CommException("Parameter的功能码不存在");
        }
    }
}

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


import com.github.xingshuangs.iot.common.IObjectByteArray;
import com.github.xingshuangs.iot.common.buff.ByteWriteBuff;
import com.github.xingshuangs.iot.protocol.s7.enums.EDestinationFileSystem;
import com.github.xingshuangs.iot.protocol.s7.enums.EErrorClass;
import com.github.xingshuangs.iot.protocol.s7.enums.EFileBlockType;
import com.github.xingshuangs.iot.protocol.s7.enums.EFunctionCode;
import lombok.Data;

import java.util.Arrays;
import java.util.List;

/**
 * S7 data structure
 *
 * @author xingshuang
 */
@Data
public class S7Data implements IObjectByteArray {

    /**
     * TPKT
     */
    private TPKT tpkt;

    /**
     * COTP
     */
    private COTP cotp;

    /**
     * header
     */
    private Header header;

    /**
     * parameter
     */
    private Parameter parameter;

    /**
     * data
     */
    private Datum datum;

    @Override
    public int byteArrayLength() {
        int length = 0;
        length += this.tpkt != null ? this.tpkt.byteArrayLength() : 0;
        length += this.cotp != null ? this.cotp.byteArrayLength() : 0;
        length += this.header != null ? this.header.byteArrayLength() : 0;
        length += this.parameter != null ? this.parameter.byteArrayLength() : 0;
        length += this.datum != null ? this.datum.byteArrayLength() : 0;
        return length;
    }

    @Override
    public byte[] toByteArray() {
        ByteWriteBuff buff = ByteWriteBuff.newInstance(this.byteArrayLength());
        if (this.tpkt != null) {
            buff.putBytes(this.tpkt.toByteArray());
        }
        if (this.cotp != null) {
            buff.putBytes(this.cotp.toByteArray());
        }
        if (this.header != null) {
            buff.putBytes(this.header.toByteArray());
        }
        if (this.parameter != null) {
            buff.putBytes(this.parameter.toByteArray());
        }
        if (this.datum != null) {
            buff.putBytes(this.datum.toByteArray());
        }
        return buff.getData();
    }

    /**
     * Self check data length, parameter length and byte length.
     * (自我数据校验)
     */
    public void selfCheck() {
        if (this.header != null) {
            this.header.setDataLength(0);
            this.header.setParameterLength(0);
        }
        if (this.parameter != null && this.header != null) {
            this.header.setParameterLength(this.parameter.byteArrayLength());
        }
        if (this.datum != null && this.header != null) {
            this.header.setDataLength(this.datum.byteArrayLength());
        }
        if (this.tpkt != null) {
            this.tpkt.setLength(this.byteArrayLength());
        }
    }

    /**
     * Parses byte array and converts it to object.
     * (根据字节数据解析S7协议数据)
     *
     * @param data byte array
     * @return s7 data
     */
    public static S7Data fromBytes(final byte[] data) {
        byte[] tpktBytes = Arrays.copyOfRange(data, 0, TPKT.BYTE_LENGTH);
        TPKT tpkt = TPKT.fromBytes(tpktBytes);
        byte[] remainBytes = Arrays.copyOfRange(data, TPKT.BYTE_LENGTH, data.length);
        return fromBytes(tpkt, remainBytes);
    }

    /**
     * Parses byte array and converts it to object.
     * (根据字节数据解析S7协议数据)
     *
     * @param tpkt   tpkt
     * @param remain remain bytes
     * @return s7 data
     */
    public static S7Data fromBytes(TPKT tpkt, final byte[] remain) {
        // tpkt
        S7Data s7Data = new S7Data();
        s7Data.tpkt = tpkt;
        // cotp
        COTP cotp = COTPBuilder.fromBytes(remain);
        s7Data.cotp = cotp;
        if (cotp == null || remain.length <= cotp.byteArrayLength()) {
            return s7Data;
        }

        //-----------------------------S7通信部分的内容--------------------------------------------
        byte[] lastBytes = Arrays.copyOfRange(remain, cotp.byteArrayLength(), remain.length);
        // header
        Header header = HeaderBuilder.fromBytes(lastBytes);
        s7Data.header = header;
        if (header == null) {
            return s7Data;
        }
        // parameter
        if (header.getParameterLength() > 0) {
            byte[] parameterBytes = Arrays.copyOfRange(lastBytes, header.byteArrayLength(), header.byteArrayLength() + header.getParameterLength());
            s7Data.parameter = ParameterBuilder.fromBytes(parameterBytes, s7Data.header.getMessageType());
        }
        // datum
        if (header.getDataLength() > 0) {
            byte[] dataBytes = Arrays.copyOfRange(lastBytes, header.byteArrayLength() + header.getParameterLength(), header.byteArrayLength() + header.getParameterLength() + header.getDataLength());
            s7Data.datum = Datum.fromBytes(dataBytes, s7Data.header.getMessageType(), s7Data.parameter.getFunctionCode());
        }
        return s7Data;
    }

    /**
     * Create connect request.
     * (创建连接请求)
     *
     * @param local  source tsap 本地参数
     * @param remote destination tsap 远程参数
     * @return s7data
     */
    public static S7Data createConnectRequest(int local, int remote) {
        S7Data s7Data = new S7Data();
        s7Data.tpkt = new TPKT();
        s7Data.cotp = COTPConnection.crConnectRequest(local, remote);
        s7Data.selfCheck();
        return s7Data;
    }

    /**
     * Create connect confirm request.
     * (创建连接确认)
     *
     * @param request request s7 data
     * @return s7data
     */
    public static S7Data createConnectConfirm(S7Data request) {
        S7Data s7Data = new S7Data();
        s7Data.tpkt = new TPKT();
        s7Data.cotp = COTPConnection.crConnectConfirm((COTPConnection) request.cotp);
        s7Data.selfCheck();
        return s7Data;
    }

    /**
     * Create connect dt data request.
     * (创建连接setup)
     *
     * @param pduLength PDU length
     * @return s7data
     */
    public static S7Data createConnectDtData(int pduLength) {
        S7Data s7Data = new S7Data();
        s7Data.tpkt = new TPKT();
        s7Data.cotp = COTPData.createDefault();
        s7Data.header = Header.createDefault();
        s7Data.parameter = SetupComParameter.createDefault(pduLength);
        s7Data.selfCheck();
        return s7Data;
    }

    /**
     * Create connect ack dt data request.
     * (创建连接响应setup)
     *
     * @param request request s7 data
     * @return s7data
     */
    public static S7Data createConnectAckDtData(S7Data request) {
        S7Data s7Data = new S7Data();
        s7Data.tpkt = new TPKT();
        s7Data.cotp = request.cotp;
        s7Data.header = AckHeader.createDefault(request.header, EErrorClass.NO_ERROR, 0);
        s7Data.parameter = request.parameter;
        s7Data.selfCheck();
        return s7Data;
    }

    /**
     * Create error response
     * (创建错误响应)
     *
     * @param request    request s7 data
     * @param errorClass error class
     * @param errorCode  error code
     * @return S7 data
     */
    public static S7Data createErrorResponse(S7Data request, EErrorClass errorClass, int errorCode) {
        S7Data s7Data = new S7Data();
        s7Data.tpkt = new TPKT();
        s7Data.cotp = COTPData.createDefault();
        s7Data.header = AckHeader.createDefault(request.header, errorClass, errorCode);
        s7Data.parameter = ReadWriteParameter.createAckParameter((ReadWriteParameter) request.parameter);
        s7Data.selfCheck();
        return s7Data;
    }

    /**
     * Create read request.
     * (创建默认读对象)
     *
     * @param requestItems request items
     * @return S7Data
     */
    public static S7Data createReadRequest(List<RequestItem> requestItems) {
        S7Data s7Data = new S7Data();
        s7Data.tpkt = new TPKT();
        s7Data.cotp = COTPData.createDefault();
        s7Data.header = Header.createDefault();
        s7Data.parameter = ReadWriteParameter.createReqParameter(EFunctionCode.READ_VARIABLE, requestItems);
        s7Data.selfCheck();
        return s7Data;
    }

    /**
     * Create write request.
     * (创建默认写对象)
     *
     * @param requestItems request items
     * @param dataItems    data items
     * @return S7Data
     */
    public static S7Data createWriteRequest(List<RequestItem> requestItems, List<DataItem> dataItems) {
        S7Data s7Data = new S7Data();
        s7Data.tpkt = new TPKT();
        s7Data.cotp = COTPData.createDefault();
        s7Data.header = Header.createDefault();
        s7Data.parameter = ReadWriteParameter.createReqParameter(EFunctionCode.WRITE_VARIABLE, requestItems);
        s7Data.datum = ReadWriteDatum.createDatum(dataItems);
        s7Data.selfCheck();
        return s7Data;
    }

    /**
     * Create read and write response.
     * (创建读写响应)
     *
     * @param request     request s7 data
     * @param returnItems return items
     * @return S7 data
     */
    public static S7Data createReadWriteResponse(S7Data request, List<ReturnItem> returnItems) {
        S7Data s7Data = new S7Data();
        s7Data.tpkt = new TPKT();
        s7Data.cotp = COTPData.createDefault();
        s7Data.header = AckHeader.createDefault(request.header, EErrorClass.NO_ERROR, 0);
        s7Data.parameter = ReadWriteParameter.createAckParameter((ReadWriteParameter) request.parameter);
        s7Data.datum = ReadWriteDatum.createDatum(returnItems);
        s7Data.selfCheck();
        return s7Data;
    }

    /**
     * Create hot restart request.
     * (创建热启动)
     *
     * @return S7Data
     */
    public static S7Data createHotRestart() {
        S7Data s7Data = new S7Data();
        s7Data.tpkt = new TPKT();
        s7Data.cotp = COTPData.createDefault();
        s7Data.header = Header.createDefault();
        s7Data.parameter = PlcControlParameter.hotRestart();
        s7Data.selfCheck();
        return s7Data;
    }

    /**
     * Create cold restart request.
     * (创建冷启动命令)
     *
     * @return S7Data
     */
    public static S7Data createColdRestart() {
        S7Data s7Data = new S7Data();
        s7Data.tpkt = new TPKT();
        s7Data.cotp = COTPData.createDefault();
        s7Data.header = Header.createDefault();
        s7Data.parameter = PlcControlParameter.coldRestart();
        s7Data.selfCheck();
        return s7Data;
    }

    /**
     * Create plc stop request.
     * (创建PLC停止命令)
     *
     * @return S7Data
     */
    public static S7Data createPlcStop() {
        S7Data s7Data = new S7Data();
        s7Data.tpkt = new TPKT();
        s7Data.cotp = COTPData.createDefault();
        s7Data.header = Header.createDefault();
        s7Data.parameter = PlcStopParameter.createDefault();
        s7Data.selfCheck();
        return s7Data;
    }

    /**
     * Create copy ram to rom request.
     * (创建复制Ram到Rom的命令)
     *
     * @return S7Data
     */
    public static S7Data createCopyRamToRom() {
        S7Data s7Data = new S7Data();
        s7Data.tpkt = new TPKT();
        s7Data.cotp = COTPData.createDefault();
        s7Data.header = Header.createDefault();
        s7Data.parameter = PlcControlParameter.copyRamToRom();
        s7Data.selfCheck();
        return s7Data;
    }

    /**
     * Create compress request.
     * (创建压缩命令)
     *
     * @return S7Data
     */
    public static S7Data createCompress() {
        S7Data s7Data = new S7Data();
        s7Data.tpkt = new TPKT();
        s7Data.cotp = COTPData.createDefault();
        s7Data.header = Header.createDefault();
        s7Data.parameter = PlcControlParameter.compress();
        s7Data.selfCheck();
        return s7Data;
    }

    /**
     * Create insert file command request
     * (创建插入文件指令)
     *
     * @param blockType             block type 数据块类型
     * @param blockNumber           block number 数据块编号
     * @param destinationFileSystem destination file system 目标文件系统
     * @return PlcControlParameter
     */
    public static S7Data createInsert(EFileBlockType blockType, int blockNumber, EDestinationFileSystem destinationFileSystem) {
        S7Data s7Data = new S7Data();
        s7Data.tpkt = new TPKT();
        s7Data.cotp = COTPData.createDefault();
        s7Data.header = Header.createDefault();
        s7Data.parameter = PlcControlParameter.insert(blockType, blockNumber, destinationFileSystem);
        s7Data.selfCheck();
        return s7Data;
    }

    /**
     * Create start download request
     * (创建开始下载)
     *
     * @param blockType             block type 数据块类型
     * @param blockNumber           block number 数据块编号
     * @param destinationFileSystem destination file system 目标文件系统
     * @param loadMemoryLength      load memory length 载入长度
     * @param mC7CodeLength         mc7 code length 文件内容长度
     * @return S7Data
     */
    public static S7Data createStartDownload(EFileBlockType blockType,
                                             int blockNumber,
                                             EDestinationFileSystem destinationFileSystem,
                                             int loadMemoryLength,
                                             int mC7CodeLength) {
        S7Data s7Data = new S7Data();
        s7Data.tpkt = new TPKT();
        s7Data.cotp = COTPData.createDefault();
        s7Data.header = Header.createDefault();
        s7Data.parameter = StartDownloadParameter.createDefault(blockType, blockNumber, destinationFileSystem, loadMemoryLength, mC7CodeLength);
        s7Data.selfCheck();
        return s7Data;
    }

    /**
     * Create download request.
     * (创建下载中)
     *
     * @param blockType             block type 数据块类型
     * @param blockNumber           block number 数据块编号
     * @param destinationFileSystem destination file system 目标文件系统
     * @param moreDataFollowing     more data following 是否还有更多数据
     * @param data                  byte array 字节数据
     * @return S7Data
     */
    public static S7Data createDownload(EFileBlockType blockType,
                                        int blockNumber,
                                        EDestinationFileSystem destinationFileSystem,
                                        boolean moreDataFollowing,
                                        byte[] data) {
        S7Data s7Data = new S7Data();
        s7Data.tpkt = new TPKT();
        s7Data.cotp = COTPData.createDefault();
        s7Data.header = Header.createDefault();
        s7Data.parameter = DownloadParameter.createDefault(blockType, blockNumber, destinationFileSystem, moreDataFollowing);
        s7Data.datum = UpDownloadDatum.createDownloadData(data);
        s7Data.selfCheck();
        return s7Data;
    }

    /**
     * Create end download request.
     * (创建结束下载)
     *
     * @param blockType             block type 数据块类型
     * @param blockNumber           block number 数据块编号
     * @param destinationFileSystem destination file system 目标文件系统
     * @return S7Data
     */
    public static S7Data createEndDownload(EFileBlockType blockType,
                                           int blockNumber,
                                           EDestinationFileSystem destinationFileSystem) {
        S7Data s7Data = new S7Data();
        s7Data.tpkt = new TPKT();
        s7Data.cotp = COTPData.createDefault();
        s7Data.header = Header.createDefault();
        s7Data.parameter = EndDownloadParameter.createDefault(blockType, blockNumber, destinationFileSystem);
        s7Data.selfCheck();
        return s7Data;
    }

    /**
     * Create start upload request.
     * (创建开始上传)
     *
     * @param blockType             block type 数据块类型
     * @param blockNumber           block number 数据块编号
     * @param destinationFileSystem destination file system 目标文件系统
     * @return S7Data
     */
    public static S7Data createStartUpload(EFileBlockType blockType,
                                           int blockNumber,
                                           EDestinationFileSystem destinationFileSystem) {
        S7Data s7Data = new S7Data();
        s7Data.tpkt = new TPKT();
        s7Data.cotp = COTPData.createDefault();
        s7Data.header = Header.createDefault();
        s7Data.parameter = StartUploadParameter.createDefault(blockType, blockNumber, destinationFileSystem);
        s7Data.selfCheck();
        return s7Data;
    }

    /**
     * Create upload request.
     * (创建上传中)
     *
     * @param uploadId upload Id
     * @return S7Data
     */
    public static S7Data createUpload(long uploadId) {
        S7Data s7Data = new S7Data();
        s7Data.tpkt = new TPKT();
        s7Data.cotp = COTPData.createDefault();
        s7Data.header = Header.createDefault();
        s7Data.parameter = UploadParameter.createDefault(uploadId);
        s7Data.selfCheck();
        return s7Data;
    }

    /**
     * Create end upload request.
     * (创建结束上传)
     *
     * @param uploadId upload Id
     * @return S7Data
     */
    public static S7Data createEndUpload(long uploadId) {
        S7Data s7Data = new S7Data();
        s7Data.tpkt = new TPKT();
        s7Data.cotp = COTPData.createDefault();
        s7Data.header = Header.createDefault();
        s7Data.parameter = EndUploadParameter.createDefault(uploadId);
        s7Data.selfCheck();
        return s7Data;
    }
}

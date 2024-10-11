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

package com.github.xingshuangs.iot.protocol.melsec.service;


import com.github.xingshuangs.iot.common.buff.ByteReadBuff;
import com.github.xingshuangs.iot.common.buff.ByteWriteBuff;
import com.github.xingshuangs.iot.common.constant.GeneralConst;
import com.github.xingshuangs.iot.exceptions.McCommException;
import com.github.xingshuangs.iot.net.client.TcpClientBasic;
import com.github.xingshuangs.iot.common.algorithm.LoopGroupAlg;
import com.github.xingshuangs.iot.common.algorithm.LoopGroupItem;
import com.github.xingshuangs.iot.protocol.melsec.enums.EMcCommand;
import com.github.xingshuangs.iot.protocol.melsec.enums.EMcDeviceCode;
import com.github.xingshuangs.iot.protocol.melsec.enums.EMcFrameType;
import com.github.xingshuangs.iot.protocol.melsec.enums.EMcSeries;
import com.github.xingshuangs.iot.protocol.melsec.model.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;

/**
 * plc network
 *
 * @author xingshuang
 */
@Slf4j
@Data
@EqualsAndHashCode(callSuper = true)
public class McNetwork extends TcpClientBasic {

    /**
     * locker
     */
    private final Object objLock = new Object();

    /**
     * Communication callback, first parameter is tag, second is package content.
     * (通信回调，第一个参数是tag标签，指示该报文含义；第二个参数是具体报文内容)
     */
    private BiConsumer<String, byte[]> comCallback;

    /**
     * Persistence, true: long connection, false: short connection.
     * (是否持久化，默认是持久化，对应长连接，true：长连接，false：短连接)
     */
    private boolean persistence = true;

    /**
     * Frame type
     * (帧类型)
     */
    protected EMcFrameType frameType = EMcFrameType.FRAME_3E;

    /**
     * Access route, 4E, 3E by default.
     * (访问路径，默认4E，3E帧访问路径)
     */
    protected McAccessRoute accessRoute = McFrame4E3EAccessRoute.createDefault();

    /**
     * Monitoring timer, default 30000ms.
     * (监视定时器，默认：3000ms，设置读取及写入的处理完成之前的等待时间。设置连接站E71向访问目标发出处理请求之后到返回响应为止的等待时间。)
     */
    protected int monitoringTimer = 3000;

    /**
     * PLC series.
     * (PLC的类型系列)
     */
    protected EMcSeries series = EMcSeries.QnA;

    public McNetwork() {
        super();
    }

    public McNetwork(String host, int port) {
        super(host, port);
    }

    //region 底层数据通信部分

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

    /**
     * Read data from server, core interaction.
     * (从服务器读取数据)
     *
     * @param req McMessageReq
     * @return McMessageAck
     */
    protected McMessageAck readFromServer(McMessageReq req) {
        byte[] reqBytes = req.toByteArray();
        if (this.comCallback != null) {
            this.comCallback.accept(GeneralConst.PACKAGE_REQ, reqBytes);
        }
        byte[] total;
        if (this.frameType == EMcFrameType.FRAME_4E || this.frameType == EMcFrameType.FRAME_3E) {
            total = this.readFromServer4E3E(reqBytes);
        } else {
            total = this.readFromServer1E(reqBytes);
        }
        if (this.comCallback != null) {
            this.comCallback.accept(GeneralConst.PACKAGE_ACK, total);
        }
        McMessageAck ack = McMessageAck.fromBytes(total, this.frameType);
        this.checkResult(req, ack);
        return ack;
    }

    /**
     * Read data from server of 1E.
     * (1E帧的通信交互)
     *
     * @param req req
     * @return ack
     */
    protected byte[] readFromServer1E(byte[] req) {
        int len;
        byte[] data = new byte[1024];
        synchronized (this.objLock) {
            this.write(req);
            len = this.read(data);
        }
        if (len < 0) {
            // McHeader 无效，读取长度不一致
            throw new McCommException(" McHeader is invalid, read length is inconsistent");
        }
        byte[] total = new byte[len];
        System.arraycopy(data, 0, total, 0, len);
        return total;
    }

    /**
     * Read data from server of 4E and 3E.
     * (3E帧和4E帧的通信交互)
     *
     * @param req req
     * @return ack
     */
    protected byte[] readFromServer4E3E(byte[] req) {
        int remainLength;
        int len;
        byte[] total;
        synchronized (this.objLock) {
            this.write(req);

            int headerLength = this.frameType == EMcFrameType.FRAME_4E ? 15 : 11;
            byte[] data = new byte[headerLength];
            len = this.read(data);
            if (len < headerLength) {
                // McHeader 无效，读取长度不一致
                throw new McCommException(" McHeader is invalid, read length is inconsistent");
            }
            McHeader3EAck header = (McHeader3EAck) McHeaderAck.fromBytes(data, this.frameType);
            remainLength = header.getDataLength() - 2;
            total = new byte[headerLength + remainLength];
            System.arraycopy(data, 0, total, 0, data.length);
            len = this.read(total, data.length, remainLength, true);
        }
        if (len < remainLength) {
            // McHeader后面的数据长度，长度不一致
            throw new McCommException("The length of the data behind the McHeader is inconsistent");
        }
        return total;
    }

    /**
     * Check req data and ack data.
     * (校验请求数据和响应数据)
     *
     * @param req req data
     * @param ack ack data
     */
    protected void checkResult(McMessageReq req, McMessageAck ack) {
        if (this.frameType == EMcFrameType.FRAME_4E && ack.getHeader().getSubHeader() != EMcFrameType.FRAME_4E.getAckSubHeader()) {
            // 4E帧类型，响应副帧头和请求副帧头不一致，请求副帧头+ req.getHeader().getSubHeader() + "，响应副帧头：" + ack.getHeader().getEndCode()
            throw new McCommException("4E frame type, the response sub header is inconsistent with the request sub header" +
                    ", the request sub header：" + req.getHeader().getSubHeader() + ", the response sub header：" + ack.getHeader().getEndCode());
        }
        if (this.frameType == EMcFrameType.FRAME_3E && ack.getHeader().getSubHeader() != EMcFrameType.FRAME_3E.getAckSubHeader()) {
            // "3E帧类型，响应副帧头和请求副帧头不一致，请求副帧头：" + req.getHeader().getSubHeader() + "，响应副帧头：" + ack.getHeader().getEndCode()
            throw new McCommException("3E frame type, the response sub header is inconsistent with the request sub header" +
                    ", the request sub header：" + req.getHeader().getSubHeader() + ", the response sub header：" + ack.getHeader().getEndCode());
        }
        if (this.frameType == EMcFrameType.FRAME_1E && ack.getHeader().getSubHeader() != req.getHeader().getSubHeader() + 0x80) {
            // "3E帧类型，响应副帧头和请求副帧头不一致，请求副帧头：" + req.getHeader().getSubHeader() + "，响应副帧头：" + ack.getHeader().getEndCode()
            throw new McCommException("1E frame type, the response sub header is inconsistent with the request sub header, error = 0x80" +
                    ", the request sub header：" + req.getHeader().getSubHeader() + ", the response sub header：" + ack.getHeader().getEndCode());
        }
        if (ack.getHeader().getEndCode() != 0) {
            String errorContent = this.extractError(ack.getHeader().getEndCode());
            // 响应返回异常，异常码
            String errorStr = String.format("The response returns an exception, an exception code:%d，%s", ack.getHeader().getEndCode(), errorContent);
            throw new McCommException(errorStr);
        }
    }

    /**
     * Extract error
     * (提取错误信息)
     *
     * @param errorCode error code
     * @return error string
     */
    private String extractError(int errorCode) {
        switch (errorCode) {
            case 0xC050:
                // 在"通信数据代码设置"中，设置ASCII代码通信时，接收了无法转换为二进制代码的ASCII代码的数据
                return "In Communication Data Code Settings, when you set up ASCII code communication, you received data in ASCII code that could not be converted to binary code";
            case 0xC051:
            case 0xC052:
            case 0xC053:
            case 0xC054:
                // 写入或读取点数超出了允许范围
                return "The number of write or read points exceeds the allowed range";
            case 0xC056:
                // 写入及读取请求超出了最大地址
                return "Write and read requests exceeded the maximum address";
            case 0xC058:
                // ASCII-二进制转换后的请求数据长度与字符部分的数据数不一致
                return "The length of the requested data after the ASCII-binary conversion is inconsistent with the number of data in the character part";
            case 0xC059:
                // 指令、子指令的指定中有错误
                return "There is an error in the assignment of instruction or subinstruction";
            case 0xC05B:
                // CPU模块无法对指定软元件尽心写入及读取
                return "The CPU module cannot write and read the specified software component";
            case 0xC05C:
                // 请求内容中有错误。（对字软元件进行了以位为单位的写入及读取等）
                return "There is an error in the request content.(The word software components are written and read in bit units, etc.)";
            case 0xC05D:
                // 未进行监视登录
                return "No monitored login";
            case 0xC05F:
                // 是无法对对象CPU模块执行的请求
                return "The request to the object CPU module could not be executed";
            case 0xC060:
                // 请求内容中有错误。（对位软元件的数据指定中有错误）
                return "There is an error in the request content.(There is an error in the data assignment of the alignment software component)";
            case 0xC061:
                // 请求数据长度与字符部分的数据数不一致
                return "The requested data length does not match the number of data in the character section";
            case 0xC0B5:
                // 指定了CPU模块中无法处理的数据
                return "Specifies data that cannot be processed in the CPU module";
            default:
                // 请查询三菱用户手册进行错误解析
                return "Please consult the Mitsubishi user manual for error resolution: " + errorCode;
        }
    }

    //endregion

    //region 软元件批量读取和写入

    /**
     * Read device batch by raw way, not support A series.
     * (软元件最原始的批量读取，不支持A系列)
     *
     * @param command           command
     * @param subCommand        sub command
     * @param deviceCode        device code
     * @param headDeviceNumber  head device number
     * @param devicePointsCount device point count
     * @return byte array
     */
    public byte[] readDeviceBatchRaw(EMcCommand command, int subCommand, EMcDeviceCode deviceCode,
                                     int headDeviceNumber, int devicePointsCount) {
        try {
            McHeaderReq header = McHeaderReq.createByFrameType(this.frameType, this.accessRoute, this.monitoringTimer);
            McDeviceAddress deviceAddress = new McDeviceAddress(deviceCode, headDeviceNumber, devicePointsCount);
            McReadDeviceBatchReqData data = new McReadDeviceBatchReqData();
            data.setSeries(this.series);
            data.setCommand(command);
            data.setSubcommand(subCommand);
            data.setDeviceAddress(deviceAddress);
            McMessageReq req = new McMessageReq(header, data);
            req.selfCheck();
            McMessageAck ack = this.readFromServer(req);
            McAckData ackData = (McAckData) ack.getData();
            return ackData.getData();
        } finally {
            if (!this.persistence) {
                this.close();
            }
        }
    }

    /**
     * Write device batch by raw way, not support A series.
     * (软元件最原始的批量写入，不支持A系列)
     *
     * @param command           command
     * @param subCommand        sub command
     * @param deviceCode        device code
     * @param headDeviceNumber  head device number
     * @param devicePointsCount device point count
     * @param dataBytes         data byte array
     */
    public void writeDeviceBatchRaw(EMcCommand command, int subCommand, EMcDeviceCode deviceCode,
                                    int headDeviceNumber, int devicePointsCount, byte[] dataBytes) {
        try {
            McHeaderReq header = McHeaderReq.createByFrameType(this.frameType, this.accessRoute, this.monitoringTimer);
            McDeviceContent deviceContent = new McDeviceContent(deviceCode, headDeviceNumber, devicePointsCount, dataBytes);
            McWriteDeviceBatchReqData data = new McWriteDeviceBatchReqData();
            data.setSeries(this.series);
            data.setCommand(command);
            data.setSubcommand(subCommand);
            data.setDeviceContent(deviceContent);
            McMessageReq req = new McMessageReq(header, data);
            req.selfCheck();
            this.readFromServer(req);
        } finally {
            if (!this.persistence) {
                this.close();
            }
        }
    }

    /**
     * Device access, batch read in word units
     * 软元件按字批量读取；<br>
     * 软元件点数 = 字的数量，而非字节数量，1个字 = 2个字节<br>
     * 不可以指定下述软元件。<br>
     * • 长定时器(触点: LTS、线圈: LTC)<br>
     * • 长累计定时器(触点: LSTS、线圈: LSTC)<br>
     * • 长变址寄存器(LZ)<br>
     *
     * @param deviceAddress device address
     * @return device content
     */
    public McDeviceContent readDeviceBatchInWord(McDeviceAddress deviceAddress) {
        if (deviceAddress == null) {
            throw new NullPointerException("deviceAddress");
        }
        if (deviceAddress.getDevicePointsCount() < 1) {
            throw new McCommException("1 < device point count");
        }
        if (deviceAddress.getDeviceCode() == EMcDeviceCode.LTS
                || deviceAddress.getDeviceCode() == EMcDeviceCode.LTC
                || deviceAddress.getDeviceCode() == EMcDeviceCode.LSTS
                || deviceAddress.getDeviceCode() == EMcDeviceCode.LSTC
                || deviceAddress.getDeviceCode() == EMcDeviceCode.LZ
        ) {
            throw new McCommException("restricted access LTS、LTC、LSTS、LSTC、LZ");
        }
        try {
            int actualLength = deviceAddress.getDevicePointsCount();
            int maxLength = this.series.getDeviceBatchInWordPointsCount();
//            int maxLength = 960;
            ByteWriteBuff buff = new ByteWriteBuff(deviceAddress.getDevicePointsCount() * 2);

            LoopGroupAlg.loopExecute(actualLength, maxLength, (off, len) -> {
                McDeviceAddress newAddress = new McDeviceAddress(deviceAddress.getDeviceCode(),
                        deviceAddress.getHeadDeviceNumber() + off, len);
                McHeaderReq header = McHeaderReq.createByFrameType(this.frameType, this.accessRoute, this.monitoringTimer);
                McMessageReq req = McReqBuilder.createReadDeviceBatchInWordReq(this.series, header, newAddress);
                McMessageAck ack = this.readFromServer(req);
                buff.putBytes(((McAckData) ack.getData()).getData());
            });
            return McDeviceContent.createByAddress(deviceAddress, buff.getData());
        } finally {
            if (!this.persistence) {
                this.close();
            }
        }
    }

    /**
     * Device access, batch write in word units
     * 软元件按字批量写入；<br>
     * 软元件点数 = 字的数量，而非字节数量，1个字 = 2个字节<br>
     * 不可以指定下述软元件。<br>
     * • 长定时器(触点: LTS、线圈: LTC、当前值: LTN)<br>
     * • 长累计定时器(触点: LSTS、线圈: LSTC、当前值: LSTN)<br>
     * • 长变址寄存器(LZ)
     *
     * @param deviceContent device content
     */
    public void writeDeviceBatchInWord(McDeviceContent deviceContent) {
        if (deviceContent == null) {
            throw new NullPointerException("deviceContent");
        }
        if (deviceContent.getDevicePointsCount() < 1) {
            throw new McCommException("1 < device point count");
        }
        if (deviceContent.getDeviceCode() == EMcDeviceCode.LTS
                || deviceContent.getDeviceCode() == EMcDeviceCode.LTC
                || deviceContent.getDeviceCode() == EMcDeviceCode.LTN
                || deviceContent.getDeviceCode() == EMcDeviceCode.LSTS
                || deviceContent.getDeviceCode() == EMcDeviceCode.LSTC
                || deviceContent.getDeviceCode() == EMcDeviceCode.LSTN
                || deviceContent.getDeviceCode() == EMcDeviceCode.LZ
        ) {
            throw new McCommException("restricted access LTS、LTC、LTN、LSTS、LSTC、LSTN、LZ");
        }

        try {
            int actualLength = deviceContent.getDevicePointsCount();
            int maxLength = this.series.getDeviceBatchInWordPointsCount();
//            int maxLength = 960;
            ByteReadBuff buff = new ByteReadBuff(deviceContent.getData());

            LoopGroupAlg.loopExecute(actualLength, maxLength, (off, len) -> {
                McDeviceContent newContent = new McDeviceContent(deviceContent.getDeviceCode(),
                        deviceContent.getHeadDeviceNumber() + off, len,
                        buff.getBytes(off * 2, len * 2));
                McHeaderReq header = McHeaderReq.createByFrameType(this.frameType, this.accessRoute, this.monitoringTimer);
                McMessageReq req = McReqBuilder.createWriteDeviceBatchInWordReq(this.series, header, newContent);
                this.readFromServer(req);
            });
        } finally {
            if (!this.persistence) {
                this.close();
            }
        }
    }

    /**
     * Device access, batch read in bit units
     * 软元件按位批量读取；
     * 软元件点数 = 位的数量，而非字节数量，2个位 = 1个字节
     * 不可以指定下述软元件。<br>
     * • 长定时器(触点: LTS、线圈: LTC)<br>
     * • 长累计定时器(触点: LSTS、线圈: LSTC)<br>
     * • 长变址寄存器(LZ)<br>
     *
     * @param deviceAddress device address
     * @return device content
     */
    public McDeviceContent readDeviceBatchInBit(McDeviceAddress deviceAddress) {
        if (deviceAddress == null) {
            throw new NullPointerException("deviceAddress");
        }
        if (deviceAddress.getDevicePointsCount() < 1) {
            throw new McCommException("1 < device point count");
        }
        if (!EMcDeviceCode.checkBitType(deviceAddress.getDeviceCode())) {
            // 只能是位软元件
            throw new McCommException("It can only be bit device code");
        }
        if (deviceAddress.getDeviceCode() == EMcDeviceCode.LTS
                || deviceAddress.getDeviceCode() == EMcDeviceCode.LTC
                || deviceAddress.getDeviceCode() == EMcDeviceCode.LSTS
                || deviceAddress.getDeviceCode() == EMcDeviceCode.LSTC
                || deviceAddress.getDeviceCode() == EMcDeviceCode.LZ
        ) {
            throw new McCommException("restricted access LTS、LTC、LSTS、LSTC、LZ");
        }

        try {
            int maxLength = this.series.getDeviceBatchInBitPointsCount();
//          int maxLength = 7168;
            int length = deviceAddress.getDevicePointsCount() % 2 == 0 ?
                    (deviceAddress.getDevicePointsCount() / 2) :
                    ((deviceAddress.getDevicePointsCount() + 1) / 2);
            ByteWriteBuff buff = new ByteWriteBuff(length);

            LoopGroupAlg.loopExecute(deviceAddress.getDevicePointsCount(), maxLength, (off, len) -> {
                McDeviceAddress newAddress = new McDeviceAddress(deviceAddress.getDeviceCode(),
                        deviceAddress.getHeadDeviceNumber() + off, len);
                McHeaderReq header = McHeaderReq.createByFrameType(this.frameType, this.accessRoute, this.monitoringTimer);
                McMessageReq req = McReqBuilder.createReadDeviceBatchInBitReq(this.series, header, newAddress);
                McMessageAck ack = this.readFromServer(req);
                buff.putBytes(((McAckData) ack.getData()).getData());
            });
            return McDeviceContent.createByAddress(deviceAddress, buff.getData());
        } finally {
            if (!this.persistence) {
                this.close();
            }
        }
    }

    /**
     * Device access, batch write in bit units
     * 软元件按位批量写入；<br>
     * 软元件点数 = 位的数量，而非字节数量，2个位 = 1个字节<br>
     * 不可以指定下述软元件。<br>
     * • 长定时器(触点: LTS、线圈: LTC、当前值: LTN)<br>
     * • 长累计定时器(触点: LSTS、线圈: LSTC、当前值: LSTN)<br>
     * • 长计数器(当前值: LCN)<br>
     * • 长变址寄存器(LZ)
     *
     * @param deviceContent device content
     */
    public void writeDeviceBatchInBit(McDeviceContent deviceContent) {
        if (deviceContent == null) {
            throw new NullPointerException("deviceContent");
        }
        if (deviceContent.getDevicePointsCount() < 1) {
            throw new McCommException("1 < device point count");
        }
        if (!EMcDeviceCode.checkBitType(deviceContent.getDeviceCode())) {
            // 只能是位软元件
            throw new McCommException("It can only be bit device code");
        }

        if (deviceContent.getDeviceCode() == EMcDeviceCode.LTS
                || deviceContent.getDeviceCode() == EMcDeviceCode.LTC
                || deviceContent.getDeviceCode() == EMcDeviceCode.LTN
                || deviceContent.getDeviceCode() == EMcDeviceCode.LSTS
                || deviceContent.getDeviceCode() == EMcDeviceCode.LSTC
                || deviceContent.getDeviceCode() == EMcDeviceCode.LSTN
                || deviceContent.getDeviceCode() == EMcDeviceCode.LCN
                || deviceContent.getDeviceCode() == EMcDeviceCode.LZ
        ) {
            throw new McCommException("restricted access LTS、LTC、LTN、LSTS、LSTC、LSTN、LCN、LZ");
        }

        try {
            int actualLength = deviceContent.getDevicePointsCount();
            int maxLength = this.series.getDeviceBatchInBitPointsCount();
//            int maxLength = 7168;
            ByteReadBuff buff = new ByteReadBuff(deviceContent.getData());

            LoopGroupAlg.loopExecute(actualLength, maxLength, (off, len) -> {
                int length = len % 2 == 0 ? (len / 2) : ((len + 1) / 2);
                McDeviceContent newContent = new McDeviceContent(deviceContent.getDeviceCode(),
                        deviceContent.getHeadDeviceNumber() + off, len,
                        buff.getBytes(off / 2, length));
                McHeaderReq header = McHeaderReq.createByFrameType(this.frameType, this.accessRoute, this.monitoringTimer);
                McMessageReq req = McReqBuilder.createWriteDeviceBatchInBitReq(this.series, header, newContent);
                this.readFromServer(req);
            });
        } finally {
            if (!this.persistence) {
                this.close();
            }
        }
    }

    //endregion

    //region 软元件随机读取和写入

    /**
     * Device access, random read in word units.
     * 软元件按字随机读取；<br>
     * 地址中软元件点数可以忽略，默认1，就算写入也自动忽略<br>
     * 不可以指定下述软元件。<br>
     * • 长定时器(触点: LTS、线圈: LTC)<br>
     * • 长累计定时器(触点: LSTS、线圈: LSTC)<br>
     * • 长计数器(触点: LCS、线圈: LCC)<br>
     *
     * @param wordAddresses  word device address
     * @param dwordAddresses dword device address
     * @return device content list.
     */
    public List<McDeviceContent> readDeviceRandomInWord(List<McDeviceAddress> wordAddresses, List<McDeviceAddress> dwordAddresses) {
        if (wordAddresses == null || dwordAddresses == null) {
            throw new NullPointerException("wordAddresses or dwordAddresses");
        }
        if (wordAddresses.isEmpty() && dwordAddresses.isEmpty()) {
            throw new IllegalArgumentException("wordAddresses and dwordAddresses is empty");
        }
        if (this.series.getFrameType() == EMcFrameType.FRAME_1E) {
            throw new McCommException("Frame 1E not supported in read device random in word");
        }
        boolean wordAllMatch = this.checkDeviceRandomCode(wordAddresses);
        boolean dwordAllMatch = this.checkDeviceRandomCode(dwordAddresses);
        if (!wordAllMatch || !dwordAllMatch) {
            throw new McCommException("restricted access LTS、LTC、LSTS、LSTC、LCS、LCC");
        }
        try {
            List<McDeviceContent> result = new ArrayList<>();
            int maxLength = this.series.getDeviceRandomReadInWordPointsCount();
//            int maxLength = this.series == EMcSeries.Q_L ? 192 : 96;

            BiPredicate<LoopGroupItem, LoopGroupItem> biPredicate = (i1, i2) -> i1.getLen() + i2.getLen() >= maxLength;
            LoopGroupItem wordItem = new LoopGroupItem(wordAddresses.size());
            LoopGroupItem dwordItem = new LoopGroupItem(dwordAddresses.size());

            LoopGroupAlg.biLoopExecute(wordItem, dwordItem, biPredicate, (i1, i2) -> {
                List<McDeviceAddress> newWords = wordAddresses.subList(i1.getOff(), i1.getOff() + i1.getLen());
                List<McDeviceAddress> newDWords = dwordAddresses.subList(i2.getOff(), i2.getOff() + i2.getLen());
                McHeaderReq header = McHeaderReq.createByFrameType(this.frameType, this.accessRoute, this.monitoringTimer);
                McMessageReq req = McReqBuilder.createReadDeviceRandomInWordReq(this.series, header, newWords, newDWords);
                McMessageAck ack = this.readFromServer(req);

                ByteReadBuff buff = new ByteReadBuff(((McAckData) ack.getData()).getData());
                for (McDeviceAddress word : newWords) {
                    result.add(McDeviceContent.createByAddress(word, buff.getBytes(2)));
                }
                for (McDeviceAddress dword : newDWords) {
                    result.add(McDeviceContent.createByAddress(dword, buff.getBytes(4)));
                }
            });
            return result;
        } finally {
            if (!this.persistence) {
                this.close();
            }
        }
    }

    /**
     * Check device random code.
     * (批量随机读写软元件约束)
     *
     * @param addresses device address list
     * @return true：match，false：mismatch
     */
    private boolean checkDeviceRandomCode(List<? extends McDeviceAddress> addresses) {
        return addresses.stream().allMatch(x -> x.getDeviceCode() != EMcDeviceCode.LTS
                && x.getDeviceCode() != EMcDeviceCode.LTC
                && x.getDeviceCode() != EMcDeviceCode.LSTS
                && x.getDeviceCode() != EMcDeviceCode.LSTC
                && x.getDeviceCode() != EMcDeviceCode.LCS
                && x.getDeviceCode() != EMcDeviceCode.LCC);
    }

    /**
     * Device access, random write in word units.
     * 软元件按字随机写入；
     * 地址中软元件点数可以忽略，默认1，就算写入也自动忽略
     *
     * @param wordContents  word device address + content
     * @param dwordContents dword device address + content
     */
    public void writeDeviceRandomInWord(List<McDeviceContent> wordContents, List<McDeviceContent> dwordContents) {
        if (wordContents == null || dwordContents == null) {
            throw new NullPointerException("wordContents or dwordContents");
        }
        if (wordContents.isEmpty() && dwordContents.isEmpty()) {
            throw new IllegalArgumentException("wordContents and dwordContents is empty");
        }
        boolean wordAllMatch = this.checkDeviceRandomCode(wordContents);
        boolean dwordAllMatch = this.checkDeviceRandomCode(dwordContents);
        if (!wordAllMatch || !dwordAllMatch) {
            throw new McCommException("restricted access LTS、LTC、LSTS、LSTC、LCS、LCC");
        }

        try {
            int maxLength = this.series.getDeviceRandomWriteInWordPointsCount();
//            int maxLength = this.series == EMcSeries.Q_L ? 1920 : 960;
            BiPredicate<LoopGroupItem, LoopGroupItem> biPredicate = (i1, i2) -> i1.getLen() * 12 + i2.getLen() * 14 >= maxLength;
            LoopGroupItem wordItem = new LoopGroupItem(wordContents.size());
            LoopGroupItem dwordItem = new LoopGroupItem(dwordContents.size());

            LoopGroupAlg.biLoopExecute(wordItem, dwordItem, biPredicate, (i1, i2) -> {
                List<McDeviceContent> newWord = wordContents.subList(i1.getOff(), i1.getOff() + i1.getLen());
                List<McDeviceContent> newDWord = dwordContents.subList(i2.getOff(), i2.getOff() + i2.getLen());
                McHeaderReq header = McHeaderReq.createByFrameType(this.frameType, this.accessRoute, this.monitoringTimer);
                McMessageReq req = McReqBuilder.createWriteDeviceRandomInWordReq(this.series, header, newWord, newDWord);
                this.readFromServer(req);
            });
        } finally {
            if (!this.persistence) {
                this.close();
            }
        }
    }

    /**
     * Device access, random write in bit units.
     * 软元件按位随机写入；应指定位软元件。
     * 软元件点数可以忽略，默认1，就算写入也自动忽略
     *
     * @param bitAddresses bit device address list
     */
    public void writeDeviceRandomInBit(List<McDeviceContent> bitAddresses) {
        if (bitAddresses == null || bitAddresses.isEmpty()) {
            throw new IllegalArgumentException("bitAddresses is null or empty");
        }
        // 软元件必须是位软元件
        boolean allMatch = bitAddresses.stream().allMatch(x -> EMcDeviceCode.checkBitType(x.getDeviceCode()));
        if (!allMatch) {
            // 只能是位软元件
            throw new McCommException("It can only be bit device code");
        }
        try {
            int maxLength = this.series.getDeviceRandomWriteInBitPointsCount();
//            int maxLength = this.series == EMcSeries.Q_L ? 188 : 94;
            LoopGroupAlg.loopExecute(bitAddresses.size(), maxLength, (off, len) -> {
                McHeaderReq header = McHeaderReq.createByFrameType(this.frameType, this.accessRoute, this.monitoringTimer);
                McMessageReq req = McReqBuilder.createWriteDeviceRandomInBitReq(this.series, header, bitAddresses.subList(off, off + len));
                this.readFromServer(req);
            });
        } finally {
            if (!this.persistence) {
                this.close();
            }
        }
    }

    //endregion

    //region 软元件多个块批量读取和写入

    /**
     * Device access, batch read multi blocks.
     * 软元件多块批量读取；<br>
     * 字访问软元件点数 = 字的数量，而非字节数量，1个字 = 2个字节；<br>
     * 位访问软元件点数 = 字的数量，而非字节数量，1个字 = 2个字节；<br>
     * 不可以指定下述软元件。<br>
     * • 长定时器(触点: LTS、线圈: LTC、当前值: LTN)<br>
     * • 长累计定时器(触点: LSTS、线圈: LSTC、当前值: LSTN)<br>
     * • 长计数器(触点: LCS、线圈: LCC、当前值: LCN)<br>
     * • 长变址寄存器(LZ)
     *
     * @param wordAddresses word address list
     * @param bitAddresses  bit address list
     * @return device content list
     */
    public List<McDeviceContent> readDeviceBatchMultiBlocks(List<McDeviceAddress> wordAddresses, List<McDeviceAddress> bitAddresses) {
        this.checkDeviceBatchMultiBlocksCondition(wordAddresses, bitAddresses);

        if (this.series.getFrameType() == EMcFrameType.FRAME_1E) {
            throw new McCommException("Frame 1E not supported in reading device batch multi blocks");
        }
        try {
            List<McDeviceContent> result = new ArrayList<>();
            int maxLength = this.series.getDeviceBlocksBlocksCount();
//            int maxLength = this.series == EMcSeries.Q_L ? 120 : 60;

            BiPredicate<LoopGroupItem, LoopGroupItem> biPredicate = (i1, i2) -> i1.getLen() + i2.getLen() >= maxLength;
            LoopGroupItem wordItem = new LoopGroupItem(wordAddresses.size());
            LoopGroupItem bitItem = new LoopGroupItem(bitAddresses.size());

            LoopGroupAlg.biLoopExecute(wordItem, bitItem, biPredicate, (i1, i2) -> {
                List<McDeviceAddress> newWords = wordAddresses.subList(i1.getOff(), i1.getOff() + i1.getLen());
                List<McDeviceAddress> newBits = bitAddresses.subList(i2.getOff(), i2.getOff() + i2.getLen());
                McHeaderReq header = McHeaderReq.createByFrameType(this.frameType, this.accessRoute, this.monitoringTimer);
                McMessageReq req = McReqBuilder.createReadDeviceBatchMultiBlocksReq(this.series, header, newWords, newBits);
                McMessageAck ack = this.readFromServer(req);

                ByteReadBuff buff = new ByteReadBuff(((McAckData) ack.getData()).getData());
                for (McDeviceAddress word : newWords) {
                    result.add(McDeviceContent.createByAddress(word, buff.getBytes(2 * word.getDevicePointsCount())));
                }
                for (McDeviceAddress bit : newBits) {
                    result.add(McDeviceContent.createByAddress(bit, buff.getBytes(2 * bit.getDevicePointsCount())));
                }
            });
            return result;
        } finally {
            if (!this.persistence) {
                this.close();
            }
        }
    }

    /**
     * Check device batch multi blocks condition.
     * (批量块读写软元件前置校验)
     *
     * @param words words device address list
     * @param bits  bits device address list
     */
    private void checkDeviceBatchMultiBlocksCondition(List<? extends McDeviceAddress> words,
                                                      List<? extends McDeviceAddress> bits) {
        if (words == null || bits == null) {
            throw new NullPointerException("wordAddresses or bitAddresses");
        }
        if (words.isEmpty() && bits.isEmpty()) {
            throw new IllegalArgumentException("the number of wordAddresses and bitAddresses is empty");
        }
        if (this.series.getFrameType() == EMcFrameType.FRAME_1E) {
            throw new McCommException("Frame 1E not supported in writing device batch multi blocks");
        }
        // TODO: 待确认
        if (this.frameType == EMcFrameType.FRAME_3E) {
            // 3E暂不支持批量块读写
            throw new McCommException("3E Currently does not support batch block read and write");
        }
        boolean b1 = words.stream().allMatch(x -> EMcDeviceCode.checkWordType(x.getDeviceCode()));
        if (!b1) {
            // 字软元件对应错误
            throw new McCommException("word device code error");
        }
        boolean b2 = bits.stream().allMatch(x -> EMcDeviceCode.checkBitType(x.getDeviceCode()));
        if (!b2) {
            // 位软元件对应错误
            throw new McCommException("bit device code error");
        }
        boolean wordAllMatch = this.checkDeviceBatchMultiBlocksCode(words);
        boolean bitAllMatch = this.checkDeviceBatchMultiBlocksCode(bits);
        if (!wordAllMatch || !bitAllMatch) {
            throw new McCommException("restricted access LTS、LTC、LTN、LSTS、LSTC、LSTN、LCS、LCC、LCN、LZ");
        }
    }

    /**
     * Check device batch multi blocks code.
     * (批量块读写软元件约束)
     *
     * @param addresses device address
     * @return true：match，false：mismatch
     */
    private boolean checkDeviceBatchMultiBlocksCode(List<? extends McDeviceAddress> addresses) {
        return addresses.stream().allMatch(x -> x.getDeviceCode() != EMcDeviceCode.LTS
                && x.getDeviceCode() != EMcDeviceCode.LTC
                && x.getDeviceCode() != EMcDeviceCode.LTN
                && x.getDeviceCode() != EMcDeviceCode.LSTS
                && x.getDeviceCode() != EMcDeviceCode.LSTC
                && x.getDeviceCode() != EMcDeviceCode.LSTN
                && x.getDeviceCode() != EMcDeviceCode.LCS
                && x.getDeviceCode() != EMcDeviceCode.LCC
                && x.getDeviceCode() != EMcDeviceCode.LCN
                && x.getDeviceCode() != EMcDeviceCode.LZ);
    }

    /**
     * Device access, batch write multi blocks.
     * 软元件多块批量写入
     * 字访问软元件点数 = 字的数量，而非字节数量，1个字 = 2个字节；
     * 位访问软元件点数 = 字的数量，而非字节数量，1个字 = 2个字节；
     *
     * @param wordContents word address + content
     * @param bitContents  bit address + content
     */
    public void writeDeviceBatchMultiBlocks(List<McDeviceContent> wordContents, List<McDeviceContent> bitContents) {
        this.checkDeviceBatchMultiBlocksCondition(wordContents, bitContents);

        try {
            BiPredicate<LoopGroupItem, LoopGroupItem> biPredicate = (i1, i2) -> {
                List<McDeviceContent> newWord = wordContents.subList(i1.getOff(), i1.getOff() + i1.getLen());
                List<McDeviceContent> newBit = bitContents.subList(i2.getOff(), i2.getOff() + i2.getLen());
                int blockNum = newWord.size() + newBit.size();
//                blockNum * (this.series == EMcSeries.Q_L ? 4 : 9)
                int count = blockNum * this.series.getDeviceBlocksWritePointsSize()
                        + (newWord.stream().mapToInt(McDeviceAddress::getDevicePointsCount).sum()
                        + newBit.stream().mapToInt(McDeviceAddress::getDevicePointsCount).sum());
                return (blockNum >= this.series.getDeviceBlocksBlocksCount()) || count >= this.series.getDeviceBlocksWritePointsCount();
//                return (blockNum >= (this.series == EMcSeries.Q_L ? 120 : 60)) || count >= 960;
            };
            LoopGroupItem wordItem = new LoopGroupItem(wordContents.size());
            LoopGroupItem bitItem = new LoopGroupItem(bitContents.size());

            LoopGroupAlg.biLoopExecute(wordItem, bitItem, biPredicate, (i1, i2) -> {
                List<McDeviceContent> newWords = wordContents.subList(i1.getOff(), i1.getOff() + i1.getLen());
                List<McDeviceContent> newBits = bitContents.subList(i2.getOff(), i2.getOff() + i2.getLen());
                McHeaderReq header = McHeaderReq.createByFrameType(this.frameType, this.accessRoute, this.monitoringTimer);
                McMessageReq req = McReqBuilder.createWriteDeviceBatchMultiBlocksReq(this.series, header, newWords, newBits);
                this.readFromServer(req);
            });
        } finally {
            if (!this.persistence) {
                this.close();
            }
        }
    }

    //endregion

    //region 软元件boolean列表和字节数组之间数据转换

    /**
     * Get boolean list by byte array.
     * (將字节数组转换为boolean列表)
     *
     * @param bytes byte array
     * @return boolean list
     */
    public List<Boolean> getBooleansBy(byte[] bytes) {
        List<Boolean> res = new ArrayList<>();
        for (byte aByte : bytes) {
            res.add((aByte & (byte) 0xF0) == 0x10);
            res.add((aByte & 0x0F) == 0x01);
        }
        return res;
    }

    /**
     * Get byte array by boolean list.
     * (将boolean列表转换为字节数组)
     *
     * @param booleans boolean list
     * @return byte array
     */
    public byte[] getBytesBy(List<Boolean> booleans) {
        int len = booleans.size() % 2 == 0 ? (booleans.size() / 2) : ((booleans.size() + 1) / 2);
        byte[] result = new byte[len];
        for (int i = 0; i < len; i++) {
            if (Boolean.TRUE.equals(booleans.get(i * 2))) {
                result[i] |= 0x10;
            }
            if (i * 2 + 1 < booleans.size() && Boolean.TRUE.equals(booleans.get(i * 2 + 1))) {
                result[i] |= 0x01;
            }
        }
        return result;
    }

    //endregion
}

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


import com.github.xingshuangs.iot.exceptions.McCommException;
import com.github.xingshuangs.iot.net.client.TcpClientBasic;
import com.github.xingshuangs.iot.protocol.common.buff.ByteReadBuff;
import com.github.xingshuangs.iot.protocol.common.buff.ByteWriteBuff;
import com.github.xingshuangs.iot.protocol.melsec.algorithm.McGroupAlg;
import com.github.xingshuangs.iot.protocol.melsec.algorithm.McGroupItem;
import com.github.xingshuangs.iot.protocol.melsec.enums.EMcCommand;
import com.github.xingshuangs.iot.protocol.melsec.enums.EMcDeviceCode;
import com.github.xingshuangs.iot.protocol.melsec.enums.EMcFrameType;
import com.github.xingshuangs.iot.protocol.melsec.enums.EMcSeries;
import com.github.xingshuangs.iot.protocol.melsec.model.*;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Consumer;

/**
 * plc的网络通信
 *
 * @author xingshuang
 */
@Slf4j
@Data
public class McNetwork extends TcpClientBasic {

    /**
     * 锁
     */
    private final Object objLock = new Object();

    /**
     * 通信回调
     */
    private Consumer<byte[]> comCallback;

    /**
     * 是否持久化，默认是持久化，对应长连接，true：长连接，false：短连接
     */
    private boolean persistence = true;

    /**
     * 帧类型
     */
    protected EMcFrameType frameType = EMcFrameType.FRAME_3E;

    /**
     * 访问路径，默认4E，3E帧访问路径
     */
    protected McAccessRoute accessRoute = McFrame4E3EAccessRoute.createDefault();

    /**
     * 监视定时器，默认：3000ms，设置读取及写入的处理完成之前的等待时间。设置连接站E71向访问目标发出处理请求之后到返回响应为止的等待时间。
     */
    protected int monitoringTimer = 3000;

    /**
     * PLC的类型系列
     */
    protected EMcSeries series = EMcSeries.Q_L;

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
     * 从服务器读取数据
     *
     * @param req McMessageReq请求
     * @return McMessageAck响应
     */
    protected McMessageAck readFromServer(McMessageReq req) {
        if (this.comCallback != null) {
            this.comCallback.accept(req.toByteArray());
        }
        McHeader header;
        int len;
        byte[] total;
        synchronized (this.objLock) {
            this.write(req.toByteArray());

            byte[] data = new byte[9];
            len = this.read(data);
            if (len < 9) {
                throw new McCommException(" McHeader 无效，读取长度不一致");
            }
            header = McHeader.fromBytes(data);
            total = new byte[9 + header.getDataLength()];
            System.arraycopy(data, 0, total, 0, data.length);
            len = this.read(total, data.length, header.getDataLength(), true);
        }
        if (len < header.getDataLength()) {
            throw new McCommException("McHeader后面的数据长度，长度不一致");
        }
        if (this.comCallback != null) {
            this.comCallback.accept(total);
        }
        McMessageAck ack = McMessageAck.fromBytes(total);
        this.checkResult(req, ack);
        return ack;
    }

    /**
     * 校验请求数据和响应数据
     *
     * @param req 请求数据
     * @param ack 响应数据
     */
    protected void checkResult(McMessageReq req, McMessageAck ack) {
        if (req.getHeader().getSubHeader() == EMcFrameType.FRAME_4E.getReqSubHeader()
                && ack.getHeader().getSubHeader() != EMcFrameType.FRAME_4E.getAckSubHeader()) {
            throw new McCommException("响应副帧头和请求副帧头不一致，请求副帧头：" + req.getHeader().getSubHeader()
                    + "，响应副帧头：" + ack.getHeader().getEndCode());
        }
        if (req.getHeader().getSubHeader() == EMcFrameType.FRAME_3E.getReqSubHeader()
                && ack.getHeader().getSubHeader() != EMcFrameType.FRAME_3E.getAckSubHeader()) {
            throw new McCommException("响应副帧头和请求副帧头不一致，请求副帧头：" + req.getHeader().getSubHeader()
                    + "，响应副帧头：" + ack.getHeader().getEndCode());
        }
        if (ack.getHeader().getEndCode() != 0) {
            String errorContent = this.extractError(ack.getHeader().getEndCode());
            String errorStr = String.format("响应返回异常，异常码:%d，%s", ack.getHeader().getEndCode(), errorContent);
            throw new McCommException(errorStr);
        }
    }

    /**
     * 提取错误信息
     *
     * @param errorCode 错误码
     * @return 错误信息字符串
     */
    private String extractError(int errorCode) {
        switch (errorCode) {
            case 0xC050:
                return "在\"通信数据代码设置\"中，设置ASCII代码通信时，接收了无法转换为二进制代码的ASCII代码的数据";
            case 0xC051:
            case 0xC052:
            case 0xC053:
            case 0xC054:
                return "写入或读取点数超出了允许范围";
            case 0xC056:
                return "写入及读取请求超出了最大地址";
            case 0xC058:
                return "ASCII-二进制转换后的请求数据长度与字符部分的数据数不一致";
            case 0xC059:
                return "指令、子指令的指定中有错误";
            case 0xC05B:
                return "CPU模块无法对指定软元件尽心写入及读取";
            case 0xC05C:
                return "请求内容内容中有错误。（对字软元件进行了以位为单位的写入及读取等）";
            case 0xC05D:
                return "未进行监视登录";
            case 0xC05F:
                return "是无法对对象CPU模块执行的请求";
            case 0xC060:
                return "请求内容中有错误。（对位软元件的数据指定中有错误）";
            case 0xC061:
                return "请求数据长度与字符部分的数据数不一致";
            case 0xC0B5:
                return "指定了CPU模块中无法处理的数据";
            default:
                return "请查询三菱用户手册进行错误解析";
        }
    }

    //endregion

    //region 软元件批量读取和写入

    /**
     * 软元件最原始的批量读取
     *
     * @param command           指令
     * @param subCommand        子指令
     * @param deviceCode        软元件代码
     * @param headDeviceNumber  起始软元件编号
     * @param devicePointsCount 软元件点数
     * @return 字节数组数据
     */
    public byte[] readDeviceBatchRaw(EMcCommand command, int subCommand, EMcDeviceCode deviceCode,
                                     int headDeviceNumber, int devicePointsCount) {
        try {
            McHeaderReq header = new McHeaderReq(this.frameType.getReqSubHeader(), this.accessRoute, this.monitoringTimer);
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
     * 软元件最原始的批量写入
     *
     * @param command           指令
     * @param subCommand        子指令
     * @param deviceCode        软元件代码
     * @param headDeviceNumber  起始软元件编号
     * @param devicePointsCount 软元件点数
     * @param dataBytes         待写入的字节数组数据
     */
    public void writeDeviceBatchRaw(EMcCommand command, int subCommand, EMcDeviceCode deviceCode,
                                    int headDeviceNumber, int devicePointsCount, byte[] dataBytes) {
        try {
            McHeaderReq header = new McHeaderReq(this.frameType.getReqSubHeader(), this.accessRoute, this.monitoringTimer);
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
     * 软元件按字批量读取；<br>
     * 软元件点数 = 字的数量，而非字节数量，1个字 = 2个字节<br>
     * 不可以指定下述软元件。<br>
     * • 长定时器(触点: LTS、线圈: LTC)<br>
     * • 长累计定时器(触点: LSTS、线圈: LSTC)<br>
     * • 长变址寄存器(LZ)<br>
     *
     * @param deviceAddress 数据地址
     * @return 数据内容
     */
    public McDeviceContent readDeviceBatchInWord(McDeviceAddress deviceAddress) {
        if (deviceAddress == null) {
            throw new NullPointerException("deviceAddress");
        }
        if (deviceAddress.getDevicePointsCount() < 1) {
            throw new McCommException("1 <= 字访问点数");
        }
        if (deviceAddress.getDeviceCode() == EMcDeviceCode.LTS
                || deviceAddress.getDeviceCode() == EMcDeviceCode.LTC
                || deviceAddress.getDeviceCode() == EMcDeviceCode.LSTS
                || deviceAddress.getDeviceCode() == EMcDeviceCode.LSTC
                || deviceAddress.getDeviceCode() == EMcDeviceCode.LZ
        ) {
            throw new McCommException("限制访问LTS、LTC、LSTS、LSTC、LZ");
        }
        try {
            int actualLength = deviceAddress.getDevicePointsCount();
            int maxLength = this.series.getDeviceBatchInWordPointsCount();
//            int maxLength = 960;
            ByteWriteBuff buff = new ByteWriteBuff(deviceAddress.getDevicePointsCount() * 2);

            McGroupAlg.loopExecute(actualLength, maxLength, (off, len) -> {
                McDeviceAddress newAddress = new McDeviceAddress(deviceAddress.getDeviceCode(),
                        deviceAddress.getHeadDeviceNumber() + off, len);
                McHeaderReq header = new McHeaderReq(this.frameType.getReqSubHeader(), this.accessRoute, this.monitoringTimer);
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
     * 软元件按字批量写入；<br>
     * 软元件点数 = 字的数量，而非字节数量，1个字 = 2个字节<br>
     * 不可以指定下述软元件。<br>
     * • 长定时器(触点: LTS、线圈: LTC、当前值: LTN)<br>
     * • 长累计定时器(触点: LSTS、线圈: LSTC、当前值: LSTN)<br>
     * • 长变址寄存器(LZ)
     *
     * @param deviceContent 数据内容
     */
    public void writeDeviceBatchInWord(McDeviceContent deviceContent) {
        if (deviceContent == null) {
            throw new NullPointerException("deviceContent");
        }
        if (deviceContent.getDevicePointsCount() < 1) {
            throw new McCommException("1 <= 字访问点数");
        }
        if (deviceContent.getDeviceCode() == EMcDeviceCode.LTS
                || deviceContent.getDeviceCode() == EMcDeviceCode.LTC
                || deviceContent.getDeviceCode() == EMcDeviceCode.LTN
                || deviceContent.getDeviceCode() == EMcDeviceCode.LSTS
                || deviceContent.getDeviceCode() == EMcDeviceCode.LSTC
                || deviceContent.getDeviceCode() == EMcDeviceCode.LSTN
                || deviceContent.getDeviceCode() == EMcDeviceCode.LZ
        ) {
            throw new McCommException("限制访问LTS、LTC、LTN、LSTS、LSTC、LSTN、LZ");
        }

        try {
            int actualLength = deviceContent.getDevicePointsCount();
            int maxLength = this.series.getDeviceBatchInWordPointsCount();
//            int maxLength = 960;
            ByteReadBuff buff = new ByteReadBuff(deviceContent.getData());

            McGroupAlg.loopExecute(actualLength, maxLength, (off, len) -> {
                McDeviceContent newContent = new McDeviceContent(deviceContent.getDeviceCode(),
                        deviceContent.getHeadDeviceNumber() + off, len,
                        buff.getBytes(off * 2, len * 2));
                McHeaderReq header = new McHeaderReq(this.frameType.getReqSubHeader(), this.accessRoute, this.monitoringTimer);
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
     * 软元件按位批量读取；
     * 软元件点数 = 位的数量，而非字节数量，2个位 = 1个字节
     * 不可以指定下述软元件。<br>
     * • 长定时器(触点: LTS、线圈: LTC)<br>
     * • 长累计定时器(触点: LSTS、线圈: LSTC)<br>
     * • 长变址寄存器(LZ)<br>
     *
     * @param deviceAddress 数据地址
     * @return 数据内容
     */
    public McDeviceContent readDeviceBatchInBit(McDeviceAddress deviceAddress) {
        if (deviceAddress == null) {
            throw new NullPointerException("deviceAddress");
        }
        if (deviceAddress.getDevicePointsCount() < 1) {
            throw new McCommException("1 <= 位访问点数");
        }
        if (!EMcDeviceCode.checkBitType(deviceAddress.getDeviceCode())) {
            throw new McCommException("只能是位软元件");
        }
        if (deviceAddress.getDeviceCode() == EMcDeviceCode.LTS
                || deviceAddress.getDeviceCode() == EMcDeviceCode.LTC
                || deviceAddress.getDeviceCode() == EMcDeviceCode.LSTS
                || deviceAddress.getDeviceCode() == EMcDeviceCode.LSTC
                || deviceAddress.getDeviceCode() == EMcDeviceCode.LZ
        ) {
            throw new McCommException("限制访问LTS、LTC、LSTS、LSTC、LZ");
        }

        try {
            int maxLength = this.series.getDeviceBatchInBitPointsCount();
//          int maxLength = 7168;
            int length = deviceAddress.getDevicePointsCount() % 2 == 0 ?
                    (deviceAddress.getDevicePointsCount() / 2) :
                    ((deviceAddress.getDevicePointsCount() + 1) / 2);
            ByteWriteBuff buff = new ByteWriteBuff(length);

            McGroupAlg.loopExecute(deviceAddress.getDevicePointsCount(), maxLength, (off, len) -> {
                McDeviceAddress newAddress = new McDeviceAddress(deviceAddress.getDeviceCode(),
                        deviceAddress.getHeadDeviceNumber() + off, len);
                McHeaderReq header = new McHeaderReq(this.frameType.getReqSubHeader(), this.accessRoute, this.monitoringTimer);
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
     * 软元件按位批量写入；<br>
     * 软元件点数 = 位的数量，而非字节数量，2个位 = 1个字节<br>
     * 不可以指定下述软元件。<br>
     * • 长定时器(触点: LTS、线圈: LTC、当前值: LTN)<br>
     * • 长累计定时器(触点: LSTS、线圈: LSTC、当前值: LSTN)<br>
     * • 长计数器(当前值: LCN)<br>
     * • 长变址寄存器(LZ)
     *
     * @param deviceContent 数据内容
     */
    public void writeDeviceBatchInBit(McDeviceContent deviceContent) {
        if (deviceContent == null) {
            throw new NullPointerException("deviceContent");
        }
        if (deviceContent.getDevicePointsCount() < 1) {
            throw new McCommException("1 <= 位访问点数");
        }
        if (!EMcDeviceCode.checkBitType(deviceContent.getDeviceCode())) {
            throw new McCommException("只能是位软元件");
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
            throw new McCommException("限制访问LTS、LTC、LTN、LSTS、LSTC、LSTN、LCN、LZ");
        }

        try {
            int actualLength = deviceContent.getDevicePointsCount();
            int maxLength = this.series.getDeviceBatchInBitPointsCount();
//            int maxLength = 7168;
            ByteReadBuff buff = new ByteReadBuff(deviceContent.getData());

            McGroupAlg.loopExecute(actualLength, maxLength, (off, len) -> {
                int length = len % 2 == 0 ? (len / 2) : ((len + 1) / 2);
                McDeviceContent newContent = new McDeviceContent(deviceContent.getDeviceCode(),
                        deviceContent.getHeadDeviceNumber() + off, len,
                        buff.getBytes(off / 2, length));
                McHeaderReq header = new McHeaderReq(this.frameType.getReqSubHeader(), this.accessRoute, this.monitoringTimer);
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
     * 软元件按字随机读取；<br>
     * 地址中软元件点数可以忽略，默认1，就算写入也自动忽略<br>
     * 不可以指定下述软元件。<br>
     * • 长定时器(触点: LTS、线圈: LTC)<br>
     * • 长累计定时器(触点: LSTS、线圈: LSTC)<br>
     * • 长计数器(触点: LCS、线圈: LCC)<br>
     *
     * @param wordAddresses  字地址
     * @param dwordAddresses 双字地址
     * @return 读取的内容
     */
    public List<McDeviceContent> readDeviceRandomInWord(List<McDeviceAddress> wordAddresses, List<McDeviceAddress> dwordAddresses) {
        if (wordAddresses == null || dwordAddresses == null) {
            throw new NullPointerException("wordAddresses or dwordAddresses");
        }
        if (wordAddresses.isEmpty() && dwordAddresses.isEmpty()) {
            throw new IllegalArgumentException("wordAddresses and dwordAddresses 数量为空");
        }
        boolean wordAllMatch = this.checkDeviceRandomCode(wordAddresses);
        boolean dwordAllMatch = this.checkDeviceRandomCode(dwordAddresses);
        if (!wordAllMatch || !dwordAllMatch) {
            throw new McCommException("限制访问LTS、LTC、LSTS、LSTC、LCS、LCC");
        }
        try {
            List<McDeviceContent> result = new ArrayList<>();
            int maxLength = this.series.getDeviceRandomReadInWordPointsCount();
//            int maxLength = this.series == EMcSeries.Q_L ? 192 : 96;

            BiPredicate<McGroupItem, McGroupItem> biPredicate = (i1, i2) -> i1.getLen() + i2.getLen() >= maxLength;
            McGroupItem wordItem = new McGroupItem(wordAddresses.size());
            McGroupItem dwordItem = new McGroupItem(dwordAddresses.size());

            McGroupAlg.biLoopExecute(wordItem, dwordItem, biPredicate, (i1, i2) -> {
                List<McDeviceAddress> newWords = wordAddresses.subList(i1.getOff(), i1.getOff() + i1.getLen());
                List<McDeviceAddress> newDWords = dwordAddresses.subList(i2.getOff(), i2.getOff() + i2.getLen());
                McHeaderReq header = new McHeaderReq(this.frameType.getReqSubHeader(), this.accessRoute, this.monitoringTimer);
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
     * 批量随机读写软元件约束
     *
     * @param addresses 软元件信息
     * @return true：符合，false：不符合
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
     * 软元件按字随机写入；
     * 地址中软元件点数可以忽略，默认1，就算写入也自动忽略
     *
     * @param wordContents  字内容
     * @param dwordContents 双字内容
     */
    public void writeDeviceRandomInWord(List<McDeviceContent> wordContents, List<McDeviceContent> dwordContents) {
        if (wordContents == null || dwordContents == null) {
            throw new NullPointerException("wordContents or dwordContents");
        }
        if (wordContents.isEmpty() && dwordContents.isEmpty()) {
            throw new IllegalArgumentException("wordContents and dwordContents 数量为空");
        }
        boolean wordAllMatch = this.checkDeviceRandomCode(wordContents);
        boolean dwordAllMatch = this.checkDeviceRandomCode(dwordContents);
        if (!wordAllMatch || !dwordAllMatch) {
            throw new McCommException("限制访问LTS、LTC、LSTS、LSTC、LCS、LCC");
        }

        try {
            int maxLength = this.series.getDeviceRandomWriteInWordPointsCount();
//            int maxLength = this.series == EMcSeries.Q_L ? 1920 : 960;
            BiPredicate<McGroupItem, McGroupItem> biPredicate = (i1, i2) -> i1.getLen() * 12 + i2.getLen() * 14 >= maxLength;
            McGroupItem wordItem = new McGroupItem(wordContents.size());
            McGroupItem dwordItem = new McGroupItem(dwordContents.size());

            McGroupAlg.biLoopExecute(wordItem, dwordItem, biPredicate, (i1, i2) -> {
                List<McDeviceContent> newWord = wordContents.subList(i1.getOff(), i1.getOff() + i1.getLen());
                List<McDeviceContent> newDWord = dwordContents.subList(i2.getOff(), i2.getOff() + i2.getLen());
                McHeaderReq header = new McHeaderReq(this.frameType.getReqSubHeader(), this.accessRoute, this.monitoringTimer);
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
     * 软元件按位随机写入；应指定位软元件。
     * 软元件点数可以忽略，默认1，就算写入也自动忽略
     *
     * @param bitAddresses 位地址
     */
    public void writeDeviceRandomInBit(List<McDeviceContent> bitAddresses) {
        if (bitAddresses == null || bitAddresses.isEmpty()) {
            throw new IllegalArgumentException("bitAddresses为null或空");
        }
        // 软元件必须是位软元件
        boolean allMatch = bitAddresses.stream().allMatch(x -> EMcDeviceCode.checkBitType(x.getDeviceCode()));
        if (!allMatch) {
            throw new McCommException("只能是位软元件");
        }
        try {
            int maxLength = this.series.getDeviceRandomWriteInBitPointsCount();
//            int maxLength = this.series == EMcSeries.Q_L ? 188 : 94;
            McGroupAlg.loopExecute(bitAddresses.size(), maxLength, (off, len) -> {
                McHeaderReq header = new McHeaderReq(this.frameType.getReqSubHeader(), this.accessRoute, this.monitoringTimer);
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
     * 软元件多块批量读取；<br>
     * 字访问软元件点数 = 字的数量，而非字节数量，1个字 = 2个字节；<br>
     * 位访问软元件点数 = 字的数量，而非字节数量，1个字 = 2个字节；<br>
     * 不可以指定下述软元件。<br>
     * • 长定时器(触点: LTS、线圈: LTC、当前值: LTN)<br>
     * • 长累计定时器(触点: LSTS、线圈: LSTC、当前值: LSTN)<br>
     * • 长计数器(触点: LCS、线圈: LCC、当前值: LCN)<br>
     * • 长变址寄存器(LZ)
     *
     * @param wordAddresses 字地址
     * @param bitAddresses  位地址
     * @return 读取的数据内容
     */
    public List<McDeviceContent> readDeviceBatchMultiBlocks(List<McDeviceAddress> wordAddresses, List<McDeviceAddress> bitAddresses) {
        this.checkDeviceBatchMultiBlocksCondition(wordAddresses, bitAddresses);

        try {
            List<McDeviceContent> result = new ArrayList<>();
            int maxLength = this.series.getDeviceBlocksBlocksCount();
//            int maxLength = this.series == EMcSeries.Q_L ? 120 : 60;

            BiPredicate<McGroupItem, McGroupItem> biPredicate = (i1, i2) -> i1.getLen() + i2.getLen() >= maxLength;
            McGroupItem wordItem = new McGroupItem(wordAddresses.size());
            McGroupItem bitItem = new McGroupItem(bitAddresses.size());

            McGroupAlg.biLoopExecute(wordItem, bitItem, biPredicate, (i1, i2) -> {
                List<McDeviceAddress> newWords = wordAddresses.subList(i1.getOff(), i1.getOff() + i1.getLen());
                List<McDeviceAddress> newBits = bitAddresses.subList(i2.getOff(), i2.getOff() + i2.getLen());
                McHeaderReq header = new McHeaderReq(this.frameType.getReqSubHeader(), this.accessRoute, this.monitoringTimer);
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
     * 批量块读写软元件前置校验
     *
     * @param words words软元件信息
     * @param bits  bits软元件信息
     */
    private void checkDeviceBatchMultiBlocksCondition(List<? extends McDeviceAddress> words,
                                                      List<? extends McDeviceAddress> bits) {
        if (words == null || bits == null) {
            throw new NullPointerException("wordAddresses or bitAddresses");
        }
        if (words.isEmpty() && bits.isEmpty()) {
            throw new IllegalArgumentException("wordAddresses and bitAddresses 数量为空");
        }
        // TODO: 待确认
        if (this.frameType == EMcFrameType.FRAME_3E) {
            throw new McCommException("3E暂不支持批量块读写");
        }
        boolean b1 = words.stream().allMatch(x -> EMcDeviceCode.checkWordType(x.getDeviceCode()));
        if (!b1) {
            throw new McCommException("字软元件对应错误");
        }
        boolean b2 = bits.stream().allMatch(x -> EMcDeviceCode.checkBitType(x.getDeviceCode()));
        if (!b2) {
            throw new McCommException("位软元件对应错误");
        }
        boolean wordAllMatch = this.checkDeviceBatchMultiBlocksCode(words);
        boolean bitAllMatch = this.checkDeviceBatchMultiBlocksCode(bits);
        if (!wordAllMatch || !bitAllMatch) {
            throw new McCommException("限制访问LTS、LTC、LTN、LSTS、LSTC、LSTN、LCS、LCC、LCN、LZ");
        }
    }

    /**
     * 批量块读写软元件约束
     *
     * @param addresses 软元件信息
     * @return true：符合，false：不符合
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
     * 软元件多块批量写入
     * 字访问软元件点数 = 字的数量，而非字节数量，1个字 = 2个字节；
     * 位访问软元件点数 = 字的数量，而非字节数量，1个字 = 2个字节；
     *
     * @param wordContents 带写入字地址+数据
     * @param bitContents  带写入位地址+数据
     */
    public void writeDeviceBatchMultiBlocks(List<McDeviceContent> wordContents, List<McDeviceContent> bitContents) {
        this.checkDeviceBatchMultiBlocksCondition(wordContents, bitContents);

        try {
            BiPredicate<McGroupItem, McGroupItem> biPredicate = (i1, i2) -> {
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
            McGroupItem wordItem = new McGroupItem(wordContents.size());
            McGroupItem bitItem = new McGroupItem(bitContents.size());

            McGroupAlg.biLoopExecute(wordItem, bitItem, biPredicate, (i1, i2) -> {
                List<McDeviceContent> newWords = wordContents.subList(i1.getOff(), i1.getOff() + i1.getLen());
                List<McDeviceContent> newBits = bitContents.subList(i2.getOff(), i2.getOff() + i2.getLen());
                McHeaderReq header = new McHeaderReq(this.frameType.getReqSubHeader(), this.accessRoute, this.monitoringTimer);
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
     * 將字节数组转换为boolean列表
     *
     * @param bytes 字节数组
     * @return boolean列表
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
     * 将boolean列表转换为字节数组
     *
     * @param booleans boolean列表
     * @return 字节数组
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

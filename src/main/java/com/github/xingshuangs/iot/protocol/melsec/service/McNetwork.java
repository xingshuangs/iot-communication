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
import com.github.xingshuangs.iot.protocol.melsec.enums.EMcCommand;
import com.github.xingshuangs.iot.protocol.melsec.enums.EMcDeviceCode;
import com.github.xingshuangs.iot.protocol.melsec.enums.EMcFrameType;
import com.github.xingshuangs.iot.protocol.melsec.enums.EMcSeries;
import com.github.xingshuangs.iot.protocol.melsec.model.*;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
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
    protected EMcFrameType frameType = EMcFrameType.FRAME_4E;

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
            len = this.read(total, data.length, header.getDataLength());
        }
        if (len < header.getDataLength()) {
            throw new McCommException(" McHeader后面的数据长度，长度不一致");
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
            throw new McCommException("响应返回异常，异常码:" + ack.getHeader().getEndCode());
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
     * 软元件按字批量读取；
     * 软元件点数 = 字的数量，而非字节数量，1个字 = 2个字节
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
        try {
            int actualLength = deviceAddress.getDevicePointsCount();
            int maxLength = 960;
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
     * 软元件按字批量写入；
     * 软元件点数 = 字的数量，而非字节数量，1个字 = 2个字节
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
        try {
            int actualLength = deviceContent.getDevicePointsCount();
            int maxLength = 960;
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
     *
     * @param deviceAddress 数据地址
     * @return 数据内容
     */
    public McDeviceContent readDeviceBatchInBit(McDeviceAddress deviceAddress) {
        if (deviceAddress == null) {
            throw new NullPointerException("deviceAddress");
        }
        if (deviceAddress.getDevicePointsCount() < 1 || deviceAddress.getDevicePointsCount() > 7168) {
            throw new McCommException("1 <= 位访问点数 <= 7168");
        }
        try {
            int actualLength = deviceAddress.getDevicePointsCount() % 2 == 0 ?
                    (deviceAddress.getDevicePointsCount() / 2) : ((deviceAddress.getDevicePointsCount() + 1) / 2);
            int maxLength = 7168 / 2;
            ByteWriteBuff buff = new ByteWriteBuff(actualLength);

            McGroupAlg.loopExecute(actualLength, maxLength, (off, len) -> {
                McDeviceAddress newAddress = new McDeviceAddress(deviceAddress.getDeviceCode(),
                        deviceAddress.getHeadDeviceNumber() + off * 2, len * 2);
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
     * 软元件按位批量写入；
     * 软元件点数 = 位的数量，而非字节数量，2个位 = 1个字节
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
        try {
            int actualLength = deviceContent.getDevicePointsCount() % 2 == 0 ?
                    (deviceContent.getDevicePointsCount() / 2) : ((deviceContent.getDevicePointsCount() + 1) / 2);
            int maxLength = 7168 / 2;
            ByteReadBuff buff = new ByteReadBuff(deviceContent.getData());

            McGroupAlg.loopExecute(actualLength, maxLength, (off, len) -> {
                McDeviceContent newContent = new McDeviceContent(deviceContent.getDeviceCode(),
                        deviceContent.getHeadDeviceNumber() + off * 2, len * 2,
                        buff.getBytes(off, len));
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
     * 软元件按字随机读取；
     * 地址中软元件点数可以忽略，默认1，就算写入也自动忽略
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
        try {
            List<McDeviceContent> result = new ArrayList<>();
            int actualLength = wordAddresses.size() + dwordAddresses.size();
            int maxLength = this.series == EMcSeries.Q_L ? 192 : 96;

            McGroupAlg.loopExecute(actualLength, maxLength, (off, len) -> {
                List<McDeviceAddress> newWords = new ArrayList<>();
                List<McDeviceAddress> newDWords = new ArrayList<>();
                if (off < wordAddresses.size() && off + len < wordAddresses.size()) {
                    newWords = wordAddresses.subList(off, off + len);
                } else if (off < wordAddresses.size() && off + len > wordAddresses.size()) {
                    newWords = wordAddresses.subList(off, wordAddresses.size());
                    newDWords = dwordAddresses.subList(0, off + len - wordAddresses.size());
                } else if (off > wordAddresses.size()) {
                    newDWords = dwordAddresses.subList(off - wordAddresses.size(), off - wordAddresses.size() + len);
                }
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
        int count = wordContents.size() * 12 + dwordContents.size() * 14;
        if (this.series == EMcSeries.Q_L && (count < 1 || count > 1920)) {
            throw new McCommException("1 ≤ (字访问点数×12)+(双字访问点数×14) ≤ 1920点");
        } else if (this.series == EMcSeries.IQ_R && (count < 1 || count > 960)) {
            throw new McCommException("1 ≤ (字访问点数×12)+(双字访问点数×14) ≤ 960点");
        }
        try {

            McHeaderReq header = new McHeaderReq(this.frameType.getReqSubHeader(), this.accessRoute, this.monitoringTimer);
            McMessageReq req = McReqBuilder.createWriteDeviceRandomInWordReq(this.series, header, wordContents, dwordContents);
            this.readFromServer(req);
        } finally {
            if (!this.persistence) {
                this.close();
            }
        }
    }

    /**
     * 软元件按位随机写入；
     * 软元件点数可以忽略，默认1，就算写入也自动忽略
     *
     * @param bitAddresses 位地址
     */
    public void writeDeviceRandomInBit(List<McDeviceContent> bitAddresses) {
        if (bitAddresses == null || bitAddresses.isEmpty()) {
            throw new IllegalArgumentException("bitAddresses为null或空");
        }
        try {
            int maxLength = this.series == EMcSeries.Q_L ? 188 : 94;
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
     * 软元件多块批量读取；
     * 字访问软元件点数 = 字的数量，而非字节数量，1个字 = 2个字节；
     * 位访问软元件点数 = 位的数量，而非字节数量，2个位 = 1个字节；
     *
     * @param wordAddresses 字地址
     * @param bitAddresses  位地址
     * @return 读取的数据内容
     */
    public List<McDeviceContent> readDeviceBatchMultiBlocks(List<McDeviceAddress> wordAddresses, List<McDeviceAddress> bitAddresses) {
        if (wordAddresses == null || bitAddresses == null) {
            throw new NullPointerException("wordAddresses or bitAddresses");
        }
        if (wordAddresses.isEmpty() && bitAddresses.isEmpty()) {
            throw new IllegalArgumentException("wordAddresses and bitAddresses 数量为空");
        }

        try {
            List<McDeviceContent> result = new ArrayList<>();
            int actualLength = wordAddresses.size() + bitAddresses.size();
            int maxLength = this.series == EMcSeries.Q_L ? 120 : 60;

            McGroupAlg.loopExecute(actualLength, maxLength, (off, len) -> {
                List<McDeviceAddress> newWords = new ArrayList<>();
                List<McDeviceAddress> newBits = new ArrayList<>();
                if (off < wordAddresses.size() && off + len < wordAddresses.size()) {
                    newWords = wordAddresses.subList(off, off + len);
                } else if (off < wordAddresses.size() && off + len > wordAddresses.size()) {
                    newWords = wordAddresses.subList(off, wordAddresses.size());
                    newBits = bitAddresses.subList(0, off + len - wordAddresses.size());
                } else if (off > wordAddresses.size()) {
                    newBits = bitAddresses.subList(off - wordAddresses.size(), off - wordAddresses.size() + len);
                }
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
     * 软元件多块批量写入
     * 字访问软元件点数 = 字的数量，而非字节数量，1个字 = 2个字节；
     * 位访问软元件点数 = 位的数量，而非字节数量，2个位 = 1个字节；
     *
     * @param wordContents 带写入字地址+数据
     * @param bitContents  带写入位地址+数据
     */
    public void writeDeviceBatchMultiBlocks(List<McDeviceContent> wordContents, List<McDeviceContent> bitContents) {
        if (wordContents == null || bitContents == null) {
            throw new NullPointerException("wordContents or bitContents");
        }
        if (wordContents.isEmpty() && bitContents.isEmpty()) {
            throw new IllegalArgumentException("wordContents and bitContents 数量为空");
        }
        if (this.series == EMcSeries.Q_L) {
            int count = (wordContents.size() + bitContents.size()) * 4
                    + (wordContents.stream().mapToInt(McDeviceAddress::getDevicePointsCount).sum()
                    + bitContents.stream().mapToInt(McDeviceAddress::getDevicePointsCount).sum());
            if (count < 1 || count > 960) {
                throw new McCommException("1 ≤ (各块数的合计×4)+(软元件点数的合计) ≤ 960点");
            }
        }
        if (this.series == EMcSeries.IQ_R) {
            int count = (wordContents.size() + bitContents.size()) * 9
                    + (wordContents.stream().mapToInt(McDeviceAddress::getDevicePointsCount).sum()
                    + bitContents.stream().mapToInt(McDeviceAddress::getDevicePointsCount).sum());
            if (count < 1 || count > 960) {
                throw new McCommException("1 ≤ (各块数的合计×9)+(软元件点数的合计) ≤ 960点");
            }
        }
        try {
            McHeaderReq header = new McHeaderReq(this.frameType.getReqSubHeader(), this.accessRoute, this.monitoringTimer);
            McMessageReq req = McReqBuilder.createWriteDeviceBatchMultiBlocksReq(this.series, header, wordContents, bitContents);
            this.readFromServer(req);
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
    protected List<Boolean> getBooleansBy(byte[] bytes) {
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
    protected byte[] getBytesBy(List<Boolean> booleans) {
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

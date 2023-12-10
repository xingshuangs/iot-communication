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

package com.github.xingshuangs.iot.protocol.melsec.model;


import com.github.xingshuangs.iot.protocol.melsec.enums.EMcFrameType;
import com.github.xingshuangs.iot.protocol.melsec.enums.EMcSeries;

import java.util.List;

/**
 * MC请求构建器
 *
 * @author xingshuang
 */
public class McReqBuilder {

    private McReqBuilder() {
        // NOOP
    }

    /**
     * 监视定时器
     */
    private static final int MONITORING_TIMER_DEFAULT = 3000;

    /**
     * 创建软元件按字批量读取请求
     *
     * @param deviceAddress 访问地址
     * @return 请求对象
     */
    public static McMessageReq createReadDeviceBatchInWordReq(McDeviceAddress deviceAddress) {
        McHeaderReq header = new McHeaderReq(EMcFrameType.FRAME_3E.getReqSubHeader(),
                McFrame4E3EAccessRoute.createDefault(), MONITORING_TIMER_DEFAULT);
        return createReadDeviceBatchInWordReq(EMcSeries.Q_L, header, deviceAddress);
    }

    /**
     * 创建软元件按字批量读取请求
     *
     * @param series        PLC系列
     * @param header        请求头
     * @param deviceAddress 访问地址
     * @return 请求对象
     */
    public static McMessageReq createReadDeviceBatchInWordReq(EMcSeries series,
                                                              McHeaderReq header,
                                                              McDeviceAddress deviceAddress) {
        McReadDeviceBatchInWordReqData data = new McReadDeviceBatchInWordReqData(series, deviceAddress);
        McMessageReq req = new McMessageReq(header, data);
        req.selfCheck();
        return req;
    }

    /**
     * 创建软元件按位批量读取请求
     *
     * @param deviceAddress 访问地址
     * @return 请求对象
     */
    public static McMessageReq createReadDeviceBatchInBitReq(McDeviceAddress deviceAddress) {
        McHeaderReq mcHeaderReq = new McHeaderReq(EMcFrameType.FRAME_3E.getReqSubHeader(),
                McFrame4E3EAccessRoute.createDefault(), MONITORING_TIMER_DEFAULT);
        return createReadDeviceBatchInBitReq(EMcSeries.Q_L, mcHeaderReq, deviceAddress);
    }

    /**
     * 创建软元件按位批量读取请求
     *
     * @param series        PLC系列
     * @param header        请求头
     * @param deviceAddress 访问地址
     * @return 请求对象
     */
    public static McMessageReq createReadDeviceBatchInBitReq(EMcSeries series,
                                                             McHeaderReq header,
                                                             McDeviceAddress deviceAddress) {
        McReadDeviceBatchInBitReqData data = new McReadDeviceBatchInBitReqData(series, deviceAddress);
        McMessageReq req = new McMessageReq(header, data);
        req.selfCheck();
        return req;
    }

    /**
     * 创建软元件按字批量写入请求
     *
     * @param deviceContent 待写入的数据内容
     * @return 请求对象
     */
    public static McMessageReq createWriteDeviceBatchInWordReq(McDeviceContent deviceContent) {
        McHeaderReq mcHeaderReq = new McHeaderReq(EMcFrameType.FRAME_3E.getReqSubHeader(),
                McFrame4E3EAccessRoute.createDefault(), MONITORING_TIMER_DEFAULT);
        return createWriteDeviceBatchInWordReq(EMcSeries.Q_L, mcHeaderReq, deviceContent);
    }

    /**
     * 创建软元件按字批量写入请求
     *
     * @param series        PLC系列
     * @param header        请求头
     * @param deviceContent 待写入的数据内容
     * @return 请求对象
     */
    public static McMessageReq createWriteDeviceBatchInWordReq(EMcSeries series,
                                                               McHeaderReq header,
                                                               McDeviceContent deviceContent) {
        McWriteDeviceBatchInWordReqData data = new McWriteDeviceBatchInWordReqData(series, deviceContent);
        McMessageReq req = new McMessageReq(header, data);
        req.selfCheck();
        return req;
    }

    /**
     * 创建软元件按位批量写入请求
     *
     * @param deviceContent 待写入的数据内容
     * @return 请求对象
     */
    public static McMessageReq createWriteDeviceBatchInBitReq(McDeviceContent deviceContent) {
        McHeaderReq mcHeaderReq = new McHeaderReq(EMcFrameType.FRAME_3E.getReqSubHeader(),
                McFrame4E3EAccessRoute.createDefault(), MONITORING_TIMER_DEFAULT);
        return createWriteDeviceBatchInBitReq(EMcSeries.Q_L, mcHeaderReq, deviceContent);
    }

    /**
     * 创建软元件按位批量写入请求
     *
     * @param series        PLC系列
     * @param header        请求头
     * @param deviceContent 待写入的数据内容
     * @return 请求对象
     */
    public static McMessageReq createWriteDeviceBatchInBitReq(EMcSeries series,
                                                              McHeaderReq header,
                                                              McDeviceContent deviceContent) {
        McWriteDeviceBatchInBitReqData data = new McWriteDeviceBatchInBitReqData(series, deviceContent);
        McMessageReq req = new McMessageReq(header, data);
        req.selfCheck();
        return req;
    }

    /**
     * 创建软元件按字随机读取请求
     *
     * @param wordAddresses  字地址
     * @param dwordAddresses 双字地址
     * @return 请求对象
     */
    public static McMessageReq createReadDeviceRandomInWordReq(List<McDeviceAddress> wordAddresses,
                                                               List<McDeviceAddress> dwordAddresses) {
        McHeaderReq mcHeaderReq = new McHeaderReq(EMcFrameType.FRAME_3E.getReqSubHeader(),
                McFrame4E3EAccessRoute.createDefault(), MONITORING_TIMER_DEFAULT);
        return createReadDeviceRandomInWordReq(EMcSeries.Q_L, mcHeaderReq, wordAddresses, dwordAddresses);
    }

    /**
     * 创建软元件按字随机读取请求
     *
     * @param series         PLC系列
     * @param header         请求头
     * @param wordAddresses  字地址
     * @param dwordAddresses 双字地址
     * @return 请求对象
     */
    public static McMessageReq createReadDeviceRandomInWordReq(EMcSeries series,
                                                               McHeaderReq header,
                                                               List<McDeviceAddress> wordAddresses,
                                                               List<McDeviceAddress> dwordAddresses) {
        McReadDeviceRandomInWordReqData data = new McReadDeviceRandomInWordReqData(series, wordAddresses, dwordAddresses);
        McMessageReq req = new McMessageReq(header, data);
        req.selfCheck();
        return req;
    }

    /**
     * 创建软元件按字随机写入请求
     *
     * @param wordContents  待写入的字数据内容
     * @param dwordContents 待写入的双字数据内容
     * @return 请求对象
     */
    public static McMessageReq createWriteDeviceRandomInWordReq(List<McDeviceContent> wordContents,
                                                                List<McDeviceContent> dwordContents) {
        McHeaderReq mcHeaderReq = new McHeaderReq(EMcFrameType.FRAME_3E.getReqSubHeader(),
                McFrame4E3EAccessRoute.createDefault(), MONITORING_TIMER_DEFAULT);
        return createWriteDeviceRandomInWordReq(EMcSeries.Q_L, mcHeaderReq, wordContents, dwordContents);
    }

    /**
     * 创建软元件按字随机写入请求
     *
     * @param series        PLC系列
     * @param header        请求头
     * @param wordContents  待写入的字数据内容
     * @param dwordContents 待写入的双字数据内容
     * @return 请求对象
     */
    public static McMessageReq createWriteDeviceRandomInWordReq(EMcSeries series,
                                                                McHeaderReq header,
                                                                List<McDeviceContent> wordContents,
                                                                List<McDeviceContent> dwordContents) {
        McWriteDeviceRandomInWordReqData data = new McWriteDeviceRandomInWordReqData(series, wordContents, dwordContents);
        McMessageReq req = new McMessageReq(header, data);
        req.selfCheck();
        return req;
    }

    /**
     * 创建软元件按位随机写入请求
     *
     * @param bitContents 待写入的位数据内容
     * @return 请求对象
     */
    public static McMessageReq createWriteDeviceRandomInBitReq(List<McDeviceContent> bitContents) {
        McHeaderReq mcHeaderReq = new McHeaderReq(EMcFrameType.FRAME_3E.getReqSubHeader(),
                McFrame4E3EAccessRoute.createDefault(), MONITORING_TIMER_DEFAULT);
        return createWriteDeviceRandomInBitReq(EMcSeries.Q_L, mcHeaderReq, bitContents);
    }

    /**
     * 创建软元件按位随机写入请求
     *
     * @param series      PLC系列
     * @param header      请求头
     * @param bitContents 待写入的位数据内容
     * @return 请求对象
     */
    public static McMessageReq createWriteDeviceRandomInBitReq(EMcSeries series,
                                                               McHeaderReq header,
                                                               List<McDeviceContent> bitContents) {
        McWriteDeviceRandomInBitReqData data = new McWriteDeviceRandomInBitReqData(series, bitContents);
        McMessageReq req = new McMessageReq(header, data);
        req.selfCheck();
        return req;
    }

    /**
     * 创建软元件多块读取请求
     *
     * @param wordAddresses 字地址
     * @param bitAddresses  位地址
     * @return 请求对象
     */
    public static McMessageReq createReadDeviceBatchMultiBlocksReq(List<McDeviceAddress> wordAddresses,
                                                                   List<McDeviceAddress> bitAddresses) {
        McHeaderReq mcHeaderReq = new McHeaderReq(EMcFrameType.FRAME_3E.getReqSubHeader(),
                McFrame4E3EAccessRoute.createDefault(), MONITORING_TIMER_DEFAULT);
        return createReadDeviceBatchMultiBlocksReq(EMcSeries.Q_L, mcHeaderReq, wordAddresses, bitAddresses);
    }

    /**
     * 创建软元件多块读取请求
     *
     * @param series        PLC系列
     * @param header        请求头
     * @param wordAddresses 字地址
     * @param bitAddresses  位地址
     * @return 请求对象
     */
    public static McMessageReq createReadDeviceBatchMultiBlocksReq(EMcSeries series,
                                                                   McHeaderReq header,
                                                                   List<McDeviceAddress> wordAddresses,
                                                                   List<McDeviceAddress> bitAddresses) {
        McReadDeviceBatchMultiBlocksReqData data = new McReadDeviceBatchMultiBlocksReqData(series, wordAddresses, bitAddresses);
        McMessageReq req = new McMessageReq(header, data);
        req.selfCheck();
        return req;
    }

    /**
     * 创建软元件多块批量写入请求
     *
     * @param wordContents 字数据
     * @param bitContents  位数据
     * @return 请求对象
     */
    public static McMessageReq createWriteDeviceBatchMultiBlocksReq(List<McDeviceContent> wordContents,
                                                                    List<McDeviceContent> bitContents) {
        McHeaderReq mcHeaderReq = new McHeaderReq(EMcFrameType.FRAME_3E.getReqSubHeader(),
                McFrame4E3EAccessRoute.createDefault(), MONITORING_TIMER_DEFAULT);
        return createWriteDeviceBatchMultiBlocksReq(EMcSeries.Q_L, mcHeaderReq, wordContents, bitContents);
    }

    /**
     * 创建软元件多块批量写入请求
     *
     * @param series       PLC系列
     * @param header       请求头
     * @param wordContents 字数据
     * @param bitContents  位数据
     * @return 请求对象
     */
    public static McMessageReq createWriteDeviceBatchMultiBlocksReq(EMcSeries series,
                                                                    McHeaderReq header,
                                                                    List<McDeviceContent> wordContents,
                                                                    List<McDeviceContent> bitContents) {
        McWriteDeviceBatchMultiBlocksReqData data = new McWriteDeviceBatchMultiBlocksReqData(series, wordContents, bitContents);
        McMessageReq req = new McMessageReq(header, data);
        req.selfCheck();
        return req;
    }
}

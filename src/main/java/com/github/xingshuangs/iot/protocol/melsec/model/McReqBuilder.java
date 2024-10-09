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


import com.github.xingshuangs.iot.protocol.melsec.enums.EMcCommand;
import com.github.xingshuangs.iot.protocol.melsec.enums.EMcFrameType;
import com.github.xingshuangs.iot.protocol.melsec.enums.EMcSeries;

import java.util.List;

/**
 * Req builder class.
 * (MC请求构建器)
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
     * Create request of read device batch in word.
     * (创建软元件按字批量读取请求)
     *
     * @param deviceAddress device address
     * @return request message
     */
    public static McMessageReq createReadDeviceBatchInWordReq(McDeviceAddress deviceAddress) {
        McHeaderReq header = McHeaderReq.createByFrameType(EMcFrameType.FRAME_3E, McFrame4E3EAccessRoute.createDefault(),
                MONITORING_TIMER_DEFAULT);
        return createReadDeviceBatchInWordReq(EMcSeries.QnA, header, deviceAddress);
    }

    /**
     * Create request of read device batch in word.
     * (创建软元件按字批量读取请求)
     *
     * @param series        PLC series
     * @param header        request header
     * @param deviceAddress device address
     * @return request message
     */
    public static McMessageReq createReadDeviceBatchInWordReq(EMcSeries series,
                                                              McHeaderReq header,
                                                              McDeviceAddress deviceAddress) {
        if (series.getFrameType() == EMcFrameType.FRAME_1E) {
            header.setSubHeader(EMcCommand.DEVICE_ACCESS_BATCH_READ_IN_WORD.getCode());
        }
        McReadDeviceBatchInWordReqData data = new McReadDeviceBatchInWordReqData(series, deviceAddress);
        McMessageReq req = new McMessageReq(header, data);
        req.selfCheck();
        return req;
    }

    /**
     * Create request of read device batch in bit.
     * (创建软元件按位批量读取请求)
     *
     * @param deviceAddress device address
     * @return request message
     */
    public static McMessageReq createReadDeviceBatchInBitReq(McDeviceAddress deviceAddress) {
        McHeaderReq header = McHeaderReq.createByFrameType(EMcFrameType.FRAME_3E, McFrame4E3EAccessRoute.createDefault(),
                MONITORING_TIMER_DEFAULT);
        return createReadDeviceBatchInBitReq(EMcSeries.QnA, header, deviceAddress);
    }

    /**
     * Create request of read device batch in bit.
     * (创建软元件按位批量读取请求)
     *
     * @param series        PLC series
     * @param header        request header
     * @param deviceAddress device address
     * @return request message
     */
    public static McMessageReq createReadDeviceBatchInBitReq(EMcSeries series,
                                                             McHeaderReq header,
                                                             McDeviceAddress deviceAddress) {
        if (series.getFrameType() == EMcFrameType.FRAME_1E) {
            header.setSubHeader(EMcCommand.DEVICE_ACCESS_BATCH_READ_IN_BIT.getCode());
        }
        McReadDeviceBatchInBitReqData data = new McReadDeviceBatchInBitReqData(series, deviceAddress);
        McMessageReq req = new McMessageReq(header, data);
        req.selfCheck();
        return req;
    }

    /**
     * Create request of write device batch in word.
     * (创建软元件按字批量写入请求)
     *
     * @param deviceContent device content
     * @return request message
     */
    public static McMessageReq createWriteDeviceBatchInWordReq(McDeviceContent deviceContent) {
        McHeaderReq header = McHeaderReq.createByFrameType(EMcFrameType.FRAME_3E, McFrame4E3EAccessRoute.createDefault(),
                MONITORING_TIMER_DEFAULT);
        return createWriteDeviceBatchInWordReq(EMcSeries.QnA, header, deviceContent);
    }

    /**
     * Create request of write device batch in word.
     * (创建软元件按字批量写入请求)
     *
     * @param series        PLC series
     * @param header        request header
     * @param deviceContent device content
     * @return request message
     */
    public static McMessageReq createWriteDeviceBatchInWordReq(EMcSeries series,
                                                               McHeaderReq header,
                                                               McDeviceContent deviceContent) {
        if (series.getFrameType() == EMcFrameType.FRAME_1E) {
            header.setSubHeader(EMcCommand.DEVICE_ACCESS_BATCH_WRITE_IN_WORD.getCode());
        }
        McWriteDeviceBatchInWordReqData data = new McWriteDeviceBatchInWordReqData(series, deviceContent);
        McMessageReq req = new McMessageReq(header, data);
        req.selfCheck();
        return req;
    }

    /**
     * Create request of write device batch in bit.
     * (创建软元件按位批量写入请求)
     *
     * @param deviceContent device content
     * @return request message
     */
    public static McMessageReq createWriteDeviceBatchInBitReq(McDeviceContent deviceContent) {
        McHeaderReq header = McHeaderReq.createByFrameType(EMcFrameType.FRAME_3E, McFrame4E3EAccessRoute.createDefault(),
                MONITORING_TIMER_DEFAULT);
        return createWriteDeviceBatchInBitReq(EMcSeries.QnA, header, deviceContent);
    }

    /**
     * Create request of write device batch in word.
     * (创建软元件按位批量写入请求)
     *
     * @param series        PLC series
     * @param header        request header
     * @param deviceContent device content
     * @return request message
     */
    public static McMessageReq createWriteDeviceBatchInBitReq(EMcSeries series,
                                                              McHeaderReq header,
                                                              McDeviceContent deviceContent) {
        if (series.getFrameType() == EMcFrameType.FRAME_1E) {
            header.setSubHeader(EMcCommand.DEVICE_ACCESS_BATCH_WRITE_IN_BIT.getCode());
        }
        McWriteDeviceBatchInBitReqData data = new McWriteDeviceBatchInBitReqData(series, deviceContent);
        McMessageReq req = new McMessageReq(header, data);
        req.selfCheck();
        return req;
    }

    /**
     * Create request of write device random in word.
     * (创建软元件按字随机读取请求)
     *
     * @param wordAddresses  word data address
     * @param dwordAddresses dword data address
     * @return request message
     */
    public static McMessageReq createReadDeviceRandomInWordReq(List<McDeviceAddress> wordAddresses,
                                                               List<McDeviceAddress> dwordAddresses) {
        McHeaderReq header = McHeaderReq.createByFrameType(EMcFrameType.FRAME_3E, McFrame4E3EAccessRoute.createDefault(),
                MONITORING_TIMER_DEFAULT);
        return createReadDeviceRandomInWordReq(EMcSeries.QnA, header, wordAddresses, dwordAddresses);
    }

    /**
     * Create request of write device random in word.
     * (创建软元件按字随机读取请求)
     *
     * @param series         PLC series
     * @param header         request header
     * @param wordAddresses  word data address
     * @param dwordAddresses dword data address
     * @return request message
     */
    public static McMessageReq createReadDeviceRandomInWordReq(EMcSeries series,
                                                               McHeaderReq header,
                                                               List<McDeviceAddress> wordAddresses,
                                                               List<McDeviceAddress> dwordAddresses) {
        if (series.getFrameType() == EMcFrameType.FRAME_1E) {
            header.setSubHeader(EMcCommand.DEVICE_ACCESS_RANDOM_WRITE_IN_WORD.getCode());
        }
        McReadDeviceRandomInWordReqData data = new McReadDeviceRandomInWordReqData(series, wordAddresses, dwordAddresses);
        McMessageReq req = new McMessageReq(header, data);
        req.selfCheck();
        return req;
    }

    /**
     * Create request of write device random in word.
     * (创建软元件按字随机写入请求)
     *
     * @param wordContents  word data content
     * @param dwordContents dword data content
     * @return request message
     */
    public static McMessageReq createWriteDeviceRandomInWordReq(List<McDeviceContent> wordContents,
                                                                List<McDeviceContent> dwordContents) {
        McHeaderReq header = McHeaderReq.createByFrameType(EMcFrameType.FRAME_3E, McFrame4E3EAccessRoute.createDefault(),
                MONITORING_TIMER_DEFAULT);
        return createWriteDeviceRandomInWordReq(EMcSeries.QnA, header, wordContents, dwordContents);
    }

    /**
     * Create request of write device random in word.
     * (创建软元件按字随机写入请求)
     *
     * @param series        PLC series
     * @param header        request header
     * @param wordContents  word data content
     * @param dwordContents dword data content
     * @return request message
     */
    public static McMessageReq createWriteDeviceRandomInWordReq(EMcSeries series,
                                                                McHeaderReq header,
                                                                List<McDeviceContent> wordContents,
                                                                List<McDeviceContent> dwordContents) {
        if (series.getFrameType() == EMcFrameType.FRAME_1E) {
            header.setSubHeader(EMcCommand.DEVICE_ACCESS_RANDOM_WRITE_IN_WORD.getCode());
        }
        McWriteDeviceRandomInWordReqData data = new McWriteDeviceRandomInWordReqData(series, wordContents, dwordContents);
        McMessageReq req = new McMessageReq(header, data);
        req.selfCheck();
        return req;
    }

    /**
     * Create request of write device random in bit.
     * (创建软元件按位随机写入请求)
     *
     * @param bitContents bit data content
     * @return request message
     */
    public static McMessageReq createWriteDeviceRandomInBitReq(List<McDeviceContent> bitContents) {
        McHeaderReq header = McHeaderReq.createByFrameType(EMcFrameType.FRAME_3E, McFrame4E3EAccessRoute.createDefault(),
                MONITORING_TIMER_DEFAULT);
        return createWriteDeviceRandomInBitReq(EMcSeries.QnA, header, bitContents);
    }

    /**
     * Create request of write device random in bit.
     * (创建软元件按位随机写入请求)
     *
     * @param series      PLC series
     * @param header      request header
     * @param bitContents bit data content
     * @return request message
     */
    public static McMessageReq createWriteDeviceRandomInBitReq(EMcSeries series,
                                                               McHeaderReq header,
                                                               List<McDeviceContent> bitContents) {
        if (series.getFrameType() == EMcFrameType.FRAME_1E) {
            header.setSubHeader(EMcCommand.DEVICE_ACCESS_RANDOM_WRITE_IN_BIT.getCode());
        }
        McWriteDeviceRandomInBitReqData data = new McWriteDeviceRandomInBitReqData(series, bitContents);
        McMessageReq req = new McMessageReq(header, data);
        req.selfCheck();
        return req;
    }

    /**
     * Create request of read device batch multi blocks.
     * (创建软元件多块读取请求)
     *
     * @param wordAddresses word data address
     * @param bitAddresses  bit data address
     * @return request message
     */
    public static McMessageReq createReadDeviceBatchMultiBlocksReq(List<McDeviceAddress> wordAddresses,
                                                                   List<McDeviceAddress> bitAddresses) {
        McHeaderReq header = McHeaderReq.createByFrameType(EMcFrameType.FRAME_3E, McFrame4E3EAccessRoute.createDefault(),
                MONITORING_TIMER_DEFAULT);
        return createReadDeviceBatchMultiBlocksReq(EMcSeries.QnA, header, wordAddresses, bitAddresses);
    }

    /**
     * Create request of read device batch multi blocks.
     * (创建软元件多块读取请求)
     *
     * @param series        PLC series
     * @param header        request header
     * @param wordAddresses word data address
     * @param bitAddresses  bit data address
     * @return request message
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
     * Create request of write device batch multi blocks.
     * (创建软元件多块批量写入请求)
     *
     * @param wordContents word data content
     * @param bitContents  bit data content
     * @return request message
     */
    public static McMessageReq createWriteDeviceBatchMultiBlocksReq(List<McDeviceContent> wordContents,
                                                                    List<McDeviceContent> bitContents) {
        McHeaderReq header = McHeaderReq.createByFrameType(EMcFrameType.FRAME_3E, McFrame4E3EAccessRoute.createDefault(),
                MONITORING_TIMER_DEFAULT);
        return createWriteDeviceBatchMultiBlocksReq(EMcSeries.QnA, header, wordContents, bitContents);
    }

    /**
     * Create request of write device batch multi blocks.
     * (创建软元件多块批量写入请求)
     *
     * @param series       PLC series
     * @param header       request header
     * @param wordContents word data content
     * @param bitContents  bit data content
     * @return request message
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

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


    public static McMessageReq createReadDeviceBatchInWordReq(McDeviceAddress deviceAddress) {
        McHeaderReq header = new McHeaderReq(EMcFrameType.FRAME_4E.getReqSubHeader(), Mc4E3EFrameAccessRoute.createDefault(), MONITORING_TIMER_DEFAULT);
        return createReadDeviceBatchInWordReq(EMcSeries.Q_L, header, deviceAddress);
    }

    public static McMessageReq createReadDeviceBatchInWordReq(EMcSeries series,
                                                              McHeaderReq header,
                                                              McDeviceAddress deviceAddress) {
        McReadDeviceBatchInWordReqData data = new McReadDeviceBatchInWordReqData(series, deviceAddress);
        McMessageReq req = new McMessageReq(header, data);
        req.selfCheck();
        return req;
    }

    public static McMessageReq createReadDeviceBatchInBitReq(McDeviceAddress deviceAddress) {
        McHeaderReq mcHeaderReq = new McHeaderReq(EMcFrameType.FRAME_4E.getReqSubHeader(), Mc4E3EFrameAccessRoute.createDefault(), MONITORING_TIMER_DEFAULT);
        return createReadDeviceBatchInBitReq(EMcSeries.Q_L, mcHeaderReq, deviceAddress);
    }

    public static McMessageReq createReadDeviceBatchInBitReq(EMcSeries series,
                                                             McHeaderReq header,
                                                             McDeviceAddress deviceAddress) {
        McReadDeviceBatchInBitReqData data = new McReadDeviceBatchInBitReqData(series, deviceAddress);
        McMessageReq req = new McMessageReq(header, data);
        req.selfCheck();
        return req;
    }

    public static McMessageReq createWriteDeviceBatchInWordReq(McDeviceContent deviceContent) {
        McHeaderReq mcHeaderReq = new McHeaderReq(EMcFrameType.FRAME_4E.getReqSubHeader(), Mc4E3EFrameAccessRoute.createDefault(), MONITORING_TIMER_DEFAULT);
        return createWriteDeviceBatchInWordReq(EMcSeries.Q_L, mcHeaderReq, deviceContent);
    }

    public static McMessageReq createWriteDeviceBatchInWordReq(EMcSeries series,
                                                               McHeaderReq header,
                                                               McDeviceContent deviceContent) {
        McWriteDeviceBatchInWordReqData data = new McWriteDeviceBatchInWordReqData(series, deviceContent);
        McMessageReq req = new McMessageReq(header, data);
        req.selfCheck();
        return req;
    }

    public static McMessageReq createWriteDeviceBatchInBitReq(McDeviceContent deviceContent) {
        McHeaderReq mcHeaderReq = new McHeaderReq(EMcFrameType.FRAME_4E.getReqSubHeader(), Mc4E3EFrameAccessRoute.createDefault(), MONITORING_TIMER_DEFAULT);
        return createWriteDeviceBatchInBitReq(EMcSeries.Q_L, mcHeaderReq, deviceContent);
    }

    public static McMessageReq createWriteDeviceBatchInBitReq(EMcSeries series,
                                                              McHeaderReq header,
                                                              McDeviceContent deviceContent) {
        McWriteDeviceBatchInBitReqData data = new McWriteDeviceBatchInBitReqData(series, deviceContent);
        McMessageReq req = new McMessageReq(header, data);
        req.selfCheck();
        return req;
    }

    public static McMessageReq createReadDeviceRandomInWordReq(List<McDeviceAddress> wordAddresses,
                                                               List<McDeviceAddress> dwordAddresses) {
        McHeaderReq mcHeaderReq = new McHeaderReq(EMcFrameType.FRAME_4E.getReqSubHeader(), Mc4E3EFrameAccessRoute.createDefault(), MONITORING_TIMER_DEFAULT);
        return createReadDeviceRandomInWordReq(EMcSeries.Q_L, mcHeaderReq, wordAddresses, dwordAddresses);
    }

    public static McMessageReq createReadDeviceRandomInWordReq(EMcSeries series,
                                                               McHeaderReq header,
                                                               List<McDeviceAddress> wordAddresses,
                                                               List<McDeviceAddress> dwordAddresses) {
        McReadDeviceRandomInWordReqData data = new McReadDeviceRandomInWordReqData(series, wordAddresses, dwordAddresses);
        McMessageReq req = new McMessageReq(header, data);
        req.selfCheck();
        return req;
    }

    public static McMessageReq createWriteDeviceRandomInWordReq(List<McDeviceContent> wordContents,
                                                                List<McDeviceContent> dwordContents) {
        McHeaderReq mcHeaderReq = new McHeaderReq(EMcFrameType.FRAME_4E.getReqSubHeader(), Mc4E3EFrameAccessRoute.createDefault(), MONITORING_TIMER_DEFAULT);
        return createWriteDeviceRandomInWordReq(EMcSeries.Q_L, mcHeaderReq, wordContents, dwordContents);
    }

    public static McMessageReq createWriteDeviceRandomInWordReq(EMcSeries series,
                                                                McHeaderReq header,
                                                                List<McDeviceContent> wordContents,
                                                                List<McDeviceContent> dwordContents) {
        McWriteDeviceRandomInWordReqData data = new McWriteDeviceRandomInWordReqData(series, wordContents, dwordContents);
        McMessageReq req = new McMessageReq(header, data);
        req.selfCheck();
        return req;
    }

    public static McMessageReq createWriteDeviceRandomInBitReq(List<McDeviceContent> bitAddresses) {
        McHeaderReq mcHeaderReq = new McHeaderReq(EMcFrameType.FRAME_4E.getReqSubHeader(), Mc4E3EFrameAccessRoute.createDefault(), MONITORING_TIMER_DEFAULT);
        return createWriteDeviceRandomInBitReq(EMcSeries.Q_L, mcHeaderReq, bitAddresses);
    }

    public static McMessageReq createWriteDeviceRandomInBitReq(EMcSeries series,
                                                               McHeaderReq header,
                                                               List<McDeviceContent> bitContents) {
        McWriteDeviceRandomInBitReqData data = new McWriteDeviceRandomInBitReqData(series, bitContents);
        McMessageReq req = new McMessageReq(header, data);
        req.selfCheck();
        return req;
    }

    public static McMessageReq createReadDeviceBatchMultiBlocksReq(List<McDeviceAddress> wordAddresses,
                                                                   List<McDeviceAddress> bitAddresses) {
        McHeaderReq mcHeaderReq = new McHeaderReq(EMcFrameType.FRAME_4E.getReqSubHeader(), Mc4E3EFrameAccessRoute.createDefault(), MONITORING_TIMER_DEFAULT);
        return createReadDeviceBatchMultiBlocksReq(EMcSeries.Q_L, mcHeaderReq, wordAddresses, bitAddresses);
    }

    public static McMessageReq createReadDeviceBatchMultiBlocksReq(EMcSeries series,
                                                                   McHeaderReq header,
                                                                   List<McDeviceAddress> wordAddresses,
                                                                   List<McDeviceAddress> bitAddresses) {
        McReadDeviceBatchMultiBlocksReqData data = new McReadDeviceBatchMultiBlocksReqData(series, wordAddresses, bitAddresses);
        McMessageReq req = new McMessageReq(header, data);
        req.selfCheck();
        return req;
    }

    public static McMessageReq createWriteDeviceBatchMultiBlocksReq(List<McDeviceContent> wordContents,
                                                                    List<McDeviceContent> bitContents) {
        McHeaderReq mcHeaderReq = new McHeaderReq(EMcFrameType.FRAME_4E.getReqSubHeader(), Mc4E3EFrameAccessRoute.createDefault(), MONITORING_TIMER_DEFAULT);
        return createWriteDeviceBatchMultiBlocksReq(EMcSeries.Q_L, mcHeaderReq, wordContents, bitContents);
    }

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

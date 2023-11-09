package com.github.xingshuangs.iot.protocol.melsec.model;


import com.github.xingshuangs.iot.protocol.melsec.enums.EMcCommand;
import com.github.xingshuangs.iot.protocol.melsec.enums.EMcSeries;
import com.github.xingshuangs.iot.protocol.melsec.enums.EMcSubHeader;

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
     * 创建4E帧的请求头
     *
     * @param accessRoute 访问路径
     * @param timer       监控时间，单位毫秒
     * @return McHeaderReq
     */
    public static McHeaderReq createMcHeaderReq4E(McAccessRoute accessRoute, int timer) {
        McHeaderReq req = new McHeaderReq();
        req.setSubHeader(EMcSubHeader.REQ_4E.getCode());
        req.setAccessRoute(accessRoute);
        req.setMonitoringTimer(timer / 250);
        return req;
    }

    public static McMessageReq createDeviceBatchReadWordReq(McDeviceAddress deviceAddress) {
        return createDeviceBatchReadWordReq(Mc4EFrameAccessRoute.createDefault(), MONITORING_TIMER_DEFAULT, deviceAddress);
    }

    public static McMessageReq createDeviceBatchReadWordReq(McAccessRoute accessRoute, int timer, McDeviceAddress deviceAddress) {
        McHeaderReq header = createMcHeaderReq4E(accessRoute, timer);
        McDeviceBatchReadReqData data = new McDeviceBatchReadReqData();
        data.setCommand(EMcCommand.DEVICE_ACCESS_BATCH_READ_IN_UNITS);
        data.setSubcommand(deviceAddress.getSeries() == EMcSeries.Q_L ? 0x0000 : 0x0002);
        data.setDeviceAddress(deviceAddress);
        McMessageReq req = new McMessageReq(header, data);
        req.selfCheck();
        return req;
    }

    public static McMessageReq createDeviceBatchReadBitReq(McDeviceAddress deviceAddress) {
        return createDeviceBatchReadBitReq(Mc4EFrameAccessRoute.createDefault(), MONITORING_TIMER_DEFAULT, deviceAddress);
    }

    public static McMessageReq createDeviceBatchReadBitReq(McAccessRoute accessRoute, int timer, McDeviceAddress deviceAddress) {
        McHeaderReq header = createMcHeaderReq4E(accessRoute, timer);
        McDeviceBatchReadReqData data = new McDeviceBatchReadReqData();
        data.setCommand(EMcCommand.DEVICE_ACCESS_BATCH_READ_IN_UNITS);
        data.setSubcommand(deviceAddress.getSeries() == EMcSeries.Q_L ? 0x0001 : 0x0003);
        data.setDeviceAddress(deviceAddress);
        McMessageReq req = new McMessageReq(header, data);
        req.selfCheck();
        return req;
    }

    public static McMessageReq createDeviceBatchWriteWordReq(McDeviceContent deviceContent) {
        return createDeviceBatchWriteWordReq(Mc4EFrameAccessRoute.createDefault(), MONITORING_TIMER_DEFAULT, deviceContent);
    }

    public static McMessageReq createDeviceBatchWriteWordReq(McAccessRoute accessRoute, int timer, McDeviceContent deviceContent) {
        McHeaderReq header = createMcHeaderReq4E(accessRoute, timer);
        McDeviceBatchWriteReqData data = new McDeviceBatchWriteReqData();
        data.setCommand(EMcCommand.DEVICE_ACCESS_BATCH_WRITE_IN_UNITS);
        data.setSubcommand(deviceContent.getSeries() == EMcSeries.Q_L ? 0x0000 : 0x0002);
        data.setDeviceContent(deviceContent);
        McMessageReq req = new McMessageReq(header, data);
        req.selfCheck();
        return req;
    }

    public static McMessageReq createDeviceBatchWriteBitReq(McDeviceContent deviceContent) {
        return createDeviceBatchWriteBitReq(Mc4EFrameAccessRoute.createDefault(), MONITORING_TIMER_DEFAULT, deviceContent);
    }

    public static McMessageReq createDeviceBatchWriteBitReq(McAccessRoute accessRoute, int timer, McDeviceContent deviceContent) {
        McHeaderReq header = createMcHeaderReq4E(accessRoute, timer);
        McDeviceBatchWriteReqData data = new McDeviceBatchWriteReqData();
        data.setCommand(EMcCommand.DEVICE_ACCESS_BATCH_WRITE_IN_UNITS);
        data.setSubcommand(deviceContent.getSeries() == EMcSeries.Q_L ? 0x0001 : 0x0003);
        data.setDeviceContent(deviceContent);
        McMessageReq req = new McMessageReq(header, data);
        req.selfCheck();
        return req;
    }

    public static McMessageReq createDeviceRandomReadWordReq(List<McDeviceAddress> wordAddresses,
                                                             List<McDeviceAddress> dwordAddresses) {
        return createDeviceRandomReadWordReq(Mc4EFrameAccessRoute.createDefault(), MONITORING_TIMER_DEFAULT, wordAddresses, dwordAddresses);
    }

    public static McMessageReq createDeviceRandomReadWordReq(McAccessRoute accessRoute, int timer,
                                                             List<McDeviceAddress> wordAddresses,
                                                             List<McDeviceAddress> dwordAddresses) {
        if (wordAddresses.isEmpty() && dwordAddresses.isEmpty()) {
            throw new IllegalArgumentException("wordAddresses&&dwordAddresses");
        }
        EMcSeries series = !wordAddresses.isEmpty() ? wordAddresses.get(0).getSeries() : dwordAddresses.get(0).getSeries();
        McHeaderReq header = createMcHeaderReq4E(accessRoute, timer);
        McDeviceRandomReadWordReqData data = new McDeviceRandomReadWordReqData();
        data.setCommand(EMcCommand.DEVICE_ACCESS_RANDOM_READ_IN_UNITS);
        data.setSubcommand(series == EMcSeries.Q_L ? 0x0000 : 0x0002);
        data.setWordAddresses(wordAddresses);
        data.setDwordAddresses(dwordAddresses);
        McMessageReq req = new McMessageReq(header, data);
        req.selfCheck();
        return req;
    }

    public static McMessageReq createDeviceRandomWriteWordReq(List<McDeviceContent> wordContents,
                                                             List<McDeviceContent> dwordContents) {
        return createDeviceRandomWriteWordReq(Mc4EFrameAccessRoute.createDefault(), MONITORING_TIMER_DEFAULT, wordContents, dwordContents);
    }

    public static McMessageReq createDeviceRandomWriteWordReq(McAccessRoute accessRoute, int timer,
                                                              List<McDeviceContent> wordContents,
                                                              List<McDeviceContent> dwordContents) {
        if (wordContents.isEmpty() && dwordContents.isEmpty()) {
            throw new IllegalArgumentException("wordContents&&dwordContents");
        }
        EMcSeries series = !wordContents.isEmpty() ? wordContents.get(0).getSeries() : dwordContents.get(0).getSeries();
        McHeaderReq header = createMcHeaderReq4E(accessRoute, timer);
        McDeviceRandomWriteWordReqData data = new McDeviceRandomWriteWordReqData();
        data.setCommand(EMcCommand.DEVICE_ACCESS_RANDOM_WRITE_IN_UNITS);
        data.setSubcommand(series == EMcSeries.Q_L ? 0x0000 : 0x0002);
        data.setWordContents(wordContents);
        data.setDwordContents(dwordContents);
        McMessageReq req = new McMessageReq(header, data);
        req.selfCheck();
        return req;
    }

    public static McMessageReq createDeviceRandomWriteBitReq(List<McDeviceContent> wordAddresses) {
        return createDeviceRandomWriteBitReq(Mc4EFrameAccessRoute.createDefault(), MONITORING_TIMER_DEFAULT, wordAddresses);
    }

    public static McMessageReq createDeviceRandomWriteBitReq(McAccessRoute accessRoute, int timer,
                                                              List<McDeviceContent> wordContents) {
        EMcSeries series = !wordContents.isEmpty() ? wordContents.get(0).getSeries() : EMcSeries.Q_L;
        McHeaderReq header = createMcHeaderReq4E(accessRoute, timer);
        McDeviceRandomWriteBitReqData data = new McDeviceRandomWriteBitReqData();
        data.setCommand(EMcCommand.DEVICE_ACCESS_RANDOM_WRITE_IN_UNITS);
        data.setSubcommand(series == EMcSeries.Q_L ? 0x0001 : 0x0003);
        data.setWordContents(wordContents);
        McMessageReq req = new McMessageReq(header, data);
        req.selfCheck();
        return req;
    }

    public static McMessageReq createDeviceBatchReadMultiBlocksReq(List<McDeviceAddress> wordAddresses,
                                                                   List<McDeviceAddress> bitAddresses) {
        return createDeviceBatchReadMultiBlocksReq(Mc4EFrameAccessRoute.createDefault(), MONITORING_TIMER_DEFAULT, wordAddresses, bitAddresses);
    }

    public static McMessageReq createDeviceBatchReadMultiBlocksReq(McAccessRoute accessRoute, int timer,
                                                             List<McDeviceAddress> wordAddresses,
                                                             List<McDeviceAddress> bitAddresses) {
        if (wordAddresses.isEmpty() && bitAddresses.isEmpty()) {
            throw new IllegalArgumentException("wordAddresses&&bitAddresses");
        }
        EMcSeries series = !wordAddresses.isEmpty() ? wordAddresses.get(0).getSeries() : bitAddresses.get(0).getSeries();
        McHeaderReq header = createMcHeaderReq4E(accessRoute, timer);
        McDeviceBatchReadMultiBlocksReqData data = new McDeviceBatchReadMultiBlocksReqData();
        data.setCommand(EMcCommand.DEVICE_ACCESS_BATCH_READ_MULTIPLE_BLOCKS);
        data.setSubcommand(series == EMcSeries.Q_L ? 0x0000 : 0x0002);
        data.setWordAddresses(wordAddresses);
        data.setBitAddresses(bitAddresses);
        McMessageReq req = new McMessageReq(header, data);
        req.selfCheck();
        return req;
    }

    public static McMessageReq createDeviceBatchWriteMultiBlocksReq(List<McDeviceContent> wordContents,
                                                                   List<McDeviceContent> bitContents) {
        return createDeviceBatchWriteMultiBlocksReq(Mc4EFrameAccessRoute.createDefault(), MONITORING_TIMER_DEFAULT, wordContents, bitContents);
    }

    public static McMessageReq createDeviceBatchWriteMultiBlocksReq(McAccessRoute accessRoute, int timer,
                                                                    List<McDeviceContent> wordContents,
                                                                    List<McDeviceContent> bitContents) {
        if (wordContents.isEmpty() && bitContents.isEmpty()) {
            throw new IllegalArgumentException("wordContents&&bitContents");
        }
        EMcSeries series = !wordContents.isEmpty() ? wordContents.get(0).getSeries() : bitContents.get(0).getSeries();
        McHeaderReq header = createMcHeaderReq4E(accessRoute, timer);
        McDeviceBatchWriteMultiBlocksReqData data = new McDeviceBatchWriteMultiBlocksReqData();
        data.setCommand(EMcCommand.DEVICE_ACCESS_BATCH_READ_MULTIPLE_BLOCKS);
        data.setSubcommand(series == EMcSeries.Q_L ? 0x0000 : 0x0002);
        data.setWordContents(wordContents);
        data.setBitContents(bitContents);
        McMessageReq req = new McMessageReq(header, data);
        req.selfCheck();
        return req;
    }
}

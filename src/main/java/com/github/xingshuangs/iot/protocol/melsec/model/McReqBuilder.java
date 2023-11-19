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

    public static McMessageReq createReadDeviceBatchInWordReq(McDeviceAddress deviceAddress) {
        return createReadDeviceBatchInWordReq(Mc4E3EFrameAccessRoute.createDefault(), MONITORING_TIMER_DEFAULT, deviceAddress);
    }

    public static McMessageReq createReadDeviceBatchInWordReq(int timer, McDeviceAddress deviceAddress) {
        return createReadDeviceBatchInWordReq(Mc4E3EFrameAccessRoute.createDefault(), timer, deviceAddress);
    }

    public static McMessageReq createReadDeviceBatchInWordReq(McAccessRoute accessRoute, int timer, McDeviceAddress deviceAddress) {
        McHeaderReq header = createMcHeaderReq4E(accessRoute, timer);
        McReadDeviceBatchReqData data = new McReadDeviceBatchReqData();
        data.setCommand(EMcCommand.DEVICE_ACCESS_BATCH_READ_IN_UNITS);
        data.setSubcommand(deviceAddress.getSeries() == EMcSeries.Q_L ? 0x0000 : 0x0002);
        data.setDeviceAddress(deviceAddress);
        McMessageReq req = new McMessageReq(header, data);
        req.selfCheck();
        return req;
    }

    public static McMessageReq createReadDeviceBatchInBitReq(McDeviceAddress deviceAddress) {
        return createReadDeviceBatchInBitReq(Mc4E3EFrameAccessRoute.createDefault(), MONITORING_TIMER_DEFAULT, deviceAddress);
    }

    public static McMessageReq createReadDeviceBatchInBitReq(McAccessRoute accessRoute, int timer, McDeviceAddress deviceAddress) {
        McHeaderReq header = createMcHeaderReq4E(accessRoute, timer);
        McReadDeviceBatchReqData data = new McReadDeviceBatchReqData();
        data.setCommand(EMcCommand.DEVICE_ACCESS_BATCH_READ_IN_UNITS);
        data.setSubcommand(deviceAddress.getSeries() == EMcSeries.Q_L ? 0x0001 : 0x0003);
        data.setDeviceAddress(deviceAddress);
        McMessageReq req = new McMessageReq(header, data);
        req.selfCheck();
        return req;
    }

    public static McMessageReq createWriteDeviceBatchInWordReq(McDeviceContent deviceContent) {
        return createWriteDeviceBatchInWordReq(Mc4E3EFrameAccessRoute.createDefault(), MONITORING_TIMER_DEFAULT, deviceContent);
    }

    public static McMessageReq createWriteDeviceBatchInWordReq(McAccessRoute accessRoute, int timer, McDeviceContent deviceContent) {
        McHeaderReq header = createMcHeaderReq4E(accessRoute, timer);
        McWriteDeviceBatchReqData data = new McWriteDeviceBatchReqData();
        data.setCommand(EMcCommand.DEVICE_ACCESS_BATCH_WRITE_IN_UNITS);
        data.setSubcommand(deviceContent.getSeries() == EMcSeries.Q_L ? 0x0000 : 0x0002);
        data.setDeviceContent(deviceContent);
        McMessageReq req = new McMessageReq(header, data);
        req.selfCheck();
        return req;
    }

    public static McMessageReq createWriteDeviceBatchInBitReq(McDeviceContent deviceContent) {
        return createWriteDeviceBatchInBitReq(Mc4E3EFrameAccessRoute.createDefault(), MONITORING_TIMER_DEFAULT, deviceContent);
    }

    public static McMessageReq createWriteDeviceBatchInBitReq(McAccessRoute accessRoute, int timer, McDeviceContent deviceContent) {
        McHeaderReq header = createMcHeaderReq4E(accessRoute, timer);
        McWriteDeviceBatchReqData data = new McWriteDeviceBatchReqData();
        data.setCommand(EMcCommand.DEVICE_ACCESS_BATCH_WRITE_IN_UNITS);
        data.setSubcommand(deviceContent.getSeries() == EMcSeries.Q_L ? 0x0001 : 0x0003);
        data.setDeviceContent(deviceContent);
        McMessageReq req = new McMessageReq(header, data);
        req.selfCheck();
        return req;
    }

    public static McMessageReq createReadDeviceRandomInWordReq(List<McDeviceAddress> wordAddresses,
                                                               List<McDeviceAddress> dwordAddresses) {
        return createReadDeviceRandomInWordReq(Mc4E3EFrameAccessRoute.createDefault(), MONITORING_TIMER_DEFAULT, wordAddresses, dwordAddresses);
    }

    public static McMessageReq createReadDeviceRandomInWordReq(McAccessRoute accessRoute, int timer,
                                                               List<McDeviceAddress> wordAddresses,
                                                               List<McDeviceAddress> dwordAddresses) {
        if (wordAddresses.isEmpty() && dwordAddresses.isEmpty()) {
            throw new IllegalArgumentException("wordAddresses&&dwordAddresses");
        }
        EMcSeries series = !wordAddresses.isEmpty() ? wordAddresses.get(0).getSeries() : dwordAddresses.get(0).getSeries();
        McHeaderReq header = createMcHeaderReq4E(accessRoute, timer);
        McReadDeviceRandomInWordReqData data = new McReadDeviceRandomInWordReqData();
        data.setCommand(EMcCommand.DEVICE_ACCESS_RANDOM_READ_IN_UNITS);
        data.setSubcommand(series == EMcSeries.Q_L ? 0x0000 : 0x0002);
        data.setWordAddresses(wordAddresses);
        data.setDwordAddresses(dwordAddresses);
        McMessageReq req = new McMessageReq(header, data);
        req.selfCheck();
        return req;
    }

    public static McMessageReq createWriteDeviceRandomInWordReq(List<McDeviceContent> wordContents,
                                                                List<McDeviceContent> dwordContents) {
        return createWriteDeviceRandomInWordReq(Mc4E3EFrameAccessRoute.createDefault(), MONITORING_TIMER_DEFAULT, wordContents, dwordContents);
    }

    public static McMessageReq createWriteDeviceRandomInWordReq(McAccessRoute accessRoute, int timer,
                                                                List<McDeviceContent> wordContents,
                                                                List<McDeviceContent> dwordContents) {
        if (wordContents.isEmpty() && dwordContents.isEmpty()) {
            throw new IllegalArgumentException("wordContents&&dwordContents");
        }
        EMcSeries series = !wordContents.isEmpty() ? wordContents.get(0).getSeries() : dwordContents.get(0).getSeries();
        McHeaderReq header = createMcHeaderReq4E(accessRoute, timer);
        McWriteDeviceRandomInWordReqData data = new McWriteDeviceRandomInWordReqData();
        data.setCommand(EMcCommand.DEVICE_ACCESS_RANDOM_WRITE_IN_UNITS);
        data.setSubcommand(series == EMcSeries.Q_L ? 0x0000 : 0x0002);
        data.setWordContents(wordContents);
        data.setDwordContents(dwordContents);
        McMessageReq req = new McMessageReq(header, data);
        req.selfCheck();
        return req;
    }

    public static McMessageReq createWriteDeviceRandomInBitReq(List<McDeviceContent> bitAddresses) {
        return createWriteDeviceRandomInBitReq(Mc4E3EFrameAccessRoute.createDefault(), MONITORING_TIMER_DEFAULT, bitAddresses);
    }

    public static McMessageReq createWriteDeviceRandomInBitReq(McAccessRoute accessRoute, int timer, List<McDeviceContent> bitContents) {
        EMcSeries series = !bitContents.isEmpty() ? bitContents.get(0).getSeries() : EMcSeries.Q_L;
        McHeaderReq header = createMcHeaderReq4E(accessRoute, timer);
        McWriteDeviceRandomInBitReqData data = new McWriteDeviceRandomInBitReqData();
        data.setCommand(EMcCommand.DEVICE_ACCESS_RANDOM_WRITE_IN_UNITS);
        data.setSubcommand(series == EMcSeries.Q_L ? 0x0001 : 0x0003);
        data.setBitContents(bitContents);
        McMessageReq req = new McMessageReq(header, data);
        req.selfCheck();
        return req;
    }

    public static McMessageReq createReadDeviceBatchMultiBlocksReq(List<McDeviceAddress> wordAddresses,
                                                                   List<McDeviceAddress> bitAddresses) {
        return createReadDeviceBatchMultiBlocksReq(Mc4E3EFrameAccessRoute.createDefault(), MONITORING_TIMER_DEFAULT, wordAddresses, bitAddresses);
    }

    public static McMessageReq createReadDeviceBatchMultiBlocksReq(McAccessRoute accessRoute, int timer,
                                                                   List<McDeviceAddress> wordAddresses,
                                                                   List<McDeviceAddress> bitAddresses) {
        if (wordAddresses.isEmpty() && bitAddresses.isEmpty()) {
            throw new IllegalArgumentException("wordAddresses&&bitAddresses");
        }
        EMcSeries series = !wordAddresses.isEmpty() ? wordAddresses.get(0).getSeries() : bitAddresses.get(0).getSeries();
        McHeaderReq header = createMcHeaderReq4E(accessRoute, timer);
        McReadDeviceBatchMultiBlocksReqData data = new McReadDeviceBatchMultiBlocksReqData();
        data.setCommand(EMcCommand.DEVICE_ACCESS_BATCH_READ_MULTIPLE_BLOCKS);
        data.setSubcommand(series == EMcSeries.Q_L ? 0x0000 : 0x0002);
        data.setWordAddresses(wordAddresses);
        data.setBitAddresses(bitAddresses);
        McMessageReq req = new McMessageReq(header, data);
        req.selfCheck();
        return req;
    }

    public static McMessageReq createWriteDeviceBatchMultiBlocksReq(List<McDeviceContent> wordContents,
                                                                    List<McDeviceContent> bitContents) {
        return createWriteDeviceBatchMultiBlocksReq(Mc4E3EFrameAccessRoute.createDefault(), MONITORING_TIMER_DEFAULT, wordContents, bitContents);
    }

    public static McMessageReq createWriteDeviceBatchMultiBlocksReq(McAccessRoute accessRoute, int timer,
                                                                    List<McDeviceContent> wordContents,
                                                                    List<McDeviceContent> bitContents) {
        if (wordContents.isEmpty() && bitContents.isEmpty()) {
            throw new IllegalArgumentException("wordContents&&bitContents");
        }
        EMcSeries series = !wordContents.isEmpty() ? wordContents.get(0).getSeries() : bitContents.get(0).getSeries();
        McHeaderReq header = createMcHeaderReq4E(accessRoute, timer);
        McWriteDeviceBatchMultiBlocksReqData data = new McWriteDeviceBatchMultiBlocksReqData();
        data.setCommand(EMcCommand.DEVICE_ACCESS_BATCH_WRITE_MULTIPLE_BLOCKS);
        data.setSubcommand(series == EMcSeries.Q_L ? 0x0000 : 0x0002);
        data.setWordContents(wordContents);
        data.setBitContents(bitContents);
        McMessageReq req = new McMessageReq(header, data);
        req.selfCheck();
        return req;
    }
}

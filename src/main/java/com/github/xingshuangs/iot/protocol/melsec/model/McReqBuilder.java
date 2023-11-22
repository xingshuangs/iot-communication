package com.github.xingshuangs.iot.protocol.melsec.model;


import com.github.xingshuangs.iot.protocol.melsec.enums.EMcCommand;
import com.github.xingshuangs.iot.protocol.melsec.enums.EMcSeries;
import com.github.xingshuangs.iot.protocol.melsec.enums.EMcFrameType;

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
        McHeaderReq mcHeaderReq = new McHeaderReq(EMcFrameType.FRAME_4E.getReqSubHeader(), Mc4E3EFrameAccessRoute.createDefault(), MONITORING_TIMER_DEFAULT);
        return createReadDeviceBatchInWordReq(mcHeaderReq, deviceAddress);
    }

    public static McMessageReq createReadDeviceBatchInWordReq(McHeaderReq header, McDeviceAddress deviceAddress) {
        McReadDeviceBatchReqData data = new McReadDeviceBatchReqData();
        data.setCommand(EMcCommand.DEVICE_ACCESS_BATCH_READ_IN_UNITS);
        data.setSubcommand(deviceAddress.getSeries() == EMcSeries.Q_L ? 0x0000 : 0x0002);
        data.setDeviceAddress(deviceAddress);
        McMessageReq req = new McMessageReq(header, data);
        req.selfCheck();
        return req;
    }

    public static McMessageReq createReadDeviceBatchInBitReq(McDeviceAddress deviceAddress) {
        McHeaderReq mcHeaderReq = new McHeaderReq(EMcFrameType.FRAME_4E.getReqSubHeader(), Mc4E3EFrameAccessRoute.createDefault(), MONITORING_TIMER_DEFAULT);
        return createReadDeviceBatchInBitReq(mcHeaderReq, deviceAddress);
    }

    public static McMessageReq createReadDeviceBatchInBitReq(McHeaderReq header, McDeviceAddress deviceAddress) {
        McReadDeviceBatchReqData data = new McReadDeviceBatchReqData();
        data.setCommand(EMcCommand.DEVICE_ACCESS_BATCH_READ_IN_UNITS);
        data.setSubcommand(deviceAddress.getSeries() == EMcSeries.Q_L ? 0x0001 : 0x0003);
        data.setDeviceAddress(deviceAddress);
        McMessageReq req = new McMessageReq(header, data);
        req.selfCheck();
        return req;
    }

    public static McMessageReq createWriteDeviceBatchInWordReq(McDeviceContent deviceContent) {
        McHeaderReq mcHeaderReq = new McHeaderReq(EMcFrameType.FRAME_4E.getReqSubHeader(), Mc4E3EFrameAccessRoute.createDefault(), MONITORING_TIMER_DEFAULT);
        return createWriteDeviceBatchInWordReq(mcHeaderReq, deviceContent);
    }

    public static McMessageReq createWriteDeviceBatchInWordReq(McHeaderReq header, McDeviceContent deviceContent) {
        McWriteDeviceBatchReqData data = new McWriteDeviceBatchReqData();
        data.setCommand(EMcCommand.DEVICE_ACCESS_BATCH_WRITE_IN_UNITS);
        data.setSubcommand(deviceContent.getSeries() == EMcSeries.Q_L ? 0x0000 : 0x0002);
        data.setDeviceContent(deviceContent);
        McMessageReq req = new McMessageReq(header, data);
        req.selfCheck();
        return req;
    }

    public static McMessageReq createWriteDeviceBatchInBitReq(McDeviceContent deviceContent) {
        McHeaderReq mcHeaderReq = new McHeaderReq(EMcFrameType.FRAME_4E.getReqSubHeader(), Mc4E3EFrameAccessRoute.createDefault(), MONITORING_TIMER_DEFAULT);
        return createWriteDeviceBatchInBitReq(mcHeaderReq, deviceContent);
    }

    public static McMessageReq createWriteDeviceBatchInBitReq(McHeaderReq header, McDeviceContent deviceContent) {
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
        McHeaderReq mcHeaderReq = new McHeaderReq(EMcFrameType.FRAME_4E.getReqSubHeader(), Mc4E3EFrameAccessRoute.createDefault(), MONITORING_TIMER_DEFAULT);
        return createReadDeviceRandomInWordReq(mcHeaderReq, wordAddresses, dwordAddresses);
    }

    public static McMessageReq createReadDeviceRandomInWordReq(McHeaderReq header,
                                                               List<McDeviceAddress> wordAddresses,
                                                               List<McDeviceAddress> dwordAddresses) {
        if (wordAddresses.isEmpty() && dwordAddresses.isEmpty()) {
            throw new IllegalArgumentException("wordAddresses&&dwordAddresses");
        }
        EMcSeries series = !wordAddresses.isEmpty() ? wordAddresses.get(0).getSeries() : dwordAddresses.get(0).getSeries();
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
        McHeaderReq mcHeaderReq = new McHeaderReq(EMcFrameType.FRAME_4E.getReqSubHeader(), Mc4E3EFrameAccessRoute.createDefault(), MONITORING_TIMER_DEFAULT);
        return createWriteDeviceRandomInWordReq(mcHeaderReq, wordContents, dwordContents);
    }

    public static McMessageReq createWriteDeviceRandomInWordReq(McHeaderReq header,
                                                                List<McDeviceContent> wordContents,
                                                                List<McDeviceContent> dwordContents) {
        if (wordContents.isEmpty() && dwordContents.isEmpty()) {
            throw new IllegalArgumentException("wordContents&&dwordContents");
        }
        EMcSeries series = !wordContents.isEmpty() ? wordContents.get(0).getSeries() : dwordContents.get(0).getSeries();
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
        McHeaderReq mcHeaderReq = new McHeaderReq(EMcFrameType.FRAME_4E.getReqSubHeader(), Mc4E3EFrameAccessRoute.createDefault(), MONITORING_TIMER_DEFAULT);
        return createWriteDeviceRandomInBitReq(mcHeaderReq, bitAddresses);
    }

    public static McMessageReq createWriteDeviceRandomInBitReq(McHeaderReq header, List<McDeviceContent> bitContents) {
        EMcSeries series = !bitContents.isEmpty() ? bitContents.get(0).getSeries() : EMcSeries.Q_L;
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
        McHeaderReq mcHeaderReq = new McHeaderReq(EMcFrameType.FRAME_4E.getReqSubHeader(), Mc4E3EFrameAccessRoute.createDefault(), MONITORING_TIMER_DEFAULT);
        return createReadDeviceBatchMultiBlocksReq(mcHeaderReq, wordAddresses, bitAddresses);
    }

    public static McMessageReq createReadDeviceBatchMultiBlocksReq(McHeaderReq header,
                                                                   List<McDeviceAddress> wordAddresses,
                                                                   List<McDeviceAddress> bitAddresses) {
        if (wordAddresses.isEmpty() && bitAddresses.isEmpty()) {
            throw new IllegalArgumentException("wordAddresses&&bitAddresses");
        }
        EMcSeries series = !wordAddresses.isEmpty() ? wordAddresses.get(0).getSeries() : bitAddresses.get(0).getSeries();
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
        McHeaderReq mcHeaderReq = new McHeaderReq(EMcFrameType.FRAME_4E.getReqSubHeader(), Mc4E3EFrameAccessRoute.createDefault(), MONITORING_TIMER_DEFAULT);
        return createWriteDeviceBatchMultiBlocksReq(mcHeaderReq, wordContents, bitContents);
    }

    public static McMessageReq createWriteDeviceBatchMultiBlocksReq(McHeaderReq header,
                                                                    List<McDeviceContent> wordContents,
                                                                    List<McDeviceContent> bitContents) {
        if (wordContents.isEmpty() && bitContents.isEmpty()) {
            throw new IllegalArgumentException("wordContents&&bitContents");
        }
        EMcSeries series = !wordContents.isEmpty() ? wordContents.get(0).getSeries() : bitContents.get(0).getSeries();
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

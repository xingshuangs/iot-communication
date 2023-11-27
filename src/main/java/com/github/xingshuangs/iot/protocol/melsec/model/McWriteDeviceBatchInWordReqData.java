package com.github.xingshuangs.iot.protocol.melsec.model;


import com.github.xingshuangs.iot.protocol.common.buff.ByteWriteBuff;
import com.github.xingshuangs.iot.protocol.melsec.enums.EMcCommand;
import com.github.xingshuangs.iot.protocol.melsec.enums.EMcSeries;
import lombok.Data;

/**
 * 软元件访问批量写请求数据，字单位
 *
 * @author xingshuang
 */
@Data
public class McWriteDeviceBatchInWordReqData extends McWriteDeviceBatchReqData {

    public McWriteDeviceBatchInWordReqData() {
        this(EMcSeries.Q_L, new McDeviceContent());
    }

    public McWriteDeviceBatchInWordReqData(EMcSeries series) {
        this(series, new McDeviceContent());
    }

    public McWriteDeviceBatchInWordReqData(EMcSeries series, McDeviceContent deviceContent) {
        this.series = series;
        this.command = EMcCommand.DEVICE_ACCESS_BATCH_WRITE_IN_UNITS;
        this.subcommand = series == EMcSeries.Q_L ? 0x0000 : 0x0002;
        this.deviceContent = deviceContent;
    }
}

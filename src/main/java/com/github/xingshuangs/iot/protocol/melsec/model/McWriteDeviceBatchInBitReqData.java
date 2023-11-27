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
public class McWriteDeviceBatchInBitReqData extends McWriteDeviceBatchReqData {

    public McWriteDeviceBatchInBitReqData() {
        this(EMcSeries.Q_L, new McDeviceContent());
    }

    public McWriteDeviceBatchInBitReqData(EMcSeries series) {
        this(series, new McDeviceContent());
    }

    public McWriteDeviceBatchInBitReqData(EMcSeries series, McDeviceContent deviceContent) {
        this.series = series;
        this.command = EMcCommand.DEVICE_ACCESS_BATCH_WRITE_IN_UNITS;
        this.subcommand = series == EMcSeries.Q_L ? 0x0001 : 0x0003;
        this.deviceContent = deviceContent;
    }
}

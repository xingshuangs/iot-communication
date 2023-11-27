package com.github.xingshuangs.iot.protocol.melsec.model;


import com.github.xingshuangs.iot.protocol.melsec.enums.EMcCommand;
import com.github.xingshuangs.iot.protocol.melsec.enums.EMcSeries;
import lombok.Data;

/**
 * 软元件访问批量读请求数据，位单位
 *
 * @author xingshuang
 */
@Data
public class McReadDeviceBatchInBitReqData extends McReadDeviceBatchReqData {

    public McReadDeviceBatchInBitReqData() {
        this(EMcSeries.Q_L, new McDeviceAddress());
    }

    public McReadDeviceBatchInBitReqData(EMcSeries series) {
        this(series, new McDeviceAddress());
    }

    public McReadDeviceBatchInBitReqData(EMcSeries series, McDeviceAddress deviceAddress) {
        this.series = series;
        this.command = EMcCommand.DEVICE_ACCESS_BATCH_READ_IN_UNITS;
        this.subcommand = series == EMcSeries.Q_L ? 0x0001 : 0x0003;
        this.deviceAddress = deviceAddress;
    }
}

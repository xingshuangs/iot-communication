package com.github.xingshuangs.iot.protocol.melsec.model;


import com.github.xingshuangs.iot.protocol.common.buff.ByteWriteBuff;
import com.github.xingshuangs.iot.protocol.melsec.enums.EMcCommand;
import com.github.xingshuangs.iot.protocol.melsec.enums.EMcSeries;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 软元件访问随机写请求数据，位单位
 *
 * @author xingshuang
 */
@Data
public class McWriteDeviceRandomInBitReqData extends McReqData {

    /**
     * 软元件设备内容，位访问地址列表
     */
    private List<McDeviceContent> bitContents;

    public McWriteDeviceRandomInBitReqData() {
        this(EMcSeries.Q_L, new ArrayList<>());
    }

    public McWriteDeviceRandomInBitReqData(EMcSeries series) {
        this(series, new ArrayList<>());
    }

    public McWriteDeviceRandomInBitReqData(EMcSeries series, List<McDeviceContent> bitContents) {
        this.series = series;
        this.command = EMcCommand.DEVICE_ACCESS_RANDOM_WRITE_IN_UNITS;
        this.subcommand = series == EMcSeries.Q_L ? 0x0001 : 0x0003;
        this.bitContents = bitContents;
    }

    @Override
    public int byteArrayLength() {
        return 4 + 1 + this.bitContents.stream().mapToInt(x -> x.byteArrayLengthWithoutPointsCount(this.series)).sum();
    }

    @Override
    public byte[] toByteArray() {
        ByteWriteBuff buff = ByteWriteBuff.newInstance(this.byteArrayLength(), true)
                .putShort(this.command.getCode())
                .putShort(this.subcommand)
                .putByte(this.bitContents.size());
        this.bitContents.forEach(x -> buff.putBytes(x.toByteArrayWithoutPointsCount(this.series)));
        return buff.getData();
    }
}

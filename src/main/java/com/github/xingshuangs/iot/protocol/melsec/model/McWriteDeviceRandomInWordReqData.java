package com.github.xingshuangs.iot.protocol.melsec.model;


import com.github.xingshuangs.iot.protocol.common.buff.ByteWriteBuff;
import com.github.xingshuangs.iot.protocol.melsec.enums.EMcCommand;
import com.github.xingshuangs.iot.protocol.melsec.enums.EMcSeries;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 软元件访问随机写请求数据，字单位
 *
 * @author xingshuang
 */
@Data
public class McWriteDeviceRandomInWordReqData extends McReqData {

    /**
     * 软元件设备内容，字访问地址列表
     */
    private List<McDeviceContent> wordContents;

    /**
     * 软元件设备内容，双字访问地址列表
     */
    private List<McDeviceContent> dwordContents;

    public McWriteDeviceRandomInWordReqData() {
        this(EMcSeries.Q_L, new ArrayList<>(), new ArrayList<>());
    }

    public McWriteDeviceRandomInWordReqData(EMcSeries series) {
        this(series, new ArrayList<>(), new ArrayList<>());
    }

    public McWriteDeviceRandomInWordReqData(EMcSeries series,
                                            List<McDeviceContent> wordContents,
                                            List<McDeviceContent> dwordContents) {
        this.series = series;
        this.command = EMcCommand.DEVICE_ACCESS_RANDOM_WRITE_IN_UNITS;
        this.subcommand = series == EMcSeries.Q_L ? 0x0000 : 0x0002;
        this.wordContents = wordContents;
        this.dwordContents = dwordContents;
    }

    @Override
    public int byteArrayLength() {
        return 4 + 2 + this.wordContents.stream().mapToInt(x -> x.byteArrayLengthWithoutPointsCount(this.series)).sum()
                + this.dwordContents.stream().mapToInt(x -> x.byteArrayLengthWithoutPointsCount(this.series)).sum();
    }

    @Override
    public byte[] toByteArray() {
        ByteWriteBuff buff = ByteWriteBuff.newInstance(this.byteArrayLength(), true)
                .putShort(this.command.getCode())
                .putShort(this.subcommand)
                .putByte(this.wordContents.size())
                .putByte(this.dwordContents.size());
        this.wordContents.forEach(x -> buff.putBytes(x.toByteArrayWithoutPointsCount(this.series)));
        this.dwordContents.forEach(x -> buff.putBytes(x.toByteArrayWithoutPointsCount(this.series)));
        return buff.getData();
    }
}

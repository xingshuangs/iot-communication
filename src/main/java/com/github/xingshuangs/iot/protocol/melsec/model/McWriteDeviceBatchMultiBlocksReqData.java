package com.github.xingshuangs.iot.protocol.melsec.model;


import com.github.xingshuangs.iot.protocol.common.buff.ByteWriteBuff;
import com.github.xingshuangs.iot.protocol.melsec.enums.EMcCommand;
import com.github.xingshuangs.iot.protocol.melsec.enums.EMcSeries;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 软元件访问多块读请求数据
 *
 * @author xingshuang
 */
@Data
public class McWriteDeviceBatchMultiBlocksReqData extends McReqData {

    /**
     * 软元件设备地址，字访问地址列表
     */
    private List<McDeviceContent> wordContents;

    /**
     * 软元件设备地址，位访问地址列表
     */
    private List<McDeviceContent> bitContents;

    public McWriteDeviceBatchMultiBlocksReqData() {
        this(EMcSeries.Q_L, new ArrayList<>(), new ArrayList<>());
    }

    public McWriteDeviceBatchMultiBlocksReqData(EMcSeries series) {
        this(series, new ArrayList<>(), new ArrayList<>());
    }

    public McWriteDeviceBatchMultiBlocksReqData(EMcSeries series,
                                                List<McDeviceContent> wordContents,
                                                List<McDeviceContent> bitContents) {
        this.series = series;
        this.command = EMcCommand.DEVICE_ACCESS_BATCH_WRITE_MULTIPLE_BLOCKS;
        this.subcommand = series == EMcSeries.Q_L ? 0x0000 : 0x0002;
        this.wordContents = wordContents;
        this.bitContents = bitContents;
    }

    @Override
    public int byteArrayLength() {
        return 4 + 2 + this.wordContents.stream().mapToInt(x -> x.byteArrayLengthWithPointsCount(this.series)).sum()
                + this.bitContents.stream().mapToInt(x -> x.byteArrayLengthWithPointsCount(this.series)).sum();
    }

    @Override
    public byte[] toByteArray() {
        ByteWriteBuff buff = ByteWriteBuff.newInstance(this.byteArrayLength(), true)
                .putShort(this.command.getCode())
                .putShort(this.subcommand)
                .putByte(this.wordContents.size())
                .putByte(this.bitContents.size());
        this.wordContents.forEach(x -> buff.putBytes(x.toByteArrayWithPointsCount(this.series)));
        this.bitContents.forEach(x -> buff.putBytes(x.toByteArrayWithPointsCount(this.series)));
        return buff.getData();
    }
}

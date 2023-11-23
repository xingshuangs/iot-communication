package com.github.xingshuangs.iot.protocol.melsec.model;


import com.github.xingshuangs.iot.protocol.common.buff.ByteWriteBuff;
import com.github.xingshuangs.iot.protocol.melsec.enums.EMcCommand;
import com.github.xingshuangs.iot.protocol.melsec.enums.EMcSeries;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 软元件访问随机读请求数据，字单位
 *
 * @author xingshuang
 */
@Data
public class McReadDeviceRandomInWordReqData extends McReqData {

    /**
     * 软元件设备地址，字访问地址列表
     */
    private List<McDeviceAddress> wordAddresses;

    /**
     * 软元件设备地址，双字访问地址列表
     */
    private List<McDeviceAddress> dwordAddresses;

    public McReadDeviceRandomInWordReqData() {
        this(EMcSeries.Q_L, new ArrayList<>(), new ArrayList<>());
    }

    public McReadDeviceRandomInWordReqData(EMcSeries series) {
        this(series, new ArrayList<>(), new ArrayList<>());
    }

    public McReadDeviceRandomInWordReqData(EMcSeries series,
                                           List<McDeviceAddress> wordAddresses,
                                           List<McDeviceAddress> dwordAddresses) {
        this.series = series;
        this.command = EMcCommand.DEVICE_ACCESS_RANDOM_READ_IN_UNITS;
        this.subcommand = series == EMcSeries.Q_L ? 0x0000 : 0x0002;
        this.wordAddresses = wordAddresses;
        this.dwordAddresses = dwordAddresses;
    }

    @Override
    public int byteArrayLength() {
        return 4 + 2 + this.wordAddresses.stream().mapToInt(x -> x.byteArrayLengthWithoutPointsCount(this.series)).sum()
                + this.dwordAddresses.stream().mapToInt(x -> x.byteArrayLengthWithoutPointsCount(this.series)).sum();
    }

    @Override
    public byte[] toByteArray() {
        ByteWriteBuff buff = ByteWriteBuff.newInstance(this.byteArrayLength(), true)
                .putShort(this.command.getCode())
                .putShort(this.subcommand)
                .putByte(this.wordAddresses.size())
                .putByte(this.dwordAddresses.size());
        this.wordAddresses.forEach(x -> buff.putBytes(x.toByteArrayWithoutPointsCount(this.series)));
        this.dwordAddresses.forEach(x -> buff.putBytes(x.toByteArrayWithoutPointsCount(this.series)));
        return buff.getData();
    }
}

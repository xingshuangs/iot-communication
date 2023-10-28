package com.github.xingshuangs.iot.protocol.melsec.model;


import com.github.xingshuangs.iot.protocol.common.buff.ByteWriteBuff;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 软元件访问随机读请求数据，字单位
 *
 * @author xingshuang
 */
@Data
public class McDeviceRandomReadWordReqData extends McReqData {

    /**
     * 软元件设备地址，字访问地址列表
     */
    private List<McDeviceAddress> wordAddresses;

    /**
     * 软元件设备地址，双字访问地址列表
     */
    private List<McDeviceAddress> dwordAddresses;

    public McDeviceRandomReadWordReqData() {
        this.wordAddresses = new ArrayList<>();
        this.dwordAddresses = new ArrayList<>();
    }

    @Override
    public int byteArrayLength() {
        return 4 + 2 + this.wordAddresses.stream().mapToInt(McDeviceAddress::byteArrayLengthWithoutPointsCount).sum()
                + this.dwordAddresses.stream().mapToInt(McDeviceAddress::byteArrayLengthWithoutPointsCount).sum();
    }

    @Override
    public byte[] toByteArray() {
        ByteWriteBuff buff = ByteWriteBuff.newInstance(this.byteArrayLength(), true)
                .putShort(this.command.getCode())
                .putShort(this.subcommand)
                .putByte(this.wordAddresses.size())
                .putByte(this.dwordAddresses.size());
        this.wordAddresses.forEach(x -> buff.putBytes(x.toByteArrayWithoutPointsCount()));
        this.dwordAddresses.forEach(x -> buff.putBytes(x.toByteArrayWithoutPointsCount()));
        return buff.getData();
    }
}

package com.github.xingshuangs.iot.protocol.melsec.model;


import com.github.xingshuangs.iot.protocol.common.buff.ByteWriteBuff;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 软元件访问多块读请求数据
 *
 * @author xingshuang
 */
@Data
public class McReadDeviceBatchMultiBlocksReqData extends McReqData {

    /**
     * 软元件设备地址，字访问地址列表
     */
    private List<McDeviceAddress> wordAddresses;

    /**
     * 软元件设备地址，位访问地址列表
     */
    private List<McDeviceAddress> bitAddresses;

    public McReadDeviceBatchMultiBlocksReqData() {
        this.wordAddresses = new ArrayList<>();
        this.bitAddresses = new ArrayList<>();
    }

    @Override
    public int byteArrayLength() {
        return 4 + 2 + this.wordAddresses.stream().mapToInt(McDeviceAddress::byteArrayLengthWithPointsCount).sum()
                + this.bitAddresses.stream().mapToInt(McDeviceAddress::byteArrayLengthWithPointsCount).sum();
    }

    @Override
    public byte[] toByteArray() {
        ByteWriteBuff buff = ByteWriteBuff.newInstance(this.byteArrayLength(), true)
                .putShort(this.command.getCode())
                .putShort(this.subcommand)
                .putByte(this.wordAddresses.size())
                .putByte(this.bitAddresses.size());
        this.wordAddresses.forEach(x -> buff.putBytes(x.toByteArrayWithPointsCount()));
        this.bitAddresses.forEach(x -> buff.putBytes(x.toByteArrayWithPointsCount()));
        return buff.getData();
    }
}

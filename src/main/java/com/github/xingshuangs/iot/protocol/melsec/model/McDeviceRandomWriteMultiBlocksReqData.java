package com.github.xingshuangs.iot.protocol.melsec.model;


import com.github.xingshuangs.iot.protocol.common.buff.ByteWriteBuff;
import lombok.Data;

import java.util.List;

/**
 * 软元件访问多块读请求数据
 *
 * @author xingshuang
 */
@Data
public class McDeviceRandomWriteMultiBlocksReqData extends McReqData {

    /**
     * 软元件设备地址，字访问地址列表
     */
    private List<McDeviceContent> wordContents;

    /**
     * 软元件设备地址，位访问地址列表
     */
    private List<McDeviceContent> bitContents;

    @Override
    public int byteArrayLength() {
        return 4 + 2 + this.wordContents.stream().mapToInt(McDeviceContent::byteArrayLengthWithPointsCount).sum()
                + this.bitContents.stream().mapToInt(McDeviceContent::byteArrayLengthWithPointsCount).sum();
    }

    @Override
    public byte[] toByteArray() {
        ByteWriteBuff buff = ByteWriteBuff.newInstance(this.byteArrayLength(), true)
                .putShort(this.command.getCode())
                .putShort(this.subcommand)
                .putByte(this.wordContents.size())
                .putByte(this.bitContents.size());
        this.wordContents.forEach(x -> buff.putBytes(x.toByteArrayWithPointsCount()));
        this.bitContents.forEach(x -> buff.putBytes(x.toByteArrayWithPointsCount()));
        return buff.getData();
    }
}

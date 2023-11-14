package com.github.xingshuangs.iot.protocol.melsec.model;


import com.github.xingshuangs.iot.protocol.common.buff.ByteWriteBuff;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 软元件访问随机写请求数据，位单位
 *
 * @author xingshuang
 */
@Data
public class McDeviceRandomWriteBitReqData extends McReqData {

    /**
     * 软元件设备内容，位访问地址列表
     */
    private List<McDeviceContent> bitContents;

    public McDeviceRandomWriteBitReqData() {
        this.bitContents = new ArrayList<>();
    }

    @Override
    public int byteArrayLength() {
        return 4 + 1 + this.bitContents.stream().mapToInt(McDeviceContent::byteArrayLengthWithoutPointsCount).sum();
    }

    @Override
    public byte[] toByteArray() {
        ByteWriteBuff buff = ByteWriteBuff.newInstance(this.byteArrayLength(), true)
                .putShort(this.command.getCode())
                .putShort(this.subcommand)
                .putByte(this.bitContents.size());
        this.bitContents.forEach(x -> buff.putBytes(x.toByteArrayWithoutPointsCount()));
        return buff.getData();
    }
}

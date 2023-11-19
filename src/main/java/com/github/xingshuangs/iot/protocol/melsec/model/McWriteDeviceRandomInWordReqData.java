package com.github.xingshuangs.iot.protocol.melsec.model;


import com.github.xingshuangs.iot.protocol.common.buff.ByteWriteBuff;
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
        this.wordContents = new ArrayList<>();
        this.dwordContents = new ArrayList<>();
    }

    @Override
    public int byteArrayLength() {
        return 4 + 2 + this.wordContents.stream().mapToInt(McDeviceContent::byteArrayLengthWithoutPointsCount).sum()
                + this.dwordContents.stream().mapToInt(McDeviceContent::byteArrayLengthWithoutPointsCount).sum();
    }

    @Override
    public byte[] toByteArray() {
        ByteWriteBuff buff = ByteWriteBuff.newInstance(this.byteArrayLength(), true)
                .putShort(this.command.getCode())
                .putShort(this.subcommand)
                .putByte(this.wordContents.size())
                .putByte(this.dwordContents.size());
        this.wordContents.forEach(x -> buff.putBytes(x.toByteArrayWithoutPointsCount()));
        this.dwordContents.forEach(x -> buff.putBytes(x.toByteArrayWithoutPointsCount()));
        return buff.getData();
    }
}

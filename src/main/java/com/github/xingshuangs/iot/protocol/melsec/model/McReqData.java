package com.github.xingshuangs.iot.protocol.melsec.model;


import com.github.xingshuangs.iot.protocol.melsec.enums.EMcCommand;
import lombok.Data;

/**
 * 协议体数据：公共请求数据
 *
 * @author xingshuang
 */
@Data
public class McReqData extends McData {

    /**
     * 指令，2个字节
     */
    protected EMcCommand command = EMcCommand.DEVICE_ACCESS_BATCH_READ_IN_UNITS;

    /**
     * 子指令，2个字节
     */
    protected int subcommand = 0x0000;

    @Override
    public int byteArrayLength() {
        return 0;
    }

    @Override
    public byte[] toByteArray() {
        return new byte[0];
    }
}

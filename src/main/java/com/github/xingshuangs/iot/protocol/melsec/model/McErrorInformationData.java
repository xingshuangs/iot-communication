package com.github.xingshuangs.iot.protocol.melsec.model;


import com.github.xingshuangs.iot.protocol.common.IObjectByteArray;
import lombok.Data;

/**
 * 协议体数据：错误信息
 *
 * @author xingshuang
 */
@Data
public class McErrorInformationData implements IObjectByteArray {

    private byte[] accessRoute;

    private byte[] command;

    private byte[] subcommand;

    @Override
    public int byteArrayLength() {
        return 0;
    }

    @Override
    public byte[] toByteArray() {
        return new byte[0];
    }
}

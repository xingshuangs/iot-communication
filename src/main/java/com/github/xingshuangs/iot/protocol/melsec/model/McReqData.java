package com.github.xingshuangs.iot.protocol.melsec.model;


import com.github.xingshuangs.iot.protocol.common.IObjectByteArray;
import com.github.xingshuangs.iot.protocol.melsec.enums.EMcCommand;

/**
 * 协议体数据：公共请求数据
 *
 * @author xingshuang
 */
public class McReqData implements IObjectByteArray {

    private EMcCommand command;

    private int subcommand;

    @Override
    public int byteArrayLength() {
        return 0;
    }

    @Override
    public byte[] toByteArray() {
        return new byte[0];
    }
}

package com.github.xingshuangs.iot.protocol.melsec.model;


import com.github.xingshuangs.iot.protocol.common.IObjectByteArray;
import com.github.xingshuangs.iot.protocol.melsec.enums.EMcCommand;
import com.github.xingshuangs.iot.protocol.melsec.enums.EMcSeries;
import lombok.Data;

/**
 * 协议体数据：响应数据
 *
 * @author xingshuang
 */
@Data
public class McAckData implements IObjectByteArray {

    /**
     * 数据内容
     */
    private byte[] data = new byte[0];

    @Override
    public int byteArrayLength() {
        return this.data.length;
    }

    @Override
    public byte[] toByteArray() {
        return this.data;
    }
}

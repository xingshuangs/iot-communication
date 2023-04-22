package com.github.xingshuangs.iot.protocol.rtcp.model;


import com.github.xingshuangs.iot.protocol.common.IObjectByteArray;
import com.github.xingshuangs.iot.protocol.common.buff.ByteWriteBuff;
import com.github.xingshuangs.iot.protocol.rtcp.enums.ERtcpSdesItemType;
import lombok.Data;

/**
 * @author xingshuang
 */
@Data
public class RtcpSdesItem implements IObjectByteArray {

    private ERtcpSdesItemType type;

    private int length;

    private String text = "";

    @Override
    public int byteArrayLength() {
        return 2 + this.text.length();
    }

    @Override
    public byte[] toByteArray() {
        return ByteWriteBuff.newInstance(2 + this.text.length())
                .putByte(this.type.getCode())
                .putByte(this.length)
                .putString(this.text)
                .getData();
    }
}

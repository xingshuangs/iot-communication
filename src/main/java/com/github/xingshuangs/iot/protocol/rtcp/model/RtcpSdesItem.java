package com.github.xingshuangs.iot.protocol.rtcp.model;


import com.github.xingshuangs.iot.protocol.common.IObjectByteArray;
import com.github.xingshuangs.iot.protocol.common.buff.ByteReadBuff;
import com.github.xingshuangs.iot.protocol.common.buff.ByteWriteBuff;
import com.github.xingshuangs.iot.protocol.rtcp.enums.ERtcpSdesItemType;
import lombok.Data;

/**
 * @author xingshuang
 */
@Data
public class RtcpSdesItem implements IObjectByteArray {

    private ERtcpSdesItemType type;

    /**
     * 文本长度
     */
    private int length;

    /**
     * 文本内容
     */
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

    /**
     * 字节数组数据解析
     *
     * @param data 字节数组数据
     * @return RtcpHeader
     */
    public static RtcpSdesItem fromBytes(final byte[] data) {
        return fromBytes(data, 0);
    }

    /**
     * 字节数组数据解析
     *
     * @param data   字节数组数据
     * @param offset 偏移量
     * @return RtcpHeader
     */
    public static RtcpSdesItem fromBytes(final byte[] data, final int offset) {
        if (data.length < 2) {
            throw new IndexOutOfBoundsException("解析RtcpSdesItem时，字节数组长度不够");
        }
        ByteReadBuff buff = new ByteReadBuff(data, offset);
        RtcpSdesItem res = new RtcpSdesItem();
        res.type = ERtcpSdesItemType.from(buff.getByte());
        res.length = buff.getByteToInt();
        res.text = buff.getString(res.length);
        return res;
    }
}

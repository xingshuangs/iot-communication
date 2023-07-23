package com.github.xingshuangs.iot.protocol.s7.model;


import com.github.xingshuangs.iot.exceptions.S7CommException;
import com.github.xingshuangs.iot.protocol.common.buff.ByteReadBuff;
import com.github.xingshuangs.iot.protocol.s7.enums.EMessageType;
import lombok.Data;

/**
 * 上传下载数据
 *
 * @author xingshuang
 */
@Data
public class UpDownloadDatum extends Datum {

    /**
     * 长度，2个字节
     */
    private int length = 0;

    /**
     * 未知，2个字节
     */
    private int unkonwnBytes = 0;

    /**
     * 数据部分
     */
    private byte[] data;

    /**
     * 字节数组数据解析
     *
     * @param data        字节数组数据
     * @param messageType 消息类型
     * @return UpDownloadDatum
     */
    public static UpDownloadDatum fromBytes(byte[] data, EMessageType messageType) {
        return fromBytes(data, 0, messageType);

    }

    /**
     * 字节数组数据解析
     *
     * @param data        字节数组数据
     * @param offset      偏移量
     * @param messageType 消息类型
     * @return UpDownloadDatum
     */
    public static UpDownloadDatum fromBytes(byte[] data, int offset, EMessageType messageType) {
        if (EMessageType.ACK_DATA != messageType) {
            throw new S7CommException("不是响应数据");
        }
        UpDownloadDatum res = new UpDownloadDatum();
        ByteReadBuff buff = new ByteReadBuff(data, offset);
        res.length = buff.getUInt16();
        res.unkonwnBytes = buff.getUInt16();
        res.data = buff.getBytes(res.length);
        return res;
    }
}

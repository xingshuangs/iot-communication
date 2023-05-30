package com.github.xingshuangs.iot.protocol.rtp.model.frame;


import com.github.xingshuangs.iot.protocol.common.buff.ByteWriteBuff;
import com.github.xingshuangs.iot.protocol.rtp.enums.EFrameType;
import com.github.xingshuangs.iot.protocol.rtp.enums.EH264NaluType;
import com.github.xingshuangs.iot.protocol.rtp.model.payload.H264NaluBuilder;
import com.github.xingshuangs.iot.protocol.rtp.model.payload.H264NaluSingle;
import lombok.Data;

/**
 * 普通帧
 *
 * @author xingshuang
 */
@Data
public class H264VideoFrame extends RawFrame {

    /**
     * 起始分割符
     */
    public static final byte[] START_MARKER = new byte[]{0x00, 0x00, 0x00, 0x01};

    private final EH264NaluType naluType;

    public H264VideoFrame(EH264NaluType naluType, long timestamp, byte[] frameSegment) {
        this.frameType = EFrameType.VIDEO;
        this.naluType = naluType;
        this.timestamp = timestamp;
        this.frameSegment = frameSegment;
    }

    @Override
    public int byteArrayLength() {
        return 6 + this.frameSegment.length;
    }

    @Override
    public byte[] toByteArray() {
        return ByteWriteBuff.newInstance(this.byteArrayLength())
                .putByte(this.frameType.getCode())
                .putByte(this.naluType.getCode())
                .putInteger(this.timestamp)
                .putBytes(this.frameSegment)
                .getData();
    }

    /**
     * 创建
     *
     * @param frameSegment 帧字节数组
     * @return 视频帧
     */
    public static H264VideoFrame createSpsPpsFrame(byte[] frameSegment) {
        H264NaluSingle naluSingle = (H264NaluSingle) H264NaluBuilder.parsePackage(frameSegment);
        return new H264VideoFrame(naluSingle.getHeader().getType(), System.currentTimeMillis(), naluSingle.getPayload());
    }
}

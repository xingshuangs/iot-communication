package com.github.xingshuangs.iot.protocol.mp4.model;


import com.github.xingshuangs.iot.protocol.common.buff.ByteWriteBuff;
import com.github.xingshuangs.iot.protocol.mp4.enums.EMp4Type;

/**
 * Sample Description Box(stbl-stsd)，存放解码必须的描述信息,其也是一个container box，对于H264码流来说其包含avc1子box
 *
 * @author xingshuang
 */
public class Mp4Avc1Box extends Mp4Box {

    /**
     * 6个字节
     */
    private final byte[] reserved1;

    /**
     * 2字节
     */
    private final int dataReferenceIndex;

    /**
     * 2字节
     */
    private final byte[] preDefined1;

    /**
     * 2个字节
     */
    private final byte[] reserved2;

    /**
     * 12字节
     */
    private final byte[] preDefined2;

    /**
     * 2个字节，宽
     */
    private final int width;

    /**
     * 2个字节，高
     */
    private final int height;

    /**
     * 4个字节，水平分辨率，默认值即可
     */
    private final int horizResolution;

    /**
     * 4字节垂直分辨率，默认值即可
     */
    private final int vertResolution;

    /**
     * 4字节
     */
    private final byte[] reserved3;

    /**
     * 2字节帧数量，fmp4封装赋值为1
     */
    private final int frameCount;

    /**
     * 压缩名称，名称+长度=总共32个字节
     */
    private final byte[] compressNameInfo;

    /**
     * 2字节，深度，赋值为24
     */
    private final int depth;

    /**
     * 2字节
     */
    private final byte[] preDefined3;

    private final Mp4AvcCBox avcCBox;

    private final Mp4BtrtBox btrtBox;

    public Mp4Avc1Box(Mp4TrackInfo trackInfo) {
        this.mp4Type = EMp4Type.AVC1;
        this.reserved1 = new byte[6];
        this.dataReferenceIndex = 1;
        this.preDefined1 = new byte[2];
        this.reserved2 = new byte[2];
        this.preDefined2 = new byte[12];
        this.width = trackInfo.getWidth();
        this.height = trackInfo.getHeight();
        this.horizResolution = 4_718_592;
        this.vertResolution = 4_718_592;
        this.reserved3 = new byte[4];
        this.frameCount = 1;
        this.compressNameInfo = new byte[]{
                0x12,
                0x62, 0x69, 0x6E, 0x65, //binelpro.ru
                0x6C, 0x70, 0x72, 0x6F,
                0x2E, 0x72, 0x75, 0x00,
                0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, // compressorname
        };
        this.depth = 24;
        this.preDefined3 = new byte[]{0x11, 0x11};
        this.avcCBox = new Mp4AvcCBox(trackInfo);
        this.btrtBox = new Mp4BtrtBox();
    }

    @Override
    public int byteArrayLength() {
        return 78 + this.avcCBox.byteArrayLength() + this.btrtBox.byteArrayLength();
    }

    @Override
    public byte[] toByteArray() {
        int size = this.byteArrayLength();
        return ByteWriteBuff.newInstance(size)
                .putInteger(size)
                .putBytes(this.mp4Type.getByteArray())
                .putBytes(this.reserved1)
                .putShort(this.dataReferenceIndex)
                .putBytes(this.preDefined1)
                .putBytes(this.reserved2)
                .putBytes(this.preDefined2)
                .putShort(this.width)
                .putShort(this.height)
                .putInteger(this.horizResolution)
                .putInteger(this.vertResolution)
                .putBytes(this.reserved3)
                .putShort(this.frameCount)
                .putBytes(this.compressNameInfo)
                .putShort(this.depth)
                .putBytes(this.preDefined3)
                .putBytes(this.avcCBox.toByteArray())
                .putBytes(this.btrtBox.toByteArray())
                .getData();
    }
}

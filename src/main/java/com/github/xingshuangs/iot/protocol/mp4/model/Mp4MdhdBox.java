package com.github.xingshuangs.iot.protocol.mp4.model;


import com.github.xingshuangs.iot.protocol.common.buff.ByteWriteBuff;
import com.github.xingshuangs.iot.protocol.mp4.enums.EMp4Type;
import com.github.xingshuangs.iot.utils.TimesUtil;

import java.time.LocalDateTime;

/**
 * Media Header Box,存放视频流创建时间，长度等信息
 *
 * @author xingshuang
 */
public class Mp4MdhdBox extends Mp4Box {

    /**
     * 1字节，版本
     */
    private final int version;

    /**
     * 3字节为flags
     */
    private final byte[] flags;

    /**
     * 4字节创建时间
     */
    private final LocalDateTime creationTime;

    /**
     * 4字节修改时间
     */
    private final LocalDateTime modificationTime;

    /**
     * 4字节timescale
     */
    private final int timescale;

    /**
     * 4字节duration
     */
    private final int duration;

    /**
     * 2字节 language，支持的语言，ISO-639-2/T 语言代码
     */
    private final byte[] language;

    /**
     * 2字节pre_defined
     */
    private final byte[] preDefined;

    public Mp4MdhdBox(int timescale, int duration) {
        this.mp4Type = EMp4Type.MDHD;
        this.version = 0;
        this.flags = new byte[3];
        this.creationTime = TimesUtil.getUTCDateTime(2);
        this.modificationTime = TimesUtil.getUTCDateTime(3);
        this.timescale = timescale;
        this.duration = duration;
        this.language = new byte[]{0x55, (byte) 0xC4};
        this.preDefined = new byte[2];
    }

    @Override
    public int byteArrayLength() {
        return 32;
    }

    @Override
    public byte[] toByteArray() {
        int size = this.byteArrayLength();
        return ByteWriteBuff.newInstance(size)
                .putInteger(size)
                .putBytes(this.mp4Type.getByteArray())
                .putByte(this.version)
                .putBytes(this.flags)
                .putInteger(TimesUtil.getUTCTotalSecond(this.creationTime))
                .putInteger(TimesUtil.getUTCTotalSecond(this.modificationTime))
                .putInteger(this.timescale)
                .putInteger(this.duration)
                .putBytes(this.language)
                .putBytes(this.preDefined)
                .getData();
    }
}

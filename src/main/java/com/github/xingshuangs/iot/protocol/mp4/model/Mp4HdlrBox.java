package com.github.xingshuangs.iot.protocol.mp4.model;


import com.github.xingshuangs.iot.protocol.common.buff.ByteWriteBuff;
import com.github.xingshuangs.iot.protocol.mp4.enums.EMp4Type;
import com.github.xingshuangs.iot.utils.TimesUtil;

import java.time.LocalDateTime;

/**
 * ftyp盒子
 *
 * @author xingshuang
 */
public class Mp4HdlrBox extends Mp4Box {

    /**
     * 1字节，版本
     */
    private final int version;

    /**
     * 3字节为flags
     */
    private final byte[] flags;

    /**
     * 4字节pre_defined
     */
    private final byte[] preDefined;

    /**
     * 4字节
     */
    private final String handlerType;

    /**
     * 12字节 保留
     */
    private final byte[] reserved;

    /**
     * 5字节 名称
     */
    private final String name;

    public Mp4HdlrBox(boolean video) {
        this.mp4Type = EMp4Type.MDHD;
        this.version = 0;
        this.flags = new byte[3];
        this.preDefined = new byte[4];
        this.reserved = new byte[12];

        this.handlerType = video ? "vide" : "soun";
        this.name = video ? "VideoHandler" : "SoundHandler";
    }

    @Override
    public int byteArrayLength() {
        return 32 + this.name.length() + 1;
    }

    @Override
    public byte[] toByteArray() {
        int size = this.byteArrayLength();
        return ByteWriteBuff.newInstance(size)
                .putInteger(size)
                .putBytes(this.mp4Type.getByteArray())
                .putByte(this.version)
                .putBytes(this.flags)
                .putBytes(this.preDefined)
                .putString(this.handlerType)
                .putBytes(this.reserved)
                .putString(this.name)
                .getData();
    }
}

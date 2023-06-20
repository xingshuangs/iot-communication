package com.github.xingshuangs.iot.protocol.mp4.model;


import com.github.xingshuangs.iot.protocol.common.buff.ByteWriteBuff;
import com.github.xingshuangs.iot.protocol.mp4.enums.EMp4Type;

/**
 * ftyp(File Type) box是fmp4第一个box，用来确定文件的类型，版本及兼容的协议，每个文件有且只有一个，由于要支持流式传输，
 * 一般在客户端第一次发起请求时，发送一次带有ftypbox的数据，一般与moov合并发送给客户端
 *
 * @author xingshuang
 */
public class Mp4FtypBox extends Mp4Box {
    /**
     * 4字节major brand，这里为isom的ASCII
     */
    private final String majorBrand;

    /**
     * 4字节minor versio，最低兼容版本，这里为1.0版本
     */
    private final int minorVersion;

    /**
     * 4字节为单位元素的数组compatible brands，兼容版本，isom和avc1的ASCII
     */
    private final String compatibleBrands;

    public Mp4FtypBox() {
        this.mp4Type = EMp4Type.FTYP;
        this.majorBrand = "isom";
        this.minorVersion = 1;
        this.compatibleBrands = "isomavc1";
    }

    @Override
    public int byteArrayLength() {
        return 24;
    }

    @Override
    public byte[] toByteArray() {
        int size = this.byteArrayLength();
        return ByteWriteBuff.newInstance(size)
                .putInteger(size)
                .putBytes(this.mp4Type.getByteArray())
                .putString(this.majorBrand)
                .putInteger(this.minorVersion)
                .putString(this.compatibleBrands)
                .getData();
    }
}

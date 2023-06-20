package com.github.xingshuangs.iot.protocol.mp4.enums;


import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * 数据包的类型
 *
 * @author xingshuang
 */
public enum EMp4Type {

    AVC1("avc1"),
    AVCC("avcC"),
    BTRT("btrt"),
    DINF("dinf"),
    DREF("dref"),
    ESDS("esds"),
    FTYP("ftyp"),
    HDLR("hdlr"),
    MDAT("mdat"),
    MDHD("mdhd"),
    MDIA("mdia"),
    MFHD("mfhd"),
    MINF("minf"),
    MOOF("moof"),
    MOOV("moov"),
    MP4A("mp4a"),
    MVEX("mvex"),
    MVHD("mvhd"),
    SDTP("sdtp"),
    STBL("stbl"),
    STCO("stco"),
    STSC("stsc"),
    STSD("stsd"),
    STSZ("stsz"),
    STTS("stts"),
    TFDT("tfdt"),
    TFHD("tfhd"),
    TRAF("traf"),
    TRAK("trak"),
    TRUN("trun"),
    TREX("trex"),
    TKHD("tkhd"),
    VMHD("vmhd"),
    SMHD("smhd"),
    ;

    private static Map<String, EMp4Type> map;

    public static EMp4Type from(String data) {
        if (map == null) {
            map = new HashMap<>();
            for (EMp4Type item : EMp4Type.values()) {
                map.put(item.code, item);
            }
        }
        return map.get(data);
    }

    private final String code;

    private final byte[] byteArray;

    EMp4Type(String code) {
        this.code = code;
        this.byteArray = code.getBytes(StandardCharsets.US_ASCII);
    }

    public String getCode() {
        return code;
    }

    public byte[] getByteArray() {
        return this.byteArray;
    }
}

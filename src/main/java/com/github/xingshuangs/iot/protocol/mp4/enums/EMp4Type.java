/*
 * MIT License
 *
 * Copyright (c) 2021-2099 Oscura (xingshuang) <xingshuang_cool@163.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.xingshuangs.iot.protocol.mp4.enums;


import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * MP4 Type.
 * (数据包的类型)
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

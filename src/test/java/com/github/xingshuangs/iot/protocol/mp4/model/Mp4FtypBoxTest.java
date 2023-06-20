package com.github.xingshuangs.iot.protocol.mp4.model;

import com.github.xingshuangs.iot.protocol.mp4.enums.EMp4Type;
import com.github.xingshuangs.iot.utils.HexUtil;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;


public class Mp4FtypBoxTest {
    @Test
    public void mp4Type() {
        System.out.println(HexUtil.toHexString(EMp4Type.TFHD.getByteArray()));
    }

    @Test
    public void mp4FtypBox() {
        byte[] expect = new byte[]{
                0x00, 0x00, 0x00, 0x18,
                0x66, 0x74, 0x79, 0x70,
                0x69, 0x73, 0x6F, 0x6D,
                0x00, 0x00, 0x00, 0x01,
                0x69, 0x73, 0x6F, 0x6D, 0x61, 0x76, 0x63, 0x31
        };
        Mp4FtypBox box = new Mp4FtypBox();
        assertArrayEquals(expect, box.toByteArray());
    }
}
package com.github.xingshuangs.iot.protocol.rtp.payload;

import com.github.xingshuangs.iot.protocol.rtp.enums.EH264NaluType;
import org.junit.Test;

import static org.junit.Assert.*;


public class H264NaluHeaderTest {

    @Test
    public void fromBytes() {
        // SPS
        byte[] expect = new byte[]{0x67};
        H264NaluHeader header = H264NaluHeader.fromBytes(expect);
        assertFalse(header.isForbiddenZeroBit());
        assertEquals(3, header.getNri());
        assertEquals(EH264NaluType.SPS, header.getType());
        assertArrayEquals(expect, header.toByteArray());

        expect = new byte[]{0x7C};
        header = H264NaluHeader.fromBytes(expect);
        assertFalse(header.isForbiddenZeroBit());
        assertEquals(3, header.getNri());
        assertEquals(EH264NaluType.FU_A, header.getType());
        assertArrayEquals(expect, header.toByteArray());
    }
}
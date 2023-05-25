package com.github.xingshuangs.iot.protocol.rtp.payload;

import com.github.xingshuangs.iot.protocol.rtp.enums.EH264NaluType;
import com.github.xingshuangs.iot.protocol.rtp.model.payload.H264NaluFuA;
import com.github.xingshuangs.iot.protocol.rtp.model.payload.H264NaluSingle;
import org.junit.Test;

import static org.junit.Assert.*;


public class H264NaluTest {

    @Test
    public void h264NaluFuA_Start() {
        byte[] expect = new byte[]{(byte) 0x7C, (byte) 0x85, (byte) 0x00, (byte) 0x01, (byte) 0x02, (byte) 0x03};
        byte[] payload = new byte[]{(byte) 0x00, (byte) 0x01, (byte) 0x02, (byte) 0x03};
        H264NaluFuA nalu = H264NaluFuA.fromBytes(expect);
        assertFalse(nalu.getHeader().isForbiddenZeroBit());
        assertEquals(3, nalu.getHeader().getNri());
        assertEquals(EH264NaluType.FU_A, nalu.getHeader().getType());
        assertArrayEquals(payload, nalu.getPayload());

        assertTrue(nalu.getFuHeader().isStart());
        assertFalse(nalu.getFuHeader().isEnd());
        assertFalse(nalu.getFuHeader().isReserve());
        assertEquals(EH264NaluType.IDR_SLICE, nalu.getFuHeader().getType());
    }

    @Test
    public void h264NaluFuA_Middle() {
        byte[] expect = new byte[]{(byte) 0x7C, (byte) 0x05, (byte) 0x00, (byte) 0x01, (byte) 0x02, (byte) 0x03};
        byte[] payload = new byte[]{(byte) 0x00, (byte) 0x01, (byte) 0x02, (byte) 0x03};
        H264NaluFuA nalu = H264NaluFuA.fromBytes(expect);
        assertFalse(nalu.getHeader().isForbiddenZeroBit());
        assertEquals(3, nalu.getHeader().getNri());
        assertEquals(EH264NaluType.FU_A, nalu.getHeader().getType());
        assertArrayEquals(payload, nalu.getPayload());

        assertFalse(nalu.getFuHeader().isStart());
        assertFalse(nalu.getFuHeader().isEnd());
        assertFalse(nalu.getFuHeader().isReserve());
        assertEquals(EH264NaluType.IDR_SLICE, nalu.getFuHeader().getType());
    }

    @Test
    public void h264NaluFuA_End() {
        byte[] expect = new byte[]{(byte) 0x7C, (byte) 0x45, (byte) 0x00, (byte) 0x01, (byte) 0x02, (byte) 0x03};
        byte[] payload = new byte[]{(byte) 0x00, (byte) 0x01, (byte) 0x02, (byte) 0x03};
        H264NaluFuA nalu = H264NaluFuA.fromBytes(expect);
        assertFalse(nalu.getHeader().isForbiddenZeroBit());
        assertEquals(3, nalu.getHeader().getNri());
        assertEquals(EH264NaluType.FU_A, nalu.getHeader().getType());
        assertArrayEquals(payload, nalu.getPayload());

        assertFalse(nalu.getFuHeader().isStart());
        assertTrue(nalu.getFuHeader().isEnd());
        assertFalse(nalu.getFuHeader().isReserve());
        assertEquals(EH264NaluType.IDR_SLICE, nalu.getFuHeader().getType());
    }

    @Test
    public void h264NaluSingle() {
        byte[] expect = new byte[]{(byte) 0x61, (byte) 0x00, (byte) 0x01, (byte) 0x02, (byte) 0x03};
        byte[] payload = new byte[]{(byte) 0x00, (byte) 0x01, (byte) 0x02, (byte) 0x03};
        H264NaluSingle nalu = H264NaluSingle.fromBytes(expect);
        assertFalse(nalu.getHeader().isForbiddenZeroBit());
        assertEquals(3, nalu.getHeader().getNri());
        assertEquals(EH264NaluType.NON_IDR_SLICE, nalu.getHeader().getType());
        assertArrayEquals(payload, nalu.getPayload());
    }
}
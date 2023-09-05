package com.github.xingshuangs.iot.protocol.s7.model;

import com.github.xingshuangs.iot.protocol.s7.enums.EFileBlockType;
import org.junit.Test;

import static org.junit.Assert.*;


public class Mc7FileTest {

    @Test
    public void fromBytes() {
        byte[] data = new byte[]{
                (byte) 0x70, (byte) 0x70, (byte) 0x00, (byte) 0x00, (byte) 0x13, (byte) 0x08, (byte) 0x00, (byte) 0x01,
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x91, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                (byte) 0x02, (byte) 0xd1, (byte) 0xb8, (byte) 0x41, (byte) 0x38, (byte) 0x9c, (byte) 0x02, (byte) 0xd1,
                (byte) 0xb8, (byte) 0x0e, (byte) 0x38, (byte) 0x9c, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x5d,
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x10,
        };
        Mc7File mc7File = Mc7File.fromBytes(data);
        assertEquals(36, mc7File.getData().length);
        assertEquals(EFileBlockType.OB, mc7File.getBlockType());
        assertEquals(1, mc7File.getBlockNumber());
        assertEquals(16, mc7File.getMC7CodeLength());
    }
}
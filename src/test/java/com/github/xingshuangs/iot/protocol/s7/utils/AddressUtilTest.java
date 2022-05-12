package com.github.xingshuangs.iot.protocol.s7.utils;

import com.github.xingshuangs.iot.protocol.s7.enums.EArea;
import com.github.xingshuangs.iot.protocol.s7.model.RequestItem;
import org.junit.Test;

import static org.junit.Assert.*;


public class AddressUtilTest {

    @Test
    public void parseDBByte() {
        RequestItem requestItem = AddressUtil.parseByte("DB12.3.5", 3);
        assertEquals(3, requestItem.getCount());
        assertEquals(EArea.DATA_BLOCKS, requestItem.getArea());
        assertEquals(12, requestItem.getDbNumber());
        assertEquals(3, requestItem.getByteAddress());
        assertEquals(0, requestItem.getBitAddress());

        requestItem = AddressUtil.parseByte("DB12.3", 3);
        assertEquals(3, requestItem.getCount());
        assertEquals(EArea.DATA_BLOCKS, requestItem.getArea());
        assertEquals(12, requestItem.getDbNumber());
        assertEquals(3, requestItem.getByteAddress());
        assertEquals(0, requestItem.getBitAddress());

        requestItem = AddressUtil.parseByte("DB12", 3);
        assertEquals(3, requestItem.getCount());
        assertEquals(EArea.DATA_BLOCKS, requestItem.getArea());
        assertEquals(12, requestItem.getDbNumber());
        assertEquals(0, requestItem.getByteAddress());
        assertEquals(0, requestItem.getBitAddress());

        requestItem = AddressUtil.parseByte("DB11.24.6", 5);
        assertEquals(5, requestItem.getCount());
        assertEquals(EArea.DATA_BLOCKS, requestItem.getArea());
        assertEquals(11, requestItem.getDbNumber());
        assertEquals(24, requestItem.getByteAddress());
        assertEquals(0, requestItem.getBitAddress());
    }

    @Test
    public void parseDBBit() {
        RequestItem requestItem = AddressUtil.parseBit("DB12.3.5");
        assertEquals(1, requestItem.getCount());
        assertEquals(EArea.DATA_BLOCKS, requestItem.getArea());
        assertEquals(12, requestItem.getDbNumber());
        assertEquals(3, requestItem.getByteAddress());
        assertEquals(5, requestItem.getBitAddress());

        requestItem = AddressUtil.parseBit("DB12.3");
        assertEquals(1, requestItem.getCount());
        assertEquals(EArea.DATA_BLOCKS, requestItem.getArea());
        assertEquals(12, requestItem.getDbNumber());
        assertEquals(3, requestItem.getByteAddress());
        assertEquals(0, requestItem.getBitAddress());

        requestItem = AddressUtil.parseBit("DB12");
        assertEquals(1, requestItem.getCount());
        assertEquals(EArea.DATA_BLOCKS, requestItem.getArea());
        assertEquals(12, requestItem.getDbNumber());
        assertEquals(0, requestItem.getByteAddress());
        assertEquals(0, requestItem.getBitAddress());
    }

    @Test
    public void parseI() {
        RequestItem requestItem = AddressUtil.parseByte("I1.1", 3);
        assertEquals(3, requestItem.getCount());
        assertEquals(EArea.INPUTS, requestItem.getArea());
        assertEquals(0, requestItem.getDbNumber());
        assertEquals(1, requestItem.getByteAddress());
        assertEquals(0, requestItem.getBitAddress());

        requestItem = AddressUtil.parseByte("I2", 3);
        assertEquals(3, requestItem.getCount());
        assertEquals(EArea.INPUTS, requestItem.getArea());
        assertEquals(0, requestItem.getDbNumber());
        assertEquals(2, requestItem.getByteAddress());
        assertEquals(0, requestItem.getBitAddress());

        requestItem = AddressUtil.parseBit("I1.2");
        assertEquals(1, requestItem.getCount());
        assertEquals(EArea.INPUTS, requestItem.getArea());
        assertEquals(0, requestItem.getDbNumber());
        assertEquals(1, requestItem.getByteAddress());
        assertEquals(2, requestItem.getBitAddress());

        requestItem = AddressUtil.parseBit("I1");
        assertEquals(1, requestItem.getCount());
        assertEquals(EArea.INPUTS, requestItem.getArea());
        assertEquals(0, requestItem.getDbNumber());
        assertEquals(1, requestItem.getByteAddress());
        assertEquals(0, requestItem.getBitAddress());
    }

    @Test
    public void parseQ() {
        RequestItem requestItem = AddressUtil.parseByte("Q1.1", 3);
        assertEquals(3, requestItem.getCount());
        assertEquals(EArea.OUTPUTS, requestItem.getArea());
        assertEquals(0, requestItem.getDbNumber());
        assertEquals(1, requestItem.getByteAddress());
        assertEquals(0, requestItem.getBitAddress());

        requestItem = AddressUtil.parseByte("Q2", 3);
        assertEquals(3, requestItem.getCount());
        assertEquals(EArea.OUTPUTS, requestItem.getArea());
        assertEquals(0, requestItem.getDbNumber());
        assertEquals(2, requestItem.getByteAddress());
        assertEquals(0, requestItem.getBitAddress());

        requestItem = AddressUtil.parseBit("Q1.2");
        assertEquals(1, requestItem.getCount());
        assertEquals(EArea.OUTPUTS, requestItem.getArea());
        assertEquals(0, requestItem.getDbNumber());
        assertEquals(1, requestItem.getByteAddress());
        assertEquals(2, requestItem.getBitAddress());

        requestItem = AddressUtil.parseBit("Q1");
        assertEquals(1, requestItem.getCount());
        assertEquals(EArea.OUTPUTS, requestItem.getArea());
        assertEquals(0, requestItem.getDbNumber());
        assertEquals(1, requestItem.getByteAddress());
        assertEquals(0, requestItem.getBitAddress());
    }

    @Test
    public void parseM() {
        RequestItem requestItem = AddressUtil.parseByte("M1.1", 3);
        assertEquals(3, requestItem.getCount());
        assertEquals(EArea.FLAGS, requestItem.getArea());
        assertEquals(0, requestItem.getDbNumber());
        assertEquals(1, requestItem.getByteAddress());
        assertEquals(0, requestItem.getBitAddress());

        requestItem = AddressUtil.parseByte("M2", 3);
        assertEquals(3, requestItem.getCount());
        assertEquals(EArea.FLAGS, requestItem.getArea());
        assertEquals(0, requestItem.getDbNumber());
        assertEquals(2, requestItem.getByteAddress());
        assertEquals(0, requestItem.getBitAddress());

        requestItem = AddressUtil.parseBit("M1.2");
        assertEquals(1, requestItem.getCount());
        assertEquals(EArea.FLAGS, requestItem.getArea());
        assertEquals(0, requestItem.getDbNumber());
        assertEquals(1, requestItem.getByteAddress());
        assertEquals(2, requestItem.getBitAddress());

        requestItem = AddressUtil.parseBit("M1");
        assertEquals(1, requestItem.getCount());
        assertEquals(EArea.FLAGS, requestItem.getArea());
        assertEquals(0, requestItem.getDbNumber());
        assertEquals(1, requestItem.getByteAddress());
        assertEquals(0, requestItem.getBitAddress());
    }

    @Test
    public void parseC() {
        RequestItem requestItem = AddressUtil.parseByte("C1.1", 3);
        assertEquals(3, requestItem.getCount());
        assertEquals(EArea.S7_COUNTERS, requestItem.getArea());
        assertEquals(0, requestItem.getDbNumber());
        assertEquals(1, requestItem.getByteAddress());
        assertEquals(0, requestItem.getBitAddress());

        requestItem = AddressUtil.parseByte("C2", 3);
        assertEquals(3, requestItem.getCount());
        assertEquals(EArea.S7_COUNTERS, requestItem.getArea());
        assertEquals(0, requestItem.getDbNumber());
        assertEquals(2, requestItem.getByteAddress());
        assertEquals(0, requestItem.getBitAddress());

        requestItem = AddressUtil.parseBit("C1.2");
        assertEquals(1, requestItem.getCount());
        assertEquals(EArea.S7_COUNTERS, requestItem.getArea());
        assertEquals(0, requestItem.getDbNumber());
        assertEquals(1, requestItem.getByteAddress());
        assertEquals(2, requestItem.getBitAddress());

        requestItem = AddressUtil.parseBit("C1");
        assertEquals(1, requestItem.getCount());
        assertEquals(EArea.S7_COUNTERS, requestItem.getArea());
        assertEquals(0, requestItem.getDbNumber());
        assertEquals(1, requestItem.getByteAddress());
        assertEquals(0, requestItem.getBitAddress());
    }

    @Test
    public void parseT() {
        RequestItem requestItem = AddressUtil.parseByte("T1.1", 3);
        assertEquals(3, requestItem.getCount());
        assertEquals(EArea.S7_TIMERS, requestItem.getArea());
        assertEquals(0, requestItem.getDbNumber());
        assertEquals(1, requestItem.getByteAddress());
        assertEquals(0, requestItem.getBitAddress());

        requestItem = AddressUtil.parseByte("T2", 3);
        assertEquals(3, requestItem.getCount());
        assertEquals(EArea.S7_TIMERS, requestItem.getArea());
        assertEquals(0, requestItem.getDbNumber());
        assertEquals(2, requestItem.getByteAddress());
        assertEquals(0, requestItem.getBitAddress());

        requestItem = AddressUtil.parseBit("T1.2");
        assertEquals(1, requestItem.getCount());
        assertEquals(EArea.S7_TIMERS, requestItem.getArea());
        assertEquals(0, requestItem.getDbNumber());
        assertEquals(1, requestItem.getByteAddress());
        assertEquals(2, requestItem.getBitAddress());

        requestItem = AddressUtil.parseBit("T1");
        assertEquals(1, requestItem.getCount());
        assertEquals(EArea.S7_TIMERS, requestItem.getArea());
        assertEquals(0, requestItem.getDbNumber());
        assertEquals(1, requestItem.getByteAddress());
        assertEquals(0, requestItem.getBitAddress());
    }
}
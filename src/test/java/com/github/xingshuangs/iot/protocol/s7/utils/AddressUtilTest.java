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

        requestItem = AddressUtil.parseByte("DB11.DBX24.6", 5);
        assertEquals(5, requestItem.getCount());
        assertEquals(EArea.DATA_BLOCKS, requestItem.getArea());
        assertEquals(11, requestItem.getDbNumber());
        assertEquals(24, requestItem.getByteAddress());
        assertEquals(0, requestItem.getBitAddress());

        requestItem = AddressUtil.parseByte("DB11.DBB24.6", 5);
        assertEquals(5, requestItem.getCount());
        assertEquals(EArea.DATA_BLOCKS, requestItem.getArea());
        assertEquals(11, requestItem.getDbNumber());
        assertEquals(24, requestItem.getByteAddress());
        assertEquals(0, requestItem.getBitAddress());

        requestItem = AddressUtil.parseByte("DB11.DBW24.6", 5);
        assertEquals(5, requestItem.getCount());
        assertEquals(EArea.DATA_BLOCKS, requestItem.getArea());
        assertEquals(11, requestItem.getDbNumber());
        assertEquals(24, requestItem.getByteAddress());
        assertEquals(0, requestItem.getBitAddress());

        requestItem = AddressUtil.parseByte("DB11.DBD24.6", 5);
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

        requestItem = AddressUtil.parseBit("DB11.DBX24.6");
        assertEquals(1, requestItem.getCount());
        assertEquals(EArea.DATA_BLOCKS, requestItem.getArea());
        assertEquals(11, requestItem.getDbNumber());
        assertEquals(24, requestItem.getByteAddress());
        assertEquals(6, requestItem.getBitAddress());

        requestItem = AddressUtil.parseBit("DB11.DBX24");
        assertEquals(1, requestItem.getCount());
        assertEquals(EArea.DATA_BLOCKS, requestItem.getArea());
        assertEquals(11, requestItem.getDbNumber());
        assertEquals(24, requestItem.getByteAddress());
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

        requestItem = AddressUtil.parseByte("IB1",1);
        assertEquals(1, requestItem.getCount());
        assertEquals(EArea.INPUTS, requestItem.getArea());
        assertEquals(0, requestItem.getDbNumber());
        assertEquals(1, requestItem.getByteAddress());
        assertEquals(0, requestItem.getBitAddress());

        requestItem = AddressUtil.parseByte("IW1",2);
        assertEquals(2, requestItem.getCount());
        assertEquals(EArea.INPUTS, requestItem.getArea());
        assertEquals(0, requestItem.getDbNumber());
        assertEquals(1, requestItem.getByteAddress());
        assertEquals(0, requestItem.getBitAddress());

        requestItem = AddressUtil.parseByte("ID1",4);
        assertEquals(4, requestItem.getCount());
        assertEquals(EArea.INPUTS, requestItem.getArea());
        assertEquals(0, requestItem.getDbNumber());
        assertEquals(1, requestItem.getByteAddress());
        assertEquals(0, requestItem.getBitAddress());

        requestItem = AddressUtil.parseBit("I1.2");
        assertEquals(1, requestItem.getCount());
        assertEquals(EArea.INPUTS, requestItem.getArea());
        assertEquals(0, requestItem.getDbNumber());
        assertEquals(1, requestItem.getByteAddress());
        assertEquals(2, requestItem.getBitAddress());

        requestItem = AddressUtil.parseBit("IB1.2");
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

        requestItem = AddressUtil.parseByte("QB2", 3);
        assertEquals(3, requestItem.getCount());
        assertEquals(EArea.OUTPUTS, requestItem.getArea());
        assertEquals(0, requestItem.getDbNumber());
        assertEquals(2, requestItem.getByteAddress());
        assertEquals(0, requestItem.getBitAddress());

        requestItem = AddressUtil.parseByte("QW2", 2);
        assertEquals(2, requestItem.getCount());
        assertEquals(EArea.OUTPUTS, requestItem.getArea());
        assertEquals(0, requestItem.getDbNumber());
        assertEquals(2, requestItem.getByteAddress());
        assertEquals(0, requestItem.getBitAddress());

        requestItem = AddressUtil.parseByte("QD2", 4);
        assertEquals(4, requestItem.getCount());
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

        requestItem = AddressUtil.parseBit("QB1.2");
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

        requestItem = AddressUtil.parseByte("MB100", 3);
        assertEquals(3, requestItem.getCount());
        assertEquals(EArea.FLAGS, requestItem.getArea());
        assertEquals(0, requestItem.getDbNumber());
        assertEquals(100, requestItem.getByteAddress());
        assertEquals(0, requestItem.getBitAddress());

        requestItem = AddressUtil.parseByte("MW100", 3);
        assertEquals(3, requestItem.getCount());
        assertEquals(EArea.FLAGS, requestItem.getArea());
        assertEquals(0, requestItem.getDbNumber());
        assertEquals(100, requestItem.getByteAddress());
        assertEquals(0, requestItem.getBitAddress());

        requestItem = AddressUtil.parseByte("MD100", 4);
        assertEquals(4, requestItem.getCount());
        assertEquals(EArea.FLAGS, requestItem.getArea());
        assertEquals(0, requestItem.getDbNumber());
        assertEquals(100, requestItem.getByteAddress());
        assertEquals(0, requestItem.getBitAddress());

        requestItem = AddressUtil.parseBit("M1.2");
        assertEquals(1, requestItem.getCount());
        assertEquals(EArea.FLAGS, requestItem.getArea());
        assertEquals(0, requestItem.getDbNumber());
        assertEquals(1, requestItem.getByteAddress());
        assertEquals(2, requestItem.getBitAddress());

        requestItem = AddressUtil.parseBit("MB1.2");
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

        requestItem = AddressUtil.parseByte("CB2", 3);
        assertEquals(3, requestItem.getCount());
        assertEquals(EArea.S7_COUNTERS, requestItem.getArea());
        assertEquals(0, requestItem.getDbNumber());
        assertEquals(2, requestItem.getByteAddress());
        assertEquals(0, requestItem.getBitAddress());

        requestItem = AddressUtil.parseByte("CW2", 3);
        assertEquals(3, requestItem.getCount());
        assertEquals(EArea.S7_COUNTERS, requestItem.getArea());
        assertEquals(0, requestItem.getDbNumber());
        assertEquals(2, requestItem.getByteAddress());
        assertEquals(0, requestItem.getBitAddress());

        requestItem = AddressUtil.parseByte("CD2", 3);
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

        requestItem = AddressUtil.parseByte("TB2", 3);
        assertEquals(3, requestItem.getCount());
        assertEquals(EArea.S7_TIMERS, requestItem.getArea());
        assertEquals(0, requestItem.getDbNumber());
        assertEquals(2, requestItem.getByteAddress());
        assertEquals(0, requestItem.getBitAddress());

        requestItem = AddressUtil.parseByte("TW2", 3);
        assertEquals(3, requestItem.getCount());
        assertEquals(EArea.S7_TIMERS, requestItem.getArea());
        assertEquals(0, requestItem.getDbNumber());
        assertEquals(2, requestItem.getByteAddress());
        assertEquals(0, requestItem.getBitAddress());

        requestItem = AddressUtil.parseByte("TD2", 3);
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

        requestItem = AddressUtil.parseBit("TB1.2");
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

    @Test
    public void parseV() {
        RequestItem requestItem = AddressUtil.parseByte("V1.1", 3);
        assertEquals(3, requestItem.getCount());
        assertEquals(EArea.DATA_BLOCKS, requestItem.getArea());
        assertEquals(1, requestItem.getDbNumber());
        assertEquals(1, requestItem.getByteAddress());
        assertEquals(0, requestItem.getBitAddress());

        requestItem = AddressUtil.parseByte("V2", 3);
        assertEquals(3, requestItem.getCount());
        assertEquals(EArea.DATA_BLOCKS, requestItem.getArea());
        assertEquals(1, requestItem.getDbNumber());
        assertEquals(2, requestItem.getByteAddress());
        assertEquals(0, requestItem.getBitAddress());

        requestItem = AddressUtil.parseByte("V100", 3);
        assertEquals(3, requestItem.getCount());
        assertEquals(EArea.DATA_BLOCKS, requestItem.getArea());
        assertEquals(1, requestItem.getDbNumber());
        assertEquals(100, requestItem.getByteAddress());
        assertEquals(0, requestItem.getBitAddress());

        requestItem = AddressUtil.parseByte("VB100", 3);
        assertEquals(3, requestItem.getCount());
        assertEquals(EArea.DATA_BLOCKS, requestItem.getArea());
        assertEquals(1, requestItem.getDbNumber());
        assertEquals(100, requestItem.getByteAddress());
        assertEquals(0, requestItem.getBitAddress());

        requestItem = AddressUtil.parseByte("VW100", 3);
        assertEquals(3, requestItem.getCount());
        assertEquals(EArea.DATA_BLOCKS, requestItem.getArea());
        assertEquals(1, requestItem.getDbNumber());
        assertEquals(100, requestItem.getByteAddress());
        assertEquals(0, requestItem.getBitAddress());

        requestItem = AddressUtil.parseByte("VD100", 3);
        assertEquals(3, requestItem.getCount());
        assertEquals(EArea.DATA_BLOCKS, requestItem.getArea());
        assertEquals(1, requestItem.getDbNumber());
        assertEquals(100, requestItem.getByteAddress());
        assertEquals(0, requestItem.getBitAddress());

        requestItem = AddressUtil.parseBit("V6.5");
        assertEquals(1, requestItem.getCount());
        assertEquals(EArea.DATA_BLOCKS, requestItem.getArea());
        assertEquals(1, requestItem.getDbNumber());
        assertEquals(6, requestItem.getByteAddress());
        assertEquals(5, requestItem.getBitAddress());

        requestItem = AddressUtil.parseBit("VB6.5");
        assertEquals(1, requestItem.getCount());
        assertEquals(EArea.DATA_BLOCKS, requestItem.getArea());
        assertEquals(1, requestItem.getDbNumber());
        assertEquals(6, requestItem.getByteAddress());
        assertEquals(5, requestItem.getBitAddress());
    }
}
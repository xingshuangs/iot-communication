package com.github.xingshuangs.iot.protocol.s7.model;

import com.github.xingshuangs.iot.protocol.s7.enums.EDataVariableType;
import com.github.xingshuangs.iot.protocol.s7.enums.EReturnCode;
import org.junit.Test;

import static org.junit.Assert.*;


public class DataItemTest {

    @Test
    public void byteArrayLength() {
        DataItem dataItem = new DataItem();
        assertEquals(4, dataItem.byteArrayLength());
    }

    @Test
    public void toByteArray() {
        DataItem dataItem = new DataItem();
        dataItem.setReturnCode(EReturnCode.from((byte) 0xFF));
        dataItem.setVariableType(EDataVariableType.BYTE_WORD_DWORD);
        dataItem.setCount(7);
        dataItem.setData(new byte[1]);
        byte[] actual = dataItem.toByteArray();
        byte[] expect = {(byte) 0xFF, (byte) 0x04, (byte) 0x00, (byte) 0x38, (byte) 0x00};
        assertArrayEquals(expect, actual);
    }
}
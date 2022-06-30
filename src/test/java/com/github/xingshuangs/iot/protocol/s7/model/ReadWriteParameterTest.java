package com.github.xingshuangs.iot.protocol.s7.model;

import com.github.xingshuangs.iot.protocol.s7.enums.EArea;
import com.github.xingshuangs.iot.protocol.s7.enums.EFunctionCode;
import com.github.xingshuangs.iot.protocol.s7.enums.EParamVariableType;
import com.github.xingshuangs.iot.protocol.s7.enums.ESyntaxID;
import org.junit.Test;

import static org.junit.Assert.*;


public class ReadWriteParameterTest {

    @Test
    public void byteArrayLength() {
        RequestItem requestItem = new RequestItem();
        ReadWriteParameter readWriteParameter = new ReadWriteParameter();
        readWriteParameter.getRequestItems().add(requestItem);
        assertEquals(14, readWriteParameter.byteArrayLength());
    }

    @Test
    public void toByteArray() {
        RequestItem requestItem = new RequestItem();
        requestItem.setSpecificationType((byte) 0x12);
        requestItem.setLengthOfFollowing((byte) 0x10);
        requestItem.setSyntaxId(ESyntaxID.S7ANY);
        requestItem.setVariableType(EParamVariableType.BYTE);
        requestItem.setCount(7);
        requestItem.setDbNumber(7);
        requestItem.setArea(EArea.DATA_BLOCKS);
        requestItem.setByteAddress(3);
        requestItem.setBitAddress(1);
        ReadWriteParameter readWriteParameter = new ReadWriteParameter();
        readWriteParameter.setFunctionCode(EFunctionCode.READ_VARIABLE);
        readWriteParameter.setItemCount((byte) 0x01);
        readWriteParameter.getRequestItems().add(requestItem);
        byte[] actual = readWriteParameter.toByteArray();
        byte[] expect = new byte[]{(byte) 0x04, (byte) 0x01, (byte) 0x12, (byte) 0x10, (byte) 0x10, (byte) 0x02, (byte) 0x00, (byte) 0x07, (byte) 0x00, (byte) 0x07, (byte) 0x84, (byte) 0x00, (byte) 0x00, (byte) 0x19};
        assertArrayEquals(expect, actual);
    }

    @Test
    public void fromBytes() {
        byte[] data = new byte[]{
                // parameter：功能 + 个数
                (byte) 0x05, (byte) 0x02,
                // request item
                (byte) 0x12,
                // 剩余长度
                (byte) 0x0A,
                // 寻址方式
                (byte) 0x10,
                // 数据类型
                (byte) 0x02,
                // 读取长度 count
                (byte) 0x00, (byte) 0x01,
                // db编号 dbNumber
                (byte) 0x00, (byte) 0x01,
                // 存储区域 area
                (byte) 0x84,
                // 起始地址 byteAddress
                (byte) 0x00, (byte) 0x00, (byte) 0x19,
                // request item-------------------------------
                (byte) 0x12,
                // 剩余长度
                (byte) 0x0A,
                // 寻址方式
                (byte) 0x10,
                // 数据类型
                (byte) 0x04,
                // 读取长度 count
                (byte) 0x00, (byte) 0x01,
                // db编号 dbNumber
                (byte) 0x00, (byte) 0x03,
                // 存储区域 area
                (byte) 0x84,
                // 起始地址 byteAddress
                (byte) 0x00, (byte) 0x00, (byte) 0x08
        };

        ReadWriteParameter parameter = ReadWriteParameter.fromBytes(data);
        assertEquals(EParamVariableType.BYTE, parameter.getRequestItems().get(0).getVariableType());
        assertEquals(1, parameter.getRequestItems().get(0).getBitAddress());
        assertEquals(3, parameter.getRequestItems().get(0).getByteAddress());
        assertEquals(1, parameter.getRequestItems().get(0).getDbNumber());
        assertEquals(EParamVariableType.WORD, parameter.getRequestItems().get(1).getVariableType());
        assertEquals(0, parameter.getRequestItems().get(1).getBitAddress());
        assertEquals(1, parameter.getRequestItems().get(1).getByteAddress());
        assertEquals(3, parameter.getRequestItems().get(1).getDbNumber());
    }
}
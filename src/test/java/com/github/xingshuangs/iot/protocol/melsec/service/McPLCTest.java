package com.github.xingshuangs.iot.protocol.melsec.service;

import com.github.xingshuangs.iot.exceptions.McCommException;
import com.github.xingshuangs.iot.protocol.melsec.enums.EMcFrameType;
import com.github.xingshuangs.iot.protocol.melsec.enums.EMcSeries;
import com.github.xingshuangs.iot.protocol.melsec.model.McDeviceAddress;
import com.github.xingshuangs.iot.protocol.melsec.model.McDeviceContent;
import com.github.xingshuangs.iot.utils.HexUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@Slf4j
@Ignore
public class McPLCTest {

    private final McPLC mcPLC = new McPLC("192.168.3.100", 6001);
//    private final McPLC mcPLC = new McPLC(GeneralConst.LOCALHOST, 6001);

    @Before
    public void before() {
        this.mcPLC.setSeries(EMcSeries.Q_L);
        this.mcPLC.setFrameType(EMcFrameType.FRAME_3E);
        this.mcPLC.setComCallback(x -> log.debug("[{}] {}", x.length, HexUtil.toHexString(x)));
    }

    @Test
    public void readWriteBoolean() {
        this.mcPLC.writeBoolean("M110",true);
        boolean m110 = this.mcPLC.readBoolean("M110");
        assertTrue(m110);

        List<Boolean> booleanList = this.mcPLC.readBooleans("M110", 7170);
        assertEquals(7170, booleanList.size());


//        McDeviceContent content = McDeviceContent.createBy("M110",new byte[]{0x01});
//        this.mcPLC.writeDeviceRandomInBit(Collections.singletonList(content));
//        this.mcPLC.writeBooleans("M100", true, true, true);
//        List<Boolean> booleanList = this.mcPLC.readBooleans("M100", 3);
//        assertEquals(4, booleanList.size());
//        booleanList.forEach(Assert::assertTrue);

//        this.mcPLC.writeInt16("D111", (short) 66);
//        this.mcPLC.writeInt16("D112", (short) 77);
//        this.mcPLC.writeInt16("D113", (short) 6444);
//        List<Short> actual = this.mcPLC.readInt16("D111", "D112", "D113");
//        assertEquals(66, actual.get(0).shortValue());
//        assertEquals(77, actual.get(1).shortValue());
//        assertEquals(6444, actual.get(2).shortValue());
    }

    @Test
    public void readWriteInt16() {
        this.mcPLC.writeInt16("D110", (short) 99);
        short shortData = this.mcPLC.readInt16("D110");
        assertEquals(99, shortData);

        this.mcPLC.writeInt16("D111", (short) 66);
        this.mcPLC.writeInt16("D112", (short) 77);
        this.mcPLC.writeInt16("D113", (short) 6444);
        List<Short> actual = this.mcPLC.readInt16("D111", "D112", "D113");
        assertEquals(66, actual.get(0).shortValue());
        assertEquals(77, actual.get(1).shortValue());
        assertEquals(6444, actual.get(2).shortValue());
    }

    @Test
    public void readWriteUInt16() {
        this.mcPLC.writeUInt16("D110", 99);
        int shortData = this.mcPLC.readUInt16("D110");
        assertEquals(99, shortData);

        this.mcPLC.writeUInt16("D111", 66);
        this.mcPLC.writeUInt16("D112", 77);
        this.mcPLC.writeUInt16("D113", 6444);
        List<Integer> actual = this.mcPLC.readUInt16("D111", "D112", "D113");
        assertEquals(66, actual.get(0).intValue());
        assertEquals(77, actual.get(1).intValue());
        assertEquals(6444, actual.get(2).intValue());
    }

    @Test(expected = McCommException.class)
    public void readWriteInt16_1() {
        short int16 = this.mcPLC.readInt16("113");
        System.out.println(int16);
    }

    @Test
    public void readWriteInt32() {
        this.mcPLC.writeInt32("D114", 799864);
        int data = this.mcPLC.readInt32("D114");
        assertEquals(799864, data);

        this.mcPLC.writeInt32("D116", 66);
        this.mcPLC.writeInt32("D118", 3541563);
        this.mcPLC.writeInt32("D120", 546345896);
        List<Integer> actual = this.mcPLC.readInt32("D116", "D118", "D120");
        assertEquals(66, actual.get(0).intValue());
        assertEquals(3541563, actual.get(1).intValue());
        assertEquals(546345896, actual.get(2).intValue());
    }

    @Test
    public void readWriteUInt32() {
        this.mcPLC.writeUInt32("D114", 799864);
        long data = this.mcPLC.readUInt32("D114");
        assertEquals(799864, data);

        this.mcPLC.writeUInt32("D116", 66);
        this.mcPLC.writeUInt32("D118", 3541563);
        this.mcPLC.writeUInt32("D120", 546345896);
        List<Long> actual = this.mcPLC.readUInt32("D116", "D118", "D120");
        assertEquals(66, actual.get(0).intValue());
        assertEquals(3541563, actual.get(1).intValue());
        assertEquals(546345896, actual.get(2).intValue());
    }

    @Test
    public void readWriteDeviceBatchInWord() {
        byte[] expect = new byte[]{0x34, 0x12, 0x02, 0x00};
        McDeviceContent reqContent = McDeviceContent.createBy("M110", 2, expect);
        this.mcPLC.writeDeviceBatchInWord(reqContent);
        McDeviceAddress address = McDeviceAddress.createBy("M110", 2);
        McDeviceContent ackContent = this.mcPLC.readDeviceBatchInWord(address);
        assertArrayEquals(expect, ackContent.getData());
    }

    @Test
    public void readWriteDeviceBatchInBit() {
        byte[] expect = new byte[]{0x11, 0x00, 0x01, 0x11};
        McDeviceContent reqContent = McDeviceContent.createBy("M110", 8, expect);
        this.mcPLC.writeDeviceBatchInBit(reqContent);
        McDeviceAddress address = McDeviceAddress.createBy("M110", 8);
        McDeviceContent ackContent = this.mcPLC.readDeviceBatchInBit(address);
        assertArrayEquals(expect, ackContent.getData());
    }

    @Test
    public void readWriteDeviceRandomInWord() {
        byte[] expect = new byte[]{0x34, 0x12, 0x02, 0x00};
        List<McDeviceContent> writeWord = new ArrayList<>();
        writeWord.add(McDeviceContent.createBy("D0", new byte[]{0x50, 0x05}));
        writeWord.add(McDeviceContent.createBy("D1", new byte[]{0x75, 0x05}));
        writeWord.add(McDeviceContent.createBy("M100", new byte[]{0x40, 0x05}));
//        writeWord.add(McDeviceContent.createBy("X20", new byte[]{(byte) 0x83, 0x05}));
        List<McDeviceContent> writeDWord = new ArrayList<>();
        writeDWord.add(McDeviceContent.createBy("D1500", new byte[]{0x02, 0x12, 0x39, 0x04}));
//        writeDWord.add(McDeviceContent.createBy("Y160", new byte[]{0x07, 0x26, 0x75, 0x23}));
//        writeDWord.add(McDeviceContent.createBy("M1111", new byte[]{0x75, 0x04, 0x25, 0x04}));
        this.mcPLC.writeDeviceRandomInWord(writeWord, writeDWord);
        List<McDeviceAddress> readWord = new ArrayList<>();
        readWord.add(McDeviceAddress.createBy("D0"));
        readWord.add(McDeviceAddress.createBy("D1"));
        readWord.add(McDeviceAddress.createBy("M100"));
//        readWord.add(McDeviceAddress.createBy("X20"));
        List<McDeviceAddress> readDWord = new ArrayList<>();
        readDWord.add(McDeviceAddress.createBy("D1500"));
//        readDWord.add(McDeviceAddress.createBy("Y160"));
//        readDWord.add(McDeviceAddress.createBy("M1111"));
        List<McDeviceContent> mcDeviceContents = this.mcPLC.readDeviceRandomInWord(readWord, readDWord);
//        assertArrayEquals(expect, ackContent.getData());
    }

    @Test
    public void readAndWriteData() {
        this.mcPLC.writeBoolean("M100", true);
        boolean m100 = this.mcPLC.readBoolean("M100");
        assertTrue(m100);

        this.mcPLC.writeInt16("D100", (short) 66);
        short int16 = this.mcPLC.readInt16("D100");
        assertEquals(66, int16);

        this.mcPLC.writeUInt16("D100", 77);
        int uint16 = this.mcPLC.readUInt16("D100");
        assertEquals(77, uint16);

        this.mcPLC.writeInt32("D100", 88);
        int int32 = this.mcPLC.readInt32("D100");
        assertEquals(88, int32);

        this.mcPLC.writeUInt32("D100", 99);
        long uint32 = this.mcPLC.readUInt32("D100");
        assertEquals(99, uint32);

        this.mcPLC.writeFloat32("D100", 99.33f);
        float float32 = this.mcPLC.readFloat32("D100");
        assertEquals(99.33f, float32, 0.001);

        this.mcPLC.writeFloat64("D100", 151.33f);
        double float64 = this.mcPLC.readFloat64("D100");
        assertEquals(151.33f, float64, 0.001);

        this.mcPLC.writeString("D100", "123456");
        String string = this.mcPLC.readString("D100", 6);
        assertEquals("123456", string);
    }

}
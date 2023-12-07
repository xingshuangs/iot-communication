package com.github.xingshuangs.iot.protocol.melsec.service;

import com.github.xingshuangs.iot.protocol.common.constant.GeneralConst;
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

//    private final McPLC mcPLC = new McPLC("192.168.3.100", 6000);
    private final McPLC mcPLC = new McPLC(GeneralConst.LOCALHOST, 6000);

    @Before
    public void before() {
        this.mcPLC.setSeries(EMcSeries.Q_L);
        this.mcPLC.setFrameType(EMcFrameType.FRAME_3E);
        this.mcPLC.setComCallback(x -> log.debug("[{}] {}", x.length, HexUtil.toHexString(x)));
    }

    @Test
    public void readWrite() {
        short m100 = this.mcPLC.readInt16("M100");
        System.out.println(m100);
    }

    @Test
    public void readWriteDeviceBatchInWord() {
        byte[] expect = new byte[]{0x34, 0x12, 0x02, 0x00};
        McDeviceContent reqContent = McDeviceContent.createBy("M100", 2, expect);
        this.mcPLC.writeDeviceBatchInWord(reqContent);
        McDeviceAddress address = McDeviceAddress.createBy("M100", 2);
        McDeviceContent ackContent = this.mcPLC.readDeviceBatchInWord(address);
        assertArrayEquals(expect, ackContent.getData());
    }

    @Test
    public void readWriteDeviceBatchInBit() {
        byte[] expect = new byte[]{0x11, 0x00, 0x11, 0x00};
        McDeviceContent reqContent = McDeviceContent.createBy("M100", 8, expect);
        this.mcPLC.writeDeviceBatchInBit(reqContent);
        McDeviceAddress address = McDeviceAddress.createBy("M100", 8);
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
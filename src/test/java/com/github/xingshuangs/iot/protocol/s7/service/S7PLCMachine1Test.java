package com.github.xingshuangs.iot.protocol.s7.service;


import com.github.xingshuangs.iot.protocol.common.buff.ByteReadBuff;
import com.github.xingshuangs.iot.protocol.s7.enums.ENckArea;
import com.github.xingshuangs.iot.protocol.s7.enums.ENckModule;
import com.github.xingshuangs.iot.protocol.s7.enums.EPlcType;
import com.github.xingshuangs.iot.protocol.s7.model.DataItem;
import com.github.xingshuangs.iot.protocol.s7.model.NckRequestBuilder;
import com.github.xingshuangs.iot.protocol.s7.model.RequestNckItem;
import com.github.xingshuangs.iot.protocol.s7.model.S7Data;
import com.github.xingshuangs.iot.utils.HexUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 握手
 * 长度[22]：03 00 00 16 11 E0 00 00 00 01 00 C0 01 0A C1 02 04 00 C2 02 0D 04
 * 长度[22]：03 00 00 16 11 D0 00 01 00 01 00 C0 01 0A C1 02 04 00 C2 02 0D 04
 * 长度[25]：03 00 00 19 02 F0 80 32 01 00 00 00 00 00 08 00 00 F0 00 00 01 00 01 00 F0
 * 长度[27]：03 00 00 1B 02 F0 80 32 03 00 00 00 00 00 08 00 00 00 00 F0 00 00 01 00 01 00 F0
 *
 * @author xingshuang
 */
@Slf4j
@Ignore
public class S7PLCMachine1Test {
    private final S7PLC s7PLC = new S7PLC(EPlcType.SINUMERIK_828D, "192.168.101.16");
//    private final S7PLC s7PLC = new S7PLC(EPlcType.S1200);

    @Before
    public void before() {
        this.s7PLC.setComCallback(x -> log.debug("长度[{}]：{}", x.length, HexUtil.toHexString(x)));
    }

    @Test
    public void feedRateTest() {
        // 发送[29]：03 00 00 1D 02 F0 80 32 01 00 00 00 13 00 0C 00 00 04 01 12 08 82 41 00 03 00 01 7F 01
        // 接收[33]：03 00 00 21 02 F0 80 32 03 00 00 00 13 00 02 00 0C 00 00 04 01 FF 09 00 08 00 00 00 00 00 00 00 00
        // 长度 29
        byte[] sendByteArray = new byte[]{
                // tpkt
                (byte) 0x03, (byte) 0x00, (byte) 0x00, (byte) 0x1D,
                // cotp DT Data
                (byte) 0x02, (byte) 0xF0, (byte) 0x80,
                // header
                (byte) 0x32, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x13, (byte) 0x00, (byte) 0x0C, (byte) 0x00, (byte) 0x00,
                // parameter
                (byte) 0x04, (byte) 0x01,
                // request item
                (byte) 0x12, (byte) 0x08, (byte) 0x82, (byte) 0x41, (byte) 0x00, (byte) 0x03, (byte) 0x00, (byte) 0x01, (byte) 0x7f, (byte) 0x01
        };
        byte[] recByteArray = this.s7PLC.readFromServerByPersistence(sendByteArray);
        // 响应 4+3+12+2+4=25
        double data = ByteReadBuff.newInstance(recByteArray, true).getFloat64(25);
        log.debug("进给速度：{}", data);
    }

    @Test
    public void setFeedRateTest() {
        // 发送[29]：03 00 00 1D 02 F0 80 32 01 00 00 00 13 00 0C 00 00 04 01 12 08 82 41 00 02 00 01 7F 01
        // 接收[33]：03 00 00 21 02 F0 80 32 03 00 00 00 13 00 02 00 0C 00 00 04 01 FF 09 00 08 00 00 00 00 00 00 00 00
        // 长度 29
        byte[] sendByteArray = new byte[]{
                // tpkt
                (byte) 0x03, (byte) 0x00, (byte) 0x00, (byte) 0x1D,
                // cotp DT Data
                (byte) 0x02, (byte) 0xF0, (byte) 0x80,
                // header
                (byte) 0x32, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x13, (byte) 0x00, (byte) 0x0C, (byte) 0x00, (byte) 0x00,
                // parameter
                (byte) 0x04, (byte) 0x01,
                // request item
                (byte) 0x12, (byte) 0x08, (byte) 0x82, (byte) 0x41, (byte) 0x00, (byte) 0x02, (byte) 0x00, (byte) 0x01, (byte) 0x7f, (byte) 0x01
        };
        byte[] recByteArray = this.s7PLC.readFromServerByPersistence(sendByteArray);
        // 响应 4+3+12+2+4=25
        double data = ByteReadBuff.newInstance(recByteArray, true).getInt16(25);
        log.debug("设定进给速度：{}", data);
    }

    @Test
    public void actFeedRateTest() {
        // 发送[29]：03 00 00 1D 02 F0 80 32 01 00 00 00 13 00 0C 00 00 04 01 12 08 82 41 00 01 00 01 7F 01
        // 接收[33]：03 00 00 21 02 F0 80 32 03 00 00 00 13 00 02 00 0C 00 00 04 01 FF 09 00 08 00 00 00 00 00 00 00 00
        // 长度 29
        byte[] sendByteArray = new byte[]{
                // tpkt
                (byte) 0x03, (byte) 0x00, (byte) 0x00, (byte) 0x1D,
                // cotp DT Data
                (byte) 0x02, (byte) 0xF0, (byte) 0x80,
                // header
                (byte) 0x32, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x13, (byte) 0x00, (byte) 0x0C, (byte) 0x00, (byte) 0x00,
                // parameter
                (byte) 0x04, (byte) 0x01,
                // request item
                (byte) 0x12, (byte) 0x08, (byte) 0x82, (byte) 0x41, (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x01, (byte) 0x7f, (byte) 0x01
        };
        byte[] recByteArray = this.s7PLC.readFromServerByPersistence(sendByteArray);
        // 响应 4+3+12+2+4=25
        double data = ByteReadBuff.newInstance(recByteArray, true).getInt16(25);
        log.debug("实际进给速度：{}", data);
    }

    @Test
    public void setSpindleSpeedTest() {
        // 长度 29
        byte[] sendByteArray = new byte[]{
                // tpkt
                (byte) 0x03, (byte) 0x00, (byte) 0x00, (byte) 0x1D,
                // cotp DT Data
                (byte) 0x02, (byte) 0xF0, (byte) 0x80,
                // header
                (byte) 0x32, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x13, (byte) 0x00, (byte) 0x0C, (byte) 0x00, (byte) 0x00,
                // parameter
                (byte) 0x04, (byte) 0x01,
                // request item
                (byte) 0x12, (byte) 0x08, (byte) 0x82, (byte) 0x01, (byte) 0x00, (byte) 0x03, (byte) 0x00, (byte) 0x04, (byte) 0x72, (byte) 0x01
        };
        byte[] recByteArray = this.s7PLC.readFromServerByPersistence(sendByteArray);
        // 响应 4+3+12+2+4=25
        double data = ByteReadBuff.newInstance(recByteArray, true).getInt16(25);
        log.debug("设定主轴转速：{}", data);
    }

    @Test
    public void actSpindleSpeedTest() {
        // 发送[29]：03 00 00 1D 02 F0 80 32 01 00 00 00 13 00 0C 00 00 04 01 12 08 82 41 00 02 00 01 72 01
        // 接收[33]：03 00 00 21 02 F0 80 32 03 00 00 00 13 00 02 00 0C 00 00 04 01 FF 09 00 08 00 00 00 00 00 00 00 00
        // 长度 29
        byte[] sendByteArray = new byte[]{
                // tpkt
                (byte) 0x03, (byte) 0x00, (byte) 0x00, (byte) 0x1D,
                // cotp DT Data
                (byte) 0x02, (byte) 0xF0, (byte) 0x80,
                // header
                (byte) 0x32, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x13, (byte) 0x00, (byte) 0x0C, (byte) 0x00, (byte) 0x00,
                // parameter
                (byte) 0x04, (byte) 0x01,
                // request item
                (byte) 0x12, (byte) 0x08, (byte) 0x82, (byte) 0x41, (byte) 0x00, (byte) 0x02, (byte) 0x00, (byte) 0x01, (byte) 0x72, (byte) 0x01
        };
        byte[] recByteArray = this.s7PLC.readFromServerByPersistence(sendByteArray);
        // 响应 4+3+12+2+4=25
        double data = ByteReadBuff.newInstance(recByteArray, true).getInt16(25);
        log.debug("实际主轴转速：{}", data);
    }

    @Test
    public void toolNumberTest() {
        // 发送[29]：03 00 00 1D 02 F0 80 32 01 00 00 00 13 00 0C 00 00 04 01 12 08 82 41 00 17 00 01 7F 01
        // 接收[27]：03 00 00 1B 02 F0 80 32 03 00 00 00 13 00 02 00 06 00 00 04 01 FF 09 00 02 01 00
        // 长度 29
//        byte[] sendByteArray = new byte[]{
//                // tpkt
//                (byte) 0x03, (byte) 0x00, (byte) 0x00, (byte) 0x1D,
//                // cotp DT Data
//                (byte) 0x02, (byte) 0xF0, (byte) 0x80,
//                // header
//                (byte) 0x32, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x13, (byte) 0x00, (byte) 0x0C, (byte) 0x00, (byte) 0x00,
//                // parameter
//                (byte) 0x04, (byte) 0x01,
//                // request item
//                (byte) 0x12, (byte) 0x08, (byte) 0x82, (byte) 0x41, (byte) 0x00, (byte) 0x17, (byte) 0x00, (byte) 0x01, (byte) 0x7f, (byte) 0x01
//        };
//        byte[] recByteArray = this.s7PLC.readFromServerByPersistence(sendByteArray);
//        // 响应 4+3+12+2+4=25
//        int data = ByteReadBuff.newInstance(recByteArray, true).getInt16(25);
//        log.debug("刀具号：{}", data);

        RequestNckItem item = new RequestNckItem(ENckArea.C_CHANNEL, 1, 23, 1, ENckModule.S, 1);
        S7Data s7Data = NckRequestBuilder.creatNckRequest(item);
        S7Data ack = this.s7PLC.readFromServerByPersistence(s7Data);
        List<DataItem> dataItems = ack.getDatum().getReturnItems().stream().map(DataItem.class::cast).collect(Collectors.toList());
        for (DataItem dataItem : dataItems) {
            log.debug("刀具：{}", ByteReadBuff.newInstance(dataItem.getData(), true).getUInt16());
        }
    }

    // 刀具半径补偿编号
    @Test
    public void toolDNumberTest() {
        // 发送[29]：03 00 00 1D 02 F0 80 32 01 00 00 00 13 00 0C 00 00 04 01 12 08 82 41 00 23 00 01 7F 01
        // 接收[57]：03 00 00 39 02 F0 80 32 03 00 00 00 13 00 02 00 24 00 00 04 01 FF 09 00 20 32 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00
//        byte[] sendByteArray = new byte[]{
//                // tpkt
//                (byte) 0x03, (byte) 0x00, (byte) 0x00, (byte) 0x1D,
//                // cotp DT Data
//                (byte) 0x02, (byte) 0xF0, (byte) 0x80,
//                // header
//                (byte) 0x32, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x13, (byte) 0x00, (byte) 0x0C, (byte) 0x00, (byte) 0x00,
//                // parameter
//                (byte) 0x04, (byte) 0x01,
//                // request item
//                (byte) 0x12, (byte) 0x08, (byte) 0x82, (byte) 0x41, (byte) 0x00, (byte) 0x23, (byte) 0x00, (byte) 0x01, (byte) 0x7f, (byte) 0x01
//        };
//        byte[] recByteArray = this.s7PLC.readFromServerByPersistence(sendByteArray);
//        // 响应 4+3+12+2+4=25
//        int data = ByteReadBuff.newInstance(recByteArray,true).getInt16(25);
//        log.debug("结果：{}", data);

        RequestNckItem item = new RequestNckItem(ENckArea.C_CHANNEL, 1, 35, 1, ENckModule.S, 1);
        S7Data s7Data = NckRequestBuilder.creatNckRequest(item);
        S7Data ack = this.s7PLC.readFromServerByPersistence(s7Data);
        List<DataItem> dataItems = ack.getDatum().getReturnItems().stream().map(DataItem.class::cast).collect(Collectors.toList());
        for (DataItem dataItem : dataItems) {
            log.debug("刀具半径补偿编号：{}", ByteReadBuff.newInstance(dataItem.getData(), true).getUInt16());
        }
    }

    @Test
    public void machinePositionTest() {
        // 发送[29]：03 00 00 1D 02 F0 80 32 01 00 00 00 13 00 0C 00 00 04 01 12 08 82 41 00 02 00 01 74 01
        // 接收[33]：03 00 00 21 02 F0 80 32 03 00 00 00 13 00 02 00 0C 00 00 04 01 FF 09 00 08 CD CC CC CC CC 6C 61 40
        // 长度 29
//        byte[] sendByteArray = new byte[]{
//                // tpkt
//                (byte) 0x03, (byte) 0x00, (byte) 0x00, (byte) 0x1D,
//                // cotp DT Data
//                (byte) 0x02, (byte) 0xF0, (byte) 0x80,
//                // header
//                (byte) 0x32, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x13, (byte) 0x00, (byte) 0x0C, (byte) 0x00, (byte) 0x00,
//                // parameter
//                (byte) 0x04, (byte) 0x01,
//                // request item
//                (byte) 0x12, (byte) 0x08, (byte) 0x82, (byte) 0x41, (byte) 0x00, (byte) 0x02, (byte) 0x00, (byte) 0x01, (byte) 0x74, (byte) 0x01
//        };
//        byte[] recByteArray = this.s7PLC.readFromServerByPersistence(sendByteArray);
//        // 响应 4+3+12+2+4=25
//        double data = ByteReadBuff.newInstance(recByteArray,true).getFloat64(25);
//        log.debug("位置x：{}", data);

        RequestNckItem item = new RequestNckItem(ENckArea.C_CHANNEL, 1, 2, 1, ENckModule.SMA, 1);
        RequestNckItem item1 = new RequestNckItem(ENckArea.C_CHANNEL, 1, 2, 2, ENckModule.SMA, 1);
        RequestNckItem item2 = new RequestNckItem(ENckArea.C_CHANNEL, 1, 2, 3, ENckModule.SMA, 1);
        S7Data s7Data = NckRequestBuilder.creatNckRequest(Arrays.asList(item, item1, item2));
        S7Data ack = this.s7PLC.readFromServerByPersistence(s7Data);
        List<DataItem> dataItems = ack.getDatum().getReturnItems().stream().map(DataItem.class::cast).collect(Collectors.toList());
        for (DataItem dataItem : dataItems) {
            log.debug("位置坐标：{}", ByteReadBuff.newInstance(dataItem.getData(), true).getFloat64());
        }

        item = new RequestNckItem(ENckArea.C_CHANNEL, 1, 2, 1, ENckModule.SMA, 3);
        s7Data = NckRequestBuilder.creatNckRequest(Arrays.asList(item));
        ack = this.s7PLC.readFromServerByPersistence(s7Data);
        dataItems = ack.getDatum().getReturnItems().stream().map(DataItem.class::cast).collect(Collectors.toList());
        for (DataItem dataItem : dataItems) {
            log.debug("位置坐标：{}", ByteReadBuff.newInstance(dataItem.getData(), true).getFloat64());
        }
    }

    @Test
    public void relativePositionTest() {
        // 发送[29]：03 00 00 1D 02 F0 80 32 01 00 00 00 13 00 0C 00 00 04 01 12 08 82 41 00 19 00 01 70 01
        // 接收[33]：03 00 00 21 02 F0 80 32 03 00 00 00 13 00 02 00 0C 00 00 04 01 FF 09 00 08 5B B6 D6 17 89 2D C8 40
        // 长度 29
//        byte[] sendByteArray = new byte[]{
//                // tpkt
//                (byte) 0x03, (byte) 0x00, (byte) 0x00, (byte) 0x1D,
//                // cotp DT Data
//                (byte) 0x02, (byte) 0xF0, (byte) 0x80,
//                // header
//                (byte) 0x32, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x13, (byte) 0x00, (byte) 0x0C, (byte) 0x00, (byte) 0x00,
//                // parameter
//                (byte) 0x04, (byte) 0x01,
//                // request item
//                (byte) 0x12, (byte) 0x08, (byte) 0x82, (byte) 0x41, (byte) 0x00, (byte) 0x19, (byte) 0x00, (byte) 0x03, (byte) 0x70, (byte) 0x01
//        };
//        byte[] recByteArray = this.s7PLC.readFromServerByPersistence(sendByteArray);
//        // 响应 4+3+12+2+4=25
//        double data = ByteReadBuff.newInstance(recByteArray).getFloat64(25);
//        log.debug("位置x：{}", data);

        RequestNckItem item = new RequestNckItem(ENckArea.C_CHANNEL, 1, 25, 1, ENckModule.SEGA, 1);
        RequestNckItem item1 = new RequestNckItem(ENckArea.C_CHANNEL, 1, 25, 2, ENckModule.SEGA, 1);
        RequestNckItem item2 = new RequestNckItem(ENckArea.C_CHANNEL, 1, 25, 3, ENckModule.SEGA, 1);
        S7Data s7Data = NckRequestBuilder.creatNckRequest(Arrays.asList(item, item1, item2));
        S7Data ack = this.s7PLC.readFromServerByPersistence(s7Data);
        List<DataItem> dataItems = ack.getDatum().getReturnItems().stream().map(DataItem.class::cast).collect(Collectors.toList());
        for (DataItem dataItem : dataItems) {
            log.debug("位置坐标：{}", ByteReadBuff.newInstance(dataItem.getData(), true).getFloat64());
        }
    }

    @Test
    public void modeTest() {
        // 发送[29]：03 00 00 1D 02 F0 80 32 01 00 00 00 00 00 0C 00 00 04 01 12 08 82 21 00 03 00 01 7F 01
        // 接收[27]：03 00 00 1B 02 F0 80 32 03 00 00 00 00 00 02 00 06 00 00 04 01 FF 09 00 02 00 00
        // 长度 29
//        byte[] sendByteArray = new byte[]{
//                // tpkt
//                (byte) 0x03, (byte) 0x00, (byte) 0x00, (byte) 0x1D,
//                // cotp DT Data
//                (byte) 0x02, (byte) 0xF0, (byte) 0x80,
//                // header
//                (byte) 0x32, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x13, (byte) 0x00, (byte) 0x0C, (byte) 0x00, (byte) 0x00,
//                // parameter
//                (byte) 0x04, (byte) 0x01,
//                // request item
//                (byte) 0x12, (byte) 0x08, (byte) 0x82, (byte) 0x21, (byte) 0x00, (byte) 0x03, (byte) 0x00, (byte) 0x01, (byte) 0x7F, (byte) 0x01
//        };
//        byte[] recByteArray = this.s7PLC.readFromServerByPersistence(sendByteArray);
        // 0000:JOG, 0100:MDA, 0200:AUTO, 其他
        // 响应 4+3+12+2+4=25
//        double data = ByteReadBuff.newInstance(recByteArray).getFloat64(25);
//        log.debug("位置x：{}", data);

        RequestNckItem item = new RequestNckItem(ENckArea.B_MODE_GROUP, 1, 3, 1, ENckModule.S, 1);
        S7Data s7Data = NckRequestBuilder.creatNckRequest(item);
        S7Data ack = this.s7PLC.readFromServerByPersistence(s7Data);
        List<DataItem> dataItems = ack.getDatum().getReturnItems().stream().map(DataItem.class::cast).collect(Collectors.toList());
        for (DataItem dataItem : dataItems) {
            // 0000:JOG, 0100:MDA, 0200:AUTO, 其他
            log.debug("模式：{}", ByteReadBuff.newInstance(dataItem.getData(), true).getUInt16());
        }
    }

    @Test
    public void modeTest1() {
        List<RequestNckItem> requestNckItems = IntStream.range(1, 8)
                .mapToObj(x -> new RequestNckItem(ENckArea.B_MODE_GROUP, 1, x, 1, ENckModule.S, 1))
                .collect(Collectors.toList());
        List<DataItem> dataItems = this.s7PLC.readS7NckData(requestNckItems);
        List<Integer> collect = dataItems.stream().map(x -> ByteReadBuff.newInstance(x.getData(), true).getUInt16())
                .collect(Collectors.toList());
        collect.forEach(x -> log.debug("数据：{}", x));
    }

}

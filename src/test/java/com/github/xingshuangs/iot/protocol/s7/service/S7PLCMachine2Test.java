package com.github.xingshuangs.iot.protocol.s7.service;


import com.github.xingshuangs.iot.protocol.s7.enums.EPlcType;
import com.github.xingshuangs.iot.utils.HexUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

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
public class S7PLCMachine2Test {
    private final S7PLC s7PLC = new S7PLC(EPlcType.SINUMERIK_828D, "192.168.101.16");
//    private final S7PLC s7PLC = new S7PLC(EPlcType.S1200);

    @Before
    public void before() {
        this.s7PLC.setComCallback(x -> log.debug("[{}]：{}", x.length, HexUtil.toHexString(x)));
    }

    @Test
    public void readCncId() {
        // 发送[29]：03 00 00 1D 02 F0 80 32 01 00 00 00 00 00 0C 00 00 04 01 12 08 82 01 46 6E 00 01 1A 01
        // 接收[57]：03 00 00 39 02 F0 80 32 03 00 00 00 00 00 02 00 24 00 00 04 01 FF 09 00 20 30 30 30 30 36 30 31 39 33 30 38 38 46 43 30 30 30 30 37 35 00 00 00 00 00 00 00 00 00 00 00 00
        String res = this.s7PLC.readCncId();
        log.debug("数据：{}", res);
    }

    @Test
    public void readCncVersion() {
        // 发送[29]：03 00 00 1D 02 F0 80 32 01 00 00 00 00 00 0C 00 00 04 01 12 08 82 01 46 78 00 01 1A 01
        // 接收[45]：03 00 00 2D 02 F0 80 32 03 00 00 00 00 00 02 00 18 00 00 04 01 FF 09 00 14 30 34 2E 30 38 2E 30 37 2E 30 30 2E 30 32 30 20 20 20 20 00
        String res = this.s7PLC.readCncVersion();
        log.debug("数据：{}", res);
    }

    @Test
    public void readCncType1() {
        // 发送[29]：03 00 00 1D 02 F0 80 32 01 00 00 00 00 00 0C 00 00 04 01 12 08 82 01 46 78 00 02 1A 01
        // 接收[45]：03 00 00 2D 02 F0 80 32 03 00 00 00 00 00 02 00 18 00 00 04 01 FF 09 00 14 38 32 38 44 5F 30 34 2E 30 38 20 20 20 20 20 00 00 00 00 00
        String res = this.s7PLC.readCncType1();
        log.debug("数据：{}", res);
    }

    @Test
    public void readCncManufactureDate() {
        // 发送[29]：03 00 00 1D 02 F0 80 32 01 00 00 00 00 00 0C 00 00 04 01 12 08 82 01 46 78 00 03 1A 01
        // 接收[45]：03 00 00 2D 02 F0 80 32 03 00 00 00 00 00 02 00 18 00 00 04 01 FF 09 00 14 31 36 2F 31 31 2F 32 31 20 32 30 3A 33 34 3A 32 30 00 00 00
        String res = this.s7PLC.readCncManufactureDate();
        log.debug("数据：{}", res);
    }

    @Test
    public void readCncType() {
        // 发送[29]：03 00 00 1D 02 F0 80 32 01 00 00 00 00 00 0C 00 00 04 01 12 08 82 01 46 78 00 04 1A 01
        // 接收[45]：03 00 00 2D 02 F0 80 32 03 00 00 00 00 00 02 00 18 00 00 04 01 FF 09 00 14 38 32 38 44 2D 4D 45 34 32 00 00 00 00 00 00 00 00 00 00 00
        String res = this.s7PLC.readCncType();
        log.debug("数据：{}", res);
    }

    @Test
    public void readMachinePosition() {
        // 发送[59]：03 00 00 3B 02 F0 80 32 01 00 00 00 00 00 2A 00 00 04 04 12 08 82 41 00 02 00 01 74 01 12 08 82 41 00 02 00 02 74 01 12 08 82 41 00 02 00 03 74 01 12 08 82 41 00 02 00 04 74 01
        // 接收[69]：03 00 00 45 02 F0 80 32 03 00 00 00 00 00 02 00 30 00 00 04 04 FF 09 00 08 AE 9E 93 DE 37 B9 64 40 FF 09 00 08 5A 2F 86 72 A2 A9 60 C0 FF 09 00 08 5B 25 58 1C CE CC 24 C0 FF 09 00 08 00 00 00 00 00 00 00 00
        List<Double> res = this.s7PLC.readMachinePosition();
        res.forEach(x -> log.debug("数据：{}", x));
    }

    @Test
    public void readRelativePosition() {
        // 发送[59]：03 00 00 3B 02 F0 80 32 01 00 00 00 00 00 2A 00 00 04 04 12 08 82 41 00 19 00 01 70 01 12 08 82 41 00 19 00 02 70 01 12 08 82 41 00 19 00 03 70 01 12 08 82 41 00 19 00 04 70 01
        // 接收[69]：03 00 00 45 02 F0 80 32 03 00 00 00 00 00 02 00 30 00 00 04 04 FF 09 00 08 AE 9E 93 DE 37 B9 64 40 FF 09 00 08 5A 2F 86 72 A2 A9 60 C0 FF 09 00 08 31 5F 5E 80 7D 14 10 C0 FF 09 00 08 00 00 00 00 00 00 00 00
        List<Double> res = this.s7PLC.readRelativePosition();
        res.forEach(x -> log.debug("数据：{}", x));
    }

    @Test
    public void readRemainPosition() {
        // 发送[59]：03 00 00 3B 02 F0 80 32 01 00 00 00 00 00 2A 00 00 04 04 12 08 82 41 00 03 00 01 74 01 12 08 82 41 00 03 00 02 74 01 12 08 82 41 00 03 00 03 74 01 12 08 82 41 00 03 00 04 74 01
        // 接收[69]：03 00 00 45 02 F0 80 32 03 00 00 00 00 00 02 00 30 00 00 04 04 FF 09 00 08 00 00 00 00 00 00 00 00 FF 09 00 08 00 00 00 00 00 00 00 00 FF 09 00 08 00 00 00 00 00 00 00 00 FF 09 00 08 00 00 00 00 00 00 00 00
        List<Double> res = this.s7PLC.readRemainPosition();
        res.forEach(x -> log.debug("数据：{}", x));
    }

    @Test
    public void readTWorkPosition() {
        // 发送[49]：03 00 00 31 02 F0 80 32 01 00 00 00 00 00 20 00 00 04 03 12 08 82 41 00 01 00 04 12 01 12 08 82 41 00 01 00 05 12 01 12 08 82 41 00 01 00 06 12 01
        // 接收[57]：03 00 00 39 02 F0 80 32 03 00 00 00 00 00 02 00 24 00 00 04 03 FF 09 00 08 00 00 00 00 00 00 00 80 FF 09 00 08 00 00 00 00 00 00 00 80 FF 09 00 08 00 00 00 00 00 00 00 80
        List<Double> res = this.s7PLC.readTWorkPiecePosition();
        res.forEach(x -> log.debug("数据：{}", x));
    }

    @Test
    public void readToolRadiusCompensationNumber() {
        // 发送[29]：03 00 00 1D 02 F0 80 32 01 00 00 00 00 00 0C 00 00 04 01 12 08 82 41 00 23 00 01 7F 01
        // 接收[57]：03 00 00 39 02 F0 80 32 03 00 00 00 00 00 02 00 24 00 00 04 01 FF 09 00 20 31 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00
        int res = this.s7PLC.readToolRadiusCompensationNumber();
        log.debug("刀具半径补偿编号：{}", res);
    }

    @Test
    public void readToolNumber() {
        // 发送[29]：03 00 00 1D 02 F0 80 32 01 00 00 00 00 00 0C 00 00 04 01 12 08 82 41 00 17 00 01 7F 01
        // 接收[27]：03 00 00 1B 02 F0 80 32 03 00 00 00 00 00 02 00 06 00 00 04 01 FF 09 00 02 01 00
        int res = this.s7PLC.readToolNumber();
        log.debug("刀具编号：{}", res);
    }

    @Test
    public void readActSpindleSpeed() {
        // 发送[29]：03 00 00 1D 02 F0 80 32 01 00 00 00 00 00 0C 00 00 04 01 12 08 82 41 00 02 00 01 72 01
        // 接收[33]：03 00 00 21 02 F0 80 32 03 00 00 00 00 00 02 00 0C 00 00 04 01 FF 09 00 08 00 00 00 00 00 00 00 00
        double res = this.s7PLC.readActSpindleSpeed();
        log.debug("实际主轴转速：{}", res);
    }

    @Test
    public void readSetSpindleSpeed() {
        // 发送[29]：03 00 00 1D 02 F0 80 32 01 00 00 00 00 00 0C 00 00 04 01 12 08 82 01 00 03 00 04 72 01
        // 接收[33]：03 00 00 21 02 F0 80 32 03 00 00 00 00 00 02 00 0C 00 00 04 01 FF 09 00 08 00 00 00 00 00 00 00 00
        double res = this.s7PLC.readSetSpindleSpeed();
        log.debug("设定主轴转速：{}", res);
    }

    @Test
    public void readSpindleRate() {
        // 发送[29]：03 00 00 1D 02 F0 80 32 01 00 00 00 00 00 0C 00 00 04 01 12 08 82 41 00 04 00 01 72 01
        // 接收[33]：03 00 00 21 02 F0 80 32 03 00 00 00 00 00 02 00 0C 00 00 04 01 FF 09 00 08 00 00 00 00 00 00 59 40
        double res = this.s7PLC.readSpindleRate();
        log.debug("主轴速率：{}", res);
    }

    @Test
    public void readFeedRate() {
        // 发送[29]：03 00 00 1D 02 F0 80 32 01 00 00 00 00 00 0C 00 00 04 01 12 08 82 41 00 03 00 01 7F 01
        // 接收[33]：03 00 00 21 02 F0 80 32 03 00 00 00 00 00 02 00 0C 00 00 04 01 FF 09 00 08 00 00 00 00 00 00 59 40
        double res = this.s7PLC.readFeedRate();
        log.debug("进给速率：{}", res);
    }

    @Test
    public void readSetFeedRate() {
        // 发送[29]：03 00 00 1D 02 F0 80 32 01 00 00 00 00 00 0C 00 00 04 01 12 08 82 41 00 02 00 01 7F 01
        // 接收[33]：03 00 00 21 02 F0 80 32 03 00 00 00 00 00 02 00 0C 00 00 04 01 FF 09 00 08 00 00 00 00 00 00 00 00
        double res = this.s7PLC.readSetFeedRate();
        log.debug("设定进给速率：{}", res);
    }

    @Test
    public void readActFeedRate() {
        // 发送[29]：03 00 00 1D 02 F0 80 32 01 00 00 00 00 00 0C 00 00 04 01 12 08 82 41 00 01 00 01 7F 01
        // 接收[33]：03 00 00 21 02 F0 80 32 03 00 00 00 00 00 02 00 0C 00 00 04 01 FF 09 00 08 00 00 00 00 00 00 00 00
        double res = this.s7PLC.readActFeedRate();
        log.debug("实际进给速率：{}", res);
    }

    @Test
    public void readWorkMode() {
        // 发送[29]：03 00 00 1D 02 F0 80 32 01 00 00 00 00 00 0C 00 00 04 01 12 08 82 21 00 03 00 01 7F 01
        // 接收[27]：03 00 00 1B 02 F0 80 32 03 00 00 00 00 00 02 00 06 00 00 04 01 FF 09 00 02 00 00
        int res = this.s7PLC.readWorkMode();
        log.debug("工作模式：{}", res);
    }

    @Test
    public void readStatus() {
        // 发送[29]：03 00 00 1D 02 F0 80 32 01 00 00 00 00 00 0C 00 00 04 01 12 08 82 41 00 0B 00 01 7F 01
        // 接收[27]：03 00 00 1B 02 F0 80 32 03 00 00 00 00 00 02 00 06 00 00 04 01 FF 09 00 02 02 00
        int res = this.s7PLC.readStatus();
        log.debug("状态：{}", res);
    }

    @Test
    public void readRunTime() {
        // 发送[29]：03 00 00 1D 02 F0 80 32 01 00 00 00 00 00 0C 00 00 04 01 12 08 82 41 01 29 00 01 7F 01
        // 接收[33]：03 00 00 21 02 F0 80 32 03 00 00 00 00 00 02 00 0C 00 00 04 01 FF 09 00 08 00 00 00 00 00 00 00 00
        double res = this.s7PLC.readRunTime();
        log.debug("工作时间：{}", res);
    }

    @Test
    public void readRemainTime() {
        // 发送[29]：03 00 00 1D 02 F0 80 32 01 00 00 00 00 00 0C 00 00 04 01 12 08 82 41 01 2A 00 01 7F 01
        // 接收[33]：03 00 00 21 02 F0 80 32 03 00 00 00 00 00 02 00 0C 00 00 04 01 FF 09 00 08 00 00 00 00 00 00 00 00
        double res = this.s7PLC.readRemainTime();
        log.debug("剩余时间：{}", res);
    }

    @Test
    public void readProgramName() {
        // 发送[29]：03 00 00 1D 02 F0 80 32 01 00 00 00 00 00 0C 00 00 04 01 12 08 82 41 00 0C 00 01 7A 01
        // 接收[185]：03 00 00 B9 02 F0 80 32 03 00 00 00 00 00 02 00 A4 00 00 04 01 FF 09 00 A0 2F 5F 4E 5F 4D 50 46 30 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00
        String res = this.s7PLC.readProgramName();
        log.debug("程序名：{}", res);
    }

    @Test
    public void readAlarmNumber() {
        // 发送[29]：03 00 00 1D 02 F0 80 32 01 00 00 00 00 00 0C 00 00 04 01 12 08 82 01 00 07 00 01 7F 01
        // 接收[27]：03 00 00 1B 02 F0 80 32 03 00 00 00 00 00 02 00 06 00 00 04 01 FF 09 00 02 05 00
        int res = this.s7PLC.readAlarmNumber();
        log.debug("报警编号：{}", res);
    }

    @Test
    public void readAlarmInfo() {
        // 发送[29]：03 00 00 1D 02 F0 80 32 01 00 00 00 00 00 0C 00 00 04 01 12 08 82 01 00 07 00 01 7F 01
        // 接收[27]：03 00 00 1B 02 F0 80 32 03 00 00 00 00 00 02 00 06 00 00 04 01 FF 09 00 02 05 00
        long res = this.s7PLC.readAlarmInfo();
        log.debug("报警编号：{}", res);
    }

}

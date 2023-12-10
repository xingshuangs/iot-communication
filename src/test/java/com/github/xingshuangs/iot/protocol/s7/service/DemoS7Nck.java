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

package com.github.xingshuangs.iot.protocol.s7.service;


import com.github.xingshuangs.iot.protocol.s7.enums.EPlcType;

import java.util.List;

/**
 * @author xingshuang
 */
public class DemoS7Nck {

    public static void main(String[] args) {
        S7PLC s7PLC = new S7PLC(EPlcType.SINUMERIK_828D, "127.0.0.1");

        String cncId = s7PLC.readCncId();
        String cncVersion = s7PLC.readCncVersion();
        String cncType = s7PLC.readCncType();
        String cncManufactureDate = s7PLC.readCncManufactureDate();
        List<Double> machinePosition = s7PLC.readMachinePosition();
        List<Double> readRelativePosition = s7PLC.readRelativePosition();
        List<Double> readRemainPosition = s7PLC.readRemainPosition();
        List<Double> tWorkPiecePosition = s7PLC.readTWorkPiecePosition();
        int toolRadiusCompensationNumber = s7PLC.readToolRadiusCompensationNumber();
        int toolNumber = s7PLC.readToolNumber();
        double actSpindleSpeed = s7PLC.readActSpindleSpeed();
        double feedRate = s7PLC.readFeedRate();
        int workMode = s7PLC.readWorkMode();
        double runTime = s7PLC.readRunTime();
        double remainTime = s7PLC.readRemainTime();
        String programName = s7PLC.readProgramName();
        int alarmNumber = s7PLC.readAlarmNumber();

        s7PLC.close();
    }
}

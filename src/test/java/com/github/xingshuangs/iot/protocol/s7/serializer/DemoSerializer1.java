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

package com.github.xingshuangs.iot.protocol.s7.serializer;


import com.github.xingshuangs.iot.common.enums.EDataType;
import com.github.xingshuangs.iot.protocol.s7.enums.EPlcType;
import com.github.xingshuangs.iot.protocol.s7.service.S7PLC;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 测试
 *
 * @author xingshuang
 */
public class DemoSerializer1 {

    public static void main(String[] args) {
        // 构建PLC对象
        S7PLC s7PLC = new S7PLC(EPlcType.S1200, "127.0.0.1");
        // 构建序列化对象
        S7Serializer s7Serializer = S7Serializer.newInstance(s7PLC);

        byte[] byteData = new byte[]{(byte) 0x01, (byte) 0x02, (byte) 0x03};
        List<S7Parameter> list = new ArrayList<>();
        list.add(new S7Parameter("DB1.0.1", EDataType.BOOL, 1, true));
        list.add(new S7Parameter("DB1.4", EDataType.UINT16, 1, 42767));
        list.add(new S7Parameter("DB1.6", EDataType.INT16, 1, (short) 32767));
        list.add(new S7Parameter("DB1.8", EDataType.UINT32, 1, 3147483647L));
        list.add(new S7Parameter("DB1.12", EDataType.INT32, 1, 2147483647));
        list.add(new S7Parameter("DB1.16", EDataType.FLOAT32, 1, 3.14f));
        list.add(new S7Parameter("DB1.20", EDataType.FLOAT64, 1, 4.15));
        list.add(new S7Parameter("DB1.28", EDataType.BYTE, 3, byteData));
        list.add(new S7Parameter("DB1.31", EDataType.STRING, 10, "1234567890"));
        list.add(new S7Parameter("DB1.43", EDataType.TIME, 1, 12L));
        list.add(new S7Parameter("DB1.47", EDataType.DATE, 1, LocalDate.of(2023, 5, 15)));
        list.add(new S7Parameter("DB1.49", EDataType.TIME_OF_DAY, 1, LocalTime.of(20, 22, 13)));
        list.add(new S7Parameter("DB1.53", EDataType.DTL, 1, LocalDateTime.of(2023, 5, 27, 12, 11, 22, 333225555)));
        s7Serializer.write(list);

        list = new ArrayList<>();
        list.add(new S7Parameter("DB1.0.1", EDataType.BOOL));
        list.add(new S7Parameter("DB1.4", EDataType.UINT16));
        list.add(new S7Parameter("DB1.6", EDataType.INT16));
        list.add(new S7Parameter("DB1.8", EDataType.UINT32));
        list.add(new S7Parameter("DB1.12", EDataType.INT32));
        list.add(new S7Parameter("DB1.16", EDataType.FLOAT32));
        list.add(new S7Parameter("DB1.20", EDataType.FLOAT64));
        list.add(new S7Parameter("DB1.28", EDataType.BYTE, 3));
        list.add(new S7Parameter("DB1.31", EDataType.STRING, 10));
        list.add(new S7Parameter("DB1.43", EDataType.TIME));
        list.add(new S7Parameter("DB1.47", EDataType.DATE));
        list.add(new S7Parameter("DB1.49", EDataType.TIME_OF_DAY));
        list.add(new S7Parameter("DB1.53", EDataType.DTL));
        List<S7Parameter> actual = s7Serializer.read(list);

        s7PLC.close();
    }
}

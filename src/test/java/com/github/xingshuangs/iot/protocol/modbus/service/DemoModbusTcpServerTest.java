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

package com.github.xingshuangs.iot.protocol.modbus.service;


import com.github.xingshuangs.iot.utils.HexUtil;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * modbus服务端的示例
 *
 * @author xingshuang
 */
public class DemoModbusTcpServerTest {

    public static void main(String[] args) {
        ModbusTcpServer server = new ModbusTcpServer();
        server.start();

        ModbusTcp modbusTcp = new ModbusTcp("127.0.0.1");
        // optional
        modbusTcp.setComCallback((tag, bytes) -> System.out.printf("%s[%d] %s%n", tag, bytes.length, HexUtil.toHexString(bytes)));

        modbusTcp.writeInt16(2, (short) 10);
        short data = modbusTcp.readInt16(2);

        modbusTcp.close();
        server.stop();
    }
}

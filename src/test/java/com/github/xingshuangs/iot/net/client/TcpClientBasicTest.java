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

package com.github.xingshuangs.iot.net.client;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

@Ignore
public class TcpClientBasicTest {

    private TcpClientBasic tcpClientBasic;

    @Before
    public void init() {
        this.tcpClientBasic = new TcpClientBasic("127.0.0.1", 8088);
    }

    @Test
    public void write() {
        String test = "hello world ";
        IntStream.range(0, 100).forEach(i -> {
            String data = test + i;
            try {
                TimeUnit.SECONDS.sleep(2);
                System.out.println(data);
                this.tcpClientBasic.write(data.getBytes(StandardCharsets.UTF_8));
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        });
    }

    @Test
    public void write1() {
    }

    @Test
    public void read() {
        byte[] data = new byte[1024];
        int read = this.tcpClientBasic.read(data);
        if (read > 0) {
            System.out.println(new String(data));
        }
    }

    @Test
    public void read1() {
        IntStream.range(0, 30).forEach(i -> {
            try {
                TimeUnit.SECONDS.sleep(2);
                System.out.println("第" + i + "次读取数据");
                byte[] data = new byte[1024];
                int read = this.tcpClientBasic.read(data);
                if (read > 0) {
                    System.out.println(new String(data));
                }
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        });
    }
}
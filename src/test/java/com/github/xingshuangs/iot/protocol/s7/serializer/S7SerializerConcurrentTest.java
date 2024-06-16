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

import com.github.xingshuangs.iot.protocol.s7.enums.EPlcType;
import com.github.xingshuangs.iot.protocol.s7.service.S7PLC;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Slf4j
@Ignore
public class S7SerializerConcurrentTest {
    private final S7PLC s7PLC = new S7PLC(EPlcType.S1200, "127.0.0.1");

    @Before
    public void before() {
//        this.s7PLC.setComCallback((tag, bytes) -> System.out.printf("%s[%d] %s%n", tag, bytes.length, HexUtil.toHexString(bytes)));
    }

    @Test
    public void concurrentTest() throws InterruptedException {
        S7Serializer s7Serializer = S7Serializer.newInstance(s7PLC);
        byte[] byteData = new byte[]{(byte) 0x01, (byte) 0x02, (byte) 0x03};
        DemoBean bean = new DemoBean();
        bean.setBitData(true);
        bean.setUint16Data(42767);
        bean.setInt16Data((short) 32767);
        bean.setUint32Data(3147483647L);
        bean.setInt32Data(2147483647);
        bean.setFloat32Data(3.14f);
        bean.setFloat64Data(4.15);
        bean.setByteData(byteData);
        bean.setStringData("123456789");
        bean.setTimeData(12L);
        bean.setDateData(LocalDate.of(2023, 5, 15));
        bean.setTimeOfDayData(LocalTime.of(20, 22, 13));
        bean.setDateTimeData(LocalDateTime.of(2023, 5, 27, 12, 11, 22, 333225555));
        s7Serializer.write(bean);

        int threadNumber = 10;
        CountDownLatch cdl = new CountDownLatch(threadNumber);
        log.info("start...");
        for (int i = 0; i < threadNumber; i++) {
            int finalI = i;
            new Thread(() -> {
                try {
                    log.info("第[{}]个线程进入并等待", finalI);
                    cdl.await();
                    log.info("第[{}]个线程开始执行", finalI);
                    for (int j = 0; j < 100; j++) {
                        s7Serializer.read(DemoBean.class);
                    }
                    log.info("第[{}]个线程结束执行", finalI);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

            }).start();

            TimeUnit.MILLISECONDS.sleep(1000);
            // 倒计时计数
            cdl.countDown();
        }
        TimeUnit.SECONDS.sleep(20);
        log.info("end...");
    }
}
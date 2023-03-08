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
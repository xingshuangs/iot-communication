package com.github.xingshuangs.iot.net.client;

import junit.framework.TestCase;
import org.junit.Ignore;
import org.junit.Test;

import java.nio.charset.StandardCharsets;

@Ignore
public class UdpClientBasicTest {

    private final UdpClientBasic udpClientBasic = new UdpClientBasic();

    @Test
    public void testWrite() {
        String data = "hello world ";
        udpClientBasic.write(data.getBytes(StandardCharsets.UTF_8));
        byte[] in = new byte[1024];
        int read = udpClientBasic.read(in);
        System.out.println("长度：" + read);
        System.out.println(new String(in));
        read = udpClientBasic.read(in);
        System.out.println("长度：" + read);
        System.out.println(new String(in));
    }

    @Test
    public void testRead() {
    }
}
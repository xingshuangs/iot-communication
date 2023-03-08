package com.github.xingshuangs.iot.net.client;

import junit.framework.TestCase;
import org.junit.Ignore;

import java.nio.charset.StandardCharsets;

@Ignore
public class UdpClientBasicTest extends TestCase {

    private final UdpClientBasic udpClientBasic = new UdpClientBasic();

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

    public void testRead() {
    }
}
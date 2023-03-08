package com.github.xingshuangs.iot.net.server;

import junit.framework.TestCase;
import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;

import java.net.DatagramPacket;
import java.nio.charset.StandardCharsets;

@Ignore
@Slf4j
public class UdpServerBasicTest extends TestCase {

    private final UdpServerBasic udpServerBasic = new UdpServerBasic(8001);

    public void testWrite() {
        byte[] data = new byte[1024];
        DatagramPacket packet = this.udpServerBasic.read(data);
        log.debug(new String(packet.getData()));
        String content = "sadfsdgsgsdf";
        byte[] sendBytes = content.getBytes(StandardCharsets.US_ASCII);
        this.udpServerBasic.write(sendBytes, packet.getSocketAddress());
    }

    public void testRead() {
    }
}
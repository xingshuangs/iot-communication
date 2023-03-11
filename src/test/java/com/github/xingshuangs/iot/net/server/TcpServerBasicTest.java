package com.github.xingshuangs.iot.net.server;

import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

@Ignore
@Slf4j
public class TcpServerBasicTest {

    private final TcpServerBasic serverSocketBasic = new TcpServerBasic();

    @Test
    public void testStart() throws InterruptedException {
        this.serverSocketBasic.start();
        TimeUnit.MINUTES.sleep(10);
    }

    @Test
    public void test1(){
        log.info("test");
        log.error("error");
    }
}
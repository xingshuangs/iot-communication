package com.github.xingshuangs.iot.net.server;

import junit.framework.TestCase;
import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;

import java.util.concurrent.TimeUnit;

@Ignore
@Slf4j
public class ServerSocketBasicTest extends TestCase {

    private final ServerSocketBasic serverSocketBasic = new ServerSocketBasic();

    public void testStart() throws InterruptedException {
        this.serverSocketBasic.start();
        TimeUnit.MINUTES.sleep(10);
    }

    public void test1(){
        log.info("test");
        log.error("error");
    }
}
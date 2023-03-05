package com.github.xingshuangs.iot.protocol.s7.service;

import junit.framework.TestCase;
import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;

import java.util.Set;
import java.util.concurrent.TimeUnit;

@Ignore
@Slf4j
public class S7PLCServerTest extends TestCase {

    private final S7PLCServer server = new S7PLCServer();

    public void testGetAvailableAreas() {
        Set<String> areas = server.getAvailableAreas();
        areas.forEach(log::debug);
        server.addDBArea(1,20,3,4);
        log.debug("----------------------------");
        areas = server.getAvailableAreas();
        areas.forEach(log::debug);
    }

    public void testAddDBArea() throws InterruptedException {
        server.start(102);
        TimeUnit.MINUTES.sleep(10);
    }
}
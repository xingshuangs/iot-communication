package com.github.xingshuangs.iot.protocol.modbus.service;

import com.github.xingshuangs.iot.utils.HexUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

@Ignore
@Slf4j
public class ModbusAsciiOverTcpTest {

    private final ModbusAsciiOverTcp plc = new ModbusAsciiOverTcp();

    @Before
    public void before() {
        this.plc.setComCallback(x -> log.debug("长度[{}]，内容：{}", x.length, HexUtil.toHexString(x)));
        this.plc.setComStringCallback(log::debug);
    }

    @Test
    public void readUInt32Test() {
        long i = plc.readUInt32(0);
        System.out.println(i);
    }

    @Test
    public void readUInt16Test() {
        int i = plc.readUInt16(0);
        System.out.println(i);
    }
}
package com.github.xingshuangs.iot.protocol.modbus.service;


import com.github.xingshuangs.iot.utils.HexUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author xingshuang
 */
@Ignore
@Slf4j
public class ModbusTcpConnectionTest {

    private final ModbusTcp plc = new ModbusTcp(1, "127.0.0.1");

    @Before
    public void before() {
        this.plc.setComCallback(x -> log.debug("长度[{}]，内容：{}", x.length, HexUtil.toHexString(x)));
        this.plc.setPersistence(false);
    }

    @Test
    public void readWriteData() {
        plc.writeInt16(2, (short) 10);
        short data = plc.readInt16(2);
        assertEquals(10, data);

        plc.writeUInt16(3, 20);
        int i = plc.readUInt16(3);
        assertEquals(20, i);

        plc.writeInt32(4, 32);
        int i1 = plc.readInt32(4);
        assertEquals(32, i1);

        plc.writeUInt32(6, 32L);
        long l = plc.readUInt32(6);
        assertEquals(32L, l);

        plc.writeFloat32(8, 12.12f);
        float v = plc.readFloat32(8);
        assertEquals(12.12f, v, 0.0001);

        plc.writeFloat64(10, 33.21);
        double v1 = plc.readFloat64(10);
        assertEquals(33.21, v1, 0.0001);

        plc.writeString(14, "pppp");
        String s = plc.readString(14, 4);
        assertEquals("pppp", s);
    }
}

package com.github.xingshuangs.iot.protocol.s7.service;


import com.github.xingshuangs.iot.protocol.s7.enums.EPlcType;
import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * S7PLC长连接
 *
 * @author xingshuang
 */
@Ignore
@Slf4j
public class S7PLCConnect {

    @Test
    public void longConnect() {
        S7PLC s7PLC = new S7PLC(EPlcType.S1200, "127.0.0.1");
        s7PLC.writeByte("DB2.1", (byte) 0x11);
        s7PLC.readByte("DB2.1");
        s7PLC.close();
    }

    @Test
    public void shortConnect() {
        S7PLC s7PLC = new S7PLC(EPlcType.S1200, "127.0.0.1");
        s7PLC.setPersistence(false);
        s7PLC.writeByte("DB2.1", (byte) 0x11);
        s7PLC.readByte("DB2.1");
    }
}

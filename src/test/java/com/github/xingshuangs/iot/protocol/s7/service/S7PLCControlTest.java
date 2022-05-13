package com.github.xingshuangs.iot.protocol.s7.service;


import com.github.xingshuangs.iot.protocol.s7.enums.EPlcType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author xingshuang
 */
//@Ignore
public class S7PLCControlTest {

//    private S7PLC s7PLC = new S7PLC(EPlcType.S1200);
    private S7PLC s7PLC = new S7PLC(EPlcType.S1200, "192.168.3.98");

    @Test
    public void hotRestart() {
        s7PLC.hotRestart();
    }

    @Test
    public void coldRestart() {
        s7PLC.coldRestart();
    }

    @Test
    public void plcStop() {
        s7PLC.plcStop();
    }

    @Test
    public void copyRamToRom() {
        s7PLC.copyRamToRom();
    }

    @Test
    public void compress() {
        s7PLC.compress();
    }
}

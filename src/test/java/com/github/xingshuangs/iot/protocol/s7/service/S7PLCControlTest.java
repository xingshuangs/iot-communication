package com.github.xingshuangs.iot.protocol.s7.service;


import com.github.xingshuangs.iot.protocol.s7.enums.EPlcType;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author xingshuang
 */
//@Ignore
public class S7PLCControlTest {

    private S7PLC s7PLC = new S7PLC(EPlcType.S1200);

    @Test
    public void hotRestart() {
        s7PLC.HotRestart();
    }

    @Test
    public void coldRestart() {
        s7PLC.ColdRestart();
    }

    @Test
    public void plcStop() {
        s7PLC.PlcStop();
    }
}

package com.github.xingshuangs.iot.protocol.s7.enums;

import com.github.xingshuangs.iot.common.constant.GeneralConst;
import com.github.xingshuangs.iot.protocol.s7.service.S7PLC;
import org.junit.Test;

import static org.junit.Assert.*;


public class EPlcTypeTest {

    @Test
    public void getSlotByType() {
        S7PLC plc = new S7PLC(EPlcType.S200);
        assertEquals(EPlcType.S200, plc.getPlcType());
        assertEquals(0, plc.getRack());
        assertEquals(1, plc.getSlot());
        assertEquals(240, plc.getPduLength());

        plc = new S7PLC(EPlcType.S200_SMART);
        assertEquals(EPlcType.S200_SMART, plc.getPlcType());
        assertEquals(0, plc.getRack());
        assertEquals(1, plc.getSlot());
        assertEquals(240, plc.getPduLength());

        plc = new S7PLC(EPlcType.S300);
        assertEquals(EPlcType.S300, plc.getPlcType());
        assertEquals(0, plc.getRack());
        assertEquals(2, plc.getSlot());
        assertEquals(240, plc.getPduLength());

        plc = new S7PLC(EPlcType.S400, GeneralConst.LOCALHOST);
        assertEquals(EPlcType.S400, plc.getPlcType());
        assertEquals(0, plc.getRack());
        assertEquals(3, plc.getSlot());
        assertEquals(480, plc.getPduLength());

        plc = new S7PLC(EPlcType.S1200, GeneralConst.LOCALHOST,GeneralConst.S7_PORT);
        assertEquals(EPlcType.S1200, plc.getPlcType());
        assertEquals(0, plc.getRack());
        assertEquals(1, plc.getSlot());
        assertEquals(240, plc.getPduLength());

        plc = new S7PLC(EPlcType.S1500);
        assertEquals(EPlcType.S1500, plc.getPlcType());
        assertEquals(0, plc.getRack());
        assertEquals(1, plc.getSlot());
        assertEquals(960, plc.getPduLength());

        plc = new S7PLC(EPlcType.SINUMERIK_828D);
        assertEquals(EPlcType.SINUMERIK_828D, plc.getPlcType());
        assertEquals(0, plc.getRack());
        assertEquals(1, plc.getSlot());
        assertEquals(240, plc.getPduLength());
    }
}
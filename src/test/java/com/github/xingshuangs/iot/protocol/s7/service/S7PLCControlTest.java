package com.github.xingshuangs.iot.protocol.s7.service;


import com.github.xingshuangs.iot.protocol.s7.enums.EFileBlockType;
import com.github.xingshuangs.iot.protocol.s7.enums.EPlcType;
import com.github.xingshuangs.iot.utils.HexUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * @author xingshuang
 */
@Slf4j
@Ignore
public class S7PLCControlTest {

    private S7PLC s7PLC = new S7PLC(EPlcType.S1200);
//    private S7PLC s7PLC = new S7PLC(EPlcType.S200_SMART, "192.168.3.102");

    @Before
    public void before() {
        this.s7PLC.setComCallback(x -> log.debug("长度[{}]，内容：{}", x.length, HexUtil.toHexString(x)));
    }

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

    @Test
    public void insert() {
        s7PLC.insert(EFileBlockType.DB, 1);
    }
}

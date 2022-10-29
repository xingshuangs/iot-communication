package com.github.xingshuangs.iot.protocol.s7.serializer;

import com.github.xingshuangs.iot.protocol.s7.enums.EPlcType;
import com.github.xingshuangs.iot.protocol.s7.service.S7PLC;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

//@Ignore
public class S7SerializerTest {
    S7PLC s7PLC = new S7PLC(EPlcType.S1200, "127.0.0.1");

    @Test
    public void read() {
        S7Serializer s7Serializer = new S7Serializer(s7PLC);
        DemoBean read = s7Serializer.read(DemoBean.class);

        System.out.println(read.toString());
    }

    @Test
    public void write() {
    }
}
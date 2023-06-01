package com.github.xingshuangs.iot.protocol.modbus.service;


import com.github.xingshuangs.iot.utils.HexUtil;

import java.util.List;

/**
 * @author xingshuang
 */
public class DemoReadTest {
    public static void main(String[] args) {
        ModbusTcp plc = new ModbusTcp(1, "127.0.0.1");
        plc.setComCallback(x -> System.out.printf("长度[%d]:%s%n", x.length, HexUtil.toHexString(x)));

        // read coil
        List<Boolean> readCoil = plc.readCoil(0, 2);

        // read discrete input
        List<Boolean> readDiscreteInput = plc.readDiscreteInput(0, 4);

        // read hold register
        byte[] readHoldRegister = plc.readHoldRegister(0, 4);

        // read input register
        byte[] readInputRegister = plc.readInputRegister(0, 2);

        // hold register read Int16
        short readInt16 = plc.readInt16(2);

        // hold register read UInt16
        int readUInt16 = plc.readUInt16(2);

        // hold register read Int32
        int readInt32 = plc.readInt32(2);

        // hold register read Int32
        long readUInt32 = plc.readUInt32(2);

        // hold register read Float32
        float readFloat32 = plc.readFloat32(2);

        // hold register read Float64
        double readFloat64 = plc.readFloat64(2);

        // hold register read String
        String readString = plc.readString(2, 4);

        plc.close();
    }
}

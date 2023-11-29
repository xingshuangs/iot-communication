package com.github.xingshuangs.iot.protocol.modbus.service;


import com.github.xingshuangs.iot.protocol.common.buff.EByteBuffFormat;
import com.github.xingshuangs.iot.utils.HexUtil;

import java.util.List;

/**
 * @author xingshuang
 */
public class DemoReadTest2 {
    public static void main(String[] args) {
        ModbusTcp plc = new ModbusTcp("127.0.0.1");
        // optional
        plc.setComCallback(x -> System.out.printf("长度[%d]:%s%n", x.length, HexUtil.toHexString(x)));

        // read coil
        List<Boolean> readCoil = plc.readCoil(2, 0, 2);

        // read discrete input
        List<Boolean> readDiscreteInput = plc.readDiscreteInput(2, 0, 4);

        // read hold register
        byte[] readHoldRegister = plc.readHoldRegister(2, 0, 4);

        // read input register
        byte[] readInputRegister = plc.readInputRegister(2, 0, 2);

        // hold register read Boolean
        boolean readBoolean = plc.readBoolean(3, 2, 1);

        // hold register read Int16
        short readInt16 = plc.readInt16(3, 2, true);

        // hold register read UInt16
        int readUInt16 = plc.readUInt16(3, 2, false);

        // hold register read Int32
        int readInt32 = plc.readInt32(4, 2, EByteBuffFormat.AB_CD);

        // hold register read Int32
        long readUInt32 = plc.readUInt32(4, 2, EByteBuffFormat.AB_CD);

        // hold register read Float32
        float readFloat32 = plc.readFloat32(4, 2, EByteBuffFormat.AB_CD);

        // hold register read Float64
        double readFloat64 = plc.readFloat64(4, 2, EByteBuffFormat.AB_CD);

        // hold register read String
        String readString = plc.readString(5, 2, 4);

        plc.close();
    }
}

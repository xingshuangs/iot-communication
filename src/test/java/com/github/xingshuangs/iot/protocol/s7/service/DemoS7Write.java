package com.github.xingshuangs.iot.protocol.s7.service;


import com.github.xingshuangs.iot.protocol.s7.enums.EPlcType;

import java.util.List;

/**
 * @author xingshuang
 */
public class DemoS7Write {

    public static void main(String[] args) {
        S7PLC s7PLC = new S7PLC(EPlcType.S1200, "127.0.0.1");
        // write boolean
        s7PLC.writeBoolean("DB2.0.7", true);
        s7PLC.writeBoolean("Q0.7", true);
        s7PLC.writeBoolean("M1.4", true);

        // write byte
        s7PLC.writeByte("DB2.1", (byte) 0x11);
        s7PLC.writeByte("M1", (byte) 0x11);
        s7PLC.writeByte("V1", (byte) 0x11); // 200smart有V区

        // write UInt16
        s7PLC.writeUInt16("DB2.0", 0x2222);

        // write UInt32
        s7PLC.writeUInt32("DB2.0", 0x11111122);

        // write float32
        s7PLC.writeFloat32("DB2.0", 12);

        // write float64
        s7PLC.writeFloat64("DB2.0", 12.02);

        // write String
        s7PLC.writeString("DB14.4", "demo");

        // write multi address
        MultiAddressWrite addressWrite = new MultiAddressWrite();
        addressWrite.addByte("DB2.0", (byte) 0x11)
                .addUInt16("DB2.2", 88)
                .addBoolean("DB2.1.0", true);
        s7PLC.writeMultiData(addressWrite);
    }
}

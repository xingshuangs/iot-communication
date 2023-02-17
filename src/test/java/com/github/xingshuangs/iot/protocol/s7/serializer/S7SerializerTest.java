package com.github.xingshuangs.iot.protocol.s7.serializer;

import com.github.xingshuangs.iot.protocol.s7.enums.EPlcType;
import com.github.xingshuangs.iot.protocol.s7.service.S7PLC;
import com.github.xingshuangs.iot.utils.HexUtil;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

@Ignore
public class S7SerializerTest {
    private S7PLC s7PLC = new S7PLC(EPlcType.S1200, "127.0.0.1");

    @Test
    public void read() {
        s7PLC.setComCallback(x -> System.out.println("长度：" + x.length + ", 内容：" + HexUtil.toHexString(x)));
        S7Serializer s7Serializer = S7Serializer.newInstance(s7PLC);
        DemoBean bean = s7Serializer.read(DemoBean.class);
        System.out.println(bean.toString());
    }

    @Test
    public void write() {
        s7PLC.setComCallback(x -> System.out.println("长度：" + x.length + ", 内容：" + HexUtil.toHexString(x)));
        S7Serializer s7Serializer = S7Serializer.newInstance(s7PLC);
        DemoBean bean = s7Serializer.read(DemoBean.class);
        System.out.println(bean.toString());
        bean.setBitData(true);
        bean.setUint16Data(42767);
        bean.setInt16Data((short) 32767);
        bean.setUint32Data(3147483647L);
        bean.setInt32Data(2147483647);
        bean.setFloat32Data(3.14f);
        bean.setFloat64Data(4.15);
        bean.setByteData(new byte[]{(byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04, (byte) 0x05});
        s7Serializer.write(bean);
    }

    @Test
    public void writeLargeData() {
        s7PLC.setComCallback(x -> System.out.println("长度：" + x.length + ", 内容：" + HexUtil.toHexString(x)));
        S7Serializer s7Serializer = S7Serializer.newInstance(s7PLC);
        DemoLargeBean bean = s7Serializer.read(DemoLargeBean.class);
        System.out.println("-------------------------------");
        bean.getByteData2()[0] = (byte) 0x05;
        bean.getByteData3()[0] = (byte) 0x05;
        bean.getByteData4()[0] = (byte) 0x05;
        bean.getByteData5()[0] = (byte) 0x05;
        bean.getByteData6()[0] = (byte) 0x05;
        bean.getByteData7()[0] = (byte) 0x05;
        bean.getByteData2()[64] = (byte) 0x02;
        bean.getByteData3()[199] = (byte) 0x03;
        bean.getByteData4()[321] = (byte) 0x04;
        bean.getByteData5()[98] = (byte) 0x05;
        bean.getByteData6()[499] = (byte) 0x06;
        bean.getByteData7()[43] = (byte) 0x07;
        s7Serializer.write(bean);
    }

    @Test
    public void dbData() {
        s7PLC.setComCallback(x -> System.out.println("长度：" + x.length + ", 内容：" + HexUtil.toHexString(x)));
        S7Serializer s7Serializer = S7Serializer.newInstance(s7PLC);
        DB80 bean = s7Serializer.read(DB80.class);
        System.out.println(bean);
    }
}
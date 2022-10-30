package com.github.xingshuangs.iot.protocol.s7.serializer;

import com.github.xingshuangs.iot.protocol.s7.enums.EPlcType;
import com.github.xingshuangs.iot.protocol.s7.service.S7PLC;
import com.github.xingshuangs.iot.utils.HexUtil;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

@Ignore
public class S7SerializerTest {
    S7PLC s7PLC = new S7PLC(EPlcType.S1200, "127.0.0.1");

    @Test
    public void read() {
        s7PLC.setComCallback(x -> System.out.println("长度：" + x.length + ", 内容：" + HexUtil.toHexString(x)));
        S7Serializer s7Serializer = S7Serializer.newInstance(s7PLC);
        DemoBean read = s7Serializer.read(DemoBean.class);
        System.out.println(read.toString());
    }

    @Test
    public void write() {
        s7PLC.setComCallback(x -> System.out.println("长度：" + x.length + ", 内容：" + HexUtil.toHexString(x)));
        S7Serializer s7Serializer = S7Serializer.newInstance(s7PLC);
        DemoBean bean = s7Serializer.read(DemoBean.class);
        bean.setBitData(true);
        bean.setByteData(new byte[]{(byte) 0x01, (byte) 0x02, (byte) 0x03});
        bean.setUint16Data(42767);
        bean.setInt16Data((short) 32767);
        bean.setUint32Data(3147483647L);
        bean.setInt32Data(2147483647);
        bean.setFloat32Data(3.14f);
        bean.setFloat64Data(4.15);
        s7Serializer.write(bean);
    }
}
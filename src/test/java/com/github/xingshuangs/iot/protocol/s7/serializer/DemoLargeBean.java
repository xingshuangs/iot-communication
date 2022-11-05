package com.github.xingshuangs.iot.protocol.s7.serializer;


import com.github.xingshuangs.iot.protocol.common.enums.EDataType;
import lombok.Data;

/**
 * 测试对象
 *
 * @author xingshuang
 */
@Data
public class DemoLargeBean {

    @S7Variable(address = "DB1.0.1", type = EDataType.BOOL)
    private boolean bitData;

    @S7Variable(address = "DB1.10", type = EDataType.BYTE, count = 50)
    private byte[] byteData1;

    @S7Variable(address = "DB1.60", type = EDataType.BYTE, count = 65)
    private byte[] byteData2;

    @S7Variable(address = "DB1.125", type = EDataType.BYTE, count = 200)
    private byte[] byteData3;

    @S7Variable(address = "DB1.325", type = EDataType.BYTE, count = 322)
    private byte[] byteData4;

    @S7Variable(address = "DB1.647", type = EDataType.BYTE, count = 99)
    private byte[] byteData5;

    @S7Variable(address = "DB1.746", type = EDataType.BYTE, count = 500)
    private byte[] byteData6;

    @S7Variable(address = "DB1.1246", type = EDataType.BYTE, count = 44)
    private byte[] byteData7;
}

package com.github.xingshuangs.iot.protocol.s7.model;


import com.github.xingshuangs.iot.exceptions.S7CommException;
import com.github.xingshuangs.iot.protocol.s7.enums.EFunctionCode;
import com.github.xingshuangs.iot.utils.ByteUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * 停止参数
 *
 * @author xingshuang
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PlcStopParameter extends Parameter implements IByteArray {

    /**
     * 未知字节，固定参数 <br>
     * 字节大小：5 <br>
     * 字节序数：1-5
     */
    private byte[] unknownBytes = new byte[]{(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00};

    /**
     * 服务名长度 <br>
     * 字节大小：1 <br>
     * 字节序数：6
     */
    private int lengthPart = 0;

    /**
     * 程序调用的服务名
     */
    private String piService = "";

    public void setPiService(String piService) {
        this.piService = piService;
        this.lengthPart = this.piService.length();
    }

    public PlcStopParameter() {
        this.functionCode = EFunctionCode.PLC_STOP;
    }

    @Override
    public int byteArrayLength() {
        return 7 + this.lengthPart;
    }

    @Override
    public byte[] toByteArray() {
        byte[] res = new byte[this.byteArrayLength()];
        res[0] = this.functionCode.getCode();
        System.arraycopy(this.unknownBytes, 0, res, 1, this.unknownBytes.length);
        res[6] = ByteUtil.toByte(this.lengthPart);
        byte[] piServiceBytes = this.piService.getBytes(StandardCharsets.US_ASCII);
        System.arraycopy(piServiceBytes, 0, res, 7, piServiceBytes.length);
        return res;
    }

    /**
     * 字节数组数据解析
     *
     * @param data 字节数组数据
     * @return PlcStopParameter
     */
    public static PlcStopParameter fromBytes(final byte[] data) {
        if (data.length < 7) {
            throw new S7CommException("StopParameter解析有误，StopParameter字节数组长度 < 7");
        }
        PlcStopParameter parameter = new PlcStopParameter();
        parameter.functionCode = EFunctionCode.from(data[0]);
        parameter.unknownBytes = Arrays.copyOfRange(data, 1, 6);
        parameter.lengthPart = ByteUtil.toUInt8(data[6]);
        parameter.piService = parameter.lengthPart == 0 ? "" : new String(data, 7, parameter.lengthPart, StandardCharsets.US_ASCII);
        return parameter;
    }

    public static PlcStopParameter createDefault(){
        PlcStopParameter parameter = new PlcStopParameter();
        parameter.setPiService("P_PROGRAM");
        return parameter;
    }
}

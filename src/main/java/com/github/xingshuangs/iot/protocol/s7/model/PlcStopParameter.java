package com.github.xingshuangs.iot.protocol.s7.model;


import com.github.xingshuangs.iot.exceptions.S7CommException;
import com.github.xingshuangs.iot.protocol.common.IByteArray;
import com.github.xingshuangs.iot.protocol.s7.enums.EFunctionCode;
import com.github.xingshuangs.iot.utils.ByteReadBuff;
import com.github.xingshuangs.iot.utils.ByteWriteBuff;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
     * 服务名长度，后续字节长度，不包含自身 <br>
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
        return ByteWriteBuff.newInstance(7 + this.lengthPart)
                .putByte(this.functionCode.getCode())
                .putBytes(this.unknownBytes)
                .putByte(this.lengthPart)
                .putString(this.piService)
                .getData();
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
        ByteReadBuff buff = new ByteReadBuff(data);
        PlcStopParameter parameter = new PlcStopParameter();
        parameter.functionCode = EFunctionCode.from(buff.getByte());
        parameter.unknownBytes = buff.getBytes(5);
        parameter.lengthPart = buff.getByteToInt();
        parameter.piService = parameter.lengthPart == 0 ? "" : buff.getString(parameter.lengthPart);
        return parameter;
    }

    public static PlcStopParameter createDefault() {
        PlcStopParameter parameter = new PlcStopParameter();
        parameter.setPiService("P_PROGRAM");
        return parameter;
    }
}

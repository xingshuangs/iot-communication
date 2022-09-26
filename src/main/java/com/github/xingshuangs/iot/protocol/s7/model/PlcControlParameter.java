package com.github.xingshuangs.iot.protocol.s7.model;


import com.github.xingshuangs.iot.exceptions.S7CommException;
import com.github.xingshuangs.iot.protocol.common.IByteArray;
import com.github.xingshuangs.iot.protocol.s7.enums.EFunctionCode;
import com.github.xingshuangs.iot.protocol.common.buff.ByteReadBuff;
import com.github.xingshuangs.iot.protocol.common.buff.ByteWriteBuff;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 启动参数
 *
 * @author xingshuang
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PlcControlParameter extends Parameter implements IByteArray {

    public static final String P_PROGRAM = "P_PROGRAM";

    /**
     * 未知字节，固定参数 <br>
     * 字节大小：7 <br>
     * 字节序数：1-7
     */
    private byte[] unknownBytes = new byte[]{(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xFD};

    /**
     * 参数块长度 <br>
     * 字节大小：2 <br>
     * 字节序数：8-9
     */
    private int parameterBlockLength = 0;

    /**
     * 参数块内容
     */
    private String parameterBlock = "";

    /**
     * 服务名长度，后续字节长度，不包含自身 <br>
     * 字节大小：1 <br>
     * 字节序数：不定
     */
    private int lengthPart = 0;

    /**
     * 程序调用的服务名
     */
    private String piService = "";

    public void setParameterBlock(String parameterBlock) {
        this.parameterBlock = parameterBlock;
        this.parameterBlockLength = this.parameterBlock.length();
    }

    public void setPiService(String piService) {
        this.piService = piService;
        this.lengthPart = this.piService.length();
    }

    public PlcControlParameter() {
        this.functionCode = EFunctionCode.PLC_CONTROL;
    }

    @Override
    public int byteArrayLength() {
        return 1 + 7 + 2 + this.parameterBlockLength + 1 + this.lengthPart;
    }

    @Override
    public byte[] toByteArray() {
        return ByteWriteBuff.newInstance(1 + 7 + 2 + this.parameterBlockLength + 1 + this.lengthPart)
                .putByte(this.functionCode.getCode())
                .putBytes(this.unknownBytes)
                .putShort(this.parameterBlockLength)
                .putString(this.parameterBlock)
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
    public static PlcControlParameter fromBytes(final byte[] data) {
        if (data.length < 11) {
            throw new S7CommException("StopParameter解析有误，StopParameter字节数组长度 < 7");
        }
        ByteReadBuff buff = new ByteReadBuff(data);
        PlcControlParameter parameter = new PlcControlParameter();
        parameter.functionCode = EFunctionCode.from(buff.getByte());
        parameter.unknownBytes = buff.getBytes(7);
        parameter.parameterBlockLength = buff.getUInt16();
        parameter.parameterBlock = parameter.parameterBlockLength == 0 ? "" : buff.getString(parameter.parameterBlockLength);
        parameter.lengthPart = buff.getByteToInt();
        parameter.piService = parameter.lengthPart == 0 ? "" : buff.getString(parameter.lengthPart);
        return parameter;
    }

    /**
     * 热重启
     *
     * @return startParameter
     */
    public static PlcControlParameter hotRestart() {
        PlcControlParameter parameter = new PlcControlParameter();
        parameter.setParameterBlock("");
        parameter.setPiService(P_PROGRAM);
        return parameter;
    }

    /**
     * 冷启动
     *
     * @return startParameter
     */
    public static PlcControlParameter coldRestart() {
        PlcControlParameter parameter = new PlcControlParameter();
        parameter.setParameterBlock("C ");
        parameter.setPiService(P_PROGRAM);
        return parameter;
    }

    /**
     * 将ram复制到rom中
     *
     * @return startParameter
     */
    public static PlcControlParameter copyRamToRom() {
        PlcControlParameter parameter = new PlcControlParameter();
        parameter.setParameterBlock("EP");
        parameter.setPiService("_MODU");
        return parameter;
    }

    /**
     * 将ram复制到rom中
     *
     * @return startParameter
     */
    public static PlcControlParameter compress() {
        PlcControlParameter parameter = new PlcControlParameter();
        parameter.setParameterBlock("");
        parameter.setPiService("_GARB");
        return parameter;
    }
}

/*
 * MIT License
 *
 * Copyright (c) 2021-2099 Oscura (xingshuang) <xingshuang_cool@163.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.xingshuangs.iot.protocol.s7.model;


import com.github.xingshuangs.iot.exceptions.S7CommException;
import com.github.xingshuangs.iot.common.IObjectByteArray;
import com.github.xingshuangs.iot.common.buff.ByteReadBuff;
import com.github.xingshuangs.iot.common.buff.ByteWriteBuff;
import com.github.xingshuangs.iot.protocol.s7.enums.EDestinationFileSystem;
import com.github.xingshuangs.iot.protocol.s7.enums.EFileBlockType;
import com.github.xingshuangs.iot.protocol.s7.enums.EFunctionCode;
import com.github.xingshuangs.iot.utils.ByteUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 启动参数
 *
 * @author xingshuang
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PlcControlParameter extends Parameter implements IObjectByteArray {

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
    private PlcControlParamBlock parameterBlock;

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

    public void setParameterBlock(PlcControlParamBlock parameterBlock) {
        this.parameterBlock = parameterBlock;
        this.parameterBlockLength = this.parameterBlock.byteArrayLength();
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
                .putBytes(this.parameterBlock.toByteArray())
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
            throw new S7CommException("PlcControlParameter解析有误，PlcControlParameter字节数组长度 < 11");
        }
        ByteReadBuff buff = new ByteReadBuff(data);
        PlcControlParameter parameter = new PlcControlParameter();
        parameter.functionCode = EFunctionCode.from(buff.getByte());
        parameter.unknownBytes = buff.getBytes(7);
        parameter.parameterBlockLength = buff.getUInt16();
        // TODO: 这里只考虑了字符串这种数据模型，未考虑其他数据模块
        parameter.parameterBlock = parameter.parameterBlockLength == 0 ? new PlcControlParamBlock() : new PlcControlStringParamBlock(buff.getString(parameter.parameterBlockLength));
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
        parameter.setParameterBlock(new PlcControlStringParamBlock());
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
        parameter.setParameterBlock(new PlcControlStringParamBlock("C "));
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
        parameter.setParameterBlock(new PlcControlStringParamBlock("EP"));
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
        parameter.setParameterBlock(new PlcControlStringParamBlock());
        parameter.setPiService("_GARB");
        return parameter;
    }

    /**
     * 创建插入文件指令
     *
     * @param blockType             块类型
     * @param blockNumber           块编号
     * @param destinationFileSystem 目标文件系统
     * @return PlcControlParameter
     */
    public static PlcControlParameter insert(EFileBlockType blockType, int blockNumber, EDestinationFileSystem destinationFileSystem) {
        byte[] data = ByteWriteBuff.newInstance(8)
                .putBytes(blockType.getByteArray())
                .putString(String.format("%05d", blockNumber))
                .putByte(destinationFileSystem.getCode())
                .getData();
        PlcControlInsertParamBlock insertParamBlock = new PlcControlInsertParamBlock();
        insertParamBlock.addFileName(ByteUtil.toStr(data));
        PlcControlParameter parameter = new PlcControlParameter();
        parameter.setParameterBlock(insertParamBlock);
        parameter.setPiService("_INSE");
        return parameter;
    }
}

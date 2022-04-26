package com.github.xingshuangs.iot.protocol.s7.model;


import com.github.xingshuangs.iot.protocol.s7.enums.EDataVariableType;
import com.github.xingshuangs.iot.utils.ShortUtil;
import lombok.Data;

/**
 * 返回数据
 *
 * @author xingshuang
 */
@Data
public class DataItem implements IByteArray {

    /**
     * 返回码 <br>
     * 字节大小：1 <br>
     * 字节序数：0
     */
    private byte returnCode = (byte) 0xFF;

    /**
     * 变量类型 <br>
     * 字节大小：1 <br>
     * 字节序数：1
     */
    private EDataVariableType variableType = EDataVariableType.BYTE_WORD_DWORD;

    /**
     * 数据长度 <br>
     * 字节大小：2 <br>
     * 字节序数：2-3
     */
    private int count = 0x0000;

    /**
     * 数据内容
     */
    private byte[] data = new byte[0];

    @Override
    public int byteArrayLength() {
        return 4 + this.data.length;
    }

    @Override
    public byte[] toByteArray() {
        byte[] res = new byte[4 + this.data.length];
        byte[] countBytes = ShortUtil.toByteArray((short) this.count);

        res[0] = this.returnCode;
        res[1] = this.variableType.getCode();
        res[2] = countBytes[0];
        res[3] = countBytes[1];

        if (this.data.length > 0) {
            System.arraycopy(this.data, 0, res, 4, this.data.length);
        }
        return res;
    }
}

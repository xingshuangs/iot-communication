package com.github.xingshuangs.iot.protocol.s7.model;


import com.github.xingshuangs.iot.protocol.s7.enums.EArea;
import com.github.xingshuangs.iot.protocol.s7.enums.EParamVariableType;
import com.github.xingshuangs.iot.protocol.s7.enums.ESyntaxID;
import com.github.xingshuangs.iot.utils.ByteUtil;
import com.github.xingshuangs.iot.utils.IntegerUtil;
import com.github.xingshuangs.iot.utils.ShortUtil;
import lombok.Data;

/**
 * 请求项
 *
 * @author xingshuang
 */
@Data
public class RequestItem implements IByteArray {

    public static final int BYTE_LENGTH = 12;

    /**
     * 变量规范，对于读/写消息，它总是具有值0x12 <br>
     * 字节大小：1 <br>
     * 字节序数：0
     */
    private byte specificationType = (byte) 0x12;

    /**
     * 其余部分的长度规范 <br>
     * 字节大小：1 <br>
     * 字节序数：1
     */
    private int lengthOfFollowing = 0x10;

    /**
     * 寻址模式和项结构其余部分的格式，它具有任意类型寻址的常量值0x10 <br>
     * 字节大小：1 <br>
     * 字节序数：2
     */
    private ESyntaxID syntaxId = ESyntaxID.S7ANY;

    /**
     * 变量的类型和长度BIT，BYTE，WORD，DWORD，COUNTER <br>
     * 字节大小：1 <br>
     * 字节序数：3
     */
    private EParamVariableType variableType = EParamVariableType.BYTE;

    /**
     * 读取长度 <br>
     * 字节大小：2 <br>
     * 字节序数：4-5
     */
    private int count = 0x0000;

    /**
     * 即 DB 编号，如果访问的不是DB区域，此处为0x0000 <br>
     * 字节大小：2 <br>
     * 字节序数：6-7
     */
    private int dbNumber = 0x0000;

    /**
     * 存储区类型DB存储区 <br>
     * 字节大小：1 <br>
     * 字节序数：8
     */
    private EArea area = EArea.DATA_BLOCKS;

    /**
     * 字节地址，位于开始字节地址address中3个字节，从第4位开始计数 <br>
     * 字节大小：3 <br>
     * 字节序数：9-11
     */
    private int byteAddress = 0;

    /**
     * 位地址，位于开始字节地址address中3个字节的最后3位
     */
    private int bitAddress = 0;

    @Override
    public int byteArrayLength() {
        return BYTE_LENGTH;
    }

    @Override
    public byte[] toByteArray() {
        byte[] res = new byte[BYTE_LENGTH];
        byte[] countBytes = ShortUtil.toByteArray(this.count);
        byte[] dbNumberBytes = ShortUtil.toByteArray(this.dbNumber);
        byte[] addressBytes = IntegerUtil.toByteArray((this.byteAddress << 3) + this.bitAddress);

        res[0] = this.specificationType;
        res[1] = ByteUtil.toByte(this.lengthOfFollowing);
        res[2] = this.syntaxId.getCode();
        res[3] = this.variableType.getCode();
        res[4] = countBytes[0];
        res[5] = countBytes[1];
        res[6] = dbNumberBytes[0];
        res[7] = dbNumberBytes[1];
        res[8] = this.area.getCode();
        // 只有3个字节，因此只取后面的3字节，第一个字节舍弃
        res[9] = addressBytes[1];
        res[10] = addressBytes[2];
        res[11] = addressBytes[3];
        return res;
    }

    public static RequestItem fromBytes(final byte[] data) {
        RequestItem requestItem = new RequestItem();
        requestItem.specificationType = data[0];
        requestItem.lengthOfFollowing = ByteUtil.toUInt8(data[1]);
        requestItem.syntaxId = ESyntaxID.from(data[2]);
        requestItem.variableType = EParamVariableType.from(data[3]);
        requestItem.count = ShortUtil.toUInt16(data, 4);
        requestItem.dbNumber = ShortUtil.toUInt16(data, 6);
        requestItem.byteAddress = IntegerUtil.toInt32In3Bytes(data, 9) >> 3;
        requestItem.bitAddress = data[11] & 0x07;
        return requestItem;
    }
}

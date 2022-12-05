package com.github.xingshuangs.iot.protocol.s7.model;


import com.github.xingshuangs.iot.protocol.common.IObjectByteArray;
import com.github.xingshuangs.iot.protocol.common.buff.ByteReadBuff;
import com.github.xingshuangs.iot.protocol.common.buff.ByteWriteBuff;
import com.github.xingshuangs.iot.protocol.s7.enums.EArea;
import com.github.xingshuangs.iot.protocol.s7.enums.EParamVariableType;
import com.github.xingshuangs.iot.protocol.s7.enums.ESyntaxID;
import com.github.xingshuangs.iot.utils.*;
import lombok.Data;

/**
 * 请求项
 *
 * @author xingshuang
 */
@Data
public class RequestItem implements IObjectByteArray {

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
    private int lengthOfFollowing = 0x0A;

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
        return ByteWriteBuff.newInstance(BYTE_LENGTH)
                .putByte(this.specificationType)
                .putByte(this.lengthOfFollowing)
                .putByte(this.syntaxId.getCode())
                .putByte(this.variableType.getCode())
                .putShort(this.count)
                .putShort(this.dbNumber)
                .putByte(this.area.getCode())
                // 只有3个字节，因此只取后面的3字节，第一个字节舍弃
                .putBytes(IntegerUtil.toByteArray((this.byteAddress << 3) + this.bitAddress), 1)
                .getData();
    }

    /**
     * 复制一个新对象
     *
     * @return requestItem
     */
    public RequestItem copy() {
        RequestItem requestItem = new RequestItem();
        requestItem.specificationType = this.specificationType;
        requestItem.lengthOfFollowing = this.lengthOfFollowing;
        requestItem.syntaxId = this.syntaxId;
        requestItem.variableType = this.variableType;
        requestItem.count = this.count;
        requestItem.area = this.area;
        requestItem.dbNumber = this.dbNumber;
        requestItem.byteAddress = this.byteAddress;
        requestItem.bitAddress = this.bitAddress;
        return requestItem;
    }

    /**
     * 字节数组数据解析
     *
     * @param data 字节数组数据
     * @return RequestItem
     */
    public static RequestItem fromBytes(final byte[] data) {
        ByteReadBuff buff = new ByteReadBuff(data);
        RequestItem requestItem = new RequestItem();
        requestItem.specificationType = buff.getByte();
        requestItem.lengthOfFollowing = buff.getByteToInt();
        requestItem.syntaxId = ESyntaxID.from(buff.getByte());
        requestItem.variableType = EParamVariableType.from(buff.getByte());
        requestItem.count = buff.getUInt16();
        requestItem.dbNumber = buff.getUInt16();
        requestItem.byteAddress = IntegerUtil.toInt32In3Bytes(data, 9) >> 3;
        requestItem.bitAddress = buff.getByte(11) & 0x07;
        return requestItem;
    }

    /**
     * 通过参数创建请求项
     *
     * @param variableType 参数类型
     * @param count        数据个数
     * @param area         区域
     * @param dbNumber     DB块编号
     * @param byteAddress  字节地址
     * @param bitAddress   位地址
     * @return 请求项
     */
    public static RequestItem createByParams(EParamVariableType variableType, int count, EArea area, int dbNumber, int byteAddress, int bitAddress) {
        RequestItem requestItem = new RequestItem();
        requestItem.setVariableType(variableType);
        requestItem.setCount(count);
        requestItem.setArea(area);
        requestItem.setDbNumber(dbNumber);
        requestItem.setByteAddress(byteAddress);
        requestItem.setBitAddress(bitAddress);
        return requestItem;
    }
}

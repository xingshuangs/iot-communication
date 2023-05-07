package com.github.xingshuangs.iot.protocol.s7.model;


import com.github.xingshuangs.iot.protocol.common.buff.ByteReadBuff;
import com.github.xingshuangs.iot.protocol.common.buff.ByteWriteBuff;
import com.github.xingshuangs.iot.protocol.s7.enums.ENckArea;
import com.github.xingshuangs.iot.protocol.s7.enums.ENckModule;
import com.github.xingshuangs.iot.protocol.s7.enums.ESyntaxID;
import lombok.Data;

/**
 * NCK请求项
 *
 * @author xingshuang
 */
@Data
public class RequestNckItem extends RequestBaseItem {

    public static final int BYTE_LENGTH = 10;

    public RequestNckItem() {
        this.specificationType = (byte) 0x12;
        this.lengthOfFollowing = 0x08;
        this.syntaxId = ESyntaxID.NCK;
    }

    public RequestNckItem(ENckArea area, int uint, int columnNumber, int lineNumber, ENckModule module, int lineCount) {
        this.specificationType = (byte) 0x12;
        this.lengthOfFollowing = 0x08;
        this.syntaxId = ESyntaxID.NCK;

        this.area = area;
        this.uint = uint;
        this.columnNumber = columnNumber;
        this.lineNumber = lineNumber;
        this.module = module;
        this.lineCount = lineCount;
    }

    /**
     * NCK区域 <br>
     * 字节大小：1 <br>
     * 字节序数：3
     */
    private ENckArea area = ENckArea.N_NCK;

    /**
     * 单位 <br>
     * 字节大小：1 <br>
     * 字节序数：4
     */
    private int uint = 0x0000;

    /**
     * 列编号 <br>
     * 字节大小：2 <br>
     * 字节序数：5
     */
    private int columnNumber = 0x0000;

    /**
     * 行编号 <br>
     * 字节大小：2 <br>
     * 字节序数：7
     */
    private int lineNumber = 0x0000;

    /**
     * 模块名 <br>
     * 字节大小：1 <br>
     * 字节序数：8
     */
    private ENckModule module = ENckModule.S;

    /**
     * 行个数 <br>
     * 字节大小：1 <br>
     * 字节序数：9
     */
    private int lineCount = 1;

    @Override
    public int byteArrayLength() {
        return BYTE_LENGTH;
    }

    @Override
    public byte[] toByteArray() {
        byte areaAndUint = (byte) (((this.area.getCode() << 5) & (byte) 0xE0)
                | (this.uint & (byte) 0x1F));
        return ByteWriteBuff.newInstance(BYTE_LENGTH)
                .putByte(this.specificationType)
                .putByte(this.lengthOfFollowing)
                .putByte(this.syntaxId.getCode())
                .putByte(areaAndUint)
                .putShort(this.columnNumber)
                .putShort(this.lineNumber)
                .putByte(this.module.getCode())
                .putByte(this.lineCount)
                .getData();
    }

    /**
     * 复制一个新对象
     *
     * @return requestItem
     */
    public RequestNckItem copy() {
        RequestNckItem requestItem = new RequestNckItem();
        requestItem.specificationType = this.specificationType;
        requestItem.lengthOfFollowing = this.lengthOfFollowing;
        requestItem.syntaxId = this.syntaxId;
        requestItem.area = this.area;
        requestItem.uint = this.uint;
        requestItem.columnNumber = this.columnNumber;
        requestItem.lineNumber = this.lineNumber;
        requestItem.module = this.module;
        requestItem.lineCount = this.lineCount;
        return requestItem;
    }

    public static RequestNckItem fromBytes(final byte[] data) {
        return fromBytes(data, 0);
    }

    /**
     * 字节数组数据解析
     *
     * @param data   字节数组数据
     * @param offset 偏移量
     * @return RequestItem
     */
    public static RequestNckItem fromBytes(final byte[] data, final int offset) {
        ByteReadBuff buff = new ByteReadBuff(data, offset);
        RequestNckItem requestItem = new RequestNckItem();
        requestItem.specificationType = buff.getByte();
        requestItem.lengthOfFollowing = buff.getByteToInt();
        requestItem.syntaxId = ESyntaxID.from(buff.getByte());
        byte areaAndUnit = buff.getByte();
        requestItem.area = ENckArea.from((byte) ((areaAndUnit & (byte) 0xE0) >> 5));
        requestItem.uint = areaAndUnit & (byte) 0x1F;
        requestItem.columnNumber = buff.getUInt16();
        requestItem.lineNumber = buff.getUInt16();
        requestItem.module = ENckModule.from(buff.getByte());
        requestItem.lineCount = buff.getByteToInt();
        return requestItem;
    }


    public static RequestNckItem createByParams(ENckArea area, int unit, int columnNumber, int lineNumber, ENckModule module, int lineCount) {
        RequestNckItem requestItem = new RequestNckItem();
        requestItem.area = area;
        requestItem.uint = unit;
        requestItem.columnNumber = columnNumber;
        requestItem.lineNumber = lineNumber;
        requestItem.module = module;
        requestItem.lineCount = lineCount;
        return requestItem;
    }
}

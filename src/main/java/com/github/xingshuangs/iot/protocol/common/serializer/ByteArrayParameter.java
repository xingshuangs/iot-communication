package com.github.xingshuangs.iot.protocol.common.serializer;


import com.github.xingshuangs.iot.protocol.common.enums.EDataType;
import lombok.Data;

/**
 * 字节数组参数
 *
 * @author xingshuang
 */
@Data
public class ByteArrayParameter {

    /**
     * 字节偏移量
     */
    private int byteOffset = 0;

    /**
     * 位偏移量
     */
    private int bitOffset = 0;

    /**
     * 数量，数量大于1的时候对应的数据必须使用list
     */
    private int count = 1;

    /**
     * 类型
     */
    private EDataType type = EDataType.BYTE;

    /**
     * 是否小端模式
     */
    private boolean littleEndian = false;

    /**
     * 具体的值
     */
    private Object value;

    public ByteArrayParameter() {
    }

    public ByteArrayParameter(int byteOffset, int bitOffset, int count, EDataType type) {
        this.byteOffset = byteOffset;
        this.bitOffset = bitOffset;
        this.count = count;
        this.type = type;
    }

    public ByteArrayParameter(int byteOffset, int bitOffset, int count, EDataType type, boolean littleEndian) {
        this.byteOffset = byteOffset;
        this.bitOffset = bitOffset;
        this.count = count;
        this.type = type;
        this.littleEndian = littleEndian;
    }
}

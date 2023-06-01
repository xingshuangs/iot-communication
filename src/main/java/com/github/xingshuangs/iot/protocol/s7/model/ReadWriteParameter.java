package com.github.xingshuangs.iot.protocol.s7.model;


import com.github.xingshuangs.iot.exceptions.S7CommException;
import com.github.xingshuangs.iot.protocol.common.IObjectByteArray;
import com.github.xingshuangs.iot.protocol.s7.enums.EFunctionCode;
import com.github.xingshuangs.iot.protocol.common.buff.ByteReadBuff;
import com.github.xingshuangs.iot.protocol.common.buff.ByteWriteBuff;
import com.github.xingshuangs.iot.protocol.s7.enums.ESyntaxID;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 读写参数
 *
 * @author xingshuang
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ReadWriteParameter extends Parameter implements IObjectByteArray {

    /**
     * Request Item结构的数量 <br>
     * 字节大小：1 <br>
     * 字节序数：1
     */
    private int itemCount = 0x00;

    /**
     * 可重复的请求项
     */
    private List<RequestBaseItem> requestItems = new ArrayList<>();

    /**
     * 添加请求项
     *
     * @param item 项
     */
    public void addItem(RequestBaseItem item) {
        this.requestItems.add(item);
        this.itemCount = this.requestItems.size();
    }

    /**
     * 添加请求项列表
     *
     * @param items 请求项列表
     */
    public void addItem(Collection<? extends RequestBaseItem> items) {
        this.requestItems.addAll(items);
        this.itemCount = this.requestItems.size();
    }

    @Override
    public int byteArrayLength() {
        return 2 + this.requestItems.stream().mapToInt(RequestBaseItem::byteArrayLength).sum();
    }

    @Override
    public byte[] toByteArray() {
        int length = 2 + this.requestItems.stream().mapToInt(RequestBaseItem::byteArrayLength).sum();
        ByteWriteBuff buff = ByteWriteBuff.newInstance(length)
                .putByte(this.functionCode.getCode())
                .putByte(this.itemCount);
        for (RequestBaseItem requestItem : this.requestItems) {
            buff.putBytes(requestItem.toByteArray());
        }
        return buff.getData();
    }

    /**
     * 字节数组数据解析
     *
     * @param data 字节数组数据
     * @return ReadWriteParameter
     */
    public static ReadWriteParameter fromBytes(final byte[] data) {
        if (data.length < 2) {
            throw new S7CommException("Parameter解析有误，parameter字节数组长度 < 2");
        }
        ByteReadBuff buff = new ByteReadBuff(data);
        ReadWriteParameter readWriteParameter = new ReadWriteParameter();
        readWriteParameter.functionCode = EFunctionCode.from(buff.getByte());
        readWriteParameter.itemCount = buff.getByteToInt();
        if (readWriteParameter.itemCount == 0) {
            return readWriteParameter;
        }
        // 读写返回时，只有功能码和个数
        if (data.length == 2) {
            return readWriteParameter;
        }
        int off = 2;
        for (int i = 0; i < readWriteParameter.itemCount; i++) {
            RequestBaseItem item = parserItem(data, off);
            readWriteParameter.requestItems.add(item);
            off += item.byteArrayLength();
        }
        return readWriteParameter;
    }

    /**
     * 解析字节数组数据
     *
     * @param data   字节数组数据
     * @param offset 偏移量
     * @return RequestBaseItem
     */
    public static RequestBaseItem parserItem(final byte[] data, final int offset) {
        ByteReadBuff buff = new ByteReadBuff(data, offset);
        byte aByte = buff.getByte(2 + offset);
        ESyntaxID syntaxID = ESyntaxID.from(aByte);
        switch (syntaxID) {
            case S7ANY:
                return RequestItem.fromBytes(data, offset);
            case NCK:
                return RequestNckItem.fromBytes(data, offset);
            default:
                throw new S7CommException("无法解析RequestBaseItem对应的类型");
        }
    }

    /**
     * 创建默认的请求参数
     *
     * @param functionCode 功能码
     * @param requestItems 请求项
     * @return ReadWriteParameter
     */
    public static ReadWriteParameter createReqParameter(EFunctionCode functionCode, Collection<? extends RequestBaseItem> requestItems) {
        ReadWriteParameter parameter = new ReadWriteParameter();
        parameter.functionCode = functionCode;
        parameter.addItem(requestItems);
        return parameter;
    }

    /**
     * 创建响应参数
     *
     * @param request 对应请求对象
     * @return 读写参数
     */
    public static ReadWriteParameter createAckParameter(ReadWriteParameter request) {
        ReadWriteParameter parameter = new ReadWriteParameter();
        parameter.functionCode = request.functionCode;
        parameter.itemCount = request.itemCount;
        return parameter;
    }
}

package com.github.xingshuangs.iot.protocol.s7.model;


import com.github.xingshuangs.iot.exceptions.S7CommException;
import com.github.xingshuangs.iot.protocol.s7.enums.EFunctionCode;
import com.github.xingshuangs.iot.utils.ByteWriteBuff;
import com.github.xingshuangs.iot.utils.ByteUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 读写参数
 *
 * @author xingshuang
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ReadWriteParameter extends Parameter implements IByteArray {

    /**
     * Request Item结构的数量 <br>
     * 字节大小：1 <br>
     * 字节序数：1
     */
    private int itemCount = 0x01;

    /**
     * 可重复的请求项
     */
    private List<RequestItem> requestItems = new ArrayList<>();

    /**
     * 添加请求项
     *
     * @param item 项
     */
    public void addItem(RequestItem item) {
        this.requestItems.add(item);
        this.itemCount = this.requestItems.size();
    }

    public void addItem(List<RequestItem> items) {
        this.requestItems.addAll(items);
        this.itemCount = this.requestItems.size();
    }

    @Override
    public int byteArrayLength() {
        return 2 + this.requestItems.stream().mapToInt(RequestItem::byteArrayLength).sum();
    }

    @Override
    public byte[] toByteArray() {
        int length = 2 + this.requestItems.stream().mapToInt(RequestItem::byteArrayLength).sum();
        ByteWriteBuff buff = ByteWriteBuff.newInstance(length)
                .putByte(this.functionCode.getCode())
                .putByte(this.itemCount);
        for (RequestItem requestItem : this.requestItems) {
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
        ReadWriteParameter readWriteParameter = new ReadWriteParameter();
        readWriteParameter.functionCode = EFunctionCode.from(data[0]);
        readWriteParameter.itemCount = ByteUtil.toUInt8(data[1]);
        if (readWriteParameter.itemCount == 0) {
            return readWriteParameter;
        }
        // 读写返回时，只有功能码和个数
        if (data.length == 2) {
            return readWriteParameter;
        }
        for (int i = 1; i <= readWriteParameter.itemCount; i++) {
            byte[] bytes = Arrays.copyOfRange(data, 2, 2 + i * RequestItem.BYTE_LENGTH);
            readWriteParameter.requestItems.add(RequestItem.fromBytes(bytes));
        }
        return readWriteParameter;
    }

    /**
     * 创建默认的读取参数
     *
     * @return ReadWriteParameter
     */
    public static ReadWriteParameter createReadDefault() {
        ReadWriteParameter parameter = new ReadWriteParameter();
        parameter.functionCode = EFunctionCode.READ_VARIABLE;
        parameter.itemCount = 0;
        return parameter;
    }

    /**
     * 创建默认的写入参数
     *
     * @return ReadWriteParameter
     */
    public static ReadWriteParameter createWriteDefault() {
        ReadWriteParameter parameter = new ReadWriteParameter();
        parameter.functionCode = EFunctionCode.WRITE_VARIABLE;
        parameter.itemCount = 0;
        return parameter;
    }
}

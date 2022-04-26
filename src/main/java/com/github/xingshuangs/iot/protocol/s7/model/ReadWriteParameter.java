package com.github.xingshuangs.iot.protocol.s7.model;


import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
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
    private byte itemCount = (byte) 0x01;

    /**
     * 可重复的请求项
     */
    private List<RequestItem> requestItems = new ArrayList<>();

    @Override
    public int getByteArrayLength() {
        return 2 + this.requestItems.stream().mapToInt(RequestItem::getByteArrayLength).sum();
    }

    @Override
    public byte[] toByteArray() {
        byte[] res = new byte[this.getByteArrayLength()];
        res[0] = this.getFunctionCode().getCode();
        res[1] = this.itemCount;
        int count = 0;
        for (RequestItem requestItem : this.requestItems) {
            byte[] bytes = requestItem.toByteArray();
            System.arraycopy(bytes, 0, res, 2 + count, bytes.length);
            count += bytes.length;
        }
        return res;
    }
}

package com.github.xingshuangs.iot.protocol.s7.model;


import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据
 *
 * @author xingshuang
 */
@Data
public class Datum implements IByteArray {

    /**
     * 数据项
     */
    private List<DataItem> dataItems = new ArrayList<>();

    @Override
    public int getByteArrayLength() {
        return this.dataItems.stream().mapToInt(DataItem::getByteArrayLength).sum();
    }

    @Override
    public byte[] toByteArray() {
        if (this.dataItems.isEmpty()) {
            return new byte[0];
        }
        byte[] res = new byte[this.getByteArrayLength()];
        int count = 0;
        for (DataItem item : this.dataItems) {
            byte[] bytes = item.toByteArray();
            System.arraycopy(bytes, 0, res, count, bytes.length);
            count += bytes.length;
        }
        return res;
    }
}

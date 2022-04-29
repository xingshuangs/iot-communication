package com.github.xingshuangs.iot.protocol.s7.model;


import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
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
    public int byteArrayLength() {
        return this.dataItems.stream().mapToInt(DataItem::byteArrayLength).sum();
    }

    @Override
    public byte[] toByteArray() {
        if (this.dataItems.isEmpty()) {
            return new byte[0];
        }
        byte[] res = new byte[this.byteArrayLength()];
        int count = 0;
        for (DataItem item : this.dataItems) {
            byte[] bytes = item.toByteArray();
            System.arraycopy(bytes, 0, res, count, bytes.length);
            count += bytes.length;
        }
        return res;
    }

    public static Datum fromBytes(final byte[] data) {
        Datum datum = new Datum();
        if (data.length == 0) {
            return datum;
        }
        int offset = 0;
        byte[] remain = data;
        while (offset < data.length) {
            DataItem dataItem = DataItem.fromBytes(remain);
            datum.dataItems.add(dataItem);
            offset += dataItem.byteArrayLength();
            remain = Arrays.copyOfRange(remain, offset, remain.length);
        }
        return datum;
    }
}

package com.github.xingshuangs.iot.protocol.s7.model;


import com.github.xingshuangs.iot.protocol.s7.enums.EFunctionCode;
import com.github.xingshuangs.iot.protocol.s7.enums.EMessageType;
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
    private List<ReturnItem> returnItems = new ArrayList<>();

    @Override
    public int byteArrayLength() {
        return this.returnItems.stream().mapToInt(ReturnItem::byteArrayLength).sum();
    }

    @Override
    public byte[] toByteArray() {
        if (this.returnItems.isEmpty()) {
            return new byte[0];
        }
        byte[] res = new byte[this.byteArrayLength()];
        int count = 0;
        for (ReturnItem item : this.returnItems) {
            byte[] bytes = item.toByteArray();
            System.arraycopy(bytes, 0, res, count, bytes.length);
            count += bytes.length;
        }
        return res;
    }

    public static Datum fromBytes(final byte[] data, EMessageType messageType, EFunctionCode functionCode) {
        Datum datum = new Datum();
        if (data.length == 0) {
            return datum;
        }
        int offset = 0;
        byte[] remain = data;
        while (offset < data.length) {
            ReturnItem dataItem;
            // 对写操作的响应结果进行特殊处理
            if (EMessageType.ACK_DATA == messageType && EFunctionCode.WRITE_VARIABLE == functionCode) {
                dataItem = ReturnItem.fromBytes(data);
            } else {
                dataItem = DataItem.fromBytes(remain);
            }

            datum.returnItems.add(dataItem);
            offset += dataItem.byteArrayLength();
            remain = Arrays.copyOfRange(remain, offset, remain.length);
        }
        return datum;
    }
}

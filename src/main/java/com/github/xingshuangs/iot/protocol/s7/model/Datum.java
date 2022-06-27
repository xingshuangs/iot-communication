package com.github.xingshuangs.iot.protocol.s7.model;


import com.github.xingshuangs.iot.protocol.s7.enums.EFunctionCode;
import com.github.xingshuangs.iot.protocol.s7.enums.EMessageType;
import com.github.xingshuangs.iot.utils.ByteWriteBuff;
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
        if (this.returnItems.isEmpty()) {
            return 0;
        }
        int sum = 0;
        for (int i = 0; i < this.returnItems.size(); i++) {
            sum += this.returnItems.get(i).byteArrayLength();
            if (this.returnItems.get(i) instanceof DataItem) {
                DataItem dataItem = (DataItem) this.returnItems.get(i);
                // 如果数据长度为奇数，S7协议会多填充一个字节，使其保持为偶数（最后一个奇数长度数据不需要填充）
                if (dataItem.getCount() % 2 != 0 && i != this.returnItems.size() - 1) {
                    sum++;
                }
            }
        }
        return sum;
    }

    @Override
    public byte[] toByteArray() {
        if (this.returnItems.isEmpty()) {
            return new byte[0];
        }
        ByteWriteBuff buff = ByteWriteBuff.newInstance(this.byteArrayLength());
        for (int i = 0; i < this.returnItems.size(); i++) {
            buff.putBytes(this.returnItems.get(i).toByteArray());
            if (this.returnItems.get(i) instanceof DataItem) {
                DataItem dataItem = (DataItem) this.returnItems.get(i);
                // 如果数据长度为奇数，S7协议会多填充一个字节，使其保持为偶数（最后一个奇数长度数据不需要填充）
                if (dataItem.getCount() % 2 != 0 && i != this.returnItems.size() - 1) {
                    buff.putByte((byte)0x00);
                }
            }
        }
        return buff.getData();
    }

    /**
     * 添加请求项
     *
     * @param item 项
     */
    public void addItem(DataItem item) {
        this.returnItems.add(item);
    }

    public void addItem(List<DataItem> items) {
        this.returnItems.addAll(items);
    }

    /**
     * 根据消息类型和功能码，对字节数组数据进行解析
     *
     * @param data         字节数组数据
     * @param messageType  头部的消息类型
     * @param functionCode 参数部分的功能码
     * @return Datum
     */
    public static Datum fromBytes(final byte[] data, EMessageType messageType, EFunctionCode functionCode) {
        Datum datum = new Datum();
        if (data.length == 0) {
            return datum;
        }
        int offset = 0;
        byte[] remain = data;
        while (true) {
            ReturnItem dataItem;
            // 对写操作的响应结果进行特殊处理
            if (EMessageType.ACK_DATA == messageType && EFunctionCode.WRITE_VARIABLE == functionCode) {
                dataItem = ReturnItem.fromBytes(data);
            } else {
                DataItem item = DataItem.fromBytes(remain);
                dataItem = item;
                // 如果数据长度为奇数，S7协议会多填充一个字节，使其保持为偶数，需要跳过这个字节
                if (item.getCount() % 2 != 0) {
                    offset++;
                }
            }

            datum.returnItems.add(dataItem);
            offset += dataItem.byteArrayLength();
            if (offset >= data.length) {
                break;
            }
            remain = Arrays.copyOfRange(data, offset, data.length);
        }
        return datum;
    }
}

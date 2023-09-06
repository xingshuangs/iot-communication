package com.github.xingshuangs.iot.protocol.s7.model;


import com.github.xingshuangs.iot.protocol.common.buff.ByteWriteBuff;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * PLC控制参数块，插入功能
 *
 * @author xingshuang
 */
@Data
public class PlcControlInsertParamBlock extends PlcControlParamBlock {

    private byte unknowByte = 0x00;

    private List<String> fileNames = new ArrayList<>();

    public void addFileName(String filename) {
        this.fileNames.add(filename);
    }

    @Override
    public int byteArrayLength() {
        int sum = 2;
        for (String item : fileNames) {
            sum += item.length();
        }
        return sum;
    }

    @Override
    public byte[] toByteArray() {
        ByteWriteBuff buff = ByteWriteBuff.newInstance(this.byteArrayLength())
                .putByte(this.fileNames.size())
                .putByte(this.unknowByte);
        for (String item : fileNames) {
            buff.putString(item);
        }
        return buff.getData();
    }
}

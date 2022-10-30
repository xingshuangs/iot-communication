package com.github.xingshuangs.iot.protocol.s7.model;


import com.github.xingshuangs.iot.protocol.common.IObjectByteArray;
import com.github.xingshuangs.iot.protocol.s7.enums.EReturnCode;
import com.github.xingshuangs.iot.protocol.common.buff.ByteWriteBuff;
import lombok.Data;

/**
 * 返回项
 *
 * @author xingshuang
 */
@Data
public class ReturnItem implements IObjectByteArray {

    /**
     * 返回码 <br>
     * 字节大小：1 <br>
     * 字节序数：0
     */
    protected EReturnCode returnCode = EReturnCode.SUCCESS;

    @Override
    public int byteArrayLength() {
        return 1;
    }

    @Override
    public byte[] toByteArray() {
        return ByteWriteBuff.newInstance(this.byteArrayLength())
                .putByte(returnCode.getCode())
                .getData();
    }

    /**
     * 字节数组数据解析
     *
     * @param data 字节数组数据
     * @return ReturnItem
     */
    public static ReturnItem fromBytes(final byte[] data) {
        ReturnItem returnItem = new ReturnItem();
        returnItem.returnCode = EReturnCode.from(data[0]);
        return returnItem;
    }
}

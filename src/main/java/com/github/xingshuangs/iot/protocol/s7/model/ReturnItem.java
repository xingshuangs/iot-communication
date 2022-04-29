package com.github.xingshuangs.iot.protocol.s7.model;


import com.github.xingshuangs.iot.protocol.s7.enums.EReturnCode;
import lombok.Data;

/**
 * 返回项
 *
 * @author xingshuang
 */
@Data
public class ReturnItem implements IByteArray {

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
        byte[] res = new byte[1];
        res[0] = returnCode.getCode();
        return res;
    }

    public static ReturnItem fromBytes(final byte[] data) {
        ReturnItem returnItem = new ReturnItem();
        returnItem.returnCode = EReturnCode.from(data[0]);
        return returnItem;
    }
}

package com.github.xingshuangs.iot.protocol.s7.model;


import com.github.xingshuangs.iot.protocol.s7.enums.EErrorClass;
import com.github.xingshuangs.iot.protocol.s7.enums.EMessageType;
import com.github.xingshuangs.iot.utils.ShortUtil;
import lombok.Data;

/**
 * 头部
 *
 * @author xingshuang
 */
@Data
public class Header implements IByteArray {

    /**
     * 协议id <br>
     * 字节大小：1 <br>
     * 字节序数：0
     */
    private byte protocolId = (byte)0x32;

    /**
     * pdu（协议数据单元（Protocol Data Unit））的类型 <br>
     * 字节大小：1 <br>
     * 字节序数：1
     */
    private EMessageType messageType = EMessageType.JOB;

    /**
     * 保留 <br>
     * 字节大小：2 <br>
     * 字节序数：2-3
     */
    private int reserved = 0x0000;

    /**
     * pdu的参考–由主站生成，每次新传输递增，Little-Endian <br>
     * 字节大小：2 <br>
     * 字节序数：4-5
     */
    private int pduReference = 0x0000;

    /**
     * 参数的长度（大端） <br>
     * 字节大小：2 <br>
     * 字节序数：6-7
     */
    private int parameterLength = 0x0000;

    /**
     * 数据的长度（大端） <br>
     * 字节大小：2 <br>
     * 字节序数：8-9
     */
    private int dataLength = 0x0000;

    /**
     * 错误类型 <br>
     * 字节大小：1 <br>
     * 字节序数：10
     */
    private EErrorClass errorClass = EErrorClass.NO_ERROR;

    /**
     * 错误码 <br>
     * 字节大小：1 <br>
     * 字节序数：11
     */
    private byte errorCode = 0x00;

    @Override
    public int getByteArrayLength() {
        return 12;
    }

    @Override
    public byte[] toByteArray() {
        byte[] res = new byte[12];
        byte[] reservedBytes = ShortUtil.toByteArray((short) this.reserved);
        byte[] pduReferenceBytes = ShortUtil.toByteArray((short) this.pduReference,true);
        byte[] parameterLengthBytes = ShortUtil.toByteArray((short) this.parameterLength);
        byte[] dataLengthBytes = ShortUtil.toByteArray((short) this.dataLength);

        res[0] = this.protocolId;
        res[1] = this.messageType.getCode();
        res[2] = reservedBytes[0];
        res[3] = reservedBytes[1];
        res[4] = pduReferenceBytes[0];
        res[5] = pduReferenceBytes[1];
        res[6] = parameterLengthBytes[0];
        res[7] = parameterLengthBytes[1];
        res[8] = dataLengthBytes[0];
        res[9] = dataLengthBytes[1];
        res[10] = errorClass.getCode();
        res[11] = errorCode;
        return res;
    }
}

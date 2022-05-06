package com.github.xingshuangs.iot.protocol.s7.model;


import com.github.xingshuangs.iot.protocol.s7.enums.EFunctionCode;
import com.github.xingshuangs.iot.utils.ShortUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 设置通信参数
 *
 * @author xingshuang
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SetupComParameter extends Parameter implements IByteArray {

    public static final int BYTE_LENGTH = 8;
    /**
     * 预留 <br>
     * 字节大小：1 <br>
     * 字节序数：1
     */
    private byte reserved = (byte) 0x00;

    /**
     * Ack队列的大小（主叫）（大端）<br>
     * 字节大小：2 <br>
     * 字节序数：2-3
     */
    private int maxAmqCaller = 0x0001;

    /**
     * Ack队列的大小（被叫）（大端）<br>
     * 字节大小：2 <br>
     * 字节序数：4-5
     */
    private int maxAmqCallee = 0x0001;

    /**
     * PDU长度（大端）<br>
     * 字节大小：2 <br>
     * 字节序数：6-7
     */
    private int pduLength = 0x0000;

    @Override
    public int byteArrayLength() {
        return BYTE_LENGTH;
    }

    @Override
    public byte[] toByteArray() {
        byte[] res = new byte[BYTE_LENGTH];
        byte[] maxAmqCallerBytes = ShortUtil.toByteArray(this.maxAmqCaller);
        byte[] maxAmqCalleeBytes = ShortUtil.toByteArray(this.maxAmqCallee);
        byte[] pduLengthBytes = ShortUtil.toByteArray(this.pduLength);

        res[0] = this.functionCode.getCode();
        res[1] = this.reserved;
        res[2] = maxAmqCallerBytes[0];
        res[3] = maxAmqCallerBytes[1];
        res[4] = maxAmqCalleeBytes[0];
        res[5] = maxAmqCalleeBytes[1];
        res[6] = pduLengthBytes[0];
        res[7] = pduLengthBytes[1];
        return res;
    }

    public static SetupComParameter fromBytes(final byte[] data) {
        SetupComParameter setupComParameter = new SetupComParameter();
        setupComParameter.functionCode = EFunctionCode.from(data[0]);
        setupComParameter.reserved = data[1];
        setupComParameter.maxAmqCaller = ShortUtil.toUInt16(data, 2);
        setupComParameter.maxAmqCallee = ShortUtil.toUInt16(data, 4);
        setupComParameter.pduLength = ShortUtil.toUInt16(data, 6);
        return setupComParameter;
    }

    public static SetupComParameter createDefault() {
        SetupComParameter parameter = new SetupComParameter();
        parameter.functionCode =  EFunctionCode.SETUP_COMMUNICATION;
        parameter.reserved = (byte)0x00;
        parameter.maxAmqCaller = 1;
        parameter.maxAmqCallee = 1;
        parameter.pduLength = 240;
        return parameter;
    }
}

package com.github.xingshuangs.iot.protocol.s7.model;


import com.github.xingshuangs.iot.exceptions.S7CommException;
import com.github.xingshuangs.iot.protocol.s7.enums.EPduType;
import com.github.xingshuangs.iot.utils.ByteUtil;
import com.github.xingshuangs.iot.utils.ShortUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * COTP连接部分
 *
 * @author xingshuang
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class COTPConnection extends COTP implements IByteArray {

    public static final int BYTE_LENGTH = 18;

    /**
     * 目标引用，用来唯一标识目标 <br>
     * 字节大小：2 <br>
     * 字节序数：2-3
     */
    private int destinationReference = 0x0000;

    /**
     * 源引用 <br>
     * 字节大小：2 <br>
     * 字节序数：4-5
     */
    private int sourceReference = 0x0001;

    /**
     * 扩展格式/流控制  前四位标识Class，  倒数第二位Extended formats，  倒数第一位No explicit flow control <br>
     * 字节大小：1 <br>
     * 字节序数：6
     */
    private byte flags = (byte) 0x00;

    /**
     * 参数代码TPDU-Size <br>
     * 字节大小：1 <br>
     * 字节序数：7
     */
    private byte parameterCodeTpduSize = (byte) 0xC0;

    /**
     * 参数长度 <br>
     * 字节大小：1 <br>
     * 字节序数：8
     */
    private int parameterLength1 = (byte) 0x01;

    /**
     * TPDU大小 TPDU Size (2^10 = 1024) <br>
     * 字节大小：1 <br>
     * 字节序数：9
     */
    private int tpduSize = (byte) 0x0A;

    /**
     * 参数代码SRC-TASP <br>
     * 字节大小：1 <br>
     * 字节序数：10
     */
    private byte parameterCodeSrcTsap = (byte) 0xC1;

    /**
     * 参数长度 <br>
     * 字节大小：1 <br>
     * 字节序数：11
     */
    private int parameterLength2 = (byte) 0x02;

    /**
     * SourceTSAP/Rack <br>
     * 字节大小：2 <br>
     * 字节序数：12-13
     */
    private int sourceTsap = 0x0201;

    /**
     * 参数代码DST-TASP <br>
     * 字节大小：1 <br>
     * 字节序数：14
     */
    private byte parameterCodeDstTsap = (byte) 0xC2;

    /**
     * 参数长度 <br>
     * 字节大小：1 <br>
     * 字节序数：15
     */
    private int parameterLength3 = (byte) 0x02;

    /**
     * DestinationTSAP/slot <br>
     * 字节大小：2 <br>
     * 字节序数：16-17
     */
    private int destinationTsap = 0x0201;

    @Override
    public int byteArrayLength() {
        return BYTE_LENGTH;
    }

    @Override
    public byte[] toByteArray() {
        byte[] res = new byte[BYTE_LENGTH];
        byte[] destRefBytes = ShortUtil.toByteArray(this.destinationReference);
        byte[] srcRefBytes = ShortUtil.toByteArray(this.sourceReference);
        byte[] srcTsapBytes = ShortUtil.toByteArray(this.sourceTsap);
        byte[] destTsapBytes = ShortUtil.toByteArray(this.destinationTsap);

        res[0] = ByteUtil.toByte(this.length);
        res[1] = this.pduType.getCode();

        res[2] = destRefBytes[0];
        res[3] = destRefBytes[1];

        res[4] = srcRefBytes[0];
        res[5] = srcRefBytes[1];

        res[6] = this.flags;
        res[7] = this.parameterCodeTpduSize;
        res[8] = ByteUtil.toByte(this.parameterLength1);
        res[9] = ByteUtil.toByte(this.tpduSize);
        res[10] = this.parameterCodeSrcTsap;
        res[11] = ByteUtil.toByte(this.parameterLength2);

        res[12] = srcTsapBytes[0];
        res[13] = srcTsapBytes[1];

        res[14] = this.parameterCodeDstTsap;
        res[15] = ByteUtil.toByte(this.parameterLength3);

        res[16] = destTsapBytes[0];
        res[17] = destTsapBytes[1];
        return res;
    }

    /**
     * CRConnect Request 连接请求
     *
     * @return COTPConnection对象
     */
    public static COTPConnection crConnectRequest() {
        COTPConnection connection = new COTPConnection();
        connection.length = (byte) 0x11;
        // FIXME:这里到底是0xE0还是0x0E
        connection.pduType = EPduType.CONNECT_REQUEST;
        connection.destinationReference = 0x0000;
        connection.sourceReference = 0x0001;
        connection.flags = (byte) 0x00;
        connection.parameterCodeTpduSize = (byte) 0xC0;
        connection.parameterLength1 = (byte) 0x01;
        connection.tpduSize = (byte) 0x0A;
        connection.parameterCodeSrcTsap = (byte) 0xC1;
        connection.parameterLength2 = (byte) 0x02;
        // FIXME:这里到底是0x0201还是0x0102
        connection.sourceTsap = 0x0201;
        connection.parameterCodeDstTsap = (byte) 0xC2;
        connection.parameterLength3 = (byte) 0x02;
        // FIXME:这里到底是0x0201还是0x0100
        connection.destinationTsap = 0x0201;
        return connection;
    }

    public static COTPConnection fromBytes(final byte[] data) {
        if (data.length < BYTE_LENGTH) {
            throw new S7CommException("COTPConnection数据字节长度不够，无法解析");
        }
        COTPConnection connection = new COTPConnection();
        connection.length = ByteUtil.toUInt8(data[0]);
        connection.pduType = EPduType.from(data[1]);
        connection.destinationReference = ShortUtil.toUInt16(data, 2);
        connection.sourceReference = ShortUtil.toUInt16(data, 4);
        connection.flags = data[6];
        connection.parameterCodeTpduSize = data[7];
        connection.parameterLength1 = ByteUtil.toUInt8(data[8]);
        connection.tpduSize = ByteUtil.toUInt8(data[9]);
        connection.parameterCodeSrcTsap = data[10];
        connection.parameterLength2 = ByteUtil.toUInt8(data[11]);
        connection.sourceTsap = ShortUtil.toUInt16(data, 12);
        connection.parameterCodeDstTsap = data[14];
        connection.parameterLength3 = ByteUtil.toUInt8(data[15]);
        connection.destinationTsap = ShortUtil.toUInt16(data, 16);
        return connection;
    }
}

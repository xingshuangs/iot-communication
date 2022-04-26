package com.github.xingshuangs.iot.protocol.s7.model;


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

    /**
     * 目标引用，用来唯一标识目标 <br>
     * 字节大小：2 <br>
     * 字节序数：2-3
     */
    private int destinationReference = 0x0001;

    /**
     * 源引用 <br>
     * 字节大小：2 <br>
     * 字节序数：4-5
     */
    private int sourceReference = 0x0006;

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
    private byte parameterLength1 = (byte) 0x01;

    /**
     * TPDU大小 <br>
     * 字节大小：1 <br>
     * 字节序数：9
     */
    private byte tpduSize = (byte) 0x0A;

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
    private byte parameterLength2 = (byte) 0x02;

    /**
     * SourceTSAP <br>
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
    private byte parameterLength3 = (byte) 0x02;

    /**
     * DestinationTSAP <br>
     * 字节大小：2 <br>
     * 字节序数：16-17
     */
    private int destinationTsap = 0x0201;

    @Override
    public int getByteArrayLength() {
        return 18;
    }

    @Override
    public byte[] toByteArray() {
        byte[] res = new byte[18];
        byte[] destRefBytes = ShortUtil.toByteArray((short) this.destinationReference);
        byte[] srcRefBytes = ShortUtil.toByteArray((short) this.sourceReference);
        byte[] srcTsapBytes = ShortUtil.toByteArray((short) this.sourceTsap);
        byte[] destTsapBytes = ShortUtil.toByteArray((short) this.destinationTsap);

        res[0] = this.getLength();
        res[1] = this.getPduType().getCode();

        res[2] = destRefBytes[0];
        res[3] = destRefBytes[1];

        res[4] = srcRefBytes[0];
        res[5] = srcRefBytes[1];

        res[6] = this.flags;
        res[7] = this.parameterCodeTpduSize;
        res[8] = this.parameterLength1;
        res[9] = this.tpduSize;
        res[10] = this.parameterCodeSrcTsap;
        res[11] = this.parameterLength2;

        res[12] = srcTsapBytes[0];
        res[13] = srcTsapBytes[1];

        res[14] = this.parameterCodeDstTsap;
        res[15] = this.parameterLength3;

        res[16] = destTsapBytes[0];
        res[17] = destTsapBytes[1];
        return res;
    }
}

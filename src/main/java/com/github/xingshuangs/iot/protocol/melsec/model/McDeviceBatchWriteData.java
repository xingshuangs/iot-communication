package com.github.xingshuangs.iot.protocol.melsec.model;


import com.github.xingshuangs.iot.protocol.common.buff.ByteWriteBuff;
import com.github.xingshuangs.iot.protocol.melsec.enums.EMcDeviceCode;
import com.github.xingshuangs.iot.protocol.melsec.enums.EMcSeries;
import com.github.xingshuangs.iot.utils.IntegerUtil;
import lombok.Data;

/**
 * 软元件访问批量写请求数据
 *
 * @author xingshuang
 */
@Data
public class McDeviceBatchWriteData extends McReqData {

    /**
     * 起始软元件编号，Q/L系列是3个字节，iQ-R系列是4个字节
     */
    private int headDeviceNumber;

    /**
     * 软元件代码，Q/L系列是1个字节，iQ-R系列是2个字节
     */
    private EMcDeviceCode deviceCode;

    /**
     * 软元件点数，2个字节
     */
    private int devicePointsCount;

    /**
     * 数据
     */
    private byte[] data = new byte[0];

    @Override
    public int byteArrayLength() {
        return this.data.length + (this.series == EMcSeries.Q_L ? 10 : 12);
    }

    @Override
    public byte[] toByteArray() {
        ByteWriteBuff buff = ByteWriteBuff.newInstance(this.byteArrayLength(), true)
                .putShort(this.command.getCode())
                .putShort(this.subcommand);
        if (this.series == EMcSeries.Q_L) {
            buff.putBytes(IntegerUtil.toCustomByteArray(this.headDeviceNumber, 1, 3));
            buff.putByte(this.deviceCode.getBinaryCode());
        } else {
            buff.putInteger(this.headDeviceNumber);
            buff.putShort(this.deviceCode.getBinaryCodeIqr());
        }
        buff.putShort(this.devicePointsCount);
        buff.putBytes(this.data);
        return buff.getData();
    }
}

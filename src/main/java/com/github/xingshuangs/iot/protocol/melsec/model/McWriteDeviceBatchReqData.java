package com.github.xingshuangs.iot.protocol.melsec.model;


import com.github.xingshuangs.iot.protocol.common.buff.ByteWriteBuff;
import com.github.xingshuangs.iot.protocol.melsec.enums.EMcCommand;
import lombok.Data;

/**
 * 软元件访问批量写请求数据
 *
 * @author xingshuang
 */
@Data
public class McWriteDeviceBatchReqData extends McReqData {

    /**
     * 软元件设备地址+内容
     */
    private McDeviceContent deviceContent;

    public McWriteDeviceBatchReqData() {
    }

    public McWriteDeviceBatchReqData(EMcCommand command, int subcommand, McDeviceContent deviceContent) {
        this.command = command;
        this.subcommand = subcommand;
        this.deviceContent = deviceContent;
    }

    @Override
    public int byteArrayLength() {
        return 4 + this.deviceContent.byteArrayLengthWithPointsCount();
    }

    @Override
    public byte[] toByteArray() {
        return ByteWriteBuff.newInstance(this.byteArrayLength(), true)
                .putShort(this.command.getCode())
                .putShort(this.subcommand)
                .putBytes(this.deviceContent.toByteArrayWithPointsCount())
                .getData();
    }
}

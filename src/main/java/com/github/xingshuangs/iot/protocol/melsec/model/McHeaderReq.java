package com.github.xingshuangs.iot.protocol.melsec.model;


import com.github.xingshuangs.iot.protocol.common.buff.ByteWriteBuff;
import lombok.Data;

/**
 * 请求头
 *
 * @author xingshuang
 */
@Data
public class McHeaderReq extends McHeader {

    /**
     * 监视定时器，2字节，设置读取及写入的处理完成之前的等待时间。设置连接站E71向访问目标发出处理请求之后到返回响应为止的等待时间。
     * 0000H(0): 无限等待(处理完成之前继续等待。)<br>
     * 0001H～FFFFH(1～65535): 等待时间(单位: 250ms)<br>
     * 连接站(本站):0001H～0028H(0.25秒～10秒)
     * 其它站:0002H～00F0H(0.5秒～60秒)
     */
    private int monitoringTimer;

    public McHeaderReq() {
    }

    public McHeaderReq(int subHeader, McAccessRoute accessRoute, int timer) {
        this.subHeader = subHeader;
        this.accessRoute = accessRoute;
        this.monitoringTimer = timer / 250;
    }

    @Override
    public int byteArrayLength() {
        return 2 + this.accessRoute.byteArrayLength() + 2 + 2;
    }

    @Override
    public byte[] toByteArray() {
        return ByteWriteBuff.newInstance(this.byteArrayLength(), true)
                .putShort(this.subHeader)
                .putBytes(this.accessRoute.toByteArray())
                .putShort(this.dataLength)
                .putShort(this.monitoringTimer)
                .getData();
    }
}

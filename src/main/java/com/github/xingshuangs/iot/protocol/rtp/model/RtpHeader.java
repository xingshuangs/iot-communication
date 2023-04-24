package com.github.xingshuangs.iot.protocol.rtp.model;


import com.github.xingshuangs.iot.protocol.common.IObjectByteArray;
import com.github.xingshuangs.iot.protocol.common.buff.ByteWriteBuff;
import com.github.xingshuangs.iot.utils.BooleanUtil;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * RTP数据包
 *
 * @author xingshuang
 */
@Data
public class RtpHeader implements IObjectByteArray {

    /**
     * 版本（V）：2 bits RTP版本号，现在用的是2
     */
    private int version;

    /**
     * 填充（P）：1比特，1 bit 如果设置了该字段，报文的末尾会包含一个或多个填充字节，这些填充字节不是payload的内容。
     * 最后一个填充字节标识了总共需要忽略多少个填充字节（包括自己）
     */
    private boolean padding;

    /**
     * 1 bit 如果设置了该字段，那么头数据后跟着一个拓展数据
     */
    private boolean extension;

    /**
     * 4 bits CSRC列表的长度。
     */
    private int csrcCount;

    /**
     * 1 bit Marker会在预设中进行定义（预设和RTP的关系可以参考rfc3551，我的理解是预设是对RTP的补充，以达到某一类实际使用场景的需要），
     * 在报文流中用它来划分每一帧的边界。预设中可能会定义附加的marker，或者移除Marker来拓展payload type字段的长度。
     */
    private boolean marker;

    /**
     * 7 bits 该字段定义RTP payload的格式和他在预设中的意义。上层应用可能会定义一个（静态的类型码<->payload格式）映射关系。
     * 也可以用RTP协议外的方式来动态地定义payload类型。在一个RTP session中payload类型可能会改变，但是不应该用payload类型来区分不同的媒体流，
     * 正如之前所说，不同的媒体流应该通过不同session分别传输。
     */
    private int payloadType;

    /**
     * 16 bits 每发送一个RTP包该序列号+1，RTP包的接受者可以通过它来确定丢包情况并且利用它来重排包的顺序。
     * 这个字段的初始值应该是随机的，这会让known-plaintext更加困难。
     */
    private int sequenceNumber;

    /**
     * 32 bits 时间戳反映了RTP数据包生成第一块数据时的时刻
     */
    private long timestamp;

    /**
     * 32 bits 该字段用来确定数据的发送源。这个身份标识应该随机生成，并且要保证同一个RTP session中没有重复的SSRC。
     * 虽然SSRC冲突的概率很小，但是每个RTP客户端都应该时刻警惕，如果发现冲突就要去解决。
     */
    private long ssrc;

    /**
     * 0 ~ 15 items， 32 bits each CSRC list表示对该payload数据做出贡献的所有SSRC。
     * 这个字段包含的SSRC数量由CC字段定义。如果有超过15个SSRC，只有15个可以被记录。
     */
    private List<Long> csrcList = new ArrayList<>();

    @Override
    public int byteArrayLength() {
        return 12 + this.csrcCount * 4;
    }

    @Override
    public byte[] toByteArray() {
        byte first = (byte) (((this.version << 6) & 0xC0)
                | BooleanUtil.setBit(6, this.padding)
                | BooleanUtil.setBit(5, this.extension)
                | (this.csrcCount & 0x0F));
        byte second = (byte) (BooleanUtil.setBit(7, this.marker) | (this.payloadType & 0x7F));

        ByteWriteBuff buff = ByteWriteBuff.newInstance(this.byteArrayLength())
                .putByte(first)
                .putByte(second)
                .putShort(this.sequenceNumber)
                .putInteger(this.timestamp)
                .putInteger(this.ssrc);
        this.csrcList.forEach(buff::putInteger);
        return buff.getData();
    }
}

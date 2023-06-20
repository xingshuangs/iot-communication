package com.github.xingshuangs.iot.protocol.rtp.model;


import com.github.xingshuangs.iot.protocol.common.IObjectByteArray;
import com.github.xingshuangs.iot.protocol.common.buff.ByteReadBuff;
import com.github.xingshuangs.iot.protocol.common.buff.ByteWriteBuff;
import com.github.xingshuangs.iot.utils.BooleanUtil;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * RTP数据包头
 * The RTP header has the following format:
 * <p>
 * 0                   1                   2                   3
 * 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 * |V=2|P|X|  CC   |M|     PT      |       sequence number         |
 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 * |                           timestamp                           |
 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 * |           synchronization source (SSRC) identifier            |
 * +=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+
 * |            contributing source (CSRC) identifiers             |
 * |                             ....                              |
 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 * <p>
 * RTP Header Extension
 * 0                   1                   2                   3
 * 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 * |      defined by profile       |           length              |
 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 * |                        header extension                       |
 * |                             ....                              |
 *
 * @author xingshuang
 */
@Data
public class RtpHeader implements IObjectByteArray {

    /**
     * 版本（V）：2 bits RTP版本号，现在用的是
     * This field identifies the version of RTP.  The version defined by
     * this specification is two (2).  (The value 1 is used by the first
     * draft version of RTP and the value 0 is used by the protocol
     * initially implemented in the "vat" audio tool.)
     */
    private int version;

    /**
     * 填充（P）：1比特，1 bit 如果设置了该字段，报文的末尾会包含一个或多个填充字节，这些填充字节不是payload的内容。
     * 最后一个填充字节标识了总共需要忽略多少个填充字节（包括自己）
     * If the padding bit is set, the packet contains one or more
     * additional padding octets at the end which are not part of the
     * payload.  The last octet of the padding contains a count of how
     * many padding octets should be ignored, including itself.  Padding
     * may be needed by some encryption algorithms with fixed block sizes
     * or for carrying several RTP packets in a lower-layer protocol data
     * unit.
     */
    private boolean padding;

    /**
     * 1 bit 如果设置了该字段，那么头数据后跟着一个拓展数据
     * If the extension bit is set, the fixed header MUST be followed by
     * exactly one header extension
     */
    private boolean extension;

    /**
     * CSRC Count，共享媒体源个数，一般用于混音和混屏中，例如某个音频流是混合了其它音频后的数据，那么其它音频源就是该音频源的 CSRC
     * 4 bits CSRC列表的长度。
     * The CSRC count contains the number of CSRC identifiers that follow
     * the fixed header.
     */
    private int csrcCount;

    /**
     * Mark 标记位，对于不同的负载类型有不同含义，例如使用 RTP 荷载 H264 码流时，如果某个帧分成多个包进行传输，可以使用该位标记是否为帧的最后一个包
     * 1 bit Marker会在预设中进行定义（预设和RTP的关系可以参考rfc3551，我的理解是预设是对RTP的补充，以达到某一类实际使用场景的需要），
     * 在报文流中用它来划分每一帧的边界。预设中可能会定义附加的marker，或者移除Marker来拓展payload type字段的长度。
     * The interpretation of the marker is defined by a profile.  It is
     * intended to allow significant events such as frame boundaries to
     * be marked in the packet stream.  A profile MAY define additional
     * marker bits or specify that there is no marker bit by changing the
     * number of bits in the payload type field
     */
    private boolean marker;

    /**
     * 7 bits 该字段定义RTP payload的格式和他在预设中的意义。上层应用可能会定义一个（静态的类型码<->payload格式）映射关系。
     * 也可以用RTP协议外的方式来动态地定义payload类型。在一个RTP session中payload类型可能会改变，但是不应该用payload类型来区分不同的媒体流，
     * 正如之前所说，不同的媒体流应该通过不同session分别传输。
     * This field identifies the format of the RTP payload and determines
     * its interpretation by the application.
     */
    private int payloadType;

    /**
     * 16 bits 每发送一个RTP包该序列号+1，RTP包的接受者可以通过它来确定丢包情况并且利用它来重排包的顺序。
     * 这个字段的初始值应该是随机的，这会让known-plaintext更加困难。
     * The sequence number increments by one for each RTP data packet
     * sent, and may be used by the receiver to detect packet loss and to
     * restore packet sequence.
     */
    private int sequenceNumber;

    /**
     * 32 bits 时间戳反映了RTP数据包生成第一块数据时的时刻
     * The timestamp reflects the sampling instant of the first octet in
     * the RTP data packet.
     */
    private long timestamp;

    /**
     * 32 bits 该字段用来确定数据的发送源。这个身份标识应该随机生成，并且要保证同一个RTP session中没有重复的SSRC。
     * 虽然SSRC冲突的概率很小，但是每个RTP客户端都应该时刻警惕，如果发现冲突就要去解决。
     * The SSRC field identifies the synchronization source.
     */
    private long ssrc;

    /**
     * 0 ~ 15 items， 32 bits each CSRC list表示对该payload数据做出贡献的所有SSRC。
     * 这个字段包含的SSRC数量由CC字段定义。如果有超过15个SSRC，只有15个可以被记录。
     * The CSRC list identifies the contributing sources for the payload
     * contained in this packet.  The number of identifiers is given by
     * the CC field.  If there are more than 15 contributing sources,
     * only 15 can be identified.
     */
    private List<Long> csrcList = new ArrayList<>();

    /**
     * 扩展头Id，2个字节
     */
    private int extensionHeaderId;

    /**
     * 扩展头长度，2个字节
     */
    private int extensionHeaderLength;

    /**
     * 扩展头的内容
     */
    private byte[] extensionHeaderContent = new byte[0];

    @Override
    public int byteArrayLength() {
        int sum = 12 + this.csrcCount * 4;
        if (this.extension) {
            sum += 4 + this.extensionHeaderLength;
        }
        return sum;
    }

    @Override
    public byte[] toByteArray() {
        byte first = (byte) (((this.version << 6) & 0xC0)
                | BooleanUtil.setBit(5, this.padding)
                | BooleanUtil.setBit(4, this.extension)
                | (this.csrcCount & 0x0F));
        byte second = (byte) (BooleanUtil.setBit(7, this.marker) | (this.payloadType & 0x7F));

        ByteWriteBuff buff = ByteWriteBuff.newInstance(this.byteArrayLength())
                .putByte(first)
                .putByte(second)
                .putShort(this.sequenceNumber)
                .putInteger(this.timestamp)
                .putInteger(this.ssrc);
        this.csrcList.forEach(buff::putInteger);
        if (this.extension) {
            buff.putShort(this.extensionHeaderId);
            buff.putShort(this.extensionHeaderLength);
            buff.putBytes(this.extensionHeaderContent);
        }
        return buff.getData();
    }

    /**
     * 字节数组数据解析
     *
     * @param data 字节数组数据
     * @return RtcpHeader
     */
    public static RtpHeader fromBytes(final byte[] data) {
        return fromBytes(data, 0);
    }

    /**
     * 字节数组数据解析
     *
     * @param data   字节数组数据
     * @param offset 偏移量
     * @return RtcpHeader
     */
    public static RtpHeader fromBytes(final byte[] data, final int offset) {
        if (data.length < 12) {
            throw new IndexOutOfBoundsException("解析header时，字节数组长度不够");
        }
        ByteReadBuff buff = new ByteReadBuff(data, offset);
        RtpHeader res = new RtpHeader();
        byte aByte = buff.getByte();
        res.version = (aByte >> 6) & 0x03;
        res.padding = BooleanUtil.getValue(aByte, 5);
        res.extension = BooleanUtil.getValue(aByte, 4);
        res.csrcCount = aByte & 0x0F;
        byte bByte = buff.getByte();
        res.marker = BooleanUtil.getValue(bByte, 7);
        res.payloadType = bByte & 0x7F;
        res.sequenceNumber = buff.getUInt16();
        res.timestamp = buff.getUInt32();
        res.ssrc = buff.getUInt32();

        for (int i = 0; i < res.csrcCount; i++) {
            res.csrcList.add(buff.getUInt32());
        }
        // 扩展头
        if (res.extension) {
            res.extensionHeaderId = buff.getUInt16();
            res.extensionHeaderLength = buff.getUInt16() * 4;
            res.extensionHeaderContent = buff.getBytes(res.extensionHeaderLength);
        }
        return res;
    }
}

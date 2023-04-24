package com.github.xingshuangs.iot.protocol.rtcp.model;


import com.github.xingshuangs.iot.exceptions.RtcpCommException;
import com.github.xingshuangs.iot.protocol.rtcp.enums.ERtcpPackageType;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据包构建器
 *
 * @author xingshuang
 */
public class RtcpPackageBuilder {

    private RtcpPackageBuilder() {
        // NOOP
    }

    public static List<RtcpBasePackage> fromBytes(final byte[] data) {
        return fromBytes(data, 0);
    }

    public static List<RtcpBasePackage> fromBytes(final byte[] data, final int offset) {
        List<RtcpBasePackage> list = new ArrayList<>();
        int off = offset;
        while (data.length > off) {
            RtcpBasePackage basePackage = parsePackage(data, off);
            list.add(basePackage);
            off += basePackage.byteArrayLength();
        }
        return list;
    }

    public static RtcpBasePackage parsePackage(final byte[] data, final int offset) {
        ERtcpPackageType type = ERtcpPackageType.from(data[1 + offset]);
        switch (type) {
            case RR:
                return RtcpReceiverReport.fromBytes(data, offset);
            case SR:
                return RtcpSenderReport.fromBytes(data, offset);
            case SDES:
                return RtcpSdesReport.fromBytes(data, offset);
            case BYE:
                return RtcpBye.fromBytes(data, offset);
            case APP:
                return RtcpApp.fromBytes(data, offset);
            default:
                throw new RtcpCommException("无法识别类型");
        }
    }
}

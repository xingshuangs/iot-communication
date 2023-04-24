package com.github.xingshuangs.iot.protocol.rtcp.model;

import com.github.xingshuangs.iot.protocol.rtcp.enums.ERtcpPackageType;
import com.github.xingshuangs.iot.protocol.rtcp.enums.ERtcpSdesItemType;
import com.github.xingshuangs.iot.utils.TimesUtil;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;


public class RtcpPackageBuilderTest {

    @Test
    public void receiveFromBytes() {
        byte[] expect = new byte[]{(byte) 0x81, (byte) 0xC9, (byte) 0x00, (byte) 0x07, (byte) 0xC3, (byte) 0x45, (byte) 0x17, (byte) 0xFC
                , (byte) 0x4F, (byte) 0x92, (byte) 0xEF, (byte) 0x4B, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF
                , (byte) 0x00, (byte) 0x01, (byte) 0x8F, (byte) 0xDF, (byte) 0x00, (byte) 0x00, (byte) 0x01, (byte) 0xD7
                , (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00

                , (byte) 0x81, (byte) 0xCA, (byte) 0x00, (byte) 0x07, (byte) 0x55, (byte) 0xA9, (byte) 0x65, (byte) 0x0B
                , (byte) 0x01, (byte) 0x09, (byte) 0x31, (byte) 0x32, (byte) 0x37, (byte) 0x2E, (byte) 0x30, (byte) 0x2E
                , (byte) 0x30, (byte) 0x2E, (byte) 0x31, (byte) 0x06, (byte) 0x09, (byte) 0x76, (byte) 0x6C, (byte) 0x63
                , (byte) 0x20, (byte) 0x33, (byte) 0x2E, (byte) 0x30, (byte) 0x2E, (byte) 0x35, (byte) 0x00, (byte) 0x00};
        List<RtcpBasePackage> basePackages = RtcpPackageBuilder.fromBytes(expect);
        RtcpReceiverReport receiverReport = (RtcpReceiverReport) basePackages.get(0);
        assertEquals(2, receiverReport.getHeader().getVersion());
        assertFalse(receiverReport.getHeader().isPadding());
        assertEquals(1, receiverReport.getHeader().getReceptionCount());
        assertEquals(ERtcpPackageType.RR, receiverReport.getHeader().getPackageType());
        assertEquals(7, receiverReport.getHeader().getLength());
        assertEquals(3276085244L, receiverReport.getSourceId());
        assertEquals(1, receiverReport.getReportBlocks().size());
        receiverReport.getReportBlocks().forEach(x -> {
            assertEquals(1335029579L, x.getSourceId());
            assertEquals(255, x.getFractionLost());
            assertEquals(16777215, x.getCumulativePacketLost());
            assertEquals(102367, x.getExtHighestSequenceNumberReceived());
            assertEquals(471, x.getJitter());
            assertEquals(0, x.getLastNtpTimeSenderReportReceived());
            assertEquals(0, x.getDelaySinceLastTimeSenderReportReceived());
        });
        byte[] receiveExpect = new byte[]{(byte) 0x81, (byte) 0xC9, (byte) 0x00, (byte) 0x07, (byte) 0xC3, (byte) 0x45, (byte) 0x17, (byte) 0xFC
                , (byte) 0x4F, (byte) 0x92, (byte) 0xEF, (byte) 0x4B, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF
                , (byte) 0x00, (byte) 0x01, (byte) 0x8F, (byte) 0xDF, (byte) 0x00, (byte) 0x00, (byte) 0x01, (byte) 0xD7
                , (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00};
        byte[] actual = receiverReport.toByteArray();
        assertArrayEquals(receiveExpect, actual);

        RtcpSdesReport sdesReport = (RtcpSdesReport) basePackages.get(1);
        assertEquals(2, sdesReport.getHeader().getVersion());
        assertFalse(sdesReport.getHeader().isPadding());
        assertEquals(1, sdesReport.getHeader().getReceptionCount());
        assertEquals(ERtcpPackageType.SDES, sdesReport.getHeader().getPackageType());
        assertEquals(7, sdesReport.getHeader().getLength());
        assertEquals(1, sdesReport.getSdesChunks().size());

        sdesReport.getSdesChunks().forEach(x -> {
            assertEquals(1437164811L, x.getSourceId());
            assertEquals(2, x.getSdesItems().size());

            RtcpSdesItem item0 = x.getSdesItems().get(0);
            assertEquals(ERtcpSdesItemType.CNAME, item0.getType());
            assertEquals(9, item0.getLength());
            assertEquals("127.0.0.1", item0.getText());

            RtcpSdesItem item1 = x.getSdesItems().get(1);
            assertEquals(ERtcpSdesItemType.TOOL, item1.getType());
            assertEquals(9, item1.getLength());
            assertEquals("vlc 3.0.5", item1.getText());
        });
        byte[] sdesExpect = new byte[]{(byte) 0x81, (byte) 0xCA, (byte) 0x00, (byte) 0x07, (byte) 0x55, (byte) 0xA9, (byte) 0x65, (byte) 0x0B
                , (byte) 0x01, (byte) 0x09, (byte) 0x31, (byte) 0x32, (byte) 0x37, (byte) 0x2E, (byte) 0x30, (byte) 0x2E
                , (byte) 0x30, (byte) 0x2E, (byte) 0x31, (byte) 0x06, (byte) 0x09, (byte) 0x76, (byte) 0x6C, (byte) 0x63
                , (byte) 0x20, (byte) 0x33, (byte) 0x2E, (byte) 0x30, (byte) 0x2E, (byte) 0x35, (byte) 0x00, (byte) 0x00};
        actual = sdesReport.toByteArray();
        assertArrayEquals(sdesExpect, actual);
    }

    @Test
    public void senderFromBytes() {
        byte[] expect = new byte[]{(byte) 0x80, (byte) 0xC8, (byte) 0x00, (byte) 0x06, (byte) 0x55, (byte) 0xA9, (byte) 0x65, (byte) 0x0B
                , (byte) 0xE7, (byte) 0xED, (byte) 0xFE, (byte) 0xA4, (byte) 0x04, (byte) 0x4E, (byte) 0x3F, (byte) 0xFE
                , (byte) 0xF2, (byte) 0x2B, (byte) 0x4E, (byte) 0xED, (byte) 0x00, (byte) 0x00, (byte) 0x08, (byte) 0xE2
                , (byte) 0x00, (byte) 0x2F, (byte) 0x78, (byte) 0x82
                , (byte) 0x81, (byte) 0xCA, (byte) 0x00, (byte) 0x07, (byte) 0x55, (byte) 0xA9, (byte) 0x65, (byte) 0x0B
                , (byte) 0x01, (byte) 0x09, (byte) 0x31, (byte) 0x32, (byte) 0x37, (byte) 0x2E, (byte) 0x30, (byte) 0x2E
                , (byte) 0x30, (byte) 0x2E, (byte) 0x31, (byte) 0x06, (byte) 0x09, (byte) 0x76, (byte) 0x6C, (byte) 0x63
                , (byte) 0x20, (byte) 0x33, (byte) 0x2E, (byte) 0x30, (byte) 0x2E, (byte) 0x35, (byte) 0x00, (byte) 0x00};

        List<RtcpBasePackage> basePackages = RtcpPackageBuilder.fromBytes(expect);
        RtcpSenderReport senderReport = (RtcpSenderReport) basePackages.get(0);
        assertEquals(2, senderReport.getHeader().getVersion());
        assertFalse(senderReport.getHeader().isPadding());
        assertEquals(0, senderReport.getHeader().getReceptionCount());
        assertEquals(ERtcpPackageType.SR, senderReport.getHeader().getPackageType());
        assertEquals(6, senderReport.getHeader().getLength());
        assertEquals(1437164811L, senderReport.getSourceId());

        assertEquals(3891134116L, senderReport.getSenderInfo().getMswTimestamp());
        assertEquals(72237054L, senderReport.getSenderInfo().getLswTimestamp());
        assertEquals(4062924525L, TimesUtil.getNTPTotalSecond(senderReport.getSenderInfo().getRtpTimestamp()));
        assertEquals(2274L, senderReport.getSenderInfo().getSenderPacketCount());
        assertEquals(3111042L, senderReport.getSenderInfo().getSenderOctetCount());

        assertEquals(0, senderReport.getReportBlocks().size());
        byte[] senderExpect = new byte[]{(byte) 0x80, (byte) 0xC8, (byte) 0x00, (byte) 0x06, (byte) 0x55, (byte) 0xA9, (byte) 0x65, (byte) 0x0B
                , (byte) 0xE7, (byte) 0xED, (byte) 0xFE, (byte) 0xA4, (byte) 0x04, (byte) 0x4E, (byte) 0x3F, (byte) 0xFE
                , (byte) 0xF2, (byte) 0x2B, (byte) 0x4E, (byte) 0xED, (byte) 0x00, (byte) 0x00, (byte) 0x08, (byte) 0xE2
                , (byte) 0x00, (byte) 0x2F, (byte) 0x78, (byte) 0x82};
        byte[] actual = senderReport.toByteArray();
        assertArrayEquals(senderExpect, actual);

        RtcpSdesReport sdesReport = (RtcpSdesReport) basePackages.get(1);
        assertEquals(2, sdesReport.getHeader().getVersion());
        assertFalse(sdesReport.getHeader().isPadding());
        assertEquals(1, sdesReport.getHeader().getReceptionCount());
        assertEquals(ERtcpPackageType.SDES, sdesReport.getHeader().getPackageType());
        assertEquals(7, sdesReport.getHeader().getLength());
        assertEquals(1, sdesReport.getSdesChunks().size());

        sdesReport.getSdesChunks().forEach(x -> {
            assertEquals(1437164811L, x.getSourceId());
            assertEquals(2, x.getSdesItems().size());

            RtcpSdesItem item0 = x.getSdesItems().get(0);
            assertEquals(ERtcpSdesItemType.CNAME, item0.getType());
            assertEquals(9, item0.getLength());
            assertEquals("127.0.0.1", item0.getText());

            RtcpSdesItem item1 = x.getSdesItems().get(1);
            assertEquals(ERtcpSdesItemType.TOOL, item1.getType());
            assertEquals(9, item1.getLength());
            assertEquals("vlc 3.0.5", item1.getText());
        });
        byte[] sdesExpect = new byte[]{(byte) 0x81, (byte) 0xCA, (byte) 0x00, (byte) 0x07, (byte) 0x55, (byte) 0xA9, (byte) 0x65, (byte) 0x0B
                , (byte) 0x01, (byte) 0x09, (byte) 0x31, (byte) 0x32, (byte) 0x37, (byte) 0x2E, (byte) 0x30, (byte) 0x2E
                , (byte) 0x30, (byte) 0x2E, (byte) 0x31, (byte) 0x06, (byte) 0x09, (byte) 0x76, (byte) 0x6C, (byte) 0x63
                , (byte) 0x20, (byte) 0x33, (byte) 0x2E, (byte) 0x30, (byte) 0x2E, (byte) 0x35, (byte) 0x00, (byte) 0x00};
        actual = sdesReport.toByteArray();
        assertArrayEquals(sdesExpect, actual);
    }

    @Test
    public void receiveAndByeFromBytes() {
        byte[] expect = new byte[]{(byte) 0x81, (byte) 0xC9, (byte) 0x00, (byte) 0x07, (byte) 0xC3, (byte) 0x45, (byte) 0x17, (byte) 0xFC
                , (byte) 0x4F, (byte) 0x92, (byte) 0xEF, (byte) 0x4B, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF
                , (byte) 0x00, (byte) 0x01, (byte) 0x8F, (byte) 0xDF, (byte) 0x00, (byte) 0x00, (byte) 0x01, (byte) 0xD7
                , (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00

                , (byte) 0x81, (byte) 0xCB, (byte) 0x00, (byte) 0x01, (byte) 0xC3, (byte) 0x45, (byte) 0x17, (byte) 0xFC};
        List<RtcpBasePackage> basePackages = RtcpPackageBuilder.fromBytes(expect);
        RtcpReceiverReport receiverReport = (RtcpReceiverReport) basePackages.get(0);
        assertEquals(2, receiverReport.getHeader().getVersion());
        assertFalse(receiverReport.getHeader().isPadding());
        assertEquals(1, receiverReport.getHeader().getReceptionCount());
        assertEquals(ERtcpPackageType.RR, receiverReport.getHeader().getPackageType());
        assertEquals(7, receiverReport.getHeader().getLength());
        assertEquals(3276085244L, receiverReport.getSourceId());
        assertEquals(1, receiverReport.getReportBlocks().size());
        receiverReport.getReportBlocks().forEach(x -> {
            assertEquals(1335029579L, x.getSourceId());
            assertEquals(255, x.getFractionLost());
            assertEquals(16777215, x.getCumulativePacketLost());
            assertEquals(102367, x.getExtHighestSequenceNumberReceived());
            assertEquals(471, x.getJitter());
            assertEquals(0, x.getLastNtpTimeSenderReportReceived());
            assertEquals(0, x.getDelaySinceLastTimeSenderReportReceived());
        });
        byte[] receiveExpect = new byte[]{(byte) 0x81, (byte) 0xC9, (byte) 0x00, (byte) 0x07, (byte) 0xC3, (byte) 0x45, (byte) 0x17, (byte) 0xFC
                , (byte) 0x4F, (byte) 0x92, (byte) 0xEF, (byte) 0x4B, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF
                , (byte) 0x00, (byte) 0x01, (byte) 0x8F, (byte) 0xDF, (byte) 0x00, (byte) 0x00, (byte) 0x01, (byte) 0xD7
                , (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00};
        byte[] actual = receiverReport.toByteArray();
        assertArrayEquals(receiveExpect, actual);

        RtcpBye bye = (RtcpBye) basePackages.get(1);
        assertEquals(2, bye.getHeader().getVersion());
        assertFalse(bye.getHeader().isPadding());
        assertEquals(1, bye.getHeader().getReceptionCount());
        assertEquals(ERtcpPackageType.BYE, bye.getHeader().getPackageType());
        assertEquals(1, bye.getHeader().getLength());
        assertEquals(3276085244L, bye.getSourceId());
        byte[] byeExpect = new byte[]{(byte) 0x81, (byte) 0xCB, (byte) 0x00, (byte) 0x01, (byte) 0xC3, (byte) 0x45, (byte) 0x17, (byte) 0xFC};
        actual = bye.toByteArray();
        assertArrayEquals(byeExpect, actual);
    }
}
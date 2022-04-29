package com.github.xingshuangs.iot.protocol.s7.model;

import com.github.xingshuangs.iot.utils.ByteUtil;
import com.github.xingshuangs.iot.utils.ShortUtil;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class S7DataTest {

    /**
     * 请求连接0xE0命令的解析
     */
    @Test
    public void connectRequest() {
        byte[] data = new byte[]{
                // tpkt
                (byte) 0x03, (byte) 0x00, (byte) 0x00, (byte) 0x16,
                // cotp:
                (byte) 0x11, (byte) 0xE0, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x01, (byte) 0x00,
                // Parameter code：tpdu-size
                (byte) 0xC0, (byte) 0x01, (byte) 0x0A,
                // Parameter code: src-tsap
                (byte) 0xC1, (byte) 0x02, (byte) 0x01, (byte) 0x02,
                // Parameter code: dst-tsap
                (byte) 0xC2, (byte) 0x02, (byte) 0x01, (byte) 0x00
        };
        S7Data s7Data = S7Data.fromBytes(data);

        assertEquals(s7Data.getTpkt().getVersion(), (byte) 0x03);
        assertEquals(s7Data.getTpkt().getLength(), ShortUtil.toUInt16(data, 2));
        COTPConnection cotpConnection = (COTPConnection) s7Data.getCotp();
        assertEquals(cotpConnection.getLength(), ByteUtil.toUInt8(data[4]));
        assertEquals(cotpConnection.getPduType().getCode(), data[5]);
        assertEquals(cotpConnection.getDestinationReference(), ShortUtil.toUInt16(data, 6));
        assertEquals(cotpConnection.getSourceReference(), ShortUtil.toUInt16(data, 8));
        assertEquals(cotpConnection.getParameterCodeTpduSize(), data[11]);
        assertEquals(cotpConnection.getParameterLength1(), ByteUtil.toUInt8(data[12]));
        assertEquals(cotpConnection.getTpduSize(), ByteUtil.toUInt8(data[13]));
        assertEquals(cotpConnection.getParameterCodeSrcTsap(), data[14]);
        assertEquals(cotpConnection.getParameterLength2(), ByteUtil.toUInt8(data[15]));
        assertEquals(cotpConnection.getSourceTsap(), ShortUtil.toUInt16(data, 16));
        assertEquals(cotpConnection.getParameterCodeDstTsap(), data[18]);
        assertEquals(cotpConnection.getParameterLength3(), ByteUtil.toUInt8(data[19]));
        assertEquals(cotpConnection.getDestinationTsap(), ShortUtil.toUInt16(data, 20));
    }

    @Test
    public void connectConfirm() {
        byte[] data = new byte[]{
                // tpkt
                (byte) 0x03, (byte) 0x00, (byte) 0x00, (byte) 0x16,
                // cotp:
                (byte) 0x11, (byte) 0xD0, (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x06, (byte) 0x00,
                // Parameter code：tpdu-size
                (byte) 0xC0, (byte) 0x01, (byte) 0x0A,
                // Parameter code: src-tsap
                (byte) 0xC1, (byte) 0x02, (byte) 0x02, (byte) 0x01,
                // Parameter code: dst-tsap
                (byte) 0xC2, (byte) 0x02, (byte) 0x02, (byte) 0x01
        };
        S7Data s7Data = S7Data.fromBytes(data);

        assertEquals(s7Data.getTpkt().getVersion(), (byte) 0x03);
        assertEquals(s7Data.getTpkt().getLength(), ShortUtil.toUInt16(data, 2));
        COTPConnection cotp = (COTPConnection) s7Data.getCotp();
        assertEquals(cotp.getLength(), ByteUtil.toUInt8(data[4]));
        assertEquals(cotp.getPduType().getCode(), data[5]);
        assertEquals(cotp.getDestinationReference(), ShortUtil.toUInt16(data, 6));
        assertEquals(cotp.getSourceReference(), ShortUtil.toUInt16(data, 8));
        assertEquals(cotp.getParameterCodeTpduSize(), data[11]);
        assertEquals(cotp.getParameterLength1(), ByteUtil.toUInt8(data[12]));
        assertEquals(cotp.getTpduSize(), ByteUtil.toUInt8(data[13]));
        assertEquals(cotp.getParameterCodeSrcTsap(), data[14]);
        assertEquals(cotp.getParameterLength2(), ByteUtil.toUInt8(data[15]));
        assertEquals(cotp.getSourceTsap(), ShortUtil.toUInt16(data, 16));
        assertEquals(cotp.getParameterCodeDstTsap(), data[18]);
        assertEquals(cotp.getParameterLength3(), ByteUtil.toUInt8(data[19]));
        assertEquals(cotp.getDestinationTsap(), ShortUtil.toUInt16(data, 20));
    }

    @Test
    public void sendDtData() {
        byte[] data = new byte[]{
                // tpkt
                (byte) 0x03, (byte) 0x00, (byte) 0x00, (byte) 0x19,
                // cotp DT Data
                (byte) 0x02, (byte) 0xF0, (byte) 0x80,
                // header
                (byte) 0x32, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x04, (byte) 0x00, (byte) 0x00, (byte) 0x08, (byte) 0x00, (byte) 0x00,
                // parameter
                (byte) 0xF0, (byte) 0x00, (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x01, (byte) 0x01, (byte) 0xE0
        };
        S7Data s7Data = S7Data.fromBytes(data);

        assertEquals(s7Data.getTpkt().getVersion(), (byte) 0x03);
        assertEquals(s7Data.getTpkt().getLength(), ShortUtil.toUInt16(data, 2));
        COTPData cotp = (COTPData) s7Data.getCotp();
        assertEquals(cotp.getLength(), ByteUtil.toUInt8(data[4]));
        assertEquals(cotp.getPduType().getCode(), data[5]);
        assertEquals(cotp.getTpduNumber(), 0);
        assertTrue(cotp.isLastDataUnit());
        Header header = s7Data.getHeader();
        assertEquals(header.getProtocolId(), data[7]);
        assertEquals(header.getMessageType().getCode(), data[8]);
        assertEquals(header.getReserved(), ShortUtil.toUInt16(data, 9));
        assertEquals(header.getPduReference(), ShortUtil.toUInt16(data, 11, true));
        assertEquals(header.getParameterLength(), ShortUtil.toUInt16(data, 13));
        assertEquals(header.getDataLength(), ShortUtil.toUInt16(data, 15));
        SetupComParameter parameter = (SetupComParameter) s7Data.getParameter();
        assertEquals(parameter.getFunctionCode().getCode(), data[17]);
        assertEquals(parameter.getReserved(), data[18]);
        assertEquals(parameter.getMaxAmqCaller(), ShortUtil.toUInt16(data, 19));
        assertEquals(parameter.getMaxAmqCallee(), ShortUtil.toUInt16(data, 21));
        assertEquals(parameter.getPduLength(), ShortUtil.toUInt16(data, 23));
    }

    @Test
    public void ackDtData() {
        byte[] data = new byte[]{
                // tpkt
                (byte) 0x03, (byte) 0x00, (byte) 0x00, (byte) 0x1B,
                // cotp DT Data
                (byte) 0x02, (byte) 0xF0, (byte) 0x80,
                // header
                (byte) 0x32, (byte) 0x03, (byte) 0x00, (byte) 0x00, (byte) 0x04, (byte) 0x00, (byte) 0x00, (byte) 0x08, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                // parameter
                (byte) 0xF0, (byte) 0x00, (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x01, (byte) 0x01, (byte) 0xF0
        };
        S7Data s7Data = S7Data.fromBytes(data);

        assertEquals(s7Data.getTpkt().getVersion(), (byte) 0x03);
        assertEquals(s7Data.getTpkt().getLength(), ShortUtil.toUInt16(data, 2));
        COTPData cotp = (COTPData) s7Data.getCotp();
        assertEquals(cotp.getLength(), ByteUtil.toUInt8(data[4]));
        assertEquals(cotp.getPduType().getCode(), (byte) data[5]);
        assertEquals(cotp.getTpduNumber(), 0);
        assertTrue(cotp.isLastDataUnit());
        AckHeader header = (AckHeader) s7Data.getHeader();
        assertEquals(header.getProtocolId(), data[7]);
        assertEquals(header.getMessageType().getCode(), (byte) data[8]);
        assertEquals(header.getReserved(), ShortUtil.toUInt16(data, 9));
        assertEquals(header.getPduReference(), ShortUtil.toUInt16(data, 11, true));
        assertEquals(header.getParameterLength(), ShortUtil.toUInt16(data, 13));
        assertEquals(header.getDataLength(), ShortUtil.toUInt16(data, 15));
        assertEquals(header.getErrorClass().getCode(), data[17]);
        assertEquals(header.getErrorCode(), data[18]);
        SetupComParameter parameter = (SetupComParameter) s7Data.getParameter();
        assertEquals(parameter.getFunctionCode().getCode(), data[19]);
        assertEquals(parameter.getReserved(), data[20]);
        assertEquals(parameter.getMaxAmqCaller(), ShortUtil.toUInt16(data, 21));
        assertEquals(parameter.getMaxAmqCallee(), ShortUtil.toUInt16(data, 23));
        assertEquals(parameter.getPduLength(), ShortUtil.toUInt16(data, 25));
    }
}
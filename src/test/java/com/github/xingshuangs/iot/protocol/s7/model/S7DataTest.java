package com.github.xingshuangs.iot.protocol.s7.model;

import com.github.xingshuangs.iot.protocol.s7.enums.*;
import com.github.xingshuangs.iot.utils.ShortUtil;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@SuppressWarnings("Duplicates")
public class S7DataTest {

    @Test
    public void createConnectRequest() {
        S7Data s7Data = S7Data.createConnectRequest(0x0100,0x0100);
        assertEquals(22, s7Data.byteArrayLength());
    }

    @Test
    public void createConnectDtData() {
        S7Data s7Data = S7Data.createConnectDtData(240);
        assertEquals(25, s7Data.byteArrayLength());
        assertEquals(8, s7Data.getHeader().getParameterLength());
    }

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
                (byte) 0xC1, (byte) 0x02, (byte) 0x01, (byte) 0x00,
                // Parameter code: dst-tsap
                (byte) 0xC2, (byte) 0x02, (byte) 0x01, (byte) 0x02
        };
        S7Data s7Data = S7Data.fromBytes(data);

        assertEquals((byte) 0x03, s7Data.getTpkt().getVersion());
        assertEquals(22, s7Data.getTpkt().getLength());
        COTPConnection cotp = (COTPConnection) s7Data.getCotp();
        assertEquals(17, cotp.getLength());
        assertEquals(EPduType.CONNECT_REQUEST, cotp.getPduType());
        assertEquals(0, cotp.getDestinationReference());
        assertEquals(1, cotp.getSourceReference());
        assertEquals((byte) 0x00, cotp.getFlags());
        assertEquals((byte) 0xC0, cotp.getParameterCodeTpduSize());
        assertEquals(1, cotp.getParameterLength1());
        assertEquals((byte) 0x0A, cotp.getTpduSize());
        assertEquals((byte) 0xC1, cotp.getParameterCodeSrcTsap());
        assertEquals(2, cotp.getParameterLength2());
        assertEquals(ShortUtil.toUInt16(data, 16), cotp.getSourceTsap());
        assertEquals((byte) 0xC2, cotp.getParameterCodeDstTsap());
        assertEquals(2, cotp.getParameterLength3());
        assertEquals(ShortUtil.toUInt16(data, 20), cotp.getDestinationTsap());
    }

    /**
     * 连接响应确认
     */
    @Test
    public void connectConfirm() {
        byte[] data = new byte[]{
                // tpkt
                (byte) 0x03, (byte) 0x00, (byte) 0x00, (byte) 0x16,
                // cotp:
                (byte) 0x11, (byte) 0xD0, (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x01, (byte) 0x00,
                // Parameter code：tpdu-size
                (byte) 0xC0, (byte) 0x01, (byte) 0x0A,
                // Parameter code: src-tsap
                (byte) 0xC1, (byte) 0x02, (byte) 0x01, (byte) 0x00,
                // Parameter code: dst-tsap
                (byte) 0xC2, (byte) 0x02, (byte) 0x01, (byte) 0x02
        };
        S7Data s7Data = S7Data.fromBytes(data);

        assertEquals((byte) 0x03, s7Data.getTpkt().getVersion());
        assertEquals(ShortUtil.toUInt16(data, 2), s7Data.getTpkt().getLength());
        COTPConnection cotp = (COTPConnection) s7Data.getCotp();
        assertEquals(17, cotp.getLength());
        assertEquals(EPduType.CONNECT_CONFIRM, cotp.getPduType());
        assertEquals(1, cotp.getDestinationReference());
        assertEquals(1, cotp.getSourceReference());
        assertEquals((byte) 0x00, cotp.getFlags());
        assertEquals((byte) 0xC0, cotp.getParameterCodeTpduSize());
        assertEquals(1, cotp.getParameterLength1());
        assertEquals((byte) 0x0A, cotp.getTpduSize());
        assertEquals((byte) 0xC1, cotp.getParameterCodeSrcTsap());
        assertEquals(2, cotp.getParameterLength2());
        assertEquals(ShortUtil.toUInt16(data, 16), cotp.getSourceTsap());
        assertEquals((byte) 0xC2, cotp.getParameterCodeDstTsap());
        assertEquals(2, cotp.getParameterLength3());
        assertEquals(ShortUtil.toUInt16(data, 20), cotp.getDestinationTsap());
    }

    /**
     * 第二次握手发送报文
     */
    @Test
    public void sendDtData() {
        byte[] data = new byte[]{
                // tpkt
                (byte) 0x03, (byte) 0x00, (byte) 0x00, (byte) 0x19,
                // cotp DT Data
                (byte) 0x02, (byte) 0xF0, (byte) 0x80,
                // header
                (byte) 0x32, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x12, (byte) 0x00, (byte) 0x00, (byte) 0x08, (byte) 0x00, (byte) 0x00,
                // parameter
                (byte) 0xF0, (byte) 0x00, (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x01, (byte) 0x01, (byte) 0xE0
        };
        S7Data s7Data = S7Data.fromBytes(data);

        assertEquals((byte) 0x03, s7Data.getTpkt().getVersion());
        assertEquals(25, s7Data.getTpkt().getLength());
        COTPData cotp = (COTPData) s7Data.getCotp();
        assertEquals(2, cotp.getLength());
        assertEquals(EPduType.DT_DATA, cotp.getPduType());
        assertEquals(0, cotp.getTpduNumber());
        assertTrue(cotp.isLastDataUnit());
        Header header = s7Data.getHeader();
        assertEquals((byte) 0x32, header.getProtocolId());
        assertEquals(EMessageType.JOB, header.getMessageType());
        assertEquals(0, header.getReserved());
        assertEquals(4608, header.getPduReference());
        assertEquals(8, header.getParameterLength());
        assertEquals(0, header.getDataLength());
        SetupComParameter parameter = (SetupComParameter) s7Data.getParameter();
        assertEquals(EFunctionCode.SETUP_COMMUNICATION, parameter.getFunctionCode());
        assertEquals((byte) 0x00, parameter.getReserved());
        assertEquals(1, parameter.getMaxAmqCaller());
        assertEquals(1, parameter.getMaxAmqCallee());
        assertEquals(480, parameter.getPduLength());
    }

    /**
     * 第二次握手响应报文
     */
    @Test
    public void ackDtData() {
        byte[] data = new byte[]{
                // tpkt
                (byte) 0x03, (byte) 0x00, (byte) 0x00, (byte) 0x1B,
                // cotp DT Data
                (byte) 0x02, (byte) 0xF0, (byte) 0x80,
                // header
                (byte) 0x32, (byte) 0x03, (byte) 0x00, (byte) 0x00, (byte) 0x12, (byte) 0x00, (byte) 0x00, (byte) 0x08, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                // parameter
                (byte) 0xF0, (byte) 0x00, (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x01, (byte) 0x01, (byte) 0xE0
        };
        S7Data s7Data = S7Data.fromBytes(data);

        assertEquals((byte) 0x03, s7Data.getTpkt().getVersion());
        assertEquals(27, s7Data.getTpkt().getLength());
        COTPData cotp = (COTPData) s7Data.getCotp();
        assertEquals(2, cotp.getLength());
        assertEquals(EPduType.DT_DATA, cotp.getPduType());
        assertEquals(0, cotp.getTpduNumber());
        assertTrue(cotp.isLastDataUnit());
        AckHeader header = (AckHeader) s7Data.getHeader();
        assertEquals((byte) 0x32, header.getProtocolId());
        assertEquals(EMessageType.ACK_DATA, header.getMessageType());
        assertEquals(0, header.getReserved());
        assertEquals(4608, header.getPduReference());
        assertEquals(8, header.getParameterLength());
        assertEquals(0, header.getDataLength());
        assertEquals((byte) 0x00, header.getErrorClass().getCode());
        assertEquals((byte) 0x00, header.getErrorCode());
        SetupComParameter parameter = (SetupComParameter) s7Data.getParameter();
        assertEquals(EFunctionCode.SETUP_COMMUNICATION, parameter.getFunctionCode());
        assertEquals((byte) 0x00, parameter.getReserved());
        assertEquals(1, parameter.getMaxAmqCaller());
        assertEquals(1, parameter.getMaxAmqCallee());
        assertEquals(480, parameter.getPduLength());
    }

    /**
     * 读取数据
     */
    @Test
    public void readData() {
        byte[] data = new byte[]{
                // tpkt
                (byte) 0x03, (byte) 0x00, (byte) 0x00, (byte) 0x1F,
                // cotp DT Data
                (byte) 0x02, (byte) 0xF0, (byte) 0x80,
                // header
                (byte) 0x32, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x1C, (byte) 0x00, (byte) 0x00, (byte) 0x0E, (byte) 0x00, (byte) 0x00,
                // parameter：读功能 + 个数
                (byte) 0x04, (byte) 0x01,
                // request item
                (byte) 0x12,
                // 剩余长度
                (byte) 0x0A,
                // 寻址方式
                (byte) 0x10,
                // 数据类型
                (byte) 0x02,
                // 读取长度 count
                (byte) 0x00, (byte) 0x01,
                // db编号 dbNumber
                (byte) 0x00, (byte) 0x01,
                // 存储区域 area
                (byte) 0x84,
                // 起始地址 byteAddress
                (byte) 0x00, (byte) 0x00, (byte) 0x19
        };

        S7Data s7Data = S7Data.fromBytes(data);

        assertEquals((byte) 0x03, s7Data.getTpkt().getVersion());
        assertEquals(31, s7Data.getTpkt().getLength());
        COTPData cotp = (COTPData) s7Data.getCotp();
        assertEquals(2, cotp.getLength());
        assertEquals(EPduType.DT_DATA, cotp.getPduType());
        assertEquals(0, cotp.getTpduNumber());
        assertTrue(cotp.isLastDataUnit());
        Header header = s7Data.getHeader();
        assertEquals((byte) 0x32, header.getProtocolId());
        assertEquals(EMessageType.JOB, header.getMessageType());
        assertEquals(0, header.getReserved());
        assertEquals(7168, header.getPduReference());
        assertEquals(14, header.getParameterLength());
        assertEquals(0, header.getDataLength());
        ReadWriteParameter parameter = (ReadWriteParameter) s7Data.getParameter();
        assertEquals(EFunctionCode.READ_VARIABLE, parameter.getFunctionCode());
        assertEquals(1, parameter.getItemCount());
        List<RequestItem> requestItems = parameter.getRequestItems();
        for (RequestItem requestItem : requestItems) {
            assertEquals((byte) 0x12, requestItem.getSpecificationType());
            assertEquals(10, requestItem.getLengthOfFollowing());
            assertEquals(ESyntaxID.S7ANY, requestItem.getSyntaxId());
            assertEquals(EParamVariableType.BYTE, requestItem.getVariableType());
            assertEquals(1, requestItem.getCount());
            assertEquals(1, requestItem.getDbNumber());
            assertEquals(EArea.DATA_BLOCKS, requestItem.getArea());
            assertEquals(3, requestItem.getByteAddress());
            assertEquals(1, requestItem.getBitAddress());
        }
    }

    /**
     * 响应读取数据
     */
    @Test
    public void ackReadData() {
        byte[] data = new byte[]{
                // tpkt
                (byte) 0x03, (byte) 0x00, (byte) 0x00, (byte) 0x1A,
                // cotp DT Data
                (byte) 0x02, (byte) 0xF0, (byte) 0x80,
                // header + 12长度
                (byte) 0x32, (byte) 0x03, (byte) 0x00, (byte) 0x00, (byte) 0x1C, (byte) 0x00, (byte) 0x00, (byte) 0x02, (byte) 0x00, (byte) 0x05, (byte) 0x00, (byte) 0x00,
                // parameter：读功能 + 个数
                (byte) 0x04, (byte) 0x01,
                // data item
                (byte) 0xFF,
                // variable type
                (byte) 0x04,
                // length
                (byte) 0x00, (byte) 0x08,
                // 起始地址 byteAddress
                (byte) 0x00
        };

        S7Data s7Data = S7Data.fromBytes(data);

        assertEquals((byte) 0x03, s7Data.getTpkt().getVersion());
        assertEquals(26, s7Data.getTpkt().getLength());
        COTPData cotp = (COTPData) s7Data.getCotp();
        assertEquals(2, cotp.getLength());
        assertEquals(EPduType.DT_DATA, cotp.getPduType());
        assertEquals(0, cotp.getTpduNumber());
        assertTrue(cotp.isLastDataUnit());
        AckHeader header = (AckHeader) s7Data.getHeader();
        assertEquals((byte) 0x32, header.getProtocolId());
        assertEquals(EMessageType.ACK_DATA, header.getMessageType());
        assertEquals(0, header.getReserved());
        assertEquals(7168, header.getPduReference());
        assertEquals(2, header.getParameterLength());
        assertEquals(5, header.getDataLength());
        assertEquals(EErrorClass.NO_ERROR, header.getErrorClass());
        assertEquals((byte) 0x00, header.getErrorCode());
        ReadWriteParameter parameter = (ReadWriteParameter) s7Data.getParameter();
        assertEquals(EFunctionCode.READ_VARIABLE, parameter.getFunctionCode());
        assertEquals(1, parameter.getItemCount());
        assertEquals(0, parameter.getRequestItems().size());
        List<ReturnItem> returnItems = s7Data.getDatum().getReturnItems();
        for (ReturnItem returnItem : returnItems) {
            DataItem item = (DataItem) returnItem;
            assertEquals(EReturnCode.SUCCESS, item.getReturnCode());
            assertEquals(EDataVariableType.BYTE_WORD_DWORD, item.getVariableType());
            assertEquals(1, item.getCount());
            assertArrayEquals(new byte[]{(byte) 0x00}, item.getData());
        }
    }

    /**
     * 写入数据
     */
    @Test
    public void writeData() {
        byte[] data = new byte[]{
                // tpkt
                (byte) 0x03, (byte) 0x00, (byte) 0x00, (byte) 0x24,
                // cotp DT Data
                (byte) 0x02, (byte) 0xF0, (byte) 0x80,
                // header
                (byte) 0x32, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x1A, (byte) 0x00, (byte) 0x00, (byte) 0x0E, (byte) 0x00, (byte) 0x05,
                // parameter：功能 + 个数
                (byte) 0x05, (byte) 0x01,
                // request item
                (byte) 0x12,
                // 剩余长度
                (byte) 0x0A,
                // 寻址方式
                (byte) 0x10,
                // 数据类型
                (byte) 0x02,
                // 读取长度 count
                (byte) 0x00, (byte) 0x01,
                // db编号 dbNumber
                (byte) 0x00, (byte) 0x01,
                // 存储区域 area
                (byte) 0x84,
                // 起始地址 byteAddress
                (byte) 0x00, (byte) 0x00, (byte) 0x19,
                // 写的内容
                (byte) 0x00, (byte) 0x04, (byte) 0x00, (byte) 0x08, (byte) 0x00
        };

        S7Data s7Data = S7Data.fromBytes(data);

        assertEquals((byte) 0x03, s7Data.getTpkt().getVersion());
        assertEquals(36, s7Data.getTpkt().getLength());
        COTPData cotp = (COTPData) s7Data.getCotp();
        assertEquals(2, cotp.getLength());
        assertEquals(EPduType.DT_DATA, cotp.getPduType());
        assertEquals(0, cotp.getTpduNumber());
        assertTrue(cotp.isLastDataUnit());
        Header header = s7Data.getHeader();
        assertEquals((byte) 0x32, header.getProtocolId());
        assertEquals(EMessageType.JOB, header.getMessageType());
        assertEquals(0, header.getReserved());
        assertEquals(6656, header.getPduReference());
        assertEquals(14, header.getParameterLength());
        assertEquals(5, header.getDataLength());
        ReadWriteParameter parameter = (ReadWriteParameter) s7Data.getParameter();
        assertEquals(EFunctionCode.WRITE_VARIABLE, parameter.getFunctionCode());
        assertEquals(1, parameter.getItemCount());
        List<RequestItem> requestItems = parameter.getRequestItems();
        for (RequestItem requestItem : requestItems) {
            assertEquals((byte) 0x12, requestItem.getSpecificationType());
            assertEquals(10, requestItem.getLengthOfFollowing());
            assertEquals(ESyntaxID.S7ANY, requestItem.getSyntaxId());
            assertEquals(EParamVariableType.BYTE, requestItem.getVariableType());
            assertEquals(1, requestItem.getCount());
            assertEquals(1, requestItem.getDbNumber());
            assertEquals(EArea.DATA_BLOCKS, requestItem.getArea());
            assertEquals(3, requestItem.getByteAddress());
            assertEquals(1, requestItem.getBitAddress());
        }
        List<ReturnItem> returnItems = s7Data.getDatum().getReturnItems();
        for (ReturnItem returnItem : returnItems) {
            DataItem item = (DataItem) returnItem;
            assertEquals(EReturnCode.RESERVED, item.getReturnCode());
            assertEquals(EDataVariableType.BYTE_WORD_DWORD, item.getVariableType());
            assertEquals(1, item.getCount());
            assertArrayEquals(new byte[]{(byte) 0x00}, item.getData());
        }
    }

    /**
     * 响应写入数据
     */
    @Test
    public void ackWriteData() {
        byte[] data = new byte[]{
                // tpkt
                (byte) 0x03, (byte) 0x00, (byte) 0x00, (byte) 0x16,
                // cotp DT Data
                (byte) 0x02, (byte) 0xF0, (byte) 0x80,
                // header + 12长度
                (byte) 0x32, (byte) 0x03, (byte) 0x00, (byte) 0x00, (byte) 0x1A, (byte) 0x00, (byte) 0x00, (byte) 0x02, (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x00,
                // parameter：读功能 + 个数
                (byte) 0x05, (byte) 0x01,
                // data item
                (byte) 0xFF
        };

        S7Data s7Data = S7Data.fromBytes(data);

        assertEquals((byte) 0x03, s7Data.getTpkt().getVersion());
        assertEquals(22, s7Data.getTpkt().getLength());
        COTPData cotp = (COTPData) s7Data.getCotp();
        assertEquals(2, cotp.getLength());
        assertEquals(EPduType.DT_DATA, cotp.getPduType());
        assertEquals(0, cotp.getTpduNumber());
        assertTrue(cotp.isLastDataUnit());
        AckHeader header = (AckHeader) s7Data.getHeader();
        assertEquals((byte) 0x32, header.getProtocolId());
        assertEquals(EMessageType.ACK_DATA, header.getMessageType());
        assertEquals(0, header.getReserved());
        assertEquals(6656, header.getPduReference());
        assertEquals(2, header.getParameterLength());
        assertEquals(1, header.getDataLength());
        assertEquals(EErrorClass.NO_ERROR, header.getErrorClass());
        assertEquals((byte) 0x00, header.getErrorCode());
        ReadWriteParameter parameter = (ReadWriteParameter) s7Data.getParameter();
        assertEquals(EFunctionCode.WRITE_VARIABLE, parameter.getFunctionCode());
        assertEquals(1, parameter.getItemCount());
        assertEquals(0, parameter.getRequestItems().size());
        List<ReturnItem> returnItems = s7Data.getDatum().getReturnItems();
        for (ReturnItem returnItem : returnItems) {
            assertEquals(EReturnCode.SUCCESS, returnItem.getReturnCode());
        }
    }

    /**
     * 响应多读取数据
     */
    @Test
    public void ackMultiReadData() {
        byte[] data = new byte[]{
                // tpkt
                (byte) 0x03, (byte) 0x00, (byte) 0x00, (byte) 0x2f,
                // cotp DT Data
                (byte) 0x02, (byte) 0xF0, (byte) 0x80,
                // header + 12长度
                (byte) 0x32, (byte) 0x03, (byte) 0x00, (byte) 0x00, (byte) 0x09, (byte) 0x00, (byte) 0x00, (byte) 0x02, (byte) 0x00, (byte) 0x1A, (byte) 0x00, (byte) 0x00,
                // parameter：读功能 + 个数
                (byte) 0x04, (byte) 0x05,
                // data item
                (byte) 0xFF, (byte) 0x04, (byte) 0x00, (byte) 0x10, (byte) 0x00, (byte) 0x01,
                (byte) 0xFF, (byte) 0x04, (byte) 0x00, (byte) 0x10, (byte) 0x00, (byte) 0x05,
                (byte) 0xFF, (byte) 0x04, (byte) 0x00, (byte) 0x10, (byte) 0x00, (byte) 0x00,
                (byte) 0x0A, (byte) 0x00, (byte) 0x00, (byte) 0x04,
                (byte) 0x0A, (byte) 0x00, (byte) 0x00, (byte) 0x04
        };

        S7Data s7Data = S7Data.fromBytes(data);

        assertEquals((byte) 0x03, s7Data.getTpkt().getVersion());
        assertEquals(47, s7Data.getTpkt().getLength());
        COTPData cotp = (COTPData) s7Data.getCotp();
        assertEquals(2, cotp.getLength());
        assertEquals(EPduType.DT_DATA, cotp.getPduType());
        assertEquals(0, cotp.getTpduNumber());
        assertTrue(cotp.isLastDataUnit());
        AckHeader header = (AckHeader) s7Data.getHeader();
        assertEquals((byte) 0x32, header.getProtocolId());
        assertEquals(EMessageType.ACK_DATA, header.getMessageType());
        assertEquals(0, header.getReserved());
        assertEquals(2304, header.getPduReference());
        assertEquals(2, header.getParameterLength());
        assertEquals(26, header.getDataLength());
        assertEquals(EErrorClass.NO_ERROR, header.getErrorClass());
        assertEquals((byte) 0x00, header.getErrorCode());
        ReadWriteParameter parameter = (ReadWriteParameter) s7Data.getParameter();
        assertEquals(EFunctionCode.READ_VARIABLE, parameter.getFunctionCode());
        assertEquals(5, parameter.getItemCount());
        assertEquals(0, parameter.getRequestItems().size());
        List<ReturnItem> returnItems = s7Data.getDatum().getReturnItems();
        for (int i = 0; i < returnItems.size(); i++) {
            DataItem item = (DataItem) returnItems.get(i);
            if(i<3){
                assertEquals(EReturnCode.SUCCESS, item.getReturnCode());
                assertEquals(EDataVariableType.BYTE_WORD_DWORD, item.getVariableType());
                assertEquals(2, item.getCount());
            }else {
                assertEquals(EReturnCode.OBJECT_DOES_NOT_EXIST, item.getReturnCode());
                assertEquals(EDataVariableType.NULL, item.getVariableType());
                assertEquals(0, item.getCount());
            }
        }
    }
}
package com.github.xingshuangs.iot.protocol.s7.service;


import com.github.xingshuangs.iot.exceptions.S7CommException;
import com.github.xingshuangs.iot.net.socket.SocketBasic;
import com.github.xingshuangs.iot.protocol.s7.enums.EErrorClass;
import com.github.xingshuangs.iot.protocol.s7.enums.EPduType;
import com.github.xingshuangs.iot.protocol.s7.model.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * plc的网络通信
 *
 * @author xingshuang
 */
public class PLCNetwork extends SocketBasic {

    private int maxPduLength = 0;

    public PLCNetwork() {
        super();
    }

    public PLCNetwork(String host, int port) {
        super(host, port);
    }

    /**
     * 连接成功之后要做的动作
     */
    @Override
    protected void doAfterConnected() throws IOException {
        this.connectionRequest();
        this.maxPduLength = this.connectDtData() - 20;
    }


    public S7Data readFromServer(S7Data s7Data) throws IOException {
        this.writeBaseBytes(s7Data.toByteArray());

        byte[] data = new byte[TPKT.BYTE_LENGTH];
        int len = this.readBaseBytes(data);
        if (len < TPKT.BYTE_LENGTH) {
            throw new S7CommException(" TPKT 无效，长度不一致");
        }
        TPKT tpkt = TPKT.fromBytes(data);
        byte[] remain = new byte[tpkt.getLength() - TPKT.BYTE_LENGTH];
        len = this.readBaseBytes(remain);
        if (len < remain.length) {
            throw new S7CommException(" TPKT后面的数据长度，长度不一致");
        }
        return S7Data.fromBytes(tpkt, remain);
    }

    /**
     * 读取字节
     *
     * @param data 字节数组
     * @return 读取数量
     * @throws IOException 异常
     */
    private int readBaseBytes(final byte[] data) throws IOException {
        int offset = 0;
        while (offset < data.length) {
            int length = this.maxPduLength == 0 ? data.length - offset : Math.min(this.maxPduLength, data.length - offset);
            this.read(data, offset, length);
            offset += length;
        }
        return offset;
    }

    /**
     * 写入字节
     *
     * @param data 字节数组
     * @throws IOException 异常
     */
    private void writeBaseBytes(final byte[] data) throws IOException {
        int offset = 0;
        while (offset < data.length) {
            int length = this.maxPduLength == 0 ? data.length - offset : Math.min(this.maxPduLength, data.length - offset);
            this.write(data, offset, length);
            offset += length;
        }
    }

    /**
     * 连接请求
     *
     * @throws IOException 异常
     */
    private void connectionRequest() throws IOException {
        S7Data request = S7Data.createConnectRequest();
        S7Data ack = this.readFromServer(request);
        if (ack.getCotp().getPduType() != EPduType.CONNECT_CONFIRM) {
            throw new S7CommException("连接请求被拒绝");
        }
    }

    /**
     * 连接setup
     *
     * @return pduLength pdu长度
     * @throws IOException 异常
     */
    private int connectDtData() throws IOException {
        S7Data request = S7Data.createConnectDtData();
        S7Data ack = this.readFromServer(request);
        if (ack.getCotp().getPduType() != EPduType.DT_DATA) {
            throw new S7CommException("连接Setup响应错误");
        }
        if (ack.getHeader() == null || ack.getHeader().byteArrayLength() != AckHeader.BYTE_LENGTH) {
            throw new S7CommException("连接Setup响应错误，缺失响应头header或响应头长度不够[12]");
        }
        AckHeader ackHeader = (AckHeader) ack.getHeader();
        if (ackHeader.getErrorClass() != EErrorClass.NO_ERROR) {
            throw new S7CommException("连接Setup响应错误，原因：" + ackHeader.getErrorClass());
        }
        return ((SetupComParameter) ack.getParameter()).getPduLength();
    }

    /**
     * 读取S7协议数据
     *
     * @param requestItems 请求项列表
     * @return 数据项列表
     * @throws IOException 异常
     */
    protected List<DataItem> readS7Data(List<RequestItem> requestItems) throws IOException {
        S7Data s7Data = S7Data.createReadDefault();
        ReadWriteParameter parameter = (ReadWriteParameter) s7Data.getParameter();
        parameter.addItem(requestItems);
        s7Data.selfCheck();
        S7Data ack = this.readFromServer(s7Data);
        AckHeader ackHeader = (AckHeader) ack.getHeader();
        if (ackHeader.getErrorClass() != EErrorClass.NO_ERROR) {
            throw new S7CommException("读取异常");
        }
        if (ackHeader.getPduReference() != s7Data.getHeader().getPduReference()) {
            throw new S7CommException("pdu应用编号不一致，数据有误");
        }
        List<ReturnItem> returnItems = ack.getDatum().getReturnItems();
        if (returnItems.size() != parameter.getItemCount()) {
            throw new S7CommException("返回的数据个数不一致");
        }
        return returnItems.stream().map(x -> (DataItem) x).collect(Collectors.toList());
    }

    /**
     * 读取S7协议数据
     *
     * @param item 请求项
     * @return 数据项
     * @throws IOException 异常
     */
    protected DataItem readS7Data(RequestItem item) throws IOException {
        List<RequestItem> list = new ArrayList<>();
        list.add(item);
        List<DataItem> dataItems = this.readS7Data(list);
        return dataItems.get(0);
    }
}

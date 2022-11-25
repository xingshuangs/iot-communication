package com.github.xingshuangs.iot.protocol.s7.service;


import com.github.xingshuangs.iot.exceptions.S7CommException;
import com.github.xingshuangs.iot.net.socket.SocketBasic;
import com.github.xingshuangs.iot.protocol.common.buff.ByteReadBuff;
import com.github.xingshuangs.iot.protocol.s7.algorithm.S7ComGroup;
import com.github.xingshuangs.iot.protocol.s7.algorithm.S7ComItem;
import com.github.xingshuangs.iot.protocol.s7.algorithm.S7SequentialGroupAlg;
import com.github.xingshuangs.iot.protocol.s7.constant.ErrorCode;
import com.github.xingshuangs.iot.protocol.s7.enums.*;
import com.github.xingshuangs.iot.protocol.s7.model.*;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * plc的网络通信
 * 最小字节数组大小是240-18=222，480-18=462,960-18=942
 * 根据测试S1200[CPU 1214C]，单次读多字节，最大字节读取长度是 222 = 240 - 18, 18(响应报文的PDU)=12(header)+2(parameter)+4(dataItem)
 * 根据测试S1200[CPU 1214C]，单次写多字节，最大字节写入长度是 212 = 240 - 28, 28(请求报文的PDU)=10(header)+14(parameter)+4(dataItem)
 *
 * @author xingshuang
 */
@SuppressWarnings("Duplicates")
public class PLCNetwork extends SocketBasic {

    /**
     * 锁
     */
    private final Object objLock = new Object();

    /**
     * PLC的类型
     */
    protected EPlcType plcType = EPlcType.S1200;

    /**
     * PLC机架号
     */
    protected int rack = 0;

    /**
     * PLC槽号
     */
    protected int slot = 0;

    /**
     * 最大的PDU长度，不同PLC对应不同值，有240,480,960，目前默认240
     */
    protected int pduLength;

    /**
     * 通信回调
     */
    private Consumer<byte[]> comCallback;

    public void setComCallback(Consumer<byte[]> comCallback) {
        this.comCallback = comCallback;
    }

    public PLCNetwork() {
        super();
    }

    public PLCNetwork(String host, int port) {
        super(host, port);
    }

    //region socket连接后握手操作

    /**
     * 连接成功之后要做的动作
     */
    @Override
    protected void doAfterConnected() {
        this.connectionRequest();
        this.pduLength = this.connectDtData();
    }

    /**
     * 连接请求
     * 	1500	1200	300	    400	    200	    200Smart
     *  0x0102	0x0102	0x0102	0x0102	0x4d57	0x1000
     *  0x0100	0x0100	0x0102	0x0103	0x4d57	0x0300
     */
    private void connectionRequest() {
        // 对应0xC1
        int local = 0x0102;
        // 对应0xC2 | 经测试：S1200支持0x0100、0x0200、0x0300，S200Smart支持0x0200、0x0300
        int remote = 0x0100;
        switch (this.plcType) {
            case S200:
                // S7net中写的是0x1000,0x1001
                local = 0x4D57;
                remote = 0x4D57;
                break;
            case S200_SMART:
                local = 0x0100;
                remote = 0x0300;
                break;
            case S300:
            case S400:
            case S1200:
            case S1500:
                remote += 0x20 * this.rack + this.slot;
                break;
        }
        S7Data req = S7Data.createConnectRequest(local, remote);
        S7Data ack = this.readFromServer(req);
        if (ack.getCotp().getPduType() != EPduType.CONNECT_CONFIRM) {
            throw new S7CommException("连接请求被拒绝");
        }
    }

    /**
     * 连接setup
     *
     * @return pduLength pdu长度
     */
    private int connectDtData() {
        S7Data req = S7Data.createConnectDtData(this.pduLength);
        S7Data ack = this.readFromServer(req);
        if (ack.getCotp().getPduType() != EPduType.DT_DATA) {
            throw new S7CommException("连接Setup响应错误");
        }
        if (ack.getHeader() == null || ack.getHeader().byteArrayLength() != AckHeader.BYTE_LENGTH) {
            throw new S7CommException("连接Setup响应错误，缺失响应头header或响应头长度不够[12]");
        }
        int length = ((SetupComParameter) ack.getParameter()).getPduLength();
        if (length <= 0) {
            throw new S7CommException("PDU的最大长度小于0");
        }
        return length;
    }
    //endregion

    //region 底层数据通信部分

    /**
     * 从服务器读取数据
     *
     * @param req S7协议数据
     * @return S7协议数据
     */
    protected S7Data readFromServer(S7Data req) {
        byte[] sendData = req.toByteArray();
        if (this.comCallback != null) {
            this.comCallback.accept(sendData);
        }

        // 将报文中的TPKT和COTP减掉，剩下PDU的内容，7=4(tpkt)+3(cotp)
        if (this.pduLength > 0 && sendData.length - 7 > this.pduLength) {
            throw new S7CommException(String.format("发送请求的字节数过长[%d]，已经大于最大的PDU长度[%d]", sendData.length, this.pduLength));
        }

        TPKT tpkt;
        int len;
        byte[] remain;
        synchronized (this.objLock) {
            this.write(sendData);

            byte[] data = new byte[TPKT.BYTE_LENGTH];
            len = this.read(data);
            if (len < TPKT.BYTE_LENGTH) {
                throw new S7CommException(" TPKT 无效，长度不一致");
            }
            tpkt = TPKT.fromBytes(data);
            remain = new byte[tpkt.getLength() - TPKT.BYTE_LENGTH];
            len = this.read(remain);
        }
        if (len < remain.length) {
            throw new S7CommException(" TPKT后面的数据长度，长度不一致");
        }
        S7Data ack = S7Data.fromBytes(tpkt, remain);

        if (this.comCallback != null) {
            this.comCallback.accept(ack.toByteArray());
        }
        this.checkPostedCom(req, ack);
        return ack;
    }

    /**
     * 后置通信处理，对请求和响应数据进行一次校验
     *
     * @param req 请求数据
     * @param ack 响应属于
     */
    private void checkPostedCom(S7Data req, S7Data ack) {
        if (ack.getHeader() == null) {
            return;
        }
        // 响应头正确
        AckHeader ackHeader = (AckHeader) ack.getHeader();
        if (ackHeader.getErrorClass() != EErrorClass.NO_ERROR) {
            throw new S7CommException(String.format("响应异常，错误类型：%s，错误原因：%s",
                    ackHeader.getErrorClass().getDescription(), ErrorCode.MAP.get(ackHeader.getErrorCode())));
        }
        // 发送和接收的PDU编号一致
        if (ackHeader.getPduReference() != req.getHeader().getPduReference()) {
            throw new S7CommException("pdu应用编号不一致，数据有误");
        }
        if (ack.getDatum() == null) {
            return;
        }
        // 请求的数据个数一致
        List<ReturnItem> returnItems = ack.getDatum().getReturnItems();
        ReadWriteParameter parameter = (ReadWriteParameter) req.getParameter();
        if (returnItems.size() != parameter.getItemCount()) {
            throw new S7CommException("返回的数据个数和请求的数据个数不一致");
        }
        // 返回结果校验
        returnItems.forEach(x -> {
            if (x.getReturnCode() != EReturnCode.SUCCESS) {
                throw new S7CommException(String.format("返回结果异常，原因：%s", x.getReturnCode().getDescription()));
            }
        });
    }

    //endregion

    //region S7数据读写部分

    /**
     * 读取S7协议数据
     *
     * @param requestItems 请求项列表
     * @return 数据项列表
     */
    public List<DataItem> readS7Data(List<RequestItem> requestItems) {
        if (requestItems == null || requestItems.isEmpty()) {
            throw new S7CommException("请求项缺失，无法获取数据");
        }
        // 根据原始请求列表提取每个请求数据大小
        List<Integer> rawNumbers = requestItems.stream().map(RequestItem::getCount).collect(Collectors.toList());
        // 根据原始请求列表构建最终结果列表
        List<DataItem> resultList = requestItems.stream().map(x -> DataItem.createByByte(new byte[x.getCount()],
                x.getVariableType() == EParamVariableType.BIT ? EDataVariableType.BIT : EDataVariableType.BYTE_WORD_DWORD))
                .collect(Collectors.toList());

        // 根据顺序分组算法得出分组结果，14=12(header)+2(parameter),5(DataItem)，dataItem可能4或5，统一采用5
        List<S7ComGroup> s7ComGroups = S7SequentialGroupAlg.recombination(rawNumbers, this.pduLength - 14, 5);
        s7ComGroups.forEach(x -> {
            // 根据分组构建对应的请求列表
            List<S7ComItem> comItemList = x.getItems();
            List<RequestItem> newRequestItems = comItemList.stream().map(i -> {
                RequestItem item = requestItems.get(i.getIndex()).copy();
                item.setCount(i.getRipeSize());
                item.setByteAddress(item.getByteAddress() + i.getSplitOffset());
                return item;
            }).collect(Collectors.toList());

            // S7数据请求
            S7Data req = S7Data.createReadDefault();
            ReadWriteParameter parameter = (ReadWriteParameter) req.getParameter();
            parameter.addItem(newRequestItems);
            req.selfCheck();
            S7Data ack = this.readFromServer(req);
            List<DataItem> dataItems = ack.getDatum().getReturnItems().stream().map(a -> (DataItem) a).collect(Collectors.toList());

            // 将获取的数据重装实际结果列表中
            for (int i = 0; i < comItemList.size(); i++) {
                S7ComItem comItem = comItemList.get(i);
                byte[] src = dataItems.get(i).getData();
                byte[] des = resultList.get(comItem.getIndex()).getData();
                System.arraycopy(src, 0, des, comItem.getSplitOffset(), src.length);
            }
        });
        return resultList;
    }

    /**
     * 读取S7协议数据
     *
     * @param requestItem 请求项
     * @return 数据项
     */
    public DataItem readS7Data(RequestItem requestItem) {
        return this.readS7Data(Collections.singletonList(requestItem)).get(0);
    }

    /**
     * 写S7协议数据
     *
     * @param requestItem 请求项
     * @param dataItem    数据项
     */
    public void writeS7Data(RequestItem requestItem, DataItem dataItem) {
        this.writeS7Data(Collections.singletonList(requestItem), Collections.singletonList(dataItem));
    }

    /**
     * 写S7协议
     *
     * @param requestItems 请求项列表
     * @param dataItems    数据项列表
     */
    public void writeS7Data(List<RequestItem> requestItems, List<DataItem> dataItems) {
        if (requestItems.size() != dataItems.size()) {
            throw new S7CommException("写操作过程中，requestItems和dataItems数据个数不一致");
        }

        // 根据原始请求列表提取每个请求数据大小
        List<Integer> rawNumbers = requestItems.stream().map(RequestItem::getCount).collect(Collectors.toList());

        // 根据顺序分组算法得出分组结果 12=10(header)+2(parameter前),17=12(parameter后)+5(dataItem)，dataItem可能4或5，统一采用5
        List<S7ComGroup> s7ComGroups = S7SequentialGroupAlg.recombination(rawNumbers, this.pduLength - 12, 17);
        s7ComGroups.forEach(x -> {
            // 根据分组构建对应的请求列表
            List<S7ComItem> comItemList = x.getItems();
            List<RequestItem> newRequestItems = comItemList.stream().map(i -> {
                RequestItem item = requestItems.get(i.getIndex()).copy();
                item.setCount(i.getRipeSize());
                item.setByteAddress(item.getByteAddress() + i.getSplitOffset());
                return item;
            }).collect(Collectors.toList());
            // 根据分组构建对应的数据列表
            List<DataItem> newDataItems = comItemList.stream().map(i -> {
                DataItem item = dataItems.get(i.getIndex()).copy();
                item.setCount(i.getRipeSize());
                item.setData(ByteReadBuff.newInstance(item.getData()).getBytes(i.getSplitOffset(), i.getRipeSize()));
                return item;
            }).collect(Collectors.toList());

            // S7数据请求
            S7Data req = S7Data.createWriteDefault();
            ReadWriteParameter parameter = (ReadWriteParameter) req.getParameter();
            parameter.addItem(newRequestItems);
            req.getDatum().addItem(newDataItems);
            req.selfCheck();
            this.readFromServer(req);
        });
    }

    //endregion
}

/*
 * MIT License
 *
 * Copyright (c) 2021-2099 Oscura (xingshuang) <xingshuang_cool@163.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.xingshuangs.iot.protocol.s7.service;


import com.github.xingshuangs.iot.exceptions.S7CommException;
import com.github.xingshuangs.iot.net.client.TcpClientBasic;
import com.github.xingshuangs.iot.common.buff.ByteReadBuff;
import com.github.xingshuangs.iot.common.buff.ByteWriteBuff;
import com.github.xingshuangs.iot.protocol.s7.algorithm.S7ComGroup;
import com.github.xingshuangs.iot.protocol.s7.algorithm.S7ComItem;
import com.github.xingshuangs.iot.protocol.s7.algorithm.S7SequentialGroupAlg;
import com.github.xingshuangs.iot.protocol.s7.constant.ErrorCode;
import com.github.xingshuangs.iot.protocol.s7.enums.*;
import com.github.xingshuangs.iot.protocol.s7.model.*;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * plc的网络通信
 * 最大读取字节数组大小是240-18=222，480-18=462,960-18=942
 * 根据测试S1200[CPU 1214C]，单次读多字节
 * 发送：最大字节读取长度是 216 = 240 - 24, 24(请求报文的PDU)=10(header)+14(parameter)
 * 接收：最大字节读取长度是 222 = 240 - 18, 18(响应报文的PDU)=12(header)+2(parameter)+4(dataItem)
 * 根据测试S1200[CPU 1214C]，单次写多字节
 * 发送：最大字节写入长度是 212 = 240 - 28, 28(请求报文的PDU)=10(header)+14(parameter)+4(dataItem)
 * 接收：最大字节写入长度是 225 = 240 - 15, 15(响应报文的PDU)=12(header)+2(parameter)+1(dataItem)
 *
 * @author xingshuang
 */
@SuppressWarnings("DuplicatedCode")
@Slf4j
public class PLCNetwork extends TcpClientBasic {

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
     * PLC槽号，S7-300 = 2
     */
    protected int slot = 1;

    /**
     * 最大的PDU长度，不同PLC对应不同值，有240,480,960，目前默认240
     */
    protected int pduLength;

    /**
     * 是否持久化，默认是持久化，对应长连接，true：长连接，false：短连接
     */
    private boolean persistence = true;

    /**
     * 通信回调
     */
    private Consumer<byte[]> comCallback;

    public void setComCallback(Consumer<byte[]> comCallback) {
        this.comCallback = comCallback;
    }

    public boolean isPersistence() {
        return persistence;
    }

    public void setPersistence(boolean persistence) {
        this.persistence = persistence;
    }

    public EPlcType getPlcType() {
        return plcType;
    }

    public int getRack() {
        return rack;
    }

    public int getSlot() {
        return slot;
    }

    public int getPduLength() {
        return pduLength;
    }

    public PLCNetwork() {
        super();
    }

    public PLCNetwork(String host, int port) {
        super(host, port);
        this.tag = "S7";
    }

    @Override
    public void connect() {
        try {
            super.connect();
        } finally {
            if (!this.persistence) {
                this.close();
            }
        }
    }

    //region socket连接后握手操作

    /**
     * 连接成功之后要做的动作
     */
    @Override
    protected void doAfterConnected() {
        this.connectionRequest();
        // 存在设置的PDULength != 实际PLC的PDULength，因此以PLC的为准
        this.pduLength = this.connectDtData();
        log.debug("PLC[{}]握手成功，机架号[{}]，槽号[{}]，PDU长度[{}]", this.plcType, this.rack, this.slot, this.pduLength);
    }

    /**
     * 连接请求
     * <p>
     * TSAP包含两个字节，远程TSAP地址是连接的远程PC Access所设置的地址，
     * 第一个字节标识访问的资源，01是PG，02是OP，03是S7单边（服务器模式），10（16进制）及以上是S7双边通信；
     * 第二个字节是访问点，是CPU的槽号+CP槽号
     * 第一个字节：0x01+连接数目（S7-200）或者0x03+连接数目（S7-300/400)
     * 第二个字节：模块位置（S7-200）或者机架和槽位（S7-300/400）
     * 1500	    1200	300	    400	    200	    200Smart
     * 0x0102	0x0102	0x0102	0x0102	0x4d57	0x1000
     * 0x0100	0x0100	0x0102	0x0103	0x4d57	0x0300
     * ------------------------------------------------
     * 0x0100	0x0100	0x0100	0x0100	0x4d57	0x1000
     * 0x0300	0x0300	0x0302	0x0303	0x4d57	0x0300
     */
    private void connectionRequest() {
        // 对应0xC1
        int local = 0x0100;
        // 对应0xC2 | 经测试：S1200支持0x0100、0x0200、0x0300，S200Smart支持0x0200、0x0300
        int remote = 0x0300;
        switch (this.plcType) {
            case S200:
                // S7net中写的是0x1000,0x1001
                local = 0x4D57;
                remote = 0x4D57;
                break;
            case S200_SMART:
                local = 0x1000;
                // 远程只能设置为0x0200,0x0201,0x0300,0x0301
                remote = 0x0300;
                break;
            case S300:
            case S400:
            case S1200:
            case S1500:
                remote += 0x20 * this.rack + this.slot;
                break;
            case SINUMERIK_828D:
                local = 0x0400;
                remote = 0x0D04;
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
    private S7Data readFromServer(S7Data req) {
        byte[] sendData = req.toByteArray();
        byte[] total = this.readFromServer(sendData);
        S7Data ack = S7Data.fromBytes(total);
        this.checkPostedCom(req, ack);
        return ack;
    }

    /**
     * 以字节数组的方式和服务器进行数据交互
     *
     * @param sendData 发送的字节数据
     * @return 接收的字节数据
     */
    private byte[] readFromServer(byte[] sendData) {
        if (this.comCallback != null) {
            this.comCallback.accept(sendData);
        }

        // 将报文中的TPKT和COTP减掉，剩下PDU的内容，7=4(tpkt)+3(cotp)
        if (this.pduLength > 0 && sendData.length - 7 > this.pduLength) {
            throw new S7CommException(String.format("发送请求的字节数过长[%d]，已经大于最大的PDU长度[%d]", sendData.length, this.pduLength));
        }

        TPKT tpkt;
        int len;
        byte[] total;
        synchronized (this.objLock) {
            this.write(sendData);

            byte[] data = new byte[TPKT.BYTE_LENGTH];
            len = this.read(data);
            if (len < TPKT.BYTE_LENGTH) {
                throw new S7CommException(" TPKT 无效，长度不一致");
            }
            tpkt = TPKT.fromBytes(data);
            total = new byte[tpkt.getLength()];
            System.arraycopy(data, 0, total, 0, data.length);
            len = this.read(total, TPKT.BYTE_LENGTH, tpkt.getLength() - TPKT.BYTE_LENGTH);
        }
        if (len < total.length - TPKT.BYTE_LENGTH) {
            throw new S7CommException(" TPKT后面的数据长度，长度不一致");
        }
        if (this.comCallback != null) {
            this.comCallback.accept(total);
        }
        return total;
    }

    /**
     * 包含持久化的从服务器读取数据，外部继承使用该方法进行交互，内部不使用
     *
     * @param req 请求数据
     * @return 响应数据
     */
    public S7Data readFromServerByPersistence(S7Data req) {
        try {
            return this.readFromServer(req);
        } finally {
            if (!this.persistence) {
                this.close();
            }
        }
    }

    /**
     * 包含持久化的从服务器读取数据，外部继承使用该方法进行交互，内部不使用
     *
     * @param req 请求数据
     * @return 响应数据
     */
    public byte[] readFromServerByPersistence(byte[] req) {
        try {
            return this.readFromServer(req);
        } finally {
            if (!this.persistence) {
                this.close();
            }
        }
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
        if (ackHeader.getErrorClass() == null) {
            throw new S7CommException(String.format("响应异常，未知异常：%s", ErrorCode.MAP.getOrDefault(ackHeader.getErrorCode(), "错误码不存在")));
        }
        if (ackHeader.getErrorClass() != EErrorClass.NO_ERROR) {
            throw new S7CommException(String.format("响应异常，错误类型：%s，错误原因：%s",
                    ackHeader.getErrorClass().getDescription(), ErrorCode.MAP.getOrDefault(ackHeader.getErrorCode(), "错误码不存在")));
        }
        // 发送和接收的PDU编号一致
        if (ackHeader.getPduReference() != req.getHeader().getPduReference()) {
            throw new S7CommException("pdu应用编号不一致，数据有误");
        }
        if (ack.getDatum() == null) {
            return;
        }
        if (!(ack.getDatum() instanceof ReadWriteDatum)) {
            return;
        }
        ReadWriteDatum datum = (ReadWriteDatum) ack.getDatum();
        // 请求的数据个数一致
        List<ReturnItem> returnItems = datum.getReturnItems();
        ReadWriteParameter parameter = (ReadWriteParameter) req.getParameter();
        if (returnItems.size() != parameter.getItemCount()) {
            throw new S7CommException("返回的数据个数和请求的数据个数不一致");
        }
        // 返回结果校验
        for (int i = 0; i < returnItems.size(); i++) {
            if (returnItems.get(i).getReturnCode() != EReturnCode.SUCCESS) {
                throw new S7CommException(String.format("返回第[%d]个结果异常，原因：%s", i + 1, returnItems.get(i).getReturnCode().getDescription()));
            }
        }
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
        List<DataItem> resultList = requestItems.stream().map(x -> DataItem.createReq(new byte[x.getCount()],
                        x.getVariableType() == EParamVariableType.BIT ? EDataVariableType.BIT : EDataVariableType.BYTE_WORD_DWORD))
                .collect(Collectors.toList());

        // 根据顺序分组算法得出分组结果，
        // 发送： 12=10(header)+2(parameter前),12(parameter后)
        // 接收： 14=12(header)+2(parameter),5(DataItem)，dataItem可能4或5，统一采用5
        List<S7ComGroup> s7ComGroups = S7SequentialGroupAlg.readRecombination(rawNumbers, this.pduLength - 14, 5, 12);
        try {
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
                S7Data req = S7Data.createReadRequest(newRequestItems);
                S7Data ack = this.readFromServer(req);
                ReadWriteDatum datum = (ReadWriteDatum) ack.getDatum();
                List<DataItem> dataItems = datum.getReturnItems().stream().map(DataItem.class::cast).collect(Collectors.toList());

                // 将获取的数据重装实际结果列表中
                for (int i = 0; i < comItemList.size(); i++) {
                    S7ComItem comItem = comItemList.get(i);
                    byte[] src = dataItems.get(i).getData();
                    byte[] des = resultList.get(comItem.getIndex()).getData();
                    System.arraycopy(src, 0, des, comItem.getSplitOffset(), src.length);
                }
            });
            return resultList;
        } finally {
            if (!this.persistence) {
                this.close();
            }
        }
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

        // 根据顺序分组算法得出分组结果
        // 发送：12=10(header)+2(parameter前),17=12(parameter后)+5(dataItem)，dataItem可能4或5，统一采用5
        // 接收：14=12(header)+2(parameter),1(DataItem)
        List<S7ComGroup> s7ComGroups = S7SequentialGroupAlg.writeRecombination(rawNumbers, this.pduLength - 12, 17);
        try {
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
                S7Data req = S7Data.createWriteRequest(newRequestItems, newDataItems);
                this.readFromServer(req);
            });
        } finally {
            if (!this.persistence) {
                this.close();
            }
        }
    }

    //endregion

    //region 读取NCK数据

    /**
     * 读取S7协议NCK数据
     *
     * @param requestItem 请求项
     * @return 数据项
     */
    public DataItem readS7NckData(RequestNckItem requestItem) {
        return this.readS7NckData(Collections.singletonList(requestItem)).get(0);
    }

    /**
     * 读取S7协议NCK数据，无法精确限制请求数量，因为响应的内容长度不定
     *
     * @param requestItems 请求项列表
     * @return 数据项
     */
    public List<DataItem> readS7NckData(List<RequestNckItem> requestItems) {
        try {
            S7Data s7Data = NckRequestBuilder.creatNckRequest(requestItems);
            S7Data ack = this.readFromServer(s7Data);
            ReadWriteDatum datum = (ReadWriteDatum) ack.getDatum();
            return datum.getReturnItems().stream().map(DataItem.class::cast).collect(Collectors.toList());
        } finally {
            if (!this.persistence) {
                this.close();
            }
        }
    }

    //endregion

    //region 上传下载

    /**
     * 下载文件，已在s200smart中测试成功
     *
     * @param mc7 Mc7File文件对象
     */
    public void downloadFile(Mc7File mc7) {
        try {
            // 开始下载
            EDestinationFileSystem destinationFileSystem = EDestinationFileSystem.P;
            S7Data reqStartDownload = S7Data.createStartDownload(mc7.getBlockType(), mc7.getBlockNumber(), destinationFileSystem,
                    mc7.getLoadMemoryLength(), mc7.getMC7CodeLength());
            this.readFromServer(reqStartDownload);

            // 下载中
            ByteReadBuff buff = new ByteReadBuff(mc7.getData());
            while (buff.getRemainSize() > 0) {
                boolean moreDataFollowing = buff.getRemainSize() > this.pduLength - 32;
                byte[] tmpData = buff.getBytes(Math.min(buff.getRemainSize(), this.pduLength - 32));
                S7Data reqDownload = S7Data.createDownload(mc7.getBlockType(), mc7.getBlockNumber(), destinationFileSystem, moreDataFollowing, tmpData);
                this.readFromServer(reqDownload);
            }

            // 下载结束
            S7Data reqEndDownload = S7Data.createEndDownload(mc7.getBlockType(), mc7.getBlockNumber(), destinationFileSystem);
            this.readFromServer(reqEndDownload);
        } finally {
            if (!this.persistence) {
                this.close();
            }
        }
    }

    /**
     * 从PLC上传文件内容到PC，已在s200smart中测试成功
     *
     * @param blockType   数据块类型
     * @param blockNumber 数据块编号
     * @return 字节数组数据
     */
    public byte[] uploadFile(EFileBlockType blockType, int blockNumber) {
        try {
            // 开始上传
            S7Data reqStartDownload = S7Data.createStartUpload(blockType, blockNumber, EDestinationFileSystem.A);
            S7Data ackStartDownload = this.readFromServer(reqStartDownload);
            StartUploadAckParameter startUploadAckParameter = (StartUploadAckParameter) ackStartDownload.getParameter();

            // 上传中
            ByteWriteBuff buff = new ByteWriteBuff(startUploadAckParameter.getBlockLength());
            UploadAckParameter uploadAckParameter = new UploadAckParameter();
            uploadAckParameter.setMoreDataFollowing(true);
            while (uploadAckParameter.isMoreDataFollowing()) {
                S7Data reqUpload = S7Data.createUpload(startUploadAckParameter.getId());
                S7Data ackUpload = this.readFromServer(reqUpload);
                uploadAckParameter = (UploadAckParameter) ackUpload.getParameter();
                if (uploadAckParameter.isErrorStatus()) {
                    throw new S7CommException("上传发生错误");
                }
                UpDownloadDatum datum = (UpDownloadDatum) ackUpload.getDatum();
                buff.putBytes(datum.getData());
            }

            // 上传结束
            S7Data reqEndUpload = S7Data.createEndUpload(startUploadAckParameter.getId());
            this.readFromServer(reqEndUpload);
            return buff.getData();
        } finally {
            if (!this.persistence) {
                this.close();
            }
        }
    }
    //endregion
}

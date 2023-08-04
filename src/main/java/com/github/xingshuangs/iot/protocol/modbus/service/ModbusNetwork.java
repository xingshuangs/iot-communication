package com.github.xingshuangs.iot.protocol.modbus.service;


import com.github.xingshuangs.iot.net.client.TcpClientBasic;
import com.github.xingshuangs.iot.protocol.modbus.model.MbPdu;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Consumer;

/**
 * plc的网络通信
 *
 * @author xingshuang
 */
@Slf4j
public class ModbusNetwork<T ,R> extends TcpClientBasic {

    /**
     * 从站编号
     */
    protected int unitId = 1;

    /**
     * 锁
     */
    protected final Object objLock = new Object();

    /**
     * 通信回调
     */
    protected Consumer<byte[]> comCallback;

    /**
     * 是否持久化，默认是持久化，对应长连接，true：长连接，false：短连接
     */
    protected boolean persistence = true;

    public void setComCallback(Consumer<byte[]> comCallback) {
        this.comCallback = comCallback;
    }

    public boolean isPersistence() {
        return persistence;
    }

    public void setPersistence(boolean persistence) {
        this.persistence = persistence;
    }

    public int getUnitId() {
        return unitId;
    }

    public ModbusNetwork() {
        super();
    }

    public ModbusNetwork(int unitId, String host, int port) {
        super(host, port);
        this.unitId = unitId;
    }

    //region 底层数据通信部分

    /**
     * 从服务器读取数据
     *
     * @param req modbus协议数据
     * @return modbus协议数据
     */
    protected R readFromServer(T req) {
        throw new UnsupportedOperationException();
    }

    /**
     * 校验请求数据和响应数据
     *
     * @param req 请求数据
     * @param ack 响应数据
     */
    protected void checkResult(T req, R ack) {
        throw new UnsupportedOperationException();
    }

    //endregion

    /**
     * 读取modbus数据
     *
     * @param reqPdu 请求对象
     * @return 响应结果
     */
    protected MbPdu readModbusData(MbPdu reqPdu) {
        throw new UnsupportedOperationException();
    }
}

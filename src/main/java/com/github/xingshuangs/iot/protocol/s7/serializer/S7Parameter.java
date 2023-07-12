package com.github.xingshuangs.iot.protocol.s7.serializer;


import com.github.xingshuangs.iot.protocol.common.enums.EDataType;
import lombok.Data;

/**
 * S7参数
 *
 * @author xingshuang
 */
@Data
public class S7Parameter {

    /**
     * 地址
     */
    private String address = "";

    /**
     * 数据类型
     */
    private EDataType dataType = EDataType.BYTE;

    /**
     * 个数
     * 除字节Byte和String类型外，其他类型对应的count必须为1
     */
    private Integer count = 1;

    /**
     * 对应的值
     */
    private Object value;

    public S7Parameter() {
    }

    public S7Parameter(String address, EDataType dataType) {
        this.address = address;
        this.dataType = dataType;
    }

    public S7Parameter(String address, EDataType dataType, Integer count) {
        this.address = address;
        this.dataType = dataType;
        this.count = count;
    }
}

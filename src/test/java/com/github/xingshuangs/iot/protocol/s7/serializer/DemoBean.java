package com.github.xingshuangs.iot.protocol.s7.serializer;


import com.github.xingshuangs.iot.protocol.common.enums.EDataType;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 测试对象
 *
 * @author xingshuang
 */
@Data
public class DemoBean {

    @S7Variable(address = "DB1.0.1", type = EDataType.BOOL)
    private Boolean bitData;

    @S7Variable(address = "DB1.4", type = EDataType.UINT16)
    private Integer uint16Data;

    @S7Variable(address = "DB1.6", type = EDataType.INT16)
    private Short int16Data;

    @S7Variable(address = "DB1.8", type = EDataType.UINT32)
    private Long uint32Data;

    @S7Variable(address = "DB1.12", type = EDataType.INT32)
    private Integer int32Data;

    @S7Variable(address = "DB1.16", type = EDataType.FLOAT32)
    private Float float32Data;

    @S7Variable(address = "DB1.20", type = EDataType.FLOAT64)
    private Double float64Data;

    @S7Variable(address = "DB1.28", type = EDataType.BYTE, count = 3)
    private byte[] byteData;

    // 注意：实际总长度为12，如果字符串后面还有其他字段，需要多预留2个字节数据
    @S7Variable(address = "DB1.31", type = EDataType.STRING, count = 10)
    private String stringData;

    @S7Variable(address = "DB1.43", type = EDataType.TIME)
    private Long timeData;

    @S7Variable(address = "DB1.47", type = EDataType.DATE)
    private LocalDate dateData;

    @S7Variable(address = "DB1.49", type = EDataType.TIME_OF_DAY)
    private LocalTime timeOfDayData;

    @S7Variable(address = "DB1.53", type = EDataType.DTL)
    private LocalDateTime dateTimeData;
}

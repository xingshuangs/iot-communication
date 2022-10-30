package com.github.xingshuangs.iot.protocol.common.serializer;


import com.github.xingshuangs.iot.protocol.common.enums.EDataType;
import lombok.Data;

import java.util.List;

/**
 * @author xingshuang
 */
@Data
public class ByteArrayListBean {

    @ByteArrayVariable(byteOffset = 0, bitOffset = 0, count = 8, type = EDataType.BOOL)
    List<Boolean> boolData;

    @ByteArrayVariable(byteOffset = 1, count = 4, type = EDataType.BYTE)
    List<Byte> byteData;

    @ByteArrayVariable(byteOffset = 1, count = 2, type = EDataType.UINT16)
    List<Integer> uint16Data;

    @ByteArrayVariable(byteOffset = 3, count = 2, type = EDataType.INT16)
    List<Short> int16Data;

    @ByteArrayVariable(byteOffset = 5, count = 2, type = EDataType.UINT32)
    List<Long> uint32Data;

    @ByteArrayVariable(byteOffset = 5, count = 2, type = EDataType.INT32)
    List<Integer> int32Data;

    @ByteArrayVariable(byteOffset = 13, count = 2, type = EDataType.FLOAT32)
    List<Float> float32Data;

    @ByteArrayVariable(byteOffset = 21, count = 2, type = EDataType.FLOAT64)
    List<Double> float64Data;

    @ByteArrayVariable(byteOffset = 37, count = 3, type = EDataType.STRING)
    String stringData;
}

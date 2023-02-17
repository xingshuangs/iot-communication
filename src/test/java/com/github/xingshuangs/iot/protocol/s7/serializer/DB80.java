package com.github.xingshuangs.iot.protocol.s7.serializer;

import com.github.xingshuangs.iot.protocol.common.enums.EDataType;
import lombok.Data;

@Data
public class DB80 {

    @S7Variable(type = EDataType.BOOL, address = "DB1.256")
    public Boolean saveEntrepot;

    //存料入库层号
    @S7Variable(type = EDataType.INT16,address = "DB1.258")
    public short saveEntrepotFloor;

    //存料入库板材长
    @S7Variable(type = EDataType.FLOAT32, address ="DB1.260")
    public Float saveEntrepotPlateLength;

    //存料入库板材宽
    @S7Variable(type = EDataType.FLOAT32, address ="DB1.264")
    public Float saveEntrepotPlateWidth;

    //存料入库板材厚
    @S7Variable(type = EDataType.FLOAT32, address ="DB1.268")
    public Float saveEntrepotPlateThickness;

    //存料入库板材数量
    @S7Variable(type = EDataType.INT16, address ="DB1.528")
    public short saveEntrepotPlateNumber;

    //是否回库请求
    @S7Variable(type = EDataType.BOOL, address ="DB1.530")
    public Boolean backEntrepot;

    //回库层号
    @S7Variable(type = EDataType.INT16, address ="DB1.532")
    public short backEntrepotFloor;

    //回库板材长
    @S7Variable(type = EDataType.FLOAT32, address ="DB1.534")
    public Float backEntrepotPlateLength;

    //回库板材宽
    @S7Variable(type = EDataType.FLOAT32, address ="DB1.538")
    public Float backEntrepotPlateWidth;

    //回库板材厚
    @S7Variable(type = EDataType.FLOAT32, address ="DB1.542")
    public Float backEntrepotPlateThickness;

    //回库板材数量
    @S7Variable(type = EDataType.INT16, address ="DB1.802")
    public short backEntrepotPlateNumber;

    //是否出库请求
    @S7Variable(type = EDataType.BOOL, address ="DB1.804")
    public Boolean outEntrepot;

    //出库层号
    @S7Variable(type = EDataType.INT16, address ="DB1.806")
    public Short outEntrepotFloor;

    //出库板材长
    @S7Variable(type = EDataType.FLOAT32, address ="DB1.808")
    public Float outEntrepotPlateLength;

    //出库板材宽
    @S7Variable(type = EDataType.FLOAT32, address ="DB1.812")
    public Float outEntrepotPlateWidth;

    //出库板材厚
    @S7Variable(type = EDataType.FLOAT32, address ="DB1.816")
    public float outEntrepotPlateThickness;

    //出库板材数量
    @S7Variable(type = EDataType.INT16, address ="DB1.1076")
    public short outEntrepotPlateNumber;

    //是否空托盘出库请求
    @S7Variable(type = EDataType.BOOL, address ="DB1.1078")
    public boolean emptyTrayOutEntrepot;

    //空托盘出库层号
    @S7Variable(type = EDataType.INT16, address ="DB1.1080")
    public short emptyTrayOutEntrepotFloor;

}

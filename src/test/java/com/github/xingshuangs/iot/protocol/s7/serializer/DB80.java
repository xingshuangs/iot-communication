package com.github.xingshuangs.iot.protocol.s7.serializer;

import com.github.xingshuangs.iot.protocol.common.enums.EDataType;
import lombok.Data;

@Data
public class DB80 {

    @S7Variable(type = EDataType.BOOL, address = "DB1.256")
    public Boolean saveEntrepot;

    @S7Variable(type = EDataType.INT16, address = "DB1.258")
    public short saveEntrepotFloor;

    @S7Variable(type = EDataType.FLOAT32, address = "DB1.260")
    public Float saveEntrepotPlateLength;

    @S7Variable(type = EDataType.FLOAT32, address = "DB1.264")
    public Float saveEntrepotPlateWidth;

    @S7Variable(type = EDataType.FLOAT32, address = "DB1.268")
    public Float saveEntrepotPlateThickness;

    @S7Variable(type = EDataType.INT16, address = "DB1.528")
    public short saveEntrepotPlateNumber;

    @S7Variable(type = EDataType.BOOL, address = "DB1.530")
    public Boolean backEntrepot;

    @S7Variable(type = EDataType.INT16, address = "DB1.532")
    public short backEntrepotFloor;

    @S7Variable(type = EDataType.FLOAT32, address = "DB1.534")
    public Float backEntrepotPlateLength;

    @S7Variable(type = EDataType.FLOAT32, address = "DB1.538")
    public Float backEntrepotPlateWidth;

    @S7Variable(type = EDataType.FLOAT32, address = "DB1.542")
    public Float backEntrepotPlateThickness;

    @S7Variable(type = EDataType.INT16, address = "DB1.802")
    public short backEntrepotPlateNumber;

    @S7Variable(type = EDataType.BOOL, address = "DB1.804")
    public Boolean outEntrepot;

    @S7Variable(type = EDataType.INT16, address = "DB1.806")
    public Short outEntrepotFloor;

    @S7Variable(type = EDataType.FLOAT32, address = "DB1.808")
    public Float outEntrepotPlateLength;

    @S7Variable(type = EDataType.FLOAT32, address = "DB1.812")
    public Float outEntrepotPlateWidth;

    @S7Variable(type = EDataType.FLOAT32, address = "DB1.816")
    public float outEntrepotPlateThickness;

    @S7Variable(type = EDataType.INT16, address = "DB1.1076")
    public short outEntrepotPlateNumber;

    @S7Variable(type = EDataType.BOOL, address = "DB1.1078")
    public boolean emptyTrayOutEntrepot;

    @S7Variable(type = EDataType.INT16, address = "DB1.1080")
    public short emptyTrayOutEntrepotFloor;

}

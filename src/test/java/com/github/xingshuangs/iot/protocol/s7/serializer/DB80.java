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

package com.github.xingshuangs.iot.protocol.s7.serializer;

import com.github.xingshuangs.iot.common.enums.EDataType;
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

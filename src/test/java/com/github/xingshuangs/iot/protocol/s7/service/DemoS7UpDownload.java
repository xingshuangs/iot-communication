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


import com.github.xingshuangs.iot.protocol.s7.enums.EFileBlockType;
import com.github.xingshuangs.iot.protocol.s7.enums.EPlcType;
import com.github.xingshuangs.iot.protocol.s7.model.Mc7File;

/**
 * @author xingshuang
 */
public class DemoS7UpDownload {

    public static void main(String[] args) {
        S7PLC s7PLC = new S7PLC(EPlcType.S200_SMART, "127.0.0.1");

        //********************************* upload ***************************************/
        // upload file data, PLC -> PC, success in 200Smart
        byte[] bytes = s7PLC.uploadFile(EFileBlockType.OB, 1);

        //******************************** download **************************************/
        // 1. create mc7 file
        Mc7File mc7 = Mc7File.fromBytes(bytes);
        // 2. plc stop, stop plc before download file
        s7PLC.plcStop();
        // 3. download file data, PC -> PLC, success in 200Smart
        s7PLC.downloadFile(mc7);
        // 4. insert new filename
        s7PLC.insert(mc7.getBlockType(), mc7.getBlockNumber());
        // 5. hot restart, restart plc after download and insert file
        s7PLC.hotRestart();

        s7PLC.close();
    }
}

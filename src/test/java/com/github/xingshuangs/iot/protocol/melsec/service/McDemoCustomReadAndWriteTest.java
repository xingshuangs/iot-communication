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

package com.github.xingshuangs.iot.protocol.melsec.service;


import com.github.xingshuangs.iot.protocol.melsec.enums.EMcSeries;
import com.github.xingshuangs.iot.protocol.melsec.model.McDeviceAddress;
import com.github.xingshuangs.iot.protocol.melsec.model.McDeviceContent;
import com.github.xingshuangs.iot.utils.HexUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xingshuang
 */
public class McDemoCustomReadAndWriteTest {

    public static void main(String[] args) {
        McPLC mcPLC = new McPLC(EMcSeries.Q_L, "127.0.0.1", 6000);

        // optional
        mcPLC.setComCallback(x -> System.out.printf("[%d] %s%n", x.length, HexUtil.toHexString(x)));

        // ------------------ read and write device batch in word ------------------------
        byte[] expect = new byte[]{0x34, 0x12, 0x02, 0x00};
        McDeviceContent reqContent = McDeviceContent.createBy("D110", 2, expect);
        mcPLC.writeDeviceBatchInWord(reqContent);
        McDeviceAddress address = McDeviceAddress.createBy("D110", 2);
        McDeviceContent ackContent = mcPLC.readDeviceBatchInWord(address);

        // ------------------ read and write device batch in bit --------------------------
        expect = new byte[]{0x11, 0x00, 0x01, 0x10};
        reqContent = McDeviceContent.createBy("M110", 8, expect);
        mcPLC.writeDeviceBatchInBit(reqContent);
        address = McDeviceAddress.createBy("M110", 8);
        ackContent = mcPLC.readDeviceBatchInBit(address);

        // ------------------ read and write device random in word ------------------------
        List<McDeviceContent> writeWord = new ArrayList<>();
        writeWord.add(McDeviceContent.createBy("D110", new byte[]{0x50, 0x05}));
        writeWord.add(McDeviceContent.createBy("D111", new byte[]{0x75, 0x05}));
        writeWord.add(McDeviceContent.createBy("M110", new byte[]{0x40, 0x05}));
        List<McDeviceContent> writeDWord = new ArrayList<>();
        writeDWord.add(McDeviceContent.createBy("D120", new byte[]{0x02, 0x12, 0x39, 0x04}));
        writeDWord.add(McDeviceContent.createBy("M130", new byte[]{0x75, 0x04, 0x25, 0x04}));
        mcPLC.writeDeviceRandomInWord(writeWord, writeDWord);

        List<McDeviceAddress> readWord = new ArrayList<>();
        readWord.add(McDeviceAddress.createBy("D110"));
        readWord.add(McDeviceAddress.createBy("D111"));
        readWord.add(McDeviceAddress.createBy("M110"));
        List<McDeviceAddress> readDWord = new ArrayList<>();
        readDWord.add(McDeviceAddress.createBy("D120"));
        readDWord.add(McDeviceAddress.createBy("M130"));
        List<McDeviceContent> mcDeviceContents = mcPLC.readDeviceRandomInWord(readWord, readDWord);

        // ------------------------ write device random in bit ---------------------------
        List<McDeviceContent> contents = new ArrayList<>();
        contents.add(McDeviceContent.createBy("M110", new byte[]{0x01}));
        contents.add(McDeviceContent.createBy("M112", new byte[]{0x01}));
        contents.add(McDeviceContent.createBy("M113", new byte[]{0x01}));
        mcPLC.writeDeviceRandomInBit(contents);

        // ---------------------- read and write device multi blocks (test failed, no reason) ---------------------
        List<McDeviceContent> wordContents = new ArrayList<>();
        wordContents.add(McDeviceContent.createBy("D110", 2, new byte[]{0x01, 0x02, 0x03, 0x04}));
        wordContents.add(McDeviceContent.createBy("D0", 1, new byte[]{0x08, 0x07}));
        List<McDeviceContent> bitContents = new ArrayList<>();
        bitContents.add(McDeviceContent.createBy("M110", 1, new byte[]{0x03, 0x04}));
        mcPLC.writeDeviceBatchMultiBlocks(wordContents, bitContents);

        List<McDeviceAddress> wordAddresses = new ArrayList<>();
        wordAddresses.add(McDeviceAddress.createBy("D110", 2));
        wordAddresses.add(McDeviceAddress.createBy("D114", 1));
        List<McDeviceAddress> bitAddresses = new ArrayList<>();
        bitAddresses.add(McDeviceAddress.createBy("M110", 1));
        mcDeviceContents = mcPLC.readDeviceBatchMultiBlocks(wordAddresses, bitAddresses);

        mcPLC.close();
    }
}

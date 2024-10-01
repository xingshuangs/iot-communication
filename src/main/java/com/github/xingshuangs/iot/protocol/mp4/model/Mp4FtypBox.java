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

package com.github.xingshuangs.iot.protocol.mp4.model;


import com.github.xingshuangs.iot.common.buff.ByteWriteBuff;
import com.github.xingshuangs.iot.protocol.mp4.enums.EMp4Type;

/**
 * ftyp(File Type) box is the first box of fmp4, which is used to determine the type, version and compatible protocol of the file.
 * There is only one for each file. When the client makes a request for the first time, the data with ftypbox is sent once,
 * and is usually combined with moov and sent to the client
 * ftyp(File Type) box是fmp4第一个box，用来确定文件的类型，版本及兼容的协议，每个文件有且只有一个，由于要支持流式传输，
 * 一般在客户端第一次发起请求时，发送一次带有ftypbox的数据，一般与moov合并发送给客户端
 *
 * @author xingshuang
 */
public class Mp4FtypBox extends Mp4Box {
    /**
     * 4-bytes, major brand，this is ASCII for isom.
     * 这里为isom的ASCII
     */
    private final String majorBrand;

    /**
     * 4-bytes,minor version，the lowest compatible version, in this case version 1.0.
     * 最低兼容版本，这里为1.0版本
     */
    private final int minorVersion;

    /**
     * 4-bytes, compatible brands，compatible versions, isom and avc1 ASCII
     * 兼容版本，isom和avc1的ASCII
     */
    private final String compatibleBrands;

    public Mp4FtypBox() {
        this.mp4Type = EMp4Type.FTYP;
        this.majorBrand = "isom";
        this.minorVersion = 1;
        this.compatibleBrands = "isomavc1";
    }

    @Override
    public int byteArrayLength() {
        return 24;
    }

    @Override
    public byte[] toByteArray() {
        int size = this.byteArrayLength();
        return ByteWriteBuff.newInstance(size)
                .putInteger(size)
                .putBytes(this.mp4Type.getByteArray())
                .putString(this.majorBrand)
                .putInteger(this.minorVersion)
                .putString(this.compatibleBrands)
                .getData();
    }
}

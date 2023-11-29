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

package com.github.xingshuangs.iot.protocol.s7.model;


import com.github.xingshuangs.iot.protocol.s7.enums.EFunctionCode;

import java.util.Collections;
import java.util.List;

/**
 * NCK请求构建器
 *
 * @author xingshuang
 */
public class NckRequestBuilder {

    private NckRequestBuilder() {
        // NOOP
    }

    public static S7Data creatNckRequest(RequestNckItem item) {
        return creatNckRequest(Collections.singletonList(item));
    }

    public static S7Data creatNckRequest(List<RequestNckItem> items) {
        S7Data s7Data = new S7Data();
        s7Data.setTpkt(new TPKT());
        s7Data.setCotp(COTPData.createDefault());
        s7Data.setHeader(Header.createDefault());
        s7Data.setParameter(ReadWriteParameter.createReqParameter(EFunctionCode.READ_VARIABLE, items));
        s7Data.selfCheck();
        return s7Data;
    }
}

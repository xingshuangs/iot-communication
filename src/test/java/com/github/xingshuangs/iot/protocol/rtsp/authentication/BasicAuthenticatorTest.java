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

package com.github.xingshuangs.iot.protocol.rtsp.authentication;

import com.github.xingshuangs.iot.utils.HexUtil;
import org.junit.Assert;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static org.junit.Assert.*;


public class BasicAuthenticatorTest {

    @Test
    public void createResponse() {
        String expect = "Basic YWRtaW46MTIzNDU2";
        UsernamePasswordCredential credential = new UsernamePasswordCredential("admin", "123456");
        BasicAuthenticator authenticator = new BasicAuthenticator(credential);
        String actual = authenticator.createResponse();
        assertEquals(expect, actual);
    }

    @Test
    public void base64() {
        Base64.Encoder encoder = Base64.getEncoder();
        Base64.Decoder decoder = Base64.getDecoder();
        String expect = "admin:123456";
        byte[] encode = encoder.encode(expect.getBytes(StandardCharsets.ISO_8859_1));
        byte[] decode = decoder.decode(encode);
        String actual = new String(decode, StandardCharsets.ISO_8859_1);
        assertEquals(expect, actual);

        // fmtp:96 profile-level-id=420029; packetization-mode=1; sprop-parameter-sets=Z00AH5Y1QKALdNwEBAQI,aO48gA==
        String src = "Z00AH5Y1QKALdNwEBAQI";
        byte[] sps = decoder.decode(src);
        String spsStr = HexUtil.toHexString(sps);
//        System.out.println(spsStr);

        src = "aO48gA==";
        byte[] pps = decoder.decode(src);
        String ppsStr = HexUtil.toHexString(pps);
//        System.out.println(ppsStr);
    }

    @Test
    public void base6DemoTest() {
        Base64.Encoder encoder = Base64.getEncoder();
        byte[] src = new byte[]{0x00, 0x02, 0x00};
        byte[] encode = encoder.encode(src);
        String actual = new String(encode, StandardCharsets.US_ASCII);
        Assert.assertEquals("AAIA", actual);
    }
}
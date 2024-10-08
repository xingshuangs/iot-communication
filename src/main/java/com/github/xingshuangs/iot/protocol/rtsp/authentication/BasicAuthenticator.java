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


import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Basic authenticator.
 * Basic64加密方式
 *
 * @author xingshuang
 */
public class BasicAuthenticator extends AbstractAuthenticator {

    public static final String BASIC_NAME = "Basic";

    private final Base64.Encoder encoder;

    public BasicAuthenticator(UsernamePasswordCredential credential) {
        this.credential = credential;
        this.name = BASIC_NAME;
        this.encoder = Base64.getEncoder();
    }

    public String createResponse() {
        String src = String.format("%s:%s", this.credential.getUsername(), this.credential.getPassword());
        return this.name + " " + this.encoder.encodeToString(src.getBytes(StandardCharsets.UTF_8));
    }
}

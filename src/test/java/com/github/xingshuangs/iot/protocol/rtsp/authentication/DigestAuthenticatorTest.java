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

import com.github.xingshuangs.iot.protocol.rtsp.enums.ERtspMethod;
import org.junit.Test;

import static org.junit.Assert.*;


public class DigestAuthenticatorTest {

    @Test
    public void createResponse() {
        String expect = "Digest username=\"Mufasa\", realm=\"testrealm@host.com\", nonce=\"dcd98b7102dd2f0e8b11d0f600bfb0c093\", uri=\"/dir/index.html\", response=\"6629fae49393a05397450978507c4ef1\", qop=auth, nc=00000001, cnonce=\"0a4f113b\"";
        UsernamePasswordCredential credential = new UsernamePasswordCredential("Mufasa", "Circle Of Life");
        DigestAuthenticator authenticator = new DigestAuthenticator(credential);
        authenticator.addServerInfoByString("Digest realm=\"testrealm@host.com\", qop=\"auth\", nonce=\"dcd98b7102dd2f0e8b11d0f600bfb0c093\"");
        authenticator.addClientInfo("/dir/index.html", "GET", "0a4f113b");
        String actual = authenticator.createResponse();
        assertEquals(expect, actual);
    }

    @Test
    public void createResponse1() {
        String expect = "Digest username=\"q\", realm=\"test\", nonce=\"N6yEOiDGTvOx9hwloHW7AQ==\", uri=\"/portal/applications\", response=\"bc3662d7309bdf68b5f6684647bd17e2\", qop=auth, nc=00000001, cnonce=\"04fefcb40dae7db4\"";
        UsernamePasswordCredential credential = new UsernamePasswordCredential("q", "q");
        DigestAuthenticator authenticator = new DigestAuthenticator(credential);
        authenticator.addServerInfo("test", "auth", "N6yEOiDGTvOx9hwloHW7AQ==", false);
        authenticator.addClientInfo("/portal/applications", "GET", "04fefcb40dae7db4");
        String actual = authenticator.createResponse();
        assertEquals(expect, actual);

    }

    @Test
    public void createResponse2() {
        String expect = "Digest username=\"admin\", realm=\"IP Camera(10789)\", nonce=\"6b9a455aec675b8db81a9ceb802e4eb8\", uri=\"rtsp://10.3.8.202:554\", response=\"876fcfac745c91bb7dd89faa660891ea\"";
        UsernamePasswordCredential credential = new UsernamePasswordCredential("admin", "12345");
        DigestAuthenticator authenticator = new DigestAuthenticator(credential);
        authenticator.addServerInfo("IP Camera(10789)", "", "6b9a455aec675b8db81a9ceb802e4eb8", false);
        authenticator.addClientInfo("rtsp://10.3.8.202:554", "DESCRIBE");
        String actual = authenticator.createResponse();
        assertEquals(expect, actual);
    }

    @Test
    public void createResponse3() {
        String expect = "Digest username=\"admin\", realm=\"IP Camera(10789)\", nonce=\"6b9a455aec675b8db81a9ceb802e4eb8\", uri=\"rtsp://10.3.8.202:554\", response=\"876fcfac745c91bb7dd89faa660891ea\"";
        UsernamePasswordCredential credential = new UsernamePasswordCredential("admin", "12345");
        DigestAuthenticator authenticator = new DigestAuthenticator(credential);
        authenticator.addServerInfoByString("Digest realm=\"IP Camera(10789)\", nonce=\"6b9a455aec675b8db81a9ceb802e4eb8\", stale=\"FALSE\"");
        authenticator.addClientInfo("rtsp://10.3.8.202:554", "DESCRIBE");
        String actual = authenticator.createResponse();
        assertEquals(expect, actual);
    }

    @Test
    public void createResponse4() {
        String expect = "Digest username=\"admin\", realm=\"IP Camera(G5366)\", nonce=\"00ea0e5bc0d4bee565d77d40502f9229\"," +
                " uri=\"rtsp://192.168.3.142:554/h264/ch1/main/av_stream\", response=\"f5592b62173d44ecc24738e0b0ed8dfd\"";
        UsernamePasswordCredential credential = new UsernamePasswordCredential("admin", "kilox1234");
        DigestAuthenticator authenticator = new DigestAuthenticator(credential);
        authenticator.addServerInfoByString("Digest realm=\"IP Camera(G5366)\", nonce=\"00ea0e5bc0d4bee565d77d40502f9229\", stale=\"FALSE\"");
        authenticator.addClientInfo("rtsp://192.168.3.142:554/h264/ch1/main/av_stream", "DESCRIBE");
        String actual = authenticator.createResponse();
        assertEquals(expect, actual);
    }

    @Test
    public void createResponse5() {
        UsernamePasswordCredential credential = new UsernamePasswordCredential("admin", "Jt70089999");
        DigestAuthenticator authenticator = new DigestAuthenticator(credential);
        authenticator.addServerInfoByString("Digest realm=\"d9ba57af23673f184a306b0b\", nonce=\"2545dad25\", algorithm=\"MD5\"");
        authenticator.addClientInfo("rtsp://192.168.110.238:554/Streaming/Channels/201", ERtspMethod.OPTIONS.getCode());
        String expect = "Digest username=\"admin\", realm=\"d9ba57af23673f184a306b0b\", nonce=\"2545dad25\", uri=\"rtsp://192.168.110.238:554/Streaming/Channels/201\", response=\"2a9abf52bd2f49183a7da1ca56695bd9\"";
        String actual = authenticator.createResponse();
        assertEquals(expect, actual);

        authenticator.setMethod(ERtspMethod.DESCRIBE.getCode());
        expect = "Digest username=\"admin\", realm=\"d9ba57af23673f184a306b0b\", nonce=\"2545dad25\", uri=\"rtsp://192.168.110.238:554/Streaming/Channels/201\", response=\"a444067f13c0211e984a79e48ea85efc\"";
        actual = authenticator.createResponse();
        assertEquals(expect, actual);

        authenticator.setMethod(ERtspMethod.SETUP.getCode());
        expect = "Digest username=\"admin\", realm=\"d9ba57af23673f184a306b0b\", nonce=\"2545dad25\", uri=\"rtsp://192.168.110.238:554/Streaming/Channels/201\", response=\"b1e31b066c0bea6fa262d4c6a115b66a\"";
        actual = authenticator.createResponse();
        assertEquals(expect, actual);

        authenticator.setMethod(ERtspMethod.PLAY.getCode());
        expect = "Digest username=\"admin\", realm=\"d9ba57af23673f184a306b0b\", nonce=\"2545dad25\", uri=\"rtsp://192.168.110.238:554/Streaming/Channels/201\", response=\"ff0e08c375f33ce90d66caffc1794f55\"";
        actual = authenticator.createResponse();
        assertEquals(expect, actual);
    }

    @Test
    public void createResponse6() {
        UsernamePasswordCredential credential = new UsernamePasswordCredential("admin", "kilox1234");
        DigestAuthenticator authenticator = new DigestAuthenticator(credential);
        authenticator.addServerInfoByString("Digest realm=\"IP Camera(G5366)\", nonce=\"2beea68cde935c964af4993f5535d15b\", stale=\"FALSE\"");
        authenticator.addClientInfo("rtsp://192.168.3.142:554/h264/ch1/main/av_stream", ERtspMethod.DESCRIBE.getCode());
        String expect = "Digest username=\"admin\", realm=\"IP Camera(G5366)\", nonce=\"2beea68cde935c964af4993f5535d15b\", uri=\"rtsp://192.168.3.142:554/h264/ch1/main/av_stream\", response=\"e89e52ddba1f60572564db63c35612b2\"";
        String actual = authenticator.createResponse();
        assertEquals(expect, actual);

        authenticator.setUri("rtsp://192.168.3.142:554/h264/ch1/main/av_stream/");
        authenticator.setMethod(ERtspMethod.SETUP.getCode());
        expect = "Digest username=\"admin\", realm=\"IP Camera(G5366)\", nonce=\"2beea68cde935c964af4993f5535d15b\", uri=\"rtsp://192.168.3.142:554/h264/ch1/main/av_stream/\", response=\"593dd32ac8986b39643c69ad41861d18\"";
        actual = authenticator.createResponse();
        assertEquals(expect, actual);

//        authenticator.setUri("rtsp://192.168.3.142:554/h264/ch1/main/av_stream/");
        authenticator.setMethod(ERtspMethod.PLAY.getCode());
        expect = "Digest username=\"admin\", realm=\"IP Camera(G5366)\", nonce=\"2beea68cde935c964af4993f5535d15b\", uri=\"rtsp://192.168.3.142:554/h264/ch1/main/av_stream/\", response=\"e7d858ae3f5d1c30bcfdfa1f022ff42e\"";
        actual = authenticator.createResponse();
        assertEquals(expect, actual);
    }
}
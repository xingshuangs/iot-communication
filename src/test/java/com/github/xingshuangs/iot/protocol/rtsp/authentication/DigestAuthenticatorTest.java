package com.github.xingshuangs.iot.protocol.rtsp.authentication;

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
}
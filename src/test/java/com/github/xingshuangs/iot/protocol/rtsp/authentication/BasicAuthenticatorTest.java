package com.github.xingshuangs.iot.protocol.rtsp.authentication;

import org.junit.Test;

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
}
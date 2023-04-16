package com.github.xingshuangs.iot.protocol.rtsp.model;

import com.github.xingshuangs.iot.protocol.rtsp.authentication.DigestAuthenticator;
import com.github.xingshuangs.iot.protocol.rtsp.authentication.UsernamePasswordCredential;
import com.github.xingshuangs.iot.protocol.rtsp.enums.ERtspAcceptContent;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.net.URI;
import java.util.Collections;

import static com.github.xingshuangs.iot.protocol.rtsp.constant.RtspCommonKey.CRLF;
import static org.junit.Assert.assertEquals;

public class RtspMessageRequestTest {

    private DigestAuthenticator authenticator;

    @Before
    public void init() {
        UsernamePasswordCredential credential = new UsernamePasswordCredential("admin", "123456");
        authenticator = new DigestAuthenticator(credential);
        authenticator.addServerInfoByString("Digest realm=\"IP Camera(10789)\", nonce=\"6b9a455aec675b8db81a9ceb802e4eb8\", stale=\"FALSE\"");
        authenticator.addClientInfo("rtsp://10.3.8.202:554", "DESCRIBE");
    }


    @Test
    public void uriTest() {
        URI uriTmp = URI.create("*");
        assertEquals("*", uriTmp.toString());
        URI uri = URI.create("rtsp://10.3.8.202:554");
        assertEquals("rtsp://10.3.8.202:554", uri.toString());
        assertEquals("\r\n", CRLF);
    }

    @Test
    public void rtspOptionRequestTest() {
        String expect = "OPTIONS rtsp://10.3.8.202:554 RTSP/1.0\r\n" +
                "CSeq: 0\r\n" +
                "User-Agent: IOT-COMMUNICATION\r\n" +
                "\r\n";
        URI uri = URI.create("rtsp://10.3.8.202:554");
        RtspOptionRequest request = new RtspOptionRequest(uri);
        request.setCSeq(0);
        String actual = request.toObjectString();
        assertEquals(expect, actual);
    }

    @Test
    public void rtspDescribeRequestTest() {
        String expect = "DESCRIBE rtsp://10.3.8.202:554 RTSP/1.0\r\n" +
                "CSeq: 0\r\n" +
                "User-Agent: IOT-COMMUNICATION\r\n" +
                "Accept: application/sdp\r\n" +
                "\r\n";
        URI uri = URI.create("rtsp://10.3.8.202:554");
        RtspDescribeRequest request = new RtspDescribeRequest(uri, Collections.singletonList(ERtspAcceptContent.SDP));
        request.setCSeq(0);
        String actual = request.toObjectString();
        assertEquals(expect, actual);

        expect = "DESCRIBE rtsp://10.3.8.202:554 RTSP/1.0\r\n" +
                "CSeq: 1\r\n" +
                "Authorization: Digest username=\"admin\", realm=\"IP Camera(10789)\", nonce=\"6b9a455aec675b8db81a9ceb802e4eb8\", uri=\"rtsp://10.3.8.202:554\", response=\"56d55b92e8b41aa6cc1a68a5c2e2de15\"\r\n" +
                "User-Agent: IOT-COMMUNICATION\r\n" +
                "Accept: application/sdp\r\n" +
                "\r\n";
        request = new RtspDescribeRequest(uri, Collections.singletonList(ERtspAcceptContent.SDP), this.authenticator);
        request.setCSeq(1);
        actual = request.toObjectString();
        assertEquals(expect, actual);
    }

    @Test
    public void rtspSetupRequestTest() {

        String expect = "SETUP rtsp://10.3.8.202:554/trackID=1 RTSP/1.0\r\n" +
                "CSeq: 0\r\n" +
                "Authorization: Digest username=\"admin\", realm=\"IP Camera(10789)\", nonce=\"6b9a455aec675b8db81a9ceb802e4eb8\", uri=\"rtsp://10.3.8.202:554\", response=\"56d55b92e8b41aa6cc1a68a5c2e2de15\"\r\n" +
                "User-Agent: IOT-COMMUNICATION\r\n" +
                "Transport: RTP/AVP;unicast;client_port=57844-57845\r\n" +
                "\r\n";

        URI uri = URI.create("rtsp://10.3.8.202:554/trackID=1");
        RtspTransport transport = RtspTransport.extractFrom("RTP/AVP;unicast;client_port=57844-57845");
        RtspSetupRequest request = new RtspSetupRequest(uri, transport, this.authenticator);
        request.setCSeq(0);
        String actual = request.toObjectString();
        assertEquals(expect, actual);
    }

    @Test
    public void rtspPlayRequestTest() {
        String expect = "PLAY rtsp://10.3.8.202:554/ RTSP/1.0\r\n" +
                "CSeq: 0\r\n" +
                "Authorization: Digest username=\"admin\", realm=\"IP Camera(10789)\", nonce=\"6b9a455aec675b8db81a9ceb802e4eb8\", uri=\"rtsp://10.3.8.202:554\", response=\"56d55b92e8b41aa6cc1a68a5c2e2de15\"\r\n" +
                "User-Agent: IOT-COMMUNICATION\r\n" +
                "Session: 1273222592\r\n" +
                "Range: npt=0.000-\r\n" +
                "\r\n";
        URI uri = URI.create("rtsp://10.3.8.202:554/");
        String range = "npt=0.000-";
        RtspPlayRequest request = new RtspPlayRequest(uri, 1273222592, range, this.authenticator);
        request.setCSeq(0);
        String actual = request.toObjectString();
        assertEquals(expect, actual);
    }

    @Test
    public void rtspPauseRequestTest() {
        String expect = "PAUSE rtsp://10.3.8.202:554/ RTSP/1.0\r\n" +
                "CSeq: 0\r\n" +
                "Authorization: Digest username=\"admin\", realm=\"IP Camera(10789)\", nonce=\"6b9a455aec675b8db81a9ceb802e4eb8\", uri=\"rtsp://10.3.8.202:554\", response=\"56d55b92e8b41aa6cc1a68a5c2e2de15\"\r\n" +
                "User-Agent: IOT-COMMUNICATION\r\n" +
                "Session: 1273222592\r\n" +
                "\r\n";
        URI uri = URI.create("rtsp://10.3.8.202:554/");
        RtspPauseRequest request = new RtspPauseRequest(uri, 1273222592, this.authenticator);
        request.setCSeq(0);
        String actual = request.toObjectString();
        assertEquals(expect, actual);
    }

    @Test
    public void rtspGetParameterRequestTest() {

        String expect = "GET_PARAMETER rtsp://10.3.8.202:554/ RTSP/1.0\r\n" +
                "CSeq: 0\r\n" +
                "Authorization: Digest username=\"admin\", realm=\"IP Camera(10789)\", nonce=\"6b9a455aec675b8db81a9ceb802e4eb8\", uri=\"rtsp://10.3.8.202:554\", response=\"56d55b92e8b41aa6cc1a68a5c2e2de15\"\r\n" +
                "User-Agent: IOT-COMMUNICATION\r\n" +
                "Session: 1273222592\r\n" +
                "\r\n";
        URI uri = URI.create("rtsp://10.3.8.202:554/");
        RtspGetParameterRequest request = new RtspGetParameterRequest(uri, 1273222592, this.authenticator);
        request.setCSeq(0);
        String actual = request.toObjectString();
        assertEquals(expect, actual);

        expect = "GET_PARAMETER rtsp://10.3.8.202:554/ RTSP/1.0\r\n" +
                "CSeq: 1\r\n" +
                "Authorization: Digest username=\"admin\", realm=\"IP Camera(10789)\", nonce=\"6b9a455aec675b8db81a9ceb802e4eb8\", uri=\"rtsp://10.3.8.202:554\", response=\"56d55b92e8b41aa6cc1a68a5c2e2de15\"\r\n" +
                "User-Agent: IOT-COMMUNICATION\r\n" +
                "Session: 1273222592\r\n" +
                "Content-Type: text/parameters\r\n" +
                "Content-Length: 26\r\n" +
                "\r\n" +
                "packets_received\r\n" +
                "jitter\r\n";
        uri = URI.create("rtsp://10.3.8.202:554/");
        request = new RtspGetParameterRequest(uri, 1273222592, this.authenticator);
        request.setCSeq(1);
        request.addParameter("packets_received");
        request.addParameter("jitter");
        actual = request.toObjectString();
        assertEquals(expect, actual);
    }

    @Test
    public void rtspSetParameterRequestTest() {

        String expect = "SET_PARAMETER rtsp://10.3.8.202:554/ RTSP/1.0\r\n" +
                "CSeq: 0\r\n" +
                "Authorization: Digest username=\"admin\", realm=\"IP Camera(10789)\", nonce=\"6b9a455aec675b8db81a9ceb802e4eb8\", uri=\"rtsp://10.3.8.202:554\", response=\"56d55b92e8b41aa6cc1a68a5c2e2de15\"\r\n" +
                "User-Agent: IOT-COMMUNICATION\r\n" +
                "Session: 1273222592\r\n" +
                "\r\n";
        URI uri = URI.create("rtsp://10.3.8.202:554/");
        RtspSetParameterRequest request = new RtspSetParameterRequest(uri, 1273222592, this.authenticator);
        request.setCSeq(0);
        String actual = request.toObjectString();
        assertEquals(expect, actual);

        expect = "SET_PARAMETER rtsp://10.3.8.202:554/ RTSP/1.0\r\n" +
                "CSeq: 1\r\n" +
                "Authorization: Digest username=\"admin\", realm=\"IP Camera(10789)\", nonce=\"6b9a455aec675b8db81a9ceb802e4eb8\", uri=\"rtsp://10.3.8.202:554\", response=\"56d55b92e8b41aa6cc1a68a5c2e2de15\"\r\n" +
                "User-Agent: IOT-COMMUNICATION\r\n" +
                "Session: 1273222592\r\n" +
                "Content-Type: text/parameters\r\n" +
                "Content-Length: 20\r\n" +
                "\r\n" +
                "barparam: barstuff\r\n";

        uri = URI.create("rtsp://10.3.8.202:554/");
        request = new RtspSetParameterRequest(uri, 1273222592, this.authenticator);
        request.setCSeq(1);
        request.addParameter("barparam", "barstuff");
        actual = request.toObjectString();
        assertEquals(expect, actual);
    }

    @Test
    public void rtspTeardownRequestTest() {
        String expect = "TEARDOWN rtsp://10.3.8.202:554/ RTSP/1.0\r\n" +
                "CSeq: 0\r\n" +
                "Authorization: Digest username=\"admin\", realm=\"IP Camera(10789)\", nonce=\"6b9a455aec675b8db81a9ceb802e4eb8\", uri=\"rtsp://10.3.8.202:554\", response=\"56d55b92e8b41aa6cc1a68a5c2e2de15\"\r\n" +
                "User-Agent: IOT-COMMUNICATION\r\n" +
                "Session: 1273222592\r\n" +
                "\r\n";
        URI uri = URI.create("rtsp://10.3.8.202:554/");
        RtspTeardownRequest request = new RtspTeardownRequest(uri, 1273222592, this.authenticator);
        request.setCSeq(0);
        String actual = request.toObjectString();
        assertEquals(expect, actual);
    }

}
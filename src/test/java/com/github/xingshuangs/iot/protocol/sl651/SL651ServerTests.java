/*
 * MIT License
 *
 * Copyright (c) 2021-2099 Oscura (xingshuang) <xingshuang_cool@163.com>, 王清攀
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
package com.github.xingshuangs.iot.protocol.sl651;

import com.github.xingshuangs.iot.protocol.sl651.event.ISl651ConnectStatusListener;
import com.github.xingshuangs.iot.protocol.sl651.event.ISl651MessageListener;
import com.github.xingshuangs.iot.protocol.sl651.model.SL651BodyResponse;
import com.github.xingshuangs.iot.protocol.sl651.model.SL651Response;
import com.github.xingshuangs.iot.protocol.sl651.service.SL651Server;
import com.github.xingshuangs.iot.utils.HexUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class SL651ServerTests {

    // 测试心跳报文：7E7E00002015900400002F000802CF4826041614352103F3BA
    // 测试定时报文：7E7E000020159018000032004002775E260416143500F1F1002015901848F0F0260416143538121367FF801A001887FF850831FF8118000000FF87120802FF882000001826FF89304EAF10100000036CC1
    public static void main(String[] args) {
        SL651Server sl651Server = new SL651Server("1234");
        sl651Server.start(7151);

        sl651Server.setConnectStatusListener(
                new ISl651ConnectStatusListener() {
                    @Override
                    public void online(String addr, int port, int localPort) {
                        log.info("[{}:{}]上线", addr, port);
                    }

                    @Override
                    public void offline(String addr, int port, int localPort) {
                        log.info("[{}:{}]下线", addr, port);
                    }
                }
        );

        sl651Server.setMessageListener(new ISl651MessageListener() {
            @Override
            public void onMessage(byte[] bytes, SL651Response response, List<SL651BodyResponse> bodyResponses) {
                log.info("收到报文：{}", response);
                if (bodyResponses == null) return;
                for (SL651BodyResponse bodyResponse : bodyResponses) {
                    log.info("报文时间：{},报文标识：{},报文数据:{}", HexUtil.toHexString(bodyResponse.getObserveTime()),
                            HexUtil.toHexString(bodyResponse.getDataIdentifier())
                            , bodyResponse.getBodyElements());
                }

            }
        });


        while (true) {
        }

    }

}

package com.github.xingshuangs.iot.net.netty.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

/**
 * @author xingshuang
 */
@Slf4j
@ChannelHandler.Sharable
public class ClientHeartbeatHandler extends ChannelInboundHandlerAdapter {

    public ClientHeartbeatHandler() {
        // NOOP
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent) evt;
            if (e.state() == IdleState.WRITER_IDLE) {
//                Channel channel = ctx.channel();
//                if (channel != null) {
//                    log.debug("WRITER_IDLE, send Heartbeat...");
//                    channel.writeAndFlush("");
//                }
            } else if (e.state() == IdleState.READER_IDLE) {
                // NOOP
            }
        }
        super.userEventTriggered(ctx, evt);
    }
}

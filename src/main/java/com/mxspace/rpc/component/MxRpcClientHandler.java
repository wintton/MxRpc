package com.mxspace.rpc.component;

import com.mxspace.rpc.service.MxRpcClientService;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

/**
 * 客户端处理对象
 */
@Slf4j
public class MxRpcClientHandler extends ChannelInboundHandlerAdapter {

    public MxRpcClientHandler(MxRpcClientService mxRpcClientService) {
        this.mxRpcClientService = mxRpcClientService;
    }

    private MxRpcClientService mxRpcClientService;

    /**
     * 读取消息
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        mxRpcClientService.handleMsg(ctx, msg.toString());
    }

    /**
     * 断开连接
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        mxRpcClientService.loginOut(ctx);
    }

    /**
     * 建立连接
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        mxRpcClientService.loginIn(ctx);
    }

    /**
     * 发送异常
     *
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            switch (event.state()) {
                case WRITER_IDLE:
                case ALL_IDLE:
                    mxRpcClientService.sendHeartBeat(ctx);
                    break;
                case READER_IDLE:
                    break;
            }
        }
    }
}

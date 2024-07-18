package com.mxspace.rpc.component;

import com.mxspace.rpc.service.MxRpcClientManService;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

/**
 * 服务端消息处理类
 */
@Slf4j
public class MxRpcServerHandler extends ChannelInboundHandlerAdapter {

    public MxRpcServerHandler(MxRpcClientManService mxRpcClientManService){
        this.mxRpcClientManService = mxRpcClientManService;
    }

    private MxRpcClientManService mxRpcClientManService;

    /**
     * 读取消息
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        mxRpcClientManService.handleMsg(ctx,msg.toString());
    }

    /**
     * 断开连接
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        mxRpcClientManService.loginOut(ctx);
    }

    /**
     * 建立连接
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    /**
     * 发送异常
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
        if (!mxRpcClientManService.checkLogin(ctx)){
            ctx.close();
        }
    }

}

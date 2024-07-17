package com.mxspace.rpc.component;


import com.mxspace.rpc.data.MxRpcClientConfig;
import com.mxspace.rpc.service.MxRpcClientManService;
import com.mxspace.rpc.service.MxRpcClientService;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * 客户端线程
 */
@Slf4j
public class MxRpcClientThread extends Thread {


    EventLoopGroup eventLoopGroup;

    ChannelFuture future;

    boolean isStop = false;

    /**
     * 客户端配置信息
     */
    private MxRpcClientConfig mxRpcClientConfig;

    /**
     * 客户端服务
     */
    private MxRpcClientService mxRpcClientService;

    public MxRpcClientThread(MxRpcClientConfig mxRpcClientConfig, MxRpcClientService mxRpcClientService){
        this.mxRpcClientConfig = mxRpcClientConfig;
        this.mxRpcClientService = mxRpcClientService;
    }

    @Override
    public void run() {

        eventLoopGroup = new NioEventLoopGroup();

        try {

            Bootstrap bootstrap = new Bootstrap();

            bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class)

                    .handler(new ChannelInitializer() {

                        @Override
                        protected void initChannel(Channel channel) throws Exception {

                            ChannelPipeline pipeline = channel.pipeline();

                            pipeline.addLast(new IdleStateHandler(0, 0, 60));

                            pipeline.addLast(new StringDecoder());

                            pipeline.addLast(new StringEncoder());

                            pipeline.addLast(new MxRpcClientHandler(mxRpcClientService));
                        }

                    });

            ChannelFuture future = bootstrap.connect(mxRpcClientConfig.getServerHost(),mxRpcClientConfig.getServerPort()).sync();

            log.info("RPC 客户端连接成功:{}:{}" ,mxRpcClientConfig.getServerHost(),mxRpcClientConfig.getServerPort());

            future.channel().closeFuture().sync();

        } catch (Exception e){
            log.error("MxRpc客户端异常：",e);
        } finally {
            eventLoopGroup.shutdownGracefully();

            if (!isStop){
                //30秒后重新连接
                try {
                    Thread.sleep(30 * 1000l);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
                this.run();
            }
        }
    }

    public void stopRun(){
        isStop = true;
        if (future != null && !future.isDone()) {
            future.cancel(true);
        }
        if (eventLoopGroup != null)eventLoopGroup.shutdownGracefully();
        log.error("MxRpc客户端关闭：");
    }
}

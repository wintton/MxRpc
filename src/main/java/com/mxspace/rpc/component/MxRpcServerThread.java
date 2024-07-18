package com.mxspace.rpc.component;

import com.mxspace.rpc.enums.ProviderVisitStrategyEnums;
import com.mxspace.rpc.service.MxRpcClientManService;
import com.mxspace.rpc.util.FastJsonUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * 服务端线程
 */
@Slf4j
public class MxRpcServerThread extends Thread {



    /**
     * 端口号
     */
    private int port;

    EventLoopGroup boss;

    EventLoopGroup worker;

    ChannelFuture future;

    MxRpcClientManService mxRpcClientManService;

    ProviderVisitStrategyEnums visitStrategy;

    public MxRpcServerThread(int port,MxRpcClientManService mxRpcClientManService){
        this.port = port;
        this.mxRpcClientManService = mxRpcClientManService;
    }


    @Override
    public void run() {

        boss = new NioEventLoopGroup();

        worker = new NioEventLoopGroup();

        try {

            ServerBootstrap bootstrap = new ServerBootstrap();

            mxRpcClientManService.setVisitStrategy(visitStrategy);

            bootstrap.group(boss, worker)

                    .childHandler(new ChannelInitializer() {

                        @Override
                        protected void initChannel(Channel channel) throws Exception {

                            ChannelPipeline pipeline = channel.pipeline();

                            ByteBuf delimiter = Unpooled.copiedBuffer(FastJsonUtil.END_CODE.getBytes());

                            pipeline.addLast(new DelimiterBasedFrameDecoder(2048,delimiter));

                            pipeline.addLast(new IdleStateHandler(0, 0, 60));

                            pipeline.addLast(new StringDecoder());

                            pipeline.addLast(new StringEncoder());

                            pipeline.addLast(new MxRpcServerHandler(mxRpcClientManService));
                        }

                    })

                    .channel(NioServerSocketChannel.class);

            ChannelFuture future = bootstrap.bind(port).sync();

            log.info("RPC 服务器启动, 监听端口:" + port);

            future.channel().closeFuture().sync();

        }catch (Exception e){

            log.error("MxRpc服务端开启异常：",e);

            boss.shutdownGracefully();

            worker.shutdownGracefully();

        }
    }

    public void stopRun(){
        if (future != null && !future.isDone()) {
            future.cancel(true);
        }
        worker.shutdownGracefully();
        boss.shutdownGracefully();

        log.error("MxRpc服务端关闭：");
    }


    public void setVisitStrategy(ProviderVisitStrategyEnums visitStrategy) {
        this.visitStrategy = visitStrategy;
    }
}

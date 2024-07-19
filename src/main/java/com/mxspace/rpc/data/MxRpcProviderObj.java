package com.mxspace.rpc.data;

import com.mxspace.rpc.util.FastJsonUtil;
import com.mxspace.rpc.util.MxRpcRequest;
import com.mxspace.rpc.util.MxRpcResponse;
import io.netty.channel.ChannelHandlerContext;
import lombok.Data;

/**
 * 服务提供者
 */
@Data
public class MxRpcProviderObj {

    /**
     * 连接对象Id
     */
    private String ctxId;

    /**
     * 客户端ID
     */
    private String clientId;

    /**
     * 权重
     */
    private Integer weight = 1;

    /**
     * 已发送次数
     */
    private Integer sendNumber;

    /**
     * 服务名称
     */
    private String serviceName;

    /**
     * 版本号
     */
    private String serviceVersion;

    /**
     * 连接对象
     */
    private ChannelHandlerContext context;

    /**
     * 发送请求
     * @param rpcRequest
     */
    public boolean sendRequest(MxRpcRequest rpcRequest){

        if (context == null){
            return false;
        }

        context.writeAndFlush(FastJsonUtil.toJSONString(rpcRequest));

        return true;
    }

    /**
     * 发送回复消息
     * @param mxRpcResponse
     * @return
     */
    public boolean sendResponse(MxRpcResponse mxRpcResponse){

        if (context == null){
            return false;
        }

        context.writeAndFlush(FastJsonUtil.toJSONString(mxRpcResponse));

        return true;
    }
}

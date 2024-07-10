package com.mxspace.rpc.data;

import com.mxspace.rpc.util.MxRpcRequest;
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
     * 发送请求
     * @param rpcRequest
     */
    public boolean sendRequest(MxRpcRequest rpcRequest){
        return false;
    }
}

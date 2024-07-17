package com.mxspace.rpc.service;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.mxspace.rpc.data.MxRpcClientConfig;
import com.mxspace.rpc.data.MxRpcServerConfig;
import com.mxspace.rpc.util.*;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * RPC客户端 服务处理类
 */
@Slf4j
@Service
public class MxRpcClientService {

    @Resource
    private MxRpcClientConfig mxRpcClientConfig;

    /**
     * 任务回调机制
     */
    private Map<String, FutureRpcData> taskMap = new ConcurrentHashMap<>();

    /**
     * 登录状态
     */
    private boolean loginState = false;

    /**
     * 发送登录请求
     * @param ctx
     */
    public void loginIn(ChannelHandlerContext ctx) {
        MxRpcLogin mxRpcLogin = new MxRpcLogin();
        mxRpcLogin.setClientId(mxRpcClientConfig.getClientId());
        mxRpcLogin.setServiceName(mxRpcClientConfig.getServiceName());
        mxRpcLogin.setLoginPassword(mxRpcClientConfig.getPassword());
        mxRpcLogin.setWeight(mxRpcClientConfig.getWeight());
        ctx.writeAndFlush(toJSonString(mxRpcLogin));
    }

    /**
     * 登录退出
     * @param ctx
     */
    public void loginOut(ChannelHandlerContext ctx) {
        this.loginState = false;
    }

    /**
     * 处理消息
     * @param ctx
     * @param msg
     */
    public void handleMsg(ChannelHandlerContext ctx, String msg) {
        log.info("客户端收到消息：{}",msg);
        Object msgData = FastJsonUtil.parse(msg);
        if (msgData instanceof MxRpcResponse){
            MxRpcResponse mxRpcResponse = (MxRpcResponse) msgData;
            String requestId = mxRpcResponse.getRequestId();
            BaseResult baseResult = mxRpcResponse.getResult();
            if (requestId.equalsIgnoreCase(mxRpcClientConfig.getClientId())){
                //登录请求
                loginState = baseResult.getCode() == 200;
            } else {
                FutureRpcData futureRpcData = taskMap.get(requestId);
                if (futureRpcData != null){
                    futureRpcData.setResult(baseResult.getData());
                }
            }
        }
    }

    /**
     * 发送验证码
     */
    public void sendHeartBeat(ChannelHandlerContext ctx) {
        if (!this.loginState){
            return;
        }
        MxRpcHeartBeat mxRpcHeartBeat = new MxRpcHeartBeat();
        mxRpcHeartBeat.setClientId(mxRpcClientConfig.getClientId());
        mxRpcHeartBeat.setServiceName(mxRpcClientConfig.getServiceName());
        ctx.writeAndFlush(toJSonString(mxRpcHeartBeat));
    }

    public String toJSonString(Object object){
        return FastJsonUtil.toJSONString(object);
    }

}

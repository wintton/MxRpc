package com.mxspace.rpc.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mxspace.rpc.data.MxRpcProviderObj;
import com.mxspace.rpc.data.MxRpcServerConfig;
import com.mxspace.rpc.data.MxRpcServiceObj;
import com.mxspace.rpc.util.FutureRpcData;
import com.mxspace.rpc.util.MxRpcLogin;
import com.mxspace.rpc.util.MxRpcRequest;
import com.mxspace.rpc.util.MxRpcResponse;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * RPC客户端管理类
 */
@Slf4j
@Service
public class MxRpcClientManService {

    @Resource
    private MxRpcServerConfig mxRpcServerConfig;

    /**
     * 连接对象
     */
    private Map<ChannelHandlerContext, String> clientLinkMap = new ConcurrentHashMap<>();

    /**
     * 服务名称Map 对象
     */
    private Map<String, MxRpcServiceObj> rpcServiceObjMap = new ConcurrentHashMap<>();

    /**
     * 服务提供者对象-验证通过后的对象
     */
    private Map<String, MxRpcProviderObj> rpcProviderObjMap = new ConcurrentHashMap<>();

    /**
     * 任务ID-
     */
    private Map<String, FutureRpcData> taskMap = new ConcurrentHashMap<>();

    public boolean checkLogin(ChannelHandlerContext context){
        String clientId = clientLinkMap.get(context);
        return clientId == null;
    }

    public void handleMsg(ChannelHandlerContext context,String dataJson){
        try {
            boolean isLogin = checkLogin(context);
            Object handleObject = JSONObject.parse(dataJson);
            if (handleObject instanceof MxRpcLogin){
                MxRpcLogin mxRpcLogin = (MxRpcLogin)handleObject;
                if (mxRpcServerConfig.getPassword().equals(mxRpcLogin.getLoginPassword())){
                    MxRpcProviderObj mxRpcProviderObj = new MxRpcProviderObj();
                } else {

                }
            }
            if (!isLogin){
                return;
            }
            if (handleObject instanceof MxRpcRequest){
                //请求对象
            } else if (handleObject instanceof MxRpcResponse){
                //回复对象
            }
        } catch (Exception e){
            log.error("数据处理异常:",e);
        }
    }

}

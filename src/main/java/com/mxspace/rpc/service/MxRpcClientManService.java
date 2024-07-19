package com.mxspace.rpc.service;

import com.mxspace.rpc.data.MxRpcProviderObj;
import com.mxspace.rpc.data.MxRpcServerConfig;
import com.mxspace.rpc.data.MxRpcServiceObj;
import com.mxspace.rpc.enums.ProviderVisitStrategyEnums;
import com.mxspace.rpc.util.*;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;
import java.util.UUID;
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
     * 访问策略
     */
    private ProviderVisitStrategyEnums visitStrategy = ProviderVisitStrategyEnums.NEXT;

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
     * 任务ID-来源目标对象
     */
    private Map<String, MxRpcHandleObj> taskMap = new ConcurrentHashMap<>();

    /**
     * 检查是否登录
     * @param context
     * @return
     */
    public boolean checkLogin(ChannelHandlerContext context){
        String clientId = clientLinkMap.get(context);
        return clientId != null;
    }

    /**
     * 获取对象ID
     * @param context
     * @return
     */
    public String getClientId(ChannelHandlerContext context){
        return clientLinkMap.get(context);
    }

    /**
     * 登录线
     * @param context
     */
    public void loginOut(ChannelHandlerContext context){
        String ctxId = clientLinkMap.get(context);
        if (ctxId == null){
            return;
        }
        MxRpcProviderObj mxRpcProviderObj = rpcProviderObjMap.get(ctxId);
        if (mxRpcProviderObj == null){
            return;
        }
        MxRpcServiceObj mxRpcServiceObj = rpcServiceObjMap.get(mxRpcProviderObj.getServiceName());
        if (mxRpcServiceObj == null){
            return;
        }
        mxRpcServiceObj.removeProvider(mxRpcProviderObj);
    }

    /**
     * 添加提供者
     * @param context
     * @param mxRpcLogin
     */
    public void loginIn(ChannelHandlerContext context, MxRpcLogin mxRpcLogin){
        String ctxId = UUID.randomUUID().toString();
        MxRpcProviderObj mxRpcProviderObj = new MxRpcProviderObj();
        mxRpcProviderObj.setClientId(mxRpcLogin.getClientId());
        mxRpcProviderObj.setCtxId(ctxId);
        mxRpcProviderObj.setWeight(mxRpcLogin.getWeight());
        mxRpcProviderObj.setServiceName(mxRpcLogin.getServiceName());
        mxRpcProviderObj.setServiceVersion(mxRpcLogin.getServiceVersion());
        mxRpcProviderObj.setContext(context);
        MxRpcServiceObj mxRpcServiceObj = rpcServiceObjMap.computeIfAbsent(mxRpcLogin.getServiceName(),k -> {
            MxRpcServiceObj mxRpcServiceObj1 = new MxRpcServiceObj();
            mxRpcServiceObj1.setVisitStrategy(visitStrategy.getCode());
            return mxRpcServiceObj1;
        });
        mxRpcServiceObj.addProvider(mxRpcProviderObj);

        MxRpcResponse mxRpcResponse = new MxRpcResponse();
        mxRpcResponse.setResult(BaseResult.success());
        mxRpcResponse.setRequestId(mxRpcLogin.getClientId());
        context.writeAndFlush(FastJsonUtil.toJSONString(mxRpcResponse));

        clientLinkMap.put(context,ctxId);
        rpcProviderObjMap.put(ctxId,mxRpcProviderObj);
    }

    /**
     * 处理接收到的消息
     * @param context
     * @param dataJson
     */
    public void handleMsg(ChannelHandlerContext context,String dataJson){
        try {
            if (dataJson.indexOf("}{") > 0){
                dataJson.replaceAll("\\}\\{","}" + FastJsonUtil.END_CODE + "{");
                String[] split = dataJson.split(FastJsonUtil.END_CODE);
                for (String s : split) {
                    handleMsg(context,s);
                }
                return;
            }
            String ctxId = getClientId(context);

            logInfo("服务端收到消息：{}",dataJson);

            boolean isLogin = ctxId != null;
            Object handleObject = FastJsonUtil.parse(dataJson);
            if (handleObject instanceof MxRpcLogin){
                MxRpcLogin mxRpcLogin = (MxRpcLogin)handleObject;
                if (mxRpcServerConfig.getPassword().equals(mxRpcLogin.getLoginPassword())){
                    //登录成功记录
                    loginIn(context,mxRpcLogin);
                } else {
                    MxRpcResponse mxRpcResponse = new MxRpcResponse();
                    mxRpcResponse.setResult(BaseResult.fail("密码验证失败"));
                    mxRpcResponse.setError("密码验证失败");
                    mxRpcResponse.setRequestId(mxRpcLogin.getClientId());
                    context.writeAndFlush(FastJsonUtil.toJSONString(mxRpcResponse));
                    context.close();
                }
                return;
            }
            if (!isLogin){
                return;
            }
            if (handleObject instanceof MxRpcRequest){
                //请求对象
                MxRpcRequest mxRpcRequest = (MxRpcRequest)handleObject;
                MxRpcHandleObj mxRpcHandleObj = new MxRpcHandleObj();
                mxRpcHandleObj.setRequestId(mxRpcRequest.getRequestId());
                mxRpcHandleObj.setRequestCtxId(ctxId);

                MxRpcServiceObj mxRpcServiceObj = rpcServiceObjMap.get(mxRpcRequest.getServiceName());

                boolean isGetPro = mxRpcServiceObj != null;

                if (isGetPro){
                    String  visitCtxId = mxRpcServiceObj.visit(mxRpcRequest);
                    if (visitCtxId == null){
                        isGetPro = false;
                    } else {
                        mxRpcHandleObj.setResponseCtxId(visitCtxId);
                        taskMap.put(mxRpcHandleObj.getRequestId(),mxRpcHandleObj);
                    }
                }

                if (!isGetPro){
                    MxRpcResponse mxRpcResponse = new MxRpcResponse();
                    mxRpcResponse.setResult(BaseResult.fail("当前没有对应服务提供者"));
                    mxRpcResponse.setError("当前没有对应服务提供者");
                    mxRpcResponse.setRequestId(mxRpcRequest.getRequestId());
                    context.writeAndFlush(FastJsonUtil.toJSONString(mxRpcResponse));
                }

            } else if (handleObject instanceof MxRpcResponse){
                //回复对象
                MxRpcResponse mxRpcResponse = (MxRpcResponse)handleObject;
                MxRpcHandleObj mxRpcHandleObj = taskMap.remove(mxRpcResponse.getRequestId());
                if (mxRpcHandleObj == null ||
                    !ctxId.equalsIgnoreCase(mxRpcHandleObj.getResponseCtxId())){
                    //未知回复 丢弃
                    logInfo("丢弃消息：{}",mxRpcResponse.getRequestId());
                } else {
                    String requestCtxId = mxRpcHandleObj.getRequestCtxId();
                    MxRpcProviderObj mxRpcProviderObj = rpcProviderObjMap.get(requestCtxId);
                    if (mxRpcProviderObj == null){
                        return;
                    }
                    mxRpcProviderObj.sendResponse(mxRpcResponse);
                }
            } else if (handleObject instanceof MxRpcHeartBeat){
                //接收到心跳包
                MxRpcHeartBeat mxRpcHeartBeat = new MxRpcHeartBeat();
                mxRpcHeartBeat.setClientId("server");
                mxRpcHeartBeat.setServiceName("server");
                context.writeAndFlush(FastJsonUtil.toJSONString(mxRpcHeartBeat));
            }

        } catch (Exception e){
            log.error("数据处理异常:",e);
        }
    }

    public void setVisitStrategy(ProviderVisitStrategyEnums visitStrategy) {
        this.visitStrategy = visitStrategy;
    }
    
    private void logInfo(String info,String msg){
        if (mxRpcServerConfig.getDebug()){
            log.info(info,msg);
        }
    }
}

package com.mxspace.rpc.service;


import com.mxspace.rpc.component.MxRpcClient;
import com.mxspace.rpc.data.MxRpcClientConfig;
import com.mxspace.rpc.util.*;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

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
     * 当前连接对象
     */
    private ChannelHandlerContext ctx;

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
        mxRpcLogin.setServiceVersion(mxRpcClientConfig.getServiceVersion());
        ctx.writeAndFlush(FastJsonUtil.toJSONString(mxRpcLogin));
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
        logInfo("客户端收到消息：{}",msg);
        if (msg.indexOf("}{") > 0){
            msg.replaceAll("\\}\\{","}" + FastJsonUtil.END_CODE + "{");
            String[] split = msg.split(FastJsonUtil.END_CODE);
            for (String s : split) {
                handleMsg(ctx,s);
            }
            return;
        }
        Object msgData = FastJsonUtil.parse(msg);
        if (msgData instanceof MxRpcResponse){
            MxRpcResponse mxRpcResponse = (MxRpcResponse) msgData;
            String requestId = mxRpcResponse.getRequestId();
            BaseResult baseResult = mxRpcResponse.getResult();
            if (requestId.equalsIgnoreCase(mxRpcClientConfig.getClientId())){
                //登录请求
                loginState = baseResult.getCode() == BaseResult.SUCCESS_CODE;
                this.ctx = ctx;
            } else {
                FutureRpcData futureRpcData = taskMap.remove(requestId);
                if (futureRpcData != null){
                    futureRpcData.setResult(mxRpcResponse);
                }
            }
        } else if (msgData instanceof MxRpcRequest){
            MxRpcRequest mxRpcRequest = (MxRpcRequest) msgData;
            MxRpcResponse mxRpcResponse = new MxRpcResponse();
            mxRpcResponse.setRequestId(mxRpcRequest.getRequestId());
            try {
                String className = mxRpcRequest.getClassName();
                String methodName = mxRpcRequest.getMethodName();
                Object[] parameters = mxRpcRequest.getParameters();
                Class<?> aClass = Class.forName(className);
                Object bean = MxRpcClient.applicationContext.getBean(aClass);
                Method method = aClass.getMethod(methodName, mxRpcRequest.getParameterTypes());
                Object invoke = method.invoke(bean, parameters);
                mxRpcResponse.setResult(BaseResult.success(invoke));
            } catch (Exception e) {
                mxRpcResponse.setError(e.toString());
                mxRpcResponse.setResult(BaseResult.fail(e.toString()));
                log.error("异常",e);
            } finally {
                ctx.writeAndFlush(FastJsonUtil.toJSONString(mxRpcResponse));
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
        ctx.writeAndFlush(FastJsonUtil.toJSONString(mxRpcHeartBeat));
    }

    /**
     * 发送请求
     * @param mxRpcRequest
     * @return
     */
    public boolean sendRequest(MxRpcRequest mxRpcRequest,FutureRpcData futureRpcData){
        if (!this.loginState){
            return false;
        }
        mxRpcRequest.setRequestId(UUID.randomUUID().toString());
        taskMap.put(mxRpcRequest.getRequestId(),futureRpcData);
        this.ctx.writeAndFlush(FastJsonUtil.toJSONString(mxRpcRequest));
        return true;
    }

    /**
     * 同步发送
     * @param mxRpcRequest
     * @return
     */
    public MxRpcResponse sendRequestSync(MxRpcRequest mxRpcRequest){
        if (!this.loginState){
            return null;
        }
        mxRpcRequest.setRequestId(UUID.randomUUID().toString());
        FutureRpcData<MxRpcResponse> futureRpcData = new FutureRpcData<>();
        taskMap.put(mxRpcRequest.getRequestId(),futureRpcData);
        this.ctx.writeAndFlush(FastJsonUtil.toJSONString(mxRpcRequest));
        try {
            MxRpcResponse mxRpcResponse = futureRpcData.get(60, TimeUnit.SECONDS);
            return mxRpcResponse;
        } catch (InterruptedException e) {
           log.error("MxRpcClient",e);
        } catch (ExecutionException e) {
            log.error("MxRpcClient",e);
        } catch (TimeoutException e) {
            log.error("MxRpcClient",e);
        }
        return null;
    }

    private void logInfo(String info,String msg){
        if (mxRpcClientConfig.getDebug()){
            log.info(info,msg);
        }
    }
}

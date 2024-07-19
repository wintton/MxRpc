package com.mxspace.rpc.component;

import com.mxspace.rpc.annotation.MxRpcResource;
import com.mxspace.rpc.service.MxRpcClientService;
import com.mxspace.rpc.util.BaseResult;
import com.mxspace.rpc.util.MxRpcRequest;
import com.mxspace.rpc.util.MxRpcResponse;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;


@Slf4j
public class MxRpcProxy implements InvocationHandler {

    private MxRpcClientService mxRpcClientService;

    public MxRpcProxy( MxRpcClientService mxRpcClientService){
        this.mxRpcClientService = mxRpcClientService;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
         if (mxRpcClientService == null){
            log.error("客户端服务对象为空");
            return null;
        }
        MxRpcRequest rpcRequest = new MxRpcRequest();

        rpcRequest.setClassName(method.getDeclaringClass().getName());

        rpcRequest.setMethodName(method.getName());

        rpcRequest.setParameters(args);

        rpcRequest.setParameterTypes(method.getParameterTypes());

        MxRpcResource annotation = proxy.getClass().getAnnotation(MxRpcResource.class);
        if (annotation != null){
            rpcRequest.setServiceName(annotation.name());
            rpcRequest.setServiceVersion(annotation.version());
        } else {
            rpcRequest.setServiceName("ALL");
        }

        MxRpcResponse mxRpcResponse = mxRpcClientService.sendRequestSync(rpcRequest);

        if (mxRpcResponse != null
        && mxRpcResponse.getResult().getCode() == BaseResult.SUCCESS_CODE){
            return mxRpcResponse.getResult().getData();
        }

        return null;
    }
}
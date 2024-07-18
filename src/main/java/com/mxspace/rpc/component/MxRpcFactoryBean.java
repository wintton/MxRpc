package com.mxspace.rpc.component;

import com.mxspace.rpc.service.MxRpcClientService;
import org.springframework.beans.factory.FactoryBean;

import java.lang.reflect.Proxy;


public class MxRpcFactoryBean implements FactoryBean {

    private Class type;


    private MxRpcProxy rpcProxy;

    public MxRpcFactoryBean(Class type, MxRpcClientService mxRpcClientService){
        this.type = type;
        this.rpcProxy = new MxRpcProxy(mxRpcClientService);
    }

    @Override
    public Object getObject() throws Exception {
        return Proxy.newProxyInstance(type.getClassLoader(), new Class[]{type}, rpcProxy);
    }

    @Override

    public Class<?> getObjectType() {
        return type;
    }

}
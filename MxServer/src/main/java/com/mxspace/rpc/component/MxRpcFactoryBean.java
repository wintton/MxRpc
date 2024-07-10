package com.mxspace.rpc.component;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Proxy;


public class MxRpcFactoryBean implements FactoryBean {

    private Class interfaceClass;

    @Autowired
    private MxRpcProxy rpcProxy;

    public MxRpcFactoryBean(Class interfaceClass){
        this.interfaceClass = interfaceClass;

    }

    @Override
    public Object getObject() throws Exception {
        return Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class[]{interfaceClass}, rpcProxy);
    }

    @Override

    public Class<?> getObjectType() {

        return interfaceClass;

    }

}
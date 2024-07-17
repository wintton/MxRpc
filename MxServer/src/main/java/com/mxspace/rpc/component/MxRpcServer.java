package com.mxspace.rpc.component;

import com.mxspace.rpc.annotation.EnableMxRpcServer;
import com.mxspace.rpc.data.MxRpcServerConfig;
import com.mxspace.rpc.service.MxRpcClientManService;
import com.mxspace.rpc.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.util.Map;

/**
 * 服务类
 */
@Component
@Slf4j
public class MxRpcServer implements ImportBeanDefinitionRegistrar {


    @Resource
    private MxRpcServerConfig mxRpcServerConfig;

    @Resource
    private MxRpcClientManService mxRpcClientManService;

    /**
     * 是否开启RPC服务
     */
    private volatile static boolean enableServer = false;

    private MxRpcServerThread curRunThread;


    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        Map<String, Object> enableEcho = importingClassMetadata.getAnnotationAttributes(EnableMxRpcServer.class.getName());
        if (enableEcho != null){
            enableServer = true;
        }
    }


    @PostConstruct
    public void start(){
        if (!enableServer){
            return;
        }
        curRunThread = new MxRpcServerThread(mxRpcServerConfig.getPort(),mxRpcClientManService);
        curRunThread.setName("MxRpcServer");
        curRunThread.start();
    }

    @PreDestroy
    public void stop(){
        if (!enableServer){
            return;
        }
        curRunThread.stopRun();
    }

}

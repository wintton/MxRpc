package com.mxspace.rpc.component;

import com.mxspace.rpc.annotation.EnableMxRpcClient;
import com.mxspace.rpc.data.MxRpcClientConfig;
import com.mxspace.rpc.service.MxRpcClientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;


/**
 * 客户端
 */
@Component
@Slf4j
public class MxRpcClient implements ApplicationContextAware, InitializingBean, ImportBeanDefinitionRegistrar {


    // RPC服务实现容器
    private Map<String, Object> rpcServices = new HashMap<>();

    @Resource
    private MxRpcClientConfig mxRpcClientConfig;

    @Resource
    private MxRpcClientService mxRpcClientService;

    public static ApplicationContext applicationContext;

    /**
     * 是否开启RPC服务
     */
    private volatile static boolean enableClient = false;

    private MxRpcClientThread curRunThread;


    @Override
    public void afterPropertiesSet() throws Exception {

    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        Map<String, Object> enableEcho = importingClassMetadata.getAnnotationAttributes(EnableMxRpcClient.class.getName());
        if (enableEcho != null){
            enableClient = true;
        }

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }


    @PostConstruct
    public void start(){
        if (!enableClient){
            return;
        }
        curRunThread = new MxRpcClientThread(mxRpcClientConfig,mxRpcClientService);
        curRunThread.setName("MxRpcClient");
        curRunThread.start();
    }

    @PreDestroy
    public void stop(){
        if (!enableClient){
            return;
        }
        curRunThread.stopRun();
    }



}

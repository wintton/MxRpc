package com.mxspace.rpc.component;

import com.mxspace.rpc.annotation.EnableMxRpc;
import com.mxspace.rpc.annotation.MxRpcService;
import com.mxspace.rpc.data.MxRpcServerConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.MergedAnnotation;
import org.springframework.core.annotation.MergedAnnotations;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

/**
 * 服务类
 */
@Component
@Slf4j
public class MxRpcServer implements ApplicationContextAware, InitializingBean, ImportBeanDefinitionRegistrar {

    // RPC服务实现容器
    private Map<String, Object> rpcServices = new HashMap<>();

    @Resource
    private MxRpcServerConfig mxRpcServerConfig;

    /**
     * 是否开启RPC服务
     */
    private volatile static boolean enableServer = false;

    private MxRpcServerThread curRunThread;

    @Override
    public void afterPropertiesSet() throws Exception {

    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        Map<String, Object> enableEcho = importingClassMetadata.getAnnotationAttributes(EnableMxRpc.class.getName());
        if (enableEcho != null){
            enableServer = true;
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

        Map<String, Object> services = applicationContext.getBeansWithAnnotation(MxRpcService.class);

        for (Map.Entry<String, Object> entry : services.entrySet()) {

            Object bean = entry.getValue();

            Class<?>[] interfaces = bean.getClass().getInterfaces();

            for (Class<?> inter : interfaces) {

                rpcServices.put(inter.getName(), bean);

            }

        }
    }

    @PostConstruct
    public void start(){
        if (!enableServer){
            return;
        }
        curRunThread = new MxRpcServerThread(mxRpcServerConfig.getPort());
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

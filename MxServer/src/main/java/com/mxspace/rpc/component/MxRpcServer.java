package com.mxspace.rpc.component;

import com.mxspace.rpc.annotation.EnableMxRpc;
import com.mxspace.rpc.annotation.MxRpcService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 服务类
 */
@Component
@Slf4j
public class MxRpcServer implements ApplicationContextAware, InitializingBean {

    // RPC服务实现容器

    private Map<String, Object> rpcServices = new HashMap<>();

    @Value("${rpc.server.port:8081}")
    private int port;

    @Override
    public void afterPropertiesSet() throws Exception {

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

        System.out.println(applicationContext.getClass());

        Map<String, Object> services = applicationContext.getBeansWithAnnotation(MxRpcService.class);

        for (Map.Entry<String, Object> entry : services.entrySet()) {

            Object bean = entry.getValue();

            Class<?>[] interfaces = bean.getClass().getInterfaces();

            for (Class<?> inter : interfaces) {

                rpcServices.put(inter.getName(), bean);

            }

        }
    }

}

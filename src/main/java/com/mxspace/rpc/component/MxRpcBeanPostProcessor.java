package com.mxspace.rpc.component;

import com.mxspace.rpc.annotation.MxRpcResource;
import com.mxspace.rpc.service.MxRpcClientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Field;

/**
 * Netty客户端Bean后置处理器
 * 实现Spring后置处理器接口：BeanPostProcessor
 * 在Bean对象在实例化和依赖注入完毕后，在显示调用初始化方法的前后添加我们自己的逻辑。注意是Bean实例化完毕后及依赖注入完成后触发的
 *
 * @author ZC
 * @date 2021/3/2 23:00
 */
@Component
@Slf4j
public class MxRpcBeanPostProcessor implements BeanPostProcessor {

    @Resource
    private MxRpcClientService mxRpcClientService;

    public MxRpcBeanPostProcessor() {

    }


    @Override
    public Object postProcessBeforeInitialization(Object bean, @Nullable String beanName) throws BeansException {

        return bean;
    }


    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        // 可以根据beanName不同执行不同的处理操作
        //获取实例Class
        Class<?> beanClass = bean.getClass();
        do {
            //获取该类所有字段
            Field[] fields = beanClass.getDeclaredFields();
            for (Field field : fields) {
                //判断该字段是否拥有@MxRpcResource
                if (field.getAnnotation(MxRpcResource.class) != null) {
                    field.setAccessible(true);
                    try {
                        //通过JDK动态代理获取该类的代理对象
                        Class<?> type = field.getType();
                        Object o = new MxRpcFactoryBean(type,this.mxRpcClientService).getObject();
                        //将代理类注入该字段
                        field.set(bean,o);
                        log.info("创建代理类 ===>>> {}", beanName);
                    } catch (IllegalAccessException e) {
                        log.error(e.getMessage());
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }
                }
            }
        } while ((beanClass = beanClass.getSuperclass()) != null);
        return bean;
    }


}
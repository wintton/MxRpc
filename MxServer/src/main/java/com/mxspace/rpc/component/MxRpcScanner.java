package com.mxspace.rpc.component;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.classreading.MetadataReader;

import java.io.IOException;
import java.util.Set;


public class MxRpcScanner extends ClassPathBeanDefinitionScanner {

    public MxRpcScanner(BeanDefinitionRegistry registry) {
        super(registry);

    }

    @Override
    protected Set doScan(String... basePackages) {

        Set<BeanDefinitionHolder> beanDefinitionHolders = super.doScan(basePackages);

        for (BeanDefinitionHolder beanDefinitionHolder : beanDefinitionHolders) {

            GenericBeanDefinition beanDefinition = (GenericBeanDefinition)beanDefinitionHolder.getBeanDefinition();

            beanDefinition.getConstructorArgumentValues().addGenericArgumentValue(beanDefinition.getBeanClassName());

            beanDefinition.setBeanClassName(MxRpcFactoryBean.class.getName());

        }

        return beanDefinitionHolders;

    }

    @Override
    protected boolean isCandidateComponent(MetadataReader metadataReader) throws IOException {

        return true;

    }

    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {

        return beanDefinition.getMetadata().isInterface() && beanDefinition.getMetadata().isIndependent();

    }

}
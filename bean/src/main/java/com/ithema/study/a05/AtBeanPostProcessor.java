package com.ithema.study.a05;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.type.MethodMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;

import java.util.Set;

@Slf4j
public class AtBeanPostProcessor implements BeanFactoryPostProcessor {


    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        try {
            CachingMetadataReaderFactory factory = new CachingMetadataReaderFactory();
            MetadataReader reader = factory.getMetadataReader(new ClassPathResource("com/ithema/study/a05/Config.class"));
            Set<MethodMetadata> methods = reader.getAnnotationMetadata().getAnnotatedMethods(Bean.class.getName());
            methods.stream().forEach(methodMetadata -> log.info("======method:{}",methodMetadata));
            for (MethodMetadata method : methods) {
                //获取@Bean注解中的属性值，属性的key为initMethod
                String initMethod = method.getAnnotationAttributes(Bean.class.getName()).get("initMethod").toString();

                BeanDefinitionBuilder builder=BeanDefinitionBuilder.genericBeanDefinition();
                //设置自动装配，就是在config类中的工厂方法，如果方法有参数，那些参数自动装配
                builder.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_CONSTRUCTOR);
                if(StringUtils.isNotBlank(initMethod)){
                    //设置初始化方法，传入方法名
                    builder.setInitMethodName(initMethod);
                }
                AbstractBeanDefinition beanDefinition = builder.setFactoryMethodOnBean(method.getMethodName(), "config").getBeanDefinition();
                if (configurableListableBeanFactory instanceof DefaultListableBeanFactory beanFactory) {
                    beanFactory.registerBeanDefinition(method.getMethodName(),beanDefinition);
                }

            }

        }catch (Exception e){

        }


    }
}

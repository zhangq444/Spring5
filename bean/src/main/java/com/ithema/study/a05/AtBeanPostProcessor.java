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

/**
 * 自己实现@Bean注解的解析
 */
@Slf4j
public class AtBeanPostProcessor implements BeanFactoryPostProcessor {


    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        try {
            /**
             * 这个Config类所充当的角色是一个工厂的角色，而类中被@Bean标注的方法就充当着工厂方法，所以创建BeanDefinition使用工厂方法的方式创建BeanDefinition
             */
            CachingMetadataReaderFactory factory = new CachingMetadataReaderFactory();
            MetadataReader reader = factory.getMetadataReader(new ClassPathResource("com/ithema/study/a05/Config.class"));
            //判断Config类中哪些方法加了@Bean注解
            Set<MethodMetadata> methods = reader.getAnnotationMetadata().getAnnotatedMethods(Bean.class.getName());
            methods.stream().forEach(methodMetadata -> log.info("======method:{}", methodMetadata));
            for (MethodMetadata method : methods) {
                //获取@Bean注解中的属性值，属性的key为initMethod
                String initMethod = method.getAnnotationAttributes(Bean.class.getName()).get("initMethod").toString();

                //创建生成BeanDefinition的builder对象
                BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition();
                //设置自动装配，就是在config类中的工厂方法(就是被标注的@Bean注解的方法)，如果方法有参数，那些参数自动装配
                builder.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_CONSTRUCTOR);
                if (StringUtils.isNotBlank(initMethod)) {
                    //设置初始化方法，传入方法名
                    builder.setInitMethodName(initMethod);
                }
                //设置哪个方法作为工厂方法，第一个参数，作为工厂方法的方法名（就是被标注了@Bean注解的方法），第二个参数，工厂对象，要调用工厂方法首先要有工厂对象，就是调用@Bean注解方法首先要有所属类的对象，
                //传进去作为第二个参数的是这个工厂对象在容器中的名字，就是Config类的类名小写
                AbstractBeanDefinition beanDefinition = builder.setFactoryMethodOnBean(method.getMethodName(), "config").getBeanDefinition();
                if (configurableListableBeanFactory instanceof DefaultListableBeanFactory beanFactory) {
                    //bean的名字就是工厂方法的名字
                    beanFactory.registerBeanDefinition(method.getMethodName(), beanDefinition);
                }

            }

        } catch (Exception e) {

        }


    }
}

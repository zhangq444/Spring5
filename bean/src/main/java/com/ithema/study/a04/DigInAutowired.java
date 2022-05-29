package com.ithema.study.a04;


import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.beans.factory.annotation.InjectionMetadata;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.ContextAnnotationAutowireCandidateResolver;
import org.springframework.core.env.StandardEnvironment;

import javax.management.StandardEmitterMBean;
import java.lang.reflect.Method;

@Slf4j
public class DigInAutowired {

    public static void main(String[] args) throws Throwable {
        DefaultListableBeanFactory beanFactory=new DefaultListableBeanFactory();
        beanFactory.registerSingleton("bean2",new Bean2());
        beanFactory.registerSingleton("bean3",new Bean3());
        beanFactory.setAutowireCandidateResolver(new ContextAnnotationAutowireCandidateResolver());//解析@Value
        beanFactory.addEmbeddedValueResolver(new StandardEnvironment()::resolvePlaceholders);//解析${}的

        AutowiredAnnotationBeanPostProcessor processor = new AutowiredAnnotationBeanPostProcessor();
        processor.setBeanFactory(beanFactory);
        Bean1 bean1 = new Bean1();
        /*log.info("======1  bean1:{}", bean1);
        processor.postProcessProperties(null,bean1,"bean1");
        log.info("======2  bean1:{}", bean1);*/

        Method method = AutowiredAnnotationBeanPostProcessor.class.getDeclaredMethod("findAutowiringMetadata", String.class, Class.class, PropertyValues.class);
        method.setAccessible(true);
        InjectionMetadata metadata = (InjectionMetadata) method.invoke(processor, "bean1", Bean1.class, null);
        System.out.println(metadata);

        metadata.inject(bean1,"bean1",null);
        System.out.println(bean1);


    }



}

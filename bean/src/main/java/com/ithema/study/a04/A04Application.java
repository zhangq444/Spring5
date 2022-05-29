package com.ithema.study.a04;

import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesBindingPostProcessor;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.CommonAnnotationBeanPostProcessor;
import org.springframework.context.annotation.ContextAnnotationAutowireCandidateResolver;
import org.springframework.context.support.GenericApplicationContext;

@SpringBootApplication
public class A04Application {

    public static void main(String[] args) {
        //一个干净的容器
        GenericApplicationContext context = new GenericApplicationContext();

        //注册3个bean
        context.registerBean("bean1",Bean1.class);
        context.registerBean("bean2",Bean2.class);
        context.registerBean("bean3",Bean3.class);
        context.registerBean("bean4",Bean4.class);

        //用来解析@Autowired和@value
        context.getDefaultListableBeanFactory().setAutowireCandidateResolver(new ContextAnnotationAutowireCandidateResolver());//这个解析器不加的话，处理@Value注解会报错
        context.registerBean(AutowiredAnnotationBeanPostProcessor.class);
        //用来解析@Resource  @PostConstruct  @PreDestroy
        context.registerBean(CommonAnnotationBeanPostProcessor.class);
        //用来解析@ConfigurationProperties
        ConfigurationPropertiesBindingPostProcessor.register(context.getDefaultListableBeanFactory());


        //初始化容器，执行beanFactory后处理器，添加bean后处理器,初始化所有单例
        context.refresh();
        System.out.println(context.getBean(Bean4.class));

        //销毁容器
        context.close();
    }



}

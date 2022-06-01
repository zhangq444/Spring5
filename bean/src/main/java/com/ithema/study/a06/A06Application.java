package com.ithema.study.a06;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.CommonAnnotationBeanPostProcessor;
import org.springframework.context.annotation.ConfigurationClassPostProcessor;
import org.springframework.context.support.GenericApplicationContext;

import java.util.Arrays;

@Slf4j
public class A06Application {

    public static void main(String[] args) {
        GenericApplicationContext context=new GenericApplicationContext();

//        context.registerBean("myBean",MyBean.class);
//        context.registerBean("myConfig1",MyConfig1.class);
        context.registerBean("myConfig2",MyConfig2.class);
        context.registerBean(AutowiredAnnotationBeanPostProcessor.class);//解析autowire
        context.registerBean(CommonAnnotationBeanPostProcessor.class);//解析@Resource  @PostConstruct  @PreDestroy
        context.registerBean(ConfigurationClassPostProcessor.class);//解析@Bean，@ComponentScan

        /**
         * ApplicationContextAware, InitializingBean这两个接口的作用是，使用@Autowired注解注入时，注入ApplicationContext可能会失效，但是用ApplicationContextAware不会失败，
         * 同时，用@PostConstructor进行初始化也会失效，但是用InitializingBean不会失败，这两个接口是spring的内置功能，不需要添加Bean的后置处理器
         */


        /**
         * 执行该方法时，也就是容器初始化时，执行顺序如下1，注册beanFactory后置处理器 2.注册bean后置处理器
         * 3.实例化单例（3.1 依赖注入 例如@Autowired 3.2 初始化扩展 例如@PostConstruct 3.3 执行Aware和InitializingBean  3.4初始化成功）
         *
         * 但是如果初始化的配置类中有beanFactory后置处理器对象的话，执行顺序就改为
         * 1.实例化单例（1.1 执行Aware和InitializingBean）
         * 2，注册beanFactory后置处理器 3.注册bean后置处理器
         * 这个时候  那些@Autowired之类的注解就失效了
         */
        context.refresh();

        Arrays.stream(context.getBeanDefinitionNames()).forEach(beanName->{
            log.info("======beanName:{}",beanName);
        });



        context.close();




    }



}

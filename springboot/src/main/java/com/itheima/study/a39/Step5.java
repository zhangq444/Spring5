package com.itheima.study.a39;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.DefaultBootstrapContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.config.ConfigDataEnvironmentPostProcessor;
import org.springframework.boot.context.event.EventPublishingRunListener;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.boot.env.EnvironmentPostProcessorApplicationListener;
import org.springframework.boot.env.RandomValuePropertySourceEnvironmentPostProcessor;
import org.springframework.boot.logging.DeferredLog;
import org.springframework.boot.logging.DeferredLogs;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.io.support.SpringFactoriesLoader;

import java.util.List;

/**
 * run方法第五步做的事情，调用了EnvironmentPostProcessor后处理器，它有很多的子类，继续增强ApplicationEnvironment环境对象，读取properties文件作为源就是在这一步添加的,
 * 增强的方法就是添加更多的PropertySources
 * @author grzha
 */
@Slf4j
public class Step5 {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication();
        StandardEnvironment env = new StandardEnvironment();
        //test01(application);

        ///读取spring的配置文件spring.factories,里面配置了接口EnvironmentPostProcessor的所有的实现类
        /*List<String> names = SpringFactoriesLoader.loadFactoryNames(EnvironmentPostProcessor.class, Step5.class.getClassLoader());
        for (String name : names) {
            log.info("======name:{}",name);
        }*/
        ///但是读取spring.factories的配置，调用所有的EnvironmentPostProcessor后处理器，增强Environment环境对象，是一个监听器处理的,这里先添加监听器
        application.addListeners(new EnvironmentPostProcessorApplicationListener());

        //然后有一个事件发布器，在运行到第5步的时候，发布一个事件，然后会被前面的监听器监听到，在监听器里面，就会调用所有配置的EnvironmentPostProcessor后处理器
        //从而增强Environment环境对象,所以第3步是创建了Environment对象，第4步是统一了一些命名的规范，然后第5步发布事件，然后由上面的监听器，去调用所有的Environment的后处理器
        EventPublishingRunListener publisher = new EventPublishingRunListener(application, args);
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>发布事件前");
        for (PropertySource<?> source : env.getPropertySources()) {
            log.info("======source:{}",source);
        }
        publisher.environmentPrepared(new DefaultBootstrapContext(),env);
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>发布事件后");
        for (PropertySource<?> source : env.getPropertySources()) {
            log.info("======source:{}",source);
        }

    }

    private static void test01(SpringApplication application) {
        StandardEnvironment env = new StandardEnvironment();

        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>增强前");
        for (PropertySource<?> source : env.getPropertySources()) {
            log.info("======source:{}",source);
        }
        //添加后处理器,这个后处理器可以添加application.properties作为环境变量的源
        ConfigDataEnvironmentPostProcessor postProcessor1 = new ConfigDataEnvironmentPostProcessor(new DeferredLogs(), new DefaultBootstrapContext());
        postProcessor1.postProcessEnvironment(env, application);

        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>增强后1");
        for (PropertySource<?> source : env.getPropertySources()) {
            log.info("======source:{}",source);
        }
        //添加random后处理器，可以生成随机数
        RandomValuePropertySourceEnvironmentPostProcessor postProcessor2=new RandomValuePropertySourceEnvironmentPostProcessor(new DeferredLog());
        postProcessor2.postProcessEnvironment(env, application);

        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>增强后2");
        for (PropertySource<?> source : env.getPropertySources()) {
            log.info("======source:{}",source);
        }

        log.info("======{}",env.getProperty("server.port"));
        log.info("======{}",env.getProperty("random.int"));
        log.info("======{}",env.getProperty("random.uuid"));
    }


}

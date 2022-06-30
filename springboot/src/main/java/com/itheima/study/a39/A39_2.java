package com.itheima.study.a39;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.DefaultBootstrapContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.io.support.SpringFactoriesLoader;

import java.lang.reflect.Constructor;
import java.util.List;

/**
 * 这一讲主要讲了SpringApplication.run方法启动后做的事情
 * @author grzha
 */
@Slf4j
public class A39_2 {

    /**
     * Boot启动过程
     * 创建SpringApplication对象
     * 1.记录BeanDefinition源
     * 2.推断应用类型
     * 3.记录ApplicationContext初始化器
     * 4.记录监听器
     * 5.推断主启动类
     * <p>
     * 执行run方法
     * 1.得到SpringApplicationRunListeners,名字取的不好，实际是事件发布器
     * 发布application starting事件
     * 2.封装启动args
     * 3.准备Environment 添加命令行参数
     * 4.ConfigurationPropertySources处理
     * 发布application environment已准备事件
     * 5.通过EnvironmentPostProcessorApplicationListener进行env后处理
     * application.properties，由StandardConfigDataLocationResolver解析
     * spring.application.json
     * 6.绑定spring.main 到SpringApplication对象
     *
     * @param args
     */
    public static void main(String[] args) throws Exception {

        SpringApplication application = new SpringApplication();
        //添加事件监听器，监听事件，打印事件的类型
        application.addListeners(event -> {
            log.info("======event:{}",event.getClass());
        });

        //获取事件发送器实现类名,事件发送器的接口是SpringApplicationRunListener，实现类是EventPublishingRunListener
        //接口和实现类的关系是配置在spring.factories这个配置文件中的，SpringFactoriesLoader这个工具类可以读取spring.factories这类配置文件，然后就可以获得实现类的名称了
        List<String> nameList = SpringFactoriesLoader.loadFactoryNames(SpringApplicationRunListener.class, A39_2.class.getClassLoader());
        nameList.stream().forEach(name->log.info("======name:{}",name));

        //获取事件发布器的构造方法，然后创建事件发布器对象
        Class<?> clazz = Class.forName(nameList.get(0));
        Constructor<?> constructor = clazz.getConstructor(SpringApplication.class, String[].class);
        SpringApplicationRunListener publisher = (SpringApplicationRunListener) constructor.newInstance(application, args);
        //发布事件
        //springboot 开始启动
        DefaultBootstrapContext bootstrapContext = new DefaultBootstrapContext();
        publisher.starting(bootstrapContext);
        //springboot 环境信息准备完毕，例如读取环境信息，读取配置文件等，这个事件发布完毕创建spring容器
        publisher.environmentPrepared(bootstrapContext, new StandardEnvironment());
        //spring容器创建，并调用初始化器之后，会发送此事件
        GenericApplicationContext context = new GenericApplicationContext();
        publisher.contextPrepared(context);
        //所有BeanDefinition加载完毕
        publisher.contextLoaded(context);
        //spring容器初始化完成（refresh方法调用完毕）
        context.refresh();
        publisher.started(context);
        //springboot 启动完毕
        publisher.running(context);

        //springboot启动错误
        publisher.failed(context, new Exception("出错了"));

        //以上就是7个事件，这7个事件发布的时候，就会被application.addListeners前面添加的监听器监听到
        //打印的日志中，包含boot.context.event的都是由SpringApplicationRunListener这个事件发布器发布的，其他的是由别的事件发布器发布的


    }


}

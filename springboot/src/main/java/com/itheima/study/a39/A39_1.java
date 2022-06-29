package com.itheima.study.a39;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.GenericApplicationContext;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;

/**
 * 这一讲讲解了SpringApplication的构造方法中，执行了哪些操作，就是里面1-5点，对应的是SpringApplication类中的SpringApplication(Class<?>... primarySources)这个方法里面干的事
 *
 * @author grzha
 */
@Slf4j
@Configuration
public class A39_1 {

    public static void main(String[] args) throws Exception {
        /**
         * 这个是Springboot启动的程序，里面做了很多的事
         */
        /*SpringApplication.run(A39_1.class, args);*/
        /**
         * 以下是老师讲的在启动时做的事情，以下5个东西是构造方法里面做的事情
         */
        System.out.println("1.演示获取 Bean Definition 源");
        /**
         * 演示bean的来源，第一个来自于配置类，第二个来自于配置文件
         */
        SpringApplication spring = new SpringApplication(A39_1.class);
        spring.setSources(Set.of("classpath:b01.xml"));
        System.out.println("2.演示推断应用类型");
        /**
         * WebApplicationType类中的deduceFromClasspath是用来判断当前项目是什么类型的项目，是普通项目还是web项目,例如它会判断在类路径下面是否有DispatcherServlet这个类等等方法
         */
        Method deduceFromClasspath = WebApplicationType.class.getDeclaredMethod("deduceFromClasspath");
        deduceFromClasspath.setAccessible(true);
        log.info("======应用类型为：{}", deduceFromClasspath.invoke(null));

        System.out.println("3.演示ApplicationContext初始化器");
        //添加ApplicationContext初始化器
        spring.addInitializers(new ApplicationContextInitializer<ConfigurableApplicationContext>() {
            @Override
            public void initialize(ConfigurableApplicationContext applicationContext) {
                //传入的参数为初始化后，但是还没有refresh的applicationContext
                if (applicationContext instanceof GenericApplicationContext gac) {
                    //在refresh之前，注册Bean3
                    gac.registerBean("bean3", Bean3.class);
                }
            }
        });
        System.out.println("4.演示监听器与事件");
        spring.addListeners(new ApplicationListener<ApplicationEvent>() {
            @Override
            public void onApplicationEvent(ApplicationEvent event) {
                //传过来的是spring启动时，就是下面的run(args)执行时，产生的事件
                //打印事件的类型
                log.info("=====事件为：{}", event.getClass());
            }
        });
        System.out.println("5.演示主类推断");
        //这个是判断springboot项目的main方法所属的类，就是启动类所在的主类是哪一个，是SpringApplication这个类的deduceMainApplicationClass做的判断
        Method deduceMainApplicationClass = SpringApplication.class.getDeclaredMethod("deduceMainApplicationClass");
        deduceMainApplicationClass.setAccessible(true);
        log.info("======main方法所在的类，就是主类是:{}", deduceMainApplicationClass.invoke(spring));


        ConfigurableApplicationContext context = spring.run(args);
        /**
         * 创建ApplicationContext，
         * 在中间，可以调用第3步中的初始化器，对ApplicationContext做扩展
         * 调用ApplicationContext的refresh方法
         */
        Arrays.stream(context.getBeanDefinitionNames()).forEach(beanName -> {
            //打印bean的来源
            String description = context.getBeanFactory().getBeanDefinition(beanName).getResourceDescription();
            log.info("======name:{} , 来源:{}", beanName, description);
        });

        context.close();

    }

    public static class Bean1 {

    }

    public static class Bean2 {

    }

    public static class Bean3 {

    }

    @Bean
    public Bean2 bean2() {
        return new Bean2();
    }


    @Bean
    public TomcatServletWebServerFactory tomcatServletWebServerFactory() {
        return new TomcatServletWebServerFactory();
    }


}

package com.itheima.study.a39;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.boot.*;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.reactive.context.AnnotationConfigReactiveWebServerApplicationContext;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.ClassPathResource;

import java.util.Arrays;
import java.util.Set;

/**
 * 这一讲讲了2，以及8-12步，首先是8，创建容器，然后是9，使用初始化器增强容器，然后是10，读取不同来源的bean的定义，来自于配置类，xml配置文件，或者包扫描，
 * 一旦BeanDefinition准备完成，就是11步，调用refresh()方法，让整个容器初始化完成，在方法内部会把所有的单例初始化好
 *
 * @author grzha
 */
@Slf4j
public class A39_3 {

    public static void main(String[] args) throws Exception {
        //这个是模拟获得系统的参数，这个会在12步使用，可以在idea中配置
        args = new String[]{"--server.port=8080", "debug"};

        SpringApplication application = new SpringApplication();
        //添加容器的初始化器
        application.addInitializers(new ApplicationContextInitializer<ConfigurableApplicationContext>() {
            @Override
            public void initialize(ConfigurableApplicationContext applicationContext) {
                log.info("======执行初始化器增强");
            }
        });

        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>2.封装启动 args");
        DefaultApplicationArguments arguments = new DefaultApplicationArguments(args);

        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>8.创建容器");
        GenericApplicationContext context = createApplicationContext(WebApplicationType.SERVLET);

        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>9.准备容器");
        //执行容器的初始化器的初始化方法
        application.getInitializers().stream().forEach(applicationContextInitializer -> {
            ApplicationContextInitializer initializer = (ApplicationContextInitializer) applicationContextInitializer;
            initializer.initialize(context);
        });

        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>10.加载bean定义");
        //读取BeanDefinition的读取器，读取的bean定义存放在context.getDefaultListableBeanFactory()容器中
        DefaultListableBeanFactory beanFactory = context.getDefaultListableBeanFactory();
        //读取配置类中的bean的定义，标注@Configuration的类
        AnnotatedBeanDefinitionReader reader = new AnnotatedBeanDefinitionReader(beanFactory);
        //开始读取bean定义，放入配置类Config.class
        reader.register(Config.class);
        //读取xml中的bean的配置
        XmlBeanDefinitionReader reader1 = new XmlBeanDefinitionReader(beanFactory);
        reader1.loadBeanDefinitions(new ClassPathResource("b03.xml"));
        //通过扫描的方式读取bean的配置
        ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(beanFactory);
        scanner.scan("com.itheima.study.a39.sub");

        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>11.refresh容器");
        context.refresh();

        Arrays.stream(context.getBeanDefinitionNames()).forEach(beanName -> {
            log.info("======name:{}, 来源:{}", beanName, context.getBeanFactory().getBeanDefinition(beanName).getResourceDescription());
        });

        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>12.执行runner");
        for (CommandLineRunner runner : context.getBeansOfType(CommandLineRunner.class).values()) {
            runner.run(args);
        }
        for (ApplicationRunner runner : context.getBeansOfType(ApplicationRunner.class).values()) {
            runner.run(arguments);
        }

    }

    /**
     * 根据应用的类型，创建不同的ApplicationContext容器
     *
     * @param type
     * @return
     */
    private static GenericApplicationContext createApplicationContext(WebApplicationType type) {
        GenericApplicationContext context = null;
        switch (type) {
            case SERVLET:
                context = new AnnotationConfigServletWebServerApplicationContext();
                break;
            case REACTIVE:
                context = new AnnotationConfigReactiveWebServerApplicationContext();
                break;
            case NONE:
                context = new AnnotationConfigApplicationContext();
                break;
            default:
                break;
        }
        return context;
    }

    public static class Bean4 {

    }

    public static class Bean5 {

    }

    public static class Bean6 {

    }

    @Configuration
    static class Config {
        @Bean
        public Bean5 bean5() {
            return new Bean5();
        }

        @Bean
        public ServletWebServerFactory servletWebServerFactory() {
            return new TomcatServletWebServerFactory();
        }

        /**
         * 这个runner会在12步，执行runner时被调用
         *
         * @return
         */
        @Bean
        public CommandLineRunner commandLineRunner() {
            return args -> {
                log.info("======commandLineRunner(),args:{}", Arrays.toString(args));
            };
        }

        /**
         * 这个runner会在12步，执行runner时被调用
         *
         * @return
         */
        @Bean
        public ApplicationRunner applicationRunner() {
            return args -> {
                log.info("======applicationRunner(),args:{}", Arrays.toString(args.getSourceArgs()));
                //option的参数，指的是选项参数，就是一开始传进来，以--开头的参数
                Set<String> optionNames = args.getOptionNames();
                log.info("======optionNames:{}", optionNames);
                optionNames.stream().forEach(name -> log.info("======values:{}", args.getOptionValues(name)));
                //NonOptionArgs,指非选项参数，就是不以--开头的参数
                log.info("======NonOptionArgs:{}", args.getNonOptionArgs());
            };
        }
    }

}

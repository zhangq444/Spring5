package com.ithema.study.a02;

import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletRegistrationBean;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.mvc.Controller;

import java.util.Arrays;

/**
 * 这一讲讲解ApplicationContext的几个实现
 *
 * @author grzha
 */
public class A02Application {

    public static void main(String[] args) {
//        testClassPathXmlApplicationContext();
//        testFileSystemXmlApplicationContext();
        testAnnotationConfigApplicationContext();
//        testAnnotationConfigServletWebServerApplicationContext();

        /**
         * 下面的代码是演示ClassPathXmlApplicationContext内部是如何解析b01.xml，然后加入到容器中的
         */
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        System.out.println("======before");
        Arrays.stream(beanFactory.getBeanDefinitionNames()).forEach(System.out::println);
        //XmlBeanDefinitionReader可以读取xml中的bean的定义，然后转化为BeanDefinition，然后将读取的bean的定义加入到beanFactory中
        //所以读取之前，beanFactory中的BeanDefinition为空，读取之后，beanFactory中的BeanDefinition就有值了
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);
        reader.loadBeanDefinitions(new ClassPathResource("b01.xml"));
        System.out.println("======after");
        Arrays.stream(beanFactory.getBeanDefinitionNames()).forEach(System.out::println);


    }

    /**
     * 基于xml配置文件的容器
     */
    public static void testClassPathXmlApplicationContext() {
        ClassPathXmlApplicationContext xmlApplicationContext = new ClassPathXmlApplicationContext("b01.xml");
        Arrays.stream(xmlApplicationContext.getBeanDefinitionNames()).forEach(System.out::println);
        System.out.println(xmlApplicationContext.getBean(Bean2.class).getBean1());
    }

    /**
     * 基于磁盘路径下xml配置文件的容器
     */
    public static void testFileSystemXmlApplicationContext() {
        FileSystemXmlApplicationContext context = new FileSystemXmlApplicationContext("D:\\ideastudyspace\\spring5gaoji\\bean\\src\\main\\resources\\b01.xml");
        Arrays.stream(context.getBeanDefinitionNames()).forEach(System.out::println);
        System.out.println(context.getBean(Bean2.class).getBean1());
    }

    /**
     * 基于java配置类创建的容器，就是注解式的配置
     */
    public static void testAnnotationConfigApplicationContext() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
        Arrays.stream(context.getBeanDefinitionNames()).forEach(System.out::println);
        System.out.println(context.getBean(Bean2.class).getBean1());
    }

    /**
     * 基于servlet技术的web容器的实现,基于java配置类创建，用于web环境
     */
    public static void testAnnotationConfigServletWebServerApplicationContext() {
        AnnotationConfigServletWebServerApplicationContext context = new AnnotationConfigServletWebServerApplicationContext(WebConfig.class);
    }


    @Configuration
    static class WebConfig {

        @Bean
        public ServletWebServerFactory servletWebServerFactory() {
            return new TomcatServletWebServerFactory();
        }

        @Bean
        public DispatcherServlet dispatcherServlet() {
            return new DispatcherServlet();
        }

        @Bean
        public DispatcherServletRegistrationBean registrationBean(DispatcherServlet dispatcherServlet) {
            return new DispatcherServletRegistrationBean(dispatcherServlet, "/");
        }

        @Bean("/hello")
        public Controller controller1() {
            /*return new Controller() {
                @Override
                public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
                    return null;
                }
            }*/
            return (request, response) -> {
                response.getWriter().println("hello");
                return null;
            };
        }

    }


    @Configuration
    static class Config {
        @Bean
        public Bean1 getBean1() {
            return new Bean1();
        }

        @Bean
        public Bean2 getBean2(Bean1 bean1) {
            Bean2 bean2 = new Bean2();
            bean2.setBean1(bean1);
            return bean2;
        }

    }


    static class Bean1 {

    }

    static class Bean2 {
        private Bean1 bean1;

        public Bean1 getBean1() {
            return bean1;
        }

        public void setBean1(Bean1 bean1) {
            this.bean1 = bean1;
        }
    }


}

package com.ithema.study.demo;

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
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

public class A02Application {

    public static void main(String[] args) {
//        testClassPathXmlApplicationContext();
//        testFileSystemXmlApplicationContext();
//        testAnnotationConfigApplicationContext();
        testAnnotationConfigServletWebServerApplicationContext();


        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        System.out.println("======before");
        Arrays.stream(beanFactory.getBeanDefinitionNames()).forEach(System.out::println);
        XmlBeanDefinitionReader reader=new XmlBeanDefinitionReader(beanFactory);
        reader.loadBeanDefinitions(new ClassPathResource("b01.xml"));
        System.out.println("======after");
        Arrays.stream(beanFactory.getBeanDefinitionNames()).forEach(System.out::println);


    }

    public static void testClassPathXmlApplicationContext(){
        ClassPathXmlApplicationContext xmlApplicationContext = new ClassPathXmlApplicationContext("b01.xml");
        Arrays.stream(xmlApplicationContext.getBeanDefinitionNames()).forEach(System.out::println);
        System.out.println(xmlApplicationContext.getBean(Bean2.class).getBean1());
    }

    public static void testFileSystemXmlApplicationContext(){
        FileSystemXmlApplicationContext context = new FileSystemXmlApplicationContext("D:\\ideastudyspace\\spring5gaoji\\bean\\src\\main\\resources\\b01.xml");
        Arrays.stream(context.getBeanDefinitionNames()).forEach(System.out::println);
        System.out.println(context.getBean(Bean2.class).getBean1());
    }


    public static void testAnnotationConfigApplicationContext(){
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
        Arrays.stream(context.getBeanDefinitionNames()).forEach(System.out::println);
        System.out.println(context.getBean(Bean2.class).getBean1());
    }

    public static void testAnnotationConfigServletWebServerApplicationContext(){
        AnnotationConfigServletWebServerApplicationContext context = new AnnotationConfigServletWebServerApplicationContext(WebConfig.class);
    }




    @Configuration
    static class WebConfig{

        @Bean
        public ServletWebServerFactory servletWebServerFactory(){
            return new TomcatServletWebServerFactory();
        }

        @Bean
        public DispatcherServlet dispatcherServlet(){
            return new DispatcherServlet();
        }

        @Bean
        public DispatcherServletRegistrationBean registrationBean(DispatcherServlet dispatcherServlet){
            return new DispatcherServletRegistrationBean(dispatcherServlet,"/");
        }

        @Bean("/hello")
        public Controller controller1(){
            return (request, response) -> {
                response.getWriter().println("hello");
                return null;
            };
        }

    }


    @Configuration
    static class Config{
        @Bean
        public Bean1 getBean1(){
            return new Bean1();
        }

        @Bean
        public Bean2 getBean2(Bean1 bean1){
            Bean2 bean2 = new Bean2();
            bean2.setBean1(bean1);
            return bean2;
        }

    }


    static class Bean1{

    }

    static class Bean2{
        private Bean1 bean1;

        public Bean1 getBean1() {
            return bean1;
        }

        public void setBean1(Bean1 bean1) {
            this.bean1 = bean1;
        }
    }





}

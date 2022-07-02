package com.itheima.study.a40;

import com.google.common.collect.Lists;
import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;
import org.apache.coyote.http11.Http11Nio2Protocol;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.io.File;
import java.nio.file.Files;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 这个类是讲了  内嵌的tomcat如何和spring整合，注册DispatcherServlet，然后启动tomcat，接受请求
 *
 * 在39讲中，第11步，applicationContext.refresh(); 调用了applicationContext的refresh()方法，
 * 然后点开refresh方法，看这个方法的一个实现类AbstractApplicationContext中，的refresh方法中
 * this.onRefresh();
 * 上面这一段代码，会创建好tomcat容器，也就是走下面的1-4的步骤
 * 然后再上面这段代码下面
 * this.finishRefresh();
 * 这段代码，就会走第5-6的步骤，启动tomcat容器
 *
 *
 * @author grzha
 */
public class TestTomcat2 {

    public static void main(String[] args) throws Exception {
        // 1.创建Tomcat对象
        Tomcat tomcat = new Tomcat();
        // 设置基础目录,Tomcat在工作时可能要产生一些临时文件等，需要存放在基础目录下面，这里用的是相对路径
        tomcat.setBaseDir("tomcat");

        // 2.创建项目文件夹，即docBase文件夹,这个目录就是用来放项目的资源的，例如class文件等等
        File docBase = Files.createTempDirectory("boot.").toFile();
        //当程序退出时，删除临时目录
        docBase.deleteOnExit();

        // 3.创建Tomcat项目，在Tomcat中称为Context
        //第一个参数指定虚拟路径，如果要设为/，就传""，第二个参数就是docBase的实际路径
        Context context = tomcat.addContext("", docBase.getAbsolutePath());

        WebApplicationContext springContext = getApplicationContext();

        // 4.编程添加Servlet
        context.addServletContainerInitializer(new ServletContainerInitializer() {
            @Override
            public void onStartup(Set<Class<?>> set, ServletContext servletContext) throws ServletException {
                HelloServlet helloServlet = new HelloServlet();
                servletContext.addServlet("MyServlet", helloServlet).addMapping("/hello");
                //注册spring容器中的DispatcherServlet
                /*DispatcherServlet dispatcherServlet = springContext.getBean(DispatcherServlet.class);
                servletContext.addServlet("dispatcherServlet",dispatcherServlet).addMapping("/");*/
                //可以获得所有的ServletRegistrationBean（这个是DispatcherServletRegistrationBean的父类）,然后调用onStartup，可以注册所有的servlet，
                //包括DispatcherServlet和其他servlet
                for (ServletRegistrationBean registrationBean : springContext.getBeansOfType(ServletRegistrationBean.class).values()) {
                    registrationBean.onStartup(servletContext);
                }
            }
        }, Collections.emptySet());

        // 5.启动Tomcat
        tomcat.start();

        // 6.创建连接器，设置监听端口
        //Http11Nio2Protocol是指定协议
        Connector connector = new Connector(new Http11Nio2Protocol());
        //设置监听的端口
        connector.setPort(8080);
        tomcat.setConnector(connector);

    }

    public static WebApplicationContext getApplicationContext(){
        /**
         * AnnotationConfigServletWebServerApplicationContext  没有使用这个容器是因为这个容器已经支持内嵌的tomcat
         * 下面这个容器不支持内嵌tomcat，我们自己在上面写了内嵌的tomcat
          */
        AnnotationConfigWebApplicationContext context=new AnnotationConfigWebApplicationContext();
        //指定配置类
        context.register(Config.class);
        //调用refresh方法，完成容器的初始化
        context.refresh();
        return context;
    }

    @Configuration
    static class Config{
        @Bean
        public DispatcherServletRegistrationBean registrationBean(DispatcherServlet dispatcherServlet){
            return new DispatcherServletRegistrationBean(dispatcherServlet,"/");
        }

        @Bean
        public DispatcherServlet dispatcherServlet(WebApplicationContext applicationContext){
            return new DispatcherServlet(applicationContext);
        }

        @Bean
        public RequestMappingHandlerAdapter requestMappingHandlerAdapter(){
            RequestMappingHandlerAdapter adapter = new RequestMappingHandlerAdapter();
            adapter.setMessageConverters(List.of(new MappingJackson2HttpMessageConverter()));
            return adapter;
        }

        @RestController
        static class MyController{
            @GetMapping("hello2")
            public Map<String,Object> hello(){
                return Map.of("hello2","hello2,spring!");
            }
        }


    }





}

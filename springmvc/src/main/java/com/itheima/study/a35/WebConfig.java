package com.itheima.study.a35;

import com.google.common.collect.Lists;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletRegistrationBean;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
import org.springframework.web.servlet.mvc.HttpRequestHandlerAdapter;
import org.springframework.web.servlet.resource.CachingResourceResolver;
import org.springframework.web.servlet.resource.EncodedResourceResolver;
import org.springframework.web.servlet.resource.PathResourceResolver;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import java.util.List;
import java.util.Map;

/**
 * @author grzha
 */
@Configuration
public class WebConfig {


    /**
     * 内嵌web容器
     *
     * @return
     */
    @Bean
    public TomcatServletWebServerFactory tomcatServletWebServerFactory() {
        return new TomcatServletWebServerFactory(8080);
    }

    /**
     * 创建DispatcherServlet
     *
     * @return
     */
    @Bean
    public DispatcherServlet dispatcherServlet() {
        return new DispatcherServlet();
    }

    /**
     * 注册DispatcherServlet到web容器，SpringMVC的入口
     *
     * @param dispatcherServlet
     * @return
     */
    @Bean
    public DispatcherServletRegistrationBean dispatcherServletRegistrationBean(DispatcherServlet dispatcherServlet) {
        return new DispatcherServletRegistrationBean(dispatcherServlet, "/");
    }

    /**
     * 处理静态资源的处理器映射器
     *
     * @return
     */
    @Bean
    public SimpleUrlHandlerMapping simpleUrlHandlerMapping(ApplicationContext context) {
        SimpleUrlHandlerMapping handlerMapping = new SimpleUrlHandlerMapping();
        //这个map的key就是下面@Bean注解中的/**和/img/**,value就是静态资源处理器
        Map<String, ResourceHttpRequestHandler> map = context.getBeansOfType(ResourceHttpRequestHandler.class);
        handlerMapping.setUrlMap(map);
        System.out.println(map);
        return handlerMapping;
    }


    /**
     * 处理静态资源的处理器适配器，和上面一个配套使用
     *
     * @return
     */
    @Bean
    public HttpRequestHandlerAdapter httpRequestHandlerAdapter() {
        return new HttpRequestHandlerAdapter();
    }

    /**
     * 处理静态资源的控制器
     * /**是匹配路径，这样例如请求/index.html就会被这个/**匹配，然后就会去static目录下面去找了
     *
     * @return
     */
    @Bean("/**")
    public ResourceHttpRequestHandler handler1() {
        ResourceHttpRequestHandler handler = new ResourceHttpRequestHandler();
        //下面这个类路径static/一定要加/，这样就会被识别为一个目录，否则会被识别为一个文件
        handler.setLocations(Lists.newArrayList(new ClassPathResource("static/")));
        //增加功能更强的资源解析器,（这个缓存自己测试没有成功）（然后压缩这个，需要写一段代码自己压缩html，就看了老师视频，也没有测试）(感觉加强功能不是很重要，所以不试了)
        handler.setResourceResolvers(List.of(
                //这个资源解析器能够带有缓存的功能，ConcurrentMapCache是基于ConcurrentMap的缓存，"cache1"是起个名字
                new CachingResourceResolver(new ConcurrentMapCache("cache1")),
                //这个资源解析器可以读取压缩的资源
                new EncodedResourceResolver(),
                //这个是默认的资源解析器，当为空时，spring默认加这个，这个就是根据路径去找对应的资源，最基本的功能
                new PathResourceResolver()
        ));
        return handler;
    }

    /**
     * 匹配路径/img/**,那么以后请求路径是/img/1.jpg这样的请求，就会去image这个目录下面去找了
     *
     * @return
     */
    @Bean("/img/**")
    public ResourceHttpRequestHandler handler2() {
        ResourceHttpRequestHandler handler = new ResourceHttpRequestHandler();
        handler.setLocations(Lists.newArrayList(new ClassPathResource("image/")));
        return handler;
    }

    /**
     * 本来老师视频里面有一个自定义的欢迎页面的，就是如果请求就是http://localhost:8080，会去欢迎也，需要配置一个bean，就是WelcomePageHandlerMapping
     * 但是发现这个类不是public的，同时又是final的，就是既不能继承又不能引用，不知道老师为什么可以配置这个bean，这个就没有搞定
     */
/*    @Bean
    public WelcomePageHandlerMapping welcomePageHandlerMapping(ApplicationContext context) throws Exception {
        Class<?> clazz = Class.forName("org.springframework.boot.autoconfigure.web.servlet.WelcomePageHandlerMapping");
        Constructor<?> constructor = clazz.getConstructor(TemplateAvailabilityProviders.class, ApplicationContext.class, Resource.class, String.class);
        Resource resource = context.getResource("classpath:static/index.html");
        WelcomePageHandlerMapping instance = constructor.newInstance(null, context, resource, "/");
    }*/


}

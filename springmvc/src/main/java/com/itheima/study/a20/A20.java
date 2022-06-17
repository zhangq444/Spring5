package com.itheima.study.a20;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.List;
import java.util.Map;

/**
 * 主要讲了一下DispatcherServlet的初始化，以及请求过来，先通过RequestMappingHandlerMapping来判断应该是哪个控制器来处理
 * 然后由RequestMappingHandlerAdapter来调用控制器方法处理请求，
 * 同时讲解了自定义的参数解析器和返回值处理器
 */
@Slf4j
public class A20 {

    public static void main(String[] args) throws Exception {
        //这个是注解配置的含有内嵌web容器(包含Tomcat)的spring容器
        AnnotationConfigServletWebServerApplicationContext context = new AnnotationConfigServletWebServerApplicationContext(WebConfig.class);
        //作用，解析@RequestMapping注解以及派生的注解(例如GetMapping)，生成路径与控制器方法的映射关系,在初始化时就生成了
        RequestMappingHandlerMapping handlerMapping = context.getBean(RequestMappingHandlerMapping.class);
        //获取映射结果，map的key封装了请求路径以及方式等信息，map的value封装了控制器方法，譬如是哪个类的哪个方法
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = handlerMapping.getHandlerMethods();
        handlerMethods.forEach((key, value) -> {
            log.info("======key:{} --- value:{}", key, value);
        });

        //模拟请求来了，获取控制器方法，返回处理器执行链对象
        //MockHttpServletRequest是spring提供的模拟request的
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/test1");
        MockHttpServletResponse response = new MockHttpServletResponse();
        HandlerExecutionChain handler = handlerMapping.getHandler(request);
        log.info("======handler:{}", handler);

        /**
         * 使用处理器适配器，调用处理器映射器所映射的控制器（就是controller的方法）
         */
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        MyRequestMappingHandlerAdapter handlerAdapter = context.getBean(MyRequestMappingHandlerAdapter.class);
        MockHttpServletRequest request1 = new MockHttpServletRequest("POST", "/test2");
        request1.setParameter("name", "张强");
        HandlerExecutionChain chain = handlerMapping.getHandler(request1);
        handlerAdapter.invokeHandlerMethod(request1, response, (HandlerMethod) chain.getHandler());

        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> 打印所有的参数解析器");
        List<HandlerMethodArgumentResolver> argumentResolvers = handlerAdapter.getArgumentResolvers();
        argumentResolvers.forEach(handlerMethodArgumentResolver -> {
            log.info("======resolver:{}", handlerMethodArgumentResolver);
        });

        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> 打印所有的返回值解析器");
        List<HandlerMethodReturnValueHandler> returnValueHandlers = handlerAdapter.getReturnValueHandlers();
        returnValueHandlers.forEach(handlerMethodReturnValueHandler -> {
            log.info("======handler:{}", handlerMethodReturnValueHandler);
        });

        /**
         * 调用增加了自定义的参数解析器的控制器,在配置文件中配置了
         */
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        MockHttpServletRequest request2 = new MockHttpServletRequest("PUT", "/test3");
        request2.setParameter("name", "张强");
        request2.addHeader("token", "某个令牌123456");
        HandlerExecutionChain chain2 = handlerMapping.getHandler(request2);
        handlerAdapter.invokeHandlerMethod(request2, response, (HandlerMethod) chain2.getHandler());

        /**
         * 调用增加了自定义的返回值解析器的控制器,在配置文件中配置了
         */
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        MockHttpServletRequest request3 = new MockHttpServletRequest("GET", "/test4.yml");
        MockHttpServletResponse response1 = new MockHttpServletResponse();
        HandlerExecutionChain chain3 = handlerMapping.getHandler(request3);
        handlerAdapter.invokeHandlerMethod(request3, response1, (HandlerMethod) chain3.getHandler());
        String contentAsString = response1.getContentAsString();
        log.info("======contentAsString:{}", contentAsString);

    }


}

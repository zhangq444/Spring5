package com.itheima.study.a32;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * 这一讲主要讲了异常处理，31讲的异常处理只能够处理mvc内部的异常，如果是过滤器的异常就没法处理了，这就需要使用tomcat容器的异常处理了
 *
 * @author grzha
 */
@Slf4j
public class A32 {

    public static void main(String[] args) {
        //注意创建的容器，和上一节课不一样
        AnnotationConfigServletWebServerApplicationContext context = new AnnotationConfigServletWebServerApplicationContext(WebConfig.class);
        RequestMappingHandlerMapping handlerMapping = context.getBean(RequestMappingHandlerMapping.class);
        handlerMapping.getHandlerMethods().forEach((requestMappingInfo, handlerMethod) -> {
            log.info("映射路径：{}，方法信息：{}", requestMappingInfo, handlerMethod);
        });


    }


}

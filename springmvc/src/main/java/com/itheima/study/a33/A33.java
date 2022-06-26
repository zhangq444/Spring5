package com.itheima.study.a33;

import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;

/**
 * 这一讲介绍了BeanNameUrlHandlerMapping和SimpleControllerHandlerAdapter，这两个处理器映射器和处理器适配器，老师说这个是springmvc早期的实现方式
 *
 * @author grzha
 */
public class A33 {

    public static void main(String[] args) {
        AnnotationConfigServletWebServerApplicationContext context = new AnnotationConfigServletWebServerApplicationContext(WebConfig.class);
    }


}

package com.itheima.study.a34;

import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;

/**
 * 这一讲介绍了RouterFunctionMapping和HandlerFunctionAdapter这一组新的，在spring5.2后面加入的处理器映射器和处理器适配器
 *
 * @author grzha
 */
public class A34 {

    public static void main(String[] args) {
        AnnotationConfigServletWebServerApplicationContext context = new AnnotationConfigServletWebServerApplicationContext(WebConfig.class);


    }


}

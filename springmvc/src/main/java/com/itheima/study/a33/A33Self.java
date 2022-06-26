package com.itheima.study.a33;

import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;

/**
 * 这一讲还自定义了一组处理器映射器和处理器适配器，在WebConfigSelf中
 *
 * @author grzha
 */
public class A33Self {

    public static void main(String[] args) {
        AnnotationConfigServletWebServerApplicationContext context = new AnnotationConfigServletWebServerApplicationContext(WebConfigSelf.class);
    }


}

package com.itheima.study.a31;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;

import java.nio.charset.StandardCharsets;

/**
 * 这一讲讲了ControllerAdvice中设置全局的异常处理
 *
 * @author grzha
 */
public class A31 {

    public static void main(String[] args) throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(WebConfig.class);
        ExceptionHandlerExceptionResolver resolver = context.getBean(ExceptionHandlerExceptionResolver.class);

        //这个时候，他会优先去找Controller5中是否有@ExceptionHandler的方法，如果没有就会去@ControllerAdvice标注的类中去找，找到就是全局的异常处理
        //这个全局的异常处理也是在afterPropertiesSet方法中的initExceptionHandlerAdviceCache方法中初始化的
        HandlerMethod handlerMethod = new HandlerMethod(new Controller5(), Controller5.class.getMethod("foo"));
        Exception e = new Exception("e1");
        resolver.resolveException(request, response, handlerMethod, e);
        System.out.println(response.getContentAsString(StandardCharsets.UTF_8));


    }


    public static class Controller5 {
        public void foo() {
        }
    }


}

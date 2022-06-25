package com.itheima.study.a30;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * 这一讲主要讲了异常处理，异常处理器方法来处理异常
 *
 * @author grzha
 */
public class A30 {

    public static void main(String[] args) throws Exception {
        ExceptionHandlerExceptionResolver resolver = new ExceptionHandlerExceptionResolver();
        //给异常处理器设置消息转换器，用来处理解决异常的方法的返回值的
        resolver.setMessageConverters(Lists.newArrayList(new MappingJackson2HttpMessageConverter()));
        //调用下面的方法，可以让它添加默认的参数解析器和返回值处理器,可以看看afterPropertiesSet这个方法里面
        resolver.afterPropertiesSet();

        //第一种json的方式
        //模拟请求和响应
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        //模拟处理器handler
        HandlerMethod handlerMethod = new HandlerMethod(new Controller1(), Controller1.class.getMethod("foo"));
        //模拟捕获的异常
        Exception e = new ArithmeticException("被零除");
        //开始处理异常,处理的过程是，首先由于有HandlerMethod，所以他知道抛出异常所在的类是哪一个，然后再看这个类中是否有被@ExceptionHandler标注的方法
        //如果有这种方法，检查这个方法的参数，看处理的异常类型是否匹配，如果匹配，就反射调用这个方法，然后获得返回值，再将返回值通过返回值处理器处理，例如处理
        //成json格式字符串，然后返回到response中
        resolver.resolveException(request, response, handlerMethod, e);
        System.out.println(response.getContentAsString(StandardCharsets.UTF_8));

        //第二种ModelAndView的方式
        MockHttpServletRequest request1 = new MockHttpServletRequest();
        MockHttpServletResponse response1 = new MockHttpServletResponse();
        HandlerMethod handlerMethod1 = new HandlerMethod(new Controller2(), Controller2.class.getMethod("foo"));
        ModelAndView modelAndView = resolver.resolveException(request1, response1, handlerMethod1, e);
        System.out.println(modelAndView.getModel());
        System.out.println(modelAndView.getViewName());

        //第三种，嵌套异常的情况
        MockHttpServletRequest request2 = new MockHttpServletRequest();
        MockHttpServletResponse response2 = new MockHttpServletResponse();
        HandlerMethod handlerMethod2 = new HandlerMethod(new Controller3(), Controller3.class.getMethod("foo"));
        Exception e2 = new Exception("e1", new RuntimeException("e2", new IOException("e3")));
        resolver.resolveException(request2, response2, handlerMethod2, e2);
        System.out.println(response2.getContentAsString(StandardCharsets.UTF_8));

        //第四种，测试异常处理方法参数解析,解析异常方法的参数HttpServletRequest，就是在前面afterPropertiesSet方法中的默认的参数解析器来解析的
        MockHttpServletRequest request3 = new MockHttpServletRequest();
        MockHttpServletResponse response3 = new MockHttpServletResponse();
        HandlerMethod handlerMethod3 = new HandlerMethod(new Controller4(), Controller4.class.getMethod("foo"));
        Exception e3 = new Exception("测试情况4");
        resolver.resolveException(request3, response3, handlerMethod3, e3);
        System.out.println(response3.getContentAsString(StandardCharsets.UTF_8));


    }

    public static class Controller1 {
        public void foo() {
        }

        @ExceptionHandler
        @ResponseBody
        public Map<String, Object> handle(ArithmeticException e) {
            Map<String, Object> result = Maps.newHashMap();
            result.put("error", e.getMessage());
            return result;
        }
    }

    public static class Controller2 {
        public void foo() {

        }

        @ExceptionHandler
        public ModelAndView handle(ArithmeticException e) {
            Map<String, Object> result = Maps.newHashMap();
            result.put("error", e.getMessage());
            return new ModelAndView("test2", result);
        }
    }

    public static class Controller3 {
        public void foo() {

        }

        @ExceptionHandler
        @ResponseBody
        public Map<String, Object> handle(IOException e) {
            Map<String, Object> result = Maps.newHashMap();
            result.put("error", e.getMessage());
            return result;
        }
    }

    public static class Controller4 {
        public void foo() {

        }

        @ExceptionHandler
        @ResponseBody
        public Map<String, Object> handle(Exception e, HttpServletRequest request) {
            System.out.println(request);
            Map<String, Object> result = Maps.newHashMap();
            result.put("error", e.getMessage());
            return result;
        }
    }


}

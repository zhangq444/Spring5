package com.itheima.study.a23;

import com.google.common.collect.Lists;
import lombok.Data;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.bind.ServletRequestParameterPropertyValues;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.support.ConfigurableWebBindingInitializer;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.support.InvocableHandlerMethod;
import org.springframework.web.servlet.mvc.method.annotation.ServletRequestDataBinderFactory;

import java.util.Date;

/**
 * 这一讲是用来将使用各种转换器，将请求中的参数，转化为controller的方法中的参数对象的
 */
public class TestServletDataBinderFactory {

    public static void main(String[] args) throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("birthday", "1999|01|02");
        request.setParameter("address.name", "西安");

        User target = new User();
//        ServletRequestDataBinder binder = new ServletRequestDataBinder(target);
        //1.用工厂，无转换，原始的，没有扩展功能的
        /*ServletRequestDataBinderFactory factory = new ServletRequestDataBinderFactory(null,null);*/

        // 2.用@InitBinder 转换
        //需要将自定义的转换器封装在一个InvocableHandlerMethod里面,将来会反射调用aaa方法，所以要把对象，和类的方法对象method传给他
        /*InvocableHandlerMethod method = new InvocableHandlerMethod(new MyController(),MyController.class.getMethod("aaa", WebDataBinder.class));
        ServletRequestDataBinderFactory factory = new ServletRequestDataBinderFactory(Lists.newArrayList(method),null);*/

        // 3.用ConversionService 转换
        /*FormattingConversionService service = new FormattingConversionService();
        service.addFormatter(new MyDateFormatter("用ConversionService方式实现"));

        ConfigurableWebBindingInitializer initializer = new ConfigurableWebBindingInitializer();
        initializer.setConversionService(service);
        ServletRequestDataBinderFactory factory = new ServletRequestDataBinderFactory(null, initializer);*/

        // 4.同时使用@InitBinder 和ConversionService转换
        /*InvocableHandlerMethod method = new InvocableHandlerMethod(new MyController(),MyController.class.getMethod("aaa", WebDataBinder.class));

        FormattingConversionService service = new FormattingConversionService();
        service.addFormatter(new MyDateFormatter("用ConversionService方式实现"));

        ConfigurableWebBindingInitializer initializer = new ConfigurableWebBindingInitializer();
        initializer.setConversionService(service);
        ServletRequestDataBinderFactory factory = new ServletRequestDataBinderFactory(Lists.newArrayList(method), initializer);*/

        // 5.使用默认ConversionService 转换,要配合@DateTimeFormat这个注解使用
        DefaultFormattingConversionService service = new DefaultFormattingConversionService();

        ConfigurableWebBindingInitializer initializer = new ConfigurableWebBindingInitializer();
        initializer.setConversionService(service);
        ServletRequestDataBinderFactory factory = new ServletRequestDataBinderFactory(null, initializer);

        WebDataBinder binder = factory.createBinder(new ServletWebRequest(request), target, "user");
        //我的理解，binder是转换器，绑定目标对象，将参数类型转换，ServletRequestParameterPropertyValues是用来获取request的参数的
        //是数据源，将数据源和转换器连接，然后转换器会将数据源中的参数绑定到目标对象中去
        binder.bind(new ServletRequestParameterPropertyValues(request));
        System.out.println(target);


    }

    static class MyController {
        @InitBinder
        public void aaa(WebDataBinder binder) {
            //扩展binder转换器
            binder.addCustomFormatter(new MyDateFormatter("用@InitBinder方式扩展"));

        }
    }


    @Data
    @ToString
    public static class User {
        @DateTimeFormat(pattern = "yyyy|MM|dd")
        private Date birthday;
        private Address address;
    }


    @Data
    @ToString
    public static class Address {
        private String name;
    }


}

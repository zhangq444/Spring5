package com.itheima.study.a25;

import com.google.common.collect.Lists;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.annotation.ExpressionValueMethodArgumentResolver;
import org.springframework.web.method.annotation.RequestHeaderMethodArgumentResolver;
import org.springframework.web.method.annotation.RequestParamMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolverComposite;
import org.springframework.web.method.support.HandlerMethodReturnValueHandlerComposite;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.*;

/**
 *  RequestMappingHandlerAdapter最重要的功能是执行控制器方法，所需的参数是HandlerMethod，HandlerMethod就代表了控制器方法，包含了一个bean和一个method这两个成员变量
 *  bean就是哪个Controller，method就是Controller中的哪个方法
 *  ServletInvocableHandlerMethod这个东西是HandlerMethod的子类，将来RequestMappingHandlerAdapter执行控制器方法，调用的就是这个ServletInvocableHandlerMethod，
 *  他继承了父类，有bean和method，同时包含4个部分，WebDataBinderFactory负责对象绑定、类型转换，ParameterNameDiscoverer负责参数名解析，
 *  HandlerMethodArgumentResolverComposite负责解析参数，HandlerMethodReturnValueHandlerComposite负责处理返回值
 *
 *
 */
public class A25 {

    public static void main(String[] args) throws Exception {
        AnnotationConfigApplicationContext context=new AnnotationConfigApplicationContext(WebConfig.class);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("name","张三");

        //创建ServletInvocableHandlerMethod对象需要两个参数，一个是控制器对象，一个是控制器的方法对象
        ServletInvocableHandlerMethod handlerMethod=new ServletInvocableHandlerMethod(new WebConfig.Controller1(),WebConfig.Controller1.class.getMethod("foo", WebConfig.User.class));

        ServletRequestDataBinderFactory factory = new ServletRequestDataBinderFactory(null,null);
        //设置数据绑定器
        handlerMethod.setDataBinderFactory(factory);
        //设置参数名解析器
        handlerMethod.setParameterNameDiscoverer(new DefaultParameterNameDiscoverer());
        //设置参数解析器
        handlerMethod.setHandlerMethodArgumentResolvers(getArgumentResolvers(context));

        //开始调用控制器方法
        ModelAndViewContainer container = new ModelAndViewContainer();
        handlerMethod.invokeAndHandle(new ServletWebRequest(request),container);

        /**
         * 执行过程，首先通过HandlerMethodArgumentResolverComposite参数解析器，找到ServletModelAttributeMethodProcessor这个参数解析器，去解析Controller中foo方法的参数，看是什么类型的参数
         * 然后通过ServletRequestDataBinderFactory，数据绑定器工厂，将参数绑定到foo方法中的user对象中，在这个过程中，会将参数对象加入到container中，名字是类名的小写，可以打印container中的数据看看，
         * 然后就是执行控制器方法
         */



        context.close();


    }

    /**
     * 添加常见的参数解析器
     * @param context
     * @return
     */
    public static HandlerMethodArgumentResolverComposite getArgumentResolvers(AnnotationConfigApplicationContext context){
        HandlerMethodArgumentResolverComposite composite = new HandlerMethodArgumentResolverComposite();
        composite.addResolvers(
                new RequestParamMethodArgumentResolver(context.getDefaultListableBeanFactory(),false),
                new PathVariableMethodArgumentResolver(),
                new RequestHeaderMethodArgumentResolver(context.getDefaultListableBeanFactory()),
                new ServletCookieValueMethodArgumentResolver(context.getDefaultListableBeanFactory()),
                new ExpressionValueMethodArgumentResolver(context.getDefaultListableBeanFactory()),
                new ServletRequestMethodArgumentResolver(),
                new ServletModelAttributeMethodProcessor(false),
                new RequestResponseBodyMethodProcessor(Lists.newArrayList(new MappingJackson2HttpMessageConverter())),
                new ServletModelAttributeMethodProcessor(true),
                new RequestParamMethodArgumentResolver(context.getDefaultListableBeanFactory(),true)
        );
        return composite;
    }




}

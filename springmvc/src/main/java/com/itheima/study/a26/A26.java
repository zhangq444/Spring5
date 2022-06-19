package com.itheima.study.a26;

import com.google.common.collect.Lists;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.annotation.ExpressionValueMethodArgumentResolver;
import org.springframework.web.method.annotation.ModelFactory;
import org.springframework.web.method.annotation.RequestHeaderMethodArgumentResolver;
import org.springframework.web.method.annotation.RequestParamMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolverComposite;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.*;

import java.lang.reflect.Method;

/**
 *  这一讲是对上一讲的扩展，内容不是很重要，主要讲了@ModelAttribute，这个注解可以加在参数上，也可以加在方法上，然后解析对象不一样，加在方法上和加在@ControllerAdvice中
 *  是由RequestMappingHandlerAdapter来解析，加在参数上，是由ServletModelAttributeMethodProcessor这个参数解析器解析，
 *  加在方法上的话，就会在ModelFactory初始化模型数据的时候，将加了@ModelAttribute的方法的返回值存入到ModelAndViewContainer容器中，作为一个模型数据
 *  好像不是很重要的内容，用的也不多，没有见到用过
 *
 *
 */
public class A26 {

    public static void main(String[] args) throws Exception {
        AnnotationConfigApplicationContext context=new AnnotationConfigApplicationContext(com.itheima.study.a26.WebConfig.class);

        //创建并初始化处理器适配器
        //在初始化时，就会解析@ControllerAdvice中的@ModelAttribute标注的方法，并且记录下来
        RequestMappingHandlerAdapter adapter = new RequestMappingHandlerAdapter();
        adapter.setApplicationContext(context);
        adapter.afterPropertiesSet();


        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("name","张三");

        //创建ServletInvocableHandlerMethod对象需要两个参数，一个是控制器对象，一个是控制器的方法对象
        ServletInvocableHandlerMethod handlerMethod=new ServletInvocableHandlerMethod(new com.itheima.study.a26.WebConfig.Controller1(), com.itheima.study.a26.WebConfig.Controller1.class.getMethod("foo", WebConfig.User.class));

        ServletRequestDataBinderFactory factory = new ServletRequestDataBinderFactory(null,null);
        //设置数据绑定器
        handlerMethod.setDataBinderFactory(factory);
        //设置参数名解析器
        handlerMethod.setParameterNameDiscoverer(new DefaultParameterNameDiscoverer());
        //设置参数解析器
        handlerMethod.setHandlerMethodArgumentResolvers(getArgumentResolvers(context));

        //开始调用控制器方法
        ModelAndViewContainer container = new ModelAndViewContainer();

        //获取模型工厂方法
        Method getModelFactory = RequestMappingHandlerAdapter.class.getDeclaredMethod("getModelFactory", HandlerMethod.class, WebDataBinderFactory.class);
        getModelFactory.setAccessible(true);
        ModelFactory modelFactory = (ModelFactory) getModelFactory.invoke(adapter, handlerMethod, factory);
        //初始化模型数据，这个时候就会调用前面记录的@ControllerAdvice中的@ModelAttribute标注的方法，然后把返回值存入container中
        modelFactory.initModel(new ServletWebRequest(request),container,handlerMethod);


        handlerMethod.invokeAndHandle(new ServletWebRequest(request),container);

        /**
         * 执行过程，首先通过HandlerMethodArgumentResolverComposite参数解析器，找到ServletModelAttributeMethodProcessor这个参数解析器，去解析Controller中foo方法的参数，看是什么类型的参数
         * 然后通过ServletRequestDataBinderFactory，数据绑定器工厂，将参数绑定到foo方法中的user对象中，在这个过程中，会将参数对象加入到container中，名字是类名的小写，可以打印container中的数据看看，
         * 然后就是执行控制器方法
         */
        ModelMap model = container.getModel();
        System.out.println(model);


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

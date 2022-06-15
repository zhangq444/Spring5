package com.itheima.study.a20;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 因为RequestMappingHandlerAdapter里面的invokeHandlerMethod这个方法是受保护的，
 * 所以写一个子类继承RequestMappingHandlerAdapter，然后重新invokeHandlerMethod方法，调用父类的方法，然后把
 * 方法改成public的，这样就可以调用了
 */
public class MyRequestMappingHandlerAdapter extends RequestMappingHandlerAdapter {

    @Override
    public ModelAndView invokeHandlerMethod(HttpServletRequest request, HttpServletResponse response, HandlerMethod handlerMethod) throws Exception {
        return super.invokeHandlerMethod(request, response, handlerMethod);
    }
}

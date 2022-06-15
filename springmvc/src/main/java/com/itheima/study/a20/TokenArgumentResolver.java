package com.itheima.study.a20;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Objects;

/**
 * 自定义的Token参数解析器
 */
public class TokenArgumentResolver implements HandlerMethodArgumentResolver {
    /**
     * 这个是判断控制器方法的参数是否满足某个条件，满足了条件，返回true，就会执行resolveArgument这个方法
     * @param parameter
     * @return
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        Token token = parameter.getParameterAnnotation(Token.class);
        return Objects.nonNull(token);
    }

    /**
     * 解析参数的方法
     * @param parameter
     * @param mavContainer
     * @param webRequest
     * @param binderFactory
     * @return
     * @throws Exception
     */
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String token = webRequest.getHeader("token");
        return token;
    }
}

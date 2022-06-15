package com.itheima.study.a20;

import org.springframework.core.MethodParameter;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.yaml.snakeyaml.Yaml;

import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * 自定义返回值解析器
 */
public class YmlReturnValueHandler implements HandlerMethodReturnValueHandler {
    /**
     * 和自定义参数解析器一样，这个判断返回值的类型，如果这个方法返回true，则执行handleReturnValue方法
     * @param returnType
     * @return
     */
    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        //判断返回的方法上面是否有自定义的@Yml注解
        Yml yml = returnType.getMethodAnnotation(Yml.class);
        return Objects.nonNull(yml);
    }

    @Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws Exception {
        //将返回值转换为yaml字符串
        String str = new Yaml().dump(returnValue);
        //获取response对象，然后输出返回信息
        HttpServletResponse response = webRequest.getNativeResponse(HttpServletResponse.class);
        response.setContentType("text/plain;charset=utf-8");
        response.getWriter().print(str);

        //设置请求已经处理完毕，这样就不会再去经过什么视图解析器之类的东西了
        mavContainer.setRequestHandled(true);
    }
}

package com.itheima.study.a29;

import lombok.*;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Objects;

/**
 * @author grzha
 */
@Configuration
public class WebConfig {

    @ControllerAdvice
    public static class MyControllerAdvice implements ResponseBodyAdvice<Object> {

        /**
         * 这个是用来判断是否满足条件，例如返回的方法是否加了@ResponseBody注解
         *
         * @param returnType
         * @param converterType
         * @return
         */
        @Override
        public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
            System.out.println("======1");
            //检查返回值对应的所在的方法或者类上面是否包含@ResponseBody，前面是检查所在方法是否有注解，后面是先获得返回值所在的类，然后看类上面是否有注解,第三个是查看类上面是否有
            //@RestController注解，这个注解里面也包含@ResponseBody注解，既检查类上，也检查@RestController里面是否有@ResponseBody注解
            if (Objects.nonNull(returnType.getMethodAnnotation(ResponseBody.class))
                    //|| returnType.getContainingClass().isAnnotationPresent(ResponseBody.class)
                    || AnnotationUtils.findAnnotation(returnType.getContainingClass(), ResponseBody.class) != null) {
                return true;
            }
            return false;
        }

        /**
         * 转换的逻辑，例如将返回类型User转换为通知一下的Result对象
         *
         * @param body                  这个是返回值
         * @param returnType            这个是返回值的一些信息，例如返回值的类型，返回值所在的方法，返回值所在方法上面所加的注解之类的
         * @param selectedContentType
         * @param selectedConverterType
         * @param request
         * @param response
         * @return
         */
        @Override
        public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
            System.out.println("======2");
            if (body instanceof Result) {
                return body;
            } else {
                return Result.success(body);
            }
        }
    }


    @Controller
    public static class MyController {
        @ResponseBody
        public User user() {
            return User.builder().name("王五").age(18).build();
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    @Builder
    public static class User {
        private String name;
        private Integer age;
    }


}

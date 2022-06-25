package com.itheima.study.a31;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;

import java.util.Map;

/**
 * @author grzha
 */
@Configuration
public class WebConfig {

    @ControllerAdvice
    static class MyControllerAdvice {
        @ExceptionHandler
        @ResponseBody
        public Map<String, Object> handle(Exception e) {
            Map<String, Object> result = Maps.newHashMap();
            result.put("error", e.getMessage());
            return result;
        }

    }

    /**
     * 这时，spring容器会初始化ExceptionHandlerExceptionResolver，因为这个类实现了InitializingBean接口，
     * 在对象被初始化之后，会自动调用afterPropertiesSet方法
     *
     * @return
     */
    @Bean
    public ExceptionHandlerExceptionResolver resolver() {
        ExceptionHandlerExceptionResolver resolver = new ExceptionHandlerExceptionResolver();
        resolver.setMessageConverters(Lists.newArrayList(new MappingJackson2HttpMessageConverter()));
        return resolver;
    }


}

package com.itheima.study.a27;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.view.AbstractUrlBasedView;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerView;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

import java.io.IOException;

/**
 * @author grzha
 */
@Configuration
public class WebConfig {

    /**
     * 要写这个一定要加上freemarker和spring-context-support这两个jar包，否则是不会成功的，这个老师视频也没有详细说明
     * 模板的地址在classpath:template
     *
     * @return
     * @throws IOException
     */
    @Bean
    public FreeMarkerConfigurer freeMarkerConfigurer() throws IOException {
        FreeMarkerConfigurer configurer = new FreeMarkerConfigurer();
        configurer.setTemplateLoaderPath("classpath:template");
        configurer.setDefaultEncoding("utf-8");
        return configurer;
    }

    /**
     * 这里写了好久，老师视频不清晰，这里要写FreeMarkerViewResolver的子类，然后重写里面的方法，写new FreeMarkerViewResolver(){}
     * 然后重写instantiateView方法，写new FreeMarkerView(){}，然后重写isContextRequired方法，老师在视频没有详细说明
     *
     * @param configurer
     * @return
     */
    @Bean
    public FreeMarkerViewResolver viewResolver(FreeMarkerConfigurer configurer) {
        FreeMarkerViewResolver resolver = new FreeMarkerViewResolver() {
            @Override
            protected AbstractUrlBasedView instantiateView() {
                FreeMarkerView view = new FreeMarkerView() {
                    @Override
                    protected boolean isContextRequired() {
                        return false;
                    }
                };
                view.setConfiguration(configurer.getConfiguration());
                return view;
            }
        };
        resolver.setContentType("text/html;charset=utf-8");
        resolver.setPrefix("/");
        resolver.setSuffix(".ftl");
        resolver.setExposeSpringMacroHelpers(false);
        return resolver;
    }


}

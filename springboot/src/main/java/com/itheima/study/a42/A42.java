package com.itheima.study.a42;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.*;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.ClassUtils;

/**
 * 这一讲讲的是，spring是如何通过注解的方式，使得在不同条件下（例如类路径下一些jar包中的类是否存在），使不同的配置类生效
 *
 * @author grzha
 */
@Slf4j
public class A42 {


    public static void main(String[] args) {
        GenericApplicationContext context = new GenericApplicationContext();
        context.registerBean("config", Config.class);
        //因为容器初始化要解析@Configuration注解和@Import注解，所以要加入下面的bean工厂后处理器
        context.registerBean(ConfigurationClassPostProcessor.class);
        context.refresh();

        for (String name : context.getBeanDefinitionNames()) {
            log.info("======name:{}", name);
        }


    }

    @Configuration
    @Import(MyImportSelector.class)
    static class Config {

    }

    static class MyImportSelector implements DeferredImportSelector {

        @Override
        public String[] selectImports(AnnotationMetadata importingClassMetadata) {
            return new String[]{AutoConfiguration1.class.getName(), AutoConfiguration2.class.getName()};
        }
    }

    /**
     * 存在Druid的依赖的条件
     */
    static class MyCondition1 implements Condition {

        @Override
        public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
            //使用ClassUtils这个工具类判断类路径下是否有DruidDataSource这个注解
            return ClassUtils.isPresent("com.alibaba.druid.pool.DruidDataSource", null);
        }
    }

    /**
     * 不存在Druid的依赖的条件
     */
    static class MyCondition2 implements Condition {

        @Override
        public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
            //使用ClassUtils这个工具类判断类路径下是否有DruidDataSource这个注解
            return !ClassUtils.isPresent("com.alibaba.druid.pool.DruidDataSource", null);
        }
    }

    /**
     * Druid的jar包存在，就配置AutoConfiguration1，会注入Bean1
     */
    @Configuration
    @Conditional(MyCondition1.class)
    static class AutoConfiguration1 {
        @Bean
        public Bean1 bean1() {
            return new Bean1();
        }
    }

    /**
     * Druid的jar包不存在，就配置AutoConfiguration2，会注入Bean2
     */
    @Configuration
    @Conditional(MyCondition2.class)
    static class AutoConfiguration2 {
        @Bean
        public Bean2 bean2() {
            return new Bean2();
        }
    }

    @Data
    @NoArgsConstructor
    @ToString
    static class Bean1 {
    }


    @Data
    @NoArgsConstructor
    @ToString
    static class Bean2 {

    }


}

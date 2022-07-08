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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;

/**
 * 这个类是将A41中的代码进行改进，将判断条件合二为一，并且类名不用写死在判断逻辑中
 *
 * @author grzha
 */
@Slf4j
public class A42_2 {


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

    static class MyCondition implements Condition {

        @Override
        public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
            // 获取ConditionalOnClass这个注解里面的属性信息
            Map<String, Object> attributes = metadata.getAnnotationAttributes(ConditionalOnClass.class.getName());
            // 获取要判断的类名
            String className = attributes.get("className").toString();
            // 判断存在还是不存在，如果为true就是要判断这个类是否存在，如果为false就是判断这个类是否不存在
            Boolean exists = (Boolean) attributes.get("exists");
            boolean present = ClassUtils.isPresent(className, null);
            return exists ? present : !present;
        }
    }


    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.METHOD, ElementType.TYPE})
    @Conditional(MyCondition.class)
    @interface ConditionalOnClass {
        //true 表示存在 false 表示不存在
        boolean exists();

        //要判断的类名
        String className();
    }

    /**
     * Druid的jar包存在，就配置AutoConfiguration1，会注入Bean1
     */
    @Configuration
    @ConditionalOnClass(className = "com.alibaba.druid.pool.DruidDataSource", exists = true)
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
    @ConditionalOnClass(className = "com.alibaba.druid.pool.DruidDataSource", exists = false)
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

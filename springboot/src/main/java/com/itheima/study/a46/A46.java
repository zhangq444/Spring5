package com.itheima.study.a46;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanExpressionContext;
import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ContextAnnotationAutowireCandidateResolver;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

/**
 * @author grzha
 */
@Configuration
@SuppressWarnings("all")
@Slf4j
public class A46 {

    public static void main(String[] args) throws NoSuchFieldException {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(A46.class);
        DefaultListableBeanFactory beanFactory = context.getDefaultListableBeanFactory();

        //解析@Value注解用到的处理器
        ContextAnnotationAutowireCandidateResolver resolver = new ContextAnnotationAutowireCandidateResolver();
        resolver.setBeanFactory(beanFactory);

        //获取@Vaule注解中的内容
        Field home = Bean1.class.getDeclaredField("home");
        test1(context, resolver, home);
        test2(context, resolver, Bean1.class.getDeclaredField("age"));
        test3(context, resolver, Bean2.class.getDeclaredField("bean3"));
        test3(context, resolver, Bean4.class.getDeclaredField("vaule"));


    }

    private static void test3(AnnotationConfigApplicationContext context, ContextAnnotationAutowireCandidateResolver resolver, Field home) {
        DependencyDescriptor descriptor = new DependencyDescriptor(home, false);
        String value = resolver.getSuggestedValue(descriptor).toString();
        log.info("======value:{}", value);

        //解析${}
        value = context.getEnvironment().resolvePlaceholders(value);
        log.info("======value:{}", value);
        log.info("======class:{}", value.getClass());

        //解析#{}，也就是spring的EL表达式
        Object bean3 = context.getBeanFactory().getBeanExpressionResolver().evaluate(value, new BeanExpressionContext(context.getBeanFactory(), null));

        //根据成员变量，将解析的结果类型转换成成员变量的类型
        Object result = context.getBeanFactory().getTypeConverter().convertIfNecessary(bean3, descriptor.getDependencyType());
        log.info("======result:{}", result);
        log.info("======class:{}", result.getClass());
    }

    private static void test2(AnnotationConfigApplicationContext context, ContextAnnotationAutowireCandidateResolver resolver, Field home) {
        DependencyDescriptor descriptor = new DependencyDescriptor(home, false);
        String value = resolver.getSuggestedValue(descriptor).toString();
        log.info("======value:{}", value);

        //解析${}
        value = context.getEnvironment().resolvePlaceholders(value);
        log.info("======value:{}", value);
        log.info("======class:{}", value.getClass());
        //根据成员变量的类型进行类型转换，把String转成int
        Object age = context.getBeanFactory().getTypeConverter().convertIfNecessary(value, descriptor.getDependencyType());
        log.info("======age:{}", age);
        log.info("======class:{}", age.getClass());
    }

    private static void test1(AnnotationConfigApplicationContext context, ContextAnnotationAutowireCandidateResolver resolver, Field home) {
        DependencyDescriptor descriptor = new DependencyDescriptor(home, false);
        String value = resolver.getSuggestedValue(descriptor).toString();
        log.info("======value:{}", value);

        //解析${}
        value = context.getEnvironment().resolvePlaceholders(value);
        log.info("======value:{}", value);
    }

    public class Bean1 {
        @Value("${JAVA_HOME}")
        private String home;
        @Value("18")
        private int age;
    }

    public class Bean2 {
        //spring的EL表达式
        @Value("#{@bean3}")
        private Bean3 bean3;
    }

    @Component("bean3")
    public class Bean3 {

    }

    public class Bean4 {
        @Value("#{'hello,'+'${JAVA_HOME}'}")
        private String vaule;
    }


}

package com.itheima.study.a47;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ContextAnnotationAutowireCandidateResolver;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Optional;

/**
 * 这一讲讲的是如何通过@Autowired注入基本的一些类型，譬如注入一些bean
 * 里面展示了根据autowired标注的变量，获取对应的类型，然后在容器中找到对应的bean，再根据最终变量的类型进行类型转换，这样就变成了可以赋值给成员变量的对象了
 *
 * @author grzha
 */
@Configuration
@Slf4j
public class A47_1 {

    public static void main(String[] args) throws NoSuchFieldException, NoSuchMethodException {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(A47_1.class);
        DefaultListableBeanFactory beanFactory = context.getDefaultListableBeanFactory();
        //1.根据成员变量的类型注入
        DependencyDescriptor descriptor = new DependencyDescriptor(Bean1.class.getDeclaredField("bean2"), false);
        //从beanFactory获得要注入的对象
        Object bean2 = beanFactory.doResolveDependency(descriptor, "bean1", null, null);
        log.info("======bean2:{}", bean2);
        //2.根据参数的类型注入
        Method setBean2 = Bean1.class.getDeclaredMethod("setBean2", Bean2.class);
        //设置是setBean2方法的第0个参数
        DependencyDescriptor descriptor1 = new DependencyDescriptor(new MethodParameter(setBean2, 0), false);
        Object bean2_2 = beanFactory.doResolveDependency(descriptor1, "bean1", null, null);
        log.info("======bean2_2:{}", bean2_2);
        //3.结果包装为Optional<Bean2>
        DependencyDescriptor descriptor2 = new DependencyDescriptor(Bean1.class.getDeclaredField("bean3"), false);
        if (descriptor2.getDependencyType() == Optional.class) {
            //获取内层的被包装的类型
            descriptor2.increaseNestingLevel();
            Object bean2_3 = beanFactory.doResolveDependency(descriptor2, "bean1", null, null);
            Optional<Object> bean2_3_optional = Optional.ofNullable(bean2_3);
            log.info("======bean2_3:{}", bean2_3_optional);
        }
        //4.结果包装为ObjectFactory<Bean2>
        DependencyDescriptor descriptor3 = new DependencyDescriptor(Bean1.class.getDeclaredField("bean4"), false);
        if (descriptor3.getDependencyType() == ObjectFactory.class) {
            descriptor3.increaseNestingLevel();
            ObjectFactory objectFactory = new ObjectFactory() {
                @Override
                public Object getObject() throws BeansException {
                    Object bean2_4 = beanFactory.doResolveDependency(descriptor3, "bean1", null, null);
                    return bean2_4;
                }
            };
            log.info("======bean2_4:{}", objectFactory.getObject());
        }
        //5.对@Lazy的处理，会延迟获得，并生成代理对象
        DependencyDescriptor descriptor4 = new DependencyDescriptor(Bean1.class.getDeclaredField("bean5"), false);
        //解析@Lazy注解的处理器
        ContextAnnotationAutowireCandidateResolver resolver = new ContextAnnotationAutowireCandidateResolver();
        resolver.setBeanFactory(beanFactory);
        Object proxy = resolver.getLazyResolutionProxyIfNecessary(descriptor4, "bean1");
        log.info("======proxy:{}", proxy);
        log.info("======proxy class:{}", proxy.getClass());


    }


    static class Bean1 {
        @Autowired
        private Bean2 bean2;

        @Autowired
        public void setBean2(Bean2 bean2) {
            this.bean2 = bean2;
        }

        @Autowired
        private Optional<Bean2> bean3;

        @Autowired
        private ObjectFactory<Bean2> bean4;

        @Autowired
        @Lazy
        private Bean2 bean5;
    }

    @Component("bean2")
    static class Bean2 {
        /**
         * 这个toString一定要加，否则第5个案例会报错，ciglib代理调用toString方法如果不重写过，调用jdk的方法会报错
         */
        @Override
        public String toString() {
            return super.toString();
        }
    }


}

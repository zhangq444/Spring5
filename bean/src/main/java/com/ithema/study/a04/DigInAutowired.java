package com.ithema.study.a04;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.beans.factory.annotation.InjectionMetadata;
import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.ContextAnnotationAutowireCandidateResolver;
import org.springframework.core.MethodParameter;
import org.springframework.core.env.StandardEnvironment;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 这一讲主要是对AutowiredAnnotationBeanPostProcessor这个bean后处理器进行分析
 *
 * @author grzha
 */
@Slf4j
public class DigInAutowired {

    public static void main(String[] args) throws Throwable {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        //这样注册的bean不会经过依赖注入，初始化等阶段
        beanFactory.registerSingleton("bean2", new Bean2());
        beanFactory.registerSingleton("bean3", new Bean3());
        //解析@Value
        beanFactory.setAutowireCandidateResolver(new ContextAnnotationAutowireCandidateResolver());
        //解析${}的，这个用在Bean1里面Bean3的注入的时候
        beanFactory.addEmbeddedValueResolver(new StandardEnvironment()::resolvePlaceholders);

        /**
         *  查找哪些属性、方法加了@Autowired注解，这称之为InjectionMetadata
         */
        AutowiredAnnotationBeanPostProcessor processor = new AutowiredAnnotationBeanPostProcessor();
        processor.setBeanFactory(beanFactory);
        Bean1 bean1 = new Bean1();
        /*log.info("======1  bean1:{}", bean1);
        //postProcessProperties这个方法是在依赖注入的时候执行的，这个在前面第三讲将bean的生命周期的时候讲过
        //执行这个方法前，bean1的@Autowired属性没有被注入，执行之后就注入了
        processor.postProcessProperties(null, bean1, "bean1");
        log.info("======2  bean1:{}", bean1);*/

        /**
         * 下面的代码是模拟AutowiredAnnotationBeanPostProcessor里面的postProcessProperties这个方法里面的执行内容写的
         */
        Method method = AutowiredAnnotationBeanPostProcessor.class.getDeclaredMethod("findAutowiringMetadata", String.class, Class.class, PropertyValues.class);
        method.setAccessible(true);
        //这一步是查找Bean1中哪些属性和方法加了@Autowired，并且封装在InjectionMetadata中
        InjectionMetadata metadata = (InjectionMetadata) method.invoke(processor, "bean1", Bean1.class, null);
        //metadata没有重写toString，可以打一个断点来看里面的属性
        System.out.println(metadata);

        //这个步是通过反射的方式，将属性进行赋值，或者通过set方法反射调用，进行依赖注入
        metadata.inject(bean1, "bean1", null);
        System.out.println(bean1);

        /**
         *  如何按类型查找值
         */
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        //在容器中查找bean4属性对应类型的值
        Field bean4 = Bean1.class.getDeclaredField("bean4");
        DependencyDescriptor descriptor1 = new DependencyDescriptor(bean4, false);
        Object o1 = beanFactory.doResolveDependency(descriptor1, null, null, null);
        log.info("======o1:{}", o1);

        //查找setBean2方法中索引为0的参数，在容器中是否有对应类型的值，注意，方法加上@Autowired注解，封装的时候是以方法的参数为单位的，几个参数就要封装几个MethodParameter对象
        Method setBean2 = Bean1.class.getDeclaredMethod("setBean2", Bean2.class);
        DependencyDescriptor descriptor2 = new DependencyDescriptor(new MethodParameter(setBean2, 0), false);
        Object o2 = beanFactory.doResolveDependency(descriptor2, null, null, null);
        log.info("======o2:{}", o2);

        //查找setHome方法中需要注入的参数，在环境变量或者配置文件中是否存在，也是放在容器中的
        Method setHome = Bean1.class.getDeclaredMethod("setHome", String.class);
        DependencyDescriptor descriptor3 = new DependencyDescriptor(new MethodParameter(setHome, 0), false);
        Object o3 = beanFactory.doResolveDependency(descriptor3, null, null, null);
        log.info("======o3:{}", o3);

    }


}

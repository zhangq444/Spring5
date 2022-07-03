package com.itheima.study.a41;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.aspectj.annotation.AnnotationAwareAspectJAutoProxyCreator;
import org.springframework.boot.autoconfigure.aop.AopAutoConfiguration;
import org.springframework.context.annotation.AnnotationConfigUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DeferredImportSelector;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.env.SimpleCommandLinePropertySource;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author grzha
 */
@Slf4j
public class TestAopAuto {

    public static void main(String[] args) {
        GenericApplicationContext context = new GenericApplicationContext();
        ///通过编码的方式往容器中增加配置，设为false，就不会初始化aop自动配置了
        StandardEnvironment environment = new StandardEnvironment();
        /*environment.getPropertySources().addLast(new SimpleCommandLinePropertySource("--spring.aop.auto=false"));
        context.setEnvironment(environment);*/

        // 注册一些后处理器
        AnnotationConfigUtils.registerAnnotationConfigProcessors(context.getDefaultListableBeanFactory());
        context.registerBean(Config.class);
        context.refresh();
        for (String name : context.getBeanDefinitionNames()) {
            log.info("======name:{}", name);
        }
        log.info("==========================");
        AnnotationAwareAspectJAutoProxyCreator creator = context.getBean("org.springframework.aop.config.internalAutoProxyCreator", AnnotationAwareAspectJAutoProxyCreator.class);
        //查看是否使用ciglib作为动态代理
        System.out.println(creator.isProxyTargetClass());


    }

    @Configuration
    @Import(MyImportSelector.class)
    static class Config {

    }

    static class MyImportSelector implements DeferredImportSelector {
        @Override
        public String[] selectImports(AnnotationMetadata importingClassMetadata) {
            return new String[]{AopAutoConfiguration.class.getName()};
        }
    }


}

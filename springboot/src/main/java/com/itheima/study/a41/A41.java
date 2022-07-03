package com.itheima.study.a41;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.*;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.support.SpringFactoriesLoader;
import org.springframework.core.type.AnnotationMetadata;

import java.util.List;

/**
 * 这一讲讲了在自己项目的配置类中，引入第三方的配置类，然后再容器初始化后，会将本配置类和第三方的配置类中配置的bean都加入到容器中去
 *
 * @author grzha
 */
@Slf4j
public class A41 {

    public static void main(String[] args) {
        GenericApplicationContext context = new GenericApplicationContext();
        //设置bean的定义是否可以覆盖，默认是可以，springboot项目改为了不允许覆盖
        context.getDefaultListableBeanFactory().setAllowBeanDefinitionOverriding(false);
        context.registerBean("config", Config.class);
        //因为容器初始化要解析@Configuration注解和@Import注解，所以要加入下面的bean工厂后处理器
        context.registerBean(ConfigurationClassPostProcessor.class);
        context.refresh();

        for (String name : context.getBeanDefinitionNames()) {
            log.info("======name:{}", name);
        }
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        log.info("======bean1:{}", context.getBean(Bean1.class));


    }

    /**
     * 本项目的配置类
     */
    @Configuration
    /**
     * 一开始是使用这个注解，直接导入两个自动配置类@Import({AutoConfiguration1.class, AutoConfiguration2.class})
     */
    @Import(MyImportSelector.class)
    static class Config {
        /**
         * 在默认情况下，首先解析的是@Import(MyImportSelector.class)这里面导入的bean，然后解析的是本项目的bean
         * 本项目的bean可以覆盖掉前面注册的bean，所以最后在容器中的是本项目的bean1
         *
         * @return
         */
        @Bean
        public Bean1 bean1() {
            return new Bean1("本项目");
        }
    }

    /**
     * 用ImportSelector接口就是先解析MyImportSelector，再解析Config的配置，
     * 如果改成DeferredImportSelector,就是推迟解析的，就是反过来，先解析自己项目配置类的bean，然后解析第三方的配置bean
     */
    static class MyImportSelector implements DeferredImportSelector {

        @Override
        public String[] selectImports(AnnotationMetadata importingClassMetadata) {
            //打印一下spring自动配置对应的第三方配置类
            log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            List<String> stringList = SpringFactoriesLoader.loadFactoryNames(EnableAutoConfiguration.class, null);
            stringList.stream().forEach(s -> log.info("======[{}]", s));
            log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

            ///不在代码中写死，转用配置文件配置
//            return new String[]{AutoConfiguration1.class.getName(), AutoConfiguration2.class.getName()};
            List<String> names = SpringFactoriesLoader.loadFactoryNames(MyImportSelector.class, null);
            return names.toArray(new String[0]);
        }
    }

    /**
     * 第三方的配置类
     */
    @Configuration
    static class AutoConfiguration1 {
        @Bean
        //@ConditionalOnMissingBean这个注解标识，当本项目没有这个bean时，才生效，本项目有配置这个bean时，就不生效
        @ConditionalOnMissingBean
        public Bean1 bean1() {
            return new Bean1("第三方");
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    static class Bean1 {
        private String name;
    }

    /**
     * 第三方的配置类
     */
    @Configuration
    static class AutoConfiguration2 {
        @Bean
        public Bean2 bean2() {
            return new Bean2();
        }
    }


    static class Bean2 {

    }


}

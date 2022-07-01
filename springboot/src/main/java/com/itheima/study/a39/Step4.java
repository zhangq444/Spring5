package com.itheima.study.a39;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.source.ConfigurationPropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.ResourcePropertySource;

/**
 * run方法第四步做的事情，往ApplicationEnvironment环境对象中增加了ConfigurationPropertySources
 *
 * @author grzha
 */
@Slf4j
public class Step4 {

    public static void main(String[] args) throws Exception {
        /**
         * ApplicationEnvironment这个类的对象老师是直接new的，但是我看这个类不是public的，不知道老师是怎么直接new的
         * 所以我是使用ApplicationEnvironment这个类的父类StandardEnvironment来写的
         */
        StandardEnvironment env = new StandardEnvironment();
        //添加properties文件作为环境变量来源
        env.getPropertySources().addLast(new ResourcePropertySource("step4", new ClassPathResource("step4.properties")));
        //这一步会把一个configurationProperties的PropertySource加到env对象的最顶层，就是优先级最高
        ConfigurationPropertySources.attach(env);
        for (PropertySource<?> propertySource : env.getPropertySources()) {
            System.out.println(propertySource);
        }

        //注意，取的时候的key值和properties文件中配的格式是不一样的，有中划线，下划线，还有驼峰，
        //如果不加ConfigurationPropertySources，不同格式是取不到值的，但是如果加了，全部用中划线也可以取到值
        log.info("======{}", env.getProperty("user.first-name"));
        log.info("======{}", env.getProperty("user.middle-name"));
        log.info("======{}", env.getProperty("user.last-name"));


    }


}

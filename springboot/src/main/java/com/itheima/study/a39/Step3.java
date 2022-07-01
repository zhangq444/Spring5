package com.itheima.study.a39;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.SimpleCommandLinePropertySource;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.ResourcePropertySource;

/**
 * run方法第三步做的事情，准备ApplicationEnvironment环境对象准备好，把SimpleCommandLinePropertySource环境来源加进来了，
 * properties文件的来源不是在第三步中加的，时候后续的操作中加的
 *
 * @author grzha
 */
@Slf4j
public class Step3 {

    public static void main(String[] args) throws Exception {
        /**
         * ApplicationEnvironment这个类的对象老师是直接new的，但是我看这个类不是public的，不知道老师是怎么直接new的
         * 所以我是使用ApplicationEnvironment这个类的父类StandardEnvironment来写的
         */
        // 环境参数主要包含系统属性和系统环境变量  还可以来自于properties yaml文件
        StandardEnvironment env = new StandardEnvironment();
        //添加properties文件作为环境变量的来源,properties的优先级最低，所以addLast()
        env.getPropertySources().addLast(new ResourcePropertySource(new ClassPathResource("application.properties")));
        //添加命令行参数作为环境变量，这个优先级最高，所以用addFirst()，idea中可以启动时配置
        env.getPropertySources().addFirst(new SimpleCommandLinePropertySource(args));
        for (PropertySource<?> ps : env.getPropertySources()) {
            log.info("======ps:{}", ps);
        }
        log.info("======java_home:{}", env.getProperty("JAVA_HOME"));
        log.info("======server.port:{}", env.getProperty("server.port"));

    }


}

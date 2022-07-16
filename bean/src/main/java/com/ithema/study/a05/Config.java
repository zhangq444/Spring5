package com.ithema.study.a05;

import com.alibaba.druid.pool.DruidDataSource;
import com.ithema.study.a05.mapper.Mapper1;
import com.ithema.study.a05.mapper.Mapper2;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.defaults.DefaultSqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@Slf4j
@ComponentScan("com.ithema.study.a05.component")
public class Config {

    @Bean
    public Bean1 bean1(){
        return new Bean1();
    }


    /**
     * 这个SqlSessionFactoryBean，实现了InitializingBean接口和FactoryBean<SqlSessionFactory>接口，
     * InitializingBean接口的作用是在初始化对象时会调用afterPropertiesSet这个方法，然后在这个方法中，会调用buildSqlSessionFactory，然后就会给sqlSessionFactory这个成员变量赋值，
     * 然后FactoryBean<SqlSessionFactory>接口，FactoryBean 是一个能生产或修饰对象生成的工厂 Bean。一个 Bean 如果实现了 FactoryBean 接口，那么根据该 Bean 的名称获取到的实际上是 getObject() 返回的对象，
     * 而不是这个 Bean 自身实例，如果要获取这个 Bean 自身实例，那么需要在名称前面加上 & 符号
     * 所以在SqlSessionFactoryBean这个类中的getObject方法返回的是sqlSessionFactory这个成员变量，所以在后面其他方法的注入时可以注入sqlSessionFactory。
     * 这个花了我3-4个小时才弄明白
     * @param dataSource
     * @return
     */
    @Bean
    public SqlSessionFactoryBean sqlSessionFactoryBean(DataSource dataSource){
        log.info("======dataSource:{}",dataSource.getClass().getName());
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource);
        log.info("======SqlSessionFactoryBean:{}",factoryBean.getClass().getName());
        return factoryBean;
    }



    @Bean(initMethod = "init")
    public DruidDataSource dataSource(){
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl("jdbc:mysql://localhost:3306/mybatis");
        dataSource.setUsername("root");
        dataSource.setPassword("root");
        return dataSource;
    }

/*    @Bean
    public MapperFactoryBean<Mapper1> mapper1(SqlSessionFactory factoryBean){
        MapperFactoryBean<Mapper1> bean = new MapperFactoryBean<>(Mapper1.class);
        bean.setSqlSessionFactory(factoryBean);
        return bean;
    }

    @Bean
    public MapperFactoryBean<Mapper2> mapper2(SqlSessionFactory factory){
        log.info("======classname:{}",factory.getClass().getName());
        log.info("======factory:{}",factory);
        MapperFactoryBean<Mapper2> bean=new MapperFactoryBean<>(Mapper2.class);
        bean.setSqlSessionFactory(factory);
        return bean;
    }*/



}

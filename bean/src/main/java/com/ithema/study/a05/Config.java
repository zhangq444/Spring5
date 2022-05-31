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

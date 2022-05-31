package com.ithema.study.a05;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.mapper.MapperFactoryBean;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;

@Slf4j
public class MapperPostProcessor implements BeanDefinitionRegistryPostProcessor {


    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanFactory) throws BeansException {
        try {
            //读取资源的factory
            Resource[] resources = new PathMatchingResourcePatternResolver().getResources("com/ithema/study/a05/mapper/**/*.class");
            //用来读取类的一些元数据的工具
            CachingMetadataReaderFactory factory = new CachingMetadataReaderFactory();
            //这个是根据beanDefinition来生成对应的注入容器后的对象名的工具
            AnnotationBeanNameGenerator generator=new AnnotationBeanNameGenerator();
            for (Resource resource : resources) {
                MetadataReader reader = factory.getMetadataReader(resource);
                //然后判断是否为接口
                if (reader.getClassMetadata().isInterface()) {
                    ClassMetadata metadata = reader.getClassMetadata();
                    //生成对应的beanDefinition,生成的类型为MapperFactoryBean，里面有个构造方法传入Mapper1，输入参数为按照类型注入
                    AbstractBeanDefinition beanDefinition = BeanDefinitionBuilder
                            .genericBeanDefinition(MapperFactoryBean.class)
                            .addConstructorArgValue(metadata.getClassName())
                            .setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE)
                            .getBeanDefinition();
                    //生成另外一个beanDefinition，专门用来生成名字用的，不用注册进入容器
                    AbstractBeanDefinition definition = BeanDefinitionBuilder.
                            genericBeanDefinition(metadata.getClassName())
                            .getBeanDefinition();
                    String beanName = generator.generateBeanName(definition, beanFactory);
                    beanFactory.registerBeanDefinition(beanName,beanDefinition);
                }
            }
        }catch (Exception e){

        }
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {

    }
}

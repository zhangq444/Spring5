package com.ithema.study.a05;

import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

/**
 *  自己实现ConfigurationClassPostProcessor功能，自己解析@Component注解生成bean
 * @author grzha
 */
@Slf4j
public class ComponentScanPostProcessor implements BeanFactoryPostProcessor {

    //这个方法会在context.refresh()的时候调用
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        //获取cofig类上ComponentScan所扫描的包路径
        ComponentScan annotation = AnnotationUtils.findAnnotation(Config.class, ComponentScan.class);
        if (Objects.nonNull(annotation)) {
            Arrays.stream(annotation.basePackages()).forEach(basePackage -> {
                log.info("======{}", JSON.toJSONString(basePackage));
                //com.ithema.study.a05.component -> classpath*:com/ithema/study/a05/component/**/*.class
                String path = "classpath*:" + basePackage.replace(".", "/") + "/**/*.class";
                log.info("======{}", JSON.toJSONString(path));
                try {
                    //用来获取class文件的一些元信息，例如类名以及类上的注解
                    CachingMetadataReaderFactory factory = new CachingMetadataReaderFactory();
                    //根据类路径，获取对应的class资源
                    Resource[] resources = new PathMatchingResourcePatternResolver().getResources(path);
                    //这个是根据beanDefinition来生成对应的注入容器后的对象名的工具
                    AnnotationBeanNameGenerator generator=new AnnotationBeanNameGenerator();
                    for (Resource resource : resources) {
//                        log.info("======{}",resource);
                        MetadataReader reader = factory.getMetadataReader(resource);
                        AnnotationMetadata annotationMetadata = reader.getAnnotationMetadata();
                        log.info("======类名:{}", reader.getClassMetadata().getClassName());
                        log.info("======类上是否加了@Component: {}", annotationMetadata.hasAnnotation(Component.class.getName()));
                        log.info("======类上是否加了@Component 派生注解: {}", annotationMetadata.hasMetaAnnotation(Component.class.getName()));

                        //判断获得类上面有没有直接或者间接的加上@Component注解
                        if (annotationMetadata.hasAnnotation(Component.class.getName()) || annotationMetadata.hasMetaAnnotation(Component.class.getName())) {
                            //根据类名，获得类的beanDefinition，bean的定义信息
                            AbstractBeanDefinition beanDefinition = BeanDefinitionBuilder.genericBeanDefinition(reader.getClassMetadata().getClassName()).getBeanDefinition();
                            if(configurableListableBeanFactory instanceof DefaultListableBeanFactory){
                                DefaultListableBeanFactory beanFactory= (DefaultListableBeanFactory) configurableListableBeanFactory;
                                String name = generator.generateBeanName(beanDefinition, beanFactory);
                                //往beanFactory中注册bean
                                beanFactory.registerBeanDefinition(name,beanDefinition);
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            });
        }

    }
}

package com.itheima.study.a44;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;

/**
 * 这一讲讲了@Indexed这个注解，是spring5的一个优化，由于spring在启动时，会扫描很多类和jar包，把要实例化的对象进行实例化，这样非常慢，
 * 所以他会去读一个META-INF文件夹下的spring.components，然后这个文件中会配置类名和类上的注解，所以spring在启动时会去读取这个文件，然后就会初始化里面配置单额类，
 * 这样就不用去遍历扫描了。
 * 这个spring.components文件可以由spring-context-indexer这个jar包生成，他在编译的时候，spring就把标注了@Indexed这个注解的类记录进入这个文件中，其中像Component注解，@Controller注解都包含@Indexed
 * 其实就是spring把扫描的时间提前了，不是放在启动的时候，而是在编译的时候就扫描，然后把结果放在spring.components这个文件夹中，之后启动就读取这个文件中的结果，然后实例化就可以了
 * 加了spring-context-indexer这个jar包，我们定义了spring.components文件会用我们的，我们没有定义，就会自动生成
 *
 * @author grzha
 */
@Slf4j
public class A44 {

    public static void main(String[] args) {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        //组件扫描的核心类
        ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(beanFactory);
        //设置扫描路径为a44包
        scanner.scan(A44.class.getPackageName());
        for (String name : beanFactory.getBeanDefinitionNames()) {
            log.info("======name:{}", name);
        }

    }

    /**
     * Bean3虽然加了@Component注解，但是在我们写的spring.components文件中没有把它加进去，所以不会有他的BeanDefinition
     *
     */


}

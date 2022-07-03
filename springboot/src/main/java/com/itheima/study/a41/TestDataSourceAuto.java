package com.itheima.study.a41;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.transaction.TransactionAutoConfiguration;
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
public class TestDataSourceAuto {

    public static void main(String[] args) {
        GenericApplicationContext context = new GenericApplicationContext();
        StandardEnvironment environment = new StandardEnvironment();
        environment.getPropertySources().addLast(new SimpleCommandLinePropertySource(
                "--spring.datasource.url=jdbc:mysql://localhost:3306/mybatis",
                "--spring.datasource.username=root",
                "--spring.datasource.password=root"));
//        AutoConfigurationPackages.register(context.getDefaultListableBeanFactory(),TestDataSourceAuto.class.getPackageName());
        context.setEnvironment(environment);
        ///注册一些后处理器
        AnnotationConfigUtils.registerAnnotationConfigProcessors(context.getDefaultListableBeanFactory());
        context.registerBean(Config.class);

        String packageName = TestDataSourceAuto.class.getPackageName();
        log.info("======包名：{}", packageName);
        //这个可以记录springboot启动类的位置，这样mybatis在做Mapper扫描的时候，就知道扫描的范围了
        AutoConfigurationPackages.register(context.getDefaultListableBeanFactory(), packageName);

        context.refresh();
        for (String name : context.getBeanDefinitionNames()) {
            String resourceDescription = context.getBeanDefinition(name).getResourceDescription();
            if (StringUtils.isNotBlank(resourceDescription)) {
                log.info("======name:{},来源:{}", name, resourceDescription);
            }
        }

    }

    @Configuration
    @Import(MyImportSelector.class)
    static class Config {

    }

    /**
     * 需要同时引入mybatis和mysql的jar包，这样才会初始化自动配置类，否则是不会初始化的
     */
    static class MyImportSelector implements DeferredImportSelector {
        @Override
        public String[] selectImports(AnnotationMetadata importingClassMetadata) {
            return new String[]{
                    DataSourceAutoConfiguration.class.getName(),
                    MybatisAutoConfiguration.class.getName(),
                    DataSourceTransactionManagerAutoConfiguration.class.getName(),
                    //声明式事务管理
                    TransactionAutoConfiguration.class.getName()
            };
        }
    }

    /**
     * DataSource的自动配置
     * 首先是DataSourceAutoConfiguration引入了这个自动配置类，然后在这个配置类中，@Conditional({DataSourceAutoConfiguration.PooledDataSourceCondition.class})满足了这个条件，是自动配置，并且是连接池的数据源
     * 然后通过注解@Import({Hikari.class, Tomcat.class, Dbcp2.class, OracleUcp.class, Generic.class, DataSourceJmxConfiguration.class})又引入了Hikari.class的配置类，在这个类中
     * HikariDataSource dataSource(DataSourceProperties properties)，就返回了一个HikariDataSource的数据源，
     * 所以在打印来源的时候，来源是DataSourceConfiguration$Hikari，就是DataSourceConfiguration中的静态内部类Hikari
     */
    /**
     * MybatisAutoConfiguration  mybatis自动配置类中
     * @Configuration
     * @ConditionalOnClass({SqlSessionFactory.class, SqlSessionFactoryBean.class})
     * @ConditionalOnSingleCandidate(DataSource.class)
     * @EnableConfigurationProperties({MybatisProperties.class})
     * @AutoConfigureAfter({DataSourceAutoConfiguration.class, MybatisLanguageDriverAutoConfiguration.class})
     *  这个是mybatis的自动配置类，其中第二行，表示要生效自动配置，必须在类路径下有SqlSessionFactory.class, SqlSessionFactoryBean.class这两个类
     *  第三行表示，在当前spring容器中，有且仅有一个DataSource
     *  第四行表示，会创建一个MybatisProperties对象，和环境信息中对应的键值信息进行绑定
     *  第五行是用来控制自动配置类的解析顺序的，mybatis的自动配置类应该放在datasource的自动配置后面解析，因为要用到DataSource，这个配置就是用来控制顺序的
     *     @Bean
     *     @ConditionalOnMissingBean
     *     public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception
     *  然后在mybatis自动配置类中，提供了SqlSessionFactory的bean，@ConditionalOnMissingBean表明如果没有配置就用默认提供的
     *     @Bean
     *     @ConditionalOnMissingBean
     *     public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory)
     *  然后自动配置类中，还提供了SqlSessionTemplate的bean
     */
    /**
     * MybatisAutoConfiguration  mybatis自动配置类中
     *     @Configuration
     *     @Import({MybatisAutoConfiguration.AutoConfiguredMapperScannerRegistrar.class})
     *     @ConditionalOnMissingBean({MapperFactoryBean.class, MapperScannerConfigurer.class})
     *     public static class MapperScannerRegistrarNotFoundConfiguration implements InitializingBean
     *  第三行表示，当缺少MapperFactoryBean.class, MapperScannerConfigurer.class这两个类时条件才成立，才提供MapperScannerRegistrarNotFoundConfiguration的bean
     *  如果第三行成立，就会执行第二行的代码，@Import，导入MybatisAutoConfiguration.AutoConfiguredMapperScannerRegistrar.class这个类
     *  然后这个类实现了ImportBeanDefinitionRegistrar这个接口，实现这个接口，可以通过编程的方式补充一些BeanDefinition,就是补充用Mapper注解标注的类
     *
     *
     */


}

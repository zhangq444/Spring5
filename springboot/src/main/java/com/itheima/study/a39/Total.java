package com.itheima.study.a39;

/**
 * @author grzha
 */
public class Total {

    public static void main(String[] args) {
        /**
         * 这一讲是对39讲的全部总结，是结合源码，对run方法的12步做一个梳理，看SpringApplication这个类的源码
         */
        /**
         * 第一步  SpringApplication中的run方法中下面那一行代码
         * SpringApplicationRunListeners listeners = this.getRunListeners(args);
         *  这一行代码会读取spring.factories配置文件，找到里面事件发布器的实现类，然后获得事件发布器对象SpringApplicationRunListeners（这个不是监听器）
         *  listeners.starting(bootstrapContext, this.mainApplicationClass);
         *  然后上面那一行代码就发布了一个事件，就是springboot程序开始启动了
         */
        /**
         * 第二步  SpringApplication中的run方法中
         * ApplicationArguments applicationArguments = new DefaultApplicationArguments(args);
         * 把main方法的命令行参数封装成为一个ApplicationArguments，一个封装后的参数对象，主要的区别是把参数分为两类，一类是选项参数，以--开头，另外一类是非选项参数
         */
        /**
         * 第三步  SpringApplication中的run方法中
         * ConfigurableEnvironment environment = this.prepareEnvironment(listeners, bootstrapContext, applicationArguments);
         * 这一行代码，prepareEnvironment这个方法进去
         *  ConfigurableEnvironment environment = this.getOrCreateEnvironment();
         *  this.configureEnvironment((ConfigurableEnvironment)environment, applicationArguments.getSourceArgs());
         * 上面两行代码，第一行就是创建Environment对象，第二行是把前面的参数对象applicationArguments封装成为一个PropertySources源，添加到Environment里面，
         * 这样，Environment对象也可以从命令行参数信息中获取一些键值信息，但是必须是选项参数
         */
        /**
         * 第四步 SpringApplication中的run方法中  prepareEnvironment这个方法进去
         *  ConfigurationPropertySources.attach((Environment)environment);
         *  上面一行代码中，ConfigurationPropertySources是将我们环境信息中，命名不规范的键值信息，就是键以下划线或者驼峰命名的，统一处理成用-分割的，
         */
        /**
         * 第五步 SpringApplication中的run方法中  prepareEnvironment这个方法进去
         * listeners.environmentPrepared(bootstrapContext, (ConfigurableEnvironment)environment);
         * 拿到事件发布器，发布一个environmentPrepared，这时说明Environment对象已经准备好了，可以对他进行增强了，
         * 那么谁来响应这个事件呢？会从spring.factories配置文件找到事件的监听器对象，这个监听器对象会去获得EnvironmentPostProcessor后处理器，对Environment对象进行增强
         * 会添加更多的源，比较典型的就是一个random随机数的源，还有一个就是application.properties作为源
         */
        /**
         * 第六步 SpringApplication中的run方法中  prepareEnvironment这个方法进去
         *  this.bindToSpringApplication((ConfigurableEnvironment)environment);
         *  上一行代码，就是把我们Environment对象中，以spring.main开头的键值信息，和SpringApplication对象进行绑定，
         */
        /**
         * 第七步 SpringApplication中的run方法中
         *  Banner printedBanner = this.printBanner(environment);
         *  上面那一行代码，就是打印springboot的标志
         */
        /**
         * 第八步 SpringApplication中的run方法中
         * context = this.createApplicationContext();
         * 上面那一行代码，就是第八步，创建spring容器，就在SpringApplication构造方法里面，推断的三种项目类型，构造对应的spring容器，
         */
        /**
         * 第九步 SpringApplication中的run方法中
         *  this.prepareContext(bootstrapContext, context, environment, listeners, applicationArguments, printedBanner);
         *  进入这个方法，prepareContext，在这个方法中，
         *  this.applyInitializers(context);
         *  上面一行代码，是应用一些初始化器，对applicationContext做一些增强，
         *  listeners.contextPrepared(context);
         *  第九步之后，调用事件发布器发布contextPrepared事件，是容器初始化好，然后调用初始化器增强之后发布事件
         */
        /**
         * 第十步 SpringApplication中的run方法中  进入这个方法，prepareContext
         *  Set<Object> sources = this.getAllSources();
         *  Assert.notEmpty(sources, "Sources must not be empty");
         *  this.load(context, sources.toArray(new Object[0]));
         *  listeners.contextLoaded(context);
         *  上面四行代码中，第一行，是获得所有的BeanDefinition的源，可以从xml，可以从配置类，可以从组建扫描
         *  第三行，在BeanDefinition源获取后，会在这一行代码中，将所有的BeanDefinition源加载到applicationContext容器中
         *  然后是第四行，通过事件发布器发布contextLoaded事件，
         */
        /**
         * 第十一步 SpringApplication中的run方法中
         * this.refreshContext(context);
         * 调用了refreshContext方法，在这个方法内部，有一行代码
         * applicationContext.refresh();
         * 就是调用了applicationContext的refresh()方法，
         * 在这个方法中，它就会调用Bean工厂的后处理器，Bean的后处理器，然后初始化每个单例
         * 然后是到SpringApplication中的run方法中
         *  listeners.started(context, timeTakenToStartup);
         *  调用事件发布器，发布started事件，标识applicationContext已经准备好了
         */
        /**
         * 第十二步 SpringApplication中的run方法中
         *  this.callRunners(context, applicationArguments);
         *  去调用所有实现了ApplicationRunner接口或者CommandLineRunner接口的bean
         *  之后会执行代码
         *  listeners.ready(context, timeTakenToReady);
         *  调用事件发布器，发布ready事件，这一步我和老师的源码不同，老师的源码是listeners.running，发布running事件，
         *  表示springboot事件启动完成
         */
        /**
         * SpringApplication中的run方法中
         * this.handleRunFailure(context, var12, listeners);
         * 如果出现异常，会在handleRunFailure这个方法中发布失败事件
         */




    }


}

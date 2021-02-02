package cn.leafw.spring.test;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * TODO
 *AbstractAutowireCapableBeanFactory
 * @author <a href="mailto:wyr95626@95626.cn">CareyWYR</a>
 * @date 2021/2/1
 */
public class Test {

    public static void main(String[] args) {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:application.xml");
        applicationContext.registerShutdownHook();
    }
}


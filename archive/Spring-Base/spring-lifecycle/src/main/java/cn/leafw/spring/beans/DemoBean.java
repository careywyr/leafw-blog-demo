package cn.leafw.spring.beans;

import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * TODO
 *
 * @author <a href="mailto:wyr95626@95626.cn">CareyWYR</a>
 * @date 2021/2/1
 */
public class DemoBean implements InitializingBean, DisposableBean, BeanNameAware {

    public DemoBean() {
        System.out.println("demoBean实例化");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("demoBean afterPropertiesSet");
    }

    public void init() {
        System.out.println("demoBean init");
    }

    @Override
    public void destroy() throws Exception {
        System.out.println("demoBean destroy");
    }

    @Override
    public void setBeanName(String s) {
        System.out.println("BeanNameAware setBeanName: "+ s);
    }
}


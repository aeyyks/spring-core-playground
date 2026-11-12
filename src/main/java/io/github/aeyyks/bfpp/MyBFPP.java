package io.github.aeyyks.bfpp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

public class MyBFPP implements BeanFactoryPostProcessor {

    private static final Logger log = LoggerFactory.getLogger(MyBFPP.class);

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        log.debug("начало работы {}", this.getClass().getSimpleName());
        if (beanFactory.containsBeanDefinition("holaBean")) {
            BeanDefinition bd = beanFactory.getBeanDefinition("holaBean");

            log.debug("Старый Scope: " + bd.getScope());
            bd.setScope(BeanDefinition.SCOPE_PROTOTYPE);
            log.debug("Новый Scope: " + bd.getScope());
        }
    }
}

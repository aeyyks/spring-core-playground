package io.github.aeyyks.bfpp.bdrpp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.core.PriorityOrdered;

import java.util.Arrays;

public class FirstBDRPP implements BeanDefinitionRegistryPostProcessor, PriorityOrdered {

    private static final Logger log = LoggerFactory.getLogger(FirstBDRPP.class);

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        log.debug("--------------- ЭТАП РАБОТЫ BDRPP ---------------");
        log.debug("Первый BDRPP {}: Начало работы", this.getClass().getSimpleName());
        log.debug("кол-во BeanDefinition: {}", registry.getBeanDefinitionCount());
        log.debug("Зарегистрированные имена: {}", Arrays.toString(registry.getBeanDefinitionNames()));
        log.debug("Первый BDRPP {}: Конец работы", this.getClass().getSimpleName());
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        log.debug("--------------- ЭТАП РАБОТЫ BFPP ---------------");
        log.debug("Первый BFPP {}: Начало работы", this.getClass().getSimpleName());

        log.debug("Финальное количество BeanDefinitions: {}", beanFactory.getBeanDefinitionCount());
        if (beanFactory.containsBeanDefinition("myService")) {
            log.debug("Scope для myService: {}", beanFactory.getBeanDefinition("myService").getScope());
        }

        log.debug("Первый BFPP {}: Конец работы", this.getClass().getSimpleName());
    }

    @Override
    public int getOrder() {
        return PriorityOrdered.HIGHEST_PRECEDENCE;
    }
}

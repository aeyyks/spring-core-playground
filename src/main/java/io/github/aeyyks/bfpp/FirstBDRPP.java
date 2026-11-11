package io.github.aeyyks.bfpp;

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
        log.info("--------------- ЭТАП РАБОТЫ BDRPP ---------------");
        log.info("Первый BDRPP {}: Начало работы", this.getClass().getSimpleName());
        log.info("кол-во BeanDefinition: {}", registry.getBeanDefinitionCount());
        log.info("Зарегистрированные имена: {}", Arrays.toString(registry.getBeanDefinitionNames()));
        log.info("Первый BDRPP {}: Конец работы", this.getClass().getSimpleName());
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        log.info("--------------- ЭТАП РАБОТЫ BFPP ---------------");
        log.info("Первый BFPP {}: Начало работы", this.getClass().getSimpleName());

        log.info("Финальное количество BeanDefinitions: {}", beanFactory.getBeanDefinitionCount());
        if (beanFactory.containsBeanDefinition("myService")) {
            log.info("Scope для myService: {}", beanFactory.getBeanDefinition("myService").getScope());
        }

        log.info("Первый BFPP {}: Конец работы", this.getClass().getSimpleName());
    }

    @Override
    public int getOrder() {
        return PriorityOrdered.HIGHEST_PRECEDENCE;
    }
}

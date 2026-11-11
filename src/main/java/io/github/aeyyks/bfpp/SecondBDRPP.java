package io.github.aeyyks.bfpp;

import io.github.aeyyks.service.Ghost;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;

import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Random;

public class SecondBDRPP implements BeanDefinitionRegistryPostProcessor {

    public static final Logger log = LoggerFactory.getLogger(SecondBDRPP.class);

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        log.info("Второй BDRPP {}: Начало работы", this.getClass().getSimpleName());

        RootBeanDefinition bd = new RootBeanDefinition();
        bd.setTargetType(Ghost.class);

        Random random = new Random();
        bd.setInstanceSupplier(() -> Proxy.newProxyInstance(
                Ghost.class.getClassLoader(),
                new Class<?>[]{Ghost.class},
                (proxy, method, args) -> {
                    if ("hola".equals(method.getName())) {
                        return "Привет, " + random.nextInt(100);
                    }
                    return null;
                }
        ));
        registry.registerBeanDefinition("holaBean", bd);

        log.info("кол-во BeanDefinition: {}", registry.getBeanDefinitionCount());
        log.info("Зарегистрированные имена: {}", Arrays.toString(registry.getBeanDefinitionNames()));

        log.info("Второй BDRPP {}: Конец работы", this.getClass().getSimpleName());
        log.info("--------------- КОНЕЦ РАБОТЫ BDRPP ---------------");
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        log.info("вызов {}", this.getClass().getSimpleName());
    }
}

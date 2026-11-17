package io.github.aeyyks;

import io.github.aeyyks.config.AppConfig;
import io.github.aeyyks.config.condition.AppRunner;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Map;

public class MainApp {
    public static void main(String[] args) {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class)) {
            Map<String, AppRunner> runners = context.getBeansOfType(AppRunner.class);
            runners.values().forEach(AppRunner::run);
        }
    }
}

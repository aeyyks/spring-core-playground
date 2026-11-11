package io.github.aeyyks;

import io.github.aeyyks.service.Ghost;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class MainApp {
    public static void main(String[] args) {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class)) {
            System.out.println("-------------------- APP --------------------");
            Ghost service1 = context.getBean(Ghost.class);
            System.out.println(service1.hola());
            Ghost service2 = context.getBean(Ghost.class);
            System.out.println(service2.hola());
        }
    }
}

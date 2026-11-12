package io.github.aeyyks;

import io.github.aeyyks.config.AppConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class MainApp {
    public static void main(String[] args) {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class)) {
//            System.out.println("-------------------- APP --------------------");
//            DynamicProxy service1 = context.getBean(DynamicProxy.class);
//            System.out.println(service1.hola());
//            DynamicProxy service2 = context.getBean(DynamicProxy.class);
//            System.out.println(service2.hola());

            MyService service = context.getBean(MyService.class);
            service.doEvent("aeyyks", "aeyyks.dev@gmail.com");
        }
    }
}

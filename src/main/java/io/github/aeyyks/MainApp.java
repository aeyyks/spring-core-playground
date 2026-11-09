package io.github.aeyyks;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class MainApp {
    public static void main(String[] args) {

        // 1. Инициализация и запуск контекста Spring
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class)) {

            // 2. Получение бина из контекста
            MyService myService = context.getBean(MyService.class);

            // 3. Использование бизнес-логики
            myService.doSomething();
        }
        // Блок try-with-resources автоматически закроет контекст при завершении работы приложения
    }
}

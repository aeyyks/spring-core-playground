package io.github.aeyyks.config;

import io.github.aeyyks.bfpp.MyBFPP;
import io.github.aeyyks.bfpp.bdrpp.FirstBDRPP;
import io.github.aeyyks.bfpp.bdrpp.SecondBDRPP;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.Executor;

@Configuration
@ComponentScan(basePackages = "io.github.aeyyks")
@EnableTransactionManagement
@EnableAsync
public class AppConfig implements AsyncConfigurer {

    @Bean
    public static FirstBDRPP first() {
        return new FirstBDRPP();
    }

    @Bean
    public static SecondBDRPP second() {
        return new SecondBDRPP();
    }

    @Bean
    public static MyBFPP finalAct() {
        return new MyBFPP();
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DummyTransactionManager();
    }

    // 2. Настраиваем пул потоков для @Async
    @Bean(name = "taskExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(5);
        executor.setThreadNamePrefix("AsyncEvent-"); // Префикс имени потока для наглядности в логах
        executor.setWaitForTasksToCompleteOnShutdown(true); // Ждем завершения задач при закрытии контекста
        executor.setAwaitTerminationSeconds(5);
        executor.initialize();
        return executor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (Throwable ex, Method method, Object... params) -> {
            String threadName = Thread.currentThread().getName();
            System.err.println("[" + threadName + "] [ASYNC ERROR] Ошибка в методе: " + method.getName());
            System.err.println("[" + threadName + "] [ASYNC ERROR] Причина: " + ex.getMessage());
            System.err.println("[" + threadName + "] [ASYNC ERROR] Переданные аргументы: " + Arrays.toString(params));
        };
    }
}

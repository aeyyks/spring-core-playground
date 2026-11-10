package io.github.aeyyks;

import com.example.demo.config.MyMetadataLoggingProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "io.github.aeyyks")
public class AppConfig {

    // КРИТИЧЕСКИ ВАЖНО: метод должен быть static!
    // Это позволяет Spring зарегистрировать BFPP до парсинга остальной части конфигурации
//    @Bean
//    public static MyMetadataLoggingProcessor metadataLoggingProcessor() {
//        return new MyMetadataLoggingProcessor();
//    }

    @Bean
    public static ScopeMutatorBeanFactoryPostProcessor scopeMutatorBeanFactoryPostProcessor() {
        return new ScopeMutatorBeanFactoryPostProcessor();
    }
}

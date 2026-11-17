package io.github.aeyyks.config;

import io.github.aeyyks.bfpp.config.BFPPConfig;
import io.github.aeyyks.event.config.AsyncConfig;
import io.github.aeyyks.event.config.TransactionManagerConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan(basePackages = "io.github.aeyyks")
@Import({BFPPConfig.class, TransactionManagerConfig.class, AsyncConfig.class})
@PropertySource("classpath:application.properties")
public class AppConfig {
}

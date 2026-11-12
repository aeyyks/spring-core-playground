package io.github.aeyyks.config;

import io.github.aeyyks.bfpp.MyBFPP;
import io.github.aeyyks.bfpp.bdrpp.FirstBDRPP;
import io.github.aeyyks.bfpp.bdrpp.SecondBDRPP;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@ComponentScan(basePackages = "io.github.aeyyks")
@EnableTransactionManagement
public class AppConfig {

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
}

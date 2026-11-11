package io.github.aeyyks;

import io.github.aeyyks.bfpp.bdrpp.FirstBDRPP;
import io.github.aeyyks.bfpp.MyBFPP;
import io.github.aeyyks.bfpp.bdrpp.SecondBDRPP;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "io.github.aeyyks")
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
}

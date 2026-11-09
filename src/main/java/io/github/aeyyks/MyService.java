package io.github.aeyyks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class MyService {

    private static final Logger log = LoggerFactory.getLogger(MyService.class);

    public void doSomething() {
        log.info("Контекст Spring успешно запущен, и бин работает!");
    }
}

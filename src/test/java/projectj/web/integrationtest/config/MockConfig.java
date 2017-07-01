package projectj.web.integrationtest.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import java.util.concurrent.Executor;

@Profile("test")
public class MockConfig {

    @Bean("commandBusThreadPool")
    public Executor commandBusExecutorPool() {
        return Runnable::run;
    }
}

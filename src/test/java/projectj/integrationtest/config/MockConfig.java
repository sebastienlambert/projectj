package projectj.integrationtest.config;


import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import projectj.integrationtest.RestClient;

import java.util.concurrent.Executor;

@Profile("test")
public class MockConfig {

    @Bean("commandBusThreadPool")
    public Executor commandBusExecutorPool() {
        return Runnable::run;
    }

    @Bean
    public RestClient restClient(TestRestTemplate restTemplate) {
        return new RestClient(restTemplate);
    }
}

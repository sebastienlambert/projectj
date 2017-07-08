package projectj.integrationtest.config;


import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import projectj.integrationtest.RestClient;
import projectj.integrationtest.UserFixture;
import projectj.integrationtest.UserProfileFixture;

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

    @Bean
    public UserFixture userFixture(RestClient restClient) {
        return new UserFixture(restClient);
    }

    @Bean
    public UserProfileFixture userProfileFixture(RestClient restClient) {
        return new UserProfileFixture(restClient);
    }
}

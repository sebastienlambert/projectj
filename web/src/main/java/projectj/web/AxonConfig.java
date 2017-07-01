package projectj.web;


import org.axonframework.commandhandling.AsynchronousCommandBus;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.commandhandling.gateway.DefaultCommandGateway;
import org.axonframework.common.transaction.TransactionManager;
import org.axonframework.messaging.interceptors.BeanValidationInterceptor;
import org.axonframework.messaging.interceptors.LoggingInterceptor;
import org.axonframework.messaging.interceptors.TransactionManagingInterceptor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


@Configuration
public class AxonConfig {

    @Profile("default")
    @Bean("commandBusThreadPool")
    public Executor commandBusExecutorPool() {
        return Executors.newCachedThreadPool();
    }

    @Bean
    public CommandBus commandBus(TransactionManager transactionManager,
                                 @Qualifier("commandBusThreadPool") Executor executor) {
        AsynchronousCommandBus commandBus = new AsynchronousCommandBus(executor);
        commandBus.registerDispatchInterceptor(new BeanValidationInterceptor<>());
        commandBus.registerHandlerInterceptor(new TransactionManagingInterceptor<>(transactionManager));
        commandBus.registerHandlerInterceptor(new LoggingInterceptor<>());
        return commandBus;
    }

    @Bean
    public CommandGateway commandGateway(CommandBus commandBus) {
        return new DefaultCommandGateway(commandBus);
    }

}

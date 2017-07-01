package projectj.web;


import org.axonframework.commandhandling.AsynchronousCommandBus;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.commandhandling.gateway.DefaultCommandGateway;
import org.axonframework.common.transaction.TransactionManager;
import org.axonframework.messaging.interceptors.BeanValidationInterceptor;
import org.axonframework.messaging.interceptors.LoggingInterceptor;
import org.axonframework.messaging.interceptors.TransactionManagingInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class AppConfig {

    @Bean
    public CommandBus commandBus(TransactionManager transactionManager) {
        AsynchronousCommandBus commandBus = new AsynchronousCommandBus();
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

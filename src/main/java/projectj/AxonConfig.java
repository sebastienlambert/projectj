package projectj;


import org.axonframework.commandhandling.AsynchronousCommandBus;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.commandhandling.gateway.DefaultCommandGateway;
import org.axonframework.common.jpa.EntityManagerProvider;
import org.axonframework.common.transaction.TransactionManager;
import org.axonframework.config.EventHandlingConfiguration;
import org.axonframework.eventhandling.tokenstore.TokenStore;
import org.axonframework.eventhandling.tokenstore.jpa.JpaTokenStore;
import org.axonframework.messaging.interceptors.BeanValidationInterceptor;
import org.axonframework.messaging.interceptors.LoggingInterceptor;
import org.axonframework.messaging.interceptors.TransactionManagingInterceptor;
import org.axonframework.serialization.Serializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import projectj.framework.axon.CommandEntityRepository;
import projectj.framework.axon.GuaranteedOrderCommandBus;
import projectj.query.user.UserEventListener;

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
                                 @Qualifier("commandBusThreadPool") Executor executor,
                                 CommandEntityRepository commandEntityRepository) {
        AsynchronousCommandBus wrappedCommandBus = new AsynchronousCommandBus(executor);
        wrappedCommandBus.registerDispatchInterceptor(new BeanValidationInterceptor<>());
        wrappedCommandBus.registerHandlerInterceptor(new TransactionManagingInterceptor<>(transactionManager));
        wrappedCommandBus.registerHandlerInterceptor(new LoggingInterceptor<>());
        return new GuaranteedOrderCommandBus(wrappedCommandBus, commandEntityRepository);
    }

    @Bean
    public CommandGateway commandGateway(CommandBus commandBus) {
        return new DefaultCommandGateway(commandBus);
    }


    @Profile("default")
    @Configuration
    static class AxonEventHandlingConfig {
        @Autowired
        public void configure(EventHandlingConfiguration configuration) {
            // configuration.usingTrackingProcessors();
            configuration.registerTrackingProcessor(UserEventListener.class.getPackage().getName());
        }

        @Bean
        public TokenStore tokenStore(EntityManagerProvider entityManagerProvider, Serializer serializer) {
            return new JpaTokenStore(entityManagerProvider, serializer);
        }
    }


}
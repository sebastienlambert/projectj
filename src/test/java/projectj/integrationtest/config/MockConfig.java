package projectj.integrationtest.config;


import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.KeyFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Profile("test")
public class MockConfig {

    @Bean("commandBusThreadPool")
    public Executor commandBusExecutorPool() {
        return Runnable::run;
    }


    @Bean
    public Datastore mockDatastore() {
        Datastore datastore = mock(Datastore.class);

        KeyFactory keyFactory = new KeyFactory("projectj");
        when(datastore.newKeyFactory()).thenReturn(keyFactory);
        
        List<Entity> entities = new ArrayList<>();
        when(datastore.add(any(Entity.class))).thenAnswer(
                invocation -> {
                    Entity entity = invocation.getArgumentAt(0, Entity.class);
                    entities.add(entity);
                    return entity;
                });
        when(datastore.get(any(Key.class))).thenAnswer(
                invocation -> {
                    Key key = invocation.getArgumentAt(0, Key.class);
                    Entity entity = entities.stream().filter(e -> e.getKey().equals(key)).findFirst().orElse(null);
                    return entity;
                });
        return datastore;
    }
}

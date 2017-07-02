package projectj.infrastructure;


import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import org.springframework.context.annotation.Bean;

public class DatastoreConfig {

    @Bean
    public Datastore datastore() {
        return DatastoreOptions.getDefaultInstance().getService();
    }
}

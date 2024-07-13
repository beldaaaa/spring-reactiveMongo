package springframework.springreactivemongo.config;

import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;

import static java.util.Collections.singletonList;


@Configuration
public class MongoConfig extends AbstractReactiveMongoConfiguration {

    @Value("${mongoooo.host}")
    private String mongoHost;

    @Bean
    MongoClient mongoClient() {
        return MongoClients.create();//("mongodb://localhost:27017/test");
    }

    @Override
    protected String getDatabaseName() {
        return "Mongoooo";
    }
//settings for Docker
    @Override
    protected void configureClientSettings(MongoClientSettings.Builder builder) {
        builder.credential(MongoCredential.
                        createCredential("root", "admin", "example".toCharArray()))
                .applyToClusterSettings(settings ->
                        settings.hosts(singletonList(new ServerAddress(mongoHost))));
    }
}

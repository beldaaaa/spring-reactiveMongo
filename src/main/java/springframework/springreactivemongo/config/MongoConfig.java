package springframework.springreactivemongo.config;

import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;


@Configuration
public class MongoConfig extends AbstractReactiveMongoConfiguration {

    @Bean
    MongoClient mongoClient() {
        return MongoClients.create();//("mongodb://localhost:27017/test");
    }

    @Override
    protected String getDatabaseName() {
        return "isThisDatabaseName";
    }
////settings for Docker
//    @Override
//    protected void configureClientSettings(MongoClientSettings.Builder builder) {
//        builder.credential(MongoCredential.
//                        createCredential("root", "admin", "example".toCharArray()))
//                .applyToClusterSettings(settings ->
//                        settings.hosts(Collections.singletonList(new ServerAddress("localhost", 27017))));
//    }
}

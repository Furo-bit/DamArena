package com.project.damarena;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.connection.ConnectionPoolSettings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * <p>MongoConfig class.</p>
 *
 * @author Andrea Furini
 * @version $Id: $Id
 */
@Configuration
public class MongoConfig {
    /**
     * <p>mongoClient.</p>
     *
     * @return a {@link com.mongodb.client.MongoClient} object
     */
    @Bean
    public MongoClient mongoClient() {
        return MongoClients.create("mongodb+srv://mcarissimi4:UftUhDGcsv8DxVpx@users.yspav9e.mongodb.net/?retryWrites=true&w=majority&appName=Users&ssl=true"); // Change this URL as per your MongoDB configuration
    }
}

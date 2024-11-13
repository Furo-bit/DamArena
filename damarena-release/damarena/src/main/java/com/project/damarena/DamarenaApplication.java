package com.project.damarena;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * <p>DamarenaApplication class.</p>
 *
 * @author Andrea Furini
 * @version $Id: $Id
 */
@SpringBootApplication
@EnableMongoRepositories
@EnableScheduling
public class DamarenaApplication {

	/**
	 * <p>main.</p>
	 *
	 * @param args an array of {@link java.lang.String} objects
	 */
	public static void main(String[] args) {

        SpringApplication.run(DamarenaApplication.class, args);
    }

}

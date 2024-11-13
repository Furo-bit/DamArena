package com.project.damarena;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.project.damarena.model.User;
import com.project.damarena.service.UserService;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static com.mongodb.client.model.Filters.eq;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class AccountCreationIntegrationTest {

    @Autowired
    private UserService userService;

    @Test
    public void testAccountCreation() {
        //Create user to add
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        User testUser = new User(
                "testName",
                "testSurname",
                "testUsername",
                "testEmail",
                "testPassword");

        //Add new user
        userService.addUser(testUser);

        // Retrieve the newly created document from MongoDB Atlas collection
        MongoConfig mongoConfig = new MongoConfig();
        MongoClient mongoClient = mongoConfig.mongoClient();
        User retrievedUser = DatabaseUtils.getUserFromEmail(testUser.getEmail());

        // Assert that the document exists and contains the expected data
        assertNotNull(retrievedUser);
        assertEquals(testUser.getName(), retrievedUser.getName());
        assertEquals(testUser.getSurname(), retrievedUser.getSurname());
        assertEquals(testUser.getUsername(), retrievedUser.getUsername());
        assertEquals(testUser.getEmail(), retrievedUser.getEmail());
        assertEquals(testUser.getPassword(), retrievedUser.getPassword());

        //Delete newly created document
        MongoDatabase database = mongoClient.getDatabase("Users");
        MongoCollection<Document> collection = database.getCollection("Users_collection");

        // Define the query criteria (example: find by username)
        Bson query = eq("_id", testUser.getEmail());

        // Delete the document matching the query
        collection.deleteOne(query);
        System.out.println("Document deleted successfully");

    }
}


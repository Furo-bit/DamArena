package com.project.damarena;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.project.damarena.model.User;
import org.bson.Document;

import java.util.Map;

/**
 * <p>DatabaseUtils class.</p>
 *
 * @author Andrea Furini
 * @version $Id: $Id
 */
public class DatabaseUtils {
    /**
     * <p>addUser.</p>
     *
     * @param mongoClient a {@link com.mongodb.client.MongoClient} object
     * @param user a {@link com.project.damarena.model.User} object
     */
    public static void addUser(MongoClient mongoClient, User user) {
        // Create a new document
        Document document = new Document()
                .append("name", user.getName())
                .append("surname", user.getSurname())
                .append("email", user.getEmail())
                .append("username", user.getUsername())
                .append("password", user.getPassword());


        // Get the database
        MongoDatabase database = mongoClient.getDatabase("Users");

        // Get the collection
        MongoCollection<Document> collection = database.getCollection("Users_collection");

        // Insert the document
        collection.insertOne(document);

        // Close the connection (optional - connection pool might handle it)
        mongoClient.close();

        System.out.println("User added successfully!");

    }


    /**
     * <p>getUserFromEmail.</p>
     *
     * @param userEmail a {@link java.lang.String} object
     * @return a {@link com.project.damarena.model.User} object
     */
    public static User getUserFromEmail(String userEmail) {
        MongoConfig mongoConfig = new MongoConfig();
        MongoClient mongoClient = mongoConfig.mongoClient();
        MongoDatabase database = mongoClient.getDatabase("Users");

        // Access the collection
        MongoCollection<Document> collection = database.getCollection("Users_collection");

        // Query to find the document you want
        Document query = new Document("_id", userEmail);

        // Retrieve the document
        Document document = collection.find(query).first();

        if(document == null){
            System.out.println("No user found for this email");
            return null;
        } else {
            System.out.println("User found! PSW = " + document.getString("password"));
        }

        User retrievedUser = new User(
                document.getString("name"),
                document.getString("surname"),
                document.getString("username"),
                document.getString("_id"),
                document.getString("password")
        );

        retrievedUser.setPassword(document.getString("password"));
        return retrievedUser;
    }
}

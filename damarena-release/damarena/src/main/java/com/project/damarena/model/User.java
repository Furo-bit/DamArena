package com.project.damarena.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.*;

/**
 * <p>User class.</p>
 *
 * @author Andrea Furini
 * @version $Id: $Id
 */
@Document(collection = "Users_collection")
public class User {
    private String name;
    private String surname;
    private String username;
    @Id
    private String email;
    private String password;
    private Map<String, Integer> eloScores;
    private byte[] profilePicture;
    private List<Notification> notifications = new ArrayList<>();

    /**
     * <p>Constructor for User.</p>
     *
     * @param name a {@link java.lang.String} object
     * @param surname a {@link java.lang.String} object
     * @param username a {@link java.lang.String} object
     * @param email a {@link java.lang.String} object
     * @param password a {@link java.lang.String} object
     */
    public User(String name, String surname, String username, String email, String password) {
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.email = email;
        this.password = password;
        this.eloScores = new HashMap<>();
        this.eloScores.put("checkers-bullet", 500);
        this.eloScores.put("checkers-blitz", 500);
        this.eloScores.put("checkers-rapid", 500);
    }

    /**
     * <p>Getter for the field <code>eloScores</code>.</p>
     *
     * @return a {@link java.util.Map} object
     */
    public Map<String, Integer> getEloScores() {
        return eloScores;
    }

    public void updateEloScore(String format, int increment) {
        eloScores.put(format, eloScores.get(format) + increment);
    }

    /**
     * <p>Getter for the field <code>name</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getName() {
        return name;
    }

    /**
     * <p>Getter for the field <code>surname</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getSurname() {
        return surname;
    }

    /**
     * <p>Getter for the field <code>username</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getUsername() {
        return username;
    }

    /**
     * <p>Setter for the field <code>username</code>.</p>
     *
     * @param username a {@link java.lang.String} object
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * <p>Getter for the field <code>email</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    /**
     * <p>Setter for the field <code>password</code>.</p>
     *
     * @param password a {@link java.lang.String} object
     */
    public void setPassword(String password){
        this.password = password;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(email, user.email);
    }

    /**
     * <p>Getter for the field <code>profilePicture</code>.</p>
     *
     * @return an array of {@link byte} objects
     */
    public byte[] getProfilePicture() {
        return profilePicture;
    }

    /**
     * <p>Setter for the field <code>profilePicture</code>.</p>
     *
     * @param profilePicture an array of {@link byte} objects
     */
    public void setProfilePicture(byte[] profilePicture) {
        this.profilePicture = profilePicture;
    }

    public List<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
    }
}

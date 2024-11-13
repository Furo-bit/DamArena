package com.project.damarena.model;

import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * <p>Friend class.</p>
 *
 * @author Andrea Furini
 * @version $Id: $Id
 */
@Document(collection = "friend_list")
public class Friend {

    private String id;
    private String userId;
    private String friendId;
    private LocalDateTime dateAdded;

    /**
     * <p>Constructor for Friend.</p>
     */
    public Friend() {
    }

    /**
     * <p>Constructor for Friend.</p>
     *
     * @param id a {@link java.lang.String} object
     * @param userId a {@link java.lang.String} object
     * @param friendId a {@link java.lang.String} object
     * @param dateAdded a {@link java.time.LocalDateTime} object
     */
    public Friend(String id, String userId, String friendId, LocalDateTime dateAdded) {
        this.id = id;
        this.userId = userId;
        this.friendId = friendId;
        this.dateAdded = dateAdded;
    }

    /**
     * <p>Getter for the field <code>id</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getId() {
        return id;
    }

    /**
     * <p>Setter for the field <code>id</code>.</p>
     *
     * @param id a {@link java.lang.String} object
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * <p>Getter for the field <code>userId</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getUserId() {
        return userId;
    }

    /**
     * <p>Setter for the field <code>userId</code>.</p>
     *
     * @param userId a {@link java.lang.String} object
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * <p>Getter for the field <code>friendId</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getFriendId() {
        return friendId;
    }

    /**
     * <p>Setter for the field <code>friendId</code>.</p>
     *
     * @param friendId a {@link java.lang.String} object
     */
    public void setFriendId(String friendId) {
        this.friendId = friendId;
    }

    /**
     * <p>Getter for the field <code>dateAdded</code>.</p>
     *
     * @return a {@link java.time.LocalDateTime} object
     */
    public LocalDateTime getDateAdded() {
        return dateAdded;
    }

    /**
     * <p>Setter for the field <code>dateAdded</code>.</p>
     *
     * @param dateAdded a {@link java.time.LocalDateTime} object
     */
    public void setDateAdded(LocalDateTime dateAdded) {
        this.dateAdded = dateAdded;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return "Friend{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", friendId='" + friendId + '\'' +
                ", dateAdded=" + dateAdded +
                '}';
    }
}

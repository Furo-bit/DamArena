package com.project.damarena.service;

import com.project.damarena.model.User;
import com.project.damarena.repository.UserRepository;
import com.project.damarena.model.Notification;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>UserService class.</p>
 *
 * @author Andrea Furini
 * @version $Id: $Id
 */
@Service
public class UserService {

    private final MongoTemplate mongoTemplate;

    private final UserRepository userRepository;

    public UserService(MongoTemplate mongoTemplate, UserRepository userRepository) {
        this.mongoTemplate = mongoTemplate;
        this.userRepository = userRepository;
    }

    /**
     * <p>addUser.</p>
     *
     * @param user a {@link com.project.damarena.model.User} object
     */
    public void addUser(User user) {
        mongoTemplate.save(user);
        System.out.println("User added successfully!");
    }

    /**
     * <p>addUser.</p>
     *
     * @param user a {@link com.project.damarena.model.User} object
     * @param profilePicture an array of {@link byte} objects
     */
    public void addUser(User user, byte[] profilePicture) {
        if (profilePicture != null && profilePicture.length > 0) {
            user.setProfilePicture(profilePicture);
        }
        userRepository.save(user);
    }

    /**
     * <p>getProfilePicture.</p>
     *
     * @param email a {@link java.lang.String} object
     * @return an array of {@link byte} objects
     */
    public byte[] getProfilePicture(String email) {

        Query query = new Query(Criteria.where("_id").is(email));

        User user = mongoTemplate.findOne(query, User.class);

        if (user != null && user.getProfilePicture() != null) {
            return user.getProfilePicture();
        }

        return null;
    }

    /**
     * <p>getAllUsers.</p>
     *
     * @return a {@link java.util.List} object
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * <p>authenticateUser.</p>
     *
     * @param userEmail a {@link java.lang.String} object
     * @param password a {@link java.lang.String} object
     * @return a boolean
     */
    public boolean authenticateUser(String userEmail, String password) {
        User user = userRepository.findByEmail(userEmail);
        if(user == null) {
            return false; }

        return password.equals(user.getPassword());
    }

    /**
     * <p>findUsersByUsername.</p>
     *
     * @param username a {@link java.lang.String} object
     * @return a {@link java.util.List} object
     */
    public List<User> findUsersByUsername(String username) {
        return userRepository.findByUsernameContaining(username);
    }

    public User findUsersByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Notification findNotificationById(String notificationId) {
        User user = userRepository.findByNotificationId(notificationId);
        if (user != null) {
            for (Notification notification : user.getNotifications()) {
                if (notification.getId().equals(notificationId)) {
                    return notification;
                }
            }
        }
        return null;
    }

    public void addNotification(User user, Notification notification) {

        List<Notification> currentNotifications = user.getNotifications();

        currentNotifications.add(notification);

        user.setNotifications(currentNotifications);
        userRepository.save(user);
    }

    public boolean deleteNotification(String email, String notificationId) {
        Query query = new Query(Criteria.where("email").is(email));
        User user = mongoTemplate.findOne(query, User.class);

        if (user != null) {
            List<Notification> currentNotifications = user.getNotifications();
            boolean removed = currentNotifications.removeIf(n -> n.getId().equals(notificationId));

            if (removed) {
                user.setNotifications(currentNotifications);
                userRepository.save(user);
                return true;
            }
        }
        return false;
    }

    public void deleteAccount(String userId) {
        userRepository.deleteByEmail(userId);
    }
}


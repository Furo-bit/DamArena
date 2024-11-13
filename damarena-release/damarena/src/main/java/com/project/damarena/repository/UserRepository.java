package com.project.damarena.repository;

import com.project.damarena.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>UserRepository interface.</p>
 *
 * @author Andrea Furini
 * @version $Id: $Id
 */
@Repository
public interface UserRepository extends MongoRepository<User, String> {
    /**
     * <p>findByUsernameContaining.</p>
     *
     * @param username a {@link java.lang.String} object
     * @return a {@link java.util.List} object
     */
    List<User> findByUsernameContaining(String username);

    @Query("{ 'notifications.id': ?0 }")
    User findByNotificationId(String notificationId);

    User findByEmail(String email);
    User findByUsername(String username);
    void deleteByEmail(String email);

    boolean existsByEmail(String email);
}


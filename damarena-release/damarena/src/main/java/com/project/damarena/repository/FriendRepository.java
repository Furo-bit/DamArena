package com.project.damarena.repository;

import com.project.damarena.model.Friend;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>FriendRepository interface.</p>
 *
 * @author Andrea Furini
 * @version $Id: $Id
 */
@Repository
public interface FriendRepository extends MongoRepository<Friend, String> {
    /**
     * <p>existsByUserIdAndFriendId.</p>
     *
     * @param userId a {@link java.lang.String} object
     * @param friendId a {@link java.lang.String} object
     * @return a boolean
     */
    boolean existsByUserIdAndFriendId(String userId, String friendId);

    /**
     * <p>deleteByUserIdAndFriendId.</p>
     *
     * @param userId a {@link java.lang.String} object
     * @param friendId a {@link java.lang.String} object
     */
    void deleteByUserIdAndFriendId(String userId, String friendId);

    /**
     * <p>findByUserId.</p>
     *
     * @param userEmail a {@link java.lang.String} object
     * @return a {@link java.util.List} object
     */
    List<Friend> findByUserId(String userEmail);

    List<Friend> findByFriendId(String friendId);
}

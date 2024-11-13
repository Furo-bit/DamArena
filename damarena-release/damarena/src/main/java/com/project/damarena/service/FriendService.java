package com.project.damarena.service;

import com.project.damarena.DatabaseUtils;
import com.project.damarena.model.Friend;
import com.project.damarena.model.User;
import com.project.damarena.repository.FriendRepository;
import com.project.damarena.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>FriendService class.</p>
 *
 * @author Andrea Furini
 * @version $Id: $Id
 */
@Service
public class FriendService {

    private final FriendRepository friendRepository;
    private final UserRepository userRepository;

    public FriendService(FriendRepository friendRepository, UserRepository userRepository) {
        this.friendRepository = friendRepository;
        this.userRepository = userRepository;
    }

    public void addFriend(String userId, String friendId) {
        if (friendRepository.existsByUserIdAndFriendId(userId, friendId)) {
            return;
        }

        Friend friend = new Friend();
        friend.setUserId(userId);
        friend.setFriendId(friendId);
        friend.setDateAdded(LocalDateTime.now());

        friendRepository.save(friend);
    }

    public boolean areFriends(String userId, String friendId) {
        return friendRepository.existsByUserIdAndFriendId(userId, friendId);
    }

    /**
     * <p>removeFriend.</p>
     *
     * @param userId a {@link java.lang.String} object
     * @param friendId a {@link java.lang.String} object
     */
    public void removeFriend(String userId, String friendId) {
        friendRepository.deleteByUserIdAndFriendId(userId, friendId);
    }

    /**
     * <p>getFriendsOfUser.</p>
     *
     * @param userEmail a {@link java.lang.String} object
     * @return a {@link java.util.List} object
     */
    public List<User> getFriendsOfUser(String userEmail) {
        List<Friend> friendships = friendRepository.findByUserId(userEmail);
        List<User> friends = new ArrayList<>();
        for (Friend friend : friendships) {
            friends.add(userRepository.findByEmail(friend.getFriendId()));
        }
        return friends;
    }

    public void deleteAllFriends(String userId) {
        List<Friend> followers = friendRepository.findByFriendId(userId);
        friendRepository.deleteAll(followers);
        List<Friend> friends = friendRepository.findByUserId(userId);
        friendRepository.deleteAll(friends);
    }

}

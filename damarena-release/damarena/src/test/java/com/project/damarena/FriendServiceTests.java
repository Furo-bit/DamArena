package com.project.damarena;

import com.project.damarena.controller.UserController;
import com.project.damarena.model.User;
import com.project.damarena.repository.FriendRepository;
import com.project.damarena.repository.UserRepository;
import com.project.damarena.service.FriendService;
import com.project.damarena.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.bind.annotation.RequestParam;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FriendServiceTests {

    @Autowired
    private FriendService friendService;

    @Autowired
    private FriendRepository friendRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserController userController;

    @Test
    public void testAddFriend() {

        String userId = "user1@mail";
        String friendId = "user2@mail";

        friendService.addFriend(userId, friendId);

        assertTrue(friendRepository.existsByUserIdAndFriendId(userId, friendId));
        friendService.removeFriend(userId, friendId);
    }

    @Test
    public void testRemoveFriend() {

        String userId = "user1@mail";
        String friendId = "user2@mail";
        friendService.addFriend(userId, friendId);

        friendService.removeFriend(userId, friendId);

        assertFalse(friendRepository.existsByUserIdAndFriendId(userId, friendId));
    }

    @Test
    public void testDeleteUser(){
        MockHttpServletRequest request = new MockHttpServletRequest();

        User testUser = new User(
                "testName",
                "testSurname",
                "testUsername",
                "testEmail",
                "testPassword");

        userService.addUser(testUser);
        String friendId = "user2@mail";
        friendService.addFriend(testUser.getEmail(), friendId);
        friendService.addFriend(friendId, testUser.getEmail());

        userController.deleteUser(testUser.getEmail(), request);

        assertFalse(userRepository.existsByEmail(testUser.getEmail()));
        assertFalse(friendRepository.existsByUserIdAndFriendId(testUser.getEmail(), friendId));
        assertFalse(friendRepository.existsByUserIdAndFriendId(friendId, testUser.getEmail()));

    }
}


package com.project.damarena;

import com.project.damarena.model.Notification;
import com.project.damarena.model.User;
import com.project.damarena.repository.UserRepository;
import com.project.damarena.service.UserService;
import org.bson.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTests {

    @Mock
    private MongoTemplate mongoTemplate;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddUser() {
        User user = new User(
                "testName",
                "testSurname",
                "testUsername",
                "testEmail",
                "testPassword");
        user.setEmail("test@example.com");

        userService.addUser(user);

        verify(mongoTemplate, times(1)).save(user);
    }

    @Test
    public void testAddUserWithProfilePicture() {
        User user = new User(
                "testName",
                "testSurname",
                "testUsername",
                "testEmail",
                "testPassword");
        user.setEmail("test@example.com");
        byte[] profilePicture = new byte[]{1, 2, 3};

        userService.addUser(user, profilePicture);

        assertArrayEquals(profilePicture, user.getProfilePicture());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testGetProfilePicture() {
        User user = new User(
                "testName",
                "testSurname",
                "testUsername",
                "testEmail",
                "testPassword");
        user.setEmail("test@example.com");
        byte[] profilePicture = new byte[]{1, 2, 3};
        user.setProfilePicture(profilePicture);

        when(mongoTemplate.findOne(any(Query.class), eq(User.class))).thenReturn(user);

        byte[] result = userService.getProfilePicture("test@example.com");

        assertArrayEquals(profilePicture, result);
    }

    @Test
    public void testGetProfilePictureNotFound() {
        when(mongoTemplate.findOne(any(Query.class), eq(User.class))).thenReturn(null);

        byte[] result = userService.getProfilePicture("test@example.com");

        assertNull(result);
    }

    @Test
    public void testAuthenticateUserWrongPassword() {
        User user = new User(
                "testName",
                "testSurname",
                "testUsername",
                "testEmail",
                "testPassword");
        user.setEmail("test@example.com");
        user.setPassword("password");

        DatabaseUtils databaseUtilsMock = mock(DatabaseUtils.class);
        when(userRepository.findByEmail("test@example.com")).thenReturn(user);

        boolean result = userService.authenticateUser("test@example.com", "wrongpassword");

        assertFalse(result);
    }

    @Test
    public void testAuthenticateUserNotFound() {
        DatabaseUtils databaseUtilsMock = mock(DatabaseUtils.class);
        when(userRepository.findByEmail("test@example.com")).thenReturn(null);

        boolean result = userService.authenticateUser("test@example.com", "password");

        assertFalse(result);
    }

    @Test
    public void testFindUsersByUsername() {
        List<User> users = new ArrayList<>();
        users.add(new User(
                "testName",
                "testSurname",
                "User1",
                "user1@example.com",
                "testPassword"));
        users.add(new User(
                "testName",
                "testSurname",
                "User2",
                "user2@example.com",
                "testPassword"));

        when(userRepository.findByUsernameContaining("User")).thenReturn(users);

        List<User> result = userService.findUsersByUsername("User");

        assertEquals(2, result.size());
        assertEquals("user1@example.com", result.get(0).getEmail());
        assertEquals("user2@example.com", result.get(1).getEmail());
    }

    @Test
    public void testFindUsersByEmail() {
        User user = new User(
                "testName",
                "testSurname",
                "User",
                "user@example.com",
                "testPassword");

        when(userRepository.findByEmail("user@example.com")).thenReturn(user);

        User result = userService.findUsersByEmail("user@example.com");

        assertEquals("user@example.com", result.getEmail());
    }

    @Test
    public void testFindNotificationById() {
        User user = new User(
                "testName",
                "testSurname",
                "User1",
                "user1@example.com",
                "testPassword");
        Notification notification = new Notification();
        notification.setId("notif1");
        user.getNotifications().add(notification);

        when(userRepository.findByNotificationId("notif1")).thenReturn(user);

        Notification result = userService.findNotificationById("notif1");

        assertEquals("notif1", result.getId());
    }

    @Test
    public void testAddNotification() {
        User user = new User("testName",
                "testSurname",
                "User1",
                "user1@example.com",
                "testPassword");
        Notification notification = new Notification();
        notification.setId("notif1");

        userService.addNotification(user, notification);

        assertEquals(1, user.getNotifications().size());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testDeleteNotification() {
        User user = new User("testName",
                "testSurname",
                "User1",
                "user1@example.com",
                "testPassword");
        user.setEmail("user@example.com");
        Notification notification = new Notification();
        notification.setId("notif1");
        user.getNotifications().add(notification);

        when(mongoTemplate.findOne(any(Query.class), eq(User.class))).thenReturn(user);

        boolean result = userService.deleteNotification("user@example.com", "notif1");

        assertTrue(result);
        assertEquals(0, user.getNotifications().size());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testDeleteNotificationNotFound() {
        when(mongoTemplate.findOne(any(Query.class), eq(User.class))).thenReturn(null);

        boolean result = userService.deleteNotification("user@example.com", "notif1");

        assertFalse(result);
    }

    @Test
    public void testDeleteAccount() {
        userService.deleteAccount("user@example.com");

        verify(userRepository, times(1)).deleteByEmail("user@example.com");
    }
}

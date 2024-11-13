package com.project.damarena;

import com.project.damarena.controller.UserController;
import com.project.damarena.model.User;
import com.project.damarena.service.FriendService;
import com.project.damarena.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class UserControllerTests {

    @Mock
    private HttpServletRequest request;

    @Mock
    private UserService userService;

    @Mock
    private FriendService friendService;

    @Mock
    private Model model;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddUser() throws Exception {

        UserService mockUserService = Mockito.mock(UserService.class);

        User user = new User("John",
                "Doe",
                "johndoe",
                "johndoe@example.com",
                "password");

        UserController userController = new UserController(mockUserService, null);

        userController.addUser(user);

        verify(mockUserService).addUser(user);
    }

    @Test
    public void testProfilePage_Success() throws Exception {
        // Mock session with userEmail
        HttpSession session = mock(HttpSession.class);
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("userEmail")).thenReturn("test@email.com");

        // Mock userService and friendService
        User user = new User("John",
                "Doe",
                "johndoe",
                "johndoe@example.com",
                "password");
        List<User> friends = new ArrayList<>();
        when(userService.findUsersByEmail("test@email.com")).thenReturn(user);
        when(friendService.getFriendsOfUser("test@email.com")).thenReturn(friends);

        // Call the method
        UserController controller = new UserController(userService, friendService);
        String viewName = controller.profilePage(request, model);

        // Assert expectations
        assertEquals("account/profile", viewName);
        verify(model).addAttribute("user", user);
        verify(model).addAttribute("friends", friends);
    }

}

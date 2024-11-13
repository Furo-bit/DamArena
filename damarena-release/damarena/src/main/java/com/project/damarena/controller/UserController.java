package com.project.damarena.controller;

import com.project.damarena.model.User;
import com.project.damarena.service.FriendService;
import com.project.damarena.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * <p>UserController class.</p>
 *
 * @author Andrea Furini
 * @version $Id: $Id
 */
@Controller
public class UserController {

    private final UserService userService;
    private final FriendService friendService;

    public UserController(UserService userService, FriendService friendService) {
        this.userService = userService;
        this.friendService = friendService;
    }

    /**
     * <p>addUser.</p>
     *
     * @param inputUser a {@link com.project.damarena.model.User} object
     * @return a {@link java.lang.String} object
     */
    @PostMapping(value = "/add-user")
    public String addUser(User inputUser) {
        User user = new User(
                inputUser.getName(),
                inputUser.getSurname(),
                inputUser.getUsername(),
                inputUser.getEmail(),
                inputUser.getPassword());
        userService.addUser(user);
        return "redirect:/";

    }

    /**
     * <p>profilePage.</p>
     *
     * @param request a {@link jakarta.servlet.http.HttpServletRequest} object
     * @param model   a {@link org.springframework.ui.Model} object
     * @return a {@link java.lang.String} object
     */
    @GetMapping("/profile")
    public String profilePage(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);
        String userEmail = (String) session.getAttribute("userEmail");
        User user = userService.findUsersByEmail(userEmail);

        List<User> friends = friendService.getFriendsOfUser(userEmail);
        model.addAttribute("user", user);
        model.addAttribute("username", user.getUsername());
        model.addAttribute("email", userEmail);

        byte[] profilePicture = (byte[]) session.getAttribute("profilePicture");

        if (profilePicture == null || profilePicture.length == 0) {
            profilePicture = userService.getProfilePicture(userEmail);
        }
        if (profilePicture != null && profilePicture.length > 0) {
            String base64Image = Base64.getEncoder().encodeToString(profilePicture);
            model.addAttribute("profilePicture", base64Image);

            System.out.println("Profile picture is not empty ");

        } else {
            System.out.println("Profile picture is empty");
        }
        model.addAttribute("friends", friends);

        for(int i = 0; i < friends.size(); i++) {
            System.out.println("Friend: "+ friends.get(i).getUsername() + " " + friends.get(i).getEloScores());
        }

        return "account/profile";
    }

    /**
     * <p>uploadProfilePicture.</p>
     *
     * @param request a {@link jakarta.servlet.http.HttpServletRequest} object
     * @param file    a {@link org.springframework.web.multipart.MultipartFile} object
     * @param model   a {@link org.springframework.ui.Model} object
     * @return a {@link java.lang.String} object
     */
    @PostMapping("/profile/uploadPicture")
    public String uploadProfilePicture(HttpServletRequest request, @RequestParam("file") MultipartFile file, Model model) {
        if (file.isEmpty()) {
            System.out.println("empty");
            return "redirect:/";
        }

        HttpSession session = request.getSession(false);
        String userEmail = (String) session.getAttribute("userEmail");

        try {
            System.out.println("not empty");
            byte[] imageData = file.getBytes();

            User user = userService.findUsersByEmail(userEmail);
            if (user != null) {
                user.setProfilePicture(imageData);
            }
            userService.addUser(user, imageData);

            session.setAttribute("profilePicture", imageData);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return "redirect:/profile";
    }


    /**
     * <p>searchUsersByUsername.</p>
     *
     * @param request  a {@link jakarta.servlet.http.HttpServletRequest} object
     * @param username a {@link java.lang.String} object
     * @param model    a {@link org.springframework.ui.Model} object
     * @return a {@link java.lang.String} object
     */
    @GetMapping("/search")
    public String searchUsersByUsername(HttpServletRequest request, @RequestParam String username, Model model) {
        HttpSession session = request.getSession(false);
        model.addAttribute("isLoggedin", session.getAttribute("isLoggedIn"));
        String userEmail = (String) session.getAttribute("userEmail");
        model.addAttribute("userEmail", userEmail);
        User currentUser = userService.findUsersByEmail(userEmail);

        List<User> users = userService.findUsersByUsername(username);
        users.remove(currentUser);

        if (!users.isEmpty()) {
            model.addAttribute("users", users);
            Map<String, Boolean> friendMap = new HashMap<>();

            for (User user : users) {
                boolean isFriend = friendService.areFriends(userEmail, user.getEmail());
                friendMap.put(user.getEmail(), isFriend);
            }
            model.addAttribute("friendMap", friendMap);

            return "search/search-results";

        } else {
            return "search/user-not-found";
        }
    }

    /**
     * <p>userProfilePage.</p>
     *
     * @param request a {@link jakarta.servlet.http.HttpServletRequest} object
     * @param userId  a {@link java.lang.String} object
     * @param model   a {@link org.springframework.ui.Model} object
     * @return a {@link java.lang.String} object
     */
    @GetMapping("/user-profile")
    public String userProfilePage(HttpServletRequest request, @RequestParam("userId") String userId, Model model) {

        HttpSession session = request.getSession(false);
        model.addAttribute("isLoggedin", session.getAttribute("isLoggedIn"));
        String userEmail = (String) session.getAttribute("userEmail");
        model.addAttribute("email", userEmail);

        User userNow = userService.findUsersByEmail(userId);
        model.addAttribute("userNow", userNow);
        List<User> friends = friendService.getFriendsOfUser(userNow.getEmail());
        model.addAttribute("friends", friends);

        User user = userService.findUsersByEmail(userEmail);
        model.addAttribute("user", user);
        if (session.getAttribute("isLoggedIn") != null) {

            if (!friends.isEmpty()) {
                Map<String, Boolean> friendMap = new HashMap<>();

                for (User friend : friends) {
                    boolean isFriend = friendService.areFriends(userEmail, friend.getEmail());
                    friendMap.put(friend.getEmail(), isFriend);
                }
                model.addAttribute("friendMap", friendMap);
            }
        }


        byte[] profilePicture = userNow.getProfilePicture();

        if (profilePicture == null || profilePicture.length == 0) {
            profilePicture = userService.getProfilePicture(userId);
        }
        if (profilePicture != null && profilePicture.length > 0) {
            String base64Image = Base64.getEncoder().encodeToString(profilePicture);
            model.addAttribute("profilePicture", base64Image);

            System.out.println("Profile picture is not empty");

        } else {
            System.out.println("Profile picture is empty");
        }

        return "search/user-profile";
    }

    /**
     * <p>unfollowUser.</p>
     *
     * @param request  a {@link jakarta.servlet.http.HttpServletRequest} object
     * @param model    a {@link org.springframework.ui.Model} object
     * @param userId   a {@link java.lang.String} object
     * @param friendId a {@link java.lang.String} object
     * @return a {@link java.lang.String} object
     */
    @PostMapping("/unfollow")
    public String unfollowUser(HttpServletRequest request, Model model, @RequestParam String userId, @RequestParam String friendId) {
        HttpSession session = request.getSession(false);
        String userEmail = (String) session.getAttribute("userEmail");
        model.addAttribute("email", userEmail);
        friendService.removeFriend(userEmail, friendId);
        return "redirect:/";
    }

    @PostMapping("/delete-user")
    public String deleteUser(@RequestParam("userId") String userId, HttpServletRequest request) {
        friendService.deleteAllFriends(userId);
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        userService.deleteAccount(userId);
        return "redirect:/";
    }

}


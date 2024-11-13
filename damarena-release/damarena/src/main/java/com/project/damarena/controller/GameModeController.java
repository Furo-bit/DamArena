package com.project.damarena.controller;

import com.project.damarena.model.User;
import com.project.damarena.service.FriendService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * <p>GameModeController class.</p>
 *
 * @author Andrea Furini
 * @version $Id: $Id
 */
@Controller
public class GameModeController {

    private final FriendService friendService;

    /**
     * <p>Constructor for GameModeController.</p>
     *
     * @param friendService a {@link com.project.damarena.service.FriendService} object
     */
    public GameModeController(FriendService friendService) {
        this.friendService = friendService;
    }

    /**
     * <p>gameMode.</p>
     *
     * @param request a {@link jakarta.servlet.http.HttpServletRequest} object
     * @param model a {@link org.springframework.ui.Model} object
     * @return a {@link java.lang.String} object
     */
    @GetMapping("/game-mode")
    public String gameMode(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);
        model.addAttribute("isLoggedin", session.getAttribute("isLoggedIn"));
        return "lobby/game_mode";
    }
    /**
     * <p>singlePlayer.</p>
     *
     * @return a {@link java.lang.String} object
     */
    @GetMapping("/single-player")
    public String singlePlayer() {return "lobby/single_player";}
    /**
     * <p>playWithFriends.</p>
     *
     * @param request a {@link jakarta.servlet.http.HttpServletRequest} object
     * @param model a {@link org.springframework.ui.Model} object
     * @return a {@link java.lang.String} object
     */
    @GetMapping("/play-friends")
    public String playWithFriends(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);
        String userEmail = (String) session.getAttribute("userEmail");
        List<User> friends = friendService.getFriendsOfUser(userEmail);
        model.addAttribute("friends", friends);
        return "lobby/play_friends";
    }

    @GetMapping("/play-friend-options")
    public String showFriendGameOptionsPage(@RequestParam("friendId") String friendId, Model model) {
        model.addAttribute("friendId", friendId);
        return "lobby/play_friend_options";
    }

    @GetMapping("/play-online")
    public String showOnlineGameOptions() {
        return "lobby/play_online";
    }

}

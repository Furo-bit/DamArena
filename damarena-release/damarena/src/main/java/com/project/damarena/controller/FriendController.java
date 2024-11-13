package com.project.damarena.controller;

import com.project.damarena.service.FriendService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
/**
 * <p>FriendController class.</p>
 *
 * @author Andrea Furini
 * @version $Id: $Id
 */
@Controller
public class FriendController {

    private final FriendService friendService;

    public FriendController(FriendService friendService) {
        this.friendService = friendService;
    }

    /**
     * <p>addFriend.</p>
     *
     * @param request a {@link jakarta.servlet.http.HttpServletRequest} object
     * @param model a {@link org.springframework.ui.Model} object
     * @param userId a {@link java.lang.String} object
     * @param friendId a {@link java.lang.String} object
     * @return a {@link java.lang.String} object
     */
    @PostMapping("/add-friend")
    public String addFriend(HttpServletRequest request, Model model, @RequestParam String userId, @RequestParam String friendId) {
        HttpSession session = request.getSession(false);
        String userEmail = (String) session.getAttribute("userEmail");
        model.addAttribute("email", userEmail);
        friendService.addFriend(userEmail, friendId);
        return "redirect:/";
    }
}

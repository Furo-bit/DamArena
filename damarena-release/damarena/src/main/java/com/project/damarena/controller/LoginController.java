package com.project.damarena.controller;

import com.project.damarena.JwtTokenUtil;
import com.project.damarena.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * <p>LoginController class.</p>
 *
 * @author Andrea Furini
 * @version $Id: $Id
 */
@Controller
public class LoginController {

    private final UserService userService;

    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/create-account")
    public String home() {
        return "/account/create_account";
    }

    /**
     * <p>loginForm.</p>
     *
     * @return a {@link java.lang.String} object
     */
    @GetMapping("/login")
    public String loginForm() {
        return "/account/login";
    }

    /**
     * <p>login.</p>
     *
     * @param userEmail a {@link java.lang.String} object
     * @param userPassword a {@link java.lang.String} object
     * @param redirectAttributes a {@link org.springframework.web.servlet.mvc.support.RedirectAttributes} object
     * @param request a {@link jakarta.servlet.http.HttpServletRequest} object
     * @return a {@link java.lang.String} object
     */
    @PostMapping("/authenticate")
    public String login(String userEmail, String userPassword, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        boolean isAuthenticated = userService.authenticateUser(userEmail, userPassword);

        if (isAuthenticated) {
            JwtTokenUtil tokenUtil = new JwtTokenUtil();
            String token = tokenUtil.generateToken(userEmail);
            HttpSession session = request.getSession();
            session.setAttribute("token", token);
            session.setAttribute("isLoggedIn", true);
            session.setAttribute("userEmail", userEmail);
            return "redirect:/";
        } else {
            redirectAttributes.addFlashAttribute("loginError", true);
            return "redirect:/login";
        }

    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {

        HttpSession session = request.getSession(false);
        if (session != null) {

            session.invalidate();
        }

        return "redirect:/login";
    }
}

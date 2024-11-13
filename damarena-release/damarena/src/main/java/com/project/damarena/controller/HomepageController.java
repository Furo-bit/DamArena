package com.project.damarena.controller;

import com.project.damarena.model.Notification;
import com.project.damarena.model.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collections;
import java.util.List;

@Controller
public class HomepageController {


    private final MongoTemplate mongoTemplate;

    public HomepageController(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @GetMapping("/")
    public String index(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            String token = (String) session.getAttribute("token");
            if (token != null) {
                model.addAttribute("token", token);
            }
            boolean isLoggedIn = session.getAttribute("isLoggedIn") != null && (boolean) session.getAttribute("isLoggedIn");
            model.addAttribute("isLoggedIn", isLoggedIn);
            String userEmail = (String) session.getAttribute("userEmail");
            model.addAttribute("userEmail", userEmail);
            List<Notification> notifications = fetchNotifications(userEmail);
            model.addAttribute("notifications", notifications);
        } else {
            model.addAttribute("isLoggedIn", false);
        }

        return "index";
    }

    private List<Notification> fetchNotifications(String userEmail) {
        Query query = new Query(Criteria.where("email").is(userEmail));
        User user = mongoTemplate.findOne(query, User.class);
        if (user != null) {
            return user.getNotifications();
        } else {
            return Collections.emptyList();
        }
    }
}

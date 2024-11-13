package com.project.damarena.controller;

import com.project.damarena.model.User;
import com.project.damarena.service.TournamentService;
import com.project.damarena.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.bson.Document;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * <p>RankingsController class.</p>
 *
 * @author Andrea Furini
 * @version $Id: $Id
 */
@Controller
public class RankingsController {

    private final UserService userService;
    private final TournamentService tournamentService;

    public RankingsController(UserService userService, TournamentService tournamentService) {
        this.userService = userService;
        this.tournamentService = tournamentService;
    }

    /**
     * <p>rankings.</p>
     *
     * @param model a {@link org.springframework.ui.Model} object
     * @return a {@link java.lang.String} object
     */
    @GetMapping("/rankings")
    public String rankings(Model model, HttpServletRequest request) {

        HttpSession session = request.getSession(false);
        String userEmail = (String) session.getAttribute("userEmail");
        User currentUser = userService.findUsersByEmail(userEmail);
        model.addAttribute("currentUser", currentUser);

        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);

        model.addAttribute("tournaments", tournamentService.getConcludedTournaments());
        return "/rankings/rankings";
    }

}

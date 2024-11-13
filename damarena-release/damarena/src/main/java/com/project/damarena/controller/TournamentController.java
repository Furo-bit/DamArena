package com.project.damarena.controller;

import com.project.damarena.DatabaseUtils;
import com.project.damarena.model.Notification;
import com.project.damarena.model.Tournament;
import com.project.damarena.model.User;
import com.project.damarena.repository.UserRepository;
import com.project.damarena.service.FriendService;
import com.project.damarena.service.TournamentService;
import com.project.damarena.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
public class TournamentController {

    private final TournamentService tournamentService;
    private final FriendService friendService;
    private final UserService userService;
    @Autowired
    private UserRepository userRepository;

    public TournamentController(TournamentService tournamentService, UserService userService, FriendService friendService) {
        this.tournamentService = tournamentService;
        this.friendService = friendService;
        this.userService = userService;
    }

    @GetMapping("/tournaments")
    public String showTournaments(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);
        String currentUserEmail = (String) session.getAttribute("userEmail");
        model.addAttribute("isLoggedin", session.getAttribute("isLoggedIn"));

        List<Tournament> tournaments = tournamentService.getAllTournamentsOrderedByStartingTime();
        List<Tournament> ended_tournaments = tournamentService.getConcludedTournaments();

        tournaments.removeAll(ended_tournaments);

        model.addAttribute("tournaments", tournaments);
        model.addAttribute("ended_tournaments", ended_tournaments);

        if (currentUserEmail != null) {
            currentUserEmail = currentUserEmail.replace(".", "[dot]");
        }

        ArrayList<String> joinedTournaments = new ArrayList<>();
        for (Tournament tournament : tournaments) {
            if (tournament.checkJoined(currentUserEmail)) {
                joinedTournaments.add(tournament.getId().toString());
            }
        }

        model.addAttribute("joinedTournaments", joinedTournaments);
        return "tournaments/tournaments";
    }

    @GetMapping("/tournaments/new-tournament")
    public String showNewForm(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        String userEmail = (String) session.getAttribute("userEmail");

        List<User> friends = friendService.getFriendsOfUser(userEmail);

        model.addAttribute("friends", friends);
        return "tournaments/form_tournament";
    }

    @PostMapping("/new-tournament")
    public String saveTournament(@RequestParam("name") String name,
                                 @RequestParam("format") String format,
                                 @RequestParam("maxNumPlayers") int maxNumPlayers,
                                 @RequestParam(value = "isPrivate", required = false) Boolean isPrivate,
                                 @RequestParam("startingTime") LocalDateTime startingTime,
                                 @RequestParam(value = "selectedFriends", required = false) List<String> selectedFriends,
                                 HttpServletRequest request,
                                 Model model) {
        Tournament tournament = new Tournament();
        tournament.setName(name);
        tournament.setFormat(format);
        tournament.setStartingTime(startingTime);
        tournament.setEndingTime(startingTime.plusDays(1));
        tournament.setMaxNumPlayers(maxNumPlayers);
        tournament.setIsPrivate(isPrivate != null ? isPrivate : false);

        HttpSession session = request.getSession(false);
        String userEmail = (String) session.getAttribute("userEmail");
        User user = userRepository.findByEmail(userEmail);
        tournament.setOwner(user);
        tournament.addPlayer(user);
        tournament.setWinner(null);

        LocalDateTime currentTime = LocalDateTime.now().plusHours(1);
        if (startingTime.isAfter(currentTime.plusHours(1)) && maxNumPlayers > 1) {
            tournamentService.addTournament(tournament);
        }

        if (selectedFriends != null && !selectedFriends.isEmpty()) {
            for (String friendEmail : selectedFriends) {
                Notification inviteNotification = new Notification();
                inviteNotification.setId(UUID.randomUUID().toString());
                inviteNotification.setMessage("play in tournament " + name);
                inviteNotification.setTimestamp(LocalDateTime.now());
                inviteNotification.setType("tournament");
                inviteNotification.setTournamentId(tournament.getId().toString());

                User friend = userService.findUsersByEmail(friendEmail);
                userService.addNotification(friend, inviteNotification);
            }
        }

        model.addAttribute("tournaments", tournamentService.getAllTournamentsOrderedByStartingTime());
        return "redirect:/tournaments";
    }

    @GetMapping("/tournaments/tournament/{id}")
    public String showTournament(@PathVariable("id") String id, Model model, HttpServletRequest request) {
        Tournament tournament = tournamentService.get(id);
        HttpSession session = request.getSession(false);
        String currentUserEmail = (String) session.getAttribute("userEmail");
        model.addAttribute("tournament", tournament);

        if (currentUserEmail != null) {
            currentUserEmail = currentUserEmail.replace(".", "[dot]");
        }

        model.addAttribute("isInProgress", tournament.getIsInProgress());
        model.addAttribute("joined", tournament.checkJoined(currentUserEmail));

        model.addAttribute("players", tournament.getPlayers().values());

        return "tournaments/tournament";
    }

    @PostMapping("/tournaments/join")
    public String joinTournament(@RequestParam String tournamentId, @RequestParam String playerId, RedirectAttributes redirectAttributes) {
        System.out.println("Trying to add " + playerId + " to tournament: " + tournamentId);
        try {
            boolean joined = tournamentService.checkAndJoinTournament(tournamentId, playerId);
            if (joined) {
                redirectAttributes.addFlashAttribute("message", "Successfully joined the tournament");
            } else {
                redirectAttributes.addFlashAttribute("message", "Failed to join the tournament");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Error joining tournament: " + e.getMessage());
        }
        return "redirect:/tournaments";
    }

    @PostMapping("/tournaments/leave")
    public String leaveTournament(@RequestParam String tournamentId, @RequestParam String playerId, RedirectAttributes redirectAttributes) {
        System.out.println("Trying to remove " + playerId + " from tournament: " + tournamentId);
        try {
            boolean left = tournamentService.checkAndLeaveTournament(tournamentId, playerId);
            if (left) {
                redirectAttributes.addFlashAttribute("message", "Successfully left the tournament");
            } else {
                redirectAttributes.addFlashAttribute("message", "Failed to leave the tournament");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Error leaving tournament: " + e.getMessage());
        }
        return "redirect:/tournaments";
    }

    @GetMapping("/check-concluded-tournaments")
    public String checkConcludedTournaments(Model model) {
        tournamentService.checkAndUpdateConcludedTournaments();
        model.addAttribute("tournaments", tournamentService.getAllTournamentsOrderedByStartingTime());
        return "redirect:/tournaments";
    }

    @PostMapping("/tournaments/removePlayer")
    public String removePlayer(@RequestParam String tournamentId, @RequestParam String playerId, RedirectAttributes redirectAttributes) {
        return leaveTournament(tournamentId, playerId, redirectAttributes);
    }
}
package com.project.damarena.service;

import com.project.damarena.model.Notification;
import com.project.damarena.model.Tournament;
import com.project.damarena.model.User;
import com.project.damarena.repository.TournamentRepository;
import com.project.damarena.repository.UserRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class TournamentService {

    private final TournamentRepository tournamentRepository;

    private final UserRepository userRepository;

    public TournamentService(TournamentRepository tournamentRepository, UserRepository userRepository) {
        this.tournamentRepository = tournamentRepository;
        this.userRepository = userRepository;
    }

    public Tournament get(String id) {
        Optional<Tournament> result = tournamentRepository.findById(id);
        return result.orElse(null);
    }

    public Object getAllTournaments() {
        return (List<Tournament>) tournamentRepository.findAll();
    }

    public List<Tournament> getAllTournamentsOrderedByStartingTime() {
        return tournamentRepository.findAllByOrderByStartingTimeAsc();
    }

    public void addTournament(Tournament tournament) {
        tournamentRepository.save(tournament);
    }

    public boolean checkAndJoinTournament(String tournamentId, String userEmail) {
        Tournament tournament = tournamentRepository.findById(tournamentId).orElseThrow(() -> new IllegalArgumentException("Invalid tournament ID"));

        User user = userRepository.findByEmail(userEmail);

        if (tournament.getPlayers().containsKey(user.getEmail())) {
            System.out.println("Tournament already joined");
            return false;
        }

        System.out.println("Num players before add: " + tournament.getPlayers().size());
        user.setEmail(user.getEmail().replace(".", "[dot]"));

        if (tournament.addPlayer(user)) {
            System.out.println("User " + userEmail + " successfully added to tournament");
            System.out.println("Num players after add: " + tournament.getPlayers().size());
            tournamentRepository.save(tournament);
            tournament = tournamentRepository.findById(tournamentId).orElseThrow(() -> new IllegalArgumentException("Invalid tournament ID"));
            System.out.println("Num players after save: " + tournament.getPlayers().size());
            return true;
        }

        return false;
    }

    public boolean checkAndLeaveTournament(String tournamentId, String userEmail) {
        Tournament tournament = tournamentRepository.findById(tournamentId).orElseThrow(() -> new RuntimeException("Tournament not found"));
        User player = userRepository.findById(userEmail).orElseThrow(() -> new RuntimeException("User not found"));
        player.setEmail(player.getEmail().replace(".", "[dot]"));

        if (tournament.checkJoined(player.getEmail())) {
            tournament.getPlayers().remove(player.getEmail(), player);
            tournamentRepository.save(tournament);
            return true;
        } else {
            return false;
        }
    }


    public List<Tournament> getConcludedTournaments() {
        return tournamentRepository.findByEndingTimeBefore(LocalDateTime.now());
    }

    @Scheduled(fixedRate = 10000)
    public void checkAndUpdateConcludedTournamentsScheduled() {
        checkStartedTournaments();
        checkAndUpdateConcludedTournaments();
    }

    public void checkAndUpdateConcludedTournaments() {
        List<Tournament> concludedTournaments = tournamentRepository.findByEndingTimeBefore(LocalDateTime.now());
        for (Tournament tournament : concludedTournaments) {
            if (tournament.getWinner() == null) {
                tournament.determineWinner();
                tournamentRepository.save(tournament);
            }
        }
    }

    public void checkStartedTournaments() {
        List<Tournament> concludedTournaments = tournamentRepository.findByEndingTimeBefore(LocalDateTime.now());
        List<Tournament> startedTournaments = tournamentRepository.findByStartingTimeBefore(LocalDateTime.now());
        startedTournaments.removeAll(concludedTournaments);
        for (Tournament tournament : startedTournaments) {
            if (tournament.getOriginalPlayerCount() == 0) {
                int playerCount = tournament.getPlayers().size();
                tournament.setOriginalPlayerCount(playerCount);
                tournament.setNextNotificationPoint(getNextNotificationPoint(playerCount));
                tournamentRepository.save(tournament);
                pairPlayers(tournament);
            } else if (tournament.getPlayers().size() <= tournament.getNextNotificationPoint() && tournament.getPlayers().size() != 1) {
                pairPlayers(tournament);
                tournament.setNextNotificationPoint(getNextNotificationPoint(tournament.getPlayers().size()));
                tournamentRepository.save(tournament);
            } else if (tournament.getPlayers().size() == 1) {
                User winner = tournament.getPlayers().values().iterator().next();
                tournament.setWinner(winner);
                if (!tournament.getIsPrivate()) {
                    winner.updateEloScore(("checkers-" + tournament.getFormat()).toLowerCase(), 15);
                    userRepository.save(winner);
                }
                tournament.setEndingTime(LocalDateTime.now());
                tournamentRepository.save(tournament);
            }
        }
    }

    private int getNextNotificationPoint(int currentPlayerCount) {
        if (currentPlayerCount % 2 == 0) {
            return currentPlayerCount / 2;
        } else {
            return currentPlayerCount / 2 + 1;
        }
    }

    public void pairPlayers(Tournament tournament) {

        List<User> allPlayers = new ArrayList<>(tournament.getPlayers().values());

        Collections.shuffle(allPlayers);

        if (allPlayers.size() % 2 != 0) {
            User lastPlayer = allPlayers.get(allPlayers.size() - 1);
            handleOddPlayer(lastPlayer);
            allPlayers.remove(lastPlayer);
        }

        for (int i = 0; i < allPlayers.size() - 1; i += 2) {
            User player1 = allPlayers.get(i);
            User player2 = allPlayers.get(i + 1);

            player1.setEmail(player1.getEmail().replace("[dot]", "."));
            player2.setEmail(player2.getEmail().replace("[dot]", "."));

            pairPlayersTogether(player1, player2, tournament);
        }
    }

    public void pairPlayersTogether(User player1, User player2, Tournament tournament) {

        Notification p1 = new Notification();
        p1.setId(UUID.randomUUID().toString());
        p1.setMessage("Play with " + player2.getUsername());
        p1.setTournamentId(tournament.getId().toString());
        p1.setType("tournamentGame");

        List<Notification> p1notif = player1.getNotifications();
        p1notif.add(p1);
        userRepository.save(player1);

        Notification p2 = new Notification();
        p2.setId(UUID.randomUUID().toString());
        p2.setMessage("Play with " + player1.getUsername());
        p2.setTournamentId(tournament.getId().toString());
        p2.setType("tournamentGame");

        List<Notification> p2notif = player2.getNotifications();
        p2notif.add(p2);
        userRepository.save(player2);
    }

    private void handleOddPlayer(User lastPlayer) {
        System.out.println("odd players, player " + lastPlayer.getUsername()
                + " added to next round");
    }

}

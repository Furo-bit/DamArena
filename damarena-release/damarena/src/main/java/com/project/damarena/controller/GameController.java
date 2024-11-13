package com.project.damarena.controller;

import com.project.damarena.model.*;
import com.project.damarena.repository.CheckersGameRepository;
import com.project.damarena.repository.TournamentRepository;
import com.project.damarena.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.sql.rowset.serial.SQLOutputImpl;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@Controller
public class GameController {

    private final CheckersGameRepository checkersGameRepository;
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    @Autowired
    private UserService userService;
    private final TournamentRepository tournamentRepository;

    public GameController(CheckersGameRepository checkersGameRepository,
                          TournamentRepository tournamentRepository) {
        this.checkersGameRepository = checkersGameRepository;
        this.tournamentRepository = tournamentRepository;
    }

    @MessageMapping("/move")
    @SendTo("/topic/game")
    public Move makeMove(Move move) {
        Optional<CheckersGame> games = checkersGameRepository.findById(move.getGameId());
        CheckersGame game = games.get();

        System.out.println("In game ==> " + game.getId());
        System.out.println("Selected move ==> " + move);

        System.out.println("Board before move:");
        game.getBoard().printBoard();


        if (game.makeMove(move)) {
            if(game.getBoard().hasPlayerWon(game.getPlayer1()) == 'w') {
                game.setWinner(game.getPlayer1().getUser().getEmail());
            }

            if(game.getBoard().hasPlayerWon(game.getPlayer2()) == 'b') {
                game.setWinner(game.getPlayer2().getUser().getEmail());
            }

            if(game.getBoard().hasPlayerWon(game.getPlayer1()) == 'e') {
                game.setWinner("none, continue playing!");
            }

            simpMessagingTemplate.convertAndSend("/topic/game", move);
            checkersGameRepository.save(game);
            return move;
        }
        else {
            return null;
        }
    }


    @GetMapping("/game/checkers-game")
    public String showGame(@RequestParam("gameId") String gameId,
                           @RequestParam("tournamentId") String tournamentId,
                           Model model, HttpServletRequest request) {

        HttpSession session = request.getSession(false);
        String userEmail = (String) session.getAttribute("userEmail");
        session.setAttribute("tournamentId", tournamentId);

        Tournament tournament = tournamentRepository.findTournamentById(tournamentId);
        String tournament_format = tournament.getFormat();
        int minutes, seconds = 0;
        if (tournament_format.equals("bullet")) {
            minutes = 1;
        } else if (tournament_format.equals("blitz")){
            minutes = 3;
        } else {
            minutes = 10;
        }

        model.addAttribute("minutes", minutes);
        model.addAttribute("seconds", seconds);

        CheckersGame checkersGame = checkersGameRepository.findById(gameId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid game ID: " + gameId));

        Player player1 = checkersGame.getPlayer1();
        Player player2 = checkersGame.getPlayer2();

        if (player1.getUser().getEmail().equals(userEmail)) {
            model.addAttribute("player", player1.getUser().getUsername());
            model.addAttribute("opponent", player2.getUser().getUsername());
        } else if (player2.getUser().getEmail().equals(userEmail)) {
            model.addAttribute("player", player2.getUser().getUsername());
            model.addAttribute("opponent", player1.getUser().getUsername());
        }

        model.addAttribute("tournamentId", tournamentId);
        model.addAttribute("game",checkersGame);

        return "game/checkers-game";
    }

    @PostMapping("/multiplayerPossibleMoves")
    @ResponseBody
    public ArrayList<Move> getPossibleMoves(@RequestBody Move move) {
        ArrayList<Move> possibleMoves = new ArrayList<>();
        Optional<CheckersGame> games = checkersGameRepository.findById(move.getGameId());
        CheckersGame game = games.get();

        int startX = move.getStartX();
        int startY = move.getStartY();

        System.out.println("Checking possible moves for: (" + startX + ", " + startY + ")" );
        Piece[][] pieces = game.getCurrentPlayer().getBoard().getPieces();
        Piece selectedPiece = pieces[startX][startY];

        int[][] directions;
        if (selectedPiece.isKing()) {
            directions = new int[][]{{1, 1}, {-1, 1}, {1, -1}, {-1, -1}};
        } else{
            directions = new int[][]{{-1, 1}, {-1, -1}};
        }

        for (int[] direction : directions) {
            int newX = startX + direction[0];
            int newY = startY + direction[1];
            Move currentMove = new Move(startX, startY, newX, newY, game.getId());
            if (game.isValidMove(currentMove)) {
                possibleMoves.add(currentMove);
            }

            System.out.println("Possible simple moves: " + possibleMoves.size());
            for (Move possibleMove : possibleMoves) {
                System.out.println(possibleMove);
            }

            // Check for capturing moves
            int captureX = startX + 2 * direction[0];
            int captureY = startY + 2 * direction[1];
            Move possibleCapture = new Move(startX, startY, captureX, captureY, game.getId());
            System.out.println("captureX: " + captureX + " captureY: " + captureY);
            if (game.isValidMove(possibleCapture)) {
                possibleMoves.add(possibleCapture);
            }

            System.out.println("Possible simple moves + capturing moves: " + possibleMoves.size());
            for (Move possibleMove : possibleMoves) {
                System.out.println(possibleMove);
            }
            System.out.println("==================================");

        }

        return possibleMoves;
    }

    @PostMapping("/surrender")
    public String surrender(@RequestParam("gameId") String gameId,
                            HttpServletRequest request) {

        HttpSession session = request.getSession(false);
        String playerEmail = (String) session.getAttribute("userEmail");
        String tournamentId = (String) session.getAttribute("tournamentId");

        Optional<CheckersGame> gameOptional = checkersGameRepository.findById(gameId);
        if (!gameOptional.isPresent()) {
            System.out.println("game doesn't exist");
            return "error";
        }

        CheckersGame game = gameOptional.get();

        Player surrenderingPlayer;
        Player opponentPlayer;
        if (game.getPlayer1().getUser().getEmail().equals(playerEmail)) {
            surrenderingPlayer = game.getPlayer1();
            opponentPlayer = game.getPlayer2();
        } else if (game.getPlayer2().getUser().getEmail().equals(playerEmail)) {
            surrenderingPlayer = game.getPlayer2();
            opponentPlayer = game.getPlayer1();
        } else {
            return "error";
        }

        System.out.println("Surrendering: " + surrenderingPlayer.getUser());
        System.out.println("Winner: " + opponentPlayer.getUser());

        Optional<Tournament> tournamentOptional = tournamentRepository.findById(tournamentId);
        if (tournamentOptional.isPresent()) {
            Tournament tournament = tournamentOptional.get();
            Map<String, User> players = tournament.getPlayers();

            System.out.println("Surrendering: " + surrenderingPlayer.getUser().getEmail());
            System.out.println("Dim of player: " + players.size());
            players.remove(surrenderingPlayer.getUser().getEmail().replace(".", "[dot]"));
            System.out.println("Dim of player: " + players.size());

            tournamentRepository.save(tournament);
        }

        game.surrender(surrenderingPlayer);
        checkersGameRepository.save(game);

        return "redirect:/";
    }

    @PostMapping("/win")
    public String win(@RequestParam("gameId") String gameId,
                    HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        String tournamentId = (String) session.getAttribute("tournamentId");

        Optional<CheckersGame> gameOptional = checkersGameRepository.findById(gameId);
        if (!gameOptional.isPresent()) {
            System.out.println("game doesn't exist");
        }

        CheckersGame game = gameOptional.get();
        String winnerEmail = game.getWinner();

        Player losingPlayer;
        Player winningPlayer;
        if (game.getPlayer1().getUser().getEmail().equals(winnerEmail)) {
            winningPlayer = game.getPlayer1();
            losingPlayer = game.getPlayer2();
        } else {
            winningPlayer = game.getPlayer2();
            losingPlayer = game.getPlayer1();
        }

        Optional<Tournament> tournamentOptional = tournamentRepository.findById(tournamentId);
        if (tournamentOptional.isPresent()) {
            Tournament tournament = tournamentOptional.get();
            Map<String, User> players = tournament.getPlayers();
            System.out.println("Loser: " + losingPlayer.getUser().getEmail());
            System.out.println("Removed player: " + players.remove(losingPlayer.getUser().getEmail()));
            players.remove(losingPlayer.getUser().getEmail());
            tournamentRepository.save(tournament);
        }

        checkersGameRepository.save(game);
        return "redirect:/";
    }

}


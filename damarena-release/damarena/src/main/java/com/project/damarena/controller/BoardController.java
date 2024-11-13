package com.project.damarena.controller;

import com.project.damarena.model.*;
import com.project.damarena.repository.CheckersGameRepository;
import com.project.damarena.repository.UserRepository;
import com.project.damarena.service.FriendService;
import com.project.damarena.service.TournamentService;
import com.project.damarena.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

@Controller
public class BoardController {

    private Board board;
    private ArrayList<CheckersMove> possibleMoves;

    private final UserService userService;
    private final TournamentService tournamentService;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CheckersGameRepository checkersGameRepository;


    public BoardController(UserService userService, TournamentService tournamentService) {
        this.userService = userService;
        this.tournamentService = tournamentService;
    }

    @PostMapping("/single_player_game")
    public String startSinglePlayerGame(Model model,
                                        @RequestParam("difficulty") String gameDifficulty,
                                        @RequestParam("color") String gameColor) {
        System.out.println("Correct mapping");
        System.out.println("Difficulty ==> " + gameDifficulty);
        System.out.println("Color ==> " + gameColor);

        if (gameColor.equals("random")) {
            if (Math.random() % 2 == 0)
                gameColor = "white";
            else
                gameColor = "black";
        }

        if (gameColor.equals("white")) {
            if (board == null)
                board = new Board('w', 'W', 'b', 'B');
        }

        if (gameColor.equals("black")) {
            if (board == null)
                board = new Board('b', 'B', 'w', 'W');
        }

        return getBoard(model);
    }

    @PostMapping("/send-invite")
    public String sendInvite(@RequestParam("friendId") String friendId,
                             @RequestParam("minutes") int minutes,
                             @RequestParam("seconds") int seconds,
                             @RequestParam("color") String color,
                             HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        String senderEmail = (String) session.getAttribute("userEmail");

        Notification inviteNotification = new Notification();
        inviteNotification.setId(UUID.randomUUID().toString());
        inviteNotification.setMessage("Play with " + senderEmail);

        String opponent_color;

        if (color.equals("white"))
            opponent_color = "black";
        else
            opponent_color = "white";

        if (board == null) {
            if (color.equals("white")) {
                board = new Board('w', 'W', 'b', 'B');
            } else {
                board = new Board('b', 'B', 'w', 'W');
            }
        }

        inviteNotification.setType(opponent_color + ", " + minutes + ":" + seconds);
        inviteNotification.setTimestamp(LocalDateTime.now());

        User friend = userService.findUsersByEmail(friendId);
        userService.addNotification(friend, inviteNotification);

        return "forward:/multiplayer_game";
    }

    @PostMapping("/delete-notification")
    public String deleteNotification(@RequestParam("notificationId") String notificationId, HttpServletRequest request, Model model) {

        HttpSession session = request.getSession(false);
        String userEmail = (String) session.getAttribute("userEmail");

        User user = userService.findUsersByEmail(userEmail);
        Notification notification = user.getNotifications().stream()
                .filter(notif -> notif.getId().equals(notificationId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid notification ID"));

        userService.deleteNotification(userEmail, notificationId);

        if ("tournament".equals(notification.getType())) {
            boolean joined = tournamentService.checkAndJoinTournament(notification.getTournamentId(), userEmail);
            if (!joined) {
                System.out.println("Max level of players reached");
            }
            return "redirect:/";
        }
        if ("tournamentGame".equals(notification.getType())) {
            String tournamentId = notification.getTournamentId();
            String opponentUsername = notification.getMessage().split("Play with ")[1];
            User opponent = userRepository.findByUsername(opponentUsername);

            CheckersGame existingGame = checkersGameRepository.findExistingGame(user.getEmail(), opponent.getEmail());

            if (existingGame == null) {
                Player player1 = new Player(user, 'w', null);
                Player player2 = new Player(opponent, 'b', null);

                CheckersGame checkersGame = new CheckersGame(player1, player2);

                player1.setBoard(checkersGame.getBoard());

                MultiPlayerBoard player2Board = checkersGame.getBoard().getFlippedBoard();
                player2.setBoard(player2Board);
                checkersGameRepository.save(checkersGame);
                model.addAttribute("game",checkersGame);
                model.addAttribute("tournamentId", notification.getTournamentId());

                return "redirect:/game/checkers-game?gameId=" + checkersGame.getId() + "&tournamentId=" + tournamentId;
            } else {

                //existingGame.getBoard().setupInitialPosition();
                //String loggedUserEmail = (String) session.getAttribute("userEmail");

//                Player currentLoggedPlayer = existingGame.getPlayer1().getUser().getEmail().equals(loggedUserEmail) ? existingGame.getPlayer1() : existingGame.getPlayer2();
//                if(existingGame.getCurrentPlayer().getColor() != existingGame.getBoardOrientation()) {
//                    existingGame.flipBoard();
//                }

                checkersGameRepository.save(existingGame);
                model.addAttribute("game", existingGame);
                return "redirect:/game/checkers-game?gameId=" + existingGame.getId() + "&tournamentId=" + tournamentId;
            }
        }
        else {

            String[] parts = notification.getMessage().split(" ");
            String senderEmail = parts[2];

            parts = notification.getType().split(", ");
            String color = parts[0];
            String[] timeParts = parts[1].split(":");
            int minutes = Integer.parseInt(timeParts[0]);
            int seconds = Integer.parseInt(timeParts[1]);

            return startMultiplayerGame(model, request, senderEmail, color, minutes, seconds);
        }
    }

    @PostMapping("/multiplayer_game")
    public String startMultiplayerGame(Model model,
                                       HttpServletRequest request,
                                       @RequestParam("friendId") String friendId,
                                       @RequestParam("color") String color,
                                       @RequestParam("minutes") int minutes,
                                       @RequestParam("seconds") int seconds) {

        HttpSession session = request.getSession(false);
        String senderEmail = (String) session.getAttribute("userEmail");

        User player = userService.findUsersByEmail(senderEmail);
        User opponent = userService.findUsersByEmail(friendId);

        if (color.equals("black")) {
            model.addAttribute("computerMove", true);
        } else {
            model.addAttribute("computerMove", false);
        }

        model.addAttribute("player", player.getUsername());
        model.addAttribute("opponent", opponent.getUsername());

        model.addAttribute("minutes", minutes);
        model.addAttribute("seconds", seconds);
        model.addAttribute("board", board.getBoard());

        model.addAttribute("changeUrl", true);

        return "game/multiplayer_board";
    }

    @GetMapping("/game")
    public String getBoard(Model model) {
        model.addAttribute("board", board.getBoard());
        return "game/board";
    }

    @PostMapping("/possibleMoves")
    @ResponseBody
    public ArrayList<CheckersMove> getPossibleMoves(@RequestBody CheckersPawn pawn) {
        pawn = board.getPawnAtPosition(pawn.getRow(), pawn.getCol());
        possibleMoves = new ArrayList<>();

        System.out.println("Pawn inspected :\n" + pawn.toString());

        if (!board.isCellFree(pawn.getRow() - 1, pawn.getCol() - 1)) {
            CheckersPawn takeablePawn = board.getPawnAtPosition(pawn.getRow() - 1, pawn.getCol() - 1);
            if (takeablePawn != null && takeablePawn.getColor().equals("OPPONENT_COLOR")) {
                if (board.isCellFree(takeablePawn.getRow() - 1, takeablePawn.getCol() - 1))
                    possibleMoves.add(new CheckersMove(pawn.getRow(), pawn.getCol(), takeablePawn.getRow() - 1, takeablePawn.getCol() - 1));
            }
        }

        if (!board.isCellFree(pawn.getRow() - 1, pawn.getCol() + 1)) {
            CheckersPawn takeablePawn = board.getPawnAtPosition(pawn.getRow() - 1, pawn.getCol() + 1);
            if (takeablePawn != null && takeablePawn.getColor().equals("OPPONENT_COLOR")) {
                if (board.isCellFree(takeablePawn.getRow() - 1, takeablePawn.getCol() + 1))
                    possibleMoves.add(new CheckersMove(pawn.getRow(), pawn.getCol(), takeablePawn.getRow() - 1, takeablePawn.getCol() + 1));
            }
        }

        if (pawn.isKing()) {

            if (!board.isCellFree(pawn.getRow() + 1, pawn.getCol() + 1)) {
                CheckersPawn takeablePawn = board.getPawnAtPosition(pawn.getRow() + 1, pawn.getCol() + 1);
                if (takeablePawn != null && takeablePawn.getColor().equals("OPPONENT_COLOR")) {
                    if (board.isCellFree(takeablePawn.getRow() + 1, takeablePawn.getCol() + 1))
                        possibleMoves.add(new CheckersMove(pawn.getRow(), pawn.getCol(), takeablePawn.getRow() + 1, takeablePawn.getCol() + 1));
                }
            }

            if (!board.isCellFree(pawn.getRow() + 1, pawn.getCol() - 1)) {
                CheckersPawn takeablePawn = board.getPawnAtPosition(pawn.getRow() + 1, pawn.getCol() - 1);
                if (takeablePawn != null && takeablePawn.getColor().equals("OPPONENT_COLOR")) {
                    if (board.isCellFree(takeablePawn.getRow() + 1, takeablePawn.getCol() - 1))
                        possibleMoves.add(new CheckersMove(pawn.getRow(), pawn.getCol(), takeablePawn.getRow() + 1, takeablePawn.getCol() - 1));
                }
            }

        }

        if (!possibleMoves.isEmpty())
            return possibleMoves;

        if (board.isCellFree(pawn.getRow() - 1, pawn.getCol() - 1))
            possibleMoves.add(new CheckersMove(pawn.getRow(), pawn.getCol(), pawn.getRow() - 1, pawn.getCol() - 1));

        if (board.isCellFree(pawn.getRow() - 1, pawn.getCol() + 1))
            possibleMoves.add(new CheckersMove(pawn.getRow(), pawn.getCol(), pawn.getRow() - 1, pawn.getCol() + 1));

        if (pawn.isKing()) {
            if (board.isCellFree(pawn.getRow() + 1, pawn.getCol() + 1))
                possibleMoves.add(new CheckersMove(pawn.getRow(), pawn.getCol(), pawn.getRow() + 1, pawn.getCol() + 1));

            if (board.isCellFree(pawn.getRow() + 1, pawn.getCol() - 1))
                possibleMoves.add(new CheckersMove(pawn.getRow(), pawn.getCol(), pawn.getRow() + 1, pawn.getCol() - 1));
        }


        System.out.println("Possible moves for this pawn: ");
        for (CheckersMove move : possibleMoves) {
            System.out.println(move.toString());
        }

        return possibleMoves;
    }

    @PostMapping("/playerMove")
    @ResponseBody
    public ResponseEntity<String> movePiece(@RequestBody CheckersMove move) {
        System.out.println("Updating the board");
        System.out.println("Move = \n" + move.toString());

        ArrayList<CheckersMove> currentPossibleMoves = getPossibleMoves(board.getPawnAtPosition(move.getStartRow(), move.getStartCol()));
        boolean validMove = currentPossibleMoves.contains(move);

        System.out.println("Selected move:\n" + move);
        System.out.println("Current possible moves:\n");
        for (CheckersMove testMove : currentPossibleMoves) {
            System.out.println(testMove.toString());
        }

        if (validMove) {
            board.updateBoard(move);
            return ResponseEntity.ok().body("Move successful!");
        } else return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid move!");
    }

    @GetMapping("/computerMove")
    public ResponseEntity<String> makeComputerMove() {
        CheckersMove computerMove = board.makeRandomMove();
        //per testare logica fine gioco
        //computerMove = null;
        //

        System.out.println(computerMove);
        if (computerMove == null) {
            System.out.println("No moves for me, you win");
            return ResponseEntity.ok().body("Player wins! Congrats");
        }

        board.updateBoard(computerMove);
        boolean computerWins = true;

        for (CheckersPawn pawn : board.getPawnsPositions().values()) {
            if (pawn.getColor().equals("PLAYER_COLOR"))
                computerWins = false;
        }

        if (computerWins)
            return ResponseEntity.ok().body("Computer wins! Game over");

        return ResponseEntity.ok().body("Your move, human.");
    }

    @PostMapping("/restartGame")
    public ResponseEntity<String> restartGame() {
        board.restartBoard();
        return ResponseEntity.ok().body("Game restarted successfully!");
    }

    @PostMapping("/resetGame")
    public ResponseEntity<String> resetGame() {
        board = null;
        return ResponseEntity.ok().body("Game reset successfully!");
    }


}

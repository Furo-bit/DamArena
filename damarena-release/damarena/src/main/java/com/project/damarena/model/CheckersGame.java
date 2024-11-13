package com.project.damarena.model;

import com.project.damarena.repository.CheckersGameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Component
@Document(collection = "Checkers_games")
public class CheckersGame {
    @Id
    private String id;
    private Player player1;
    private Player player2;
    private MultiPlayerBoard board;
    private Player currentPlayer;
    private char boardOrientation;
    private String winner;

    public CheckersGame() {
    }

    public CheckersGame(Player player1) {
        this.player1 = player1;
        this.board = new MultiPlayerBoard();
        this.currentPlayer = player1;
        boardOrientation = 'w';
        initializeGame();
    }

    public CheckersGame(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
        this.board = new MultiPlayerBoard();
        this.currentPlayer = player1;
        boardOrientation = 'w';
        initializeGame();
    }

    public void initializeGame() {
        board.setupInitialPosition();
    }

    public boolean makeMove(Move move) {
        if (isValidMove(move)) {
            if(currentPlayer.getColor() == player1.getColor()) {
                player1.getBoard().updateBoard(move);
                player2.setBoard(player1.getBoard().getFlippedBoard());
            }

            if(currentPlayer.getColor() == player2.getColor()) {
                player2.getBoard().updateBoard(move);
                player1.setBoard(player2.getBoard().getFlippedBoard());
            }

            switchPlayer();
            return true;
        }
        return false;
    }

    public boolean isValidMove(Move move) {
        if (currentPlayer.getColor() == player1.getColor()) {
            return player1.getBoard().isValidMove(move, player1);
        }

        if(currentPlayer.getColor() == player2.getColor()) {
            return player2.getBoard().isValidMove(move, player2);
        }

        return false;

    }

    private void switchPlayer() {
        currentPlayer = (currentPlayer.equals(player1)) ? player2 : player1;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Player getPlayer1() {
        return player1;
    }

    public void setPlayer1(Player player1) {
        this.player1 = player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public void setPlayer2(Player player2) {
        this.player2 = player2;
    }

    public MultiPlayerBoard getBoard() {
        return board;
    }

    public void setBoard(MultiPlayerBoard board) {
        this.board = board;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public char getBoardOrientation() {
        return boardOrientation;
    }

    public void setBoardOrientation(char boardOrientation) {
        this.boardOrientation = boardOrientation;
    }

    public void flipBoard() {
        board.flip();

        if(boardOrientation == 'w') {
            boardOrientation = 'b';
        }

        if(boardOrientation == 'b') {
            boardOrientation = 'w';
        }
    }

    public void surrender(Player player) {
        if (player.equals(player1)) {
            this.winner = player2.getUser().getEmail();
        } else if (player.equals(player2)) {
            this.winner = player1.getUser().getEmail();
        }
    }
}

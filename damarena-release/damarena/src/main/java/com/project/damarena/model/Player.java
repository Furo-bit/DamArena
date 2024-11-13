package com.project.damarena.model;

import java.util.Objects;

public class Player {

    private User user;
    private char color;
    private MultiPlayerBoard board;

    public Player(User user, char color, MultiPlayerBoard board) {
        this.user = user;
        this.color = color;
        this.board = board;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public char getColor() {
        return color;
    }

    public void setColor(char color) {
        this.color = color;
    }

    public MultiPlayerBoard getBoard() {
        return board;
    }

    public void setBoard(MultiPlayerBoard board) {
        this.board = board;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return color == player.color && Objects.equals(user, player.user);
    }

}

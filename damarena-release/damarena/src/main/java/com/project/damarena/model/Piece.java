package com.project.damarena.model;

public class Piece {
    private char playerColor;
    private boolean isKing;

    public Piece(char playerColor) {
        this.playerColor = playerColor;
        this.isKing = false;
    }

    public char getPlayerColor() {
        return playerColor;
    }

    public boolean isKing() {
        return isKing;
    }

    public void makeKing() {
        this.isKing = true;
    }

    @Override
    public String toString() {
        return "Piece{" +
                "playerColor=" + playerColor +
                ", isKing=" + isKing +
                '}';
    }
}


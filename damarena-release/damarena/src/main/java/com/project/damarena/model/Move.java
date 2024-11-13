package com.project.damarena.model;

public class Move {
    private String gameId;
    private int startX;
    private int startY;
    private int endX;
    private int endY;

    public Move(int startX, int startY, int endX, int endY, String gameId) {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.gameId = gameId;
    }

    public Move() {
    }

    public void setStartX(int startX) {
        this.startX = startX;
    }

    public void setStartY(int startY) {
        this.startY = startY;
    }

    public void setEndX(int endX) {
        this.endX = endX;
    }

    public void setEndY(int endY) {
        this.endY = endY;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getGameId() {
        return gameId;
    }

    public int getStartX() {
        return startX;
    }

    public int getStartY() {
        return startY;
    }

    public int getEndX() {
        return endX;
    }

    public int getEndY() {
        return endY;
    }

    @Override
    public String toString() {
        return "Move{" +
                "startX= " + startX +
                ", startY= " + startY +
                ", endX= " + endX +
                ", endY= " + endY +
                ", gameId= " + gameId +
                '}';
    }
}


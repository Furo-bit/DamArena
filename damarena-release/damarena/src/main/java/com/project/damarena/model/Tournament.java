package com.project.damarena.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

@Document(collection = "Tournaments_collection")
public class Tournament {

    @Id
    private String id = new ObjectId().toString();
    private String name;
    private Boolean isPrivate;
    private String format;
    private LocalDateTime startingTime;
    private LocalDateTime endingTime;
    private User owner;
    private HashMap<String, User> players = new HashMap<>();
    private User winner;
    private int maxNumPlayers;
    private int originalPlayerCount;
    private int nextNotificationPoint;

    public Object getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getIsPrivate() {
        return isPrivate;
    }

    public void setIsPrivate(Boolean isPrivate) {
        this.isPrivate = isPrivate;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public LocalDateTime getStartingTime() {
        return startingTime;
    }

    public void setStartingTime(LocalDateTime starting_time) {
        this.startingTime = starting_time;
    }

    public LocalDateTime getEndingTime() {
        return endingTime;
    }

    public void setEndingTime(LocalDateTime endingTime) {
        this.endingTime = endingTime;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public HashMap<String, User> getPlayers() {
        return players;
    }

    public void setPlayers(HashMap<String, User> players) {
        this.players = players;
    }

    public int getOriginalPlayerCount() {
        return originalPlayerCount;
    }

    public void setOriginalPlayerCount(int originalPlayerCount) {
        this.originalPlayerCount = originalPlayerCount;
    }

    public int getNextNotificationPoint() {
        return nextNotificationPoint;
    }

    public void setNextNotificationPoint(int nextNotificationPoint) {
        this.nextNotificationPoint = nextNotificationPoint;
    }

    public boolean addPlayer(User user) {
        if (this.players.size() != this.maxNumPlayers) {
            this.players.put(user.getEmail().replace(".", "[dot]"), user);
            System.out.println(players.get(user.getEmail()));
            return true;
        }
        return false;
    }

    public void removePlayer(User user) {
        this.players.remove(user);
    }

    public User getWinner() {
        return winner;
    }

    public void setWinner(User winner) {
        this.winner = winner;
    }

    public void determineWinner() {
        if (!players.isEmpty()) {
            ArrayList<User> playerList = new ArrayList<>(players.values());
            Random random = new Random();
            int winnerIndex = random.nextInt(playerList.size());
            this.winner = playerList.get(winnerIndex);
        }
    }

    public int getMaxNumPlayers() {
        return maxNumPlayers;
    }

    public void setMaxNumPlayers(int maxNumPlayers) {
        this.maxNumPlayers = maxNumPlayers;
    }

    public boolean getIsInProgress() {
        LocalDateTime now = LocalDateTime.now();
        return now.isAfter(this.getStartingTime()) && now.isBefore(this.getEndingTime());
    }

    public boolean getIsEnded() {
        LocalDateTime now = LocalDateTime.now();
        return now.isAfter(this.getEndingTime());
    }

    @Override
    public String toString() {
        return "Tournament{" + "id=" + id + ", private='" + isPrivate + '\'' + ", format='" + format + '\'' + ", starting_time=" + startingTime + ", owner=" + owner + ", players=" + players + '}';
    }

    public boolean checkJoined(String userEmail) {
        return players.containsKey(userEmail);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tournament that = (Tournament) o;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}

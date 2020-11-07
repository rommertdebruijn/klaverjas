package com.keemerz.klaverjas.domain;

public class Player {

    private String userId; // Spring security token
    private String playerId; // GameState identifier
    private String name; // human readable

    public Player(String userId, String playerId, String name) {
        this.userId = userId;
        this.playerId = playerId;
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public String getPlayerId() {
        return playerId;
    }

    public String getName() {
        return name;
    }
}

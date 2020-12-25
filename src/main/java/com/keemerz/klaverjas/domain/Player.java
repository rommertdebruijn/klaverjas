package com.keemerz.klaverjas.domain;

import java.time.LocalDateTime;

public class Player {

    static final int MAX_IDLE_TIME_IN_SECONDS = 24 * 60 * 60; // 1 day idle max

    private String userId; // Spring security token
    private String playerId; // Player identifier
    private String name; // human readable
    private LocalDateTime loginTimestamp; // will be null if idle for too long

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

    public void setLoginTimestamp(LocalDateTime loginTimestamp) {
        this.loginTimestamp = loginTimestamp;
    }

    public boolean isActive() {
        return loginTimestamp != null && loginTimestamp.isAfter(LocalDateTime.now().minusSeconds(MAX_IDLE_TIME_IN_SECONDS));
    }
}

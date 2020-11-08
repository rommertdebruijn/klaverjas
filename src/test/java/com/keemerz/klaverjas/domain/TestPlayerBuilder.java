package com.keemerz.klaverjas.domain;

import java.util.UUID;

public class TestPlayerBuilder {

    private String userId = UUID.randomUUID().toString();
    private String playerId = UUID.randomUUID().toString();
    private String playerName = "Bertje";

    public Player build() {
        return new Player(userId, playerId, playerName);
    }

    public TestPlayerBuilder withUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public TestPlayerBuilder withPlayerId(String playerId) {
        this.playerId = playerId;
        return this;
    }

    public TestPlayerBuilder withPlayerName(String playerName) {
        this.playerName = playerName;
        return this;
    }
}

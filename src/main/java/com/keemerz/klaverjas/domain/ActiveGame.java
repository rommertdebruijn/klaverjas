package com.keemerz.klaverjas.domain;

import java.util.List;

public class ActiveGame {

    private final String gameId;
    private final List<String> playerNames;

    public ActiveGame(String gameId, List<String> playerNames) {
        this.gameId = gameId;
        this.playerNames = playerNames;
    }

    public String getGameId() {
        return gameId;
    }

    public List<String> getPlayerNames() {
        return playerNames;
    }
}

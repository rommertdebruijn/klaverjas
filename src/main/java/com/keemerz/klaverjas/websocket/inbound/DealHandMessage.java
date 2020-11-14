package com.keemerz.klaverjas.websocket.inbound;

public class DealHandMessage {
    private String gameId;

    public DealHandMessage() {
    }

    public DealHandMessage(String gameId) {
        this.gameId = gameId;
    }

    public String getGameId() {
        return gameId;
    }

    public DealHandMessage setGameId(String gameId) {
        this.gameId = gameId;
        return this;
    }
}

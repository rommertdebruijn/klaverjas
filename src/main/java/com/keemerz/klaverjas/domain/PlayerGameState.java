package com.keemerz.klaverjas.domain;

import java.util.List;
import java.util.Map;

public class PlayerGameState {

    private String gameId;
    private boolean playerStillPlaying;

    private List<Card> hand;
    private Trick currentTrick;
    private Map<Seat, String> players;
    private Map<Seat, Integer> nrOfCardsInHand;
    private Seat turn;

    public static PlayerGameState playerLeftGameState(String gameId) {
        return new PlayerGameState(gameId, false);
    }

    private PlayerGameState(String gameId, boolean playerStillPlaying) {
        this.gameId = gameId;
        this.playerStillPlaying = playerStillPlaying;
    }

    public PlayerGameState(String gameId, boolean playerStillPlaying, List<Card> hand, Trick currentTrick, Map<Seat, String> players, Map<Seat, Integer> nrOfCardsInHand, Seat turn) {
        this.gameId = gameId;
        this.playerStillPlaying = playerStillPlaying;
        this.hand = hand;
        this.currentTrick = currentTrick;
        this.players = players;
        this.nrOfCardsInHand = nrOfCardsInHand;
        this.turn = turn;
    }

    public String getGameId() {
        return gameId;
    }

    public boolean isPlayerStillPlaying() {
        return playerStillPlaying;
    }

    public List<Card> getHand() {
        return hand;
    }

    public Trick getCurrentTrick() {
        return currentTrick;
    }

    public Map<Seat, String> getPlayers() {
        return players;
    }

    public Map<Seat, Integer> getNrOfCardsInHand() {
        return nrOfCardsInHand;
    }

    public Seat getTurn() {
        return turn;
    }
}

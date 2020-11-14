package com.keemerz.klaverjas.domain;

import java.util.List;
import java.util.Map;

public class PlayerGameState {

    private String gameId;
    private boolean playerStillPlaying;

    private Bidding bidding;
    private List<Card> hand;
    private Trick currentTrick;
    private Map<Seat, String> players;
    private Map<Seat, Integer> nrOfCardsInHand;
    private Seat turn;
    private Seat dealer;
    private ComboPoints comboPoints;

    public static PlayerGameState playerLeftGameState(String gameId) {
        return new PlayerGameState(gameId, false);
    }

    private PlayerGameState(String gameId, boolean playerStillPlaying) {
        this.gameId = gameId;
        this.playerStillPlaying = playerStillPlaying; // ok not too pretty
    }

    public PlayerGameState(String gameId, Bidding bidding, boolean playerStillPlaying, List<Card> hand, Trick currentTrick,
                           Map<Seat, String> players, Map<Seat, Integer> nrOfCardsInHand, Seat turn, Seat dealer, ComboPoints comboPoints) {
        this.gameId = gameId;
        this.playerStillPlaying = playerStillPlaying;
        this.bidding = bidding;
        this.hand = hand;
        this.currentTrick = currentTrick;
        this.players = players;
        this.nrOfCardsInHand = nrOfCardsInHand;
        this.turn = turn;
        this.dealer = dealer;
        this.comboPoints = comboPoints;
    }

    public String getGameId() {
        return gameId;
    }

    public boolean isPlayerStillPlaying() {
        return playerStillPlaying;
    }

    public Bidding getBidding() {
        return bidding;
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

    public Seat getDealer() {
        return dealer;
    }

    public ComboPoints getComboPoints() {
        return comboPoints;
    }
}

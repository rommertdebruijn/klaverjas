package com.keemerz.klaverjas.domain;

import java.util.List;
import java.util.Map;

public class PlayerGameState {

    private final String gameId;
    private String deckSignature;
    private final boolean playerStillPlaying;

    private Bidding bidding;
    private List<Card> hand;
    private Trick currentTrick;
    private Map<Seat, String> players;
    private Map<Seat, Integer> nrOfCardsInHand;
    private Map<Seat, Integer> nrOfTricks;
    private Seat turn;
    private Seat dealer;
    private ComboPoints comboPoints;
    private List<GameScore> gameScores;
    private MatchScore totalScore;
    private boolean dealerButtonAvailable;

    public static PlayerGameState playerLeftGameState(String gameId) {
        return new PlayerGameState(gameId, false);
    }

    private PlayerGameState(String gameId, boolean playerStillPlaying) {
        this.gameId = gameId;
        this.playerStillPlaying = playerStillPlaying; // ok not too pretty
    }

    public PlayerGameState(String gameId, String deckSignature, Bidding bidding, boolean playerStillPlaying, List<Card> hand, Trick currentTrick,
                           Map<Seat, String> players, Map<Seat, Integer> nrOfCardsInHand, Map<Seat, Integer> nrOfTricks, Seat turn, Seat dealer,
                           ComboPoints comboPoints, List<GameScore> gameScores, MatchScore totalScore,
                           boolean dealerButtonAvailable) {
        this.gameId = gameId;
        this.deckSignature = deckSignature;
        this.playerStillPlaying = playerStillPlaying;
        this.bidding = bidding;
        this.hand = hand;
        this.currentTrick = currentTrick;
        this.players = players;
        this.nrOfCardsInHand = nrOfCardsInHand;
        this.nrOfTricks = nrOfTricks;
        this.turn = turn;
        this.dealer = dealer;
        this.comboPoints = comboPoints;
        this.gameScores = gameScores;
        this.totalScore = totalScore;
        this.dealerButtonAvailable = dealerButtonAvailable;
    }

    public String getGameId() {
        return gameId;
    }

    public String getDeckSignature() {
        return deckSignature;
    }

    public void setDeckSignature(String deckSignature) {
        this.deckSignature = deckSignature;
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

    public Map<Seat, Integer> getNrOfTricks() {
        return nrOfTricks;
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

    public List<GameScore> getGameScores() {
        return gameScores;
    }

    public MatchScore getTotalScore() {
        return totalScore;
    }

    public boolean isDealerButtonAvailable() {
        return dealerButtonAvailable;
    }
}

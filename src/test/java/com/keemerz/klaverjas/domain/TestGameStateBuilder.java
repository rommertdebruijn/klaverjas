package com.keemerz.klaverjas.domain;

import java.util.*;

import static com.keemerz.klaverjas.domain.Seat.*;

public class TestGameStateBuilder {

    private String gameId = UUID.randomUUID().toString();
    private Map<Seat, List<Card>> hands = new HashMap<>();
    private Map<Seat, Player> players = new HashMap<>();
    private Seat dealer = WEST;
    private List<Trick> previousTricks = new ArrayList<>();
    private Suit trump = Suit.CLUBS;
    private Seat turn = NORTH;
    private Trick currentTrick = new Trick(trump, turn, new HashMap<>());

    public GameState build() {
        GameState gameState = new GameState(gameId);
        gameState.setPlayers(players);
        gameState.setHands(hands);
        gameState.setDealer(dealer);
        gameState.setCurrentTrick(currentTrick);
        gameState.setPreviousTricks(previousTricks);
        gameState.setTurn(turn);
        gameState.setTrump(trump);
        return gameState;
    }

    public TestGameStateBuilder withGameId(String gameId) {
        this.gameId = gameId;
        return this;
    }

    public TestGameStateBuilder withHands(Map<Seat, List<Card>> hands) {
        this.hands = hands;
        return this;
    }

    public TestGameStateBuilder withHand(Seat seat, List<Card> hand) {
        this.hands.put(seat, hand);
        return this;
    }

    public TestGameStateBuilder withPlayers(Map<Seat, Player> players) {
        this.players = players;
        return this;
    }

    public TestGameStateBuilder withPlayer(Seat seat, Player player) {
        this.players.put(seat, player);
        return this;
    }

    public TestGameStateBuilder withDealer(Seat dealer) {
        this.dealer = dealer;
        return this;
    }

    public TestGameStateBuilder withPreviousTricks(List<Trick> previousTricks) {
        this.previousTricks = previousTricks;
        return this;
    }

    public TestGameStateBuilder withTrump(Suit trump) {
        this.trump = trump;
        return this;
    }

    public TestGameStateBuilder withTurn(Seat turn) {
        this.turn = turn;
        return this;
    }

    public TestGameStateBuilder withCurrentTrick(Trick currentTrick) {
        this.currentTrick = currentTrick;
        return this;
    }
}

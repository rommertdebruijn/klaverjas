package com.keemerz.klaverjas.domain;

import java.util.HashMap;
import java.util.Map;

import static com.keemerz.klaverjas.domain.Seat.NORTH;
import static com.keemerz.klaverjas.domain.Suit.CLUBS;

public class TestTrickBuilder {

    private Suit trump = CLUBS;
    private Seat startingPlayer = NORTH;
    private Map<Seat, Card> cardsPlayed = new HashMap<>();
    private Seat trickWinner;

    public Trick build() {
        return new Trick(trump, startingPlayer, cardsPlayed, trickWinner, false);
    }

    public TestTrickBuilder withTrump(Suit trump) {
        this.trump = trump;
        return this;
    }

    public TestTrickBuilder withStartingPlayer(Seat startingPlayer) {
        this.startingPlayer = startingPlayer;
        return this;
    }

    public TestTrickBuilder withCardPlayed(Seat seat, Card card) {
        this.cardsPlayed.put(seat, card);
        return this;
    }

    public TestTrickBuilder withTrickWinner(Seat trickWinner) {
        this.trickWinner = trickWinner;
        return this;
    }
}

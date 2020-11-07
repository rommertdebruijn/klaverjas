package com.keemerz.klaverjas.domain;

import java.util.Map;

public class Trick {

    private Suit trump;
    private Seat startingPlayer;
    private Map<Seat, Card> cardsPlayed;

    public Trick(Suit trump, Seat startingPlayer, Map<Seat, Card> cardsPlayed) {
        this.trump = trump;
        this.startingPlayer = startingPlayer;
        this.cardsPlayed = cardsPlayed;
    }

    public Suit getTrump() {
        return trump;
    }

    public Seat getStartingPlayer() {
        return startingPlayer;
    }

    public Map<Seat, Card> getCardsPlayed() {
        return cardsPlayed;
    }
}

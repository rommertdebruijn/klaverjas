package com.keemerz.klaverjas.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.keemerz.klaverjas.domain.Seat.*;

public class Trick {

    private Suit trump;
    private Seat startingPlayer;
    private Map<Seat, Card> cardsPlayed = new HashMap<>();

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

    public Trick rotateForSeat(Seat currentPlayerSeat) {
        Map<Seat, Card> rotatedCardsPlayed = new HashMap<>();
        rotatedCardsPlayed.put(SOUTH, cardsPlayed.get(currentPlayerSeat));
        rotatedCardsPlayed.put(WEST, cardsPlayed.get(currentPlayerSeat.getLeftHandPlayer()));
        rotatedCardsPlayed.put(NORTH, cardsPlayed.get(currentPlayerSeat.getPartner()));
        rotatedCardsPlayed.put(EAST, cardsPlayed.get(currentPlayerSeat.getRightHandPlayer()));

        return new Trick(this.trump, null, rotatedCardsPlayed); // startingPlayer is only useful when calculating winner. No need to pass it to PlayerGameState
    }
}

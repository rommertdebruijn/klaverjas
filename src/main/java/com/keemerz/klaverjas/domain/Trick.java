package com.keemerz.klaverjas.domain;

import com.keemerz.klaverjas.comparator.HighestCardInTrickComparator;

import java.util.HashMap;
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

    public Card determineHighestCard() {
        if (cardsPlayed.isEmpty()) {
            throw new IllegalStateException("getHighestCard should not be called from places where no cards have been played");
        }

        return cardsPlayed.values().stream()
                .max(new HighestCardInTrickComparator(trump, cardsPlayed.get(startingPlayer).getSuit()))
                .orElseThrow(IllegalStateException::new);
    }

    public Card determineOpeningCard() {
        if (cardsPlayed.isEmpty()) {
            return null;
        }
        return cardsPlayed.get(startingPlayer);
    }

    public Seat determineHighestCardSeat() {
        return cardsPlayed.entrySet().stream()
                .filter(entry -> entry.getValue() == determineHighestCard())
                .map(Map.Entry::getKey)
                .findFirst()
                .orElseThrow(IllegalStateException::new);
    }

    public boolean isTrickFinished() {
        return cardsPlayed.values().size() == 4 &&
                !cardsPlayed.containsValue(null);
    }
}

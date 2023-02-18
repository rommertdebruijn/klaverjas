package com.keemerz.klaverjas.domain;

import com.keemerz.klaverjas.comparator.HighestCardInTrickComparator;

import java.util.*;
import java.util.stream.Collectors;

import static com.keemerz.klaverjas.comparator.NaturalOrderCardComparator.RANK_NATURAL_ORDER;
import static com.keemerz.klaverjas.domain.Rank.*;
import static com.keemerz.klaverjas.domain.Seat.*;

public class Trick {

    private final Suit trump;
    private final Seat startingPlayer;
    private Map<Seat, Card> cardsPlayed = new HashMap<>();
    private boolean comboClaimed;
    private Seat trickWinner;

    public Trick(Suit trump, Seat startingPlayer, Map<Seat, Card> cardsPlayed, Seat trickWinner, boolean comboClaimed) {
        this.trump = trump;
        this.startingPlayer = startingPlayer;
        this.cardsPlayed = cardsPlayed;
        this.comboClaimed = comboClaimed;
        this.trickWinner = trickWinner;
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

    public boolean isComboClaimed() {
        return comboClaimed;
    }

    public void claimCombo() {
        this.comboClaimed = true;
    }

    public Seat getTrickWinner() {
        return trickWinner;
    }

    public void setTrickWinner(Seat trickWinner) {
        this.trickWinner = trickWinner;
    }

    public Trick rotateForSeat(Seat currentPlayerSeat) {
        Map<Seat, Card> rotatedCardsPlayed = new HashMap<>();
        rotatedCardsPlayed.put(SOUTH, cardsPlayed.get(currentPlayerSeat));
        rotatedCardsPlayed.put(WEST, cardsPlayed.get(currentPlayerSeat.getLeftHandPlayer()));
        rotatedCardsPlayed.put(NORTH, cardsPlayed.get(currentPlayerSeat.getPartner()));
        rotatedCardsPlayed.put(EAST, cardsPlayed.get(currentPlayerSeat.getRightHandPlayer()));


        Seat trickWinner = this.trickWinner != null ? this.trickWinner.rotateForSeat(currentPlayerSeat) : null;
        return new Trick(this.trump, null, rotatedCardsPlayed, trickWinner, comboClaimed);
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

    public int nrOfComboPoints() {
        List<Card> allCards = new ArrayList<>(cardsPlayed.values());
        if (allCardsHaveSameRank(allCards)) { // rather exotic
            if (allCards.get(0).getRank() == JACK) {
                return 200;
            }
            return 100;
        }

        int nrOfRoemPoints = 0;
        for (Suit suit : Suit.values()) {
            List<Card> cardsForSuit = allCards.stream()
                    .filter(card -> card.getSuit() == suit)
                    .collect(Collectors.toList());

            nrOfRoemPoints += nrOfComboPoints(cardsForSuit); // 3-kaart or 4-kaart roem
        }

        if (allCards.contains(Card.of(trump, QUEEN)) &&
            allCards.contains(Card.of(trump, KING))) {
            nrOfRoemPoints += 20; // stuk
        }

        return nrOfRoemPoints;
    }

    private boolean allCardsHaveSameRank(Collection<Card> allCards) {
        return allCards.stream().map(Card::getRank).collect(Collectors.toSet()).size() == 1;
    }

    private int nrOfComboPoints(List<Card> cardsForSuit) {
        if (cardsForSuit.size() < 3) {
            return 0; // cant have roem in 2 cards or less
        }

        List<Integer> cardIndices = cardsForSuit.stream()
                .map(card -> RANK_NATURAL_ORDER.indexOf(card.getRank()))
                .sorted(Integer::compareTo)
                .collect(Collectors.toList());

        if (cardIndices.size() == 3 &&
               cardIndices.get(2) - cardIndices.get(0) == 2) {
            return 20;
        }

        if (cardIndices.size() == 4) {
            if (cardIndices.get(3) - cardIndices.get(0) == 3) {
                return 50;
            }

            if (cardIndices.get(2) - cardIndices.get(0) == 2 ||
                cardIndices.get(3) - cardIndices.get(1) == 2) {
                return 20;
            }
        }
        return 0;
    }
}

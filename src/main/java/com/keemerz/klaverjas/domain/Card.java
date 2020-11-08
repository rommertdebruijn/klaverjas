package com.keemerz.klaverjas.domain;

import java.util.Objects;
import java.util.UUID;

public class Card {
    private String cardId;
    private Suit suit;
    private Rank rank;

    public static Card of(Suit suit, Rank rank) {
        return new Card(UUID.randomUUID().toString(), suit, rank);
    }

    private Card(String cardId, Suit suit, Rank rank) {
        this.cardId = cardId;
        this.suit = suit;
        this.rank = rank;
    }

    public String getCardId() {
        return cardId;
    }

    public Suit getSuit() {
        return suit;
    }

    public Rank getRank() {
        return rank;
    }

    // note that we skip cardId in equals/hashcode, because we sometimes want to pass cards without revealing their id. Suit+Rank is unique enough
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return suit == card.suit &&
                rank == card.rank;
    }

    @Override
    public int hashCode() {
        return Objects.hash(suit, rank);
    }

    @Override
    public String toString() {
        return "Card{" +
                "suit=" + suit +
                ", rank=" + rank +
                '}';
    }
}

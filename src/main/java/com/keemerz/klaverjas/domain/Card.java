package com.keemerz.klaverjas.domain;

public class Card {
    private Suit suit;
    private Rank rank;

    public static Card of(Suit suit, Rank rank) {
        return new Card(suit, rank);
    }

    private Card(Suit suit, Rank rank) {
        this.suit = suit;
        this.rank = rank;
    }

    public Suit getSuit() {
        return suit;
    }

    public Rank getRank() {
        return rank;
    }


}

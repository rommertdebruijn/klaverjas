package com.keemerz.klaverjas.domain;

import java.util.*;

public class ShuffledDeck {

    private List<Card> cards = new ArrayList<>();

    public ShuffledDeck() {
        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                cards.add(Card.of(suit, rank));
            }
        }
        Collections.shuffle(cards);
    }

    public List<Card> getCards() {
        return Collections.unmodifiableList(cards);
    }
}

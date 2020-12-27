package com.keemerz.klaverjas.domain;

import com.keemerz.klaverjas.encoding.DeckSignature;

import java.util.*;

public class ShuffledDeck implements Deck {

    private List<Card> cards = new ArrayList<>();
    private String signature;

    public ShuffledDeck() {
        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                cards.add(Card.of(suit, rank));
            }
        }
        Collections.shuffle(cards);
        signature = DeckSignature.encrypt(cards);
    }

    @Override
    public List<Card> getCards() {
        return Collections.unmodifiableList(cards);
    }

    @Override
    public String getSignature() {
        return signature;
    }
}

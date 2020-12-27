package com.keemerz.klaverjas.domain;

import com.keemerz.klaverjas.encoding.DeckSignature;

import java.util.Collections;
import java.util.List;

public class StackedDeck implements Deck {

    private List<Card> cards;
    private String signature;

    public StackedDeck(String signature) {
        cards = DeckSignature.decrypt(signature);
        this.signature = signature;
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

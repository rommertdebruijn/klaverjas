package com.keemerz.klaverjas.domain;

public class DeckFactory {

    // set a deck signature here to play the same game over and over again. For regular use, set signature to null
    static final String DEBUG_DECK_SIGNATURE = null;

    public static Deck getDeck() {
        if (DEBUG_DECK_SIGNATURE != null) {
            // for debugging purposes
            return new StackedDeck(DEBUG_DECK_SIGNATURE);
        }
        // regular game
        return new ShuffledDeck();
    }
}

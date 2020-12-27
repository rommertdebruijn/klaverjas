package com.keemerz.klaverjas.domain;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.keemerz.klaverjas.domain.Suit.*;

public class BiddingFactory {

    // set a Suit here to play the same suit over and over again. For regular use, set suit to null
    static final Suit DEBUG_SUIT = null;

    public static Bidding createFirstGameBidding() {
        if (DEBUG_SUIT != null) {
            return new Bidding(DEBUG_SUIT);
        }
        // For regular games, first game always clubs
        return new Bidding(CLUBS);
    }

    public static Bidding createBidding() {
        if (DEBUG_SUIT != null) {
            return new Bidding(DEBUG_SUIT);
        }

        // Return bidding for random trump
        List<Suit> randomList = Arrays.asList(Suit.values());
        Collections.shuffle(randomList);
        Suit randomTrump = randomList.get(0);
        return new Bidding(randomTrump);
    }
}

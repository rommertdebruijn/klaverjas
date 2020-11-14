package com.keemerz.klaverjas.comparator;

import com.keemerz.klaverjas.domain.Card;
import com.keemerz.klaverjas.domain.Rank;
import com.keemerz.klaverjas.domain.Suit;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static com.keemerz.klaverjas.domain.Rank.*;
import static com.keemerz.klaverjas.domain.Rank.JACK;
import static com.keemerz.klaverjas.domain.Suit.*;

public class NaturalOrderCardComparator implements Comparator<Card> {
    public static final List<Rank> RANK_NATURAL_ORDER = Arrays.asList(
            SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING, ACE);

    private static final List<Suit> SUIT_NATURAL_ORDER = Arrays.asList(
            CLUBS, DIAMONDS, SPADES, HEARTS);

    @Override
    public int compare(Card c1, Card c2) {
        if (c1.getSuit() != c2.getSuit()) {
            return Integer.compare(SUIT_NATURAL_ORDER.indexOf(c1.getSuit()), SUIT_NATURAL_ORDER.indexOf(c2.getSuit()));
        }
        return Integer.compare(RANK_NATURAL_ORDER.indexOf(c1.getRank()), RANK_NATURAL_ORDER.indexOf(c2.getRank()));
    }
}

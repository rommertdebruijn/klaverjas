package com.keemerz.klaverjas.comparator;

import com.keemerz.klaverjas.domain.Card;
import com.keemerz.klaverjas.domain.Rank;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static com.keemerz.klaverjas.domain.Rank.*;

public class NonTrumpOrderComparator implements Comparator<Card> {

    private static final List<Rank> TRUMP_ORDER = Arrays.asList(
            SEVEN, EIGHT, NINE, JACK, QUEEN, KING, TEN, ACE);


    @Override
    public int compare(Card c1, Card c2) {
        return Integer.compare(TRUMP_ORDER.indexOf(c1.getRank()), TRUMP_ORDER.indexOf(c2.getRank()));
    }
}

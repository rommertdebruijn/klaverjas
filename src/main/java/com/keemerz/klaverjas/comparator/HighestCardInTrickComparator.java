package com.keemerz.klaverjas.comparator;

import com.keemerz.klaverjas.domain.Card;
import com.keemerz.klaverjas.domain.Rank;
import com.keemerz.klaverjas.domain.Suit;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static com.keemerz.klaverjas.domain.Rank.*;

public class HighestCardInTrickComparator implements Comparator<Card> {
    private static final List<Rank> NON_TRUMP_ORDER = Arrays.asList(
            SEVEN, EIGHT, NINE, JACK, QUEEN, KING, TEN, ACE);

    private static final List<Rank> TRUMP_ORDER = Arrays.asList(
            SEVEN, EIGHT, QUEEN, KING, TEN, ACE, NINE, JACK);

    private Suit trump;


    public HighestCardInTrickComparator(Suit trump) {
        this.trump = trump;
    }

    @Override
    public int compare(Card c1, Card c2) {
        if (c1.getSuit() == trump) {
            if (c2.getSuit() == trump) {
                return Integer.compare(TRUMP_ORDER.indexOf(c1.getRank()), TRUMP_ORDER.indexOf(c2.getRank()));
            } else {
                return 1;
            }
        }
        if (c2.getSuit() != trump) {
            return Integer.compare(NON_TRUMP_ORDER.indexOf(c1.getRank()), NON_TRUMP_ORDER.indexOf(c2.getRank()));
        } else {
            return -1;
        }
    }
}

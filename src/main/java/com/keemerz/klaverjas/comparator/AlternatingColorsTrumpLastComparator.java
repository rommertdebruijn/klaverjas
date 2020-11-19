package com.keemerz.klaverjas.comparator;

import com.keemerz.klaverjas.domain.Suit;

import java.util.Arrays;
import java.util.List;

import static com.keemerz.klaverjas.comparator.CardInHandComparator.SUIT_NATURAL_ORDER;

public class AlternatingColorsTrumpLastComparator implements java.util.Comparator<Suit> {
    private Suit trump;

    public AlternatingColorsTrumpLastComparator(Suit trump) {
        this.trump = trump;
    }

    @Override
    public int compare(Suit s1, Suit s2) {
        if (getIndex(s1) <= getIndex(trump) && getIndex(s2) <= getIndex(trump) ||
            getIndex(s1) > getIndex(trump) && getIndex(s2) > getIndex(trump)) {
            return Integer.compare(getIndex(s1), getIndex(s2));
        }

        if (getIndex(s1) <= getIndex(trump) && getIndex(s2) > getIndex(trump)) {
            return 1;
        }
        return -1;
    }

    private int getIndex(Suit s1) {
        return SUIT_NATURAL_ORDER.indexOf(s1);
    }
}

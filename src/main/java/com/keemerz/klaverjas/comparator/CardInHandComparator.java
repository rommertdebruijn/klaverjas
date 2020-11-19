package com.keemerz.klaverjas.comparator;

import com.keemerz.klaverjas.domain.Card;
import com.keemerz.klaverjas.domain.Rank;
import com.keemerz.klaverjas.domain.Suit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static com.keemerz.klaverjas.domain.Rank.*;
import static com.keemerz.klaverjas.domain.Suit.*;

public class CardInHandComparator implements Comparator<Card> {
   static final List<Suit> SUIT_NATURAL_ORDER = Arrays.asList(
            HEARTS, SPADES, DIAMONDS, CLUBS);

    private Suit trump;

    public CardInHandComparator(Suit trump) {
        this.trump = trump;
    }

    @Override
    public int compare(Card c1, Card c2) {
        List<Suit> reOrderedSuits = new ArrayList<>(SUIT_NATURAL_ORDER);
        reOrderedSuits.sort(new AlternatingColorsTrumpLastComparator(trump));

        if (c1.getSuit() != c2.getSuit()) {
            return Integer.compare(reOrderedSuits.indexOf(c1.getSuit()), reOrderedSuits.indexOf(c2.getSuit()));
        }
        if (c1.getSuit() == trump) {
            return new TrumpOrderComparator().compare(c1, c2);
        }
        return new NonTrumpOrderComparator().compare(c1, c2);
    }
}

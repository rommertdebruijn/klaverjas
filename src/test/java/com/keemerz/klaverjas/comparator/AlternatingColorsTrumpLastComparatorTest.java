package com.keemerz.klaverjas.comparator;

import com.keemerz.klaverjas.domain.Suit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.keemerz.klaverjas.comparator.CardInHandComparator.SUIT_NATURAL_ORDER;
import static com.keemerz.klaverjas.domain.Suit.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

class AlternatingColorsTrumpLastComparatorTest {

    private List<Suit> suits;

    @BeforeEach
    void setUp() {
        suits = new ArrayList<>(SUIT_NATURAL_ORDER);
    }

    @Test
    public void reorderWithAlternatingColorClubsLast() {
        suits.sort(new AlternatingColorsTrumpLastComparator(CLUBS));
        assertThat(suits, is(Arrays.asList(HEARTS, SPADES, DIAMONDS, CLUBS)));
    }

    @Test
    public void reorderWithAlternatingColorDiamondsLast() {
        suits.sort(new AlternatingColorsTrumpLastComparator(DIAMONDS));
        assertThat(suits, is(Arrays.asList(CLUBS, HEARTS, SPADES, DIAMONDS)));
    }

    @Test
    public void reorderWithAlternatingColorSpadesLast() {
        suits.sort(new AlternatingColorsTrumpLastComparator(SPADES));
        assertThat(suits, is(Arrays.asList(DIAMONDS, CLUBS, HEARTS, SPADES)));
    }

    @Test
    public void reorderWithAlternatingColorHeartsLast() {
        suits.sort(new AlternatingColorsTrumpLastComparator(HEARTS));
        assertThat(suits, is(Arrays.asList(SPADES, DIAMONDS, CLUBS, HEARTS)));
    }

}
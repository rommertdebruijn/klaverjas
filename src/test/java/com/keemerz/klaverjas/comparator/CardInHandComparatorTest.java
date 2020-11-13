package com.keemerz.klaverjas.comparator;

import com.keemerz.klaverjas.domain.Card;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static com.keemerz.klaverjas.domain.Rank.*;
import static com.keemerz.klaverjas.domain.Suit.CLUBS;
import static com.keemerz.klaverjas.domain.Suit.HEARTS;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

class CardInHandComparatorTest {

    @Test
    public void mixedSuitShouldBeSortedBySuitThenRankWithTrumpLastInTrumpOrder() {
        List<Card> actual = Arrays.asList(
                Card.of(HEARTS, SEVEN),
                Card.of(CLUBS, ACE),
                Card.of(HEARTS, TEN),
                Card.of(HEARTS, EIGHT),
                Card.of(CLUBS, JACK),
                Card.of(HEARTS, JACK),
                Card.of(HEARTS, ACE),
                Card.of(CLUBS, SEVEN),
                Card.of(HEARTS, NINE),
                Card.of(HEARTS, QUEEN),
                Card.of(CLUBS, QUEEN),
                Card.of(CLUBS, TEN),
                Card.of(CLUBS, EIGHT),
                Card.of(CLUBS, NINE),
                Card.of(HEARTS, KING),
                Card.of(CLUBS, KING));

        List<Card> expected = Arrays.asList(
                Card.of(HEARTS, SEVEN),
                Card.of(HEARTS, EIGHT),
                Card.of(HEARTS, NINE),
                Card.of(HEARTS, JACK),
                Card.of(HEARTS, QUEEN),
                Card.of(HEARTS, KING),
                Card.of(HEARTS, TEN),
                Card.of(HEARTS, ACE),
                Card.of(CLUBS, SEVEN),
                Card.of(CLUBS, EIGHT),
                Card.of(CLUBS, QUEEN),
                Card.of(CLUBS, KING),
                Card.of(CLUBS, TEN),
                Card.of(CLUBS, ACE),
                Card.of(CLUBS, NINE),
                Card.of(CLUBS, JACK));

        actual.sort(new CardInHandComparator(CLUBS));
        assertThat(actual, is(expected));
    }

}
package com.keemerz.klaverjas.comparator;

import com.keemerz.klaverjas.domain.Card;
import com.keemerz.klaverjas.domain.Suit;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static com.keemerz.klaverjas.domain.Rank.*;
import static com.keemerz.klaverjas.domain.Suit.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

class NaturalOrderCardComparatorTest {

    @Test
    public void singleSuitShouldBeSortedByRank() {
        List<Card> actual = Arrays.asList(
                Card.of(HEARTS, SEVEN),
                Card.of(HEARTS, TEN),
                Card.of(HEARTS, EIGHT),
                Card.of(HEARTS, JACK),
                Card.of(HEARTS, ACE),
                Card.of(HEARTS, NINE),
                Card.of(HEARTS, QUEEN),
                Card.of(HEARTS, KING));

        List<Card> expected = Arrays.asList(
                Card.of(HEARTS, SEVEN),
                Card.of(HEARTS, EIGHT),
                Card.of(HEARTS, NINE),
                Card.of(HEARTS, TEN),
                Card.of(HEARTS, JACK),
                Card.of(HEARTS, QUEEN),
                Card.of(HEARTS, KING),
                Card.of(HEARTS, ACE));

        actual.sort(new NaturalOrderCardComparator());
        assertThat(actual, is(expected));
    }

    @Test
    public void mixedSuitShouldBeSortedBySuitThenRank() {
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
                Card.of(CLUBS, SEVEN),
                Card.of(CLUBS, EIGHT),
                Card.of(CLUBS, NINE),
                Card.of(CLUBS, TEN),
                Card.of(CLUBS, JACK),
                Card.of(CLUBS, QUEEN),
                Card.of(CLUBS, KING),
                Card.of(CLUBS, ACE),
                Card.of(HEARTS, SEVEN),
                Card.of(HEARTS, EIGHT),
                Card.of(HEARTS, NINE),
                Card.of(HEARTS, TEN),
                Card.of(HEARTS, JACK),
                Card.of(HEARTS, QUEEN),
                Card.of(HEARTS, KING),
                Card.of(HEARTS, ACE));

        actual.sort(new NaturalOrderCardComparator());
        assertThat(actual, is(expected));
    }

}
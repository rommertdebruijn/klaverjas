package com.keemerz.klaverjas.comparator;

import com.keemerz.klaverjas.domain.Card;
import com.keemerz.klaverjas.domain.Suit;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static com.keemerz.klaverjas.domain.Suit.*;
import static com.keemerz.klaverjas.domain.Rank.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;

class HighestCardComparatorTest {

    @Test
    public void trumpShouldBeFirst() {
        Suit trump = CLUBS;
        List<Card> actual = Arrays.asList(
                Card.of(HEARTS, SEVEN),
                Card.of(HEARTS, TEN),
                Card.of(CLUBS, SEVEN),
                Card.of(HEARTS, ACE));

        List<Card> expected = Arrays.asList(
                Card.of(HEARTS, SEVEN),
                Card.of(HEARTS, TEN),
                Card.of(HEARTS, ACE),
                Card.of(CLUBS, SEVEN));

        actual.sort(new HighestCardInTrickComparator(CLUBS));
        assertThat(actual, is(expected));
    }

    @Test
    public void trumpCardsShouldHaveDifferentOrder() {
        List<Card> actual = Arrays.asList(
                Card.of(HEARTS, NINE),
                Card.of(CLUBS, NINE),
                Card.of(HEARTS, JACK),
                Card.of(CLUBS, JACK),
                Card.of(HEARTS, TEN),
                Card.of(CLUBS, TEN),
                Card.of(HEARTS, ACE),
                Card.of(CLUBS, ACE)
        );

        List<Card> expected = Arrays.asList(
                Card.of(HEARTS, NINE),
                Card.of(HEARTS, JACK),
                Card.of(HEARTS, TEN),
                Card.of(HEARTS, ACE),
                Card.of(CLUBS, TEN),
                Card.of(CLUBS, ACE),
                Card.of(CLUBS, NINE),
                Card.of(CLUBS, JACK)
        );

        actual.sort(new HighestCardInTrickComparator(CLUBS));
        assertThat(actual, is(expected));
    }

}
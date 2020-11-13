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

        actual.sort(new HighestCardInTrickComparator(CLUBS, HEARTS));
        assertThat(actual, is(expected));
    }

    @Test
    public void trumpCardsShouldHaveDifferentOrder() {
        List<Card> actual = Arrays.asList(
                Card.of(CLUBS, JACK),
                Card.of(HEARTS, NINE),
                Card.of(DIAMONDS, ACE),
                Card.of(CLUBS, ACE)
        );

        List<Card> expected = Arrays.asList(
                Card.of(DIAMONDS, ACE),
                Card.of(HEARTS, NINE),
                Card.of(CLUBS, ACE),
                Card.of(CLUBS, JACK)
        );

        actual.sort(new HighestCardInTrickComparator(CLUBS, HEARTS));
        assertThat(actual, is(expected));
    }


    @Test
    public void startColorShouldWinFromOtherCards() {
        List<Card> actual = Arrays.asList(
                Card.of(HEARTS, SEVEN),
                Card.of(SPADES, ACE),
                Card.of(DIAMONDS, ACE),
                Card.of(DIAMONDS, TEN)
        );

        List<Card> expected = Arrays.asList(
                Card.of(DIAMONDS, TEN),
                Card.of(DIAMONDS, ACE),
                Card.of(SPADES, ACE),
                Card.of(HEARTS, SEVEN)
        );

        actual.sort(new HighestCardInTrickComparator(CLUBS, HEARTS));
        assertThat(actual, is(expected));
    }

}
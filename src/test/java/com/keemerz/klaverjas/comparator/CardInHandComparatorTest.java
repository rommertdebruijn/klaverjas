package com.keemerz.klaverjas.comparator;

import com.keemerz.klaverjas.domain.Card;
import com.keemerz.klaverjas.domain.Suit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static com.keemerz.klaverjas.domain.Rank.*;
import static com.keemerz.klaverjas.domain.Suit.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

class CardInHandComparatorTest {

    private List<Card> initialHand;

    @BeforeEach
    void setUp() {
        initialHand = Arrays.asList(
                Card.of(CLUBS, JACK),
                Card.of(HEARTS, ACE),
                Card.of(SPADES, ACE),
                Card.of(SPADES, JACK),
                Card.of(DIAMONDS, JACK),
                Card.of(DIAMONDS, ACE),
                Card.of(CLUBS, ACE),
                Card.of(HEARTS, JACK)
        );
    }

    @Test
    public void mixedSuitShouldBeSortedBySuitThenRankWithClubsLastInTrumpOrder() {
        List<Card> expected = Arrays.asList(
                Card.of(HEARTS, JACK),
                Card.of(HEARTS, ACE),
                Card.of(SPADES, JACK),
                Card.of(SPADES, ACE),
                Card.of(DIAMONDS, JACK),
                Card.of(DIAMONDS, ACE),
                Card.of(CLUBS, ACE),
                Card.of(CLUBS, JACK)
        );

        initialHand.sort(new CardInHandComparator(CLUBS));
        assertThat(initialHand, is(expected));
    }

    @Test
    public void mixedSuitShouldBeSortedBySuitThenRankWithSpadesLastInTrumpOrder() {
        List<Card> expected = Arrays.asList(
                Card.of(DIAMONDS, JACK),
                Card.of(DIAMONDS, ACE),
                Card.of(CLUBS, JACK),
                Card.of(CLUBS, ACE),
                Card.of(HEARTS, JACK),
                Card.of(HEARTS, ACE),
                Card.of(SPADES, ACE),
                Card.of(SPADES, JACK)
        );

        initialHand.sort(new CardInHandComparator(SPADES));
        assertThat(initialHand, is(expected));
    }

    @Test
    public void mixedSuitShouldBeSortedBySuitThenRankWithDiamondsLastInTrumpOrder() {
        List<Card> expected = Arrays.asList(
                Card.of(CLUBS, JACK),
                Card.of(CLUBS, ACE),
                Card.of(HEARTS, JACK),
                Card.of(HEARTS, ACE),
                Card.of(SPADES, JACK),
                Card.of(SPADES, ACE),
                Card.of(DIAMONDS, ACE),
                Card.of(DIAMONDS, JACK)
        );

        initialHand.sort(new CardInHandComparator(DIAMONDS));
        assertThat(initialHand, is(expected));
    }

    @Test
    public void mixedSuitShouldBeSortedBySuitThenRankWithHeartsLastInTrumpOrder() {
        List<Card> expected = Arrays.asList(
                Card.of(SPADES, JACK),
                Card.of(SPADES, ACE),
                Card.of(DIAMONDS, JACK),
                Card.of(DIAMONDS, ACE),
                Card.of(CLUBS, JACK),
                Card.of(CLUBS, ACE),
                Card.of(HEARTS, ACE),
                Card.of(HEARTS, JACK)
        );

        initialHand.sort(new CardInHandComparator(HEARTS));
        assertThat(initialHand, is(expected));
    }

    @Test
    public void singleNonTrumpSuitShouldHaveCorrectOrder() {
        List<Card> singleSuitHand = Arrays.asList(
                Card.of(DIAMONDS, KING),
                Card.of(DIAMONDS, SEVEN),
                Card.of(DIAMONDS, TEN),
                Card.of(DIAMONDS, JACK),
                Card.of(DIAMONDS, QUEEN),
                Card.of(DIAMONDS, NINE),
                Card.of(DIAMONDS, ACE),
                Card.of(DIAMONDS, EIGHT)
        );

        List<Card> expected = Arrays.asList(
                Card.of(DIAMONDS, SEVEN),
                Card.of(DIAMONDS, EIGHT),
                Card.of(DIAMONDS, NINE),
                Card.of(DIAMONDS, JACK),
                Card.of(DIAMONDS, QUEEN),
                Card.of(DIAMONDS, KING),
                Card.of(DIAMONDS, TEN),
                Card.of(DIAMONDS, ACE)
        );

        singleSuitHand.sort(new CardInHandComparator(SPADES));
        assertThat(singleSuitHand, is(expected));
    }

    @Test
    public void singleTrumpSuitShouldHaveCorrectOrder() {
        List<Card> singleSuitHand = Arrays.asList(
                Card.of(DIAMONDS, KING),
                Card.of(DIAMONDS, SEVEN),
                Card.of(DIAMONDS, TEN),
                Card.of(DIAMONDS, JACK),
                Card.of(DIAMONDS, QUEEN),
                Card.of(DIAMONDS, NINE),
                Card.of(DIAMONDS, ACE),
                Card.of(DIAMONDS, EIGHT)
        );

        List<Card> expected = Arrays.asList(
                Card.of(DIAMONDS, SEVEN),
                Card.of(DIAMONDS, EIGHT),
                Card.of(DIAMONDS, QUEEN),
                Card.of(DIAMONDS, KING),
                Card.of(DIAMONDS, TEN),
                Card.of(DIAMONDS, ACE),
                Card.of(DIAMONDS, NINE),
                Card.of(DIAMONDS, JACK)
        );

        singleSuitHand.sort(new CardInHandComparator(DIAMONDS));
        assertThat(singleSuitHand, is(expected));
    }

}
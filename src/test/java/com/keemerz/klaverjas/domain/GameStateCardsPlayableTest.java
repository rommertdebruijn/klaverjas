package com.keemerz.klaverjas.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.keemerz.klaverjas.domain.Rank.*;
import static com.keemerz.klaverjas.domain.Seat.*;
import static com.keemerz.klaverjas.domain.Suit.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

class GameStateCardsPlayableTest {

    private GameState gameState;

    @BeforeEach
    void setUp() {
        gameState = new TestGameStateBuilder()
            .withTurn(SOUTH)
            .build();
    }

    @Test
    public void firstCardCanBeAnything() {
        Trick currentTrick = new TestTrickBuilder()
                .withTrump(CLUBS)
                .build();

        List<Card> hand = Arrays.asList(
                Card.of(SPADES, SEVEN), Card.of(SPADES, TEN),
                Card.of(DIAMONDS, TEN), Card.of(DIAMONDS, ACE),
                Card.of(CLUBS, QUEEN),  Card.of(CLUBS, JACK));

        List<Card> expected = Arrays.asList(
                Card.of(SPADES, SEVEN), Card.of(SPADES, TEN),
                Card.of(DIAMONDS, TEN), Card.of(DIAMONDS, ACE),
                Card.of(CLUBS, QUEEN),  Card.of(CLUBS, JACK));

        List<Card> actual = gameState.determinePlayableCards(currentTrick, hand);
        assertThat(expected, is(actual));
    }

    @Test
    public void requestedSuitShouldBeOnlySuitPlayable() {
        Trick currentTrick = new TestTrickBuilder()
                .withTrump(CLUBS)
                .withCardPlayed(NORTH, Card.of(DIAMONDS, SEVEN))
                .withCardPlayed(EAST, Card.of(DIAMONDS, KING))
                .build();

        List<Card> hand = Arrays.asList(
                        Card.of(SPADES, SEVEN), Card.of(SPADES, TEN),
                        Card.of(DIAMONDS, TEN), Card.of(DIAMONDS, ACE),
                        Card.of(CLUBS, QUEEN),  Card.of(CLUBS, JACK));

        List<Card> expected = Arrays.asList(
                Card.of(DIAMONDS, TEN),
                Card.of(DIAMONDS, ACE));

        List<Card> actual = gameState.determinePlayableCards(currentTrick, hand);
        assertThat(actual, is(expected));
    }

    @Test
    public void trumpShouldBeOnlyPlayableSuitWhenRenonceInStartSuit() {
        Trick currentTrick = new TestTrickBuilder()
                .withTrump(CLUBS)
                .withCardPlayed(NORTH, Card.of(DIAMONDS, SEVEN))
                .withCardPlayed(EAST, Card.of(DIAMONDS, KING))
                .build();

        List<Card> hand = Arrays.asList(
                Card.of(SPADES, SEVEN), Card.of(SPADES, TEN),
                Card.of(HEARTS, TEN), Card.of(HEARTS, ACE),
                Card.of(CLUBS, QUEEN),  Card.of(CLUBS, JACK));

        List<Card> expected = Arrays.asList(
                Card.of(CLUBS, QUEEN),
                Card.of(CLUBS, JACK));

        List<Card> actual = gameState.determinePlayableCards(currentTrick, hand);
        assertThat(actual, is(expected));
    }

    @Test
    public void onlyHigherTrumpCardPlayableWhenTrumpIsRequested() {
        Trick currentTrick = new TestTrickBuilder()
                .withTrump(CLUBS)
                .withCardPlayed(NORTH, Card.of(CLUBS, SEVEN))
                .withCardPlayed(EAST, Card.of(CLUBS, KING))
                .build();

        List<Card> hand = Arrays.asList(
                Card.of(SPADES, SEVEN), Card.of(SPADES, TEN),
                Card.of(HEARTS, TEN), Card.of(HEARTS, ACE),
                Card.of(CLUBS, QUEEN),  Card.of(CLUBS, JACK));

        List<Card> expected = Collections.singletonList(Card.of(CLUBS, JACK));

        List<Card> actual = gameState.determinePlayableCards(currentTrick, hand);
        assertThat(actual, is(expected));
    }

    @Test
    public void whenNoHigherTrumpCardInHandThenOtherTrumpCardsArePlayableWhenTrumpIsRequested() {
        Trick currentTrick = new TestTrickBuilder()
                .withTrump(CLUBS)
                .withCardPlayed(NORTH, Card.of(CLUBS, SEVEN))
                .withCardPlayed(EAST, Card.of(CLUBS, KING))
                .build();

        List<Card> hand = Arrays.asList(
                Card.of(SPADES, SEVEN), Card.of(SPADES, TEN),
                Card.of(HEARTS, TEN), Card.of(HEARTS, ACE),
                Card.of(CLUBS, EIGHT),  Card.of(CLUBS, QUEEN));

        List<Card> expected = Arrays.asList(
                Card.of(CLUBS, EIGHT),  Card.of(CLUBS, QUEEN));

        List<Card> actual = gameState.determinePlayableCards(currentTrick, hand);
        assertThat(actual, is(expected));
    }

    @Test
    public void whenPartnerLeadsTrickAndRenonceInStartColorThenAllCardsArePlayable() {
        Trick currentTrick = new TestTrickBuilder()
                .withTrump(CLUBS)
                .withCardPlayed(NORTH, Card.of(DIAMONDS, ACE))
                .withCardPlayed(EAST, Card.of(DIAMONDS, KING))
                .build();

        List<Card> hand = Arrays.asList(
                Card.of(SPADES, SEVEN), Card.of(SPADES, TEN),
                Card.of(HEARTS, TEN), Card.of(HEARTS, ACE),
                Card.of(CLUBS, QUEEN),  Card.of(CLUBS, JACK));

        // all cards allowed because maatslag
        List<Card> expected = Arrays.asList(
                Card.of(SPADES, SEVEN), Card.of(SPADES, TEN),
                Card.of(HEARTS, TEN), Card.of(HEARTS, ACE),
                Card.of(CLUBS, QUEEN),  Card.of(CLUBS, JACK)
        );

        List<Card> actual = gameState.determinePlayableCards(currentTrick, hand);
        assertThat(actual, is(expected));
    }

    @Test
    public void whenPartnerLeadsTrickWithTrumpAndRenonceInStartSuitThenAllCardsArePlayable() {
        Trick currentTrick = new TestTrickBuilder()
                .withTrump(CLUBS)
                .withStartingPlayer(WEST)
                .withCardPlayed(WEST, Card.of(DIAMONDS, EIGHT))
                .withCardPlayed(NORTH, Card.of(CLUBS, SEVEN)) // plays trump, leads trick
                .withCardPlayed(EAST, Card.of(DIAMONDS, KING))
                .build();

        List<Card> hand = Arrays.asList(
                Card.of(SPADES, SEVEN), Card.of(SPADES, TEN),
                Card.of(HEARTS, TEN), Card.of(HEARTS, ACE),
                Card.of(CLUBS, QUEEN),  Card.of(CLUBS, JACK));

        // all cards allowed because maatslag
        List<Card> expected = Arrays.asList(
                Card.of(SPADES, SEVEN), Card.of(SPADES, TEN),
                Card.of(HEARTS, TEN), Card.of(HEARTS, ACE),
                Card.of(CLUBS, QUEEN),  Card.of(CLUBS, JACK)
        );

        List<Card> actual = gameState.determinePlayableCards(currentTrick, hand);
        assertThat(actual, is(expected));
    }

    @Test
    public void whenOpponentLeadsTrickWithTrumpOnlyHigherTrumpCardsArePlayable() {
        Trick currentTrick = new TestTrickBuilder()
                .withTrump(CLUBS)
                .withCardPlayed(NORTH, Card.of(DIAMONDS, SEVEN)) // plays trump, leads trick
                .withCardPlayed(EAST, Card.of(CLUBS, KING))
                .build();

        List<Card> hand = Arrays.asList(
                Card.of(SPADES, SEVEN), Card.of(SPADES, TEN),
                Card.of(HEARTS, TEN), Card.of(HEARTS, ACE),
                Card.of(CLUBS, QUEEN),  Card.of(CLUBS, JACK));

        // only higher trump allowed
        List<Card> expected = Collections.singletonList(Card.of(CLUBS, JACK));

        List<Card> actual = gameState.determinePlayableCards(currentTrick, hand);
        assertThat(actual, is(expected));
    }

    @Test
    public void whenOpponentLeadsTrickWithTrumpButOnlyLowerTrumpCardsInHandThenAllNonTrumpCardsArePlayable() {
        Trick currentTrick = new TestTrickBuilder()
                .withTrump(CLUBS)
                .withCardPlayed(NORTH, Card.of(DIAMONDS, SEVEN)) // plays trump, leads trick
                .withCardPlayed(EAST, Card.of(CLUBS, KING))
                .build();

        List<Card> hand = Arrays.asList(
                Card.of(SPADES, SEVEN), Card.of(SPADES, TEN),
                Card.of(HEARTS, TEN), Card.of(HEARTS, ACE),
                Card.of(CLUBS, SEVEN),  Card.of(CLUBS, EIGHT));

        // since no higher trump is available, we're not allowed to play lower trump card
        List<Card> expected = Arrays.asList(
                Card.of(SPADES, SEVEN), Card.of(SPADES, TEN),
                Card.of(HEARTS, TEN), Card.of(HEARTS, ACE));

        List<Card> actual = gameState.determinePlayableCards(currentTrick, hand);
        assertThat(actual, is(expected));
    }

    @Test
    public void ifOnlyTrumpCardsLeftThenTrumpIsAlwaysAllowed() {
        Trick currentTrick = new TestTrickBuilder()
                .withTrump(CLUBS)
                .withCardPlayed(NORTH, Card.of(DIAMONDS, SEVEN))
                .withCardPlayed(EAST, Card.of(CLUBS, KING))
                .build();

        List<Card> hand = Arrays.asList(
                Card.of(CLUBS, SEVEN),  Card.of(CLUBS, QUEEN));

        List<Card> expected = Arrays.asList(
                Card.of(CLUBS, SEVEN),
                Card.of(CLUBS, QUEEN));

        List<Card> actual = gameState.determinePlayableCards(currentTrick, hand);
        assertThat(actual, is(expected));
    }
}
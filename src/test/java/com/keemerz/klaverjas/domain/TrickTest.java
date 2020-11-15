package com.keemerz.klaverjas.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.HashMap;
import java.util.Map;

import static com.keemerz.klaverjas.domain.Rank.*;
import static com.keemerz.klaverjas.domain.Seat.*;
import static com.keemerz.klaverjas.domain.Suit.*;
import static org.junit.jupiter.api.Assertions.assertNull;

class TrickTest {

    Trick baseTrick;

    @BeforeEach
    void setUp() {
        Map<Seat, Card> cards = new HashMap<>();
        cards.put(NORTH,  Card.of(SPADES, ACE));
        cards.put(EAST,  Card.of(SPADES, SEVEN));
        cards.put(SOUTH,  Card.of(SPADES, EIGHT));

        baseTrick = new Trick(HEARTS, NORTH,
                cards,
                null,
                false);
    }

    @Test
    public void rotateForSouth() {
        // no real rotation since current player was already SOUTH
        Trick rotatedTrick = baseTrick.rotateForSeat(SOUTH);

        assertThat(rotatedTrick.getCardsPlayed().get(NORTH), is(baseTrick.getCardsPlayed().get(NORTH)));
        assertThat(rotatedTrick.getCardsPlayed().get(EAST), is(baseTrick.getCardsPlayed().get(EAST)));
        assertThat(rotatedTrick.getCardsPlayed().get(SOUTH), is(baseTrick.getCardsPlayed().get(SOUTH)));
        assertThat(rotatedTrick.getCardsPlayed().get(WEST), is(baseTrick.getCardsPlayed().get(WEST)));
        assertNull(rotatedTrick.getStartingPlayer());
    }

    @Test
    public void rotateForNorth() {
        Trick rotatedTrick = baseTrick.rotateForSeat(NORTH);

        assertThat(rotatedTrick.getCardsPlayed().get(NORTH), is(baseTrick.getCardsPlayed().get(SOUTH)));
        assertThat(rotatedTrick.getCardsPlayed().get(EAST), is(baseTrick.getCardsPlayed().get(WEST)));
        assertThat(rotatedTrick.getCardsPlayed().get(SOUTH), is(baseTrick.getCardsPlayed().get(NORTH)));
        assertThat(rotatedTrick.getCardsPlayed().get(WEST), is(baseTrick.getCardsPlayed().get(EAST)));
        assertNull(rotatedTrick.getStartingPlayer());
    }

    @Test
    public void rotateForEast() {
        Trick rotatedTrick = baseTrick.rotateForSeat(EAST);

        assertThat(rotatedTrick.getCardsPlayed().get(NORTH), is(baseTrick.getCardsPlayed().get(WEST)));
        assertThat(rotatedTrick.getCardsPlayed().get(EAST), is(baseTrick.getCardsPlayed().get(NORTH)));
        assertThat(rotatedTrick.getCardsPlayed().get(SOUTH), is(baseTrick.getCardsPlayed().get(EAST)));
        assertThat(rotatedTrick.getCardsPlayed().get(WEST), is(baseTrick.getCardsPlayed().get(SOUTH)));
        assertNull(rotatedTrick.getStartingPlayer());
    }

    @Test
    public void rotateForWest() {
        Trick rotatedTrick = baseTrick.rotateForSeat(WEST);

        assertThat(rotatedTrick.getCardsPlayed().get(NORTH), is(baseTrick.getCardsPlayed().get(EAST)));
        assertThat(rotatedTrick.getCardsPlayed().get(EAST), is(baseTrick.getCardsPlayed().get(SOUTH)));
        assertThat(rotatedTrick.getCardsPlayed().get(SOUTH), is(baseTrick.getCardsPlayed().get(WEST)));
        assertThat(rotatedTrick.getCardsPlayed().get(WEST), is(baseTrick.getCardsPlayed().get(NORTH)));
    }

    @Test
    public void highestCardInTrickIsTrump() {
        Trick trick = new TestTrickBuilder()
                .withTrump(CLUBS)
                .withStartingPlayer(NORTH)
                .withCardPlayed(NORTH, Card.of(HEARTS, ACE))
                .withCardPlayed(EAST, Card.of(HEARTS, SEVEN))
                .withCardPlayed(SOUTH, Card.of(SPADES, SEVEN)) //maatslag? :)
                .withCardPlayed(WEST, Card.of(CLUBS, SEVEN))
                .build();

        assertThat(trick.determineHighestCard(), is(Card.of(CLUBS, SEVEN)));
    }

    @Test
    public void checkTheTen() {
        Trick trick = new TestTrickBuilder()
                .withTrump(CLUBS)
                .withStartingPlayer(NORTH)
                .withCardPlayed(NORTH, Card.of(HEARTS, TEN))
                .withCardPlayed(EAST, Card.of(HEARTS, KING))
                .withCardPlayed(SOUTH, Card.of(SPADES, SEVEN)) //maatslag? :)
                .withCardPlayed(WEST, Card.of(HEARTS, QUEEN))
                .build();

        assertThat(trick.determineHighestCard(), is(Card.of(HEARTS, TEN)));
    }

    @Test
    public void openingCardWinsIfNoOneFollowsSuit() {
        Trick trick = new TestTrickBuilder()
                .withTrump(CLUBS)
                .withStartingPlayer(NORTH)
                .withCardPlayed(NORTH, Card.of(HEARTS, SEVEN))
                .withCardPlayed(EAST, Card.of(DIAMONDS, KING))
                .withCardPlayed(SOUTH, Card.of(SPADES, SEVEN)) //maatslag? :)
                .withCardPlayed(WEST, Card.of(SPADES, QUEEN))
                .build();

        assertThat(trick.determineHighestCard(), is(Card.of(HEARTS, SEVEN)));
    }

    @Test
    public void threeCardComboFromLowestCard() {
        Trick trick = new TestTrickBuilder()
                .withTrump(CLUBS)
                .withStartingPlayer(NORTH)
                .withCardPlayed(NORTH, Card.of(HEARTS, NINE))
                .withCardPlayed(EAST, Card.of(HEARTS, TEN))
                .withCardPlayed(SOUTH, Card.of(HEARTS, JACK))
                .withCardPlayed(WEST, Card.of(HEARTS, ACE))
                .build();

        assertThat(trick.nrOfComboPoints(), is(20));
    }

    @Test
    public void threeCardComboNotFromLowestCard() {
        Trick trick = new TestTrickBuilder()
                .withTrump(CLUBS)
                .withStartingPlayer(NORTH)
                .withCardPlayed(NORTH, Card.of(HEARTS, SEVEN))
                .withCardPlayed(EAST, Card.of(HEARTS, TEN))
                .withCardPlayed(SOUTH, Card.of(HEARTS, JACK))
                .withCardPlayed(WEST, Card.of(HEARTS, NINE))
                .build();

        assertThat(trick.nrOfComboPoints(), is(20));
    }

    @Test
    public void fourCardCombo() {
        Trick trick = new TestTrickBuilder()
                .withTrump(CLUBS)
                .withStartingPlayer(NORTH)
                .withCardPlayed(NORTH, Card.of(HEARTS, EIGHT))
                .withCardPlayed(EAST, Card.of(HEARTS, TEN))
                .withCardPlayed(SOUTH, Card.of(HEARTS, JACK))
                .withCardPlayed(WEST, Card.of(HEARTS, NINE))
                .build();

        assertThat(trick.nrOfComboPoints(), is(50));
    }

    @Test
    public void noCombo() {
        Trick trick = new TestTrickBuilder()
                .withTrump(CLUBS)
                .withStartingPlayer(NORTH)
                .withCardPlayed(NORTH, Card.of(HEARTS, EIGHT))
                .withCardPlayed(EAST, Card.of(HEARTS, TEN))
                .withCardPlayed(SOUTH, Card.of(SPADES, JACK))
                .withCardPlayed(WEST, Card.of(SPADES, NINE))
                .build();

        assertThat(trick.nrOfComboPoints(), is(0));
    }

    @Test
    public void allCardsSameRankCombo() {
        Trick trick = new TestTrickBuilder()
                .withTrump(CLUBS)
                .withStartingPlayer(NORTH)
                .withCardPlayed(NORTH, Card.of(HEARTS, TEN))
                .withCardPlayed(EAST, Card.of(DIAMONDS, TEN))
                .withCardPlayed(SOUTH, Card.of(SPADES, TEN))
                .withCardPlayed(WEST, Card.of(CLUBS, TEN))
                .build();

        assertThat(trick.nrOfComboPoints(), is(100));
    }

    @Test
    public void allCardsJacks() {
        Trick trick = new TestTrickBuilder()
                .withTrump(CLUBS)
                .withStartingPlayer(NORTH)
                .withCardPlayed(NORTH, Card.of(HEARTS, JACK))
                .withCardPlayed(EAST, Card.of(DIAMONDS, JACK))
                .withCardPlayed(SOUTH, Card.of(SPADES, JACK))
                .withCardPlayed(WEST, Card.of(CLUBS, JACK))
                .build();

        assertThat(trick.nrOfComboPoints(), is(200));
    }

    @Test
    public void honorsCombo() {
        Trick trick = new TestTrickBuilder()
                .withTrump(CLUBS)
                .withStartingPlayer(NORTH)
                .withCardPlayed(NORTH, Card.of(CLUBS, KING))
                .withCardPlayed(EAST, Card.of(DIAMONDS, SEVEN))
                .withCardPlayed(SOUTH, Card.of(SPADES, SEVEN))
                .withCardPlayed(WEST, Card.of(CLUBS, QUEEN))
                .build();

        assertThat(trick.nrOfComboPoints(), is(20));
    }

    @Test
    public void honorsAndThreeCardCombo() {
        Trick trick = new TestTrickBuilder()
                .withTrump(CLUBS)
                .withStartingPlayer(NORTH)
                .withCardPlayed(NORTH, Card.of(CLUBS, KING))
                .withCardPlayed(EAST, Card.of(CLUBS, JACK))
                .withCardPlayed(SOUTH, Card.of(SPADES, SEVEN))
                .withCardPlayed(WEST, Card.of(CLUBS, QUEEN))
                .build();

        assertThat(trick.nrOfComboPoints(), is(40));
    }

    @Test
    public void honorsAndFourCardCombo() {
        Trick trick = new TestTrickBuilder()
                .withTrump(CLUBS)
                .withStartingPlayer(NORTH)
                .withCardPlayed(NORTH, Card.of(CLUBS, KING))
                .withCardPlayed(EAST, Card.of(CLUBS, JACK))
                .withCardPlayed(SOUTH, Card.of(CLUBS, ACE))
                .withCardPlayed(WEST, Card.of(CLUBS, QUEEN))
                .build();

        assertThat(trick.nrOfComboPoints(), is(70));
    }

}
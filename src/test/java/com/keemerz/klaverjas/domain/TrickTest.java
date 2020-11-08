package com.keemerz.klaverjas.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Map;

import static com.keemerz.klaverjas.domain.Rank.*;
import static com.keemerz.klaverjas.domain.Seat.*;
import static com.keemerz.klaverjas.domain.Suit.*;
import static org.junit.jupiter.api.Assertions.assertNull;

class TrickTest {

    Trick baseTrick;

    @BeforeEach
    void setUp() {
        baseTrick = new Trick(HEARTS, NORTH,
                Map.of(
                NORTH, Card.of(SPADES, ACE),
                EAST, Card.of(SPADES, SEVEN),
                SOUTH, Card.of(CLUBS, EIGHT)));
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

}
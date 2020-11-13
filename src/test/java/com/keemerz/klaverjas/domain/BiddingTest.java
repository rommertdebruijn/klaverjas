package com.keemerz.klaverjas.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import static com.keemerz.klaverjas.domain.Bid.*;
import static com.keemerz.klaverjas.domain.Seat.*;
import static com.keemerz.klaverjas.domain.Suit.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

class BiddingTest {

    private Bidding baseBidding;

    @BeforeEach
    void setUp() {
        baseBidding = new Bidding(HEARTS,
                Map.of(
                        NORTH, PASS,
                        EAST, PASS,
                        SOUTH, PLAY));
        baseBidding.setAvailableSuits(Arrays.asList(CLUBS, DIAMONDS, SPADES));
        baseBidding.setFinalTrump(SPADES);
        baseBidding.setFinalBidBy(EAST);
    }

    @Test
    public void rotateForSouth() {
        // no real rotation since current player was already SOUTH
        Bidding rotatedBidding = baseBidding.rotateForSeat(SOUTH);

        assertThat(rotatedBidding.getProposedTrump(), is(HEARTS));
        assertThat(rotatedBidding.getAvailableSuits(), is(baseBidding.getAvailableSuits()));
        assertThat(rotatedBidding.getFinalTrump(), is(SPADES));
        assertThat(rotatedBidding.getFinalBidBy(), is(EAST));
        assertThat(rotatedBidding.getBids().get(NORTH), is(baseBidding.getBids().get(NORTH)));
        assertThat(rotatedBidding.getBids().get(EAST), is(baseBidding.getBids().get(EAST)));
        assertThat(rotatedBidding.getBids().get(SOUTH), is(baseBidding.getBids().get(SOUTH)));
        assertThat(rotatedBidding.getBids().get(WEST), is(baseBidding.getBids().get(WEST)));
    }

    @Test
    public void rotateForNorth() {
        Bidding rotatedBidding = baseBidding.rotateForSeat(NORTH);

        assertThat(rotatedBidding.getProposedTrump(), is(HEARTS));
        assertThat(rotatedBidding.getAvailableSuits(), is(baseBidding.getAvailableSuits()));
        assertThat(rotatedBidding.getFinalTrump(), is(SPADES));
        assertThat(rotatedBidding.getFinalBidBy(), is(WEST));
        assertThat(rotatedBidding.getBids().get(NORTH), is(baseBidding.getBids().get(SOUTH)));
        assertThat(rotatedBidding.getBids().get(EAST), is(baseBidding.getBids().get(WEST)));
        assertThat(rotatedBidding.getBids().get(SOUTH), is(baseBidding.getBids().get(NORTH)));
        assertThat(rotatedBidding.getBids().get(WEST), is(baseBidding.getBids().get(EAST)));
    }

    @Test
    public void rotateForEast() {
        Bidding rotatedBidding = baseBidding.rotateForSeat(EAST);

        assertThat(rotatedBidding.getProposedTrump(), is(HEARTS));
        assertThat(rotatedBidding.getAvailableSuits(), is(baseBidding.getAvailableSuits()));
        assertThat(rotatedBidding.getFinalTrump(), is(SPADES));
        assertThat(rotatedBidding.getFinalBidBy(), is(SOUTH));
        assertThat(rotatedBidding.getBids().get(NORTH), is(baseBidding.getBids().get(WEST)));
        assertThat(rotatedBidding.getBids().get(EAST), is(baseBidding.getBids().get(NORTH)));
        assertThat(rotatedBidding.getBids().get(SOUTH), is(baseBidding.getBids().get(EAST)));
        assertThat(rotatedBidding.getBids().get(WEST), is(baseBidding.getBids().get(SOUTH)));
    }

    @Test
    public void rotateForWest() {
        Bidding rotatedBidding = baseBidding.rotateForSeat(WEST);

        assertThat(rotatedBidding.getProposedTrump(), is(HEARTS));
        assertThat(rotatedBidding.getAvailableSuits(), is(baseBidding.getAvailableSuits()));
        assertThat(rotatedBidding.getFinalTrump(), is(SPADES));
        assertThat(rotatedBidding.getFinalBidBy(), is(NORTH));
        assertThat(rotatedBidding.getBids().get(NORTH), is(baseBidding.getBids().get(EAST)));
        assertThat(rotatedBidding.getBids().get(EAST), is(baseBidding.getBids().get(SOUTH)));
        assertThat(rotatedBidding.getBids().get(SOUTH), is(baseBidding.getBids().get(WEST)));
        assertThat(rotatedBidding.getBids().get(WEST), is(baseBidding.getBids().get(NORTH)));
    }

}
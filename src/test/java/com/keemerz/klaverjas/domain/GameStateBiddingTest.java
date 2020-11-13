package com.keemerz.klaverjas.domain;

import org.junit.jupiter.api.Test;

import static com.keemerz.klaverjas.domain.Bid.PASS;
import static com.keemerz.klaverjas.domain.Bid.PLAY;
import static com.keemerz.klaverjas.domain.Seat.*;
import static com.keemerz.klaverjas.domain.Seat.NORTH;
import static com.keemerz.klaverjas.domain.Suit.*;
import static com.keemerz.klaverjas.domain.Suit.SPADES;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GameStateBiddingTest {

    @Test
    public void BidShouldPassTurn() {
        GameState gameState = new TestGameStateBuilder()
                .withBidding(new TestBiddingBuilder()
                        .withBid(NORTH, PASS)
                        .withBid(EAST, PASS)
                        .build())
                .withTurn(SOUTH)
                .build();

        gameState.makeBid(PASS);

        assertThat(gameState.getTurn(), is(WEST));
        assertThat(gameState.getBidding().getBids().size(), is(3));
    }

    @Test
    public void lastPassShouldFillAvailableSuits() {
        GameState gameState = new TestGameStateBuilder()
                .withBidding(new TestBiddingBuilder()
                        .withBid(NORTH, PASS)
                        .withBid(EAST, PASS)
                        .withBid(SOUTH, PASS)
                        .build())
                .withTurn(WEST)
                .build();

        assertTrue(gameState.getBidding().getAvailableSuits().isEmpty());

        gameState.makeBid(PASS);

        assertThat(gameState.getTurn(), is(NORTH));
        assertThat(gameState.getBidding().getAvailableSuits().size(), is(3));
        assertFalse(gameState.getBidding().getAvailableSuits().contains(CLUBS));
    }

    @Test
    public void playBidShouldFillFinalTrumpAndSetTurnToDealerLeftHandPlayer() {
        GameState gameState = new TestGameStateBuilder()
                .withBidding(new TestBiddingBuilder()
                        .withProposedTrump(DIAMONDS)
                        .withBid(NORTH, PASS)
                        .withBid(EAST, PASS)
                        .build())
                .withTurn(SOUTH)
                .build();

        assertThat(gameState.getBidding().getProposedTrump(), is(DIAMONDS));
        assertNull(gameState.getBidding().getFinalTrump());

        gameState.makeBid(PLAY);

        assertThat(gameState.getTurn(), is(NORTH));
        assertThat(gameState.getBidding().getFinalTrump(), is(DIAMONDS));
        assertThat(gameState.getBidding().getFinalBidBy(), is(SOUTH));
        assertTrue(gameState.getBidding().getAvailableSuits().isEmpty());
    }

    @Test
    public void forcedBidShouldFillFinalTrumpAndSetTurnToDealerLeftHandPlayer() {
        GameState gameState = new TestGameStateBuilder()
                .withBidding(new TestBiddingBuilder()
                        .withProposedTrump(DIAMONDS)
                        .withBid(NORTH, PASS)
                        .withBid(EAST, PASS)
                        .withBid(SOUTH, PASS)
                        .withBid(WEST, PASS)
                        .withAvailableSuits(SPADES, HEARTS, CLUBS)
                        .build())
                .withTurn(NORTH)
                .build();

        assertNull(gameState.getBidding().getFinalTrump());

        gameState.makeForcedBid(SPADES);

        assertThat(gameState.getTurn(), is(NORTH));
        assertThat(gameState.getBidding().getFinalTrump(), is(SPADES));
        assertThat(gameState.getBidding().getFinalBidBy(), is(NORTH));
        assertTrue(gameState.getBidding().getAvailableSuits().isEmpty());
    }
}

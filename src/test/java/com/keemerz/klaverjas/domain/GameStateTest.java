package com.keemerz.klaverjas.domain;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static com.keemerz.klaverjas.domain.Bid.PASS;
import static com.keemerz.klaverjas.domain.Bid.PLAY;
import static com.keemerz.klaverjas.domain.Seat.*;
import static com.keemerz.klaverjas.domain.Suit.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.*;

class GameStateTest {

    @Test
    public void shuffleShouldDeal8CardsToEachSeat() {
        GameState gameState = GameState.createNewGame();
        gameState.dealHands();

        for (Seat seat : Seat.values()) {
            assertThat(gameState.getHands().get(seat).size(), is(8));
        }
    }

    @Test
    public void fillSeatShouldStartNorth() {
        GameState gameState = GameState.createNewGame();
        String testId = "testPlayerId1";

        gameState.fillSeat(new TestPlayerBuilder().withPlayerId(testId).build());

        assertThat(gameState.determinePlayerIds().size(), is(1));
        assertThat(gameState.determinePlayerIds().get(0), is(testId));

        assertThat(gameState.getPlayers().size(), is(1));
        assertThat(gameState.getPlayers().get(NORTH).getPlayerId(), is(testId));
    }

    @Test
    public void fillSeatShouldContinueClockwise() {
        GameState gameState = GameState.createNewGame();
        String testId1 = "testPlayerId1";
        String testId2 = "testPlayerId2";

        gameState.fillSeat(new TestPlayerBuilder().withPlayerId(testId1).build()); // North
        gameState.fillSeat(new TestPlayerBuilder().withPlayerId(testId2).build()); // East

        assertThat(gameState.determinePlayerIds().size(), is(2));
        assertTrue(gameState.determinePlayerIds().contains(testId1));
        assertTrue(gameState.determinePlayerIds().contains(testId2));

        assertThat(gameState.getPlayers().size(), is(2));
        assertThat(gameState.getPlayers().get(NORTH).getPlayerId(), is(testId1));
        assertThat(gameState.getPlayers().get(EAST).getPlayerId(), is(testId2));
    }

    @Test
    public void fillSeatShouldNoOpWhenAllSeatsArePopulated() {
        GameState gameState = GameState.createNewGame();
        gameState.fillSeat(new TestPlayerBuilder().build());
        gameState.fillSeat(new TestPlayerBuilder().build());
        gameState.fillSeat(new TestPlayerBuilder().build());
        gameState.fillSeat(new TestPlayerBuilder().build());
        assertThat(gameState.getPlayers().size(), is(4));

        // adding a 5th player
        String fifthPlayerId = "fifthPlayerId";
        gameState.fillSeat(new TestPlayerBuilder().withPlayerId(fifthPlayerId).build());

        // 5th player should not be in the list of players
        assertTrue(gameState.getPlayers().values().stream()
                .noneMatch(player -> player.getPlayerId().equals(fifthPlayerId)));
    }

}
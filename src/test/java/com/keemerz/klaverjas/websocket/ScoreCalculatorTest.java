package com.keemerz.klaverjas.websocket;

import com.keemerz.klaverjas.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.keemerz.klaverjas.domain.Rank.*;
import static com.keemerz.klaverjas.domain.Rank.EIGHT;
import static com.keemerz.klaverjas.domain.Seat.*;
import static com.keemerz.klaverjas.domain.Suit.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ScoreCalculatorTest {

    private Bidding bidding;

    @BeforeEach
    void setUp() {
        bidding = new TestBiddingBuilder()
                .withFinalTrump(CLUBS)
                .withFinalBidBy(NORTH)
                .build();
    }

    @Test
    void gameWithoutCombos() {

        Trick trickForNS = new TestTrickBuilder()
                .withTrump(CLUBS)
                .withCardPlayed(NORTH, Card.of(CLUBS, JACK))
                .withCardPlayed(EAST, Card.of(CLUBS, TEN))
                .withCardPlayed(SOUTH, Card.of(CLUBS, NINE))
                .withCardPlayed(WEST, Card.of(CLUBS, EIGHT))
                .withTrickWinner(NORTH)
                .build();

        Trick trickForEW = new TestTrickBuilder()
                .withTrump(CLUBS)
                .withCardPlayed(NORTH, Card.of(HEARTS, TEN))
                .withCardPlayed(EAST, Card.of(HEARTS, ACE))
                .withCardPlayed(SOUTH, Card.of(HEARTS, NINE))
                .withCardPlayed(WEST, Card.of(HEARTS, EIGHT))
                .withTrickWinner(EAST)
                .build();
        List<Trick> tricks = Arrays.asList(trickForNS, trickForEW);

        ComboPoints comboPoints = new ComboPoints(0, 0);

        assertThat(ScoreCalculator.calculateGameScore(bidding, tricks, comboPoints), is(new Score(44, 21, "", "")));
    }

    @Test
    void gameWithComboNS() {

        Trick trickForNS = new TestTrickBuilder()
                .withTrump(CLUBS)
                .withCardPlayed(NORTH, Card.of(CLUBS, JACK))
                .withCardPlayed(EAST, Card.of(CLUBS, TEN))
                .withCardPlayed(SOUTH, Card.of(CLUBS, NINE))
                .withCardPlayed(WEST, Card.of(CLUBS, EIGHT))
                .withTrickWinner(NORTH)
                .build();

        Trick trickForEW = new TestTrickBuilder()
                .withTrump(CLUBS)
                .withCardPlayed(NORTH, Card.of(HEARTS, TEN))
                .withCardPlayed(EAST, Card.of(HEARTS, ACE))
                .withCardPlayed(SOUTH, Card.of(HEARTS, NINE))
                .withCardPlayed(WEST, Card.of(HEARTS, EIGHT))
                .withTrickWinner(EAST)
                .build();
        List<Trick> tricks = Arrays.asList(trickForNS, trickForEW);

        ComboPoints comboPoints = new ComboPoints(20, 0);

        assertThat(ScoreCalculator.calculateGameScore(bidding, tricks, comboPoints), is(new Score(64, 21, "", "")));
    }

    @Test
    void regularDownForNS() {

        Trick trickForNS = new TestTrickBuilder()
                .withTrump(CLUBS)
                .withCardPlayed(NORTH, Card.of(CLUBS, QUEEN))
                .withCardPlayed(EAST, Card.of(CLUBS, SEVEN))
                .withCardPlayed(SOUTH, Card.of(CLUBS, NINE))
                .withCardPlayed(WEST, Card.of(CLUBS, EIGHT))
                .withTrickWinner(SOUTH)
                .build();

        Trick trickForEW = new TestTrickBuilder()
                .withTrump(CLUBS)
                .withCardPlayed(NORTH, Card.of(HEARTS, TEN))
                .withCardPlayed(EAST, Card.of(HEARTS, ACE))
                .withCardPlayed(SOUTH, Card.of(HEARTS, NINE))
                .withCardPlayed(WEST, Card.of(HEARTS, EIGHT))
                .withTrickWinner(EAST)
                .build();
        List<Trick> tricks = Arrays.asList(trickForNS, trickForEW);

        ComboPoints comboPoints = new ComboPoints(0, 0);

        // NB: We take a shortcut here. We didn't use full 8 tricks to determine winner, so in these tricks there are not 162 points.
        // But since NS is down, EW gets all the points in the game. Normally that would be 162.
        assertThat(ScoreCalculator.calculateGameScore(bidding, tricks, comboPoints), is(new Score(0, 162, "NAT", "")));
    }

    @Test
    void gameWithComboEWCausesNSToGoDown() {

        Trick trickForNS = new TestTrickBuilder()
                .withTrump(CLUBS)
                .withCardPlayed(NORTH, Card.of(CLUBS, JACK))
                .withCardPlayed(EAST, Card.of(CLUBS, SEVEN))
                .withCardPlayed(SOUTH, Card.of(CLUBS, NINE))
                .withCardPlayed(WEST, Card.of(CLUBS, EIGHT))
                .withTrickWinner(NORTH)
                .build();

        Trick trickForEW = new TestTrickBuilder()
                .withTrump(CLUBS)
                .withCardPlayed(NORTH, Card.of(HEARTS, TEN))
                .withCardPlayed(EAST, Card.of(HEARTS, ACE))
                .withCardPlayed(SOUTH, Card.of(HEARTS, NINE))
                .withCardPlayed(WEST, Card.of(HEARTS, EIGHT))
                .withTrickWinner(EAST)
                .build();
        List<Trick> tricks = Arrays.asList(trickForNS, trickForEW);

        ComboPoints comboPoints = new ComboPoints(0, 20);

        // NB: We take a shortcut here. We didn't use full 8 tricks to determine winner, so in these tricks there are not 162 points.
        // But since NS is down, EW gets all the points in the game. Normally that would be 162. Plus 20 comboPoints = 182
        assertThat(ScoreCalculator.calculateGameScore(bidding, tricks, comboPoints), is(new Score(0, 182, "NAT", "")));
    }

    @Test
    void regularSweep() {

        Trick trickForNS = new TestTrickBuilder()
                .withTrump(CLUBS)
                .withCardPlayed(NORTH, Card.of(CLUBS, JACK))
                .withCardPlayed(EAST, Card.of(CLUBS, SEVEN))
                .withCardPlayed(SOUTH, Card.of(CLUBS, NINE))
                .withCardPlayed(WEST, Card.of(CLUBS, EIGHT))
                .withTrickWinner(SOUTH)
                .build();

        Trick trickForEW = new TestTrickBuilder()
                .withTrump(CLUBS)
                .withCardPlayed(NORTH, Card.of(HEARTS, ACE))
                .withCardPlayed(EAST, Card.of(HEARTS, TEN))
                .withCardPlayed(SOUTH, Card.of(HEARTS, NINE))
                .withCardPlayed(WEST, Card.of(HEARTS, EIGHT))
                .withTrickWinner(NORTH)
                .build();
        List<Trick> tricks = Arrays.asList(trickForNS, trickForEW);

        ComboPoints comboPoints = new ComboPoints(0, 0);

        // NB: We take a shortcut here. We didn't use full 8 tricks to determine winner, so in these tricks there are not 162 points.
        // Since NS has all the tricks, they receive an additional 100 bonus points
        assertThat(ScoreCalculator.calculateGameScore(bidding, tricks, comboPoints), is(new Score(155, 0, "", "PIT")));
    }

    @Test
    void sweepWithAdditionalComboPoints() {

        Trick trickForNS = new TestTrickBuilder()
                .withTrump(CLUBS)
                .withCardPlayed(NORTH, Card.of(CLUBS, JACK))
                .withCardPlayed(EAST, Card.of(CLUBS, SEVEN))
                .withCardPlayed(SOUTH, Card.of(CLUBS, NINE))
                .withCardPlayed(WEST, Card.of(CLUBS, EIGHT))
                .withTrickWinner(SOUTH)
                .build();

        Trick trickForEW = new TestTrickBuilder()
                .withTrump(CLUBS)
                .withCardPlayed(NORTH, Card.of(HEARTS, ACE))
                .withCardPlayed(EAST, Card.of(HEARTS, TEN))
                .withCardPlayed(SOUTH, Card.of(HEARTS, NINE))
                .withCardPlayed(WEST, Card.of(HEARTS, EIGHT))
                .withTrickWinner(NORTH)
                .build();
        List<Trick> tricks = Arrays.asList(trickForNS, trickForEW);

        ComboPoints comboPoints = new ComboPoints(40, 0);

        // NB: We take a shortcut here. We didn't use full 8 tricks to determine winner, so in these tricks there are not 162 points.
        // Since NS has all the tricks, they receive an additional 100 bonus points
        assertThat(ScoreCalculator.calculateGameScore(bidding, tricks, comboPoints), is(new Score(195, 0, "", "PIT")));
    }

    @Test
    public void multipleGameScores() {
        List<Score> scores = new ArrayList<>();
        scores.add(new Score(171, 31, "", ""));
        scores.add(new Score(82, 80, "", ""));
        scores.add(new Score(0, 162, "NAT", ""));
        scores.add(new Score(56, 126, "", ""));

        assertThat(ScoreCalculator.calculateMatchScore(scores), is(new Score(309, 399, "", "WINNAAR")));
    }

    @Test
    public void totalScoreWithNoGamesPlayed() {
        assertThat(ScoreCalculator.calculateMatchScore(new ArrayList<>()), is(new Score(0, 0, "", "")));
    }


}
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

class GameScoreCalculatorTest {

    private Bidding bidding;

    @BeforeEach
    void setUp() {
        bidding = new TestBiddingBuilder()
                .withFinalTrump(HEARTS)
                .withFinalBidBy(EAST)
                .build();
    }

    @Test
    void gameWithoutCombos() {
        Trick lastTrick = new TestTrickBuilder()
                .withTrump(HEARTS)
                .withCardPlayed(WEST, Card.of(HEARTS, SEVEN))
                .withCardPlayed(NORTH, Card.of(HEARTS, EIGHT))
                .withCardPlayed(EAST, Card.of(HEARTS, QUEEN))
                .withCardPlayed(SOUTH, Card.of(HEARTS, KING))
                .withStartingPlayer(WEST)
                .withTrickWinner(SOUTH)
                .build();
        List<Trick> tricks = buildSevenTricksPlusLastTrick(lastTrick);

        ComboPoints comboPoints = new ComboPoints(0, 0);

        assertThat(ScoreCalculator.calculateGameScore(bidding, tricks, comboPoints), is(new GameScore(17, 145, 17, 145, 0, 0, "", "")));
    }

    @Test
    void gameWithComboNS() {
        Trick lastTrick = new TestTrickBuilder()
                .withTrump(HEARTS)
                .withCardPlayed(WEST, Card.of(HEARTS, SEVEN))
                .withCardPlayed(NORTH, Card.of(HEARTS, EIGHT))
                .withCardPlayed(EAST, Card.of(HEARTS, QUEEN))
                .withCardPlayed(SOUTH, Card.of(HEARTS, KING))
                .withStartingPlayer(WEST)
                .withTrickWinner(SOUTH)
                .build();
        List<Trick> tricks = buildSevenTricksPlusLastTrick(lastTrick);

        ComboPoints comboPoints = new ComboPoints(20, 0);

        assertThat(ScoreCalculator.calculateGameScore(bidding, tricks, comboPoints), is(new GameScore(37, 145, 17, 145, 20, 0, "", "")));
    }

    @Test
    void regularDownForNS() {
        bidding.setFinalBidBy(SOUTH);

        Trick lastTrick = new TestTrickBuilder()
                .withTrump(HEARTS)
                .withCardPlayed(WEST, Card.of(HEARTS, SEVEN))
                .withCardPlayed(NORTH, Card.of(HEARTS, EIGHT))
                .withCardPlayed(EAST, Card.of(HEARTS, QUEEN))
                .withCardPlayed(SOUTH, Card.of(HEARTS, KING))
                .withStartingPlayer(WEST)
                .withTrickWinner(SOUTH)
                .build();
        List<Trick> tricks = buildSevenTricksPlusLastTrick(lastTrick);

        ComboPoints comboPoints = new ComboPoints(0, 0);

        // Since NS is down, EW gets all the points in the game. Normally that would be 162. Table scores come from the 7 generated tricks mostly
        assertThat(ScoreCalculator.calculateGameScore(bidding, tricks, comboPoints), is(new GameScore(0, 162, 17, 145, 0, 0, "NAT", "")));
    }

    @Test
    void gameWithComboNSCausesEWToGoDown() {

        Trick lastTrick = new TestTrickBuilder()
                .withTrump(HEARTS)
                .withCardPlayed(WEST, Card.of(HEARTS, SEVEN))
                .withCardPlayed(NORTH, Card.of(HEARTS, EIGHT))
                .withCardPlayed(EAST, Card.of(HEARTS, QUEEN))
                .withCardPlayed(SOUTH, Card.of(HEARTS, KING))
                .withStartingPlayer(WEST)
                .withTrickWinner(SOUTH)
                .build();
        List<Trick> tricks = buildSevenTricksPlusLastTrick(lastTrick);

        ComboPoints comboPoints = new ComboPoints(160, 0); // because 160 roem is something you see every day!

        // But since EW is down, NS gets all the points in the game. Thats 162 plus a lucious 160 comboPoints = 322
        assertThat(ScoreCalculator.calculateGameScore(bidding, tricks, comboPoints), is(new GameScore(322, 0, 17, 145, 160, 0, "", "NAT")));
    }

    @Test
    void regularSweep() {

        Trick lastTrick = new TestTrickBuilder()
                .withTrump(HEARTS)
                .withCardPlayed(WEST, Card.of(HEARTS, SEVEN))
                .withCardPlayed(NORTH, Card.of(HEARTS, EIGHT))
                .withCardPlayed(EAST, Card.of(HEARTS, KING))
                .withCardPlayed(SOUTH, Card.of(HEARTS, QUEEN))
                .withStartingPlayer(WEST)
                .withTrickWinner(EAST)
                .build();
        List<Trick> tricks = buildSevenTricksPlusLastTrick(lastTrick);

        ComboPoints comboPoints = new ComboPoints(0, 0);

        // Since EW has all the tricks, they receive an additional 100 bonus points
        assertThat(ScoreCalculator.calculateGameScore(bidding, tricks, comboPoints), is(new GameScore(0, 262, 0, 162, 0, 0, "PIT", "")));
    }

    @Test
    void sweepWithAdditionalComboPoints() {
        Trick lastTrick = new TestTrickBuilder()
                .withTrump(HEARTS)
                .withCardPlayed(WEST, Card.of(HEARTS, SEVEN))
                .withCardPlayed(NORTH, Card.of(HEARTS, EIGHT))
                .withCardPlayed(EAST, Card.of(HEARTS, KING))
                .withCardPlayed(SOUTH, Card.of(HEARTS, QUEEN))
                .withStartingPlayer(WEST)
                .withTrickWinner(EAST)
                .build();
        List<Trick> tricks = buildSevenTricksPlusLastTrick(lastTrick);

        ComboPoints comboPoints = new ComboPoints(0, 40);

        // NB: We take a shortcut here. We didn't use full 8 tricks to determine winner, so in these tricks there are not 162 points.
        // Since NS has all the tricks, they receive an additional 100 bonus points
        assertThat(ScoreCalculator.calculateGameScore(bidding, tricks, comboPoints), is(new GameScore(0, 302, 0, 162, 0, 40, "PIT", "")));
    }

    @Test
    public void multipleGameScores() {
        List<GameScore> gameScores = new ArrayList<>();
        gameScores.add(new GameScore(171, 31, 171, 31, 0, 0, "", ""));
        gameScores.add(new GameScore(82, 80, 82, 80, 0, 0, "", ""));
        gameScores.add(new GameScore(0, 162, 80, 82, 0, 0, "NAT", ""));
        gameScores.add(new GameScore(56, 126, 56, 126, 0, 0, "", ""));

        assertThat(ScoreCalculator.calculateMatchScore(gameScores), is(new MatchScore(309, 399)));
    }

    @Test
    public void totalScoreWithNoGamesPlayed() {
        assertThat(ScoreCalculator.calculateMatchScore(new ArrayList<>()), is(new MatchScore(0, 0)));
    }

    private List<Trick> buildSevenTricksPlusLastTrick(Trick lastTrick) {
        List<Trick> previousTricks = new ArrayList<>();

        // non trump cards all go to East
        for (Suit suit : Arrays.asList(CLUBS, DIAMONDS, SPADES)) {
            previousTricks.add(new TestTrickBuilder()
                    .withStartingPlayer(WEST)
                    .withTrump(HEARTS)
                    .withCardPlayed(WEST, Card.of(suit, ACE))
                    .withCardPlayed(NORTH, Card.of(suit, QUEEN))
                    .withCardPlayed(EAST, Card.of(suit, KING))
                    .withCardPlayed(SOUTH, Card.of(suit, TEN))
                    .withTrickWinner(WEST)
                    .build());
            previousTricks.add(new TestTrickBuilder()
                    .withStartingPlayer(WEST)
                    .withTrump(HEARTS)
                    .withCardPlayed(WEST, Card.of(suit, JACK))
                    .withCardPlayed(NORTH, Card.of(suit, SEVEN))
                    .withCardPlayed(EAST, Card.of(suit, EIGHT))
                    .withCardPlayed(SOUTH, Card.of(suit, NINE))
                    .withTrickWinner(WEST)
                    .build());
        }

        previousTricks.add(new TestTrickBuilder()
                .withStartingPlayer(WEST)
                .withTrump(HEARTS)
                .withCardPlayed(WEST, Card.of(HEARTS, JACK))
                .withCardPlayed(NORTH, Card.of(HEARTS, TEN))
                .withCardPlayed(EAST, Card.of(HEARTS, ACE))
                .withCardPlayed(SOUTH, Card.of(HEARTS, NINE))
                .withTrickWinner(WEST)
                .build());

        previousTricks.add(lastTrick);

        return previousTricks;
    }

}
package com.keemerz.klaverjas.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.*;

import static com.keemerz.klaverjas.domain.GameState.MAX_IDLE_TIME_IN_SECONDS;
import static com.keemerz.klaverjas.domain.Rank.*;
import static com.keemerz.klaverjas.domain.Seat.*;
import static com.keemerz.klaverjas.domain.Suit.*;
import static com.keemerz.klaverjas.domain.Team.NS;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.*;

class GameStateTest {

    @Test
    public void oldGameShouldNotBeConsideredActive() {
        GameState gameState = new GameState("someGameId", LocalDateTime.now().minusSeconds(MAX_IDLE_TIME_IN_SECONDS).minusSeconds(1));
        assertThat(gameState.isActive(), is(false));
    }

    @Test
    public void recentGameShouldBeConsideredActive() {
        GameState gameState = new GameState("someGameId", LocalDateTime.now().minusSeconds(MAX_IDLE_TIME_IN_SECONDS).plusSeconds(1));
        assertThat(gameState.isActive(), is(true));
    }

    @Test
    public void shuffleShouldDeal8CardsToEachSeat() {
        GameState gameState = GameState.createNewGame();

        assertTrue(gameState.isDealerButtonAvailable());
        gameState.dealHands();

        for (Seat seat : Seat.values()) {
            assertThat(gameState.getHands().get(seat).size(), is(8));
        }
        assertFalse(gameState.isDealerButtonAvailable());
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

    @Test
    public void lastCardInTrickShouldStartNextTrickWithSouthToPlay() {
        GameState gameState = new TestGameStateBuilder()
                .withGameId("someGameId")
                .withDealer(NORTH)
                .withBidding(new TestBiddingBuilder()
                        .withFinalTrump(HEARTS)
                        .withFinalBidBy(EAST)
                        .build())
                .withHand(WEST, new ArrayList<>())
                .withHand(NORTH, new ArrayList<>())
                .withHand(EAST, new ArrayList<>())
                .withHand(SOUTH, Arrays.asList(
                        Card.of(HEARTS, KING),
                        Card.of(SPADES, SEVEN),
                        Card.of(SPADES, EIGHT),
                        Card.of(CLUBS, SEVEN),
                        Card.of(CLUBS, EIGHT),
                        Card.of(DIAMONDS, SEVEN),
                        Card.of(DIAMONDS, EIGHT)))
                .withCurrentTrick(new TestTrickBuilder()
                        .withTrump(HEARTS)
                        .withCardPlayed(WEST, Card.of(HEARTS, SEVEN))
                        .withCardPlayed(NORTH, Card.of(HEARTS, EIGHT))
                        .withCardPlayed(EAST, Card.of(HEARTS, QUEEN))
                        .withStartingPlayer(EAST)
                        .build())
                .build();

        gameState.processCard(SOUTH, Card.of(HEARTS, KING));

        // score should be scored, game should be fresh
        assertThat(gameState.getGameGameScores().size(), is(0));
        assertThat(gameState.getDealer(), is(NORTH));
        assertThat(gameState.getTurn(), is(SOUTH));
        assertThat(gameState.getCurrentTrick().getTrickWinner(), is(SOUTH));

        gameState.processCard(SOUTH, Card.of(SPADES, SEVEN));

        assertThat(gameState.getTurn(), is(WEST));
        assertThat(gameState.getCurrentTrick().getStartingPlayer(), is(SOUTH));
        assertThat(gameState.getCurrentTrick().getCardsPlayed().get(SOUTH), is(Card.of(SPADES, SEVEN)));
        assertNull(gameState.getCurrentTrick().getTrickWinner());
    }

    @Test
    public void lastCardInLastTrickShouldAllowComboToBeClaimed() {
       GameState gameState = new TestGameStateBuilder()
               .withGameId("someGameId")
               .withDealer(NORTH)
               .withBidding(new TestBiddingBuilder()
                    .withFinalTrump(HEARTS)
                    .withFinalBidBy(EAST)
                    .build())
               .withPreviousTricks(buildSevenPreviousTricks())
               .withHand(WEST, new ArrayList<>())
               .withHand(NORTH, new ArrayList<>())
               .withHand(EAST, new ArrayList<>())
               .withHand(SOUTH, Arrays.asList(Card.of(HEARTS, KING)))
               .withCurrentTrick(new TestTrickBuilder()
                       .withTrump(HEARTS)
                       .withCardPlayed(WEST, Card.of(HEARTS, SEVEN))
                       .withCardPlayed(NORTH, Card.of(HEARTS, EIGHT))
                       .withCardPlayed(EAST, Card.of(HEARTS, QUEEN))
                       .withStartingPlayer(EAST)
                       .build())
               .build();

       gameState.processCard(SOUTH, Card.of(HEARTS, KING));

       // score should be scored, game should be fresh
       assertThat(gameState.getGameGameScores().size(), is(0));
       assertThat(gameState.getDealer(), is(NORTH));
       assertThat(gameState.getTurn(), is(SOUTH));
       assertThat(gameState.getComboPoints(), is(new ComboPoints(0, 0)));
       assertThat(gameState.getPreviousTricks().size(), is(8));

       gameState.claimCombo();

       assertThat(gameState.getComboPoints(), is(new ComboPoints(20, 0)));
       assertThat(gameState.getTurn(), is(SOUTH));
       assertThat(gameState.isDealerButtonAvailable(), is(false));
    }

    @Test
    public void StukInLastTrickShouldGiveComboPoints() {
        GameState gameState = new TestGameStateBuilder()
                .withGameId("someGameId")
                .withDealer(NORTH)
                .withBidding(new TestBiddingBuilder()
                        .withFinalTrump(CLUBS)
                        .withFinalBidBy(SOUTH)
                        .build())
                .withPreviousTricks(buildSevenPreviousTricks())
                .withHand(WEST, new ArrayList<>())
                .withHand(NORTH, new ArrayList<>())
                .withHand(EAST, new ArrayList<>())
                .withHand(SOUTH, Arrays.asList(Card.of(CLUBS, KING)))
                .withCurrentTrick(new TestTrickBuilder()
                        .withTrump(CLUBS)
                        .withCardPlayed(WEST, Card.of(CLUBS, QUEEN))
                        .withCardPlayed(NORTH, Card.of(SPADES, TEN))
                        .withCardPlayed(EAST, Card.of(SPADES, NINE))
                        .withStartingPlayer(EAST)
                        .build())
                .build();

        gameState.processCard(SOUTH, Card.of(CLUBS, KING));

        // score should be scored, game should be fresh
        assertThat(gameState.getComboPoints(), is(new ComboPoints(0, 0)));
        assertThat(gameState.getPreviousTricks().size(), is(8));

        gameState.claimCombo();

        assertThat(gameState.getComboPoints(), is(new ComboPoints(20, 0)));
    }

    @Test
    public void calculateScoreShouldStartFreshGame() {
        List<Trick> previousTricks = buildSevenPreviousTricks();
        previousTricks.add(new TestTrickBuilder()
                .withTrump(HEARTS)
                .withCardPlayed(WEST, Card.of(HEARTS, SEVEN))
                .withCardPlayed(NORTH, Card.of(HEARTS, EIGHT))
                .withCardPlayed(EAST, Card.of(HEARTS, QUEEN))
                .withCardPlayed(SOUTH, Card.of(HEARTS, KING))
                .withStartingPlayer(EAST)
                .withTrickWinner(SOUTH)
                .build()); // quickest way to get 8 tricks :S

        GameState gameState = new TestGameStateBuilder()
                .withGameId("someGameId")
                .withDealer(NORTH)
                .withBidding(new TestBiddingBuilder()
                        .withFinalTrump(HEARTS)
                        .withFinalBidBy(EAST)
                        .build())
                .withPreviousTricks(previousTricks)
                .withHand(WEST, new ArrayList<>())
                .withHand(NORTH, new ArrayList<>())
                .withHand(EAST, new ArrayList<>())
                .withHand(SOUTH, new ArrayList<>())
                .build();
        assertThat(gameState.isDealerButtonAvailable(), is(false));

        gameState.calculateScore();

        // score should be scored, game should be fresh
        assertThat(gameState.getGameGameScores().size(), is(1));
        assertThat(gameState.getGameGameScores().get(0).getScores().get(NS), is(17));
        assertThat(gameState.getDealer(), is(EAST));
        assertThat(gameState.getTurn(), is(EAST));
        assertNull(gameState.getBidding());
        assertNull(gameState.getCurrentTrick());
        assertTrue(gameState.getHands().values().stream().allMatch(Objects::isNull));
        assertThat(gameState.getGameId(), is("someGameId"));
        assertThat(gameState.isDealerButtonAvailable(), is(true));
    }

    private List<Trick> buildSevenPreviousTricks() {
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

        return previousTricks;
    }
}
package com.keemerz.klaverjas.converter;

import com.keemerz.klaverjas.comparator.NonTrumpOrderComparator;
import com.keemerz.klaverjas.comparator.TrumpOrderComparator;
import com.keemerz.klaverjas.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.keemerz.klaverjas.domain.Bid.*;
import static com.keemerz.klaverjas.domain.Rank.*;
import static com.keemerz.klaverjas.domain.Seat.*;
import static com.keemerz.klaverjas.domain.Suit.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;

class GameStateToPlayerGameStateConverterTest {

    private List<Card> ALL_SPADES = filterSuit(SPADES).stream().sorted(new NonTrumpOrderComparator()).collect(Collectors.toList());;
    private List<Card> ALL_HEARTS = filterSuit(HEARTS).stream().sorted(new TrumpOrderComparator()).collect(Collectors.toList());
    private List<Card> ALL_DIAMONDS = filterSuit(DIAMONDS).stream().sorted(new NonTrumpOrderComparator()).collect(Collectors.toList());;
    private List<Card> ALL_CLUBS = filterSuit(CLUBS).stream().sorted(new NonTrumpOrderComparator()).collect(Collectors.toList());;

    private GameState inputGameState;

    @BeforeEach
    void setUp() {
        inputGameState = new TestGameStateBuilder()
                .withPlayer(NORTH, new TestPlayerBuilder().withPlayerId("playerNorthId").withPlayerName("Nico").build())
                .withPlayer(EAST, new TestPlayerBuilder().withPlayerId("playerEastId").withPlayerName("Eddy").build())
                .withPlayer(SOUTH, new TestPlayerBuilder().withPlayerId("playerSouthId").withPlayerName("Simone").build())
                .withPlayer(WEST, new TestPlayerBuilder().withPlayerId("playerWestId").withPlayerName("Wendy").build())
                .withBidding(
                        new TestBiddingBuilder()
                            .withProposedTrump(HEARTS)
                            .withBid(NORTH, PASS)
                            .withBid(EAST, PASS)
                            .withBid(SOUTH, PLAY)
                            .build())
                .withCurrentTrick( // TODO we can enhance the test builder to automatically remove cards in trick and in previousTricks from the starting hands!
                        new TestTrickBuilder()
                                .withCardPlayed(NORTH, Card.of(SPADES, ACE))
                                .withCardPlayed(EAST, Card.of(HEARTS, SEVEN))
                                .build())
                .withHand(NORTH, removeCardPlayed(ALL_SPADES, Card.of(SPADES, ACE)))
                .withHand(EAST, removeCardPlayed(ALL_HEARTS, Card.of(HEARTS, SEVEN)))
                .withHand(SOUTH, ALL_DIAMONDS)
                .withHand(WEST, ALL_CLUBS)
                .withPreviousTricks(Arrays.asList(
                        new TestTrickBuilder()
                        .withTrickWinner(NORTH)
                        .build(),
                        new TestTrickBuilder()
                                .withTrickWinner(NORTH)
                                .build(),
                        new TestTrickBuilder()
                                .withTrickWinner(WEST)
                                .build()
                ))
                .withTurn(SOUTH)
                .withDealer(NORTH)
                .withGameScore(new GameScore(121, 41, 121, 41, 0, 0, "", ""))
                .withGameScore(new GameScore(0, 182, 80, 82, 0, 20, "NAT", ""))
                .build();
    }

    @Test
    public void shouldRotateGameForNorth() {
        PlayerGameState output = GameStateToPlayerGameStateConverter.toPlayerGameStateForPlayer("playerNorthId", inputGameState);

        assertThat(output.getBidding().getProposedTrump(), is(HEARTS));
        assertThat(output.getBidding().getBids().get(NORTH), is(PLAY));
        assertNull(output.getBidding().getBids().get(EAST));
        assertThat(output.getBidding().getBids().get(SOUTH), is(PASS));
        assertThat(output.getBidding().getBids().get(WEST), is(PASS));

        assertThat(output.getHand(), is(removeCardPlayed(ALL_SPADES, Card.of(SPADES, ACE))));

        assertNull(output.getCurrentTrick().getCardsPlayed().get(NORTH));
        assertNull(output.getCurrentTrick().getCardsPlayed().get(EAST));
        assertThat(output.getCurrentTrick().getCardsPlayed().get(SOUTH), is(Card.of(SPADES, ACE)));
        assertThat(output.getCurrentTrick().getCardsPlayed().get(WEST), is(Card.of(HEARTS, SEVEN)));

        assertThat(output.getNrOfCardsInHand().get(NORTH), is(8));
        assertThat(output.getNrOfCardsInHand().get(EAST), is(8));
        assertThat(output.getNrOfCardsInHand().get(SOUTH), is(7));
        assertThat(output.getNrOfCardsInHand().get(WEST), is(7));

        assertThat(output.getNrOfTricks().get(NORTH), is(0));
        assertThat(output.getNrOfTricks().get(EAST), is(1));
        assertThat(output.getNrOfTricks().get(SOUTH), is(2));
        assertThat(output.getNrOfTricks().get(WEST), is(0));

        assertThat(output.getPlayers().get(NORTH), is("Simone"));
        assertThat(output.getPlayers().get(EAST), is("Wendy"));
        assertThat(output.getPlayers().get(SOUTH), is("Nico"));
        assertThat(output.getPlayers().get(WEST), is("Eddy"));

        assertThat(output.getTurn(), is(NORTH));

        assertThat(output.getDealer(), is(SOUTH));

        assertThat(output.getGameScores().get(0), is(new GameScore(121, 41, 121, 41, 0, 0, "", "")));
        assertThat(output.getGameScores().get(1), is(new GameScore(0, 182, 80, 82, 0, 20, "NAT", "")));
        assertThat(output.getTotalScore(), is(new MatchScore(121, 223)));
    }

    @Test
    public void shouldRotateGameForEast() {
        PlayerGameState output = GameStateToPlayerGameStateConverter.toPlayerGameStateForPlayer("playerEastId", inputGameState);

        assertThat(output.getBidding().getProposedTrump(), is(HEARTS));
        assertNull(output.getBidding().getBids().get(NORTH));
        assertThat(output.getBidding().getBids().get(EAST), is(PASS));
        assertThat(output.getBidding().getBids().get(SOUTH), is(PASS));
        assertThat(output.getBidding().getBids().get(WEST), is(PLAY));

        assertThat(output.getHand(), is(removeCardPlayed(ALL_HEARTS, Card.of(HEARTS, SEVEN))));

        assertNull(output.getCurrentTrick().getCardsPlayed().get(NORTH));
        assertThat(output.getCurrentTrick().getCardsPlayed().get(EAST), is(Card.of(SPADES, ACE)));
        assertThat(output.getCurrentTrick().getCardsPlayed().get(SOUTH), is(Card.of(HEARTS, SEVEN)));
        assertNull(output.getCurrentTrick().getCardsPlayed().get(WEST));

        assertThat(output.getNrOfCardsInHand().get(NORTH), is(8));
        assertThat(output.getNrOfCardsInHand().get(EAST), is(7));
        assertThat(output.getNrOfCardsInHand().get(SOUTH), is(7));
        assertThat(output.getNrOfCardsInHand().get(WEST), is(8));

        assertThat(output.getNrOfTricks().get(NORTH), is(1));
        assertThat(output.getNrOfTricks().get(EAST), is(2));
        assertThat(output.getNrOfTricks().get(SOUTH), is(0));
        assertThat(output.getNrOfTricks().get(WEST), is(0));

        assertThat(output.getPlayers().get(NORTH), is("Wendy"));
        assertThat(output.getPlayers().get(EAST), is("Nico"));
        assertThat(output.getPlayers().get(SOUTH), is("Eddy"));
        assertThat(output.getPlayers().get(WEST), is("Simone"));

        assertThat(output.getTurn(), is(WEST));

        assertThat(output.getDealer(), is(EAST));

        assertThat(output.getGameScores().get(0), is(new GameScore(41, 121, 41, 121, 0, 0, "", "")));
        assertThat(output.getGameScores().get(1), is(new GameScore(182, 0, 82, 80, 20, 0, "", "NAT")));
        assertThat(output.getTotalScore(), is(new MatchScore(223, 121)));
    }

    @Test
    public void shouldRotateGameForSouth() {
        PlayerGameState output = GameStateToPlayerGameStateConverter.toPlayerGameStateForPlayer("playerSouthId", inputGameState);

        assertThat(output.getBidding().getProposedTrump(), is(HEARTS));
        assertThat(output.getBidding().getBids().get(NORTH), is(PASS));
        assertThat(output.getBidding().getBids().get(EAST), is(PASS));
        assertThat(output.getBidding().getBids().get(SOUTH), is(PLAY));
        assertNull(output.getBidding().getBids().get(WEST));

        assertThat(output.getHand(), is(ALL_DIAMONDS));

        assertThat(output.getCurrentTrick().getCardsPlayed().get(NORTH), is(Card.of(SPADES, ACE)));
        assertThat(output.getCurrentTrick().getCardsPlayed().get(EAST), is(Card.of(HEARTS, SEVEN)));
        assertNull(output.getCurrentTrick().getCardsPlayed().get(SOUTH));
        assertNull(output.getCurrentTrick().getCardsPlayed().get(WEST));

        assertThat(output.getNrOfCardsInHand().get(NORTH), is(7));
        assertThat(output.getNrOfCardsInHand().get(EAST), is(7));
        assertThat(output.getNrOfCardsInHand().get(SOUTH), is(8));
        assertThat(output.getNrOfCardsInHand().get(WEST), is(8));

        assertThat(output.getNrOfTricks().get(NORTH), is(2));
        assertThat(output.getNrOfTricks().get(EAST), is(0));
        assertThat(output.getNrOfTricks().get(SOUTH), is(0));
        assertThat(output.getNrOfTricks().get(WEST), is(1));

        assertThat(output.getPlayers().get(NORTH), is("Nico"));
        assertThat(output.getPlayers().get(EAST), is("Eddy"));
        assertThat(output.getPlayers().get(SOUTH), is("Simone"));
        assertThat(output.getPlayers().get(WEST), is("Wendy"));

        assertThat(output.getTurn(), is(SOUTH));

        assertThat(output.getDealer(), is(NORTH));

        assertThat(output.getGameScores().get(0), is(new GameScore(121, 41, 121, 41, 0, 0, "", "")));
        assertThat(output.getGameScores().get(1), is(new GameScore(0, 182, 80, 82, 0, 20, "NAT", "")));
        assertThat(output.getTotalScore(), is(new MatchScore(121, 223)));
    }

    @Test
    public void shouldRotateGameForWest() {
        PlayerGameState output = GameStateToPlayerGameStateConverter.toPlayerGameStateForPlayer("playerWestId", inputGameState);

        assertThat(output.getBidding().getProposedTrump(), is(HEARTS));
        assertThat(output.getBidding().getBids().get(NORTH), is(PASS));
        assertThat(output.getBidding().getBids().get(EAST), is(PLAY));
        assertNull(output.getBidding().getBids().get(SOUTH));
        assertThat(output.getBidding().getBids().get(WEST), is(PASS));

        assertThat(output.getHand(), is(ALL_CLUBS));

        assertThat(output.getCurrentTrick().getCardsPlayed().get(NORTH), is(Card.of(HEARTS, SEVEN)));
        assertNull(output.getCurrentTrick().getCardsPlayed().get(EAST));
        assertNull(output.getCurrentTrick().getCardsPlayed().get(SOUTH));
        assertThat(output.getCurrentTrick().getCardsPlayed().get(WEST), is(Card.of(SPADES, ACE)));

        assertThat(output.getNrOfCardsInHand().get(NORTH), is(7));
        assertThat(output.getNrOfCardsInHand().get(EAST), is(8));
        assertThat(output.getNrOfCardsInHand().get(SOUTH), is(8));
        assertThat(output.getNrOfCardsInHand().get(WEST), is(7));

        assertThat(output.getNrOfTricks().get(NORTH), is(0));
        assertThat(output.getNrOfTricks().get(EAST), is(0));
        assertThat(output.getNrOfTricks().get(SOUTH), is(1));
        assertThat(output.getNrOfTricks().get(WEST), is(2));

        assertThat(output.getPlayers().get(NORTH), is("Eddy"));
        assertThat(output.getPlayers().get(EAST), is("Simone"));
        assertThat(output.getPlayers().get(SOUTH), is("Wendy"));
        assertThat(output.getPlayers().get(WEST), is("Nico"));

        assertThat(output.getTurn(), is(EAST));

        assertThat(output.getDealer(), is(WEST));

        assertThat(output.getGameScores().get(0), is(new GameScore(41, 121, 41, 121, 0, 0, "", "")));
        assertThat(output.getGameScores().get(1), is(new GameScore(182, 0, 82, 80, 20, 0, "", "NAT")));
        assertThat(output.getTotalScore(), is(new MatchScore(223, 121)));
    }

    private List<Card> removeCardPlayed(List<Card> allCards, Card... cardsPlayed) {
        List<Card> remainingCards = new ArrayList<>(allCards);
        for (Card cardPlayed : cardsPlayed) {
             remainingCards.remove(cardPlayed);
        }
        return remainingCards;
    }

    private List<Card> filterSuit(Suit clubs) {
        return new ShuffledDeck().getCards().stream()
                .filter(card -> card.getSuit() == clubs).collect(Collectors.toList());
    }


}
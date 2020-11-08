package com.keemerz.klaverjas.converter;

import com.keemerz.klaverjas.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.keemerz.klaverjas.domain.Rank.ACE;
import static com.keemerz.klaverjas.domain.Rank.SEVEN;
import static com.keemerz.klaverjas.domain.Seat.*;
import static com.keemerz.klaverjas.domain.Suit.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;

class GameStateToPlayerGameStateConverterTest {

    private List<Card> ALL_SPADES = filterSuit(SPADES);
    private List<Card> ALL_HEARTS = filterSuit(HEARTS);
    private List<Card> ALL_DIAMONDS = filterSuit(DIAMONDS);
    private List<Card> ALL_CLUBS = filterSuit(CLUBS);

    private GameState inputGameState;

    @BeforeEach
    void setUp() {
        inputGameState = new TestGameStateBuilder()
                .withPlayer(NORTH, new TestPlayerBuilder().withPlayerId("playerNorthId").withPlayerName("Nico").build())
                .withPlayer(EAST, new TestPlayerBuilder().withPlayerId("playerEastId").withPlayerName("Eddy").build())
                .withPlayer(SOUTH, new TestPlayerBuilder().withPlayerId("playerSouthId").withPlayerName("Simone").build())
                .withPlayer(WEST, new TestPlayerBuilder().withPlayerId("playerWestId").withPlayerName("Wendy").build())
                .withCurrentTrick( // TODO we can enhance the test builder to automatically remove cards in trick and in previousTricks from the starting hands!
                        new TestTrickBuilder()
                                .withCardPlayed(NORTH, Card.of(SPADES, ACE))
                                .withCardPlayed(EAST, Card.of(HEARTS, SEVEN))
                                .build())
                .withHand(NORTH, removeCardPlayed(ALL_SPADES, Card.of(SPADES, ACE)))
                .withHand(EAST, removeCardPlayed(ALL_HEARTS, Card.of(HEARTS, SEVEN)))
                .withHand(SOUTH, ALL_DIAMONDS)
                .withHand(WEST, ALL_CLUBS)
                .withTurn(SOUTH)
                .build();
    }

    @Test
    public void shouldRotateForNorth() {
        PlayerGameState output = GameStateToPlayerGameStateConverter.toPlayerGameStateForPlayer("playerNorthId", inputGameState);

        assertThat(output.getHand(), is(removeCardPlayed(ALL_SPADES, Card.of(SPADES, ACE))));

        assertNull(output.getCurrentTrick().getCardsPlayed().get(NORTH));
        assertNull(output.getCurrentTrick().getCardsPlayed().get(EAST));
        assertThat(output.getCurrentTrick().getCardsPlayed().get(SOUTH), is(Card.of(SPADES, ACE)));
        assertThat(output.getCurrentTrick().getCardsPlayed().get(WEST), is(Card.of(HEARTS, SEVEN)));

        assertThat(output.getNrOfCardsInHand().get(NORTH), is(8));
        assertThat(output.getNrOfCardsInHand().get(EAST), is(8));
        assertThat(output.getNrOfCardsInHand().get(SOUTH), is(7));
        assertThat(output.getNrOfCardsInHand().get(WEST), is(7));

        assertThat(output.getPlayers().get(NORTH), is("Simone"));
        assertThat(output.getPlayers().get(EAST), is("Wendy"));
        assertThat(output.getPlayers().get(SOUTH), is("Nico"));
        assertThat(output.getPlayers().get(WEST), is("Eddy"));

        assertThat(output.getTurn(), is(NORTH));
    }

    @Test
    public void shouldRotateForEast() {
        PlayerGameState output = GameStateToPlayerGameStateConverter.toPlayerGameStateForPlayer("playerEastId", inputGameState);

        assertThat(output.getHand(), is(removeCardPlayed(ALL_HEARTS, Card.of(HEARTS, SEVEN))));

        assertNull(output.getCurrentTrick().getCardsPlayed().get(NORTH));
        assertThat(output.getCurrentTrick().getCardsPlayed().get(EAST), is(Card.of(SPADES, ACE)));
        assertThat(output.getCurrentTrick().getCardsPlayed().get(SOUTH), is(Card.of(HEARTS, SEVEN)));
        assertNull(output.getCurrentTrick().getCardsPlayed().get(WEST));

        assertThat(output.getNrOfCardsInHand().get(NORTH), is(8));
        assertThat(output.getNrOfCardsInHand().get(EAST), is(7));
        assertThat(output.getNrOfCardsInHand().get(SOUTH), is(7));
        assertThat(output.getNrOfCardsInHand().get(WEST), is(8));

        assertThat(output.getPlayers().get(NORTH), is("Wendy"));
        assertThat(output.getPlayers().get(EAST), is("Nico"));
        assertThat(output.getPlayers().get(SOUTH), is("Eddy"));
        assertThat(output.getPlayers().get(WEST), is("Simone"));

        assertThat(output.getTurn(), is(WEST));
    }

    @Test
    public void shouldRotateForSouth() {
        PlayerGameState output = GameStateToPlayerGameStateConverter.toPlayerGameStateForPlayer("playerSouthId", inputGameState);

        assertThat(output.getHand(), is(ALL_DIAMONDS));

        assertThat(output.getCurrentTrick().getCardsPlayed().get(NORTH), is(Card.of(SPADES, ACE)));
        assertThat(output.getCurrentTrick().getCardsPlayed().get(EAST), is(Card.of(HEARTS, SEVEN)));
        assertNull(output.getCurrentTrick().getCardsPlayed().get(SOUTH));
        assertNull(output.getCurrentTrick().getCardsPlayed().get(WEST));

        assertThat(output.getNrOfCardsInHand().get(NORTH), is(7));
        assertThat(output.getNrOfCardsInHand().get(EAST), is(7));
        assertThat(output.getNrOfCardsInHand().get(SOUTH), is(8));
        assertThat(output.getNrOfCardsInHand().get(WEST), is(8));

        assertThat(output.getPlayers().get(NORTH), is("Nico"));
        assertThat(output.getPlayers().get(EAST), is("Eddy"));
        assertThat(output.getPlayers().get(SOUTH), is("Simone"));
        assertThat(output.getPlayers().get(WEST), is("Wendy"));

        assertThat(output.getTurn(), is(SOUTH));
    }

    @Test
    public void shouldRotateForWest() {
        PlayerGameState output = GameStateToPlayerGameStateConverter.toPlayerGameStateForPlayer("playerWestId", inputGameState);

        assertThat(output.getHand(), is(ALL_CLUBS));

        assertThat(output.getCurrentTrick().getCardsPlayed().get(NORTH), is(Card.of(HEARTS, SEVEN)));
        assertNull(output.getCurrentTrick().getCardsPlayed().get(EAST));
        assertNull(output.getCurrentTrick().getCardsPlayed().get(SOUTH));
        assertThat(output.getCurrentTrick().getCardsPlayed().get(WEST), is(Card.of(SPADES, ACE)));

        assertThat(output.getNrOfCardsInHand().get(NORTH), is(7));
        assertThat(output.getNrOfCardsInHand().get(EAST), is(8));
        assertThat(output.getNrOfCardsInHand().get(SOUTH), is(8));
        assertThat(output.getNrOfCardsInHand().get(WEST), is(7));

        assertThat(output.getPlayers().get(NORTH), is("Eddy"));
        assertThat(output.getPlayers().get(EAST), is("Simone"));
        assertThat(output.getPlayers().get(SOUTH), is("Wendy"));
        assertThat(output.getPlayers().get(WEST), is("Nico"));

        assertThat(output.getTurn(), is(EAST));
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
package com.keemerz.klaverjas.converter;

import com.keemerz.klaverjas.domain.Card;
import com.keemerz.klaverjas.domain.GameState;
import com.keemerz.klaverjas.domain.PlayerGameState;
import com.keemerz.klaverjas.domain.Trick;
import com.keemerz.klaverjas.repository.PlayerRepository;

import java.util.List;
import java.util.Map;

import static com.keemerz.klaverjas.domain.Rank.*;
import static com.keemerz.klaverjas.domain.Seat.*;
import static com.keemerz.klaverjas.domain.Suit.*;

public class GameStateToPlayerGameStateConverter {

    private static PlayerRepository playerRepository = PlayerRepository.getInstance();

    public static PlayerGameState toPlayerGameState(String playerId, GameState gameState) {
        // Rigged!
        return new PlayerGameState(gameState.getGameId(),
                true,
                List.of(Card.of(HEARTS, JACK),
                        Card.of(SPADES, QUEEN),
                        Card.of(SPADES, ACE),
                        Card.of(DIAMONDS, EIGHT),
                        Card.of(CLUBS, TEN),
                        Card.of(CLUBS, ACE)),
                new Trick(CLUBS, NORTH, Map.of(
                        NORTH, Card.of(HEARTS, SEVEN),
                        EAST, Card.of(HEARTS, ACE))),
                Map.of(NORTH, "Ernst", EAST, "Marlies", SOUTH, "Luigi", WEST, "Jim-Bob"),
                Map.of(NORTH, 5, EAST, 5, SOUTH, 6, WEST, 6),
                SOUTH
                );
    }
}

package com.keemerz.klaverjas.converter;

import com.keemerz.klaverjas.domain.*;

import java.util.HashMap;
import java.util.Map;

import static com.keemerz.klaverjas.domain.Seat.*;

public class GameStateToPlayerGameStateConverter {

    public static PlayerGameState toPlayerGameStateForPlayer(String playerId, GameState gameState) {
        Seat currentPlayerSeat = gameState.getAbsoluteSeatForPlayer(playerId);

        Trick rotatedTrick = gameState.getCurrentTrick().rotateForSeat(currentPlayerSeat);
        return new PlayerGameState(
                gameState.getGameId(),
                true,
                gameState.getHands().get(currentPlayerSeat),
                rotatedTrick,
                buildPlayerNamesMap(gameState, currentPlayerSeat),
                buildCardsInHandMap(gameState, currentPlayerSeat),
                gameState.getTurn().rotateForSeat(currentPlayerSeat)
        );
    }

    private static Map<Seat, Integer> buildCardsInHandMap(GameState gameState, Seat currentPlayerSeat) {
        Map<Seat, Integer> cardsPerPlayer = new HashMap<>();
        cardsPerPlayer.put(SOUTH, gameState.getHands().get(currentPlayerSeat).size());
        cardsPerPlayer.put(WEST, gameState.getHands().get(currentPlayerSeat.getLeftHandPlayer()).size());
        cardsPerPlayer.put(NORTH, gameState.getHands().get(currentPlayerSeat.getPartner()).size());
        cardsPerPlayer.put(EAST, gameState.getHands().get(currentPlayerSeat.getRightHandPlayer()).size());
        return cardsPerPlayer;
    }

    private static Map<Seat, String> buildPlayerNamesMap(GameState gameState, Seat currentPlayerSeat) {
        Map<Seat, String> playerNames = new HashMap<>();
        playerNames.put(SOUTH, gameState.getPlayers().get(currentPlayerSeat).getName());
        playerNames.put(WEST, gameState.getPlayers().get(currentPlayerSeat.getLeftHandPlayer()).getName());
        playerNames.put(NORTH, gameState.getPlayers().get(currentPlayerSeat.getPartner()).getName());
        playerNames.put(EAST, gameState.getPlayers().get(currentPlayerSeat.getRightHandPlayer()).getName());
        return playerNames;
    }

//    public static PlayerGameState toPlayerGameState(String playerId, GameState gameState) {
//        // Rigged!
//        return new PlayerGameState(gameState.getGameId(),
//                true,
//                List.of(Card.of(HEARTS, JACK),
//                        Card.of(SPADES, QUEEN),
//                        Card.of(SPADES, ACE),
//                        Card.of(DIAMONDS, EIGHT),
//                        Card.of(CLUBS, TEN),
//                        Card.of(CLUBS, ACE)),
//                new Trick(CLUBS, NORTH, Map.of(
//                        NORTH, Card.of(HEARTS, SEVEN),
//                        EAST, Card.of(HEARTS, ACE))),
//                Map.of(NORTH, "Ernst", EAST, "Marlies", SOUTH, "Luigi", WEST, "Jim-Bob"),
//                Map.of(NORTH, 5, EAST, 5, SOUTH, 6, WEST, 6),
//                SOUTH
//        );
//    }
}

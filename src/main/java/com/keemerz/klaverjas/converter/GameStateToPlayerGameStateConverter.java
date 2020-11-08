package com.keemerz.klaverjas.converter;

import com.keemerz.klaverjas.domain.*;

import java.util.HashMap;
import java.util.List;
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
        cardsPerPlayer.put(SOUTH, getCardsInHand(gameState, currentPlayerSeat));
        cardsPerPlayer.put(WEST, getCardsInHand(gameState, currentPlayerSeat.getLeftHandPlayer()));
        cardsPerPlayer.put(NORTH, getCardsInHand(gameState, currentPlayerSeat.getPartner()));
        cardsPerPlayer.put(EAST, getCardsInHand(gameState, currentPlayerSeat.getRightHandPlayer()));
        return cardsPerPlayer;
    }

    private static Map<Seat, String> buildPlayerNamesMap(GameState gameState, Seat currentPlayerSeat) {
        Map<Seat, String> playerNames = new HashMap<>();
        playerNames.put(SOUTH, getName(gameState, currentPlayerSeat));
        playerNames.put(WEST, getName(gameState, currentPlayerSeat.getLeftHandPlayer()));
        playerNames.put(NORTH, getName(gameState, currentPlayerSeat.getPartner()));
        playerNames.put(EAST, getName(gameState, currentPlayerSeat.getRightHandPlayer()));
        return playerNames;
    }

    private static String getName(GameState gameState, Seat seat) {
        return gameState.getPlayers().entrySet().stream()
                .filter(entry -> entry.getKey().equals(seat))
                .map(Map.Entry::getValue)
                .map(Player::getName)
                .findFirst()
                .orElse("");
    }

    private static int getCardsInHand(GameState gameState, Seat seat) {
        return gameState.getHands().entrySet().stream()
                .filter(entry -> entry.getKey().equals(seat))
                .map(Map.Entry::getValue)
                .map(List::size)
                .findFirst()
                .orElse(0);
    }
}

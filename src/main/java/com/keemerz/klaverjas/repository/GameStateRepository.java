package com.keemerz.klaverjas.repository;

import com.keemerz.klaverjas.domain.GameState;
import com.keemerz.klaverjas.domain.Player;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class GameStateRepository {

    private static Map<String, GameState> GAMESTATES = new HashMap<>();

    public GameState getGameState(String gameId) {
        return GAMESTATES.get(gameId);
    }

    public void changeGameState(GameState state) {
        GAMESTATES.put(state.getGameId(), state);
    }

    public List<GameState> getAllGames() {
        return GAMESTATES.values().stream()
                .sorted(Comparator.comparing(game -> game.determinePlayerIds().size()))
                .collect(Collectors.toList());
    }

    public void removePlayerFromGames(Player player) {
        GAMESTATES.entrySet().stream()
                .filter(entry -> entry.getValue().determinePlayerIds().contains(player.getPlayerId()))
                .forEach(entry -> {
                    GameState gameState = entry.getValue();
                    gameState.freeSeat(player);
                });
        GAMESTATES.entrySet().removeIf(entry -> entry.getValue().determinePlayerIds().isEmpty());
    }
}

package com.keemerz.klaverjas.converter;

import com.keemerz.klaverjas.domain.ActiveGame;
import com.keemerz.klaverjas.domain.GameState;
import com.keemerz.klaverjas.domain.Player;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class GameStateToActiveGameConverter {

    public static ActiveGame toActiveGame(GameState gameState) {
        List<String> playerNames = new ArrayList<>();
        for (Player player : gameState.getPlayers().values()) {
            playerNames.add(player.getName());
        }
        playerNames.sort(Comparator.naturalOrder());

        return new ActiveGame(gameState.getGameId(), playerNames);
    }
}

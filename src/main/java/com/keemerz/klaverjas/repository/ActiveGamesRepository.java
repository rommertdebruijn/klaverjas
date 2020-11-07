package com.keemerz.klaverjas.repository;

import com.keemerz.klaverjas.converter.GameStateToActiveGameConverter;
import com.keemerz.klaverjas.domain.ActiveGame;

import java.util.List;
import java.util.stream.Collectors;

public class ActiveGamesRepository {

    private static final ActiveGamesRepository INSTANCE = new ActiveGamesRepository();

    private ActiveGamesRepository() {
    }

    public static ActiveGamesRepository getInstance() {
        return INSTANCE;
    }

    public List<ActiveGame> getActiveGames() {
        return GameStateRepository.getInstance().getAllGames().stream()
                .map(GameStateToActiveGameConverter::toActiveGame)
                .collect(Collectors.toList());
    }
}

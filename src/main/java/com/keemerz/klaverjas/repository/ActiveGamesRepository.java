package com.keemerz.klaverjas.repository;

import com.keemerz.klaverjas.converter.GameStateToActiveGameConverter;
import com.keemerz.klaverjas.domain.ActiveGame;
import com.keemerz.klaverjas.domain.GameState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ActiveGamesRepository {

    @Autowired
    GameStateRepository gameStateRepository;

    public List<ActiveGame> getActiveGames() {
        return gameStateRepository.getAllGames().stream()
                .filter(GameState::isActive)
                .map(GameStateToActiveGameConverter::toActiveGame)
                .collect(Collectors.toList());
    }
}

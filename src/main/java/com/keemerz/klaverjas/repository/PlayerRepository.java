package com.keemerz.klaverjas.repository;

import com.keemerz.klaverjas.domain.Player;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerRepository {

    private static final PlayerRepository INSTANCE = new PlayerRepository();
    private static final List<Player> PLAYERS = new ArrayList<>();
    static {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        PLAYERS.add(new Player("user1", UUID.randomUUID().toString(), "Jim-Bob"));
        PLAYERS.add(new Player("user2", UUID.randomUUID().toString(), "Marlies"));
        PLAYERS.add(new Player("user3", UUID.randomUUID().toString(), "Ernst"));
        PLAYERS.add(new Player("user4", UUID.randomUUID().toString(), "Luigi"));
    }

    private PlayerRepository() {}

    public static PlayerRepository getInstance() {
        return INSTANCE;
    }

    public Player getPlayerByUserId(String userId) {
        return PLAYERS.stream()
                .filter(player -> player.getUserId().equals(userId))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    public Player getPlayerByPlayerId(String playerId) {
        return PLAYERS.stream()
                .filter(player -> player.getPlayerId().equals(playerId))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    public void removePlayer(String userId) {
        PLAYERS.remove(getPlayerByUserId(userId));
    }

}

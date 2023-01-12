package com.keemerz.klaverjas.repository;

import com.keemerz.klaverjas.domain.Player;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class PlayerRepository {

    private static final List<Player> PLAYERS = new ArrayList<>();
    static {
        PLAYERS.add(new Player("user1", UUID.randomUUID().toString(), "Jim-Bob"));
        PLAYERS.add(new Player("user2", UUID.randomUUID().toString(), "Marlies"));
        PLAYERS.add(new Player("user3", UUID.randomUUID().toString(), "Ernst"));
        PLAYERS.add(new Player("user4", UUID.randomUUID().toString(), "Luigi"));
        PLAYERS.add(new Player("user5", UUID.randomUUID().toString(), "Gregor"));

        PLAYERS.add(new Player("martin", UUID.randomUUID().toString(), "Martin"));
        PLAYERS.add(new Player("alex", UUID.randomUUID().toString(), "Alex"));
        PLAYERS.add(new Player("ronald", UUID.randomUUID().toString(), "Ronald"));
        PLAYERS.add(new Player("eelco", UUID.randomUUID().toString(), "Eelco"));
        PLAYERS.add(new Player("robert", UUID.randomUUID().toString(), "Robert"));
        PLAYERS.add(new Player("rommert", UUID.randomUUID().toString(), "Rommert"));
        PLAYERS.add(new Player("toon", UUID.randomUUID().toString(), "Toon"));
        PLAYERS.add(new Player("jasper", UUID.randomUUID().toString(), "Jasper"));
        PLAYERS.add(new Player("yoran", UUID.randomUUID().toString(), "Yoran"));
        PLAYERS.add(new Player("paul", UUID.randomUUID().toString(), "Paul"));
        PLAYERS.add(new Player("joris", UUID.randomUUID().toString(), "Joris"));
        PLAYERS.add(new Player("frank", UUID.randomUUID().toString(), "Frank"));
        PLAYERS.add(new Player("janthijs", UUID.randomUUID().toString(), "Jan-Thijs"));
        PLAYERS.add(new Player("anton", UUID.randomUUID().toString(), "Anton"));
        PLAYERS.add(new Player("edzo", UUID.randomUUID().toString(), "Edzo"));
        PLAYERS.add(new Player("marc", UUID.randomUUID().toString(), "Marc"));
        PLAYERS.add(new Player("sander", UUID.randomUUID().toString(), "Sander"));
        PLAYERS.add(new Player("mitchell", UUID.randomUUID().toString(), "Mitchell"));
        PLAYERS.add(new Player("jonathan", UUID.randomUUID().toString(), "Jonathan"));
        PLAYERS.add(new Player("niek", UUID.randomUUID().toString(), "Niek"));
        PLAYERS.add(new Player("stefan", UUID.randomUUID().toString(), "Stefan"));
        PLAYERS.add(new Player("ties", UUID.randomUUID().toString(), "Ties"));
        PLAYERS.add(new Player("hans", UUID.randomUUID().toString(), "Hans"));
        PLAYERS.add(new Player("mark", UUID.randomUUID().toString(), "Mark"));
        PLAYERS.add(new Player("matthijs", UUID.randomUUID().toString(), "Matthijs"));
        PLAYERS.add(new Player("sidney", UUID.randomUUID().toString(), "Sidney")); // maatjes van Ties
        PLAYERS.add(new Player("rogier", UUID.randomUUID().toString(), "Rogier")); // maatjes van Ties
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

    public void setLoginTimestamp(String userId, LocalDateTime now) {
        getPlayerByUserId(userId).setLoginTimestamp(now);
    }

    public List<Player> getActivePlayers() {
        return PLAYERS.stream()
                .filter(Player::isActive)
                .collect(Collectors.toList());
    }

    public void removeLoginTimestamp(String userId) {
        Player player = getPlayerByUserId(userId);
        player.setLoginTimestamp(null); // no longer logged in
    }
}

package com.keemerz.klaverjas.websocket.outbound;

import com.keemerz.klaverjas.domain.ActiveGame;

import java.util.List;

import static com.keemerz.klaverjas.websocket.outbound.LobbyMessage.MessageType.ACTIVE_GAMES;

public class ActiveGamesMessage extends LobbyMessage {

    private List<ActiveGame> activeGames;

    public ActiveGamesMessage(List<ActiveGame> activeGames) {
        super(ACTIVE_GAMES);
        this.activeGames = activeGames;
    }

    public List<ActiveGame> getActiveGames() {
        return activeGames;
    }
}

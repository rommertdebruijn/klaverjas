package com.keemerz.klaverjas.websocket.outbound;

import com.keemerz.klaverjas.domain.ActiveGame;

import java.util.List;

import static com.keemerz.klaverjas.websocket.outbound.LobbyMessage.MessageType.GOODBYE;

public class PlayerLeavesLobbyMessage extends LobbyMessage {

    private String content;
    private List<String> loggedInPlayers;
    private List<ActiveGame> activeGames;

    public PlayerLeavesLobbyMessage(String content, List<String> loggedInPlayers, List<ActiveGame> activeGames) {
        super(GOODBYE);
        this.content = content;
        this.loggedInPlayers = loggedInPlayers;
        this.activeGames = activeGames;
    }

    public String getContent() {
        return content;
    }

    public List<String> getLoggedInPlayers() {
        return loggedInPlayers;
    }

    public List<ActiveGame> getActiveGames() {
        return activeGames;
    }
}

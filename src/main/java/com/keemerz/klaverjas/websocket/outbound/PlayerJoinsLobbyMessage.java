package com.keemerz.klaverjas.websocket.outbound;

import com.keemerz.klaverjas.domain.ActiveGame;

import java.util.List;

import static com.keemerz.klaverjas.websocket.outbound.LobbyMessage.MessageType.WELCOME;

public class PlayerJoinsLobbyMessage extends LobbyMessage {

    private String content;
    private List<String> loggedInPlayers;
    private List<ActiveGame> activeGames;

    public PlayerJoinsLobbyMessage(String content, List<String> loggedInPlayers, List<ActiveGame> activeGames) {
        super(WELCOME);
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

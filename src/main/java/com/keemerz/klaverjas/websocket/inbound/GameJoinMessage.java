package com.keemerz.klaverjas.websocket.inbound;

public class GameJoinMessage {

	private String gameId;

	public GameJoinMessage() {
	}

	public GameJoinMessage(String gameId) {
		this.gameId = gameId;
	}

	public String getGameId() {
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}
}

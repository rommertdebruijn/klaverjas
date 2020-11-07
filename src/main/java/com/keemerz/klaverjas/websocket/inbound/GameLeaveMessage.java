package com.keemerz.klaverjas.websocket.inbound;

public class GameLeaveMessage {

	private String gameId;

	public GameLeaveMessage() {
	}

	public GameLeaveMessage(String gameId) {
		this.gameId = gameId;
	}

	public String getGameId() {
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}
}

package com.keemerz.klaverjas.websocket.inbound;

public class CalculateScoreMessage {

	private String gameId;

	public CalculateScoreMessage() {
	}

	public CalculateScoreMessage(String gameId) {
		this.gameId = gameId;
	}

	public String getGameId() {
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}
}

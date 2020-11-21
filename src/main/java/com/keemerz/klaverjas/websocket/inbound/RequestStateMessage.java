package com.keemerz.klaverjas.websocket.inbound;

public class RequestStateMessage {

	private String gameId;

	public RequestStateMessage() {
	}

	public RequestStateMessage(String gameId) {
		this.gameId = gameId;
	}

	public String getGameId() {
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}
}

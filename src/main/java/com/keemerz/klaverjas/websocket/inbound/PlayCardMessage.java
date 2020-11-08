package com.keemerz.klaverjas.websocket.inbound;

public class PlayCardMessage {

	private String gameId;
	private String cardId;

	public PlayCardMessage() {
	}

	public PlayCardMessage(String gameId, String cardId) {
		this.gameId = gameId;
		this.cardId = cardId;
	}

	public String getGameId() {
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

	public String getCardId() {
		return cardId;
	}

	public PlayCardMessage setCardId(String cardId) {
		this.cardId = cardId;
		return this;
	}
}

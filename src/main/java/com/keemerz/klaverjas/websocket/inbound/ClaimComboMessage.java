package com.keemerz.klaverjas.websocket.inbound;

import com.keemerz.klaverjas.domain.Bid;

public class ClaimComboMessage {

	private String gameId;

	public ClaimComboMessage() {
	}

	public ClaimComboMessage(String gameId) {
		this.gameId = gameId;
	}

	public String getGameId() {
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}
}

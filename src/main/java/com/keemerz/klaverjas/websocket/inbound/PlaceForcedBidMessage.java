package com.keemerz.klaverjas.websocket.inbound;

import com.keemerz.klaverjas.domain.Bid;
import com.keemerz.klaverjas.domain.Suit;

public class PlaceForcedBidMessage {

	private String gameId;
	private Suit forcedTrump;

	public PlaceForcedBidMessage() {
	}

	public PlaceForcedBidMessage(String gameId, Suit forcedTrump) {
		this.gameId = gameId;
		this.forcedTrump = forcedTrump;
	}

	public String getGameId() {
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

	public Suit getForcedTrump() {
		return forcedTrump;
	}

	public PlaceForcedBidMessage setForcedTrump(Suit forcedTrump) {
		this.forcedTrump = forcedTrump;
		return this;
	}
}

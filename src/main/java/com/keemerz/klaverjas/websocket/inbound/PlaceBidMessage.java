package com.keemerz.klaverjas.websocket.inbound;

import com.keemerz.klaverjas.domain.Bid;

public class PlaceBidMessage {

	private String gameId;
	private Bid bid;

	public PlaceBidMessage() {
	}

	public PlaceBidMessage(String gameId, Bid bid) {
		this.gameId = gameId;
		this.bid = bid;
	}

	public String getGameId() {
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

	public Bid getBid() {
		return bid;
	}

	public PlaceBidMessage setBid(Bid bid) {
		this.bid = bid;
		return this;
	}
}

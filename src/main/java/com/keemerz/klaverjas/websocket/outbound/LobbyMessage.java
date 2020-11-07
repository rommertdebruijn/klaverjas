package com.keemerz.klaverjas.websocket.outbound;

public class LobbyMessage {

	public enum MessageType {
		WELCOME,
		GOODBYE,
        ACTIVE_GAMES
	}

	private MessageType messageType;

	public LobbyMessage(MessageType messageType) {
		this.messageType = messageType;
	}

	public MessageType getMessageType() {
		return messageType;
	}
}

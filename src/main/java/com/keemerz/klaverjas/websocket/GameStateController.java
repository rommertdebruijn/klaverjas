package com.keemerz.klaverjas.websocket;

import com.keemerz.klaverjas.converter.GameStateToPlayerGameStateConverter;
import com.keemerz.klaverjas.domain.*;
import com.keemerz.klaverjas.repository.ActiveGamesRepository;
import com.keemerz.klaverjas.repository.GameStateRepository;
import com.keemerz.klaverjas.repository.PlayerRepository;
import com.keemerz.klaverjas.websocket.inbound.*;
import com.keemerz.klaverjas.websocket.outbound.ActiveGamesMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.List;

@Controller
public class GameStateController {

    @Autowired
    private SimpMessagingTemplate webSocket;

    private GameStateRepository gameStateRepository = GameStateRepository.getInstance();

    @MessageMapping("/game/start")
    public void startGame(GameStartMessage message, Principal principal) {
        Player sendingPlayer = PlayerRepository.getInstance().getPlayerByUserId(principal.getName());

        GameState gameState = GameState.createNewGame();
        gameState.fillSeat(sendingPlayer);
        gameStateRepository.changeGameState(gameState);

        updateGameStateForAllPlayers(gameState);
        updateLobby();

    }

    @MessageMapping("/game/join")
    public void joinGame(GameJoinMessage message, Principal principal) {
        Player sendingPlayer = PlayerRepository.getInstance().getPlayerByUserId(principal.getName());
        GameState gameState = gameStateRepository.getGameState(message.getGameId());

        if (!gameState.determinePlayerIds().contains(sendingPlayer.getPlayerId())) {
            gameState.fillSeat(sendingPlayer);
            if (gameState.getHands().isEmpty() && gameState.getPlayers().size() > 3) { // if 4th player joins, deal first hand
                gameState.dealHands();
                gameState.setBidding(Bidding.createFirstGameBidding()); // first game always clubs
            }
            gameStateRepository.changeGameState(gameState);
        }
        updateGameStateForAllPlayers(gameState);
        updateLobby();
    }

    @MessageMapping("/game/leave")
    public void leaveGame(GameLeaveMessage message, Principal principal) {
        Player sendingPlayer = PlayerRepository.getInstance().getPlayerByUserId(principal.getName());
        String sendingPlayerId = sendingPlayer.getPlayerId();

        GameState gameState = gameStateRepository.getGameState(message.getGameId());
        if (gameState.determinePlayerIds().contains(sendingPlayerId)) {
            gameStateRepository.removePlayerFromGames(sendingPlayer);

            updateGameStateForAllPlayers(gameState);
            updateGameStateForLeavingPlayer(sendingPlayerId, gameState.getGameId());

            updateLobby();
        }
    }

    private void updateLobby() {
        List<ActiveGame> activeGames = ActiveGamesRepository.getInstance().getActiveGames();
        webSocket.convertAndSend("/topic/lobby", new ActiveGamesMessage(activeGames));
    }

    private void updateGameStateForLeavingPlayer(String sendingPlayerId, String gameId) {
        String userId = PlayerRepository.getInstance().getPlayerByPlayerId(sendingPlayerId).getUserId();
        webSocket.convertAndSendToUser(userId, "/topic/game", PlayerGameState.playerLeftGameState(gameId));
    }

    @MessageMapping("/game/playcard")
    public void playCard(PlayCardMessage message, Principal principal) {
        GameState gameState = determineGameStateForPlayer(principal.getName(), message.getGameId());
        if (gameState != null) {
            Seat seat = gameState.getTurn(); // only player whose turn it is can play cards
            List<Card> cards = gameState.getHands().get(seat);
            cards.stream()
                    .filter(card -> card.getCardId().equals(message.getCardId()))
                    .findFirst()
                    .ifPresent(card -> {
                        gameState.playCard(seat, message.getCardId());
                        gameStateRepository.changeGameState(gameState);
                    });
            updateGameStateForAllPlayers(gameState);
        }
    }

    @MessageMapping("/game/makebid")
    public void placeBid(PlaceBidMessage message, Principal principal) {
        GameState gameState = determineGameStateForPlayer(principal.getName(), message.getGameId());
        if (gameState != null) {
            Seat seat = gameState.getTurn();
            Bidding bidding = gameState.getBidding();
            if (bidding.getBids().size() < 4 && bidding.getBids().get(seat) == null) {
                bidding.addBid(seat, message.getBid());

                if (message.getBid() == Bid.PLAY) {
                    bidding.setFinalTrump(bidding.getProposedTrump());
                    bidding.setFinalBidBy(seat);
                    gameState.setTurn(gameState.getDealer().getLeftHandPlayer());
                }
            }
            gameState.setTurn(seat.getLeftHandPlayer());
            gameStateRepository.changeGameState(gameState);
            updateGameStateForAllPlayers(gameState);
        }
    }

    private @Nullable GameState determineGameStateForPlayer(String userId, String gameId) {
        Player sendingPlayer = PlayerRepository.getInstance().getPlayerByUserId(userId);
        GameState gameState = gameStateRepository.getGameState(gameId);
        if (gameState.determinePlayerIds().contains(sendingPlayer.getPlayerId())) {
            Seat seat = gameState.getAbsoluteSeatForPlayer(sendingPlayer.getPlayerId());
            if (gameState.getTurn() == seat) {
                return gameState;
            }
        }
        return null; // player is not in this game, or it's not their turn
    }

    private void updateGameStateForAllPlayers(GameState gameState) {
        for (String playerId : gameState.determinePlayerIds()) {
            String userId = PlayerRepository.getInstance().getPlayerByPlayerId(playerId).getUserId();

            PlayerGameState playerGameState = GameStateToPlayerGameStateConverter.toPlayerGameStateForPlayer(playerId, gameState);
            webSocket.convertAndSendToUser(userId, "/topic/game", playerGameState);
        }
    }
}

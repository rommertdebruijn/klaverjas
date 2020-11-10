package com.keemerz.klaverjas.websocket;

import com.keemerz.klaverjas.converter.GameStateToPlayerGameStateConverter;
import com.keemerz.klaverjas.domain.*;
import com.keemerz.klaverjas.repository.ActiveGamesRepository;
import com.keemerz.klaverjas.repository.GameStateRepository;
import com.keemerz.klaverjas.repository.PlayerRepository;
import com.keemerz.klaverjas.websocket.inbound.GameJoinMessage;
import com.keemerz.klaverjas.websocket.inbound.GameLeaveMessage;
import com.keemerz.klaverjas.websocket.inbound.GameStartMessage;
import com.keemerz.klaverjas.websocket.inbound.PlayCardMessage;
import com.keemerz.klaverjas.websocket.outbound.ActiveGamesMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.List;

import static com.keemerz.klaverjas.domain.Suit.CLUBS;

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
        gameStateRepository.updateGameState(gameState);

        updateGameStateForAllPlayers(sendingPlayer.getPlayerId(), gameState);

        List<ActiveGame> activeGames = ActiveGamesRepository.getInstance().getActiveGames();
        webSocket.convertAndSend("/topic/lobby", new ActiveGamesMessage(activeGames));

    }

    @MessageMapping("/game/join")
    public void joinGame(GameJoinMessage message, Principal principal) {
        Player sendingPlayer = PlayerRepository.getInstance().getPlayerByUserId(principal.getName());

        GameState gameState = gameStateRepository.getGameState(message.getGameId());
        if (!gameState.determinePlayerIds().contains(sendingPlayer.getPlayerId())) {
            gameState.fillSeat(sendingPlayer);
            if (gameState.getPlayers().size() > 1) { // if 4th player joins, deal first hand
                gameState.dealHands();
                gameState.setBidding(Bidding.createFirstGameBidding()); // first game always clubs
            }
            gameStateRepository.updateGameState(gameState);
        }
        updateGameStateForAllPlayers(sendingPlayer.getPlayerId(), gameState);

        List<ActiveGame> activeGames = ActiveGamesRepository.getInstance().getActiveGames();
        webSocket.convertAndSend("/topic/lobby", new ActiveGamesMessage(activeGames));
    }

    @MessageMapping("/game/leave")
    public void leaveGame(GameLeaveMessage message, Principal principal) {
        Player sendingPlayer = PlayerRepository.getInstance().getPlayerByUserId(principal.getName());
        String sendingPlayerId = sendingPlayer.getPlayerId();

        GameState gameState = gameStateRepository.getGameState(message.getGameId());
        if (gameState.determinePlayerIds().contains(sendingPlayerId)) {
            gameStateRepository.removePlayerFromGames(sendingPlayer);

            updateGameStateForAllPlayers(sendingPlayerId, gameState);

            List<ActiveGame> activeGames = ActiveGamesRepository.getInstance().getActiveGames();
            webSocket.convertAndSend("/topic/lobby", new ActiveGamesMessage(activeGames));
        }
    }

    @MessageMapping("/game/playcard")
    public void playCard(PlayCardMessage message, Principal principal) {
        Player sendingPlayer = PlayerRepository.getInstance().getPlayerByUserId(principal.getName());

        // TODO only 1 card per turn for this player :)
        // TODO render currentTrick
        GameState gameState = gameStateRepository.getGameState(message.getGameId());
        if (gameState.determinePlayerIds().contains(sendingPlayer.getPlayerId())) {
            Seat seat = gameState.getAbsoluteSeatForPlayer(sendingPlayer.getPlayerId());
            List<Card> cards = gameState.getHands().get(seat);
            cards.stream()
                    .filter(card -> card.getCardId().equals(message.getCardId()))
                    .findFirst()
                    .ifPresent(card -> {
                        gameState.playCard(seat, message.getCardId());
                        gameStateRepository.updateGameState(gameState);

                        updateGameStateForAllPlayers(sendingPlayer.getPlayerId(), gameState);
                    });

        }
    }

    private void updateGameStateForAllPlayers(String sendingPlayerId, GameState gameState) {

        for (String playerId : gameState.determinePlayerIds()) {
            String userId = PlayerRepository.getInstance().getPlayerByPlayerId(playerId).getUserId();

            PlayerGameState playerGameState = GameStateToPlayerGameStateConverter.toPlayerGameStateForPlayer(playerId, gameState);
            webSocket.convertAndSendToUser(userId, "/topic/game", playerGameState);
        }
        if (!gameState.determinePlayerIds().contains(sendingPlayerId)) {
            String userId = PlayerRepository.getInstance().getPlayerByPlayerId(sendingPlayerId).getUserId();
            webSocket.convertAndSendToUser(userId, "/topic/game", PlayerGameState.playerLeftGameState(gameState.getGameId()));
        }
    }
}

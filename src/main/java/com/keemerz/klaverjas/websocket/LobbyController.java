package com.keemerz.klaverjas.websocket;

import com.keemerz.klaverjas.domain.ActiveGame;
import com.keemerz.klaverjas.domain.Player;
import com.keemerz.klaverjas.repository.ActiveGamesRepository;
import com.keemerz.klaverjas.repository.GameStateRepository;
import com.keemerz.klaverjas.repository.PlayerRepository;
import com.keemerz.klaverjas.websocket.inbound.GoodbyeMessage;
import com.keemerz.klaverjas.websocket.inbound.HelloMessage;
import com.keemerz.klaverjas.websocket.outbound.LobbyMessage;
import com.keemerz.klaverjas.websocket.outbound.PlayerJoinsLobbyMessage;
import com.keemerz.klaverjas.websocket.outbound.PlayerLeavesLobbyMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class LobbyController {

	private PlayerRepository playerRepository = PlayerRepository.getInstance();
	private ActiveGamesRepository activeGamesRepository = ActiveGamesRepository.getInstance();
	private GameStateRepository gameStateRepository = GameStateRepository.getInstance();

	@MessageMapping("/lobby/hello")
	@SendTo("/topic/lobby")
	public LobbyMessage helloMessage(HelloMessage message, Principal principal) {
		String userId = principal.getName(); // from Spring security
		playerRepository.setLoginTimestamp(userId, LocalDateTime.now());
		Player player = playerRepository.getPlayerByUserId(userId);

		List<Player> activePlayers = playerRepository.getActivePlayers();
		List<ActiveGame> activeGames = activeGamesRepository.getActiveGames();
		return new PlayerJoinsLobbyMessage(HtmlUtils.htmlEscape(player.getName()) + " komt binnen.", getPlayerNames(activePlayers), activeGames);
	}

	@MessageMapping("/lobby/goodbye")
	@SendTo("/topic/lobby")
	public LobbyMessage goodbyeMessage(GoodbyeMessage message, Principal principal) {
		String userId = principal.getName(); // from Spring security
		Player player = playerRepository.getPlayerByUserId(userId);

		playerRepository.removeLoginTimestamp(userId);
		gameStateRepository.removePlayerFromGames(player);

		List<Player> activePlayers = playerRepository.getActivePlayers();
		List<ActiveGame> activeGames = activeGamesRepository.getActiveGames();
		return new PlayerLeavesLobbyMessage(HtmlUtils.htmlEscape(player.getName()) + " is er vandoor.", getPlayerNames(activePlayers), activeGames);
	}

	private List<String> getPlayerNames(List<Player> players) {
		return players.stream()
				.map(Player::getName)
				.collect(Collectors.toList());
	}

}

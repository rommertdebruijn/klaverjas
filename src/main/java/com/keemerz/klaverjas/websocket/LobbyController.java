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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class LobbyController {

	private PlayerRepository playerRepository = PlayerRepository.getInstance();
	private ActiveGamesRepository activeGamesRepository = ActiveGamesRepository.getInstance();
	private GameStateRepository gameStateRepository = GameStateRepository.getInstance();

	private List<Player> playersInLobby = new ArrayList<>();

	@MessageMapping("/lobby/hello")
	@SendTo("/topic/lobby")
	public LobbyMessage helloMessage(HelloMessage message, Principal principal) {
		String userId = principal.getName(); // from Spring security
		Player player = playerRepository.getPlayerByUserId(userId);
		if (!playersInLobby.contains(player)) {
			playersInLobby.add(player);
		}

		List<ActiveGame> activeGames = activeGamesRepository.getActiveGames();
		return new PlayerJoinsLobbyMessage(HtmlUtils.htmlEscape(player.getName()) + " has joined the lobby.", getPlayerNames(), activeGames);
	}

	@MessageMapping("/lobby/goodbye")
	@SendTo("/topic/lobby")
	public LobbyMessage goodbyeMessage(GoodbyeMessage message, Principal principal) {
		String userId = principal.getName(); // from Spring security
		Player player = playerRepository.getPlayerByUserId(userId);
		playersInLobby.remove(player);

		gameStateRepository.removePlayerFromGames(player);
		List<ActiveGame> activeGames = activeGamesRepository.getActiveGames();
		return new PlayerLeavesLobbyMessage(HtmlUtils.htmlEscape(player.getName()) + " has left the lobby.", getPlayerNames(), activeGames);
	}

	private List<String> getPlayerNames() {
		return playersInLobby.stream()
				.map(Player::getName)
				.collect(Collectors.toList());
	}

}

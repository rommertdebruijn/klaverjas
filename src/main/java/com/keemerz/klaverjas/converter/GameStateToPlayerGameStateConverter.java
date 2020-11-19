package com.keemerz.klaverjas.converter;

import com.keemerz.klaverjas.comparator.CardInHandComparator;
import com.keemerz.klaverjas.domain.*;
import com.keemerz.klaverjas.websocket.ScoreCalculator;

import java.util.*;
import java.util.stream.Collectors;

import static com.keemerz.klaverjas.domain.Seat.*;

public class GameStateToPlayerGameStateConverter {

    public static PlayerGameState toPlayerGameStateForPlayer(String playerId, GameState gameState) {
        Seat currentPlayerSeat = gameState.getAbsoluteSeatForPlayer(playerId);

        Bidding rotatedBidding = null;
        if (gameState.getBidding() != null) {
            rotatedBidding = gameState.getBidding().rotateForSeat(currentPlayerSeat);
        }

        Trick rotatedTrick = null;
        if (gameState.getCurrentTrick() != null) {
            rotatedTrick = gameState.getCurrentTrick().rotateForSeat(currentPlayerSeat);
        }

        List<Card> modifiedHand = null;
        if (gameState.getHands().get(currentPlayerSeat) != null) {
            modifiedHand = modifyCardsForPlayer(currentPlayerSeat, gameState);
        }

        ComboPoints rotatedComboPoints = gameState.getComboPoints().rotateForSeat(currentPlayerSeat);


        List<Score> rotatedGameScores = gameState.getGameScores().stream()
                .map(gameScore -> gameScore.rotateForSeat(currentPlayerSeat))
                .collect(Collectors.toList());

        Score totalScore = ScoreCalculator.calculateMatchScore(gameState.getGameScores()).rotateForSeat(currentPlayerSeat);

        return new PlayerGameState(
                gameState.getGameId(),
                rotatedBidding,
                true,
                modifiedHand,
                rotatedTrick,
                buildPlayerNamesMap(gameState, currentPlayerSeat),
                buildCardsInHandMap(gameState, currentPlayerSeat),
                buildNrOfTricksMap(gameState, currentPlayerSeat),
                gameState.getTurn().rotateForSeat(currentPlayerSeat),
                gameState.getDealer().rotateForSeat(currentPlayerSeat),
                rotatedComboPoints,
                rotatedGameScores,
                totalScore,
                gameState.isPointsCounted()
        );
    }

    private static List<Card> modifyCardsForPlayer(Seat currentPlayerSeat, GameState gameState) {
        List<Card> modifiedHand;
        List<Card> hand = gameState.getHands().get(currentPlayerSeat);
        List<Card> playableCards = gameState.determinePlayableCards(currentPlayerSeat);
        modifiedHand = new ArrayList<>();
        for (Card card : hand) {
            if (playableCards.contains(card)) {
                modifiedHand.add(card); // including cardID
            } else {
                modifiedHand.add(Card.unplayableCopyOf(card.getSuit(), card.getRank()));
            }
        }

        Suit trumpForSorting = gameState.getBidding().getFinalTrump() != null ? gameState.getBidding().getFinalTrump() : gameState.getBidding().getProposedTrump();
        modifiedHand.sort(new CardInHandComparator(trumpForSorting));
        return modifiedHand;
    }

    private static Map<Seat, Integer> buildCardsInHandMap(GameState gameState, Seat currentPlayerSeat) {
        Map<Seat, Integer> cardsPerPlayer = new HashMap<>();
        cardsPerPlayer.put(SOUTH, getNumberOfCardsInHand(gameState, currentPlayerSeat));
        cardsPerPlayer.put(WEST, getNumberOfCardsInHand(gameState, currentPlayerSeat.getLeftHandPlayer()));
        cardsPerPlayer.put(NORTH, getNumberOfCardsInHand(gameState, currentPlayerSeat.getPartner()));
        cardsPerPlayer.put(EAST, getNumberOfCardsInHand(gameState, currentPlayerSeat.getRightHandPlayer()));
        return cardsPerPlayer;
    }

    private static Map<Seat, Integer> buildNrOfTricksMap(GameState gameState, Seat currentPlayerSeat) {
        Map<Seat, Integer> cardsPerPlayer = new HashMap<>();
        cardsPerPlayer.put(SOUTH, getNumberOfTricks(gameState, currentPlayerSeat));
        cardsPerPlayer.put(WEST, getNumberOfTricks(gameState, currentPlayerSeat.getLeftHandPlayer()));
        cardsPerPlayer.put(NORTH, getNumberOfTricks(gameState, currentPlayerSeat.getPartner()));
        cardsPerPlayer.put(EAST, getNumberOfTricks(gameState, currentPlayerSeat.getRightHandPlayer()));
        return cardsPerPlayer;
    }


    private static Map<Seat, String> buildPlayerNamesMap(GameState gameState, Seat currentPlayerSeat) {
        Map<Seat, String> playerNames = new HashMap<>();
        playerNames.put(SOUTH, getName(gameState, currentPlayerSeat));
        playerNames.put(WEST, getName(gameState, currentPlayerSeat.getLeftHandPlayer()));
        playerNames.put(NORTH, getName(gameState, currentPlayerSeat.getPartner()));
        playerNames.put(EAST, getName(gameState, currentPlayerSeat.getRightHandPlayer()));
        return playerNames;
    }

    private static String getName(GameState gameState, Seat seat) {
        return gameState.getPlayers().entrySet().stream()
                .filter(entry -> entry.getKey().equals(seat))
                .map(Map.Entry::getValue)
                .map(Player::getName)
                .findFirst()
                .orElse("");
    }

    private static int getNumberOfCardsInHand(GameState gameState, Seat seat) {
        return gameState.getHands().entrySet().stream()
                .filter(entry -> entry.getKey().equals(seat))
                .map(Map.Entry::getValue)
                .map(List::size)
                .findFirst()
                .orElse(0);
    }

    private static int getNumberOfTricks(GameState gameState, Seat seat) {
        if (gameState.getPreviousTricks() == null) {
            return 0;
        }

        return (int) gameState.getPreviousTricks().stream()
                .filter(trick -> trick.getTrickWinner() == seat)
                .count();
    }
}

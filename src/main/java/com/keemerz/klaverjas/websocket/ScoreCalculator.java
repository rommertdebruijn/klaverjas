package com.keemerz.klaverjas.websocket;

import com.keemerz.klaverjas.domain.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.keemerz.klaverjas.domain.Rank.*;
import static com.keemerz.klaverjas.domain.Team.EW;
import static com.keemerz.klaverjas.domain.Team.NS;

public class ScoreCalculator {

    private static final Map<Rank, Integer> NON_TRUMP_CARD_VALUES = new HashMap<>();
    private static final Map<Rank, Integer> TRUMP_CARD_VALUES = new HashMap<>();
    public static final int MAX_POINTS_162 = 162;

    static {
        NON_TRUMP_CARD_VALUES.put(JACK, 2);
        NON_TRUMP_CARD_VALUES.put(QUEEN, 3);
        NON_TRUMP_CARD_VALUES.put(KING, 4);
        NON_TRUMP_CARD_VALUES.put(TEN, 10);
        NON_TRUMP_CARD_VALUES.put(ACE, 11);

        TRUMP_CARD_VALUES.put(QUEEN, 3);
        TRUMP_CARD_VALUES.put(KING, 4);
        TRUMP_CARD_VALUES.put(TEN, 10);
        TRUMP_CARD_VALUES.put(ACE, 11);
        TRUMP_CARD_VALUES.put(NINE, 14);
        TRUMP_CARD_VALUES.put(JACK, 20);
    }

    public static GameScore calculateGameScore(Bidding bidding, List<Trick> tricks, ComboPoints comboPoints) {
        Suit trump = bidding.getFinalTrump();
        Team contractTeam = Team.forSeat(bidding.getFinalBidBy());

        int gameScoreForTeamNS = getGameScoreForTeam(NS, tricks, trump, comboPoints);
        int gameScoreForTeamEW = getGameScoreForTeam(EW, tricks, trump, comboPoints);

        GameScore gameScore = new GameScore();
        gameScore.getScores().put(NS, gameScoreForTeamNS);
        gameScore.getScores().put(EW, gameScoreForTeamEW);
        gameScore.getTableScores().put(NS, calculateTablePointsForTeam(NS, tricks, trump));
        gameScore.getTableScores().put(EW, calculateTablePointsForTeam(EW, tricks, trump));
        gameScore.getComboScores().put(NS, comboPoints.getComboPointsForTeam(NS));
        gameScore.getComboScores().put(EW, comboPoints.getComboPointsForTeam(EW));
        gameScore.getRemarks().put(NS, "");
        gameScore.getRemarks().put(EW, "");

        // Contract breach?
        if (gameScore.getScores().get(contractTeam) <= gameScore.getScores().get(contractTeam.opponentTeam())) {
            // Contract breach! All Combo points go to opponent!
            int allPoints = MAX_POINTS_162 + comboPoints.getComboPoints().values().stream().reduce(0, Integer::sum);
            gameScore.getScores().put(contractTeam, 0);
            gameScore.getScores().put(contractTeam.opponentTeam(), allPoints);
            gameScore.getRemarks().put(contractTeam, "NAT");
        }

        // Pit?
        if (getTricksForTeam(contractTeam.opponentTeam(), tricks).isEmpty()) {
            // opponent did not get any tricks! Add 100 bonus points!
            gameScore.getScores().put(contractTeam, gameScore.getScores().get(contractTeam) + 100);
            gameScore.getRemarks().put(contractTeam.opponentTeam(), "PIT");
        }
        return gameScore;
    }

    private static int getGameScoreForTeam(Team team, List<Trick> tricks, Suit trump, ComboPoints comboPoints) {
        int gameScoreForTeam = calculateTablePointsForTeam(team, tricks, trump);

        // add comboPoints
        gameScoreForTeam += comboPoints.getComboPointsForTeam(team);
        return gameScoreForTeam;
    }

    private static int calculateTablePointsForTeam(Team team, List<Trick> tricks, Suit trump) {
        int gameScoreForTeam = 0;

        // last trick is worth 10 points!
        Seat lastTrickWinner = tricks.get(tricks.size()-1).getTrickWinner();
        if (team == Team.forSeat(lastTrickWinner)) {
            gameScoreForTeam += 10;
        }

        // All cards in tricks are worth points
        List<Trick> tricksForTeam = getTricksForTeam(team, tricks);
        gameScoreForTeam += tricksForTeam.stream()
                .map(trick -> trick.getCardsPlayed().values())
                .flatMap(Collection::stream)
                .map(card -> {
                    return ScoreCalculator.getCardValue(card, trump);
                })
                .reduce(0, Integer::sum);
        return gameScoreForTeam;
    }

    private static List<Trick> getTricksForTeam(Team team, List<Trick> tricks) {
        return tricks.stream()
                .filter(trick -> Team.forSeat(trick.getTrickWinner()) == team)
                .collect(Collectors.toList());
    }

    public static MatchScore calculateMatchScore(List<GameScore> gameScores) {
        int totalScoreNS = gameScores.stream()
                .map(score -> score.getScores().get(NS))
                .reduce(0, Integer::sum);

        int totalScoreEW = gameScores.stream()
                .map(score -> score.getScores().get(EW))
                .reduce(0, Integer::sum);

        return new MatchScore(totalScoreNS, totalScoreEW);
    }

    private static int getCardValue(Card card, Suit trump) {
        Map<Rank, Integer> pointsMap = card.getSuit() == trump ? TRUMP_CARD_VALUES : NON_TRUMP_CARD_VALUES;
        if (pointsMap.get(card.getRank()) != null) {
            return pointsMap.get(card.getRank());
        }
        return 0;
    }

}

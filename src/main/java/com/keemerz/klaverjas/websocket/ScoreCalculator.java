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

    public static Score calculateGameScore(Bidding bidding, List<Trick> tricks, ComboPoints comboPoints) {
        Suit trump = bidding.getFinalTrump();
        Team contractTeam = Team.forSeat(bidding.getFinalBidBy());

        Score score = new Score();
        score.getScores().put(NS, getGameScoreForTeam(NS, tricks, trump, comboPoints));
        score.getScores().put(EW, getGameScoreForTeam(EW, tricks, trump, comboPoints));
        score.getRemarks().put(NS, "");
        score.getRemarks().put(EW, "");

        // Contract breach?
        if (score.getScores().get(contractTeam) <= score.getScores().get(contractTeam.opponentTeam())) {
            // Contract breach! All Combo points go to opponent!
            int allPoints = MAX_POINTS_162 + comboPoints.getComboPoints().values().stream().reduce(0, Integer::sum);
            score.getScores().put(contractTeam, 0);
            score.getScores().put(contractTeam.opponentTeam(), allPoints);
            score.getRemarks().put(contractTeam, "NAT");
        }

        // Pit?
        if (getTricksForTeam(contractTeam.opponentTeam(), tricks).isEmpty()) {
            // opponent did not get any tricks! Add 100 bonus points!
            score.getScores().put(contractTeam, score.getScores().get(contractTeam) + 100);
            score.getRemarks().put(contractTeam.opponentTeam(), "PIT");
        }
        return score;
    }

    private static int getGameScoreForTeam(Team team, List<Trick> tricks, Suit trump, ComboPoints comboPoints) {
        List<Trick> tricksForTeam = getTricksForTeam(team, tricks);

        Integer cardPoints = tricksForTeam.stream()
                .map(trick -> trick.getCardsPlayed().values())
                .flatMap(Collection::stream)
                .map(card -> {
                    return ScoreCalculator.getCardValue(card, trump);
                })
                .reduce(0, Integer::sum);

        Integer comboBonus = comboPoints.getComboPoints().get(team) == null ? 0 : comboPoints.getComboPoints().get(team);
        return cardPoints + comboBonus;
    }

    private static List<Trick> getTricksForTeam(Team team, List<Trick> tricks) {
        return tricks.stream()
                .filter(trick -> Team.forSeat(trick.getTrickWinner()) == team)
                .collect(Collectors.toList());
    }

    public static Score calculateMatchScore(List<Score> scores) {
        int totalScoreNS = scores.stream()
                .map(score -> score.getScores().get(NS))
                .reduce(0, Integer::sum);

        int totalScoreEW = scores.stream()
                .map(score -> score.getScores().get(EW))
                .reduce(0, Integer::sum);

        String remarkNS = totalScoreNS > totalScoreEW ? "WINNAAR" : "";
        String remarkEW = totalScoreEW > totalScoreNS ? "WINNAAR" : "";

        return new Score(totalScoreNS, totalScoreEW, remarkNS, remarkEW);
    }

    private static int getCardValue(Card card, Suit trump) {
        Map<Rank, Integer> pointsMap = card.getSuit() == trump ? TRUMP_CARD_VALUES : NON_TRUMP_CARD_VALUES;
        if (pointsMap.get(card.getRank()) != null) {
            return pointsMap.get(card.getRank());
        }
        return 0;
    }

}

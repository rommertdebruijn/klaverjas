package com.keemerz.klaverjas.domain;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.keemerz.klaverjas.domain.Team.*;

public class GameScore {

    private final Map<Team, Integer> scores = new HashMap<>();
    private final Map<Team, Integer> tableScores = new HashMap<>();
    private final Map<Team, Integer> comboScores = new HashMap<>();
    private final Map<Team, String> remarks = new HashMap<>();

    public GameScore() {
    }

    public GameScore(int scoreNS, int scoreEW, int tableScoresNS, int tableScoresEW, int comboScoresNS, int comboScoresEW, String remarkNS, String remarkEW) {
        scores.put(NS, scoreNS);
        scores.put(EW, scoreEW);
        tableScores.put(NS, tableScoresNS);
        tableScores.put(EW, tableScoresEW);
        comboScores.put(NS, comboScoresNS);
        comboScores.put(EW, comboScoresEW);
        remarks.put(NS, remarkNS);
        remarks.put(EW, remarkEW);
    }

    public Map<Team, Integer> getScores() {
        return scores;
    }

    public Map<Team, Integer> getTableScores() {
        return tableScores;
    }

    public Map<Team, Integer> getComboScores() {
        return comboScores;
    }

    public Map<Team, String> getRemarks() {
        return remarks;
    }

    public GameScore rotateForSeat(Seat seat) {
        if (Team.forSeat(seat) == EW) {
            return new GameScore(
                    this.getScores().get(EW), this.getScores().get(NS),
                    this.getTableScores().get(EW), this.getTableScores().get(NS),
                    this.getComboScores().get(EW), this.getComboScores().get(NS),
                    this.getRemarks().get(EW), this.getRemarks().get(NS));
        }
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameScore gameScore = (GameScore) o;
        return Objects.equals(scores, gameScore.scores) &&
                Objects.equals(tableScores, gameScore.tableScores) &&
                Objects.equals(comboScores, gameScore.comboScores) &&
                Objects.equals(remarks, gameScore.remarks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(scores, tableScores, comboScores, remarks);
    }

    @Override
    public String toString() {
        return "Score{" +
                "scores=" + scores +
                ", tableScores=" + tableScores +
                ", comboScores=" + comboScores +
                ", remarks=" + remarks +
                '}';
    }
}

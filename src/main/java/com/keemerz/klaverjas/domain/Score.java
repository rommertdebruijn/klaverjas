package com.keemerz.klaverjas.domain;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.keemerz.klaverjas.domain.Team.*;

public class Score {

    Map<Team, Integer> scores = new HashMap<>();
    Map<Team, String> remarks = new HashMap<>();

    public Score() {
    }

    public Score(int scoreNS, int scoreEW, String remarkNS, String remarkEW) {
        scores.put(NS, scoreNS);
        scores.put(EW, scoreEW);
        remarks.put(NS, remarkNS);
        remarks.put(EW, remarkEW);
    }

    public Map<Team, Integer> getScores() {
        return scores;
    }

    public Map<Team, String> getRemarks() {
        return remarks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Score score = (Score) o;
        return Objects.equals(scores, score.scores) &&
                Objects.equals(remarks, score.remarks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(scores, remarks);
    }

    @Override
    public String toString() {
        return "Score{" +
                "scores=" + scores +
                ", remarks=" + remarks +
                '}';
    }
}

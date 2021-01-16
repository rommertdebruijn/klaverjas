package com.keemerz.klaverjas.domain;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.keemerz.klaverjas.domain.Team.EW;
import static com.keemerz.klaverjas.domain.Team.NS;

public class MatchScore {
    private Map<Team, Integer> scores = new HashMap<>();

    public MatchScore() {
    }

    public MatchScore(int scoreNS, int scoreEW) {
        scores.put(NS, scoreNS);
        scores.put(EW, scoreEW);
    }

    public Map<Team, Integer> getScores() {
        return scores;
    }

    public MatchScore rotateForSeat(Seat seat) {
        if (Team.forSeat(seat) == EW) {
            return new MatchScore(this.getScores().get(EW), this.getScores().get(NS));
        }
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MatchScore that = (MatchScore) o;
        return Objects.equals(scores, that.scores);
    }

    @Override
    public int hashCode() {
        return Objects.hash(scores);
    }

    @Override
    public String toString() {
        return "MatchScore{" +
                "scores=" + scores +
                '}';
    }
}

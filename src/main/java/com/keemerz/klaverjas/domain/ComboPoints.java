package com.keemerz.klaverjas.domain;

import java.util.HashMap;
import java.util.Map;

import static com.keemerz.klaverjas.domain.Team.EW;
import static com.keemerz.klaverjas.domain.Team.NS;

public class ComboPoints {

    Map<Team, Integer> comboPoints = new HashMap<>();

    public ComboPoints(int comboPointsNS, int comboPointsEW) {
        comboPoints.put(NS, comboPointsNS);
        comboPoints.put(EW, comboPointsEW);
    }

    public void claimFor(Team team, int nrOfComboPoints) {
        if (team == NS) {
            comboPoints.put(NS, comboPoints.get(NS) + nrOfComboPoints);
        } else {
            comboPoints.put(EW, comboPoints.get(EW) + nrOfComboPoints);
        }
    }

    public Map<Team, Integer> getComboPoints() {
        return comboPoints;
    }

    public ComboPoints rotateForSeat(Seat seat) {
        if (Team.forSeat(seat) == EW) {
            return new ComboPoints(comboPoints.get(EW), comboPoints.get(NS));
        }
        return this;
    }
}

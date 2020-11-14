package com.keemerz.klaverjas.domain;

import static com.keemerz.klaverjas.domain.Seat.EAST;
import static com.keemerz.klaverjas.domain.Seat.WEST;

public class ComboPoints {

    private int comboPointsNS = 0;
    private int comboPointsEW = 0;

    public ComboPoints(int comboPointsNS, int comboPointsEW) {
        this.comboPointsNS = comboPointsNS;
        this.comboPointsEW = comboPointsEW;
    }

    public void claimFor(Seat seat, int nrOfComboPoints) {
        switch (seat) {
            case NORTH:
            case SOUTH:
                comboPointsNS += nrOfComboPoints;
                break;
            case EAST:
            case WEST:
                comboPointsEW += nrOfComboPoints;
        }
    }

    public int getComboPointsNS() {
        return comboPointsNS;
    }

    public int getComboPointsEW() {
        return comboPointsEW;
    }

    public ComboPoints rotateForSeat(Seat seat) {
        if (seat == EAST || seat == WEST) {
            return new ComboPoints(comboPointsEW, comboPointsNS);
        }
        return this;
    }
}

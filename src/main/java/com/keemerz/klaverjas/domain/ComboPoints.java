package com.keemerz.klaverjas.domain;

public class ComboPoints {

    private int comboPointsNS = 0;
    private int comboPointsEW = 0;

    public void claimFor(Seat seat, int nrOfComboPoints) {
        switch (seat) {
            case NORTH:
            case SOUTH:
                comboPointsNS += nrOfComboPoints;
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
}

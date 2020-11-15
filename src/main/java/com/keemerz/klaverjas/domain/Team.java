package com.keemerz.klaverjas.domain;

import static com.keemerz.klaverjas.domain.Seat.NORTH;
import static com.keemerz.klaverjas.domain.Seat.SOUTH;

public enum Team {
    NS,
    EW;

    public static Team forSeat(Seat seat) {
        if (seat == NORTH || seat == SOUTH) {
            return NS;
        }
        return EW;
    }

    public Team opponentTeam() {
        if (this == NS) {
            return EW;
        }
        return NS;
    }
}

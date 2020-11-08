package com.keemerz.klaverjas.domain;

public enum Team {
    NS,
    OW;

    public static Team forSeat(Seat seat) {
        switch (seat) {
            case NORTH:
            case SOUTH:
                return NS;
            case EAST:
            case WEST:
                return OW;
            default:
                throw new IllegalArgumentException("No team for seat " + seat);
        }
    }
}

package com.keemerz.klaverjas.domain;

import java.util.Arrays;
import java.util.List;

public enum Seat {
    NORTH,
    EAST,
    SOUTH,
    WEST;

    public Seat getLeftHandPlayer() {
        List<Seat> seats = Arrays.asList(values());
        int currentPlayerIndex = seats.indexOf(this);
        int leftHandPlayerIndex = (4 + (currentPlayerIndex + 1)) % seats.size();
        return seats.get(leftHandPlayerIndex);
    }

    public Seat getRightHandPlayer() {
        List<Seat> seats = Arrays.asList(values());
        int currentPlayerIndex = seats.indexOf(this);
        int rightHandPlayerIndex = (4 + (currentPlayerIndex - 1)) % seats.size();
        return seats.get(rightHandPlayerIndex);
    }

    public Seat getPartner() {
        List<Seat> seats = Arrays.asList(values());
        int currentPlayerIndex = seats.indexOf(this);
        int partnerIndex = (4 + (currentPlayerIndex - 2)) % seats.size();
        return seats.get(partnerIndex);
    }

    public Seat rotateForSeat(Seat currentPlayerSeat) {
        if (this == currentPlayerSeat.getLeftHandPlayer()) {
            return WEST;
        }
        if (this == currentPlayerSeat.getRightHandPlayer()) {
            return EAST;
        }
        if (this == currentPlayerSeat.getPartner()) {
            return NORTH;
        }
        return SOUTH;
    }
}

package com.keemerz.klaverjas.domain;

import java.util.Arrays;

public enum Suit {
    SPADES,
    HEARTS,
    DIAMONDS,
    CLUBS;

    public String getAbbreviation() {
        return this.name().substring(0,1).toUpperCase();
    }

    public static Suit fromAbbreviation(String abbreviation) {
        return Arrays.stream(values())
                .filter(suit -> suit.getAbbreviation().equals(abbreviation))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}

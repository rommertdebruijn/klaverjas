package com.keemerz.klaverjas.domain;

import java.util.Arrays;

public enum Rank {
    SEVEN("7"),
    EIGHT("8"),
    NINE("9"),
    TEN("T"),
    JACK("J"),
    QUEEN("Q"),
    KING("K"),
    ACE("A");

    private String abbreviation;

    Rank(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public static Rank fromAbbreviation(String abbreviation) {
        return Arrays.stream(values())
                .filter(rank -> rank.getAbbreviation().equals(abbreviation))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}

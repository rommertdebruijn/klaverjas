package com.keemerz.klaverjas.domain;

import java.util.List;

public interface Deck {
    List<Card> getCards();
    String getSignature();
}

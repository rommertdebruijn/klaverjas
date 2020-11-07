package com.keemerz.klaverjas.domain;

import java.util.List;
import java.util.Map;

public class TestGameStateBuilder {

    private String gameId;
    private Map<Seat, List<Card>> hands;
    private Map<Seat, String> players;
    private List<Trick> previousTricks;
    private Trick currentTrick;
    private Seat turn;
}

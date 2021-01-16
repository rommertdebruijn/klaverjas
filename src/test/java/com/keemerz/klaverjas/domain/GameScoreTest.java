package com.keemerz.klaverjas.domain;

import org.junit.jupiter.api.Test;

import static com.keemerz.klaverjas.domain.Seat.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;

class GameScoreTest {

    @Test
    public void rotateForEW() {
        GameScore gameScore = new GameScore(121, 41, 121, 41, 0, 0, "remark for NS", "remark for EW");

        assertThat(gameScore.rotateForSeat(EAST), is(new GameScore(41, 121, 41, 121, 0, 0, "remark for EW", "remark for NS")));
        assertThat(gameScore.rotateForSeat(WEST), is(new GameScore(41, 121, 41, 121, 0, 0, "remark for EW", "remark for NS")));
    }

    @Test
    public void rotateForNS() {
        GameScore gameScore = new GameScore(121, 41, 121, 41, 0, 0, "remark for NS", "remark for EW");

        assertThat(gameScore.rotateForSeat(NORTH), is(new GameScore(121, 41, 121, 41, 0, 0, "remark for NS", "remark for EW")));
        assertThat(gameScore.rotateForSeat(SOUTH), is(new GameScore(121, 41, 121, 41, 0, 0, "remark for NS", "remark for EW")));
    }

    @Test
    public void rotateContractBreachForNS() {
        GameScore gameScore = new GameScore(0, 182, 80, 82, 0, 20, "NAT", "");

        assertThat(gameScore.rotateForSeat(NORTH), is(new GameScore(0, 182, 80, 82, 0, 20, "NAT", "")));
        assertThat(gameScore.rotateForSeat(SOUTH), is(new GameScore(0, 182, 80, 82, 0, 20, "NAT", "")));
    }

    @Test
    public void rotateContractBreachForEW() {
        GameScore gameScore = new GameScore(0, 182, 80, 82, 0, 20, "NAT", "");

        assertThat(gameScore.rotateForSeat(EAST), is(new GameScore(182, 0, 82, 80, 20, 0, "", "NAT")));
        assertThat(gameScore.rotateForSeat(WEST), is(new GameScore(182, 0, 82, 80, 20, 0, "", "NAT")));
    }

}
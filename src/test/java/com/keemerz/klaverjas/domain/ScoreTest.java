package com.keemerz.klaverjas.domain;

import org.junit.jupiter.api.Test;

import static com.keemerz.klaverjas.domain.Seat.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;

class ScoreTest {

    @Test
    public void rotateForEW() {
        Score score = new Score(121, 41, "remark for NS", "remark for EW");

        assertThat(score.rotateForSeat(EAST), is(new Score(41, 121, "remark for EW", "remark for NS")));
        assertThat(score.rotateForSeat(WEST), is(new Score(41, 121, "remark for EW", "remark for NS")));
    }

    @Test
    public void rotateForNS() {
        Score score = new Score(121, 41, "remark for NS", "remark for EW");

        assertThat(score.rotateForSeat(NORTH), is(new Score(121, 41, "remark for NS", "remark for EW")));
        assertThat(score.rotateForSeat(SOUTH), is(new Score(121, 41, "remark for NS", "remark for EW")));
    }

}
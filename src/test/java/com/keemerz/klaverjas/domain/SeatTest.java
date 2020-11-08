package com.keemerz.klaverjas.domain;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static com.keemerz.klaverjas.domain.Seat.*;

class SeatTest {

    @Test
    public void leftPlayerShouldBeClockwise() {
        assertThat(NORTH.getLeftHandPlayer(), is(EAST));
        assertThat(EAST.getLeftHandPlayer(), is(SOUTH));
        assertThat(SOUTH.getLeftHandPlayer(), is(WEST));
        assertThat(WEST.getLeftHandPlayer(), is(NORTH));
    }

    @Test
    public void rightPlayerShouldBeCounterClockwise() {
        assertThat(NORTH.getRightHandPlayer(), is(WEST));
        assertThat(EAST.getRightHandPlayer(), is(NORTH));
        assertThat(SOUTH.getRightHandPlayer(), is(EAST));
        assertThat(WEST.getRightHandPlayer(), is(SOUTH));
    }

    @Test
    public void partnerShouldBeOpposite() {
        assertThat(NORTH.getPartner(), is(SOUTH));
        assertThat(EAST.getPartner(), is(WEST));
        assertThat(SOUTH.getPartner(), is(NORTH));
        assertThat(WEST.getPartner(), is(EAST));
    }

    @Test
    public void relativeSeatForCurrentPlayer() {
        assertThat(NORTH.rotateForSeat(SOUTH), is(NORTH));
        assertThat(NORTH.rotateForSeat(EAST), is(EAST));
        assertThat(NORTH.rotateForSeat(NORTH), is(SOUTH));
        assertThat(NORTH.rotateForSeat(WEST), is(WEST));

        assertThat(EAST.rotateForSeat(SOUTH), is(EAST));
        assertThat(EAST.rotateForSeat(EAST), is(SOUTH));
        assertThat(EAST.rotateForSeat(NORTH), is(WEST));
        assertThat(EAST.rotateForSeat(WEST), is(NORTH));

        assertThat(SOUTH.rotateForSeat(SOUTH), is(SOUTH));
        assertThat(SOUTH.rotateForSeat(EAST), is(WEST));
        assertThat(SOUTH.rotateForSeat(NORTH), is(NORTH));
        assertThat(SOUTH.rotateForSeat(WEST), is(EAST));

        assertThat(WEST.rotateForSeat(SOUTH), is(WEST));
        assertThat(WEST.rotateForSeat(EAST), is(NORTH));
        assertThat(WEST.rotateForSeat(NORTH), is(EAST));
        assertThat(WEST.rotateForSeat(WEST), is(SOUTH));
    }

}
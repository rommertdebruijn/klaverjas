package com.keemerz.klaverjas.domain;

import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

class BiddingFactoryTest {

    @Test
    void debugSuitShouldBeNull() {
        assertThat(BiddingFactory.DEBUG_SUIT, nullValue());
    }

}
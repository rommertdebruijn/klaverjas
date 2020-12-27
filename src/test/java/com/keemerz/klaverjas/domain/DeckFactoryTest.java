package com.keemerz.klaverjas.domain;

import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

class DeckFactoryTest {

    @Test
    void debugDeckSignatureShouldBeNull() {
        assertThat(DeckFactory.DEBUG_DECK_SIGNATURE, nullValue());
    }

}
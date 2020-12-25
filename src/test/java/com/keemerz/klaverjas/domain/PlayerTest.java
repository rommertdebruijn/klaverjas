package com.keemerz.klaverjas.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static com.keemerz.klaverjas.domain.Player.MAX_IDLE_TIME_IN_SECONDS;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

class PlayerTest {

    @Test
    public void playerWithStaleLoginTimestampShouldNotBeConsideredActive() {
        Player player = new TestPlayerBuilder().build();
        player.setLoginTimestamp(LocalDateTime.now().minusSeconds(MAX_IDLE_TIME_IN_SECONDS).minusSeconds(1));
        assertThat(player.isActive(), is(false));
    }

    @Test
    public void playerWithNoLoginTimestampShouldNotBeConsideredActive() {
        Player player = new TestPlayerBuilder().build();
        player.setLoginTimestamp(null);
        assertThat(player.isActive(), is(false));
    }

    @Test
    public void playerWithRecentLoginTimestampShouldBeConsideredActive() {
        Player player = new TestPlayerBuilder().build();
        player.setLoginTimestamp(LocalDateTime.now().minusSeconds(MAX_IDLE_TIME_IN_SECONDS).plusSeconds(1));
        assertThat(player.isActive(), is(true));
    }

}
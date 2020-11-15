package com.keemerz.klaverjas.converter;

import com.keemerz.klaverjas.domain.ActiveGame;
import com.keemerz.klaverjas.domain.GameState;
import com.keemerz.klaverjas.domain.Player;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;


class GameStateToActiveGameConverterTest {

    @Test
    public void FullGameShouldContainPlayersButNoGameId() {
        GameState input = new GameState("someGameId");
        input.fillSeat(new Player("user1", UUID.randomUUID().toString(), "Name1"));
        input.fillSeat(new Player("user2", UUID.randomUUID().toString(), "Name2"));
        input.fillSeat(new Player("user3", UUID.randomUUID().toString(), "Name3"));
        input.fillSeat(new Player("user4", UUID.randomUUID().toString(), "Name4"));

        ActiveGame output = GameStateToActiveGameConverter.toActiveGame(input);

        assertThat(output.getGameId(), is("someGameId"));
        assertThat(output.getPlayerNames(), is(Arrays.asList("Name1", "Name2", "Name3", "Name4")));
    }

    @Test
    public void partlyFilledGameShouldContainPlayersAndGameId() {
        GameState input = new GameState("someGameId");
        input.fillSeat(new Player("user1", UUID.randomUUID().toString(), "Name1"));
        input.fillSeat(new Player("user2", UUID.randomUUID().toString(), "Name2"));

        ActiveGame output = GameStateToActiveGameConverter.toActiveGame(input);

        assertThat(output.getGameId(), is("someGameId"));
        assertThat(output.getPlayerNames(), is(Arrays.asList("Name1", "Name2")));
    }

}
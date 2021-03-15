package com.mortonstudios.fictionalmatch.multiplayer.repo;

import com.mortonstudios.fictionalmatch.multiplayer.entities.Player;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class GameManagerTest {

    private final GameManager gameManager = GameManager.getInstance();

    @Test
    void contextLoads() {
        assertThat(gameManager).isNotNull();
    }

    @Test
    void endTurnIfBothPlayersOnTheSameTurn() {
        Map<String, Player> testRepo = new HashMap<>();
        testRepo.put("Player1", new Player());
        testRepo.put("Player2", new Player());

        gameManager.setRepository(testRepo);

        assertThat(GameManager.getInstance().endTurn()).isTrue();
    }

    @Test
    void doNotEndTurnIfBothPlayersOnTheDifferentTurns() {
        Map<String, Player> testRepo = new HashMap<>();
        testRepo.put("Player1", new Player());
        testRepo.put("Player2", new Player());

        testRepo.get("Player1").incrementTurn();

        gameManager.setRepository(testRepo);

        assertThat(GameManager.getInstance().endTurn()).isFalse();
    }


}
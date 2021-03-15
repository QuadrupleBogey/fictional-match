package com.mortonstudios.fictionalmatch.controllers;

import com.mortonstudios.fictionalmatch.multiplayer.entities.Player;
import com.mortonstudios.fictionalmatch.multiplayer.repo.GameManager;

import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Handlers for managing game state. This includes saving, loading and ending games
 *
 * @author Cam
 * @since 1.0.0
 */
@RestController
@RequestMapping("actions")
public class GameActions {

    private final GameManager repository = GameManager.getInstance();

    /**
     * @return all the metrics from the current game
     */
    @GetMapping("/end")
    public JSONObject getGameMetrics() {
        // post out meta data to web app
        // clear all other information
        return repository.getAllMetrics();
    }

    /**
     * This is subscription based
     * @return the game information
     */
    @GetMapping("/save")
    public Map<String, Player> saveGame() {
        return repository.getRepository();
    }

    /**
     * @param gameInfo load game information that
     * @return the loaded game information
     */
    @PostMapping("/load")
    public Map<String, Player> loadGame(@RequestBody final Map<String, Player> gameInfo) {
        repository.setRepository(gameInfo);
        return repository.getRepository();
    }

}

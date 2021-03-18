package com.mortonstudios.fictionalmatch.controllers;

import java.util.*;

import com.mortonstudios.fictionalmatch.multiplayer.entities.Player;
import com.mortonstudios.fictionalmatch.multiplayer.entities.SharedAnchor;
import com.mortonstudios.fictionalmatch.multiplayer.entities.Unit;
import com.mortonstudios.fictionalmatch.multiplayer.repo.GameManager;
import com.mortonstudios.fictionalmatch.utils.exceptions.NoAnchorFoundException;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Subscription based updates to players armies and their positions.
 * In order to do this it makes use of {@link SharedAnchor} entities
 * to render a model in a "real world" position for their opponent.
 *
 * This is a Controller that utilises websockets to keep players
 * up to date with out them having to poll the server.
 *
 * @author Cam
 * @since 1.0.0
 */
@Controller
public class SharedAnchors {

    final private GameManager repository = GameManager.getInstance();

    /**
     * @return all of the current game information
     */
    @SubscribeMapping("/all")
    public Map<String, Player> getAll() {
        return this.repository.getRepository();
    }

    /**
     * Returns all of the selected players unit positions.
     *
     * @param player uuid of the player in the game
     * @return returns all of their {@link Unit} information
     */
    @MessageMapping("/all/{player}/positions")
    @SendTo("/topics/{player}/positions")
    public Map<String, Unit> getAllPlayersPositions(@DestinationVariable final String player) {
        try {
            Player playerObj = repository.getRepository().get(player);
            if (playerObj != null) {
                return playerObj.getArmy();
            } else {
                throw new NoAnchorFoundException(player);
            }
        } catch (final NoAnchorFoundException exception) {
            exception.printStackTrace();
        }
        return new HashMap<>();
    }

    /**
     * Adds a new player into the game, accessible from the all subscription
     *
     * @param player uuid of the player
     * @param playObj all of the associated player information
     * @return The updated repository
     */
    @MessageMapping("/join-as/{player}")
    @SendTo("/topics/all")
    public Map<String, Player> newPlayer(@DestinationVariable String player, final Player playObj) {
        repository.getRepository().put(player, playObj);
        return repository.getRepository();
    }

    /**
     * Updates the position of a certain unit with in a players army
     *
     * @param playerID uuid of the {@link Player}
     * @param unitID uuid of the {@link Unit}
     * @param newUnit an updated {@link Unit}
     * @return The updated repository
     */
    @MessageMapping("/{playerID}/update/{unitID}")
    @SendTo("/topics/all")
    public Map<String, Player> editAnchor(@DestinationVariable final String playerID,
                           @DestinationVariable final String unitID,
                           Unit newUnit
    ) {
        System.out.println("PlayerID" + playerID);
        System.out.println("UnitID" + unitID);
        repository.getRepository().get(playerID).getArmy().replace(unitID, newUnit);
        return repository.getRepository();
    }

    /**
     * Declares that certain unit in a players army has been destroyed
     *
     * @param player who is model has been destroyed
     * @param id position in the index of the model
     * @return The updated repository
     */
    @DeleteMapping("/{player}/destroy/{id}")
    @SendTo("/topics/all")
    public Map<String, Player> destroyUnit(@PathVariable final String player, @PathVariable final String id) {
        repository.getRepository().get(player).getArmy().get(id).setDestroyed(true);
        return repository.getRepository();
    }

}

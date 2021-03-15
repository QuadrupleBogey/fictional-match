package com.mortonstudios.fictionalmatch.controllers;

import java.util.*;

import com.mortonstudios.fictionalmatch.multiplayer.entities.Player;
import com.mortonstudios.fictionalmatch.multiplayer.entities.SharedAnchor;
import com.mortonstudios.fictionalmatch.multiplayer.entities.Unit;
import com.mortonstudios.fictionalmatch.multiplayer.repo.GameManager;
import com.mortonstudios.fictionalmatch.utils.exceptions.NoAnchorFoundException;

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
 * Subscription based updates
 *
 * @author Cam
 * @since 1.0.0
 */
@Controller
public class SharedAnchors {

    final private GameManager repository = GameManager.getInstance();

    @SubscribeMapping("/all")
    public Map<String, Player> getAll() {
        return this.repository.getRepository();
    }

//    @GetMapping("/{player}/all")
    public List<Unit> getAllPlayersPositions(@PathVariable final String player) {
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
        return new ArrayList<>();
    }

    // Returns the players UUID, which is essentially who they are to the match
    @MessageMapping("/newPlayer")
    @SendTo("/topics/newPlayers")
    public String newPlayer(final Player playObj) {
        System.out.println("Player: " + playObj.toString());
        String player = UUID.randomUUID().toString();
        System.out.println("PlayerID: " + player);
        repository.getRepository().put(player, playObj);
        return player;
    }

//    @MessageMapping("/game")
//    @SendTo("/topics/players")
//    @PutMapping("/{player}/updateModelPosition/{id}")
    public SharedAnchor editAnchor(
        @RequestBody final Unit newUnit, @PathVariable final String player,
        @PathVariable final String id
    ) {
        repository.getRepository().get(player).getArmy().add(newUnit);
        return newUnit;
    }

    /**
     * @param player who is model has been destroyed
     * @param id position in the index of the model
     */
    @DeleteMapping("/{player}/{id}")
    public void deleteEmployee(@PathVariable final String player, @PathVariable final int id) {
        repository.getRepository().get(player).getArmy().get(id).setDestroyed(true);
    }

}

package com.mortonstudios.fictionalmatch.controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.mortonstudios.fictionalmatch.multiplayer.entities.Player;
import com.mortonstudios.fictionalmatch.multiplayer.entities.SharedAnchor;
import com.mortonstudios.fictionalmatch.utils.exceptions.NoAnchorFoundException;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
public class SharedAnchors {

    private Map<String, Player> repository = new HashMap<>();

    @GetMapping("/{player}/all")
    public Map<String, SharedAnchor> getAllPlayersPositions(@PathVariable final String player) {
        try {
            Player playerObj = repository.get(player);
            if (playerObj != null) {
                return playerObj.getArmy();
            } else {
                throw new NoAnchorFoundException(player);
            }
        } catch (final NoAnchorFoundException exception) {
            exception.getMessage();
        }
        return new HashMap<>();
    }

    // Returns the players UUID, which is essentially who they are to the match
    @PostMapping("/newPlayer")
    public String newPlayer(@RequestBody final Player playObj) {
        String player = UUID.randomUUID().toString();
        repository.put(player, playObj);
        return player;
    }

    @PutMapping("/{player}/updateModelPosition/{id}")
    public SharedAnchor editAnchor(
        @RequestBody final SharedAnchor newAnchor, @PathVariable final String player,
        @PathVariable final String id
    ) {
        repository.get(player).getArmy().put(id, newAnchor);
        return newAnchor;
    }

    @DeleteMapping("/{player}/{id}")
    public void deleteEmployee(@PathVariable final String player, @PathVariable final String id) {
        repository.get(player).getArmy().remove(id);
    }

}

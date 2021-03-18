package com.mortonstudios.fictionalmatch.multiplayer.repo;

import com.mortonstudios.fictionalmatch.multiplayer.entities.Player;
import com.mortonstudios.fictionalmatch.multiplayer.entities.Unit;
import com.mortonstudios.fictionalmatch.utils.Metrics;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

/**
 * Singleton to hold and manager connected Players and the games state
 *
 * @author Cam
 * @since 1.0.0
 */
public class GameManager {

    // Data Counters and fancy calculators
    final private Metrics metrics = Metrics.getInstance();

    private static final GameManager INSTANCE = new GameManager();

    private GameManager(){
        // Another Singleton!
    }

    public static GameManager getInstance() {
        return INSTANCE;
    }

    private Map<String, Player> repository = new HashMap<>();

    /**
     * @return current database of player information
     */
    public Map<String, Player> getRepository() {
        return repository;
    }

    /**
     * @param repository load a copy of game state
     */
    public void setRepository(Map<String, Player> repository) {
        this.repository = repository;
    }

    /**
     * Ends the current turn if both players have ended their turn.
     *
     * @return true if endTurn has been preformed
     */
    public boolean endTurn() {
        List<String> playerIDs = new ArrayList<>(this.getRepository().keySet());
        for(int i = 0; i < playerIDs.size(); i++) {

            if(i + 1 >= playerIDs.size()) {
                return false;
            }

            if(this.getRepository().get(playerIDs.get(i)).getTurn() == this.getRepository().get(playerIDs.get(i + 1)).getTurn()) {
                this.metrics.setTurn(this.getRepository().get(playerIDs.get(i)).getTurn());
                return true;
            }
        }
        return false;
    }

    public int getTurn() {
        return this.metrics.getTurn();
    }

    /**
     * @return all game information
     */
    public JSONObject getAllMetrics() {
        JSONObject object = new JSONObject();
        JSONArray playerObjects = new JSONArray();
        DateTime dateTime = new DateTime().withZone(DateTimeZone.UTC);

        object.put("matchLength", this.metrics.totalPlayedTime(dateTime));

        Set<String> playerIDs = this.getRepository().keySet();

        for(String playerID : playerIDs) {
            Player player = this.getRepository().get(playerID);
            Map<String, Unit> playersArmy = player.getArmy();
            JSONObject playerObject = new JSONObject();
            playerObject.put("id", playerID);
            playerObject.put("totalPointsLost", this.metrics.totalPointsLost(playersArmy));
            playerObject.put("percentageOfArmyLost", this.metrics.percentageOfArmyListDestroyed(playersArmy));
            playerObject.put("commandPointsRemaining", player.getCommandPoints());
            playerObject.put("victoryPointsRemaining", player.getVictoryPoints());
            playerObjects.put(playerObject);
        }

        object.put("playerMatchMetaData", playerObjects);
        return object;
    }
}

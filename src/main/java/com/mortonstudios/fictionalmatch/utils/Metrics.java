package com.mortonstudios.fictionalmatch.utils;

import com.mortonstudios.fictionalmatch.multiplayer.entities.Unit;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Minutes;

import java.util.List;

/**
 * Metrics for calculating interesting statistics from the match
 *
 * @author Cam
 * @since 1.0.0
 */
public class Metrics {

    private static final Metrics INSTANCE = new Metrics();

    private final DateTime startTime;
    private int turn;

    private Metrics() {
        // Do nothing
        this.startTime = new DateTime().withZone(DateTimeZone.UTC);
    }

    public static Metrics getInstance() {
        return INSTANCE;
    }

    public float percentageOfArmyListDestroyed(final List<Unit> army) {
        return Float.valueOf(this.totalPointsLost(army)) / Float.valueOf(army.stream().mapToInt(Unit::getPoints).sum());
    }

    public int totalPointsLost(final List<Unit> army) {
        return army.stream().filter(Unit::isDestroyed).mapToInt(Unit::getPoints).sum();
    }

    public String totalPlayedTime(final DateTime currentTime) {
        return String.valueOf(Minutes.minutesBetween(this.startTime, currentTime).getMinutes());
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }
}

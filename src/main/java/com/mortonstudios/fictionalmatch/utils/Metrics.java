package com.mortonstudios.fictionalmatch.utils;

import com.mortonstudios.fictionalmatch.multiplayer.entities.Unit;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Minutes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

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

    public float percentageOfArmyListDestroyed(final Map<String, Unit> army) {
        return (float) this.totalPointsLost(army) /
                (float) new ArrayList<>(army.values()).stream().mapToInt(Unit::getPoints).sum();
    }

    public int totalPointsLost(final Map<String, Unit> army) {
        return new ArrayList<>(army.values()).stream().filter(Unit::isDestroyed).mapToInt(Unit::getPoints).sum();
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

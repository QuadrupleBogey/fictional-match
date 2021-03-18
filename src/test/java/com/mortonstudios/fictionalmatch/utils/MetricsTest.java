package com.mortonstudios.fictionalmatch.utils;

import com.mortonstudios.fictionalmatch.multiplayer.entities.Unit;
import org.joda.time.DateTime;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for {@link Metrics}
 *
 * @author Cam
 * @since 1.0.0
 */
class MetricsTest {

    @Test
    void percentageOfArmyListDestroyed() {
        Map<String, Unit> army = new HashMap<>();
        army.put("1", new Unit.Builder().givePointValue(10).build());
        army.put("2", new Unit.Builder().givePointValue(10).build());
        army.put("3", new Unit.Builder().givePointValue(10).build());
        army.put("4", new Unit.Builder().givePointValue(10).build());

        army.get("2").setDestroyed(true);
        army.get("3").setDestroyed(true);

        assertEquals(.50, Metrics.getInstance().percentageOfArmyListDestroyed(army));
    }

    @Test
    void totalPointsLost() {
        Map<String, Unit> army = new HashMap<>();
        army.put("1", new Unit.Builder().givePointValue(10).build());
        army.put("2", new Unit.Builder().givePointValue(10).build());
        army.put("3", new Unit.Builder().givePointValue(10).build());
        army.put("4", new Unit.Builder().givePointValue(10).build());

        army.get("2").setDestroyed(true);
        army.get("3").setDestroyed(true);

        assertEquals(20, Metrics.getInstance().totalPointsLost(army));
    }

    @Test
    void totalPlayedTime() throws InterruptedException {
        Metrics metrics = Metrics.getInstance();
        TimeUnit.MINUTES.sleep(1);
        assertEquals("1", metrics.totalPlayedTime(new DateTime()));
    }
}
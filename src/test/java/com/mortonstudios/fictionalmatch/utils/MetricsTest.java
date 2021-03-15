package com.mortonstudios.fictionalmatch.utils;

import com.mortonstudios.fictionalmatch.multiplayer.entities.Unit;
import org.joda.time.DateTime;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
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
        List<Unit> army = new ArrayList<>();
        army.add(new Unit.Builder().givePointValue(17).build());
        army.add(new Unit.Builder().givePointValue(17).build());
        army.add(new Unit.Builder().givePointValue(17).build());
        army.add(new Unit.Builder().givePointValue(17).build());

        army.get(2).setDestroyed(true);
        army.get(3).setDestroyed(true);

        assertEquals(.50, Metrics.getInstance().percentageOfArmyListDestroyed(army));
    }

    @Test
    void totalPointsLost() {
        List<Unit> army = new ArrayList<>();
        army.add(new Unit.Builder().givePointValue(10).build());
        army.add(new Unit.Builder().givePointValue(10).build());
        army.add(new Unit.Builder().givePointValue(10).build());
        army.add(new Unit.Builder().givePointValue(10).build());

        army.get(2).setDestroyed(true);
        army.get(3).setDestroyed(true);

        assertEquals(20, Metrics.getInstance().totalPointsLost(army));
    }

    @Test
    void totalPlayedTime() throws InterruptedException {
        Metrics metrics = Metrics.getInstance();
        TimeUnit.MINUTES.sleep(1);
        assertEquals("1", metrics.totalPlayedTime(new DateTime()));
    }
}
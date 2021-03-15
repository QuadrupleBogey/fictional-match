package com.mortonstudios.fictionalmatch.utils;

/**
 * Counters to holder information about current game
 * Merged with Metrics
 *
 * @author Cam
 * @since 1.0.0
 */
@Deprecated
public class Counters {

    private static final Counters INSTANCE = new Counters();

    static public Counters getInstance() {
        return INSTANCE;
    }

    private Counters() {
        // Doesn't do anything
    }

}

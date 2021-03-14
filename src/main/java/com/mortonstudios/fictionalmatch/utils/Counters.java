package com.mortonstudios.fictionalmatch.utils;

public class Counters {

    public static final Counters INSTANCE = new Counters();

    static public Counters getInstance() {
        return INSTANCE;
    }

    private Counters() {
        // Doesn't do anything
    }
    
}

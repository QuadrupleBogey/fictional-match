package com.mortonstudios.fictionalmatch.multiplayer.entities;

/**
 * Unit is the class that holds all of the positional data and rendering
 * information of a unit within a players army
 *
 * @author Cam
 * @since 1.0.0
 */
public class Unit extends SharedAnchor {

    private boolean destroyed = false;
    private int points;
    private String name;

    /**
     * @param x position
     * @param y position
     * @param z position
     * @param w rotation
     * @param destroyed has this model been removed from table top?
     * @param points cost of the model (for metrics)
     * @param name helper for deciding which model to render in app
     */
    public Unit(float x, float y, float z, float w, boolean destroyed, int points, String name) {
        super(x, y, z, w);
        this.destroyed = destroyed;
        this.points = points;
        this.name = name;
    }

    public Unit() {
        super();
        // Doesn't do a lot
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public void setDestroyed(boolean destroyed) {
        this.destroyed = destroyed;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static class Builder {
        private float x = 0;
        private float y = 0;
        private float z = 0;
        private float w = 0;
        private int points = 0;
        private String name = "Unknown";

        public Builder() {
            // Builder Pattern because this is a complicated object
        }

        public Builder xPosition(final float x) {
            this.x = x;
            return this;
        }

        public Builder yPosition(final float y) {
            this.y = y;
            return this;
        }

        public Builder zPosition(final float z) {
            this.z = z;
            return this;
        }

        public Builder wPosition(final float w) {
            this.w = w;
            return this;
        }

        public Builder givePointValue(final int points) {
            this.points = points;
            return this;
        }

        public Builder giveUnitAName(final String name) {
            this.name = name;
            return this;
        }

        public Unit build() {
            return new Unit(this.x, this.y, this.z, this.w, false, this.points, this.name);
        }
    }
}

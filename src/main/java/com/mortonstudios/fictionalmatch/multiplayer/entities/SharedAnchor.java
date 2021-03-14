package com.mortonstudios.fictionalmatch.multiplayer.entities;

public class SharedAnchor {

    // Positioning
    private float X;
    private float Y;
    private float Z;
    private float W;

    public SharedAnchor() {
        super();
    }

    public SharedAnchor(final float x, final float y, final float z, final float w) {
        this.X = x;
        this.Y = y;
        this.Z = z;
        this.W = w;
    }

    public float getX() {
        return X;
    }

    public void setX(final float x) {
        this.X = x;
    }

    public float getW() {
        return W;
    }

    public void setW(final float w) {
        this.W = w;
    }

    public float getZ() {
        return Z;
    }

    public void setZ(final float z) {
        this.Z = z;
    }

    public float getY() {
        return Y;
    }

    public void setY(final float y) {
        this.Y = y;
    }

    @Override
    public String toString() {
        return "{ SharedAnchor: { x: " + this.X + ", y: " + this.Y + ", z: " + this.Z
                + ", W: " + this.W + " } }";
    }
}

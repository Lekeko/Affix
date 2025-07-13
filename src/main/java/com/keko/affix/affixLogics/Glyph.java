package com.keko.affix.affixLogics;

import net.minecraft.resources.ResourceLocation;

public class Glyph {
    private final ResourceLocation image;
    private final float rage;
    private int x;
    private float deltaX;
    private int y;
    private float deltaY;

    public Glyph(ResourceLocation image, float rage, int x, int y, float deltaX, float deltaY) {
        this.image = image;
        this.rage = rage;
        this.x = x;
        this.deltaX = deltaX;
        this.y = y;
        this.deltaY = deltaY;
    }

    public void move(){
        float drag = 0.1f;

        float signX = Math.signum(deltaX);
        if (signX != 0) {
            float potentialNewDeltaX = deltaX - signX * drag;
            if (Math.signum(potentialNewDeltaX) != signX || potentialNewDeltaX == 0) {
                deltaX = 0;
            } else {
                deltaX = potentialNewDeltaX;
            }
        }

        float signY = Math.signum(deltaY);
        if (signY != 0) {
            float potentialNewDeltaY = deltaY - signY * drag;
            if (Math.signum(potentialNewDeltaY) != signY || potentialNewDeltaY == 0) {
                deltaY = 0;
            } else {
                deltaY = potentialNewDeltaY;
            }
        }

        this.x += deltaX;
        this.y += deltaY;

    }

    public float getRage() {
        return rage;
    }

    public float getDeltaX() {
        return deltaX;
    }

    public float getDeltaY() {
        return deltaY;
    }

    public ResourceLocation getImage() {
        return image;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}

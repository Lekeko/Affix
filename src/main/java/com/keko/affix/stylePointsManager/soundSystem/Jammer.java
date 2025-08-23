package com.keko.affix.stylePointsManager.soundSystem;

public class Jammer {
    private Sound sound;
    private float volume;
    private int index;

    public Jammer(Sound sound, float volume, int index) {
        this.sound = sound;
        this.volume = volume;
        this.index = index;
    }

    public Sound getSound() {
        return sound;
    }

    public void setSound(Sound sound) {
        this.sound = sound;
    }

    public float getVolume() {
        return volume;
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}

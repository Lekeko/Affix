package com.keko.affix.util.cc;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;

public class ActualScoreComponent implements ScoreComponent, AutoSyncedComponent {
    private int score = 0;
    private final Object provider;

    @Override
    public int getScore() {
        return score;
    }

    public ActualScoreComponent(Object provider){
        this.provider = provider;
    }

    @Override
    public void setScore(int score) {
        this.score = score;
        MyComponents.SCORE.sync(this.provider);
    }

    @Override
    public void readFromNbt(CompoundTag tag, HolderLookup.Provider registryLookup) {
        this.score = tag.getInt("score");
    }

    @Override
    public void writeToNbt(CompoundTag tag, HolderLookup.Provider registryLookup) {
        tag.putInt("score", this.score);

    }
}

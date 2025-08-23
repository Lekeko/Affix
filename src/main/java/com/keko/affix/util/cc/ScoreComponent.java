package com.keko.affix.util.cc;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import org.ladysnake.cca.api.v3.component.Component;

public interface ScoreComponent extends Component {
   int getScore();
   void setScore(int score);
}


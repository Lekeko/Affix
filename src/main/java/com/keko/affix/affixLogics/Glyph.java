package com.keko.affix.affixLogics;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.resources.ResourceLocation;

@Setter @Getter @AllArgsConstructor
public class Glyph {
    private final ResourceLocation image;
    private final float rage;
    private int x;
    private int y;
    private float deltaX;
    private float deltaY;
    private float acceleration = 60;


}

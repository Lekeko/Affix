package com.keko.affixLogics;

import com.keko.items.custom.HeavyMirror;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

import java.awt.*;

public class GlyphMarking {


    public static void renderImage(GuiGraphics guiGraphics, DeltaTracker deltaTracker, float mirrorOffset){

    }

    public static void renderCoordinates(GuiGraphics guiGraphics, DeltaTracker deltaTracker, float mirrorOffset) {
        int xMid = guiGraphics.guiWidth() / 2;
        int yMid = guiGraphics.guiHeight() / 2;

        int alphaFactor = (int)(mirrorOffset * 100);
        System.out.println(alphaFactor);
        if ( alphaFactor > 10)
            guiGraphics.drawCenteredString(Minecraft.getInstance().font, new String("Pulancur"), xMid, yMid, new Color(255, 255, 255, Math.max(0, alphaFactor * 2)).getRGB());
    }
}

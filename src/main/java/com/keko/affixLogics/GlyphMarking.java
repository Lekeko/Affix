package com.keko.affixLogics;

import com.keko.items.custom.HeavyMirror;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Player;

import java.awt.*;

public class GlyphMarking {


    public static void renderImage(GuiGraphics guiGraphics, DeltaTracker deltaTracker, float mirrorOffset){

    }

    public static void renderCoordinates(GuiGraphics guiGraphics, DeltaTracker deltaTracker, float mirrorOffset) {
        int xMid = guiGraphics.guiWidth() / 2;
        int yMid = guiGraphics.guiHeight() / 2 - 4;

        LocalPlayer player = Minecraft.getInstance().player;

        int alphaFactor = (int)(mirrorOffset * 100 * 2);
        if ( alphaFactor > 10 && player != null) {
            guiGraphics.drawCenteredString(Minecraft.getInstance().font, String.format("%.3f", player.getX()), xMid - alphaFactor / 5, yMid, new Color(255, 255, 255, alphaFactor).getRGB());
            guiGraphics.drawCenteredString(Minecraft.getInstance().font, String.format("%.3f", player.getZ()), xMid + alphaFactor / 5, yMid, new Color(255, 255, 255, alphaFactor).getRGB());
        }
    }
}

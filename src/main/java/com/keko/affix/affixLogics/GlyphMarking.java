package com.keko.affix.affixLogics;

import com.keko.affix.AffixClient;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;

import java.awt.*;

public class GlyphMarking {

    public static void renderCoordinates(GuiGraphics guiGraphics, DeltaTracker deltaTracker, float mirrorOffset) {
        int xMid = guiGraphics.guiWidth() / 2;
        int yMid = guiGraphics.guiHeight() / 2 - 4;

        LocalPlayer player = Minecraft.getInstance().player;
        Color color = AffixClient.getClientColor();

        int alphaFactor = (int)(mirrorOffset * 100 * 2);
        if (alphaFactor >= 5 && alphaFactor <= 255 && player != null) {
            color = new Color(color.getRed(), color.getGreen(), color.getBlue(), alphaFactor);
            guiGraphics.drawCenteredString(Minecraft.getInstance().font, AffixClient.xAnom != 1111111111 ? String.format("%d", AffixClient.xAnom) : "✌☠⚐\uD83D\uDCA3✌☹✡", xMid - alphaFactor / 5, yMid  - alphaFactor / 10, color.getRGB());
            guiGraphics.drawCenteredString(Minecraft.getInstance().font, AffixClient.zAnom != 1111111111 ? String.format("%d", AffixClient.zAnom) : "\uD83D\uDC4E☜❄☜\uD83D\uDC4D❄☜\uD83D\uDC4E", xMid + alphaFactor / 5, yMid  - alphaFactor / 10, color.getRGB());
            guiGraphics.drawCenteredString(Minecraft.getInstance().font, String.format("%.0f", player.getX()), xMid - alphaFactor / 5, yMid  + alphaFactor / 10, color.getRGB());
            guiGraphics.drawCenteredString(Minecraft.getInstance().font, String.format("%.0f", player.getZ()), xMid + alphaFactor / 5, yMid  + alphaFactor / 10, color.getRGB());

            guiGraphics.drawCenteredString(Minecraft.getInstance().font, String.format("%d", AffixClient.anomalyCooldown / 20), xMid, yMid  - alphaFactor / 5, color.getRGB());
        }
    }
}

package com.keko.affix.affixLogics;

import com.keko.affix.Affix;
import com.keko.affix.AffixClient;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;

public class GlyphSpawner {
    private static final float SPEED = 2f;
    private static final int HUD_MARGINS_OFFSET = 0;

    public static Glyph createGlyph(float rage){
        Minecraft minecraft = Minecraft.getInstance();
        int monitorSizeX = 0;
        int monitorSizeY = 0;
        if (AffixClient.width != 0 && AffixClient.height != 0){
            monitorSizeX = AffixClient.width;
            monitorSizeY = AffixClient.height;
        }
        int posX = 0;
        int posY = 0;
        boolean mark = false;

        assert minecraft.level != null;
        if (minecraft.level.random.nextBoolean()) {
            posX = minecraft.level.random.nextIntBetweenInclusive(HUD_MARGINS_OFFSET, monitorSizeX - 64);
            posY = minecraft.level.random.nextBoolean() ? 0 : monitorSizeY - 64;
            mark = true;
        }
        else {
            posY = minecraft.level.random.nextIntBetweenInclusive(HUD_MARGINS_OFFSET, monitorSizeY - 64);
            posX = minecraft.level.random.nextBoolean() ? 0 : monitorSizeX - 64;
        }

        ResourceLocation image = ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, "textures/glyphs/glyph_" +  minecraft.level.random.nextIntBetweenInclusive(1, 4) + ".png");


        float deltaX = 5 * (posX == 0 ? 1 : -1);
        float deltaY = 5 * (posY == 0 ? 1 : -1);

        if (mark)
            deltaX = 0;
        else
            deltaY = 0;


        return new Glyph(image, rage, posX, posY, deltaX, deltaY);
    }
}

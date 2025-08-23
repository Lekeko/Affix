package com.keko.affix.affixLogics;

import com.keko.affix.Affix;
import com.keko.affix.AffixClient;
import com.keko.affix.helpers.Directional;
import com.keko.affix.packet.BeaconSummonPacketC2S;
import com.keko.affix.packet.DragonSummonPacketC2S;
import com.keko.affix.packet.SpearSummonPacketC2S;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

import java.awt.*;
import java.util.ArrayList;

public class SpellHud {
    private static float alpha = 0;
    private static float alpha2 = 0;

    public static void setAlpha(float alpha) {
        SpellHud.alpha = alpha;
    }

    public static void setAlpha2(float alpha2) {
        SpellHud.alpha2 = alpha2;
    }

    public static void initiateSpellHud(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        int border = 10;
        int spacing = 70;
        int spellNumber = 3;
        int size = 48;

        alpha+= deltaTracker.getGameTimeDeltaTicks() / 10;
        ArrayList<String> code = new ArrayList<String>();
        int[] mayContinue = {1,1,1};

        code.add("rdurd");
        code.add("dulrlr");
        code.add("udlru");
        float drag = 0.1f;
        float deltaX = 60 - (alpha* 200);
        float signX = Math.signum(deltaX);
        if (signX != 0) {
            float potentialNewDeltaX = deltaX - signX * drag;
            if (Math.signum(potentialNewDeltaX) != signX || potentialNewDeltaX == 0) {
                deltaX = 0;
            } else {
                deltaX = potentialNewDeltaX;
            }
        }
        int xmodif = (int) -Math.max(deltaX, 0);

        evaluateCombination(code, guiGraphics, deltaTracker);

        for (int i = 0; i < spellNumber; i++){

            //frame
            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, Math.min(7, alpha));

            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();

            guiGraphics.blit(
                    ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, "textures/gui/arrow_command_textures/frame.png"),
                    border + xmodif,
                    border + i * spacing,
                    0,
                    0,
                    size,
                    size,
                    size,
                    size);

            //spell


            String sprite = "nope";
            switch (i){
                case 0 -> sprite = "lazer";
                case 1 -> sprite = "shackle";
                case 2 -> sprite = "beacon";
            }

            guiGraphics.blit(
                    ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, "textures/gui/arrow_command_textures/" + sprite + ".png"),
                    border + 4 + xmodif,
                    border + i * spacing + 4,
                    0,
                    0,
                    (int)(size /1.2),
                    (int)(size /1.2),
                    (int)(size /1.2),
                    (int)(size /1.2));


            //Text

            String name = "nope";
            switch (i){
                case 0 -> name = "Dragon Laser";
                case 1 -> name = "Imprisonment";
                case 2 -> name = "Infernal Beacon";
            }

            guiGraphics.drawString(Minecraft.getInstance().font,
                    name,
                    border + 4 + size + 8 + xmodif,
                    border + i * spacing + 4,
                    new Color(240, 227, 183).getRGB());


            //arrows
            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f,  Math.min(7, alpha));
            RenderSystem.enableBlend();

            for (int j = 0; j < code.get(i).length(); j++){

                    int ok = 0;
                    try {
                        if (AffixRenderingHandlers.getKeyInputs().get(j).charAt(0) == code.get(i).charAt(j))
                            ok = 1;
                        else mayContinue[i] = 0;

                    }catch (Exception ignored){}

                    if (ok == 1 && mayContinue[i] == 1)
                        guiGraphics.blit(
                        ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, "textures/gui/arrow_command_textures/glow.png"),
                        border + j * 15 + size + 4 + xmodif,
                        (int) (border + i * spacing + size / 2.6f),
                        0,
                        0,
                        size / 2,
                        size / 2,
                        size / 2,
                        size / 2);

                        guiGraphics.blit(
                        ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, "textures/gui/arrow_command_textures/arrow_" +
                                code.get(i).charAt(j) + (ok == 1 && mayContinue[i] == 1 ? "a" : "")
                                + ".png"),
                        border + j * 15 + size + border + xmodif,
                        border + i * spacing + size / 2,
                        0,
                        0,
                        size / 4,
                        size / 4,
                        size / 4,
                        size / 4);

            }

        }


    }

    public static void initiateBeaconHud(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        Minecraft minecraft = Minecraft.getInstance();
        int width = guiGraphics.guiWidth();
        int height = guiGraphics.guiHeight();
        int size = 50;

        alpha2+= deltaTracker.getGameTimeDeltaTicks() / 3;
        float drag = 0.1f;
        float deltaX = (alpha2* 60);
        float signX = Math.signum(deltaX);
        if (signX != 0) {
            float potentialNewDeltaX = deltaX - signX * drag;
            if (Math.signum(potentialNewDeltaX) != signX || potentialNewDeltaX == 0) {
                deltaX = 0;
            } else {
                deltaX = potentialNewDeltaX;
            }
        }
        int xmodif = (int) Math.min(deltaX, 80);
        int count = 1;
        for (int j = -1; j <= 1; j+=2){

            //frame
            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, Math.min(7, alpha2));

            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();

            guiGraphics.blit(
                    ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, "textures/gui/arrow_command_textures/frame_b.png"),
                    width/2 - size/2,
                    height/2 - size/2 + xmodif * j,
                    0,
                    0,
                    size,
                    size,
                    size,
                    size);

            //spell
            guiGraphics.blit(
                    ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, "textures/gui/arrow_command_textures/beacon_" + count  + ".png"),
                    width/2 - (size/2 - 4),
                    height/2 - (size/2 - 4) + xmodif * j,
                    0,
                    0,
                    (int)(size /1.2),
                    (int)(size /1.2),
                    (int)(size /1.2),
                    (int)(size /1.2));


            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f,  Math.min(7, alpha2));
            RenderSystem.enableBlend();
            count++;
        }

        for (int j = -1; j <= 1; j+=2){

            //frame
            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, Math.min(7, alpha2));

            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();

            guiGraphics.blit(
                    ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, "textures/gui/arrow_command_textures/frame_b.png"),
                    width/2 - size/2 + xmodif * j,
                    height/2 - size/2 ,
                    0,
                    0,
                    size,
                    size,
                    size,
                    size);

            //spell
            guiGraphics.blit(
                    ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, "textures/gui/arrow_command_textures/beacon_" + count  + ".png"),
                    width/2 - (size/2 - 4) + xmodif * j,
                    height/2 - (size/2 - 4),
                    0,
                    0,
                    (int)(size /1.2),
                    (int)(size /1.2),
                    (int)(size /1.2),
                    (int)(size /1.2));


            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f,  Math.min(7, alpha2));
            RenderSystem.enableBlend();
            count++;
        }


    }

    private static void evaluateCombination(ArrayList<String> code, GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        StringBuilder input = new StringBuilder();
        Vec3 vec3 = Directional.rayCast(Minecraft.getInstance().level, Minecraft.getInstance().player, Minecraft.getInstance().player.getViewVector(1.0f), 13 * (Minecraft.getInstance().options.renderDistance().get()-2));

        for (int i = 0; i < AffixRenderingHandlers.getKeyInputs().size(); i++)
            input.append(AffixRenderingHandlers.getKeyInputs().get(i));
        if (code.contains(input.toString())){
            switch (code.indexOf(input.toString())){
                case 0 : ClientPlayNetworking.send(new DragonSummonPacketC2S((int) vec3.x, (int) vec3.y, (int) vec3.z)); AffixRenderingHandlers.getKeyInputs().add("z");break;
                case 1 : ClientPlayNetworking.send(new SpearSummonPacketC2S((int) vec3.x, (int) vec3.y, (int) vec3.z, Minecraft.getInstance().player.getId()));AffixRenderingHandlers.getKeyInputs().add("z");break;
                case 2 : {
                    initiateBeaconHud(guiGraphics, deltaTracker);
                }
            }
        }
        if (AffixRenderingHandlers.getKeyInputs().size() == 6)
            if (code.contains(input.substring(0, AffixRenderingHandlers.getKeyInputs().size()-1))){
                int beacon = 0;
                switch (AffixRenderingHandlers.getKeyInputs().getLast()){
                    case "u"-> beacon = 1;
                    case "d"-> beacon = 2;
                    case "l"-> beacon = 3;
                    case "r"-> beacon = 4;
                }
                if (beacon != 0) {
                    ClientPlayNetworking.send(new BeaconSummonPacketC2S((int) vec3.x, (int) vec3.y, (int) vec3.z, beacon));
                    AffixRenderingHandlers.getKeyInputs().add("z");
                }
            }

    }


}

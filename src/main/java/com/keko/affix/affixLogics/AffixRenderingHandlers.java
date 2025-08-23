package com.keko.affix.affixLogics;

import com.keko.affix.Affix;
import com.keko.affix.AffixClient;
import com.keko.affix.items.ModItems;
import com.keko.affix.midLib.AffixConfigs;
import com.keko.affix.packet.CrazyStatusC2S;
import com.keko.affix.sounds.ModSounds;
import com.keko.affix.stylePointsManager.rockets.RocketScreenManager;
import com.keko.affix.stylePointsManager.soundSystem.Sound;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import foundry.veil.Veil;
import foundry.veil.api.client.render.VeilRenderSystem;
import foundry.veil.api.client.util.Easing;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Camera;
import net.minecraft.client.CameraType;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.Input;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.joml.*;

import java.awt.*;
import java.lang.Math;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;

public class AffixRenderingHandlers {

    //RENDERING THE SPELL HUD
    private static float tick = 0;

    private static String lastMovement = "nope";
    private static ArrayList<String> keyInputs = new ArrayList<>();

    public static ArrayList<String> getKeyInputs() {
        return keyInputs;
    }

    public static void handleSpellHud(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {

        if(Minecraft.getInstance().player != null){
            if (Minecraft.getInstance().player.getUseItem().is(ModItems.INFERNAL_TOME)) {
                Input input = Minecraft.getInstance().player.input;
                String message = "nope";
                if (Minecraft.getInstance().player.getUseItemRemainingTicks() < ModItems.INFERNAL_TOME.getUseDuration(ModItems.INFERNAL_TOME.getDefaultInstance(), Minecraft.getInstance().player) - 1) {
                    message = getString(input);
                    if (!message.equals("nope")) {
                        keyInputs.add(message);
                        Minecraft.getInstance().level.playLocalSound(Minecraft.getInstance().player, ModSounds.SPELL, SoundSource.PLAYERS, 1, 0.6f + 0.2f * keyInputs.size());
                    }
                }
                SpellHud.initiateSpellHud(guiGraphics, deltaTracker);
            }else {
                if (!keyInputs.isEmpty()) keyInputs.clear();
                SpellHud.setAlpha(0);
                SpellHud.setAlpha2(0);
            }
        }

    }
    private static @NotNull String getString(Input input) {
        float leftImpulse = input.leftImpulse;
        float forwardImpulse = input.forwardImpulse;
        String message = "nope";

        if (forwardImpulse == 0.2f) {
            message = "u";
        } else if (forwardImpulse == -0.2f) {
            message = "d";
        }

        if (leftImpulse == 0.2f) {
            message = "l";
        } else if (leftImpulse == -0.2f) {
            message = "r";
        }
        if (message.equals(lastMovement))
            return "nope";
        lastMovement = message;
        return message;
    }

    //RENDERING THE GLYPHS FROM THE ACCELERATOR (I HATYE THIS)
    private static ArrayList<Glyph> spawnedGlyphs = new ArrayList<>();
    public static float rage = 0;


    public static void addGlyph() {
        spawnedGlyphs.add(GlyphSpawner.createGlyph(rage));
        rage += 0.3f;
        if (rage > 1.2f)
            ClientPlayNetworking.send(new CrazyStatusC2S(true));
    }


    public static void handleGlyphsAccel(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {

        GlyphMarking.renderCoordinates(guiGraphics, deltaTracker, AffixClient.mirrorOffset);
        if (rage > 0)
            rage-= 0.004f;

        if (!spawnedGlyphs.isEmpty()) {
            Iterator<Glyph> iterator = spawnedGlyphs.iterator();
            while (iterator.hasNext()) {
                Glyph glyph = iterator.next();
                moveGlyph(glyph);

                if (glyph.getAcceleration() < 0.2f)
                    iterator.remove();
                else {

                    Color color = Color.decode(AffixConfigs.rageAccentColor);
                    float red = (float) color.getRed() / 255 + 1.3f - Math.min(rage, 1.3f);
                    float green = (float) color.getGreen() / 255 + 1.3f -  Math.min(rage, 1.3f);
                    float blue = (float) color.getBlue() / 255 + 1.3f -  Math.min(rage, 1.3f);

                    float alpha = glyph.getAcceleration()/ 10;


                    RenderSystem.enableBlend();
                    RenderSystem.defaultBlendFunc();

                    RenderSystem.setShaderColor(red, green, blue, alpha);

                    guiGraphics.blit(
                            ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, "textures/glyphs/glyph_glow.png"),
                            glyph.getX(),
                            glyph.getY(),
                            0,
                            0,
                            64,
                            64,
                            64,
                            64
                    );
                    guiGraphics.blit(
                            glyph.getImage(),
                            glyph.getX(),
                            glyph.getY(),
                            0,
                            0,
                            64,
                            64,
                            64,
                            64
                    );

                    RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
                }

            }
        }

    }

    private static void moveGlyph(Glyph glyph) {
        Minecraft minecraft = Minecraft.getInstance();
        glyph.setX((int) (glyph.getX() + minecraft.getTimer().getGameTimeDeltaTicks() * glyph.getAcceleration() * Math.signum(glyph.getDeltaX())));
        glyph.setY((int) (glyph.getY() + minecraft.getTimer().getGameTimeDeltaTicks() * glyph.getAcceleration() * Math.signum(glyph.getDeltaY())));

        if (glyph.getAcceleration() > 0)
            glyph.setAcceleration(glyph.getAcceleration() - minecraft.getTimer().getGameTimeDeltaTicks() * 15);
        else glyph.setAcceleration(0);
    }


    public static void handleMissileMark(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        int width = guiGraphics.guiWidth();
        int height = guiGraphics.guiHeight();
        Minecraft client = Minecraft.getInstance();
        if (client.player == null) return;

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        int size = 32;
        int sizeBig = size + 6;

        Color hitMarkColor;
        try {
            hitMarkColor = Color.decode(AffixConfigs.jamPrimaryColor);
        }catch (Exception e){
            Affix.LOGGER.warn("Please provide correct color input :3 (or imma send ur pc in space lol)");
            hitMarkColor = Color.BLACK;
        }

        Vector3f normalizedColor = new Vector3f(hitMarkColor.getRed(), hitMarkColor.getGreen(), hitMarkColor.getBlue()).div(255);


        for (LivingEntity livingEntity : RocketScreenManager.getHitmarkPositions()){


            Vec3 worldPos = livingEntity.getPosition(1.0f).add(0,livingEntity.getEyeHeight(), 0);
            Vec3 screenPos = worldToScreen(worldPos, guiGraphics);
            RenderSystem.setShaderColor(normalizedColor.x + 1, normalizedColor.y + 1, normalizedColor.z + 1, 22.0f);

            guiGraphics.blit(
                    ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, "textures/gui/jam_textures/hitmark.png"),
                    (int) screenPos.x + width / 2 - sizeBig / 2,
                    (int) screenPos.y + height / 2 - sizeBig / 2,
                    0,
                    0,
                    sizeBig,
                    sizeBig,
                    sizeBig,
                    sizeBig);

            RenderSystem.setShaderColor(normalizedColor.x, normalizedColor.y, normalizedColor.z, 1.0f);
                guiGraphics.blit(
                        ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, "textures/gui/jam_textures/hitmark.png"),
                        (int) screenPos.x + width / 2 - size / 2,
                        (int) screenPos.y + height / 2 - size / 2,
                        0,
                        0,
                        size,
                        size,
                        size,
                        size);
        }
        RenderSystem.setShaderColor(1.0f,1.0f,1.0f,1.0f);

        RenderSystem.disableBlend();

    }

    public static Vec3 worldToScreen(Vec3 pos, GuiGraphics guiGraphics){
        Minecraft minecraft = Minecraft.getInstance();
        Camera camera = minecraft.gameRenderer.getMainCamera();

        Matrix4f projMat = VeilRenderSystem.renderer().getCameraMatrices().getProjectionMatrix();
        Matrix4f viewMat = VeilRenderSystem.renderer().getCameraMatrices().getViewMatrix();

        Vec3 relativePositionFromCamera = pos.subtract(camera.getPosition());
        Vector4f screenPosition = new Vector4f((float) relativePositionFromCamera.x, (float) relativePositionFromCamera.y, (float) relativePositionFromCamera.z, 1);

        screenPosition = screenPosition.mul(viewMat);
        screenPosition = screenPosition.mul(projMat);

        int width = guiGraphics.guiWidth();
        int height = guiGraphics.guiHeight();

        float screenX = (screenPosition.x / screenPosition.z) * width/2;
        float screenY = (-screenPosition.y / screenPosition.z) * height/2;

        return new Vec3(screenX, screenY, screenPosition.z);

    }

    public static void handleJammingHud(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        renderFrameAndStyle(guiGraphics);
        renderText(guiGraphics);

    }

    private static void renderText(GuiGraphics guiGraphics) {

        int width = guiGraphics.guiWidth();
        int height = guiGraphics.guiHeight();
        float scaleFactor = 1.5f;

        Sound sound = AffixClient.getJammer().getSound();
        if (sound != null && sound.isPlaying()) {
            scaleFactor += (sound.getAmplitude()) + AffixClient.styleSystemManager.getStyle() / 8f;
        }

        Color primaryColor;
        try {
            primaryColor = Color.decode(AffixConfigs.jamSecondaryColor);
        } catch (Exception e) {
            Affix.LOGGER.warn("Please provide correct color input :3 (or imma send ur pc in space lol)");
            primaryColor = Color.MAGENTA;
        }

        Color lightenedColor = new Color(
                Math.clamp(primaryColor.getRed() + 80, 0, 255),
                Math.clamp(primaryColor.getGreen() + 80, 0, 255),
                Math.clamp(primaryColor.getBlue() + 80, 0, 255)
        );

        int score = (int) AffixClient.getStyleSystemManager().getScore();
        String scoreText = NumberFormat.getNumberInstance(Locale.US).format(score);

        PoseStack stack = guiGraphics.pose();
        stack.pushPose();
        float clampedProgress = 1 - Math.clamp((200 - tick)/200, 0, 1);

        stack.translate( (width - Easing.EASE_OUT_ELASTIC.ease(clampedProgress) * 115.0F), 60.0F - AffixClient.styleSystemManager.getStyle() / 4f, 0);
        stack.scale(scaleFactor, scaleFactor, scaleFactor);
        stack.mulPose(Axis.ZN.rotationDegrees((float) Math.sin(tick / 100)*10));



        guiGraphics.drawCenteredString(Minecraft.getInstance().font,
                scoreText,
                0,
                0,
                lightenedColor.getRGB());

        stack.popPose();

        stack.pushPose();

        stack.translate(Easing.EASE_OUT_ELASTIC.ease(clampedProgress) * 140.5F, 110.0F, 0);
        stack.scale(scaleFactor, scaleFactor, scaleFactor);
        stack.mulPose(Axis.ZN.rotationDegrees((float) Math.sin(tick / 100 + 2)*10));

        guiGraphics.drawCenteredString(Minecraft.getInstance().font,
                String.format("%.1f", AffixClient.getStyleSystemManager().getStyle()) + "x",
                0,
                0,
                lightenedColor.getRGB());

        stack.popPose();

        stack.pushPose();

        stack.translate((float) width /2 + 11, Easing.EASE_OUT_ELASTIC.ease(clampedProgress) * 30, 0);
        stack.scale(scaleFactor, scaleFactor, scaleFactor);

        guiGraphics.drawCenteredString(Minecraft.getInstance().font,
                String.valueOf(AffixClient.getStyleSystemManager().getRockets()) + "x",
                0,
                0,
                lightenedColor.getRGB());

        stack.popPose();
    }


    public static void setTick(float tick) {
        AffixRenderingHandlers.tick = tick;
    }

    private static void renderFrameAndStyle(GuiGraphics guiGraphics) {
        int width = guiGraphics.guiWidth();
        int height = guiGraphics.guiHeight();
        int sizeNoAmp = 200;
        int sizeRank = (int) (sizeNoAmp / 4f);

        Sound sound = AffixClient.getJammer().getSound();
        tick += Minecraft.getInstance().getTimer().getGameTimeDeltaTicks() * 10;

        Color primaryColor;
        Color secondaryColor;
        try {
            primaryColor = Color.decode(AffixConfigs.jamPrimaryColor);
            secondaryColor = Color.decode(AffixConfigs.jamSecondaryColor);
        }catch (Exception e){
            Affix.LOGGER.warn("Please provide correct color input :3 (or imma send ur pc in space lol)");
            primaryColor = Color.MAGENTA;
            secondaryColor = Color.BLACK;
        }
        float sucker = 160;

        float clampedProgress = 1 - Math.clamp((200 - tick)/200, 0, 1);
        PoseStack stack = guiGraphics.pose();

        RenderSystem.setShaderColor(
                (float) primaryColor.getRed() / sucker,
                (float) primaryColor.getGreen() / sucker,
                (float) primaryColor.getBlue() / sucker,
                1.0f);
        stack.pushPose();

        if (AffixClient.getStyleSystemManager().getDashes() >= 1 && Minecraft.getInstance().options.getCameraType().isFirstPerson()) {
            if (width % 2 == 0)
                stack.translate(-0.5, 0, 0);

            guiGraphics.blit(
                    ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, "textures/gui/jam_textures/d" + ((int) AffixClient.getStyleSystemManager().getDashes()) + ".png"),
                    width / 2 - 24,
                    height / 2 - 24,
                    0,
                    0,
                    48,
                    48,
                    48,
                    48);
        }

        stack.popPose();

        guiGraphics.blit(
                ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, "textures/gui/jam_textures/style_frame.png"),
                (int) (Easing.EASE_OUT_ELASTIC.ease(clampedProgress) * 100) - 100,
                0,
                0,
                0,
                sizeNoAmp,
                sizeNoAmp,
                sizeNoAmp,
                sizeNoAmp);




        guiGraphics.blit(
                ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, "textures/gui/jam_textures/points_frame.png"),
                width -
                (int) (Easing.EASE_OUT_ELASTIC.ease(clampedProgress) * 100) - 100,
                0,
                0,
                0,
                sizeNoAmp,
                sizeNoAmp,
                sizeNoAmp,
                sizeNoAmp);

        guiGraphics.blit(
                ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, "textures/gui/jam_textures/style_rockets.png"),
                width/2 - 101,
                (int) (Easing.EASE_OUT_ELASTIC.ease(clampedProgress) * 100) - 100,
                0,
                0,
                sizeNoAmp,
                sizeNoAmp,
                sizeNoAmp,
                sizeNoAmp);

        guiGraphics.blit(
                ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, "textures/gui/jam_textures/style_right.png"),
                width - (int) (Easing.EASE_OUT_ELASTIC.ease(clampedProgress) * 100) - 100,
                height/2,
                0,
                0,
                sizeNoAmp,
                sizeNoAmp,
                sizeNoAmp,
                sizeNoAmp);

        guiGraphics.blit(
                ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, "textures/gui/jam_textures/style_left.png"),
                (int) (Easing.EASE_OUT_ELASTIC.ease(clampedProgress) * 100) - 100,
                height /2 ,
                0,
                0,
                sizeNoAmp,
                sizeNoAmp,
                sizeNoAmp,
                sizeNoAmp);






        float scaleFactor = 1 + AffixClient.styleSystemManager.getStyle() / 16;
        if (sound != null)
            scaleFactor += sound.getAmplitude();

        stack.pushPose();

        stack.translate(
                (int) (Easing.EASE_OUT_ELASTIC.ease(clampedProgress) * 100) - 100 + 61.5 - scaleFactor * 21,
                62 - scaleFactor * 21,
                0);

        stack.scale(scaleFactor, scaleFactor, scaleFactor);

        RenderSystem.setShaderColor(
                (float) secondaryColor.getRed() / sucker,
                (float) secondaryColor.getGreen() / sucker,
                (float) secondaryColor.getBlue() / sucker,
                1.0f);

        guiGraphics.blit(
                ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, "textures/gui/jam_textures/style_" + (int)(AffixClient.getStyleSystemManager().getStyle()) + ".png"),
                0,
                0,
                0,
                0,
                sizeRank,
                sizeRank,
                sizeRank,
                sizeRank);

        stack.popPose();



        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);


    }
}

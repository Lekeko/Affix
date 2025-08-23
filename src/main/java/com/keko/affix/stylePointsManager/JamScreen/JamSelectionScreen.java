package com.keko.affix.stylePointsManager.JamScreen;

import com.keko.affix.Affix;
import com.keko.affix.AffixClient;
import com.keko.affix.packet.JamRequestPacketC2S;
import com.keko.affix.stylePointsManager.ReadFromFolder;
import com.keko.affix.stylePointsManager.pointsSystem.StyleSystemManager;
import com.keko.affix.stylePointsManager.shopScreen.ShopSelectionScreen;
import com.keko.affix.stylePointsManager.soundSystem.Sound;
import com.keko.affix.stylePointsManager.soundSystem.SoundAssets;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.lwjgl.glfw.GLFW;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class JamSelectionScreen extends Screen {
    private float timerSinceOpen = 0;

    private Button jamButton;
    private Button jamButtonStop;
    private Button jamSelector;
    private Button jamInfo;
    private Button jamShop;
    private Button refreshTracks;
    private AbstractSliderButton jamVolume;
    private Sound sound;

    private double volume;

    public JamSelectionScreen(Component component) {
        super(component);
    }


    @Override
    public boolean keyPressed(int i, int j, int k) {
        if (i == GLFW.GLFW_KEY_ESCAPE)
            Minecraft.getInstance().setScreen(null);
        return super.keyPressed(i, j, k);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int i, int j, float f) {
        timerSinceOpen += Minecraft.getInstance().getTimer().getGameTimeDeltaTicks() / 15;
        super.render(guiGraphics, i, j, f);
        RenderSystem.setShaderColor(1,1,1,0.2f);
        guiGraphics.fill(0,0,width,height, new Color(92, 36, 130, 255).getRGB());
        RenderSystem.setShaderColor(1,1,1,1);
        renderFrame(guiGraphics);
        renderEnderHeart(guiGraphics);

    }

    private void renderFrame(GuiGraphics guiGraphics) {
        int width = guiGraphics.guiWidth();
        int height = guiGraphics.guiHeight();
        int size = 256;

        if (sound != null &&sound.isPlaying()){
            size += sound.getAmplitude() * 40;
        }

        guiGraphics.blit(
                ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, "textures/gui/jam_textures/frame_1.png"),
                0,
                0,
                0,
                0,
                size,
                size,
                size,
                size);
        guiGraphics.blit(
                ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, "textures/gui/jam_textures/frame_2.png"),
                width-size,
                0,
                0,
                0,
                size,
                size,
                size,
                size);
        guiGraphics.blit(
                ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, "textures/gui/jam_textures/frame_3.png"),
                width-size,
                height-size,
                0,
                0,
                size,
                size,
                size,
                size);
        guiGraphics.blit(
                ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, "textures/gui/jam_textures/frame_4.png"),
                0,
                height-size,
                0,
                0,
                size,
                size,
                size,
                size);
    }

    @Override
    public void tick() {
        super.tick();
        sound = AffixClient.getJammer().getSound();
        volume = AffixClient.getJammer().getVolume();
        if (sound == null){
            jamButtonStop.active = false;
            jamButton.active = false;
            jamSelector.active = false;
            jamVolume.active = false;
            jamInfo.active = true;
            jamSelector.setMessage(Component.literal("No Music has been found!!!"));

            return;
        }
        jamButton.active = !sound.isPlaying();

        jamSelector.setMessage(Component.literal("Music : " + sound.getName()));
        if (AffixClient.jamCooldown > 0) {
            jamButton.setMessage(Component.literal("You can jam again in : " + String.valueOf((int) (AffixClient.jamCooldown / 20)) + " seconds"));
            jamButton.active = false;
        }else {
            jamButton.active = true;
            jamButton.setMessage(Component.literal("Let's jam!"));

        }
        jamButtonStop.active = sound.isPlaying();
        jamSelector.active =  !sound.isPlaying();
        refreshTracks.active = !sound.isPlaying();

    }

    @Override
    protected void init() {
        super.init();
        if (SoundAssets.getAllSounds().isEmpty()) {
            SoundAssets.refreshOrInnitSounds();
            try {
                AffixClient.getJammer().setSound(SoundAssets.getSound(AffixClient.getJamTracks().getFirst().getAbsolutePath()));
            }catch (Exception ignored){}
        }
        sound = AffixClient.getJammer().getSound();
        volume = AffixClient.getJammer().getVolume();

        renderButtons();
    }

    @Override
    public void onClose() {
        super.onClose();
    }

    private void renderButtons() {

        int spacing = 20;
        int buttonWidth = 200;
        int buttonHeight = 20;
        if (SoundAssets.getAllSounds().isEmpty())
            SoundAssets.refreshOrInnitSounds();


        jamButton = Button.builder(Component.literal("Let's jam!"), button -> {
                Minecraft.getInstance().setScreen(null);
                JamScreenHandler.setScreenOpen(false);
                    sound.play();
                    sound.setVolume(volume);
                    ClientPlayNetworking.send(new JamRequestPacketC2S(1));
                })
                .bounds((int)(width / 1.1f) - spacing - buttonWidth, height/2 - 10, buttonWidth, buttonHeight)
                        .build();
        jamShop = Button.builder(Component.literal("\uD83D\uDED2"), button -> {
                    Minecraft.getInstance().setScreen(new ShopSelectionScreen(Component.literal("Shopping Time")));
                })
                .bounds((int) ((int)(width / 1.1f) - spacing * 2.5 - buttonWidth), height/2 - 10, buttonHeight, buttonHeight)
                .build();

        jamButtonStop = Button.builder(Component.literal("Let's not jam!"), button -> {
                    sound.stop();
                    StyleSystemManager styleSystemManager = AffixClient.getStyleSystemManager();
                    styleSystemManager.reset();
                })
                .bounds((int)(width / 1.1f) - spacing - buttonWidth - spacing, height/2 - 10 + spacing * 2, buttonWidth, buttonHeight)
                .build();

        jamInfo = Button.builder(Component.literal("Info / Tutorial"), button -> {
                })
                .bounds((int)(width / 1.1f) - spacing - buttonWidth - spacing, height/2 - 10 - spacing * 2, buttonWidth, buttonHeight)
                .tooltip(Tooltip.create(Component.literal("Add your ogg files in the affixMusic folder that is in your minecraft instance location (the same where texture packs are located!)")))
                .build();

        jamVolume = new AbstractSliderButton((int)(width / 1.1f) - spacing - buttonWidth - spacing * 2, height/2 - 10 + spacing * 4, buttonWidth, buttonHeight, Component.literal("Volume : " + (int)(AffixClient.getJammer().getVolume() * 100)), AffixClient.getJammer().getVolume()) {
            @Override
            protected void updateMessage() {
                setMessage(Component.literal("Volume : " + (int) (value * 100)));
            }

            @Override
            protected void applyValue() {
                sound.setVolume(value);
                AffixClient.getJammer().setVolume((float) (value));
            }
        };

        jamSelector = Button.builder(Component.literal(sound != null ? "Music : " + sound.getName() : "No Music has been found!!!"), button -> {
                    int pos = AffixClient.getJammer().getIndex();
                    if (pos != -1) {
                        pos++;
                        if (AffixClient.getJamTracks().size() > pos) {
                            AffixClient.getJammer().setSound(SoundAssets.getSound(AffixClient.getJamTracks().get(pos).getAbsolutePath()));
                            AffixClient.getJammer().setIndex(pos);
                        } else {
                            AffixClient.getJammer().setSound(SoundAssets.getSound(AffixClient.getJamTracks().getFirst().getAbsolutePath()));
                            AffixClient.getJammer().setIndex(0);

                        }
                    }

                })
                .bounds((int) (width / 1.1f) - spacing - buttonWidth - spacing * 2, height / 2 - 10 - spacing * 4, buttonWidth, buttonHeight)
                .build();

        refreshTracks = Button.builder(Component.literal("\uD83D\uDDD8"), button -> {
                    try {
                        AffixClient.setJamTracks(ReadFromFolder.read());
                        if (!AffixClient.getJamTracks().isEmpty()) {
                            AffixClient.getJammer().setSound(SoundAssets.getSound(AffixClient.getJamTracks().getFirst().getAbsolutePath()));
                            AffixClient.getJammer().setIndex(0);
                        }else {
                            AffixClient.getJammer().setSound(null);
                            AffixClient.getJammer().setIndex(0);
                        }
                    } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                        AffixClient.setJamTracks(new ArrayList<>());
                    }
                    SoundAssets.refreshOrInnitSounds();

                })
                .bounds((int) ((int)(width / 1.1f) - spacing * 2.5), height/2 - 10 - spacing * 4, buttonHeight, buttonHeight)
                .tooltip(Tooltip.create(Component.literal("Refresh the listing of tracks (USE IF IT BUGS OUT)")))
                .build();



        addRenderableWidget(jamButton);
        addRenderableWidget(jamShop);
        addRenderableWidget(jamButtonStop);
        addRenderableWidget(jamVolume);
        addRenderableWidget(jamInfo);
        addRenderableWidget(jamSelector);
        addRenderableWidget(refreshTracks);
    }

    private void renderEnderHeart(GuiGraphics guiGraphics) {
        int width = guiGraphics.guiWidth();
        int height = guiGraphics.guiHeight();
        int size = 256;


        double xOffset = 0;
        double yOffset = Math.sin(timerSinceOpen) * 20;

        if (sound != null && sound.isPlaying()){
            size += (int) (sound.getAmplitude() * 60);
            xOffset = Math.cos(timerSinceOpen) * 20;
        }
        guiGraphics.blit(
                ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, "textures/glyphs/ender_heart.png"),
                (int) ((double) width / 4 - (double) size / 2 + xOffset),
                (int) ((double) height /2 - (double) size /2 + yOffset),
                0,
                0,
                size,
                size,
                size,
                size);

    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}

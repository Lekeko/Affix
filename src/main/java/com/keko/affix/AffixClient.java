package com.keko.affix;

import com.keko.affix.affixLogics.*;
import com.keko.affix.effects.ModStatusEffects;
import com.keko.affix.entity.ModEntitiesRenderer;
import com.keko.affix.entity.infernalBeacon.InfernalBeacon;
import com.keko.affix.items.ModItems;
import com.keko.affix.midLib.AffixConfigs;
import com.keko.affix.packet.AnomalyPacketC2S;
import com.keko.affix.packet.JamRequestPacketC2S;
import com.keko.affix.packet.PunchRequestPacketC2S;
import com.keko.affix.packet.networking.ModMessagesServer;
import com.keko.affix.particle.ModParticles;
import com.keko.affix.particle.RocketParticleExplosion;
import com.keko.affix.stylePointsManager.ReadFromFolder;
import com.keko.affix.stylePointsManager.pointsSystem.StyleSystemManager;
import com.keko.affix.stylePointsManager.soundSystem.Jammer;
import com.keko.affix.stylePointsManager.soundSystem.Sound;
import com.keko.affix.stylePointsManager.soundSystem.SoundAssets;
import com.keko.affix.util.cc.ScoreComponent;
import eu.midnightdust.lib.config.MidnightConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.keko.affix.Affix.MOD_ID;
import static com.keko.affix.affixLogics.AffixShaderHandlers.amplitudeVisualizer;
import static com.keko.affix.util.cc.MyComponents.SCORE;

public class AffixClient implements ClientModInitializer {
    public static float mirrorOffset = 0;
    public static boolean renderMirror = false;
    public static float transition = 0;
    public static float armTranzition = 0;
    public static float acceleratorShaderTranzition = 0;
    public static int xAnom = 1111111111;     //stay mad
    public static int zAnom = 1111111111;    //stay mad
    public static int yAnom = 1111111111;   //stay mad
    public static float jamCooldown = 0;   //stay mad
    public static int anomalyCooldown = 0;
    public static StyleSystemManager styleSystemManager;
    private static ArrayList<PlayerPhase> phasingPlayers = new ArrayList<>();
    private static List<File> jamTracks;
    private static Jammer jammer;

    public static void addPlayerClient(PlayerPhase playerPhase) {
        for (PlayerPhase playerPhase1 : phasingPlayers)
            if (playerPhase1.getPlayer() == playerPhase.getPlayer())
                return;
        phasingPlayers.add(playerPhase);
    }

    public static boolean isPLayerClient(Player player) {
        for (PlayerPhase playerPhase1 : phasingPlayers)
            if (playerPhase1.getPlayer().getUUID().equals(player.getUUID()))
                return true;
        return false;
    }

    public static void removeClientPlayer(PlayerPhase playerPhase1) {
        phasingPlayers.removeIf(playerPhase -> playerPhase.getPlayer().getUUID().equals(playerPhase1.getPlayer().getUUID()));
    }

    @Override
    public void onInitializeClient() {
        ParticleFactoryRegistry.getInstance().register(ModParticles.ROCKET_EXPLOSION, RocketParticleExplosion.Factory::new);
        ModEntitiesRenderer.register();
        ModMessagesServer.registerS2CPacket();
        KeyBinds.registerKeyInputs();
        try {
            jamTracks = ReadFromFolder.read();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            jamTracks = new ArrayList<>();
        }
        MidnightConfig.init(MOD_ID, AffixConfigs.class);
        jammer = new Jammer(null, 0.5f, 0);
        styleSystemManager = new StyleSystemManager();
        WorldRenderEvents.LAST.register((ctx) -> {
            try {
                if (mirrorOffset>0) {
                    AffixShaderHandlers.mirrorShaderActivator();
                }
                if (transition > 0)
                    AffixShaderHandlers.phaserShaderActivator();
                if (Minecraft.getInstance().player != null){
                    if (acceleratorShaderTranzition > 0 || Minecraft.getInstance().player.hasEffect(ModStatusEffects.ACCELERATED))
                        AffixShaderHandlers.acceleratorShaderActivator();
                }
                amplitudeVisualizer();
            }catch (Exception ignored){
            }

        });

        HudRenderCallback.EVENT.register(((guiGraphics, deltaTracker) -> {


            if (renderMirror) {
                if (mirrorOffset < Math.PI / 2)
                    mirrorOffset += deltaTracker.getGameTimeDeltaTicks() / 6;
            } else {
                    if (mirrorOffset > 0)
                        mirrorOffset -= deltaTracker.getGameTimeDeltaTicks() / 5;
                    else
                        mirrorOffset = 0;
                }
            if (Minecraft.getInstance().player == null) return;

            AffixRenderingHandlers.handleSpellHud(guiGraphics, deltaTracker);
            AffixRenderingHandlers.handleGlyphsAccel(guiGraphics, deltaTracker);
            AffixRenderingHandlers.handleMissileMark(guiGraphics, deltaTracker);
            if (Minecraft.getInstance().player.hasEffect(ModStatusEffects.OTHERWORDLY))
                AffixRenderingHandlers.handleJammingHud(guiGraphics, deltaTracker);
            else
                AffixRenderingHandlers.setTick(0);
        }));



        ClientTickEvents.START_CLIENT_TICK.register((minecraft -> {
            if (transition > 0) transition-= 0.01f; //linear cuz im not gonna bother with this one
            if (anomalyCooldown > 0) anomalyCooldown--;
            if (armTranzition > 0) armTranzition /= 1.06;
            if (acceleratorShaderTranzition > 0) acceleratorShaderTranzition -= 0.044;
            if (xAnom == 1111111111 && anomalyCooldown < 1) createAnomalyCoords(minecraft);
            if (playerInVicinity(minecraft)) initiateAnomaly(minecraft);

            if (minecraft.player != null) {
                if (jamCooldown > 0)
                    jamCooldown--;
                renderMirror = minecraft.player.getUseItem().is(ModItems.HEAVY_MIRROR);

                if (getJammer().getSound() == null) {
                    if (minecraft.player.hasEffect(ModStatusEffects.OTHERWORDLY))
                        ClientPlayNetworking.send(new JamRequestPacketC2S(10));

                } else {
                    if (minecraft.player.hasEffect(ModStatusEffects.OTHERWORDLY) && !getJammer().getSound().isPlaying())
                        ClientPlayNetworking.send(new JamRequestPacketC2S(10));
                    if (!minecraft.player.isAlive()) {
                        ClientPlayNetworking.send(new JamRequestPacketC2S(10));
                        getJammer().getSound().stop();
                        styleSystemManager.reset();
                    }
                    if (minecraft.player.hasEffect(ModStatusEffects.OTHERWORDLY) && getJammer().getSound().isPlaying()) {
                        AffixClient.jamCooldown = AffixConfigs.jamCooldown * 20;
                        ClientPlayNetworking.send(new PunchRequestPacketC2S(false, AffixConstantsPunch.REMAKE_FISTS));
                    }


                }
                if (styleSystemManager != null)
                    styleSystemManager.tick();


            }else {
                Sound sound = getJammer().getSound();
                if (sound != null && sound.isPlaying()) {
                    sound.stop();
                    styleSystemManager.reset();
                }
            }
        }));




    }


    public static List<File> getJamTracks() {
        return jamTracks;
    }

    public static Jammer getJammer() {
        return jammer;
    }

    public static void setJamTracks(List<File> jamTracks) {
        AffixClient.jamTracks = jamTracks;
    }


    private void initiateAnomaly(Minecraft minecraft) {
        yAnom = 300;
        BlockState state = minecraft.level.getBlockState(new BlockPos(xAnom, yAnom, zAnom));
        while (state.is(Blocks.AIR) && yAnom > -64) state = minecraft.level.getBlockState(new BlockPos(xAnom, --yAnom, zAnom));
        yAnom += 5;
        ClientPlayNetworking.send(new AnomalyPacketC2S(xAnom, yAnom ,zAnom));
        xAnom = 1111111111;
        zAnom = 1111111111;
        yAnom = 1111111111;
        anomalyCooldown = 20 * 60 * 2;
    }

    private boolean playerInVicinity(Minecraft minecraft) {
        if (minecraft.player != null && minecraft.level != null) {
            return minecraft.player.distanceToSqr(xAnom, minecraft.player.getY(), zAnom) < 20;
        }
        return false;
    }

    private void createAnomalyCoords(Minecraft minecraft) {
        if (minecraft.player != null && minecraft.level != null) {
            xAnom = (int) (minecraft.level.random.nextIntBetweenInclusive(1, 256) - 128 + minecraft.player.getX());
            zAnom = (int) (minecraft.level.random.nextIntBetweenInclusive(1, 256) - 128 + minecraft.player.getZ());

            yAnom = 200;
            BlockState state = minecraft.level.getBlockState(new BlockPos(xAnom, yAnom, zAnom));
            while (state.is(Blocks.AIR) && yAnom > -64) state = minecraft.level.getBlockState(new BlockPos(xAnom, --yAnom, zAnom));


        }
    }

    public static StyleSystemManager getStyleSystemManager() {
        return styleSystemManager;
    }
}

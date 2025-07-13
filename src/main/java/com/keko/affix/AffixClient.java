package com.keko.affix;

import com.keko.affix.affixLogics.*;
import com.keko.affix.effects.ModStatusEffects;
import com.keko.affix.entity.ModEntitiesRenderer;
import com.keko.affix.entity.infernalBeacon.InfernalBeacon;
import com.keko.affix.items.ModItems;
import com.keko.affix.items.custom.Accelerator;
import com.keko.affix.midLib.AffixConfigs;
import com.keko.affix.packet.AnomalyPacketC2S;
import com.keko.affix.packet.CrazyStatusC2S;
import com.keko.affix.packet.networking.ModMessagesServer;
import com.keko.affix.sounds.ModSounds;
import com.mojang.blaze3d.systems.RenderSystem;
import foundry.veil.api.client.render.VeilRenderSystem;
import foundry.veil.api.client.render.post.PostPipeline;
import foundry.veil.api.client.render.post.PostProcessingManager;
import foundry.veil.api.client.util.Easing;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.Input;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;

public class AffixClient implements ClientModInitializer {
    public static boolean renderMirror = false;
    public static float mirrorOffset = 0;
    public static float transition = 0;
    public static float armTranzition = 0;
    public static float acceleratorShaderTranzition = 0;
    private static ArrayList<Glyph> spawnedGlyphs = new ArrayList<>();
    public static int width = 0;
    public static int height = 0;
    public static float rage = 0;
    public static int xAnom = 1111111111;   //stay mad
    public static int zAnom = 1111111111;   //stay mad
    public static int yAnom = 1111111111;   //stay mad
    public static int anomalyCooldown = 0;
    private static ArrayList<PlayerPhase> phasingPlayers = new ArrayList<>();
    private static ArrayList<String> keyInputs = new ArrayList<>();
    private static String lastMovement = "nope";
    private static boolean usedItemATickAgo = false;

    public static void addGlyph() {
        spawnedGlyphs.add(GlyphSpawner.createGlyph(rage));
        rage += 0.2f;
        if (rage > 1.2f)
            ClientPlayNetworking.send(new CrazyStatusC2S(true));
    }

    public static ArrayList<String> getKeyInputs() {
        return keyInputs;
    }

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
        Iterator<PlayerPhase> iterator = phasingPlayers.iterator();
        while (iterator.hasNext()) {
            PlayerPhase playerPhase = iterator.next();
            if (playerPhase.getPlayer().getUUID().equals(playerPhase1.getPlayer().getUUID())) {
                iterator.remove();
            }
        }
    }

    @Override
    public void onInitializeClient() {

        ModEntitiesRenderer.register();
        ModMessagesServer.registerS2CPacket();
        KeyBinds.registerKeyInputs();

        WorldRenderEvents.LAST.register((ctx) -> {
            try {
                if (renderMirror) {
                    mirrorShaderActivator();
                    renderMirror = checkForMirror();
                }
                if (transition > 0)
                    phaserShaderActivator();
                if (Minecraft.getInstance().player != null){
                    if (acceleratorShaderTranzition > 0 || Minecraft.getInstance().player.hasEffect(ModStatusEffects.ACCELERATED))
                        acceleratorShaderActivator();
                }

                makeBlackWholeShaders();
            }catch (Exception e){
            }
        });

        HudRenderCallback.EVENT.register(((guiGraphics, deltaTracker) -> {
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


            GlyphMarking.renderCoordinates(guiGraphics, deltaTracker, mirrorOffset);
            width = guiGraphics.guiWidth();
            height = guiGraphics.guiHeight();
            if (rage > 0)
                rage-= 0.005f;

            if (!spawnedGlyphs.isEmpty()) {
                Iterator<Glyph> iterator = spawnedGlyphs.iterator();
                while (iterator.hasNext()) {
                    Glyph glyph = iterator.next();
                    glyph.move();

                    if (Mth.abs(glyph.getDeltaX()) < 0.2 && Mth.abs(glyph.getDeltaY()) < 0.2)
                        iterator.remove();
                    else {

                        Color color = Color.decode(AffixConfigs.rageAccentColor);
                        float red = (float) color.getRed() / 255 + 1.3f - Math.min(rage, 1.3f);
                        float green = (float) color.getGreen() / 255 + 1.3f -  Math.min(rage, 1.3f);
                        float blue = (float) color.getBlue() / 255 + 1.3f -  Math.min(rage, 1.3f);

                        float alpha = Mth.abs(glyph.getDeltaX()) + Mth.abs(glyph.getDeltaY()) / 2;


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

        }));

        ClientTickEvents.START_CLIENT_TICK.register((minecraft -> {
            if (transition > 0) transition-= 0.01f; //linear cuz im not gonna bother with this one
            if (anomalyCooldown > 0) anomalyCooldown--;
            if (armTranzition > 0) armTranzition /= 1.06;
            if (acceleratorShaderTranzition > 0) acceleratorShaderTranzition -= 0.044;
            if (xAnom == 1111111111 && anomalyCooldown < 1) createAnomalyCoords(minecraft);
            if (playerInVicinity(minecraft)) initiateAnomaly(minecraft);


        }));


    }

    private void makeBlackWholeShaders() {
        Level level = Minecraft.getInstance().level;
        if (level == null || Minecraft.getInstance().player == null) return;
        BlockPos pos = Minecraft.getInstance().player.getOnPos();
        for (InfernalBeacon beacon : level.getEntitiesOfClass(InfernalBeacon.class, new AABB(pos.getX() + 120, pos.getY() + 80, pos.getZ() + 120, pos.getX() - 120, pos.getY() - 80, pos.getZ() - 120))) {

            beacon.addTimer(Minecraft.getInstance().getTimer().getGameTimeDeltaPartialTick(true));
            blackWholeShaderActivator(beacon.getPosition(1.0f), beacon.getTimer());
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

    private boolean checkForMirror() {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player != null){
            if (!minecraft.player.getMainHandItem().is(ModItems.HEAVY_MIRROR) && !minecraft.player.getOffhandItem().is(ModItems.HEAVY_MIRROR) )
                return false;
        }
        return true;
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

            yAnom = 250;
            BlockState state = minecraft.level.getBlockState(new BlockPos(xAnom, yAnom, zAnom));
            while (state.is(Blocks.AIR) && yAnom > -64) state = minecraft.level.getBlockState(new BlockPos(xAnom, --yAnom, zAnom));

            yAnom += 5;

        }
    }

    private void mirrorShaderActivator() {
        try {
            PostProcessingManager postProcessingManager = VeilRenderSystem.renderer().getPostProcessingManager();
            PostPipeline postPipeline = postProcessingManager.getPipeline(ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, "mirror"));
            assert postPipeline != null;
            postPipeline.getUniformSafe("mirrorOffset").setFloat(Easing.EASE_OUT_CIRC.ease(mirrorOffset));
            Color color = getClientColor();
            postPipeline.getUniformSafe("shaderAccent").setVector(color.getRed(), color.getGreen(), color.getBlue());
            postProcessingManager.runPipeline(postPipeline);
        }catch (Exception ignored){
        }
    }

    private void acceleratorShaderActivator() {
        try {
            PostProcessingManager postProcessingManager = VeilRenderSystem.renderer().getPostProcessingManager();
            PostPipeline postPipeline = postProcessingManager.getPipeline(ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, "accelerator"));
            assert postPipeline != null;
            Color color = getClientColor();
            postPipeline.getUniformSafe("shaderAccent").setVector(color.getRed(), color.getGreen(), color.getBlue());
            postPipeline.getUniformSafe("tranzitioner").setFloat(Accelerator.TRANZITIONER - acceleratorShaderTranzition);
            postPipeline.getUniformSafe("shaderStrength").setFloat(AffixConfigs.acceleratedEffectStrength);
            postProcessingManager.runPipeline(postPipeline);

        }catch (Exception ignored){
        }
    }

    private void blackWholeShaderActivator(Vec3 pos, float timer) {
        try {
            PostProcessingManager postProcessingManager = VeilRenderSystem.renderer().getPostProcessingManager();
            PostPipeline postPipeline = postProcessingManager.getPipeline(ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, "black_whole"));
            assert postPipeline != null;
            Color color = getClientColor();
            postPipeline.getUniformSafe("pos").setVector((float) pos.x, (float) pos.y, (float) pos.z);
            postPipeline.getUniformSafe("timer").setFloat(timer);

            postProcessingManager.runPipeline(postPipeline);

        }catch (Exception ignored){
        }
    }

    private void phaserShaderActivator() {

        try {

            PostProcessingManager postProcessingManager = VeilRenderSystem.renderer().getPostProcessingManager();
            PostPipeline postPipeline = postProcessingManager.getPipeline(ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, "whiteblack"));
            assert postPipeline != null;
            Color color = getClientColor();
            postPipeline.getUniformSafe("shaderAccent").setVector(color.getRed(), color.getGreen(), color.getBlue());
            postPipeline.getUniformSafe("transition").setFloat(transition);
            postProcessingManager.runPipeline(postPipeline);
        }catch (Exception ignored){
        }

    }

    public static Color getClientColor() {
        try {
            return Color.decode(AffixConfigs.shaderAccentColor);
        }catch (Exception e){
            return new Color(255, 255, 255);
        }
    }

}

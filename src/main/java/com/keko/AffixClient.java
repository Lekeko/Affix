package com.keko;

import com.keko.affixLogics.*;
import com.keko.effects.ModStatusEffects;
import com.keko.entity.ModEntitiesModel;
import com.keko.entity.ModEntitiesRenderer;
import com.keko.items.ModItems;
import com.keko.items.custom.Accelerator;
import com.keko.packet.AnomalyPacketC2S;
import com.keko.packet.CrazyStatusC2S;
import com.keko.packet.networking.ModMessagesServer;
import com.mojang.blaze3d.systems.RenderSystem;
import foundry.veil.api.client.render.VeilRenderSystem;
import foundry.veil.api.client.render.post.PostPipeline;
import foundry.veil.api.client.render.post.PostProcessingManager;
import foundry.veil.api.client.util.Easings;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

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
    public static int xAnom = 1111111111;
    public static int zAnom = 1111111111;
    public static int yAnom = 1111111111;
    public static int anomalyCooldown = 0;
    private static ArrayList<PlayerPhase> phasingPlayers = new ArrayList<>();


    public static void addGlyph() {
        spawnedGlyphs.add(GlyphSpawner.createGlyph(rage));
        rage += 0.2f;
        if (rage > 1.2f)
            ClientPlayNetworking.send(new CrazyStatusC2S(true));
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
        ModEntitiesModel.register();
        ModMessagesServer.registerS2CPacket();
        KeyBinds.registerKeyInputs();

        WorldRenderEvents.END.register((ctx) -> {
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
            }catch (Exception e){
                System.out.println(e);
            }
        });

        HudRenderCallback.EVENT.register(((guiGraphics, deltaTracker) -> {
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
                        float red = 1;
                        float green = 1-rage;
                        float blue = 1-rage;

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

        ClientTickEvents.END_CLIENT_TICK.register((minecraft -> {
            if (transition > 0) transition-= 0.01f; //linear cuz im not gonna bother with this one
            if (anomalyCooldown > 0) anomalyCooldown--;
            if (armTranzition > 0) armTranzition /= 1.06;
            if (acceleratorShaderTranzition > 0) acceleratorShaderTranzition -= 0.044;
            if (xAnom == 1111111111 && anomalyCooldown < 1) createAnomalyCoords(minecraft);
            if (playerInVicinity(minecraft)) initiateAnomaly(minecraft);

        }));


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

            yAnom = 300;
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
            postPipeline.setFloat("mirrorOffset", Easings.Easing.easeOutCirc.ease(mirrorOffset));
            postProcessingManager.runPipeline(postPipeline);
        }catch (Exception ignored){
        }
    }

    private void acceleratorShaderActivator() {
        try {
            PostProcessingManager postProcessingManager = VeilRenderSystem.renderer().getPostProcessingManager();
            PostPipeline postPipeline = postProcessingManager.getPipeline(ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, "accelerator"));
            assert postPipeline != null;
            postPipeline.setFloat("tranzitioner", Accelerator.TRANZITIONER - acceleratorShaderTranzition);
            postProcessingManager.runPipeline(postPipeline);
        }catch (Exception ignored){
        }
    }

    private void phaserShaderActivator() {
        try {
            PostProcessingManager postProcessingManager = VeilRenderSystem.renderer().getPostProcessingManager();
            PostPipeline postPipeline = postProcessingManager.getPipeline(ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, "whiteblack"));
            assert postPipeline != null;
            postPipeline.setFloat("transition", transition);
            postProcessingManager.runPipeline(postPipeline);
        }catch (Exception ignored){
        }
    }

}

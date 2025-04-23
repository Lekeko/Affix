package com.keko.packet.networking;

import com.keko.Affix;
import com.keko.affixLogics.DodgeHandler;
import com.keko.effects.ModStatusEffects;
import com.keko.entity.ModEntities;
import com.keko.entity.fabricPocket.FabricPocket;
import com.keko.packet.*;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;

import java.awt.*;

public class ModMessagesClient {
    public static final ResourceLocation RENDER_MIRROR_SHADER = ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, "render_mirror_shader_tp");
    public static final ResourceLocation SEND_PLAYER_FOR_DODGE = ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, "SEND_PLAYER_FOR_DODGE_TP".toLowerCase());
    public static final ResourceLocation SPAWN_GENERIC_PARTICLE = ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, "SPAWN_GENERIC_PARTICLE_TP".toLowerCase());
    public static final ResourceLocation CRAAZY_STATUS = ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, "CRAAZY_STATUS_TP".toLowerCase());
    public static final ResourceLocation ANOMALY_ID = ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, "ANOMALY_ID_TP".toLowerCase());
    public static final ResourceLocation SYNC_PHASERS_ID = ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, "SYNC_PHASERS_ID_TP".toLowerCase());
    public static final ResourceLocation SYNC_PHASERS_REMOVER_ID = ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, "SYNC_PHASERS_REMOVER_ID_TP".toLowerCase());


    public static void registerC2SPacket(){
        PayloadTypeRegistry.playS2C().register(GenericSpawnerParticleS2C.ID, GenericSpawnerParticleS2C.CODEC);
        PayloadTypeRegistry.playS2C().register(RenderHeavyMirrorShaderS2C.ID, RenderHeavyMirrorShaderS2C.CODEC);
        PayloadTypeRegistry.playC2S().register(SendPlayerToDodgeC2S.ID, SendPlayerToDodgeC2S.CODEC);
        PayloadTypeRegistry.playC2S().register(CrazyStatusC2S.ID, CrazyStatusC2S.CODEC);
        PayloadTypeRegistry.playC2S().register(AnomalyPacketC2S.ID, AnomalyPacketC2S.CODEC);
        PayloadTypeRegistry.playS2C().register(SyncPhaserS2C.ID, SyncPhaserS2C.CODEC);
        PayloadTypeRegistry.playS2C().register(SyncPhaserRemoverS2C.ID, SyncPhaserRemoverS2C.CODEC);

        ServerPlayNetworking.registerGlobalReceiver(SendPlayerToDodgeC2S.ID, (payload, context) -> {
            context.server().execute(() -> {
                DodgeHandler.dodgePlayer(context.player(), payload.forwardImpulse(), payload.leftImpulse(), payload.jump());
            });
        });

        ServerPlayNetworking.registerGlobalReceiver(CrazyStatusC2S.ID, (payload, context) -> {
            context.server().execute(() -> {
                context.player().addEffect(new MobEffectInstance(ModStatusEffects.UNSTABLE, 30 * 20,0));
            });
        });

        ServerPlayNetworking.registerGlobalReceiver(AnomalyPacketC2S.ID, (payload, context) -> {
            context.server().execute(() -> {
                FabricPocket fabricPocket = new FabricPocket(ModEntities.FABRIC_POCKET_ENTITY_TYPE, context.player().level());
                fabricPocket.setPos(
                        payload.x(),
                        payload.y(),
                        payload.z()
                );
                context.player().level().addFreshEntity(fabricPocket);
            });
        });
    }
}

package com.keko.affix.packet.networking;

import com.keko.affix.Affix;
import com.keko.affix.AffixClient;
import com.keko.affix.affixLogics.PlayerPhase;
import com.keko.affix.entity.infernalDragon.InfernalDragon;
import com.keko.affix.packet.*;
import com.keko.affix.stylePointsManager.pointsSystem.StyleSystemManager;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import java.util.Objects;

public class ModMessagesServer {


    public static void registerS2CPacket(){



        ClientPlayNetworking.registerGlobalReceiver(GenericSpawnerParticleS2C.ID, (payload, context) -> {
            context.client().execute(() -> {
                try {
                    context.client().level.addParticle(ParticleTypes.END_ROD, payload.x(), payload.y(), payload.z(), payload.deltaX(), payload.deltaY(), payload.deltaZ());
                }catch (Exception e){Affix.LOGGER.error("Failed to add particle of dash!");}
            });
        });

        ClientPlayNetworking.registerGlobalReceiver(SyncPhaserS2C.ID, (payload, context) -> {
            context.client().execute(() -> {
                try {
                    AffixClient.addPlayerClient(new PlayerPhase((Player) context.player().level().getEntity(payload.id()), Objects.requireNonNull(context.player().level().getEntity(payload.id())).getViewVector(1.0f).add(0,5.2f, 0)));
                }catch (Exception ignored){}
            });
        });
        ClientPlayNetworking.registerGlobalReceiver(SyncPhaserRemoverS2C.ID, (payload, context) -> {
            context.client().execute(() -> {
                try {
                    AffixClient.removeClientPlayer(new PlayerPhase((Player) context.player().level().getEntity(payload.id()), Objects.requireNonNull(context.player().level().getEntity(payload.id())).getViewVector(1.0f).add(0,5.2f, 0)));
                }catch (Exception ignored){}
            });
        });

        ClientPlayNetworking.registerGlobalReceiver(AwardForKillingS2C.ID, (payload, context) -> {
            context.client().execute(() -> {
                try {
                    AffixClient.getStyleSystemManager().awardKill(payload.score(), payload.style());
                }catch (Exception ignored){}
            });
        });

    }
}

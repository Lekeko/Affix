package com.keko.packet.networking;

import com.keko.Affix;
import com.keko.AffixClient;
import com.keko.affixLogics.DodgeHandler;
import com.keko.affixLogics.PlayerPhase;
import com.keko.packet.*;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

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
    }
}

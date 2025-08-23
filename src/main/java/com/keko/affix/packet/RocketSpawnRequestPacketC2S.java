package com.keko.affix.packet;

import com.keko.affix.packet.networking.ModMessagesClient;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

public record RocketSpawnRequestPacketC2S(int id) implements CustomPacketPayload {
    public static final Type<RocketSpawnRequestPacketC2S> ID = new Type<>(ModMessagesClient.R_REQUEST);
    public static final StreamCodec<RegistryFriendlyByteBuf, RocketSpawnRequestPacketC2S> CODEC = StreamCodec.composite(

            ByteBufCodecs.INT, RocketSpawnRequestPacketC2S::id,

            RocketSpawnRequestPacketC2S::new);

    public @NotNull Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}

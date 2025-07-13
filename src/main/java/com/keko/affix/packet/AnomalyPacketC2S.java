package com.keko.affix.packet;

import com.keko.affix.packet.networking.ModMessagesClient;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

public record AnomalyPacketC2S(int x, int y, int z) implements CustomPacketPayload {
    public static final Type<AnomalyPacketC2S> ID = new Type<>(ModMessagesClient.ANOMALY_ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, AnomalyPacketC2S> CODEC = StreamCodec.composite(

            ByteBufCodecs.INT, AnomalyPacketC2S::x,
            ByteBufCodecs.INT, AnomalyPacketC2S::y,
            ByteBufCodecs.INT, AnomalyPacketC2S::z,

            AnomalyPacketC2S::new);

    public @NotNull Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}

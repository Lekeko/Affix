package com.keko.affix.packet;

import com.keko.affix.packet.networking.ModMessagesClient;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public record SpearSummonPacketC2S(int x, int y, int z, int Id) implements CustomPacketPayload {
    public static final Type<SpearSummonPacketC2S> ID = new Type<>(ModMessagesClient.SPEAR_SUMMON_ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, SpearSummonPacketC2S> CODEC = StreamCodec.composite(

            ByteBufCodecs.INT, SpearSummonPacketC2S::x,
            ByteBufCodecs.INT, SpearSummonPacketC2S::y,
            ByteBufCodecs.INT, SpearSummonPacketC2S::z,
            ByteBufCodecs.INT, SpearSummonPacketC2S::Id ,

            SpearSummonPacketC2S::new);

    public @NotNull Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}

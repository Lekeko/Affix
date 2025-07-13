package com.keko.affix.packet;

import com.keko.affix.packet.networking.ModMessagesClient;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

public record DragonSummonPacketC2S(int x, int y, int z) implements CustomPacketPayload {
    public static final Type<DragonSummonPacketC2S> ID = new Type<>(ModMessagesClient.DRAGON_SUMMON_ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, DragonSummonPacketC2S> CODEC = StreamCodec.composite(

            ByteBufCodecs.INT, DragonSummonPacketC2S::x,
            ByteBufCodecs.INT, DragonSummonPacketC2S::y,
            ByteBufCodecs.INT, DragonSummonPacketC2S::z,

            DragonSummonPacketC2S::new);

    public @NotNull Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}

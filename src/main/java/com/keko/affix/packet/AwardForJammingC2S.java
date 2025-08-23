package com.keko.affix.packet;

import com.keko.affix.packet.networking.ModMessagesClient;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

public record AwardForJammingC2S(float score, float style) implements CustomPacketPayload {
    public static final Type<AwardForJammingC2S> ID = new Type<>(ModMessagesClient.AWARD_FOR_JAMMING);
    public static final StreamCodec<RegistryFriendlyByteBuf, AwardForJammingC2S> CODEC = StreamCodec.composite(
            ByteBufCodecs.FLOAT, AwardForJammingC2S::score,
            ByteBufCodecs.FLOAT, AwardForJammingC2S::style,

            AwardForJammingC2S::new);

    public @NotNull Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}

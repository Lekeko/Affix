package com.keko.affix.packet;

import com.keko.affix.packet.networking.ModMessagesClient;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

public record AwardForKillingS2C(float score, float style) implements CustomPacketPayload {
    public static final Type<AwardForKillingS2C> ID = new Type<>(ModMessagesClient.AWARD_FOR_KILL);
    public static final StreamCodec<RegistryFriendlyByteBuf, AwardForKillingS2C> CODEC = StreamCodec.composite(
            ByteBufCodecs.FLOAT, AwardForKillingS2C::score,
            ByteBufCodecs.FLOAT, AwardForKillingS2C::style,

            AwardForKillingS2C::new);

    public @NotNull Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}

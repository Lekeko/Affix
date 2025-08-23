package com.keko.affix.packet;

import com.keko.affix.packet.networking.ModMessagesClient;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

public record ConfirmPurchaseC2S(int ga, int ns, int di, int em, int tk) implements CustomPacketPayload {
    public static final Type<ConfirmPurchaseC2S> ID = new Type<>(ModMessagesClient.PURCHASE);
    public static final StreamCodec<RegistryFriendlyByteBuf, ConfirmPurchaseC2S> CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, ConfirmPurchaseC2S::ga,
            ByteBufCodecs.INT, ConfirmPurchaseC2S::ns,
            ByteBufCodecs.INT, ConfirmPurchaseC2S::di,
            ByteBufCodecs.INT, ConfirmPurchaseC2S::em,
            ByteBufCodecs.INT, ConfirmPurchaseC2S::tk,

            ConfirmPurchaseC2S::new);

    public @NotNull Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}

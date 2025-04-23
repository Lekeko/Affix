package com.keko.packet;

import com.keko.packet.networking.ModMessagesClient;
import com.keko.packet.networking.ModMessagesServer;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

public record CrazyStatusC2S(boolean a) implements CustomPacketPayload {
    public static final Type<CrazyStatusC2S> ID = new Type<>(ModMessagesClient.CRAAZY_STATUS);
    public static final StreamCodec<RegistryFriendlyByteBuf, CrazyStatusC2S> CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL, CrazyStatusC2S::a,

            CrazyStatusC2S::new);

    public @NotNull Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}

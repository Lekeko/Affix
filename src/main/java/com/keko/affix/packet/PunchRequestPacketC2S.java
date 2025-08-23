package com.keko.affix.packet;

import com.keko.affix.packet.networking.ModMessagesClient;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

public record PunchRequestPacketC2S(boolean left, int superP) implements CustomPacketPayload {
    public static final Type<PunchRequestPacketC2S> ID = new Type<>(ModMessagesClient.PUNCH_REQUEST);
    public static final StreamCodec<RegistryFriendlyByteBuf, PunchRequestPacketC2S> CODEC = StreamCodec.composite(

            ByteBufCodecs.BOOL, PunchRequestPacketC2S::left,
            ByteBufCodecs.INT, PunchRequestPacketC2S::superP,

            PunchRequestPacketC2S::new);

    public @NotNull Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}

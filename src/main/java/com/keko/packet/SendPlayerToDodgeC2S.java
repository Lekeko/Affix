package com.keko.packet;

import com.keko.packet.networking.ModMessagesClient;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

public record SendPlayerToDodgeC2S(float forwardImpulse, float leftImpulse, boolean jump) implements CustomPacketPayload {
    public static final Type<SendPlayerToDodgeC2S> ID = new Type<>(ModMessagesClient.SEND_PLAYER_FOR_DODGE);
    public static final StreamCodec<RegistryFriendlyByteBuf, SendPlayerToDodgeC2S> CODEC = StreamCodec.composite(
            ByteBufCodecs.FLOAT, SendPlayerToDodgeC2S::forwardImpulse,
            ByteBufCodecs.FLOAT, SendPlayerToDodgeC2S::leftImpulse,
            ByteBufCodecs.BOOL, SendPlayerToDodgeC2S::jump,

            SendPlayerToDodgeC2S::new);

    public @NotNull Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}

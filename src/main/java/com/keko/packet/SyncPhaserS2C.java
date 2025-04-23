package com.keko.packet;

import com.keko.packet.networking.ModMessagesClient;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

public record SyncPhaserS2C(int id) implements CustomPacketPayload {
    public static final Type<SyncPhaserS2C> ID = new Type<>(ModMessagesClient.SYNC_PHASERS_ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, SyncPhaserS2C> CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, SyncPhaserS2C::id,

            SyncPhaserS2C::new);

    public @NotNull Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}

package com.keko.affix.packet;

import com.keko.affix.packet.networking.ModMessagesClient;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

public record SyncPhaserRemoverS2C(int id) implements CustomPacketPayload {
    public static final Type<SyncPhaserRemoverS2C> ID = new Type<>(ModMessagesClient.SYNC_PHASERS_REMOVER_ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, SyncPhaserRemoverS2C> CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, SyncPhaserRemoverS2C::id,

            SyncPhaserRemoverS2C::new);

    public @NotNull Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}

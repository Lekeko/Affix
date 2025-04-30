package com.keko.packet;

import com.keko.packet.networking.ModMessagesClient;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

public record GenericSpawnerParticleS2C(double x, double y, double z, double deltaX, double deltaY, double deltaZ) implements CustomPacketPayload {
    public static final Type<GenericSpawnerParticleS2C> ID = new Type<>(ModMessagesClient.SPAWN_GENERIC_PARTICLE);
    public static final StreamCodec<RegistryFriendlyByteBuf, GenericSpawnerParticleS2C> CODEC = StreamCodec.composite(
            ByteBufCodecs.DOUBLE, GenericSpawnerParticleS2C::x,
            ByteBufCodecs.DOUBLE, GenericSpawnerParticleS2C::y,
            ByteBufCodecs.DOUBLE, GenericSpawnerParticleS2C::z,
            ByteBufCodecs.DOUBLE, GenericSpawnerParticleS2C::deltaX,
            ByteBufCodecs.DOUBLE, GenericSpawnerParticleS2C::deltaY,
            ByteBufCodecs.DOUBLE, GenericSpawnerParticleS2C::deltaZ,

            GenericSpawnerParticleS2C::new);

    public @NotNull Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}

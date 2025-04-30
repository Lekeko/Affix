package com.keko.packet;

import com.keko.packet.networking.ModMessagesClient;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

public record RenderHeavyMirrorShaderS2C (int cube, int red, int green, int blue) implements CustomPacketPayload {
    public static final Type<RenderHeavyMirrorShaderS2C> ID = new Type<>(ModMessagesClient.RENDER_MIRROR_SHADER);
    public static final StreamCodec<RegistryFriendlyByteBuf, RenderHeavyMirrorShaderS2C> CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, RenderHeavyMirrorShaderS2C::cube,
            ByteBufCodecs.INT, RenderHeavyMirrorShaderS2C::red,
            ByteBufCodecs.INT, RenderHeavyMirrorShaderS2C::green,
            ByteBufCodecs.INT, RenderHeavyMirrorShaderS2C::blue,

            RenderHeavyMirrorShaderS2C::new);

    public @NotNull Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}

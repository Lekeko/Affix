package com.keko.affix.packet;

import com.keko.affix.packet.networking.ModMessagesClient;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

public record BeaconSummonPacketC2S(int x, int y, int z, int btype) implements CustomPacketPayload {
    public static final Type<BeaconSummonPacketC2S> ID = new Type<>(ModMessagesClient.BEACON_SUMMON_ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, BeaconSummonPacketC2S> CODEC = StreamCodec.composite(

            ByteBufCodecs.INT, BeaconSummonPacketC2S::x,
            ByteBufCodecs.INT, BeaconSummonPacketC2S::y,
            ByteBufCodecs.INT, BeaconSummonPacketC2S::z,
            ByteBufCodecs.INT, BeaconSummonPacketC2S::btype,

            BeaconSummonPacketC2S::new);

    public @NotNull Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}

package com.keko.packet.networking;

import com.keko.Affix;
import com.keko.packet.RenderHeavyMirrorShaderS2C;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.resources.ResourceLocation;

public class ModMessagesServer {
    public static final ResourceLocation RENDER_MIRROR_SHADER = ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, "render_mirror_shader_tp");


    public static void registerS2CPacket(){
        PayloadTypeRegistry.playS2C().register(RenderHeavyMirrorShaderS2C.ID, RenderHeavyMirrorShaderS2C.CODEC);


    }
}

package com.keko.packet.networking;

import com.keko.packet.RenderHeavyMirrorShaderS2C;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import java.awt.*;

public class ModMessagesClient {

    public static void registerC2SPacket(){

        ClientPlayNetworking.registerGlobalReceiver(RenderHeavyMirrorShaderS2C.ID, (payload, context) -> {
            context.client().execute(() -> {

            });
        });
    }
}

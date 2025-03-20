package com.keko;

import foundry.veil.api.client.render.VeilRenderSystem;
import foundry.veil.api.client.render.post.PostPipeline;
import foundry.veil.api.client.render.post.PostProcessingManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.resources.ResourceLocation;

public class AffixClient implements ClientModInitializer {
    public static boolean renderMirror = false;


    @Override
    public void onInitializeClient() {

        WorldRenderEvents.END.register((ctx) -> {
            try {
                if (renderMirror)
                    mirrorShaderActivator();
            }catch (Exception ignored){}
        });

    }



    private  void mirrorShaderActivator() {
        try {
            PostProcessingManager postProcessingManager = VeilRenderSystem.renderer().getPostProcessingManager();
            PostPipeline postPipeline = postProcessingManager.getPipeline(ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, "mirror"));
            postProcessingManager.runPipeline(postPipeline);
        }catch (Exception e){
        }
    }

}

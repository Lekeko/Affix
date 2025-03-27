package com.keko;

import com.keko.affixLogics.GlyphMarking;
import com.keko.entity.ModEntitiesRenderer;
import foundry.veil.api.client.render.VeilRenderSystem;
import foundry.veil.api.client.render.post.PostPipeline;
import foundry.veil.api.client.render.post.PostProcessingManager;
import foundry.veil.api.client.util.Easings;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.resources.ResourceLocation;

public class AffixClient implements ClientModInitializer {
    public static boolean renderMirror = false;
    public static float mirrorOffset = 0;


    @Override
    public void onInitializeClient() {

        ModEntitiesRenderer.register();

        WorldRenderEvents.END.register((ctx) -> {
            try {
                if (renderMirror)
                    mirrorShaderActivator();
            }catch (Exception ignored){}
        });

        HudRenderCallback.EVENT.register(((guiGraphics, deltaTracker) -> {
            GlyphMarking.renderCoordinates(guiGraphics, deltaTracker, mirrorOffset);
            GlyphMarking.renderImage(guiGraphics, deltaTracker, mirrorOffset);
        }));


    }



    private  void mirrorShaderActivator() {
        try {
            float speeder = 2.3f * mirrorOffset;
            PostProcessingManager postProcessingManager = VeilRenderSystem.renderer().getPostProcessingManager();
            PostPipeline postPipeline = postProcessingManager.getPipeline(ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, "mirror"));
            postPipeline.setFloat("mirrorOffset", Easings.Easing.easeOutCirc.ease(mirrorOffset));
            postProcessingManager.runPipeline(postPipeline);
        }catch (Exception e){
        }
    }

}

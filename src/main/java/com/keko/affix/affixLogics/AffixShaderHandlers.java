package com.keko.affix.affixLogics;

import com.keko.affix.Affix;
import com.keko.affix.AffixClient;
import com.keko.affix.items.custom.Accelerator;
import com.keko.affix.midLib.AffixConfigs;
import com.keko.affix.stylePointsManager.soundSystem.SoundAssets;
import foundry.veil.api.client.render.VeilRenderSystem;
import foundry.veil.api.client.render.post.PostPipeline;
import foundry.veil.api.client.render.post.PostProcessingManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

import java.awt.*;

public class AffixShaderHandlers {

    public static Color getClientColor() {
        try {
            return Color.decode(AffixConfigs.shaderAccentColor);
        }catch (Exception e){
            return new Color(255, 255, 255);
        }
    }


    public static void amplitudeVisualizer(){
        if (!AffixClient.getJammer().getSound().isPlaying()) return;

        try {
            Color primaryColor = Color.decode(AffixConfigs.jamPrimaryColor);
            Color secondaryColor = Color.decode(AffixConfigs.jamSecondaryColor);

            PostProcessingManager postProcessingManager = VeilRenderSystem.renderer().getPostProcessingManager();
            PostPipeline postPipeline = postProcessingManager.getPipeline(ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, "style_cubes"));
            assert postPipeline != null;
            postPipeline.getUniformSafe("amp").setFloat(AffixClient.getJammer().getSound().getAmplitude());
            postPipeline.getUniformSafe("cubeCount").setFloat(AffixConfigs.cubeCount);
            postPipeline.getUniformSafe("primaryColor").setVector(primaryColor.getRed(), primaryColor.getGreen(), primaryColor.getBlue());
            postPipeline.getUniformSafe("secondaryColor").setVector(secondaryColor.getRed(), secondaryColor.getGreen(), secondaryColor.getBlue());

            Color color = getClientColor();
            postProcessingManager.runPipeline(postPipeline);

        }catch (Exception ignored){
        }

        try {
            Color primaryColor = Color.decode(AffixConfigs.jamPrimaryColor);

            PostProcessingManager postProcessingManager = VeilRenderSystem.renderer().getPostProcessingManager();
            PostPipeline postPipeline = postProcessingManager.getPipeline(ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, "style_waves"));
            assert postPipeline != null;
            postPipeline.getUniformSafe("amp").setFloat(AffixClient.getJammer().getSound().getAmplitude());
            postPipeline.getUniformSafe("primaryColor").setVector(primaryColor.getRed(), primaryColor.getGreen(), primaryColor.getBlue());

            Color color = getClientColor();
            postProcessingManager.runPipeline(postPipeline);

        }catch (Exception ignored){
        }
    }

    public static void mirrorShaderActivator() {
        try {
            PostProcessingManager postProcessingManager = VeilRenderSystem.renderer().getPostProcessingManager();
            PostPipeline postPipeline = postProcessingManager.getPipeline(ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, "mirror"));
            assert postPipeline != null;
            postPipeline.getUniformSafe("mirrorOffset").setFloat((float) Math.min(Math.PI/2,AffixClient.mirrorOffset));
            Color color = getClientColor();
            postPipeline.getUniformSafe("shaderAccent").setVector(color.getRed(), color.getGreen(), color.getBlue());
            postProcessingManager.runPipeline(postPipeline);

        }catch (Exception ignored){
        }
    }

    public static void acceleratorShaderActivator() {
        try {
            PostProcessingManager postProcessingManager = VeilRenderSystem.renderer().getPostProcessingManager();
            PostPipeline postPipeline = postProcessingManager.getPipeline(ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, "accelerator"));
            assert postPipeline != null;
            Color color = getClientColor();
            postPipeline.getUniformSafe("shaderAccent").setVector(color.getRed(), color.getGreen(), color.getBlue());
            postPipeline.getUniformSafe("tranzitioner").setFloat(Accelerator.TRANZITIONER - AffixClient.acceleratorShaderTranzition);
            postPipeline.getUniformSafe("shaderStrength").setFloat(AffixConfigs.acceleratedEffectStrength);
            postProcessingManager.runPipeline(postPipeline);

        }catch (Exception ignored){
        }
    }

    public static void blackWholeShaderActivator(Vec3 pos, float timer) {
        try {
            PostProcessingManager postProcessingManager = VeilRenderSystem.renderer().getPostProcessingManager();
            PostPipeline postPipeline = postProcessingManager.getPipeline(ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, "black_whole"));
            assert postPipeline != null;
            Color color = getClientColor();
            postPipeline.getUniformSafe("pos").setVector((float) pos.x, (float) pos.y, (float) pos.z);
            postPipeline.getUniformSafe("timer").setFloat(timer);

            postProcessingManager.runPipeline(postPipeline);

        }catch (Exception ignored){
        }
    }

    public static void phaserShaderActivator() {

        try {

            PostProcessingManager postProcessingManager = VeilRenderSystem.renderer().getPostProcessingManager();
            PostPipeline postPipeline = postProcessingManager.getPipeline(ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, "whiteblack"));
            assert postPipeline != null;
            Color color = getClientColor();
            postPipeline.getUniformSafe("shaderAccent").setVector(color.getRed(), color.getGreen(), color.getBlue());
            postPipeline.getUniformSafe("transition").setFloat(AffixClient.transition);
            postProcessingManager.runPipeline(postPipeline);
        }catch (Exception ignored){
        }

    }

}

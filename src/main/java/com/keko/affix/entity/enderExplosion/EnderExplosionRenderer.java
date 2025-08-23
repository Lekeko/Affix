package com.keko.affix.entity.enderExplosion;

import com.keko.affix.Affix;
import com.keko.affix.midLib.AffixConfigs;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

import java.awt.*;

public class EnderExplosionRenderer extends GeoEntityRenderer<EnderExplosion> {

    public EnderExplosionRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new EnderExplosionModel());
    }


    @Override
    public @NotNull ResourceLocation getTextureLocation(EnderExplosion enderExplosion) {
        return ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, "textures/entity/ender_explosion.png");
    }

    @Override
    public void render(EnderExplosion enderExplosion, float f, float g, PoseStack poseStack, MultiBufferSource multiBufferSource, int i) {
        RenderSystem.enableCull();
        RenderSystem.enableBlend();
        int fullBright = LightTexture.pack(15, 15);
        poseStack.translate(0,1,0);

        super.render(enderExplosion, f, g, poseStack, multiBufferSource, fullBright);

    }

    @Override
    public @Nullable RenderType getRenderType(EnderExplosion enderExplosion, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderType.entityTranslucentCull(getTextureLocation(enderExplosion));
    }

    @Override
    public software.bernie.geckolib.util.Color getRenderColor(EnderExplosion animatable, float partialTick, int packedLight) {
        Color primaryColor;
        try {
            primaryColor = Color.decode(AffixConfigs.jamPrimaryColor);
        }catch (Exception e){
            Affix.LOGGER.warn("Please provide correct color input :3 (or imma send ur pc in space lol)");
            primaryColor = Color.MAGENTA;
        }
        int brightnessModifier = 100;
        primaryColor = new Color(
                Math.clamp(primaryColor.getRed() + brightnessModifier, 0, 255),
                Math.clamp(primaryColor.getGreen() + brightnessModifier, 0, 255),
                Math.clamp(primaryColor.getBlue() + brightnessModifier, 0, 255),
                Math.clamp(255 - animatable.tickCount * 30L, 0, 255)
        );
        int alpha = primaryColor.getAlpha();
        int red = primaryColor.getRed();
        int green = primaryColor.getGreen();
        int blue = primaryColor.getBlue();

        return new software.bernie.geckolib.util.Color((alpha << 24) | (red << 16) | (green << 8)  | blue);
    }

    @Override
    public boolean shouldRender(EnderExplosion enderExplosion, Frustum frustum, double d, double e, double f) {
        return true;
    }


}

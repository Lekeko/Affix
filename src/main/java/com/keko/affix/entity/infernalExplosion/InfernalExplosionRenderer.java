package com.keko.affix.entity.infernalExplosion;

import com.keko.affix.Affix;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class InfernalExplosionRenderer extends GeoEntityRenderer<InfernalExplosion> {

    public InfernalExplosionRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new InfernalExplosionModel());
    }


    @Override
    public @NotNull ResourceLocation getTextureLocation(InfernalExplosion infernalExplosion) {
        return ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, "textures/entity/infernal_explosion.png");
    }

    @Override
    public void render(InfernalExplosion infernalExplosion, float f, float g, PoseStack poseStack, MultiBufferSource multiBufferSource, int i) {
        int size = (int) (3 * infernalExplosion.getSize());
        RenderSystem.enableCull();
        RenderSystem.enableBlend();
        poseStack.scale(size, size,size);
        if (infernalExplosion.getSize() ==1 ){
            poseStack.mulPose(Axis.YP.rotationDegrees(infernalExplosion.getId() * 2));
            poseStack.mulPose(Axis.XP.rotationDegrees(infernalExplosion.getId() * 2));
            poseStack.mulPose(Axis.ZP.rotationDegrees(infernalExplosion.getId() * 2));
        }
        super.render(infernalExplosion, f, g, poseStack, multiBufferSource, i+255);


    }

    @Override
    public @Nullable RenderType getRenderType(InfernalExplosion infernalExplosion, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderType.entityCutoutNoCull(getTextureLocation(infernalExplosion));
    }

    @Override
    public boolean shouldRender(InfernalExplosion infernalExplosion, Frustum frustum, double d, double e, double f) {
        return true;
    }


}

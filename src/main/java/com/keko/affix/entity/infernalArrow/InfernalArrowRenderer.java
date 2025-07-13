package com.keko.affix.entity.infernalArrow;

import com.keko.affix.Affix;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class InfernalArrowRenderer extends GeoEntityRenderer<InfernalArrow> {

    public InfernalArrowRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new InfernalArrowModel());
    }


    @Override
    public @NotNull ResourceLocation getTextureLocation(InfernalArrow entity) {
        return ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, "textures/entity/infernal_arrow.png");
    }

    @Override
    public void render(InfernalArrow infernalArrow, float f, float g, PoseStack poseStack, MultiBufferSource multiBufferSource, int i) {
        int size = 3;
        RenderSystem.enableCull();
        RenderSystem.enableBlend();
        poseStack.scale(size, size,size);
        poseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(g, infernalArrow.yRotO, infernalArrow.getYRot()) - 90.0F));
        poseStack.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(g, infernalArrow.xRotO, infernalArrow.getXRot()) + 90.0F));
        poseStack.mulPose(Axis.YP.rotationDegrees(infernalArrow.getId() * 5));

        if (infernalArrow.tickCount > 1)
            super.render(infernalArrow, f, g, poseStack, multiBufferSource, i+255);

    }

    @Override
    public @Nullable RenderType getRenderType(InfernalArrow animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderType.entityTranslucent(getTextureLocation(animatable));
    }

    @Override
    public boolean shouldRender(InfernalArrow entity, Frustum frustum, double d, double e, double f) {
        return super.shouldRender(entity, frustum, d, e, f);
    }


}

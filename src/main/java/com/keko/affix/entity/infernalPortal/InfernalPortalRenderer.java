package com.keko.affix.entity.infernalPortal;

import com.keko.affix.Affix;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
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

public class InfernalPortalRenderer extends GeoEntityRenderer<InfernalPortal> {

    public InfernalPortalRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new InfernalPortalModel());
    }


    @Override
    public @NotNull ResourceLocation getTextureLocation(InfernalPortal infernalPortal) {
        return ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, "textures/entity/infernal_portal.png");
    }

    @Override
    public void render(InfernalPortal infernalPortal, float f, float g, PoseStack poseStack, MultiBufferSource multiBufferSource, int i) {
        int size = 20;
        RenderSystem.enableCull();
        RenderSystem.enableBlend();
        poseStack.scale(size, size,size);
        poseStack.translate(0, -1,0);
        poseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(g, infernalPortal.yRotO, infernalPortal.getYRot()) + 90.0F));
        poseStack.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(g, infernalPortal.xRotO, infernalPortal.getXRot()) - 90.0F));
        super.render(infernalPortal, f, g, poseStack, multiBufferSource, i+255);


    }

    @Override
    public @Nullable RenderType getRenderType(InfernalPortal infernalPortal, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderType.entityTranslucent(getTextureLocation(infernalPortal));
    }

    @Override
    public boolean shouldRender(InfernalPortal infernalPortal, Frustum frustum, double d, double e, double f) {
        return true;
    }


}

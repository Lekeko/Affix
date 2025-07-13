package com.keko.affix.entity.infernalDragon;

import com.keko.affix.Affix;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.kinds.IdF;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class InfernalDragonRenderer extends GeoEntityRenderer<InfernalDragon> {

    public InfernalDragonRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new InfernalDragonModel());
    }


    @Override
    public @NotNull ResourceLocation getTextureLocation(InfernalDragon entity) {
        return ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, "textures/entity/infernal_dragon.png");
    }

    @Override
    public void render(InfernalDragon infernalDragon, float f, float g, PoseStack poseStack, MultiBufferSource multiBufferSource, int i) {
        float size = 40f;
        poseStack.pushPose();
        poseStack.scale(size, size, size);
        if (infernalDragon.tickCount > 30 && infernalDragon.tickCount < 90)
        poseStack.translate(
                (infernalDragon.level().random.nextFloat()-0.5f)/30,
                (infernalDragon.level().random.nextFloat()-0.5f)/30,
                (infernalDragon.level().random.nextFloat()-0.5f)/30
        );
        poseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(g, infernalDragon.yRotO, infernalDragon.getYRot()) - 180.0F));
        poseStack.mulPose(Axis.XN.rotationDegrees(Mth.lerp(g, infernalDragon.xRotO, infernalDragon.getXRot())));
        int fullBright = LightTexture.pack(15, 15);

        super.render(infernalDragon, f, g, poseStack, multiBufferSource, fullBright);

        poseStack.popPose();
    }

    @Override
    public @Nullable RenderType getRenderType(InfernalDragon animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderType.entityCutoutNoCull(texture);
    }

    @Override
    public boolean shouldRender(InfernalDragon entity, Frustum frustum, double d, double e, double f) {
        return true;
    }


}

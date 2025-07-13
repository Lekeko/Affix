package com.keko.affix.entity.infernalPrison;

import com.keko.affix.Affix;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

import java.util.Objects;

public class InfernalPrisonRenderer extends GeoEntityRenderer<InfernalPrison> {

    public InfernalPrisonRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new InfernalPrisonModel());
    }


    @Override
    public @NotNull ResourceLocation getTextureLocation(InfernalPrison infernalPrison) {
        return ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, "textures/entity/infernal_prison.png");
    }

    @Override
    public void render(InfernalPrison infernalPrison, float f, float g, PoseStack poseStack, MultiBufferSource multiBufferSource, int i) {
        Entity entity = infernalPrison.level().getEntity(infernalPrison.getTrappedMob());
        float scale = 1.2f;
        if (entity != null){
            scale += (float) Objects.requireNonNull(infernalPrison.level().getEntity(infernalPrison.getTrappedMob())).getBoundingBox().getYsize() +
                        (float) Objects.requireNonNull(infernalPrison.level().getEntity(infernalPrison.getTrappedMob())).getBoundingBox().getXsize();
        }
        poseStack.translate(0,scale/2,0);
        poseStack.scale(scale,scale,scale);
        int fullBright = LightTexture.pack(15, 15);
        poseStack.mulPose(Axis.YP.rotationDegrees(RandomSource.create(infernalPrison.getId()).nextIntBetweenInclusive(0,360)));


        super.render(infernalPrison, f, g, poseStack, multiBufferSource, fullBright);
    }

    @Override
    public @Nullable RenderType getRenderType(InfernalPrison infernalPrison, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderType.entityCutout(texture);
    }

    @Override
    public boolean shouldRender(InfernalPrison infernalPrison, Frustum frustum, double d, double e, double f) {
        return infernalPrison.getOrder() < infernalPrison.tickCount;
    }


}

package com.keko.affix.entity.infernalBeacon;

import com.keko.affix.Affix;
import com.keko.affix.affixLogics.AffixShaderHandlers;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class InfernalBeaconRenderer extends GeoEntityRenderer<InfernalBeacon> {

    public InfernalBeaconRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new InfernalBeaconModel());
    }


    @Override
    public @NotNull ResourceLocation getTextureLocation(InfernalBeacon infernalBeacon) {
        return ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, "textures/entity/infernal_beacon_" + infernalBeacon.getBeaconType() + ".png");
    }

    @Override
    public void render(InfernalBeacon infernalBeacon, float f, float g, PoseStack poseStack, MultiBufferSource multiBufferSource, int i) {
        float size = Math.min((float) infernalBeacon.getTimer() /5, 2);
        RenderSystem.enableCull();
        RenderSystem.enableBlend();
        poseStack.pushPose();
        poseStack.translate(0,0.5f,0);
        float rotation = (Minecraft.getInstance().getTimer().getGameTimeDeltaPartialTick(true) + infernalBeacon.tickCount);

        poseStack.mulPose(Axis.ZP.rotationDegrees(rotation));
        poseStack.mulPose(Axis.XP.rotationDegrees(rotation));
        poseStack.mulPose(Axis.YN.rotationDegrees(rotation));

        poseStack.scale(size,size,size);

        int fullBright = LightTexture.pack(15, 15);
        super.render(infernalBeacon, f, g, poseStack, multiBufferSource, fullBright);poseStack.popPose();

        infernalBeacon.addTimer(Minecraft.getInstance().getTimer().getGameTimeDeltaTicks() * 5);
        AffixShaderHandlers.blackWholeShaderActivator(infernalBeacon.getPosition(1.0f), infernalBeacon.getTimer());


    }

    @Override
    public @Nullable RenderType getRenderType(InfernalBeacon infernalBeacon, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderType.entityCutout(getTextureLocation(infernalBeacon));
    }

    @Override
    public boolean shouldRender(InfernalBeacon infernalBeacon, Frustum frustum, double d, double e, double f) {
        return true;
    }

}

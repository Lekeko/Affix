package com.keko.entity.fabricPocket;

import com.keko.Affix;
import com.keko.AffixClient;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import foundry.veil.api.client.util.Easings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class FabricPocketRenderer extends GeoEntityRenderer<FabricPocket> {

    public FabricPocketRenderer(EntityRendererProvider.Context context) {
        super(context, new FabricPocketModel());

    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(FabricPocket entity) {
        return ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, "textures/entity/fabric_pocket.png");
    }

    @Override
    public void render(FabricPocket entity, float f, float g, PoseStack poseStack, MultiBufferSource multiBufferSource, int i) {
        float rotation = (Minecraft.getInstance().getTimer().getGameTimeDeltaPartialTick(true) + entity.tickCount);
        poseStack.mulPose(Axis.XN.rotationDegrees(rotation));
        poseStack.mulPose(Axis.YN.rotationDegrees(-rotation));
        poseStack.mulPose(Axis.ZN.rotationDegrees(rotation));
        float size = Easings.Easing.easeInExpo.ease(AffixClient.mirrorOffset) / 4f;

        poseStack.scale(size,size,size);


        super.render(entity, f, g, poseStack, multiBufferSource, i);
    }

    @Override
    public boolean shouldRender(FabricPocket entity, Frustum frustum, double d, double e, double f) {
        return AffixClient.renderMirror;
    }

}

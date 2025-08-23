package com.keko.affix.entity.enderFist;

import com.keko.affix.Affix;
import com.keko.affix.AffixClient;
import com.keko.affix.midLib.AffixConfigs;
import com.keko.affix.stylePointsManager.soundSystem.Sound;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

import java.awt.*;

public class EnderFistRenderer extends GeoEntityRenderer<EnderFist> {

    public EnderFistRenderer(EntityRendererProvider.Context context) {
        super(context, new EnderFistModel());

    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(EnderFist enderFist) {
        return ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, "textures/entity/ender_fist.png");
    }

    @Override
    public void render(EnderFist enderFist, float f, float g, PoseStack poseStack, MultiBufferSource multiBufferSource, int i) {
        if (enderFist.getOwner() == null) return;
        Color primaryColor;
        Color secondaryColor;
        try {
            primaryColor = Color.decode(AffixConfigs.jamPrimaryColor);
            secondaryColor = Color.decode(AffixConfigs.jamSecondaryColor);
        }catch (Exception e){
            Affix.LOGGER.warn("Please provide correct color input :3 (or imma send ur pc in space lol)");
            primaryColor = Color.MAGENTA;
            secondaryColor = Color.BLACK;
        }
        int brightnessModifier = 22;

        primaryColor = new Color(
                Math.clamp(primaryColor.getRed() + brightnessModifier, 0, 255),
                Math.clamp(primaryColor.getGreen() + brightnessModifier, 0, 255),
                Math.clamp(primaryColor.getBlue() + brightnessModifier, 0, 255),
                170
        );

        int alpha = primaryColor.getAlpha();
        int red = primaryColor.getRed();
        int green = primaryColor.getGreen();
        int blue = primaryColor.getBlue();

        float rotationHoriz = enderFist.getOwner().getRotationVector().y; // WHY IS HORIZONTAL Y WHYYYY
        float rotationVert = enderFist.getOwner().getRotationVector().x;
        poseStack.mulPose(Axis.YN.rotationDegrees(rotationHoriz+ 20 * (enderFist.getLeft() ? 1 : -1)));
        poseStack.mulPose(Axis.XP.rotationDegrees(rotationVert - 7));
        poseStack.pushPose();
        super.actuallyRender(poseStack, enderFist, this.model.getBakedModel(this.model.getModelResource(enderFist)),
                RenderType.eyes(getTextureLocation(enderFist)), multiBufferSource, multiBufferSource.getBuffer(RenderType.eyes(getTextureLocation(enderFist))),
                false, g, 255, OverlayTexture.NO_OVERLAY, secondaryColor.getRGB());
        poseStack.popPose();
        poseStack.scale(1.1f, 1.2f, 1.1f);
        int color = (alpha << 24) | (red << 16) | (green << 8) | blue;
        super.actuallyRender(poseStack, enderFist, this.model.getBakedModel(this.model.getModelResource(enderFist)),
                RenderType.eyes(getTextureLocation(enderFist)), multiBufferSource, multiBufferSource.getBuffer(RenderType.entityTranslucent(getTextureLocation(enderFist))),
                true, g, 255, OverlayTexture.NO_OVERLAY, color);
        super.actuallyRender(poseStack, enderFist, this.model.getBakedModel(this.model.getModelResource(enderFist)),
                RenderType.eyes(getTextureLocation(enderFist)), multiBufferSource, multiBufferSource.getBuffer(RenderType.eyes(getTextureLocation(enderFist))),
                true, g, 255, OverlayTexture.NO_OVERLAY, color);

    }

    @Override
    public boolean shouldRender(EnderFist enderFist, Frustum frustum, double d, double e, double f) {
        return true;
    }

    @Override
    public @Nullable RenderType getRenderType(EnderFist enderFist, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderType.eyes(getTextureLocation(enderFist));
    }
}

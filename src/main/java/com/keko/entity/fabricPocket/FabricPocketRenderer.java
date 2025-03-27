package com.keko.entity.fabricPocket;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

public class FabricPocketRenderer extends EntityRenderer<FabricPocket> {
    public FabricPocketRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(FabricPocket entity) {
        return null;
    }

    @Override
    public void render(FabricPocket entity, float f, float g, PoseStack poseStack, MultiBufferSource multiBufferSource, int i) {
        super.render(entity, f, g, poseStack, multiBufferSource, i);
    }

    @Override
    public boolean shouldRender(FabricPocket entity, Frustum frustum, double d, double e, double f) {
        return super.shouldRender(entity, frustum, d, e, f);
    }

    @Override
    protected boolean shouldShowName(FabricPocket entity) {
        return false;
    }
}

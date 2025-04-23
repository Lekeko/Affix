package com.keko.mixin;


import com.keko.AffixClient;
import com.keko.affixLogics.HandsAnimationSystem;
import com.keko.items.custom.Accelerator;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import foundry.veil.api.client.util.Easings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import org.joml.Vector4d;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemInHandRenderer.class)
public abstract class RenderHandsMixin {

    @Shadow @Final private Minecraft minecraft;
    @Shadow @Final private EntityRenderDispatcher entityRenderDispatcher;

    @Inject(method = "renderPlayerArm", at = @At("HEAD"), cancellable = true)
    private void renderTwoArmsSeparately(PoseStack poseStack, MultiBufferSource multiBufferSource, int light, float tick, float equipProgress, HumanoidArm humanoidArm, CallbackInfo ci) {
        HandsAnimationSystem.methodInMixin(poseStack, multiBufferSource, light, tick, equipProgress, humanoidArm, ci, minecraft, entityRenderDispatcher);
    }

}
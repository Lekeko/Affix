package com.keko.affix.affixLogics;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import foundry.veil.api.client.util.Easing;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class HandsAnimationSystem {

    private static float tickerAccelerator = 5;
    private static float tickerInfernalArrow = 0;

    public static void resetTickerAccelerator(){
        tickerAccelerator = 0;
    }

    public static void reseTickerInfernalArrow(AbstractClientPlayer abstractClientPlayer){
        if (!abstractClientPlayer.isUsingItem())
            tickerInfernalArrow = -1.5f;
    }

    public static void methodInMixin(PoseStack poseStack, MultiBufferSource multiBufferSource, int light, float tick, float equipProgress, HumanoidArm humanoidArm, CallbackInfo ci, Minecraft minecraft, EntityRenderDispatcher entityRenderDispatcher){
        AbstractClientPlayer clientPlayer = minecraft.player;
        if (clientPlayer == null) {
            return;
        }

        PlayerRenderer playerRenderer = (PlayerRenderer) entityRenderDispatcher.getRenderer(clientPlayer);

        if (tickerAccelerator <=4) {
            tickerAccelerator += Minecraft.getInstance().getTimer().getGameTimeDeltaTicks() / 15;
            equipProgress = 0;
            poseStack.pushPose();
            {
                boolean bl = humanoidArm != HumanoidArm.LEFT;
                float h = bl ? 1.0F : -1.0F;
                float j = Mth.sqrt(equipProgress);
                float k = -0.3F * Mth.sin(j * (float) Math.PI);
                float l = 0.4F * Mth.sin(j * ((float) Math.PI * 2F));
                float m = -0.4F * Mth.sin(equipProgress * (float) Math.PI);
                poseStack.translate(h * (k + 0.64000005F), l + -0.6F + tick * -0.6F, m + -0.71999997F);
                poseStack.mulPose(Axis.YP.rotationDegrees(h * 45.0F));
                float n = Mth.sin(equipProgress * equipProgress * (float) Math.PI);
                float o = Mth.sin(j * (float) Math.PI);
                poseStack.mulPose(Axis.YP.rotationDegrees(h * o * 70.0F));
                poseStack.mulPose(Axis.ZP.rotationDegrees(h * n * -20.0F));
                poseStack.translate(h * -1.0F, 3.6F, 3.5F);
                poseStack.mulPose(Axis.ZP.rotationDegrees(h * 120.0F));
                poseStack.mulPose(Axis.XP.rotationDegrees(200.0F));
                poseStack.mulPose(Axis.YP.rotationDegrees(h * -135.0F));
                poseStack.mulPose(Axis.ZN.rotationDegrees(-36 * tickerAccelerator));
                poseStack.translate(h * 5.6F, 0.0F, 0.0F);

                if (bl) {
                    playerRenderer.renderRightHand(poseStack, multiBufferSource, light, clientPlayer);
                } else {
                    playerRenderer.renderLeftHand(poseStack, multiBufferSource, light, clientPlayer);
                }
            }
            poseStack.popPose();

            poseStack.pushPose();
            {
                HumanoidArm secondArm = (humanoidArm == HumanoidArm.LEFT) ? HumanoidArm.RIGHT : HumanoidArm.LEFT;
                boolean bl = secondArm != HumanoidArm.LEFT;
                float h = bl ? 1.0F : -1.0F;
                float j = Mth.sqrt(equipProgress);
                float k = -0.3F * Mth.sin(j * (float) Math.PI);
                float l = 0.4F * Mth.sin(j * ((float) Math.PI * 2F));
                float m = -0.4F * Mth.sin(equipProgress * (float) Math.PI);
                poseStack.translate(h * (k + 0.64000005F), l + -0.6F + tick * -0.6F, m + -0.71999997F);
                poseStack.mulPose(Axis.YP.rotationDegrees(h * 45.0F));
                float n = Mth.sin(equipProgress * equipProgress * (float) Math.PI);
                float o = Mth.sin(j * (float) Math.PI);
                poseStack.mulPose(Axis.YP.rotationDegrees(h * o * 70.0F));
                poseStack.mulPose(Axis.ZP.rotationDegrees(h * n * -20.0F));
                poseStack.translate(h * -1.0F, 3.6F, 3.5F);
                poseStack.mulPose(Axis.ZP.rotationDegrees(h * 120.0F));
                poseStack.mulPose(Axis.XP.rotationDegrees(200.0F));
                poseStack.mulPose(Axis.YP.rotationDegrees(h * -135.0F));
                poseStack.mulPose(Axis.ZP.rotationDegrees(-36 * tickerAccelerator));

                poseStack.translate(h * 5.6F, 0.0F, 0.0F);

                if (bl) {
                    playerRenderer.renderRightHand(poseStack, multiBufferSource, light, clientPlayer);
                } else {
                    playerRenderer.renderLeftHand(poseStack, multiBufferSource, light, clientPlayer);
                }
            }
            poseStack.popPose();

            ci.cancel();
        }


    }

    public static void anotherMethodInMixinLol(PoseStack poseStack, MultiBufferSource multiBufferSource, int light, float tick, float equipProgress, HumanoidArm humanoidArm, CallbackInfo ci, Minecraft minecraft, EntityRenderDispatcher entityRenderDispatcher) {
        AbstractClientPlayer clientPlayer = minecraft.player;

        if (clientPlayer == null) {
            return;
        }

        PlayerRenderer playerRenderer = (PlayerRenderer) entityRenderDispatcher.getRenderer(clientPlayer);

        if (tickerInfernalArrow <=4 && tickerInfernalArrow >= -1.5f) {
            tickerInfernalArrow += Minecraft.getInstance().getTimer().getGameTimeDeltaTicks() / 8;
            equipProgress = 0;
            poseStack.pushPose();
            {
                boolean bl = humanoidArm != HumanoidArm.LEFT;
                float h = bl ? 1.0F : -1.0F;
                float j = Mth.sqrt(equipProgress);
                float k = -0.3F * Mth.sin(j * (float) Math.PI);
                float l = 0.4F * Mth.sin(j * ((float) Math.PI * 2F));
                float m = -0.4F * Mth.sin(equipProgress * (float) Math.PI);
                poseStack.translate(h * (k + 0.64000005F), l + -0.6F + tick * -0.6F, m + -0.71999997F);
                poseStack.mulPose(Axis.YP.rotationDegrees(h * 45.0F - tickerInfernalArrow * 40));
                float n = Mth.sin(equipProgress * equipProgress * (float) Math.PI);
                float o = Mth.sin(j * (float) Math.PI);
                poseStack.mulPose(Axis.YP.rotationDegrees(h * o * 70.0F));
                poseStack.mulPose(Axis.ZP.rotationDegrees(h * n * -20.0F));
                poseStack.translate(h * -1.0F, 3.6F, 3.5F);
                poseStack.mulPose(Axis.ZP.rotationDegrees(h * 120.0F));
                poseStack.mulPose(Axis.XP.rotationDegrees(200.0F));
                poseStack.mulPose(Axis.YP.rotationDegrees(h * -135.0F));
                poseStack.translate(h * 5.6F, 0.0F + (tickerInfernalArrow) * 5, 0.0F + tickerInfernalArrow);

                if (bl) {
                    playerRenderer.renderRightHand(poseStack, multiBufferSource, light, clientPlayer);
                } else {
                    playerRenderer.renderLeftHand(poseStack, multiBufferSource, light, clientPlayer);
                }
            }
            poseStack.popPose();


            poseStack.pushPose();
            {
                HumanoidArm secondArm = (humanoidArm == HumanoidArm.LEFT) ? HumanoidArm.RIGHT : HumanoidArm.LEFT;
                boolean bl = secondArm != HumanoidArm.LEFT;
                float h = bl ? 1.0F : -1.0F;
                float j = Mth.sqrt(equipProgress);
                float k = -0.3F * Mth.sin(j * (float) Math.PI);
                float l = 0.4F * Mth.sin(j * ((float) Math.PI * 2F));
                float m = -0.4F * Mth.sin(equipProgress * (float) Math.PI);
                poseStack.translate(h * (k + 0.64000005F), l + -0.6F + tick * -0.6F, m + -0.71999997F);
                poseStack.mulPose(Axis.YP.rotationDegrees(h * 45.0F + (tickerInfernalArrow - 1.5f) * 40));
                float n = Mth.sin(equipProgress * equipProgress * (float) Math.PI);
                float o = Mth.sin(j * (float) Math.PI);
                poseStack.mulPose(Axis.YP.rotationDegrees(h * o * 70.0F));
                poseStack.mulPose(Axis.ZP.rotationDegrees(h * n * -20.0F));
                poseStack.translate(h * -1.0F, 3.6F, 3.5F);
                poseStack.mulPose(Axis.ZP.rotationDegrees(h * 120.0F));
                poseStack.mulPose(Axis.XP.rotationDegrees(200.0F));
                poseStack.mulPose(Axis.YP.rotationDegrees(h * -135.0F));

                poseStack.translate(h * 5.6F, (tickerInfernalArrow - 1.5f) * 5, 0.0F + tickerInfernalArrow - 1.5f);

                if (bl) {
                    playerRenderer.renderRightHand(poseStack, multiBufferSource, light, clientPlayer);
                } else {
                    playerRenderer.renderLeftHand(poseStack, multiBufferSource, light, clientPlayer);
                }
            }
            poseStack.popPose();

            ci.cancel();
        }else {
            tickerInfernalArrow += (float) (Minecraft.getInstance().getTimer().getGameTimeDeltaTicks() / 300);
            equipProgress = 0;
            float randomX = clientPlayer.getRandom().nextFloat()/ 15;
            float randomY = clientPlayer.getRandom().nextFloat()/ 15;
            float randomZ = clientPlayer.getRandom().nextFloat()/ 15;


            poseStack.pushPose();
            {
                boolean bl = humanoidArm != HumanoidArm.LEFT;
                float h = bl ? 1.0F : -1.0F;
                float j = Mth.sqrt(equipProgress);
                float k = -0.3F * Mth.sin(j * (float) Math.PI);
                float l = 0.4F * Mth.sin(j * ((float) Math.PI * 2F));
                float m = -0.4F * Mth.sin(equipProgress * (float) Math.PI);
                poseStack.translate(h * (k + 0.64000005F), l + -0.6F + tick * -0.6F, m + -0.71999997F + randomX);
                poseStack.mulPose(Axis.YP.rotationDegrees(h * 45.0F+ randomY));
                float n = Mth.sin(equipProgress * equipProgress * (float) Math.PI);
                float o = Mth.sin(j * (float) Math.PI);
                poseStack.mulPose(Axis.YP.rotationDegrees(h * o * 70.0F+ randomZ));
                poseStack.mulPose(Axis.ZP.rotationDegrees(h * n * -20.0F));
                poseStack.translate(h * -1.0F, 3.6F, 3.5F);
                poseStack.mulPose(Axis.ZP.rotationDegrees(h * 120.0F));
                poseStack.mulPose(Axis.XP.rotationDegrees(200.0F));
                poseStack.mulPose(Axis.YP.rotationDegrees(h * -135.0F));
                poseStack.mulPose(Axis.ZN.rotationDegrees(-36 * (tickerInfernalArrow - 4) + 5));
                poseStack.translate(h * 5.6F, 0.0F, 0.0F);

                if (bl) {
                    playerRenderer.renderRightHand(poseStack, multiBufferSource, light, clientPlayer);
                } else {
                    playerRenderer.renderLeftHand(poseStack, multiBufferSource, light, clientPlayer);
                }
            }
            poseStack.popPose();

            poseStack.pushPose();
            {
                HumanoidArm secondArm = (humanoidArm == HumanoidArm.LEFT) ? HumanoidArm.RIGHT : HumanoidArm.LEFT;
                boolean bl = secondArm != HumanoidArm.LEFT;
                float h = bl ? 1.0F : -1.0F;
                float j = Mth.sqrt(equipProgress);
                float k = -0.3F * Mth.sin(j * (float) Math.PI);
                float l = 0.4F * Mth.sin(j * ((float) Math.PI * 2F));
                float m = -0.4F * Mth.sin(equipProgress * (float) Math.PI);
                poseStack.translate(h * (k + 0.64000005F), l + -0.6F + tick * -0.6F, m + -0.71999997F + randomX );
                poseStack.mulPose(Axis.YP.rotationDegrees(h * 45.0F+ randomY));
                float n = Mth.sin(equipProgress * equipProgress * (float) Math.PI);
                float o = Mth.sin(j * (float) Math.PI);
                poseStack.mulPose(Axis.YP.rotationDegrees(h * o * 70.0F + randomZ));
                poseStack.mulPose(Axis.ZP.rotationDegrees(h * n * -20.0F));
                poseStack.translate(h * -1.0F, 3.6F, 3.5F);
                poseStack.mulPose(Axis.ZP.rotationDegrees(h * 120.0F));
                poseStack.mulPose(Axis.XP.rotationDegrees(200.0F));
                poseStack.mulPose(Axis.YP.rotationDegrees(h * -135.0F));
                poseStack.mulPose(Axis.ZP.rotationDegrees(-36 * (tickerInfernalArrow - 4) + 5));

                poseStack.translate(h * 5.6F, 0.0F, 0.0F);

                if (bl) {
                    playerRenderer.renderRightHand(poseStack, multiBufferSource, light, clientPlayer);
                } else {
                    playerRenderer.renderLeftHand(poseStack, multiBufferSource, light, clientPlayer);
                }
            }
            poseStack.popPose();

            ci.cancel();
        }
    }
}

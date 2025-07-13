package com.keko.affix.mixin;


import com.keko.affix.affixLogics.HandsAnimationSystem;
import com.keko.affix.items.ModItems;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
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

    @Inject(method = "renderArmWithItem", at = @At("HEAD"), cancellable = true)
    private void renderArmAnimation(AbstractClientPlayer abstractClientPlayer, float f, float g, InteractionHand interactionHand, float equipProgress, ItemStack itemStack, float tick, PoseStack poseStack, MultiBufferSource multiBufferSource, int light, CallbackInfo ci){
        if (!abstractClientPlayer.isScoping()){
            boolean bl = interactionHand == InteractionHand.MAIN_HAND;
            HumanoidArm humanoidArm = bl ? abstractClientPlayer.getMainArm() : abstractClientPlayer.getMainArm().getOpposite();
            poseStack.pushPose();
            if (itemStack.is(ModItems.QUASAR_CALLER ) && abstractClientPlayer.isUsingItem()){
                HandsAnimationSystem.anotherMethodInMixinLol(poseStack, multiBufferSource, light, tick, equipProgress, humanoidArm, ci, minecraft, entityRenderDispatcher);
            }else HandsAnimationSystem.reseTickerInfernalArrow(abstractClientPlayer);
        }
    }

}
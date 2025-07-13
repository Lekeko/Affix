package com.keko.affix.entity.infernalPortal;

import com.keko.affix.affixLogics.ModDamageTypes;
import foundry.veil.api.client.render.VeilRenderSystem;
import foundry.veil.api.client.render.light.data.PointLightData;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.animation.*;


public class InfernalPortal extends AbstractArrow implements GeoEntity {

    public InfernalPortal(EntityType<? extends AbstractArrow> entityType, Level level) {
        super(entityType, level);
    }


    @Override
    public void tick() {
        super.tick();
        if (!level().isClientSide && tickCount > 100) discard();
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return Items.GLASS.getDefaultInstance();
    }

    @Override
    public boolean isPickable() {
        return false;
    }

    @Override
    protected boolean tryPickup(Player player) {
        return false;
    }

    @Override
    public boolean isNoPhysics() {
        return true;
    }

    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);


    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }


    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 0, this::predicate));
        AnimationController<InfernalPortal> controller = new AnimationController<>(this, "controller", 0, this::predicate);
        controller.triggerableAnim("portal.spin", RawAnimation.begin().thenPlayAndHold("portal.spin"));

        controllers.add(controller);
    }


    private PlayState predicate(AnimationState<InfernalPortal> fabricPocketAnimationState) {
        triggerAnim("controller","portal.spin");
        return PlayState.CONTINUE;
    }
}

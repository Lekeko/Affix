package com.keko.affix.entity.enderExplosion;

import com.keko.affix.affixLogics.ModDamageTypes;
import com.keko.affix.entity.enderFist.EnderFist;
import foundry.veil.api.client.render.VeilRenderSystem;
import foundry.veil.api.client.render.light.data.PointLightData;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.joml.Vector3f;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.animation.*;


public class EnderExplosion extends Entity implements GeoEntity {

    private static final EntityDataAccessor<Boolean> small = SynchedEntityData.defineId
            (EnderExplosion.class, EntityDataSerializers.BOOLEAN);

    public EnderExplosion(EntityType<? extends EnderExplosion> entityType, Level level) {
        super(entityType, level);
    }

    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(small, false);
    }

    @Override
    public void onClientRemoval() {
        super.onClientRemoval();
    }

    @Override
    public boolean canCollideWith(Entity entity) {
        return super.canCollideWith(entity);
    }

    @Override
    public void tick() {
        super.tick();

        if (!level().isClientSide && tickCount > 25)
            discard();
    }

    public void setSmall(boolean f){
        if (!this.level().isClientSide){
            this.entityData.set(small, f);
        }
    }

    public boolean getSmall() {
        return this.entityData.get(small);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag) {

    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag) {

    }

    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);


    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }


    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 0, this::predicate));
        AnimationController<EnderExplosion> controller = new AnimationController<>(this, "controller", 0, this::predicate);
        controller.triggerableAnim("explosion.blow", RawAnimation.begin().thenPlayAndHold("explosion.blow"));
        controller.triggerableAnim("explosion.blow_small", RawAnimation.begin().thenPlayAndHold("explosion.blow_small"));

        controllers.add(controller);
    }


    private PlayState predicate(AnimationState<EnderExplosion> enderExplosionAnimationState) {
        triggerAnim("controller","explosion.blow" + (getSmall() ? "_small" : ""));
        return PlayState.CONTINUE;
    }
}

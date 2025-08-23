package com.keko.affix.entity.enderFist;

import com.keko.affix.AffixClient;
import com.keko.affix.affixLogics.BoxHelper;
import com.keko.affix.entity.ModEntities;
import com.keko.affix.entity.enderExplosion.EnderExplosion;
import com.keko.affix.entity.infernalDragon.InfernalDragon;
import com.keko.affix.items.ModItems;
import com.keko.affix.sounds.ModSounds;
import net.fabricmc.loader.impl.lib.sat4j.core.Vec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.*;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix3f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.animation.*;

import javax.swing.*;
import java.util.ArrayList;

public class EnderFist extends AbstractArrow implements GeoEntity {

    private static final EntityDataAccessor<Vector3f> sleevePosition = SynchedEntityData.defineId
            (EnderFist.class, EntityDataSerializers.VECTOR3);
    private static final EntityDataAccessor<Boolean> left = SynchedEntityData.defineId
            (EnderFist.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> wantsToExplode = SynchedEntityData.defineId
            (EnderFist.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> wantsToDiscard = SynchedEntityData.defineId
            (EnderFist.class, EntityDataSerializers.BOOLEAN);

    private boolean canBeDestroyed = false;

    public EnderFist(EntityType<? extends EnderFist> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public void tick() {
        if (!level().isClientSide){
            if (getOwner() == null) {
                discard();
                return;
            }
            if (!getOwner().isAlive()) discard();
            Vec3 playerDirection = getOwner().getLookAngle().normalize();
            Vec3 sideVector = playerDirection.cross(getOwner().getUpVector(1.0f));
            float dist = 1.7f;
            if (tickCount % 20 == 0)
                killDupes();

            if (!getLeft()) {
                setSleevePosition(getOwner().getPosition(1.0f).add(sideVector.multiply(dist,dist,dist)).add(0,1,0).add(playerDirection.multiply(0.5,0.5,0.5)).toVector3f());
            } else {
                setSleevePosition(getOwner().getPosition(1.0f).subtract(sideVector.multiply(dist,dist,dist)).add(0,1,0).add(playerDirection.multiply(0.5,0.5,0.5)).toVector3f());
            }


            if (getIfWantsToExplode() && getOwner().onGround())
                createCustomExplosion();

            trackPosition();

        }
        super.tick();
    }

    private void killDupes() {
        AABB aabb = BoxHelper.createEvenVec3Box(getPosition(1.0f), 2);
        for (EnderFist enderFist : level().getEntitiesOfClass(EnderFist.class, aabb)){
            if ((enderFist.getOwner() == this.getOwner()) && (enderFist.getLeft() == this.getLeft()) && tickCount != enderFist.tickCount){
                enderFist.discard();
            }
        }
    }


    private void createCustomExplosion() {
        setWantsToExplode(false);
        if (!getLeft()){
            level().playSound(null, getOwner().getOnPos(), SoundEvents.GENERIC_EXPLODE.value(), SoundSource.PLAYERS, 2f, 0.6f + getRandom().nextFloat());
            EnderExplosion enderExplosion = new EnderExplosion(ModEntities.ENDER_EXPLOSION, level());
            enderExplosion.setPos(getOwner().getPosition(1.0f));
            enderExplosion.setSmall(false);
            level().addFreshEntity(enderExplosion);

            AABB aabb = BoxHelper.createVec3Box(getOwner().getPosition(1.0f), 17,5,17);

            for (LivingEntity livingEntity : level().getEntitiesOfClass(LivingEntity.class, aabb, LivingEntity::isAlive)){
                livingEntity.hurtDuration = 0;
                livingEntity.hurt(level().damageSources().playerAttack((Player) getOwner()), (float) 40 / (livingEntity == getOwner() ? 5 : 1));
            }
        }

    }

    private void trackPosition() {

        Vec3 direction = getSleevePosition().subtract(getPosition(1.0f)).normalize();

        double distance = distanceToSqr(getSleevePosition());
        if (distance > 20) {
            setPos(getSleevePosition());
            setDeltaMovement(0,0,0);
        } else {
            double multiplier = distance/5;
            setDeltaMovement(direction.multiply(multiplier, multiplier, multiplier));
        }

    }

    public void setSleevePosition(Vector3f vector3f){
        if (!this.level().isClientSide){
            this.entityData.set(sleevePosition, vector3f);
        }
    }

    public void setLeft (Boolean left){
        if (!this.level().isClientSide){
            this.entityData.set(this.left, left);
        }
    }

    public void setWantsToDiscard (Boolean wantsToDiscard){
        if (!this.level().isClientSide){
            this.entityData.set(this.wantsToDiscard, wantsToDiscard);
        }
    }

    public void setWantsToExplode (Boolean wantsToExplode){
        if (!this.level().isClientSide){
            this.entityData.set(this.wantsToExplode, wantsToExplode);
        }
    }

    public boolean getIfWantsToExplode() {
        return this.entityData.get(wantsToExplode);
    }

    public boolean getIfWantsToDiscard() {
        return this.entityData.get(wantsToDiscard);
    }

    public boolean getLeft() {
        return this.entityData.get(left);
    }

    public Vec3 getSleevePosition() {
        return new Vec3(
                this.entityData.get(sleevePosition)
        );
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        compoundTag.putBoolean("isLeft", this.getLeft());
        compoundTag.putBoolean("wantsToExplode", this.getIfWantsToExplode());
        compoundTag.putBoolean("wantsToDiscard", this.getIfWantsToDiscard());

        Vec3 sleevePos = getSleevePosition();
        compoundTag.putFloat("sleevePosX", (float) sleevePos.x);
        compoundTag.putFloat("sleevePosY", (float) sleevePos.y);
        compoundTag.putFloat("sleevePosZ", (float) sleevePos.z);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        if (compoundTag.contains("isLeft")) {
            this.setLeft(compoundTag.getBoolean("isLeft"));
        }
        if (compoundTag.contains("wantsToDiscard")){
            this.setWantsToDiscard(compoundTag.getBoolean("wantsToDiscard"));

        }
    }

    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(sleevePosition, new Vector3f(0,0,0));
        builder.define(left, false);
        builder.define(wantsToExplode, false);
        builder.define(wantsToDiscard, false);
    }


    @Override
    public boolean isNoPhysics() {
        return true;
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    @Override
    protected boolean tryPickup(Player player) {
        return false;
    }

    @Override
    public boolean isPickable() {
        return false;
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return Items.GLASS.getDefaultInstance();
    }


    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);


    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }


    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 0, this::predicate));
        AnimationController<EnderFist> controller = new AnimationController<>(this, "controller", 0, this::predicate);
        controller.triggerableAnim("ender_fist.punch", RawAnimation.begin().thenPlay("ender_fist.punch"));

        controllers.add(controller);
    }

    private PlayState predicate(AnimationState<EnderFist> fabricPocketAnimationState) {
        return PlayState.CONTINUE;
    }

    public void punch() {
        stopTriggeredAnim("controller","ender_fist.punch");
        triggerAnim("controller","ender_fist.punch");
        level().playSound(null, getOwner().getOnPos(), ModSounds.PUNCH, SoundSource.PLAYERS, 1F, (float) (0.5 + level().random.nextFloat()));

        Vec3 direction = getOwner().getViewVector(1.0f).normalize();
        int distance = 7;
        Vec3 position = getOwner().getEyePosition();

        for (int i = 0 ; i < distance; i++) {
            AABB aabb = BoxHelper.createEvenVec3Box(position, 2);

            for (LivingEntity livingEntity : level().getEntitiesOfClass(LivingEntity.class, aabb, LivingEntity::isAlive)){
                if (livingEntity != getOwner()){
                    livingEntity.hurtDuration = 0;
                    ((Entity) livingEntity).invulnerableTime = 0;
                    livingEntity.hurt(level().damageSources().playerAttack((Player) getOwner()), 3);
                }
            }
            position = position.add(direction);

        }


    }
}

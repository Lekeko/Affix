package com.keko.affix.entity.infernalPrison;

import com.keko.affix.sounds.ModSounds;
import foundry.veil.api.client.render.VeilRenderSystem;
import foundry.veil.api.client.render.light.data.PointLightData;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.animation.*;


public class InfernalPrison extends AbstractArrow implements GeoEntity {
    private boolean soundPlayed = false;

    PointLightData light = new PointLightData().setBrightness(0.001f).setRadius(54f).setColor(255,212,102);
    private static final EntityDataAccessor<Integer> order = SynchedEntityData.defineId(
            InfernalPrison.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> mobId = SynchedEntityData.defineId(
            InfernalPrison.class, EntityDataSerializers.INT);


    public InfernalPrison(EntityType<? extends AbstractArrow> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public void onClientRemoval() {
        VeilRenderSystem.renderer().getLightRenderer().getLights(light.getType()).removeIf(l -> l.getLightData().equals(light));
        super.onClientRemoval();
    }

    @Override
    public void recreateFromPacket(ClientboundAddEntityPacket clientboundAddEntityPacket) {
        super.recreateFromPacket(clientboundAddEntityPacket);
        triggerAnim("controller","prison.init");
        VeilRenderSystem.renderer().getLightRenderer().addLight(light);

    }


    @Override
    public void tick() {
        super.tick();
        light.setPosition(this.getX(), this.getY(), this.getZ());

        if (!level().isClientSide && tickCount > 180 + getOrder() + 5) discard();
        if (!level().isClientSide){
            if (tickCount == getOrder()+5) {
                this.playSound(ModSounds.SPEAR, 11F, 0.6f + random.nextFloat());
            }


            this.setDeltaMovement(this.getDeltaMovement().multiply(0.9f,0.9f,0.9f));
            Entity entity = level().getEntity(getTrappedMob());
            if (entity != null)
                entity.setPos(this.getPosition(1.0f).add(0,0.3f,0));

            if (tickCount == 100 && entity != null && getOwner() != null){
                DamageSource damageSource = this.level().damageSources().indirectMagic(this, this.getOwner());
                entity.hurt(damageSource, 1);
            }
        }
    }

    @Override
    public SoundSource getSoundSource() {
        return SoundSource.PLAYERS;
    }

    public int getOrder() {
        return this.entityData.get(order);
    }
    public int getTrappedMob() {
        return this.entityData.get(mobId);
    }

    public void setOrder(int f){
        if (!this.level().isClientSide){
            this.entityData.set(order, f);
        }
    }
    public void setTrappedMob(int f){
        if (!this.level().isClientSide){
            this.entityData.set(mobId, f);
        }
    }



    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(order, 0);
        builder.define(mobId, -1);
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
        AnimationController<InfernalPrison> controller = new AnimationController<>(this, "controller", 0, this::predicate);
        controller.triggerableAnim("prison.init",
                RawAnimation.begin().
                        thenPlay("prison.init")
                        .thenPlayAndHold("prison.exit"));

        controllers.add(controller);
    }


    private PlayState predicate(AnimationState<InfernalPrison> fabricPocketAnimationState) {
        return PlayState.CONTINUE;
    }
}

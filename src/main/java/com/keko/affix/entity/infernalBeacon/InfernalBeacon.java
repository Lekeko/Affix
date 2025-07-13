package com.keko.affix.entity.infernalBeacon;

import foundry.veil.api.client.render.VeilRenderSystem;
import foundry.veil.api.client.render.light.data.PointLightData;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.joml.Vector3f;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.animation.*;

import java.util.ArrayList;


public class InfernalBeacon extends Entity implements GeoEntity {
    private static final EntityDataAccessor<Integer> type = SynchedEntityData.defineId(
            InfernalBeacon.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Vector3f> color = SynchedEntityData.defineId(
            InfernalBeacon.class, EntityDataSerializers.VECTOR3);
    private final ArrayList<Holder<MobEffect>> effects = new ArrayList<>();

    private float timer = 0;

    PointLightData light = new PointLightData();

    public InfernalBeacon(EntityType<? extends InfernalBeacon> entityType, Level level) {
        super(entityType, level);
        effects.add(MobEffects.DAMAGE_BOOST);
        effects.add(MobEffects.DIG_SPEED);
        effects.add(MobEffects.REGENERATION);
        effects.add(MobEffects.MOVEMENT_SPEED);
    }

    public int getBeaconType() {
        return this.entityData.get(type);
    }
    public Vector3f getColor() {
        return this.entityData.get(color);
    }

    public void setType(int f){
        if (!this.level().isClientSide){
            this.entityData.set(type, f);
        }
    }
    public void setColor(Vector3f f){
        if (!this.level().isClientSide){
            this.entityData.set(color, f);
        }
    }


    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(type, 0);
        builder.define(color, new Vector3f(255,255,255));
    }


    @Override
    public void onClientRemoval() {
        VeilRenderSystem.renderer().getLightRenderer().getLights(light.getType()).removeIf(l -> l.getLightData().equals(light));
        super.onClientRemoval();
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    @Override
    public void recreateFromPacket(ClientboundAddEntityPacket clientboundAddEntityPacket) {
        super.recreateFromPacket(clientboundAddEntityPacket);
        VeilRenderSystem.renderer().getLightRenderer().addLight(light);
        
    }

    public float getTimer() {
        return timer;
    }

    public void addTimer(float time){
        timer+=time;
    }

    @Override
    public void tick() {
        super.tick();

        light.setPosition(this.getX(), this.getY(), this.getZ()).setBrightness(0.01F).setRadius(17).setColor(getColor().x,getColor().y,getColor().z);

        if (!level().isClientSide &&  this.getBeaconType() != 0){
            giveEffects();
        }

        if (!level().isClientSide && (this.tickCount > 20 * 90 || this.getBeaconType() == 0))
            discard();

        if (level().isClientSide && this.getX() != this.xOld && this.getY() != this.yOld && this.getZ() != this.zOld)
            level().addParticle(ParticleTypes.END_ROD, true, this.getX(), this.getY(), this.getZ(),
                    level().random.nextFloat() - 0.5f,
                    level().random.nextFloat() - 0.5f,
                    level().random.nextFloat() - 0.5f);

    }

    private void giveEffects() {
        for (Player player : level().getEntitiesOfClass(Player.class, new AABB(getX()+14, getY()+14, getZ()+14, getX()-14, getY()-14, getZ()-14)))
            if (player.distanceTo(this) <= 14)
                player.addEffect(new MobEffectInstance(effects.get(getBeaconType()-1), 20 * 30,effects.get(getBeaconType()-1).equals(MobEffects.DIG_SPEED) ? 7 : 3));
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
        AnimationController<InfernalBeacon> controller = new AnimationController<>(this, "controller", 0, this::predicate);
        controller.triggerableAnim("beacon.spin", RawAnimation.begin().thenPlayXTimes("beacon.spin", 5));

        controllers.add(controller);
    }


    private PlayState predicate(AnimationState<InfernalBeacon> fabricPocketAnimationState) {
        triggerAnim("controller","beacon.spin");
        return PlayState.CONTINUE;
    }
}

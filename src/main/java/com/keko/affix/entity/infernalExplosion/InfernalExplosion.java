package com.keko.affix.entity.infernalExplosion;

import com.keko.affix.affixLogics.ModDamageTypes;
import com.keko.affix.entity.infernalDragon.InfernalDragon;
import foundry.veil.api.client.registry.LightTypeRegistry;
import foundry.veil.api.client.render.VeilRenderSystem;
import foundry.veil.api.client.render.light.data.LightData;
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


public class InfernalExplosion extends Entity implements GeoEntity {
    private boolean lightAdded = false;
    private static final EntityDataAccessor<Float> size = SynchedEntityData.defineId(
            InfernalExplosion.class, EntityDataSerializers.FLOAT);

    PointLightData light = new PointLightData();

    public InfernalExplosion(EntityType<? extends InfernalExplosion> entityType, Level level) {
        super(entityType, level);
    }

    public float getSize() {
        return this.entityData.get(size);
    }

    public void setSize(float f){
        if (!this.level().isClientSide){
            this.entityData.set(size, f);
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(size, 1.0f);
    }

    @Override
    public void onClientRemoval() {
        VeilRenderSystem.renderer().getLightRenderer().getLights(light.getType()).removeIf(l -> l.getLightData().equals(light));

        super.onClientRemoval();
    }

    @Override
    public boolean canCollideWith(Entity entity) {
        entity.hurt(level().damageSources().generic(), 10);
        return super.canCollideWith(entity);
    }

    @Override
    public void tick() {
        super.tick();
        light.setPosition(this.getX(), this.getY(), this.getZ()).setBrightness(0.02f / (tickCount/ 2f)).setRadius(19f * getSize()).setColor(255,121,242);
        if (!lightAdded && level().isClientSide){
            VeilRenderSystem.renderer().getLightRenderer().addLight(light);
            lightAdded = true;
        }
        if (level().isClientSide && this.tickCount > 10)
            VeilRenderSystem.renderer().getLightRenderer().getLights(light.getType()).removeIf(l -> l.getLightData().equals(light));

        if (!level().isClientSide){
            damageEnemies();
        }

        if (!level().isClientSide && this.tickCount > 20)
            discard();

        if (level().isClientSide && this.getX() != this.xOld && this.getY() != this.yOld && this.getZ() != this.zOld)
            level().addParticle(ParticleTypes.END_ROD, true, this.getX(), this.getY(), this.getZ(),
                    -this.getDeltaMovement().x / 3 + level().random.nextFloat() - 0.5f,
                    -this.getDeltaMovement().y / 3 - level().random.nextFloat() - 0.5f,
                    -this.getDeltaMovement().z / 3 + level().random.nextFloat() - 0.5f);

    }

    private void damageEnemies() {
        AABB aabb = new AABB(getPosition(1.0f).x + 5, getPosition(1.0f).y + 5, getPosition(1.0f).z + 5,
                getPosition(1.0f).x - 5, getPosition(1.0f).y - 5, getPosition(1.0f).z - 5);
        for (LivingEntity livingEntity : level().getEntitiesOfClass(LivingEntity.class, aabb, LivingEntity::isAlive))
            livingEntity.hurt(ModDamageTypes.of(level(), ModDamageTypes.INFERNAL_DAMAGE_TYPE), 12);

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
        AnimationController<InfernalExplosion> controller = new AnimationController<>(this, "controller", 0, this::predicate);
        controller.triggerableAnim("infernal_explosion.spin", RawAnimation.begin().thenPlayAndHold("infernal_explosion.spin"));

        controllers.add(controller);
    }


    private PlayState predicate(AnimationState<InfernalExplosion> fabricPocketAnimationState) {
        triggerAnim("controller","infernal_explosion.spin");
        return PlayState.CONTINUE;
    }
}

package com.keko.affix.entity.infernalDragon;

import com.keko.affix.Affix;
import com.keko.affix.effects.ModStatusEffects;
import com.keko.affix.entity.ModEntities;
import com.keko.affix.entity.infernalExplosion.InfernalExplosion;
import com.keko.affix.sounds.ModSounds;
import foundry.veil.api.client.render.VeilRenderSystem;
import foundry.veil.api.client.render.light.data.PointLightData;
import foundry.veil.api.quasar.data.ParticleSettings;
import foundry.veil.api.quasar.particle.ParticleEmitter;
import foundry.veil.api.quasar.particle.ParticleSystemManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.animation.*;

import java.awt.*;


public class InfernalDragon extends AbstractArrow implements GeoEntity {
    private boolean lightAdded = false;
    private boolean light2Added = false;

    PointLightData light = new PointLightData();
    PointLightData lightLaser = new PointLightData();

    private static final EntityDataAccessor<Vector3f> attackPosition = SynchedEntityData.defineId
            (InfernalDragon.class, EntityDataSerializers.VECTOR3);

    public InfernalDragon(EntityType<? extends InfernalDragon> entityType, Level level) {
        super(entityType, level);
    }


    @Override
    protected @NotNull ItemStack getDefaultPickupItem() {
        return Items.GLASS.getDefaultInstance();
    }

    @Override
    public boolean isNoPhysics() {
        return true;
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {

    }

    public void setAttackPosition(Vector3f vector3f){
        if (!this.level().isClientSide){
            this.entityData.set(attackPosition, vector3f);
        }
    }

    public Vec3 getAttackPosition() {
        return new Vec3(
                this.entityData.get(attackPosition)
        );
    }

    @Override
    public void remove(RemovalReason removalReason) {
        if (!level().isClientSide ){
            int radius = 40;
            AABB aabb = new AABB(getAttackPosition().x + radius, getAttackPosition().y + radius, getAttackPosition().z + radius, getAttackPosition().x - radius, getAttackPosition().y - radius, getAttackPosition().z - radius);
            for (LivingEntity livingEntity : level().getEntitiesOfClass(LivingEntity.class, aabb, LivingEntity::isAlive)) {
                livingEntity.setDeltaMovement(livingEntity.getPosition(1.0f).subtract(getAttackPosition()).normalize().multiply(6, 6, 6).add(0, 5, 0));
                livingEntity.hurtMarked = true;
            }

            InfernalExplosion infernalExplosion = new InfernalExplosion(ModEntities.INFERNAL_EXPLOSION, level());
            infernalExplosion.setPos(getAttackPosition());
            infernalExplosion.setSize(5);
            level().addFreshEntity(infernalExplosion);
            level().playSound(null, new BlockPos((int) getAttackPosition().x, (int) getAttackPosition().y, (int) getAttackPosition().z), ModSounds.ACCELERATOR, SoundSource.PLAYERS, 10, 0.2f);


        }
        super.remove(removalReason);
    }

    @Override
    public void onClientRemoval() {
        VeilRenderSystem.renderer().getLightRenderer().getLights(light.getType()).removeIf(l -> l.getLightData().equals(light));
        VeilRenderSystem.renderer().getLightRenderer().getLights(light.getType()).removeIf(l -> l.getLightData().equals(lightLaser));
        super.onClientRemoval();
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(attackPosition, new Vector3f(0,0,0));
    }

    @Override
    public void tick() {
        super.tick();
        //Dont even ask
        //server

        if (!level().isClientSide && this.tickCount > 127) {
            discard();
        }
        if (!level().isClientSide && this.tickCount == 30){
            this.setDeltaMovement(this.getDeltaMovement().multiply(0.2,0.2,0.2));
        }
        if (!level().isClientSide && this.tickCount > 30) {
            int radius = 40;
            AABB aabb = new AABB(getAttackPosition().x + radius, getAttackPosition().y + radius, getAttackPosition().z + radius, getAttackPosition().x - radius, getAttackPosition().y - radius, getAttackPosition().z - radius);
            for (LivingEntity livingEntity : level().getEntitiesOfClass(LivingEntity.class, aabb, LivingEntity::isAlive)) {
                livingEntity.addEffect(new MobEffectInstance(ModStatusEffects.ADUSTED, 20, 1));
            }
        }
        if (!level().isClientSide && this.tickCount == 30) {
            level().playSound(null, new BlockPos((int) getAttackPosition().x, (int) getAttackPosition().y, (int) getAttackPosition().z), ModSounds.DRAGON_BLAST, SoundSource.PLAYERS, 10, 1f);

        }


        //client
        if (level().isClientSide && tickCount > 30 && tickCount < 90) {
            spawnShootingParticle(getPosition(1.0f), ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, "fire_stars"), 0);
        }
        if (level().isClientSide && tickCount == 126) {
            for (int i = 0; i < 10; i++)
                spawnParticle(getAttackPosition(), ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, "fire_stars"));

        }
        if (level().isClientSide && tickCount > 60 && lightLaser.getRadius() >0) {
            lightLaser.setRadius(lightLaser.getRadius() - 0.5f);
        }

        Color color = new Color(198, 164, 110, 255);
        if (level().isClientSide){
            light.setPosition(this.getX(), this.getY(), this.getZ()).setBrightness(0.01f).setRadius(435f).setColor(color.getRed(), color.getGreen(), color.getBlue());
            if (!light2Added)
                lightLaser.setPosition(getAttackPosition().x, getAttackPosition().y, getAttackPosition().z).
                        setBrightness(0.19f).setRadius(90).setColor(color.getRed(), color.getGreen(), color.getBlue());
        }
        if (!lightAdded && level().isClientSide){
            VeilRenderSystem.renderer().getLightRenderer().addLight(light);
            lightAdded = true;
        }
        if (!light2Added && level().isClientSide && tickCount > 30){
            VeilRenderSystem.renderer().getLightRenderer().addLight(lightLaser);
            light2Added = true;
        }
    }


    @Override
    protected void onHitBlock(BlockHitResult blockHitResult) {
    }

    @Override
    protected boolean tryPickup(Player player) {
        return false;
    }



    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);


    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }


    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 0, this::predicate));
        AnimationController<InfernalDragon> controller = new AnimationController<>(this, "controller", 0, this::predicate);
        controller.triggerableAnim("dragon.shot", RawAnimation.begin().thenPlay("dragon.shot").thenPlayAndHold("dragon.plunge"));

        controllers.add(controller);
    }


    private PlayState predicate(AnimationState<InfernalDragon> fabricPocketAnimationState) {
        if (tickCount > 30)
            triggerAnim("controller","dragon.shot");
        return PlayState.CONTINUE;
    }

    public void spawnParticle(Vec3 position, ResourceLocation id){
        try {
            ParticleSystemManager manager = VeilRenderSystem.renderer().getParticleManager();
            ParticleEmitter emitter = manager.createEmitter(id);
            emitter.setPosition(position);
            emitter.setParticleSettings(new ParticleSettings(200, 0.1f, 0, 60, 0,
                    new Vector3f(
                            (level().random.nextFloat() -.5f),
                            (level().random.nextFloat() -.5f),
                            (level().random.nextFloat() -.5f)
                    ),
                    true, true, false, false, false));
            manager.addParticleSystem(emitter);
        } catch (Exception ignored) {

        }
    }

    public void spawnShootingParticle(Vec3 position, ResourceLocation id, int size ){
        try {
            ParticleSystemManager manager = VeilRenderSystem.renderer().getParticleManager();
            ParticleEmitter emitter = manager.createEmitter(id);
            emitter.setPosition(position);
            emitter.setParticleSettings(new ParticleSettings(29, size, 0, 60, 0, this.getDeltaMovement().toVector3f().mul(14).
                    add(
                            (level().random.nextFloat() -.5f) / 2,
                            (level().random.nextFloat() -.5f) / 2,
                            (level().random.nextFloat() -.5f) / 2
                    ),
                    false, false, false, false, false));
            manager.addParticleSystem(emitter);
        } catch (Exception ignored) {

        }
    }
}

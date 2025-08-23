package com.keko.affix.entity.enderFingers;

import com.keko.affix.Affix;
import com.keko.affix.affixLogics.BoxHelper;
import com.keko.affix.affixLogics.ModDamageTypes;
import com.keko.affix.entity.infernalDragon.InfernalDragon;
import com.keko.affix.midLib.AffixConfigs;
import com.keko.affix.particle.ModParticles;
import com.keko.affix.sounds.ModSounds;
import foundry.veil.api.client.render.VeilRenderSystem;
import foundry.veil.api.client.render.light.data.PointLightData;
import foundry.veil.api.quasar.data.ParticleSettings;
import foundry.veil.api.quasar.particle.ParticleEmitter;
import foundry.veil.api.quasar.particle.ParticleSystemManager;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.animation.*;

import java.awt.*;


public class EnderFingers extends AbstractArrow implements GeoEntity {
    private LivingEntity target;
    private boolean wantToDiscard = false;
    private int timeToDiscard = 10;
    PointLightData light = new PointLightData();


    public EnderFingers(EntityType<? extends EnderFingers> entityType, Level level) {
        super(entityType, level);
    }

    public boolean isWantToDiscard() {
        return wantToDiscard;
    }

    public void setTarget(LivingEntity target) {
        this.target = target;
    }

    public LivingEntity getTarget() {
        return target;
    }

    @Override
    public boolean isCurrentlyGlowing() {
        return super.isCurrentlyGlowing();
    }

    @Override
    public void onClientRemoval() {
        VeilRenderSystem.renderer().getLightRenderer().getLights(light.getType()).removeIf(l -> l.getLightData().equals(light));
        super.onClientRemoval();
    }

    @Override
    public boolean canCollideWith(Entity entity) {
        return super.canCollideWith(entity);
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket(ServerEntity serverEntity) {
        return super.getAddEntityPacket(serverEntity);
    }

    @Override
    public void recreateFromPacket(ClientboundAddEntityPacket clientboundAddEntityPacket) {
        super.recreateFromPacket(clientboundAddEntityPacket);
        VeilRenderSystem.renderer().getLightRenderer().addLight(light);

    }

    @Override
    public void tick() {
        super.tick();
        if (!level().isClientSide){
                if (getTarget() == null){
                    wantToDiscard = true;
                }else {
                    if (!getTarget().isAlive()) wantToDiscard = true;
                }


                if (wantToDiscard){
                    this.hurtMarked = true;
                    timeToDiscard--;
                    if (timeToDiscard <= 0)
                        discard();
                    return;
                }

            Vec3 currentDirection = this.getDeltaMovement().normalize();
            Vec3 targetDirection = this.getTarget().getPosition(1.0f).subtract(getPosition(1.0f)).normalize();

            currentDirection = currentDirection.lerp(targetDirection, Math.min(tickCount/100f, 0.4f));

            this.setDeltaMovement(currentDirection.scale(4));
            this.hurtMarked = true;


        }else {
            if (wantToDiscard){
                if (timeToDiscard == 10)
                    level().addParticle(ModParticles.ROCKET_EXPLOSION, getX(), getY() + 1, getZ(), 0,0,0);
                timeToDiscard--;
            }
            Color color = Color.decode(AffixConfigs.jamPrimaryColor);
            light.setPosition(this.getX(), this.getY(), this.getZ()).setBrightness(.01f).setRadius(3f).setColor(color.getRed(), color.getGreen(), color.getBlue());
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult blockHitResult) {
        super.onHitBlock(blockHitResult);
        createExplosion();
        if (!level().isClientSide){
            wantToDiscard = true;
        }else {
            wantToDiscard = true;
            spawnParticle(getPosition(1.0f), ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, "finger_stars"));
        }
    }

    private void createExplosion() {

        if (!level().isClientSide && getOwner() instanceof Player){
            level().playSound(this,getOnPos(), ModSounds.ROCKET_EXPLODE, SoundSource.PLAYERS, 1.4f, 0.5f + level().random.nextFloat());
            ((ServerLevel)level()).sendParticles(ModParticles.ROCKET_EXPLOSION, getX(), getY(), getZ(), 3, 0,0,0, 2);
            AABB aabb = BoxHelper.createEvenBox(getOnPos(), 4);
            for (LivingEntity livingEntity : level().getEntitiesOfClass(LivingEntity.class, aabb, LivingEntity::isAlive)){
                if (livingEntity != getOwner()){
                    livingEntity.hurt(level().damageSources().playerAttack((Player) getOwner()), 20 + livingEntity.getMaxHealth()/2);
                    livingEntity.hurtDuration = 0;
                    livingEntity.invulnerableTime = 0;
                }
            }
        }

    }

    @Override
    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundEvents.EMPTY;
    }


    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        super.onHitEntity(entityHitResult);
        createExplosion();
        if (!level().isClientSide){
            wantToDiscard = true;
        }else {
            wantToDiscard = true;
            spawnParticle(getPosition(1.0f), ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, "finger_stars"));
        }
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
    public boolean isNoGravity() {
        return true;
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
        AnimationController<EnderFingers> controller = new AnimationController<>(this, "controller", 0, this::predicate);
        controller.triggerableAnim("finger.spin", RawAnimation.begin().thenLoop("finger.spin"));

        controllers.add(controller);
    }


    private PlayState predicate(AnimationState<EnderFingers> fabricPocketAnimationState) {
        triggerAnim("controller","finger.spin");
        return PlayState.CONTINUE;
    }

    public void spawnParticle(Vec3 position, ResourceLocation id){
        try {
            ParticleSystemManager manager = VeilRenderSystem.renderer().getParticleManager();
            ParticleEmitter emitter = manager.createEmitter(id);
            emitter.setPosition(position);
            manager.addParticleSystem(emitter);
        } catch (Exception ignored) {

        }
    }
}

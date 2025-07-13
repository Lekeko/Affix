package com.keko.affix.entity.fabricPocket;

import com.keko.affix.affixLogics.BoxHelper;
import com.keko.affix.items.ModItems;
import com.keko.affix.sounds.ModSounds;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.animation.*;

import java.util.ArrayList;

public class FabricPocket extends Entity implements GeoEntity {
    private float shyness = 0;
    private final int PARTICLE_COUNT = 20;

    public FabricPocket(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public void tick() {

        if (!this.level().isClientSide){
            if (this.tickCount % 10 == 0) addShynessIfPlayerLooks();
        }

        super.tick();
    }

    private void addShynessIfPlayerLooks() {
        ArrayList<Player> players = new ArrayList<>(level().getEntitiesOfClass(Player.class, BoxHelper.createEvenBox(this.getOnPos(), 10), Player::isAlive));
        for (Player player : players){
            if  (player.getUseItem().is(ModItems.HEAVY_MIRROR)) {
                Vec3 vec3 = player.getViewVector(1.0F).normalize();
                Vec3 vec32 = new Vec3(this.getX() - player.getX(), this.getEyeY() - player.getEyeY(), this.getZ() - player.getZ());
                double d = vec32.length();
                vec32 = vec32.normalize();
                double e = vec3.dot(vec32);
                if (e > (double) 1.0F - 0.025 / d && player.hasLineOfSight(this)) {
                    shyness += 1;
                    level().playSound(null, this.getOnPos(), ModSounds.WARP_BREAK, SoundSource.PLAYERS, 1, 1);
                    spawnParticles();
                    if (shyness >= 10) {
                        this.discard();
                    }
                }
            }
        }
    }

    private void spawnParticles() {
        ((ServerLevel)this.level()).sendParticles(ParticleTypes.END_ROD, getX(), getY(), getZ(), PARTICLE_COUNT, random.nextDouble(), random.nextDouble(), random.nextDouble(), 1);
    }

    @Override
    public void remove(RemovalReason removalReason) {
        if (!level().isClientSide){
            for (int i = 0 ; i < 10; i++){
                ItemEntity entity = new ItemEntity(level(), getX(), getY(), getZ(),level().random.nextBoolean() ? ModItems.FABRIC_OF_REALITY.getDefaultInstance() : ModItems.GRAVITY_CORE.getDefaultInstance() );
                level().addFreshEntity(entity);
            }
        }
        super.remove(removalReason);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {

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
        AnimationController<FabricPocket> controller = new AnimationController<>(this, "controller", 0, this::predicate);
        controller.triggerableAnim("fabric_pocket.rotator", RawAnimation.begin().thenPlay("fabric_pocket.rotator"));

        controllers.add(controller);
    }

    private PlayState predicate(AnimationState<FabricPocket> fabricPocketAnimationState) {
        triggerAnim("controller","fabric_pocket.rotator");
        return PlayState.CONTINUE;
    }
}

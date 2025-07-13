package com.keko.affix.entity.infernalArrow;

import com.keko.affix.entity.ModEntities;
import com.keko.affix.entity.infernalExplosion.InfernalExplosion;
import com.keko.affix.sounds.ModSounds;
import foundry.veil.api.client.registry.LightTypeRegistry;
import foundry.veil.api.client.render.VeilRenderSystem;
import foundry.veil.api.client.render.light.data.PointLightData;
import foundry.veil.api.client.render.light.renderer.LightRenderer;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.animation.*;


public class InfernalArrow extends AbstractArrow implements GeoEntity {
    private double speed = 0;
    private boolean lightAdded = false;

    PointLightData light = new PointLightData();

    public InfernalArrow(EntityType<? extends InfernalArrow> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected @NotNull ItemStack getDefaultPickupItem() {
        return Items.GLASS.getDefaultInstance();
    }

    @Override
    public boolean isNoPhysics() {
        return false;
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {

    }

    @Override
    public void onClientRemoval() {
        VeilRenderSystem.renderer().getLightRenderer().getLights(light.getType()).removeIf(l -> l.getLightData().equals(light));

        super.onClientRemoval();
    }

    @Override
    public void tick() {
        updateSpeed();
        super.tick();
        light.setPosition(this.getX(), this.getY(), this.getZ()).setBrightness(0.01f).setRadius(15f).setColor(255,121,242);
        if (!lightAdded && level().isClientSide){
            VeilRenderSystem.renderer().getLightRenderer().addLight(light);
            lightAdded = true;
        }
        if (!level().isClientSide && this.tickCount > 70)
            discard();


        if (level().isClientSide && this.getX() != this.xOld && this.getY() != this.yOld && this.getZ() != this.zOld)
            level().addParticle(ParticleTypes.END_ROD, true, this.getX(), this.getY(), this.getZ(),
                    -this.getDeltaMovement().x / 3 + level().random.nextFloat() - 0.5f,
                    -this.getDeltaMovement().y / 3 - level().random.nextFloat() - 0.5f,
                    -this.getDeltaMovement().z / 3 + level().random.nextFloat() - 0.5f);

    }

    private void updateSpeed() {
        speed = (Mth.abs((float) (getX() - xOld)) +
                Mth.abs((float) (getY() - yOld)) +
                Mth.abs((float) (getZ() - zOld))) / 3;
    }

    public double getSpeed() {
        return speed;
    }

    @Override
    protected void onHitBlock(BlockHitResult blockHitResult) {
        this.level().playSound(null, this.getOnPos(), ModSounds.INFERNAL_ARROW_HIT, SoundSource.PLAYERS, 10F, (float) (0.5 + (this.level().random.nextFloat() / 2f)));
        createCustomExplosion();
        super.onHitBlock(blockHitResult);
    }

    private void createCustomExplosion() {
        if(!level().isClientSide){
            InfernalExplosion infernalExplosion = new InfernalExplosion(ModEntities.INFERNAL_EXPLOSION, level());
            infernalExplosion.setPos(this.getPosition(1.0f));
            level().addFreshEntity(infernalExplosion);
            discard();
        }else {
            VeilRenderSystem.renderer().getLightRenderer().getLights(light.getType()).remove(light);

        }
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
        AnimationController<InfernalArrow> controller = new AnimationController<>(this, "controller", 0, this::predicate);
        controller.triggerableAnim("infernal_arrow.trail", RawAnimation.begin().thenPlayAndHold("infernal_arrow.trail"));

        controllers.add(controller);
    }


    private PlayState predicate(AnimationState<InfernalArrow> fabricPocketAnimationState) {
        if (getX() == xOld && getY() == yOld && getZ() == zOld)
            triggerAnim("controller","infernal_arrow.trail");
        return PlayState.CONTINUE;
    }
}

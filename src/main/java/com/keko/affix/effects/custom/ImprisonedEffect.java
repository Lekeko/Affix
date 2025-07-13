package com.keko.affix.effects.custom;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class ImprisonedEffect extends MobEffect {
    public ImprisonedEffect() {
        super(MobEffectCategory.HARMFUL, 0xffffff);
    }


    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        return super.applyEffectTick(entity, amplifier);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int i, int j) {
        return true;
    }

    @Override
    public void onEffectAdded(LivingEntity livingEntity, int i) {
        super.onEffectAdded(livingEntity, i);
        if (!livingEntity.level().isClientSide){

        }
    }
}

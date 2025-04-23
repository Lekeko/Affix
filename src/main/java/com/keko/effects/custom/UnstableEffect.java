package com.keko.effects.custom;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class UnstableEffect extends MobEffect {
    public UnstableEffect() {
        super(MobEffectCategory.HARMFUL, 0xffffff);
    }


    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        return super.applyEffectTick(entity, amplifier);
    }
}

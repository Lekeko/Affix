package com.keko.affix.effects.custom;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class AcceleratedEffect extends MobEffect {
    public AcceleratedEffect() {
        super(MobEffectCategory.BENEFICIAL, 0xffffff);
    }


    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        return super.applyEffectTick(entity, amplifier);
    }
}

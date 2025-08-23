package com.keko.affix.effects.custom;

import com.keko.affix.affixLogics.ModDamageTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class AdustedEffect extends MobEffect {
    public AdustedEffect() {
        super(MobEffectCategory.HARMFUL, 0xffffff);
    }


    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        if (!entity.level().isClientSide){
            entity.hurt(ModDamageTypes.of(entity.level(), ModDamageTypes.SCORCHED_DAMAGE_TYPE), entity.getMaxHealth() / 8 + 2);

        }
        return super.applyEffectTick(entity, amplifier);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int i, int j) {

        return true;
    }
}

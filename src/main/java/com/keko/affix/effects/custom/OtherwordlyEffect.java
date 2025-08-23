package com.keko.affix.effects.custom;

import com.keko.affix.affixLogics.ModDamageTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.EnderMan;

public class OtherwordlyEffect extends MobEffect {
    public OtherwordlyEffect() {
        super(MobEffectCategory.BENEFICIAL, 0xcc00cc);
    }


    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        return super.applyEffectTick(entity, amplifier);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int i, int j) {
        return true;
    }


}

package com.keko.affix.effects;

import com.keko.affix.Affix;
import com.keko.affix.effects.custom.AcceleratedEffect;
import com.keko.affix.effects.custom.AdustedEffect;
import com.keko.affix.effects.custom.ImprisonedEffect;
import com.keko.affix.effects.custom.UnstableEffect;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;

public class ModStatusEffects {
    public static Holder<MobEffect> UNSTABLE;
    public static Holder<MobEffect> ACCELERATED;
    public static Holder<MobEffect> ADUSTED;
    public static Holder<MobEffect> IMPRINSONED;

    private static Holder<MobEffect> register(String id, MobEffect effect) {
        return Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT, ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, id), effect);
    }

    public static void registerStatusEffects(){
        ACCELERATED = register("accelerated", new AcceleratedEffect());
        UNSTABLE = register("unstable", new UnstableEffect());
        ADUSTED = register("adusted", new AdustedEffect());
        IMPRINSONED = register("imprisoned", new ImprisonedEffect());
    }
}

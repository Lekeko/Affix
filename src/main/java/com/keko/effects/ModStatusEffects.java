package com.keko.effects;

import com.keko.Affix;
import com.keko.effects.custom.AcceleratedEffect;
import com.keko.effects.custom.UnstableEffect;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;

public class ModStatusEffects {
    public static Holder<MobEffect> UNSTABLE;
    public static Holder<MobEffect> ACCELERATED;

    private static Holder<MobEffect> register(String id) {
        if (id.equals("unstable"))
            return Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT, ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, id), new UnstableEffect());
        return Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT, ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, id), new AcceleratedEffect());
    }

    public static void registerStatusEffects(){
        ACCELERATED = register("accelerated");
        UNSTABLE = register("unstable");
    }
}

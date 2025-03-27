package com.keko.particle;

import com.keko.Affix;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;

public class ModParticles {

    public static final SimpleParticleType LOCATOR_PARTICLE = FabricParticleTypes.simple(true);


    public static void register(){
        Registry.register(BuiltInRegistries.PARTICLE_TYPE, ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, "locator_particle"),
                LOCATOR_PARTICLE);
    }

}

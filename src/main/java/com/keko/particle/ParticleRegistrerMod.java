package com.keko.particle;

import com.keko.particle.custom.LocatorParticle;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;

public class ParticleRegistrerMod {
    public static void register() {
        ParticleFactoryRegistry.getInstance().register(ModParticles.LOCATOR_PARTICLE, LocatorParticle.Provider::new);
    }
}

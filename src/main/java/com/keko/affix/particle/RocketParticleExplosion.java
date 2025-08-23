package com.keko.affix.particle;

import com.keko.affix.midLib.AffixConfigs;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

public class RocketParticleExplosion extends SimpleAnimatedParticle {
    public RocketParticleExplosion(ClientLevel clientLevel, double x, double y, double z, double xd, double yd, double zd, SpriteSet spriteSet) {
        super(clientLevel, x, y, z, spriteSet, 0.0125F);

        this.friction = 0.5f;
        this.lifetime = 5;
        this.quadSize = 1;

        this.setSpriteFromAge(spriteSet);

    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_LIT;
    }

    public static class Factory implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public Factory(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Override
        public @Nullable Particle createParticle(SimpleParticleType particleOptions, ClientLevel clientLevel, double d, double e, double f, double g, double h, double i) {
            return new RocketParticleExplosion(clientLevel, d, e, f, g, h, i, this.spriteSet);
        }
    }
}

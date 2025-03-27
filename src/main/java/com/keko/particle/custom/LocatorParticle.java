package com.keko.particle.custom;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import org.jetbrains.annotations.NotNull;

@Environment(EnvType.CLIENT)
public class LocatorParticle extends TextureSheetParticle {
    private final SpriteSet spriteProvider;


    LocatorParticle(ClientLevel clientLevel, double d, double e, double f, double g, double h, double i, SpriteSet spriteSet) {
        super(clientLevel, d, e, f, f,g,h);
        this.spriteProvider = spriteSet;
        this.xd = g;
        this.yd = h;
        this.zd = i;
        float fa = 1;
        this.rCol = fa;
        this.gCol = fa;
        this.bCol = fa;
        this.quadSize = 0.7f;
        this.lifetime = 60;
        this.setSpriteFromAge(spriteSet);
    }

    public void move(double d, double e, double f) {
        this.setBoundingBox(this.getBoundingBox().move(d, e, f));
        this.setLocationFromBoundingbox();
    }

    public int getLightColor(float tint) {
        return 15728880;
    }
    @Override
    public FacingCameraMode getFacingCameraMode() {
        return FacingCameraMode.LOOKAT_XYZ;
    }

    @Override
    public void tick() {
        fade();
        super.tick();
    }
    private void fade() {
        this.alpha = (-(1/(float)lifetime) * age + 1);
    }

    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_LIT;
    }


    @Environment(EnvType.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet spriteSet) {
            this.sprites = spriteSet;
        }

        public Particle createParticle(SimpleParticleType simpleParticleType, ClientLevel clientLevel, double d, double e, double f, double g, double h, double i) {
            return new LocatorParticle(clientLevel, d, e, f, g, h, i, this.sprites);
        }
    }
}

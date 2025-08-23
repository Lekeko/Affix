package com.keko.affix.entity.enderExplosion;

import com.keko.affix.Affix;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

@Environment(EnvType.CLIENT)
public class EnderExplosionModel extends GeoModel<EnderExplosion> {

    @Override
    public ResourceLocation getModelResource(EnderExplosion infernalExplosion) {
        return ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, "geo/ender_explosion.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(EnderExplosion infernalExplosion) {
        return ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, "textures/entity/ender_explosion.png");

    }

    @Override
    public ResourceLocation getAnimationResource(EnderExplosion infernalExplosion) {
        return ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, "animations/ender_explosion.animation.json");

    }

}

package com.keko.affix.entity.infernalExplosion;

import com.keko.affix.Affix;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

@Environment(EnvType.CLIENT)
public class InfernalExplosionModel extends GeoModel<InfernalExplosion> {

    @Override
    public ResourceLocation getModelResource(InfernalExplosion infernalExplosion) {
        return ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, "geo/infernal_explosion.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(InfernalExplosion infernalExplosion) {
        return ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, "textures/entity/infernal_explosion.png");

    }

    @Override
    public ResourceLocation getAnimationResource(InfernalExplosion infernalExplosion) {
        return ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, "animations/infernal_explosion.animation.json");

    }

}

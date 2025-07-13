package com.keko.affix.entity.infernalBeacon;

import com.keko.affix.Affix;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

@Environment(EnvType.CLIENT)
public class InfernalBeaconModel extends GeoModel<InfernalBeacon> {

    @Override
    public ResourceLocation getModelResource(InfernalBeacon infernalExplosion) {
        return ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, "geo/infernal_beacon.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(InfernalBeacon infernalExplosion) {
        return ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, "textures/entity/infernal_beacon" + infernalExplosion.getBeaconType() + ".png");

    }

    @Override
    public ResourceLocation getAnimationResource(InfernalBeacon infernalExplosion) {
        return ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, "animations/infernal_beacon.animation.json");

    }

}

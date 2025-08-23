package com.keko.affix.entity.enderFist;

import com.keko.affix.Affix;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;


@Environment(EnvType.CLIENT)

public class EnderFistModel extends GeoModel<EnderFist> {
    @Override
    public ResourceLocation getModelResource(EnderFist animatable) {
        return ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, "geo/ender_fist.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(EnderFist animatable) {
        return ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, "textures/entity/ender_fist.png");
    }

    @Override
    public ResourceLocation getAnimationResource(EnderFist animatable) {
        return ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, "animations/ender_fist.animation.json");
    }
}

package com.keko.affix.entity.enderFingers;

import com.keko.affix.Affix;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

@Environment(EnvType.CLIENT)
public class EnderFingersModel extends GeoModel<EnderFingers> {

    @Override
    public ResourceLocation getModelResource(EnderFingers infernalExplosion) {
        return ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, "geo/ender_fingers.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(EnderFingers infernalExplosion) {
        return ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, "textures/entity/ender_fingers.png");

    }

    @Override
    public ResourceLocation getAnimationResource(EnderFingers infernalExplosion) {
        return ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, "animations/ender_fingers.animation.json");

    }

}

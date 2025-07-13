package com.keko.affix.entity.infernalPrison;

import com.keko.affix.Affix;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

@Environment(EnvType.CLIENT)
public class InfernalPrisonModel extends GeoModel<InfernalPrison> {

    @Override
    public ResourceLocation getModelResource(InfernalPrison infernalPrison) {
        return ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, "geo/infernal_prison.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(InfernalPrison infernalPrison) {
        return ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, "textures/entity/infernal_prison.png");

    }

    @Override
    public ResourceLocation getAnimationResource(InfernalPrison infernalPrison) {
        return ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, "animations/infernal_prison.animation.json");

    }

}

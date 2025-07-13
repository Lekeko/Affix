package com.keko.affix.entity.infernalDragon;

import com.keko.affix.Affix;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

@Environment(EnvType.CLIENT)
public class InfernalDragonModel extends GeoModel<InfernalDragon> {

    @Override
    public ResourceLocation getModelResource(InfernalDragon infernalDragon) {
        return ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, "geo/infernal_dragon.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(InfernalDragon infernalDragon) {
        return ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, "textures/entity/infernal_dragon.png");

    }

    @Override
    public ResourceLocation getAnimationResource(InfernalDragon infernalDragon) {
        return ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, "animations/infernal_dragon.animation.json");

    }

}

package com.keko.entity.fabricPocket;

import com.keko.Affix;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;


@Environment(EnvType.CLIENT)

public class FabricPocketModel extends GeoModel<FabricPocket> {
    @Override
    public ResourceLocation getModelResource(FabricPocket animatable) {
        return ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, "geo/fabric_pocket.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(FabricPocket animatable) {
        return ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, "textures/entity/fabric_pocket.png");
    }

    @Override
    public ResourceLocation getAnimationResource(FabricPocket animatable) {
        return ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, "animations/fabric_pocket.animation.json");
    }
}

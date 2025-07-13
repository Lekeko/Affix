package com.keko.affix.entity.infernalPortal;

import com.keko.affix.Affix;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

@Environment(EnvType.CLIENT)
public class InfernalPortalModel extends GeoModel<InfernalPortal> {

    @Override
    public ResourceLocation getModelResource(InfernalPortal infernalPortal) {
        return ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, "geo/infernal_portal.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(InfernalPortal infernalPortal) {
        return ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, "textures/entity/infernal_portal.png");

    }

    @Override
    public ResourceLocation getAnimationResource(InfernalPortal infernalPortal) {
        return ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, "animations/infernal_portal.animation.json");

    }

}

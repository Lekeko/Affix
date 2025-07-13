package com.keko.affix.entity.infernalArrow;

import com.keko.affix.Affix;
import com.keko.affix.entity.fabricPocket.FabricPocket;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.model.GeoModel;

@Environment(EnvType.CLIENT)
public class InfernalArrowModel extends GeoModel<InfernalArrow> {

    @Override
    public ResourceLocation getModelResource(InfernalArrow infernalArrow) {
        return ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, "geo/infernal_arrow.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(InfernalArrow infernalArrow) {
        return ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, "textures/entity/infernal_arrow.png");

    }

    @Override
    public ResourceLocation getAnimationResource(InfernalArrow infernalArrow) {
        return ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, "animations/infernal_arrow.animation.json");

    }

}

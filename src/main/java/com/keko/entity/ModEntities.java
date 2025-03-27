package com.keko.entity;

import com.keko.Affix;
import com.keko.entity.fabricPocket.FabricPocket;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class ModEntities {

    public static final EntityType<FabricPocket> FABRIC_POCKET_ENTITY_TYPE = Registry.register(
            BuiltInRegistries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, "light_entity"),
            EntityType.Builder.of(FabricPocket::new, MobCategory.MISC).sized(0.1f, 0.1f).build());


    public static void registerEntities(){}

}

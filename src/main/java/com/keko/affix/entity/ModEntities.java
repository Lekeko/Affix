package com.keko.affix.entity;

import com.keko.affix.Affix;
import com.keko.affix.entity.enderExplosion.EnderExplosion;
import com.keko.affix.entity.enderFingers.EnderFingers;
import com.keko.affix.entity.enderFist.EnderFist;
import com.keko.affix.entity.enderFist.EnderFistModel;
import com.keko.affix.entity.fabricPocket.FabricPocket;
import com.keko.affix.entity.infernalArrow.InfernalArrow;
import com.keko.affix.entity.infernalBeacon.InfernalBeacon;
import com.keko.affix.entity.infernalDragon.InfernalDragon;
import com.keko.affix.entity.infernalExplosion.InfernalExplosion;
import com.keko.affix.entity.infernalPortal.InfernalPortal;
import com.keko.affix.entity.infernalPrison.InfernalPrison;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class ModEntities {

    public static final EntityType<FabricPocket> FABRIC_POCKET_ENTITY_TYPE = Registry.register(
            BuiltInRegistries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, "fabric_pocket"),
            EntityType.Builder.of(FabricPocket::new, MobCategory.MISC).sized(0.1f, 0.1f).build());

    public static final EntityType<InfernalArrow> INFERNAL_ARROW = Registry.register(
            BuiltInRegistries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, "infernal_arrow"),
            EntityType.Builder.of(InfernalArrow::new, MobCategory.MISC).sized(0.5f, 0.5f).build());

    public static final EntityType<InfernalExplosion> INFERNAL_EXPLOSION = Registry.register(
            BuiltInRegistries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, "infernal_explosion"),
            EntityType.Builder.of(InfernalExplosion::new, MobCategory.MISC).sized(5.5f, 5.5f).build());

    public static final EntityType<InfernalPortal> INFERNAL_PORTAL = Registry.register(
            BuiltInRegistries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, "infernal_portal"),
            EntityType.Builder.of(InfernalPortal::new, MobCategory.MISC).sized(0.5f, 0.5f).build());

    public static final EntityType<InfernalPrison> INFERNAL_PRISON = Registry.register(
            BuiltInRegistries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, "infernal_prison"),
            EntityType.Builder.of(InfernalPrison::new, MobCategory.MISC).sized(0.5f, 0.5f).build());

    public static final EntityType<InfernalBeacon> INFERNAL_BEACON = Registry.register(
            BuiltInRegistries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, "infernal_beacon"),
            EntityType.Builder.of(InfernalBeacon::new, MobCategory.MISC).sized(0.5f, 0.5f).build());

    public static final EntityType<InfernalDragon> INFERNAL_DRAGON = Registry.register(
            BuiltInRegistries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, "infernal_dragon"),
            EntityType.Builder.of(InfernalDragon::new, MobCategory.MISC).sized(5.5f, 5.5f).build());

    public static final EntityType<EnderFingers> ENDER_FINGERS = Registry.register(
            BuiltInRegistries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, "ender_fingers"),
            EntityType.Builder.of(EnderFingers::new, MobCategory.MISC).sized(.1f, .1f).build());

    public static final EntityType<EnderFist> ENDER_FIST = Registry.register(
            BuiltInRegistries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, "ender_fist"),
            EntityType.Builder.of(EnderFist::new, MobCategory.MISC).sized(.1f, .1f).build());

    public static final EntityType<EnderExplosion> ENDER_EXPLOSION = Registry.register(
            BuiltInRegistries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, "ender_explosion"),
            EntityType.Builder.of(EnderExplosion::new, MobCategory.MISC).sized(.1f, .1f).build());

    public static void registerEntities(){}

}

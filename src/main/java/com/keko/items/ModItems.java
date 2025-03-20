package com.keko.items;

import com.keko.Affix;
import com.keko.items.custom.HeavyMirror;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

public class ModItems {

    public static final Item HEAVY_MIRROR = registerItem(new HeavyMirror(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC)), "heavy_mirror");


    public static <T extends net.minecraft.world.item.Item> T registerItem(T item, String path) {
        return Registry.register(BuiltInRegistries.ITEM, ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, path), item);
    }

    public static void registerModItems() {

    }
}

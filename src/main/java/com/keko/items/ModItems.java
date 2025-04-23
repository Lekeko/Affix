package com.keko.items;

import com.keko.Affix;
import com.keko.items.custom.Accelerator;
import com.keko.items.custom.HeavyMirror;
import com.keko.items.custom.Refractor;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

public class ModItems {

    public static final Item HEAVY_MIRROR = registerItem(new HeavyMirror(new Item.Properties().rarity(Rarity.EPIC).stacksTo(1)), "heavy_mirror");
    public static final Item FABRIC_OF_REALITY = registerItem(new Item(new Item.Properties().rarity(Rarity.RARE)), "fabric_of_reality");
    public static final Item GRAVITY_CORE = registerItem(new Item(new Item.Properties().rarity(Rarity.RARE)), "gravity_core");
    public static final Item ACCELERATOR = registerItem(new Accelerator(new Item.Properties().rarity(Rarity.EPIC).stacksTo(1)), "accelerator");
    public static final Item REFRACTOR = registerItem(new Refractor(new Item.Properties().rarity(Rarity.EPIC).stacksTo(1)), "refractor");


    public static <T extends net.minecraft.world.item.Item> T registerItem(T item, String path) {
        return Registry.register(BuiltInRegistries.ITEM, ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, path), item);
    }

    public static void registerModItems() {

    }
}

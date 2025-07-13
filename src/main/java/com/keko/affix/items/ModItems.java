package com.keko.affix.items;

import com.keko.affix.Affix;
import com.keko.affix.items.custom.*;
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


    //infernal update items
    public static final Item INFERNAL_TOME = registerItem(new InfernalTome(new Item.Properties().rarity(Rarity.EPIC).stacksTo(1)), "infernal_tome");
    public static final Item DEATH_SIMULACRUM = registerItem(new DeathSimulacrum(new Item.Properties().rarity(Rarity.EPIC).stacksTo(1)), "death_simulacrum");
    public static final Item QUASAR_CALLER = registerItem(new QuasarCaller(new Item.Properties().rarity(Rarity.EPIC).stacksTo(1)), "quasar_caller");

    //blud
    public static final Item BRUSH = registerItem(new BrushItemModed(new Item.Properties().rarity(Rarity.EPIC).stacksTo(1)), "brush");


    public static <T extends net.minecraft.world.item.Item> T registerItem(T item, String path) {
        return Registry.register(BuiltInRegistries.ITEM, ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, path), item);
    }

    public static void registerModItems() {

    }
}

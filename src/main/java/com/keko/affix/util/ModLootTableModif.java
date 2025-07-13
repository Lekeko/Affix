package com.keko.affix.util;

import com.keko.affix.items.ModItems;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

public class ModLootTableModif {
    private static final ResourceLocation END_TREASURE_ID = ResourceLocation.fromNamespaceAndPath("minecraft", "chests/end_city_treasure");
    private static final ResourceLocation BASTION_TREASURE_ID = ResourceLocation.fromNamespaceAndPath("minecraft", "chests/bastion_treasure");

    public static void modifu(){
        LootTableEvents.MODIFY.register((key, tableBuilder, source, registries) -> {

            if (END_TREASURE_ID.equals(key.location())){
                LootPool.Builder poolBuilder = LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1))
                        .when(LootItemRandomChanceCondition.randomChance(0.30f))
                        .add(LootItem.lootTableItem(ModItems.HEAVY_MIRROR))
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 1.0f)).build());

                tableBuilder.pool(poolBuilder.build());
            }

            if (BASTION_TREASURE_ID.equals(key.location())){
                LootPool.Builder poolBuilder = LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1))
                        .when(LootItemRandomChanceCondition.randomChance(0.30f))
                        .add(LootItem.lootTableItem(ModItems.QUASAR_CALLER))
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 1.0f)).build());

                tableBuilder.pool(poolBuilder.build());
            }


        });
    }
}

package com.keko.affix.items;

import com.keko.affix.Affix;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ModGroupItem {
    public static final CreativeModeTab AFFIX_GROUP = FabricItemGroup.builder()
            .icon(() -> new ItemStack(ModItems.ACCELERATOR))
            .title(Component.translatable("Affix"))
            .displayItems((context, entries) -> {
                entries.accept(ModItems.HEAVY_MIRROR);
                entries.accept(ModItems.ACCELERATOR);
                entries.accept(ModItems.REFRACTOR);
                entries.accept(ModItems.FABRIC_OF_REALITY);
                entries.accept(ModItems.GRAVITY_CORE);
                entries.accept(ModItems.INFERNAL_TOME);
                entries.accept(ModItems.QUASAR_CALLER);
                //entries.accept(ModItems.DEATH_SIMULACRUM);
            })
            .build();

    public static void register() {
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, "affix_group"), AFFIX_GROUP);

    }
}

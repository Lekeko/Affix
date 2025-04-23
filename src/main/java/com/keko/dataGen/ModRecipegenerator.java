package com.keko.dataGen;

import com.keko.items.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;

import java.util.concurrent.CompletableFuture;

public class ModRecipegenerator extends FabricRecipeProvider {
    public ModRecipegenerator(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void buildRecipes(RecipeOutput recipeOutput) {
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.REFRACTOR)
                .pattern("121")
                .pattern("232")
                .pattern("121")
                .define('1', ModItems.FABRIC_OF_REALITY)
                .define('2', ModItems.GRAVITY_CORE)
                .define('3', Items.SOUL_LANTERN)
                .group("multi_bench")
                .unlockedBy(FabricRecipeProvider.getHasName(Items.CRAFTING_TABLE), FabricRecipeProvider.has(Items.CRAFTING_TABLE))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.ACCELERATOR)
                .pattern("121")
                .pattern("232")
                .pattern("121")
                .define('1', ModItems.FABRIC_OF_REALITY)
                .define('2', ModItems.GRAVITY_CORE)
                .define('3', Items.AMETHYST_SHARD)
                .group("multi_bench")
                .unlockedBy(FabricRecipeProvider.getHasName(Items.CRAFTING_TABLE), FabricRecipeProvider.has(Items.CRAFTING_TABLE))
                .save(recipeOutput);
    }
}

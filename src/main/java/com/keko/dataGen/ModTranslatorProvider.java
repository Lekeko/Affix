package com.keko.dataGen;

import com.keko.affixLogics.KeyBinds;
import com.keko.effects.ModStatusEffects;
import com.keko.entity.ModEntities;
import com.keko.items.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.core.HolderLookup;

import java.util.concurrent.CompletableFuture;

public class ModTranslatorProvider extends FabricLanguageProvider {
    public ModTranslatorProvider(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generateTranslations(HolderLookup.Provider provider, TranslationBuilder translationBuilder) {
        translationBuilder.add(ModItems.HEAVY_MIRROR, "Heavy Mirror");
        translationBuilder.add(ModItems.GRAVITY_CORE, "Gravity Core");
        translationBuilder.add(ModItems.FABRIC_OF_REALITY, "Fabric of Reality");
        translationBuilder.add(ModItems.ACCELERATOR, "Accelerator");
        translationBuilder.add(ModItems.REFRACTOR, "Refractor");
        translationBuilder.add(ModEntities.FABRIC_POCKET_ENTITY_TYPE, "|Fabric Pocket|");
        translationBuilder.add(KeyBinds.dodgeKeyBind.getName(), "Dodge/Dash");
        translationBuilder.add(KeyBinds.dodgeKeyBind.getCategory(), "Affix Controls");
        translationBuilder.add("effect.affix.accelerated", "Accelerated");
        translationBuilder.add("effect.affix.unstable", "behind you");

    }
}

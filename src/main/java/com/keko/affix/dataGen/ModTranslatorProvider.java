package com.keko.affix.dataGen;

import com.keko.affix.affixLogics.KeyBinds;
import com.keko.affix.entity.ModEntities;
import com.keko.affix.items.ModItems;
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
        translationBuilder.add(ModItems.INFERNAL_TOME, "Infernal Tome");
        translationBuilder.add(ModItems.QUASAR_CALLER, "Quasar Caller");
        translationBuilder.add(ModItems.DEATH_SIMULACRUM, "Death Simulacrum");
        translationBuilder.add(ModEntities.FABRIC_POCKET_ENTITY_TYPE, "|Fabric Pocket|");
        translationBuilder.add(KeyBinds.dodgeKeyBind.getName(), "Dodge/Dash");
        translationBuilder.add(KeyBinds.dodgeKeyBind.getCategory(), "Affix Controls");
        translationBuilder.add(KeyBinds.rocketScanKey.getName(), "Rocket scan");
        translationBuilder.add(KeyBinds.rocketLaunchKey.getName(), "Rocket launch");
        translationBuilder.add(KeyBinds.jamKeyBind.getName(), "Open jamming screen");
        translationBuilder.add("effect.affix.accelerated", "Accelerated");
        translationBuilder.add("effect.affix.unstable", "behind you");
        translationBuilder.add("effect.affix.otherwordly", "It's jamming time!");
        translationBuilder.add("affix.midnightconfig.title", "Affix Configs");
        translationBuilder.add("death.attack.flamed", "%1$s got their soul kindled!");
        translationBuilder.add("death.attack.scorched", "%1$s got their skin scorched!");

    }
}

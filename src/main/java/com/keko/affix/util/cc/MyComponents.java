package com.keko.affix.util.cc;

import com.keko.affix.Affix;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentInitializer;

public final class MyComponents implements EntityComponentInitializer {
    public static final ComponentKey<ScoreComponent> SCORE =
            ComponentRegistry.getOrCreate(ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, "score"), ScoreComponent.class);


    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry entityComponentFactoryRegistry) {
        entityComponentFactoryRegistry.registerFor(Player.class ,SCORE, ActualScoreComponent::new);
    }
}

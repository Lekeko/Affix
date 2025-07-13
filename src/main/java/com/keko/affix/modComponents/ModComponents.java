package com.keko.affix.modComponents;

import com.keko.affix.Affix;
import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;

import java.util.function.UnaryOperator;

public class ModComponents {

    public static final DataComponentType<Float> MIRROR_POSITION_TRANSITION = register("MIRROR_POSITION_TRANSITION_ID".toLowerCase(), a -> a.persistent(Codec.FLOAT));

    public static void registerDataComponents() {

    }

    private static <T>DataComponentType<T> register(String name, UnaryOperator<DataComponentType.Builder<T>> builderUnaryOperator){
        return Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, name),
                builderUnaryOperator.apply(DataComponentType.builder()).build());
    }
}

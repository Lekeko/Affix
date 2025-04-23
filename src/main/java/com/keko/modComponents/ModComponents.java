package com.keko.modComponents;

import com.keko.Affix;
import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;

import java.util.function.UnaryOperator;

public class ModComponents {

    public static final DataComponentType<Float> MIRROR_POSITION_TRANSITION = register("MIRROR_POSITION_TRANSITION_ID".toLowerCase(), a -> a.persistent(Codec.FLOAT));
    public static final DataComponentType<Integer> GENERALIZED_COUNTER = register("GENERALIZED_COUNTER_ID".toLowerCase(), a -> a.persistent(Codec.INT));
    public static final DataComponentType<Integer> ANOMALY_ID = register("ANOMALY_ID_ID".toLowerCase(), a -> a.persistent(Codec.INT)); //heheheheheheh

    public static void registerDataComponents() {

    }

    private static <T>DataComponentType<T> register(String name, UnaryOperator<DataComponentType.Builder<T>> builderUnaryOperator){
        return Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, name),
                builderUnaryOperator.apply(DataComponentType.builder()).build());
    }
}

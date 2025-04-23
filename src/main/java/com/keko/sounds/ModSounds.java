package com.keko.sounds;

import com.keko.Affix;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

public class ModSounds {
    public static final SoundEvent WARP_BREAK = register("warp_break");
    public static final SoundEvent REFRACTOR_PHASE = register("refractor_phase");
    public static final SoundEvent ACCELERATOR = register("accelerator");
    public static final SoundEvent DASH = register("dash");
    public static final SoundEvent WHISPER =register("whisper") ;

    private static SoundEvent register(String path) {
        ResourceLocation identifier = ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, path);
        return Registry.register(BuiltInRegistries.SOUND_EVENT, identifier, SoundEvent.createVariableRangeEvent(identifier));

    }

    public static void registerSounds() {

    }
}

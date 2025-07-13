package com.keko.affix.sounds;

import com.keko.affix.Affix;
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
    public static final SoundEvent SPELL =register("spell") ;
    public static final SoundEvent INFERNAL_ARROW_HIT =register("infernal_arrow_hit") ;
    public static final SoundEvent DRAGON_SUMMON =register("dragon_summon") ;
    public static final SoundEvent DRAGON_BLAST =register("dragon_blast") ;
    public static final SoundEvent SPEAR =register("spear") ;

    private static SoundEvent register(String path) {
        ResourceLocation identifier = ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, path);
        return Registry.register(BuiltInRegistries.SOUND_EVENT, identifier, SoundEvent.createVariableRangeEvent(identifier));

    }

    public static void registerSounds() {

    }
}

package com.keko.affix.stylePointsManager.soundSystem;

import com.keko.affix.AffixClient;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SoundAssets {

    private static Map<String, Sound> sounds = new HashMap<>();

    public static Sound getSound(String soundFile){
        File file = new File(soundFile);

        if (sounds.containsKey(file.getAbsolutePath()))
            return sounds.get(file.getAbsolutePath());
        else {
            assert false: "Sound file not added";
        }
        return null;
    }

    public static Map<String, Sound> getSounds() {
        return sounds;
    }

    public static Sound addSound(String soundFile, boolean loops){
        File file = new File(soundFile);

        if (sounds.containsKey(file.getAbsolutePath()))
            return sounds.get(file.getAbsolutePath());
        else {
            Sound sound = new Sound(file.getAbsolutePath(), loops);
            sounds.put(file.getAbsolutePath(), sound);
            return sound;
        }
    }

    public static Collection<Sound> getAllSounds(){
        return sounds.values();
    }

    public static void clear() {
        sounds.clear();
    }

    public static void refreshOrInnitSounds() {
        if (AffixClient.getJamTracks().isEmpty()) return;

        clear();

        for (File jams :AffixClient.getJamTracks()){
            addSound(jams.getAbsolutePath(), false);
        }
    }
}

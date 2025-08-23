package com.keko.affix.stylePointsManager;

import com.keko.affix.AffixClient;
import com.keko.affix.stylePointsManager.soundSystem.Sound;
import com.keko.affix.stylePointsManager.soundSystem.SoundAssets;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.commons.io.FileUtils;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReadFromFolder {

    public static List<File> read() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        File path = FabricLoader.getInstance().getGameDir().toFile();

        File musicDir = new File(path, "affixMusic");

        if (!musicDir.exists()){
            boolean ok = musicDir.mkdirs();
            if (ok){
                System.out.println("Jam folder created!");
            }else {
                System.err.println("Error : no jam folder created cuz of error :C");
                return new ArrayList<>();
            }
        }else {
            System.out.println("Jam folder located!");
        }

        List<File> jamTracks = (List<File>) FileUtils.listFiles(musicDir, new String[]{"ogg"}, true);

        return jamTracks;
    }
}

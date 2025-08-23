package com.keko.affix.midLib;

import com.keko.affix.AffixClient;
import com.keko.affix.stylePointsManager.ReadFromFolder;
import eu.midnightdust.lib.config.MidnightConfig;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AffixConfigs extends MidnightConfig{
    public static final String MAIN = "main";

    @Entry(category = MAIN, width = 7, min = 7, isColor = true, name = "Shader Accent Color") public static String shaderAccentColor = "#ffffff";
    @Entry(category = MAIN, width = 7, min = 7, isColor = true, name = "Rage (Glyphs) Accent Color") public static String rageAccentColor = "#ff0000";
    @Entry(category = MAIN, width = 7, min = 1, name = "Accelerated shader strength") public static float acceleratedEffectStrength = 900f;


    @Entry(category = MAIN, width = 7, min = 1, name = "Jamming cooldown (seconds)") public static int jamCooldown = 120;
    @Entry(category = MAIN, width = 7, min = 1, name = "Number of cubes") public static int cubeCount = 100;
    @Entry(category = MAIN, width = 7, min = 7, isColor = true, name = "Jamming Shader Primary Color") public static String jamPrimaryColor = "#cc00cc";
    @Entry(category = MAIN, width = 7, min = 7, isColor = true, name = "Jamming Shader Secondary Color") public static String jamSecondaryColor = "#330033";
}
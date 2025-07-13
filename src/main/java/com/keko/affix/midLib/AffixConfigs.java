package com.keko.affix.midLib;

import eu.midnightdust.lib.config.MidnightConfig;

public class AffixConfigs extends MidnightConfig{
    public static final String MAIN = "main";

    @Entry(category = MAIN, width = 7, min = 7, isColor = true, name = "Shader Accent Color") public static String shaderAccentColor = "#ffffff";
    @Entry(category = MAIN, width = 7, min = 7, isColor = true, name = "Rage (Glyphs) Accent Color") public static String rageAccentColor = "#ff0000";
    @Entry(category = MAIN, width = 7, min = 1, name = "Accelerated shader strength") public static float acceleratedEffectStrength = 900f;

}
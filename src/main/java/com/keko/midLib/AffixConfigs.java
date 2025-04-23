package com.keko.midLib;

import eu.midnightdust.lib.config.MidnightConfig;

public class AffixConfigs extends MidnightConfig{
    public static final String NUMBERS = "numbers";
    public static final String TEXT = "text";
    public static final String SLIDERS = "sliders";


    @Entry(category = SLIDERS, name = "Shader", isSlider = true, min = 0, max = 100f, precision = 100) public static float shader = 0;


}

package com.keko.affix.stylePointsManager.JamScreen;

public class JamScreenHandler {
    private static boolean screenOpen = false;

    public static void setScreenOpen(boolean screenOpen1) {
        screenOpen = screenOpen1;
    }

    public static boolean isScreenOpen() {
        return screenOpen;
    }
}

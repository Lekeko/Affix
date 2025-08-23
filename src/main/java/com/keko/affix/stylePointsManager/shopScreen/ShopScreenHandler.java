package com.keko.affix.stylePointsManager.shopScreen;

public class ShopScreenHandler {
    private static boolean screenOpen = false;

    public static void setScreenOpen(boolean screenOpen1) {
        screenOpen = screenOpen1;
    }

    public static boolean isScreenOpen() {
        return screenOpen;
    }
}

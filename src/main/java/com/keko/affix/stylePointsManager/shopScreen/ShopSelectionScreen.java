package com.keko.affix.stylePointsManager.shopScreen;

import com.keko.affix.Affix;
import com.keko.affix.AffixClient;
import com.keko.affix.midLib.AffixConfigs;
import com.keko.affix.packet.AwardForJammingC2S;
import com.keko.affix.packet.ConfirmPurchaseC2S;
import com.keko.affix.packet.JamRequestPacketC2S;
import com.keko.affix.stylePointsManager.JamScreen.JamScreenHandler;
import com.keko.affix.stylePointsManager.JamScreen.JamSelectionScreen;
import com.keko.affix.stylePointsManager.ReadFromFolder;
import com.keko.affix.stylePointsManager.pointsSystem.StyleSystemManager;
import com.keko.affix.stylePointsManager.soundSystem.Sound;
import com.keko.affix.stylePointsManager.soundSystem.SoundAssets;
import com.keko.affix.util.cc.MyComponents;
import com.keko.affix.util.cc.ScoreComponent;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.lwjgl.glfw.GLFW;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.awt.*;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.*;
import java.util.List;

public class ShopSelectionScreen extends Screen {
    private float timerSinceOpen = 0;

    private Button addEnchantedGoldenApple;
    private Button subEnchantedGoldenApple;

    private Button addNetheriteScrap;
    private Button subNetheriteScrap;

    private Button addDiamonds;
    private Button subDiamonds;

    private Button addEmeralds;
    private Button subEmeralds;

    private Button addTrialKey;
    private Button subTrialKey;

    private Button purchase;

    private final int ENCHANTED_GOLDEN_APPLE_PRICE = 35000;
    private final int NETHERITE_SCRAP_PRICE = 25000;
    private final int DIAMOND_PRICE = 7000;
    private final int EMERALD_PRICE = 5000;
    private final int TRIAL_KEY_PRICE = 15000;

    int[] shoppingCart = new int[]{0,0,0,0,0};

    private Sound sound;

    int score = 0;
    int scoreWithprice = 0;

    public ShopSelectionScreen(Component component) {
        super(component);
    }


    @Override
    public boolean keyPressed(int i, int j, int k) {
        if (i == GLFW.GLFW_KEY_ESCAPE)
            Minecraft.getInstance().setScreen(new JamSelectionScreen(Component.literal("Jammin time")));
        return super.keyPressed(i, j, k);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int i, int j, float f) {
        timerSinceOpen += Minecraft.getInstance().getTimer().getGameTimeDeltaTicks() / 15;
        super.render(guiGraphics, i, j, f);
        RenderSystem.setShaderColor(1,1,1,0.2f);
        guiGraphics.fill(0,0,width,height, new Color(92, 36, 130, 255).getRGB());
        RenderSystem.setShaderColor(1,1,1,1);
        renderFrame(guiGraphics);
        renderItems(guiGraphics);
        renderText(guiGraphics);

    }

    private void renderItems(GuiGraphics guiGraphics) {

        int size = 32;
        int spacing = 40;


        for (int i = -2; i <= 2; i++) {
            guiGraphics.blit(
                    ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, "textures/gui/jam_textures/shop_slot.png"),
                    width / 2,
                    height / 2 - size / 5 - i * spacing,
                    0,
                    0,
                    size,
                    size,
                    size,
                    size);
        }
        PoseStack stack = guiGraphics.pose();
        stack.pushPose();
        float itemSize = 1.25f;

        spacing /= (itemSize);
        stack.scale(itemSize,itemSize,itemSize);
        guiGraphics.renderItem(Items.ENCHANTED_GOLDEN_APPLE.getDefaultInstance(), (int) (width/(2 * itemSize) + 5), (int) (height/(2 * itemSize)) - 2 * spacing);
        guiGraphics.renderItem(Items.NETHERITE_SCRAP.getDefaultInstance(), (int) (width/(2 * itemSize) + 5), (int) (height/(2 * itemSize)) - spacing);
        guiGraphics.renderItem(Items.DIAMOND.getDefaultInstance(), (int) (width/(2 * itemSize) + 5), (int) (height/(2 * itemSize)) );
        guiGraphics.renderItem(Items.EMERALD.getDefaultInstance(), (int) (width/(2 * itemSize) + 5), (int) (height/(2 * itemSize)) + spacing);
        guiGraphics.renderItem(Items.OMINOUS_TRIAL_KEY.getDefaultInstance(), (int) (width/(2 * itemSize) + 5), (int) (height/(2 * itemSize)) + 2 * spacing);

        stack.popPose();
    }

    private void renderText(GuiGraphics guiGraphics) {
        int spacingW = 11;
        int spacingH = 6;
        int spacing = 40;
        int width = guiGraphics.guiWidth();
        int height = guiGraphics.guiHeight();

        PoseStack stack = guiGraphics.pose();
        guiGraphics.drawCenteredString(Minecraft.getInstance().font, String.valueOf(shoppingCart[0]),
                (int) (width/1.5 + spacingW), height/2 + spacingH - spacing - spacing,
                Color.WHITE.getRGB());
        guiGraphics.drawCenteredString(Minecraft.getInstance().font, String.valueOf(shoppingCart[1]),
                (int) (width/1.5 + spacingW), height/2 + spacingH - spacing,
                Color.WHITE.getRGB());
        guiGraphics.drawCenteredString(Minecraft.getInstance().font, String.valueOf(shoppingCart[2]),
                (int) (width/1.5 + spacingW), height/2 + spacingH,
                Color.WHITE.getRGB());
        guiGraphics.drawCenteredString(Minecraft.getInstance().font, String.valueOf(shoppingCart[3]),
                (int) (width/1.5 + spacingW), height/2 + spacingH + spacing,
                Color.WHITE.getRGB());
        guiGraphics.drawCenteredString(Minecraft.getInstance().font, String.valueOf(shoppingCart[4]),
                (int) (width/1.5 + spacingW), height/2 + spacingH + spacing + spacing,
                Color.WHITE.getRGB());

        stack.pushPose();

        stack.scale(3,3,3);
        stack.translate(guiGraphics.guiWidth() / 2f / 5f,
                        guiGraphics.guiHeight() / 2f / 3f -2,0);

        Color primaryColor;
        try {
            primaryColor = Color.decode(AffixConfigs.jamSecondaryColor);
        } catch (Exception e) {
            Affix.LOGGER.warn("Please provide correct color input :3 (or imma send ur pc in space lol)");
            primaryColor = Color.MAGENTA;
        }

        Color lightenedColor = new Color(
                Math.clamp(primaryColor.getRed() + 80, 0, 255),
                Math.clamp(primaryColor.getGreen() + 80, 0, 255),
                Math.clamp(primaryColor.getBlue() + 80, 0, 255)
        );



        guiGraphics.drawCenteredString(Minecraft.getInstance().font, NumberFormat.getNumberInstance(Locale.US).format(scoreWithprice),
                0, 0,
                score == scoreWithprice ? Color.WHITE.getRGB() : lightenedColor.getRGB());

        stack.popPose();
    }


    @Override
    public void tick() {
        super.tick();
        sound = AffixClient.getJammer().getSound();
        scoreWithprice = score;
        scoreWithprice -= shoppingCart[0] * ENCHANTED_GOLDEN_APPLE_PRICE;
        scoreWithprice -= shoppingCart[1] * NETHERITE_SCRAP_PRICE;
        scoreWithprice -= shoppingCart[2] * DIAMOND_PRICE;
        scoreWithprice -= shoppingCart[3] * EMERALD_PRICE;
        scoreWithprice -= shoppingCart[4] * TRIAL_KEY_PRICE;

        addEnchantedGoldenApple.active = ENCHANTED_GOLDEN_APPLE_PRICE <= scoreWithprice;
        addNetheriteScrap.active = NETHERITE_SCRAP_PRICE <= scoreWithprice;
        addDiamonds.active = DIAMOND_PRICE <= scoreWithprice;
        addEmeralds.active = EMERALD_PRICE <= scoreWithprice;
        addTrialKey.active = TRIAL_KEY_PRICE <= scoreWithprice;

        purchase.active = scoreWithprice < score;

        subEnchantedGoldenApple.active = shoppingCart[0] > 0;
        subNetheriteScrap.active = shoppingCart[1] > 0;
        subDiamonds.active = shoppingCart[2] > 0;
        subEmeralds.active = shoppingCart[3] > 0;
        subTrialKey.active = shoppingCart[4] > 0;
    }

    @Override
    protected void init() {
        super.init();
        sound = AffixClient.getJammer().getSound();
        ScoreComponent component = MyComponents.SCORE.get(Minecraft.getInstance().player);
        score = component.getScore();
        renderButtons();
        scoreWithprice = score;
        scoreWithprice -= shoppingCart[0] * ENCHANTED_GOLDEN_APPLE_PRICE;
        scoreWithprice -= shoppingCart[1] * NETHERITE_SCRAP_PRICE;
        scoreWithprice -= shoppingCart[2] * DIAMOND_PRICE;
        scoreWithprice -= shoppingCart[3] * EMERALD_PRICE;
        scoreWithprice -= shoppingCart[4] * TRIAL_KEY_PRICE;






    }

    @Override
    public void onClose() {
        super.onClose();
    }

    private void renderButtons() {

        int spacing = 50;
        int spacingHeight = 40;
        int addSubButtonSize = 20;

        /////////////////////////////////

        addEnchantedGoldenApple = Button.builder(Component.literal("+1"), button -> {
                    shoppingCart[0]++;
                })
                .bounds((int) (width/1.5 + spacing), height/2 - spacingHeight * 2, addSubButtonSize, addSubButtonSize)
                .tooltip(Tooltip.create(Component.literal("-"+ NumberFormat.getNumberInstance(Locale.US).format(ENCHANTED_GOLDEN_APPLE_PRICE))))
                .build();

        subEnchantedGoldenApple = Button.builder(Component.literal("-1"), button -> {
                    shoppingCart[0]--;

                })
                .bounds((int) (width/1.5 - spacing), height/2 - spacingHeight * 2, addSubButtonSize, addSubButtonSize)
                .tooltip(Tooltip.create(Component.literal("+"+NumberFormat.getNumberInstance(Locale.US).format(ENCHANTED_GOLDEN_APPLE_PRICE))))

                .build();

        /////////////////////////////////
        addNetheriteScrap = Button.builder(Component.literal("+1"), button -> {
                    shoppingCart[1]++;

                })
                .bounds((int) (width/1.5 + spacing), height/2 - spacingHeight, addSubButtonSize, addSubButtonSize)
                .tooltip(Tooltip.create(Component.literal("-"+NumberFormat.getNumberInstance(Locale.US).format(NETHERITE_SCRAP_PRICE))))

                .build();

        subNetheriteScrap = Button.builder(Component.literal("-1"), button -> {
                    shoppingCart[1]--;
                })
                .bounds((int) (width/1.5 - spacing), height/2 - spacingHeight, addSubButtonSize, addSubButtonSize)
                .tooltip(Tooltip.create(Component.literal("+"+NumberFormat.getNumberInstance(Locale.US).format(NETHERITE_SCRAP_PRICE))))

                .build();


        /////////////////////////////////
        addDiamonds = Button.builder(Component.literal("+1"), button -> {
                    shoppingCart[2]++;
                })
                .bounds((int) (width/1.5 + spacing), height/2, addSubButtonSize, addSubButtonSize)
                .tooltip(Tooltip.create(Component.literal("-"+NumberFormat.getNumberInstance(Locale.US).format(DIAMOND_PRICE))))

                .build();

        subDiamonds = Button.builder(Component.literal("-1"), button -> {
                    shoppingCart[2]--;
                })
                .bounds((int) (width/1.5 - spacing), height/2, addSubButtonSize, addSubButtonSize)
                .tooltip(Tooltip.create(Component.literal("+"+NumberFormat.getNumberInstance(Locale.US).format(DIAMOND_PRICE))))

                .build();


        /////////////////////////////////
        addEmeralds = Button.builder(Component.literal("+1"), button -> {
                    shoppingCart[3]++;
                })
                .bounds((int) (width/1.5 + spacing), height/2 + spacingHeight, addSubButtonSize, addSubButtonSize)
                .tooltip(Tooltip.create(Component.literal("-"+NumberFormat.getNumberInstance(Locale.US).format(EMERALD_PRICE))))

                .build();

        subEmeralds = Button.builder(Component.literal("-1"), button -> {
                    shoppingCart[3]--;
                })
                .bounds((int) (width/1.5 - spacing), height/2 + spacingHeight, addSubButtonSize, addSubButtonSize)
                .tooltip(Tooltip.create(Component.literal("+"+NumberFormat.getNumberInstance(Locale.US).format(EMERALD_PRICE))))

                .build();

        /////////////////////////////////
        addTrialKey = Button.builder(Component.literal("+1"), button -> {
                    shoppingCart[4]++;
                })
                .bounds((int) (width/1.5 + spacing), height/2 + spacingHeight * 2, addSubButtonSize, addSubButtonSize)
                .tooltip(Tooltip.create(Component.literal("-"+NumberFormat.getNumberInstance(Locale.US).format(TRIAL_KEY_PRICE))))

                .build();

        subTrialKey = Button.builder(Component.literal("-1"), button -> {
                    shoppingCart[4]--;
                })
                .bounds((int) (width/1.5 - spacing), height/2 + spacingHeight * 2, addSubButtonSize, addSubButtonSize)
                .tooltip(Tooltip.create(Component.literal("+"+NumberFormat.getNumberInstance(Locale.US).format(TRIAL_KEY_PRICE))))

                .build();

        purchase = Button.builder(Component.literal("Buy"), button -> {
                    ClientPlayNetworking.send(new AwardForJammingC2S(scoreWithprice - score, 0));
                    ClientPlayNetworking.send(new ConfirmPurchaseC2S(shoppingCart[0], shoppingCart[1], shoppingCart[2], shoppingCart[3], shoppingCart[4]));
                    shoppingCart = new int[]{0,0,0,0,0};
                    score = scoreWithprice;

        })
                .bounds((int) (width/1.5f - addSubButtonSize * 1.5f), (int) (height/1.2f), addSubButtonSize * 4, addSubButtonSize)
                .build();

        addRenderableWidget(addDiamonds);
        addRenderableWidget(addTrialKey);
        addRenderableWidget(addEmeralds);
        addRenderableWidget(addNetheriteScrap);
        addRenderableWidget(addEnchantedGoldenApple);
        addRenderableWidget(subDiamonds);
        addRenderableWidget(subEmeralds);
        addRenderableWidget(subNetheriteScrap);
        addRenderableWidget(subEnchantedGoldenApple);
        addRenderableWidget(subTrialKey);
        addRenderableWidget(purchase);


    }

    private void renderFrame(GuiGraphics guiGraphics) {
        int width = guiGraphics.guiWidth();
        int height = guiGraphics.guiHeight();
        int size = 256;

        if (sound != null && sound.isPlaying()){
            size += sound.getAmplitude() * 40;
        }

        guiGraphics.blit(
                ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, "textures/gui/jam_textures/frame_1.png"),
                0,
                0,
                0,
                0,
                size,
                size,
                size,
                size);
        guiGraphics.blit(
                ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, "textures/gui/jam_textures/frame_2.png"),
                width-size,
                0,
                0,
                0,
                size,
                size,
                size,
                size);
        guiGraphics.blit(
                ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, "textures/gui/jam_textures/frame_3.png"),
                width-size,
                height-size,
                0,
                0,
                size,
                size,
                size,
                size);
        guiGraphics.blit(
                ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, "textures/gui/jam_textures/frame_4.png"),
                0,
                height-size,
                0,
                0,
                size,
                size,
                size,
                size);

        Random random = new Random(score);
        if (random.nextFloat() > 0.9f){
            guiGraphics.blit(
                    ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, "textures/gui/jam_textures/asshole.png"),
                    width/4,
                    (int) (height/1.5) - 32,
                    0,
                    0,
                    64,
                    64,
                    64,
                    64);
        }
    }
    @Override
    public boolean isPauseScreen() {
        return false;
    }
}

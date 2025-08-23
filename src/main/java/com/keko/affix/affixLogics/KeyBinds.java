package com.keko.affix.affixLogics;

import com.keko.affix.AffixClient;
import com.keko.affix.effects.ModStatusEffects;
import com.keko.affix.packet.PunchRequestPacketC2S;
import com.keko.affix.packet.RocketSpawnRequestPacketC2S;
import com.keko.affix.packet.SendPlayerToDodgeC2S;
import com.keko.affix.sounds.ModSounds;
import com.keko.affix.stylePointsManager.JamScreen.JamScreenHandler;
import com.keko.affix.stylePointsManager.JamScreen.JamSelectionScreen;
import com.keko.affix.stylePointsManager.rockets.RocketScreenManager;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.datafixers.kinds.IdF;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.Input;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.PlayerAdvancements;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class KeyBinds {

    private static int rockets = 0;
    static List<LivingEntity> livingEntities = new ArrayList<>();
    private static int timer1 = 0;
    private static int timer2 = 0;
    private static boolean grounded = false;

    public static KeyMapping dodgeKeyBind = KeyBindingHelper.registerKeyBinding(new KeyMapping(
            "key.affix.dodge",
            InputConstants.Type.MOUSE,
            GLFW.GLFW_MOUSE_BUTTON_4,
            "key.affix.affixmod"
    ));

    public static KeyMapping jamKeyBind = KeyBindingHelper.registerKeyBinding(new KeyMapping(
            "key.affix.jammingtime",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_K,
            "key.affix.affixmod"
    ));

    public static KeyMapping rocketLaunchKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
            "key.affix.rocketLaunchKey",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_V,
            "key.affix.affixmod"
    ));

    public static KeyMapping rocketScanKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
            "key.affix.rocketScanKey",
            InputConstants.Type.MOUSE,
            GLFW.GLFW_MOUSE_BUTTON_5,
            "key.affix.affixmod"
    ));

    public static void registerKeyInputs() {


        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            if (client.player == null) return;
            if (timer1 > 0) timer1-- ;
            if (timer2 > 0) timer2-- ;
            if (client.player.onGround())
                grounded = false;
            if (dodgeKeyBind.consumeClick()){
                Input input = client.player.input;
                float forwardImpulse = input.forwardImpulse;
                float leftImpulse = input.leftImpulse;
                boolean jump = input.jumping;
                assert client.level != null;
                if(client.player.hasEffect(ModStatusEffects.ACCELERATED)){
                    client.player.level().playLocalSound(client.player, ModSounds.DASH, SoundSource.PLAYERS, 1, 0.5f + client.level.random.nextFloat());
                    AffixRenderingHandlers.addGlyph();
                    ClientPlayNetworking.send(new SendPlayerToDodgeC2S(forwardImpulse, leftImpulse, jump));
                }
                if (client.player.hasEffect(ModStatusEffects.OTHERWORDLY)){
                    AffixClient.getStyleSystemManager().dashed(forwardImpulse, leftImpulse, jump);
                }
            }




            if (jamKeyBind.consumeClick() ){

                if ((client.player.connection.getAdvancements().get(ResourceLocation.fromNamespaceAndPath("minecraft","end/kill_dragon"))) != null) {
                    Minecraft.getInstance().setScreen(new JamSelectionScreen(Component.literal("Jammin time")));
                    JamScreenHandler.setScreenOpen(true);
                }else {
                    Minecraft.getInstance().player.sendSystemMessage(Component.literal("Beat the dragon to use this!"));
                }
            }

            if (client.player.hasEffect(ModStatusEffects.OTHERWORDLY)){
                if (rocketScanKey.isDown()) {
                    RocketScreenManager.updateTicks();
                } else {
                    RocketScreenManager.reset();
                }
                if (rocketLaunchKey.consumeClick() && AffixClient.getStyleSystemManager().getRockets() >= 10) {
                    rockets = 10;
                    AffixClient.getStyleSystemManager().subRockets(10);
                    livingEntities = List.copyOf(RocketScreenManager.getHitmarkPositions());
                }
            }

            if (rockets > 0 && !livingEntities.isEmpty()) {
                int entity = 10 - rockets;
                while (entity >= livingEntities.size())
                     entity -= livingEntities.size();

                ClientPlayNetworking.send(new RocketSpawnRequestPacketC2S(livingEntities.get(entity).getId()));
                rockets--;
            }

            if (client.player.getMainHandItem().is(Items.AIR) && client.player.getOffhandItem().is(Items.AIR) && client.player.hasEffect(ModStatusEffects.OTHERWORDLY)){

                boolean leftClickDown = client.options.keyAttack.consumeClick();
                boolean rightClickDown = client.options.keyUse.consumeClick();

                if (leftClickDown && rightClickDown) {
                    double angle = client.player.getViewVector(1.0f).dot(new Vec3(0,-1,0));

                    if (!grounded){
                        ClientPlayNetworking.send(new PunchRequestPacketC2S(false, AffixConstantsPunch.DOUBLE_BLAST_PUNCH));
                        grounded = true;
                    }else {
                        if (angle > 0) {
                            ClientPlayNetworking.send(new PunchRequestPacketC2S(false, AffixConstantsPunch.SLAM_GROUND_PUNCH));
                            grounded = false;
                        }

                    }
                }

                if (leftClickDown && timer1 == 0){
                    ClientPlayNetworking.send(new PunchRequestPacketC2S(true, AffixConstantsPunch.NORMAL_PUNCH));
                    timer1 = 4;
                }
                if (rightClickDown && timer2 == 0) {
                    ClientPlayNetworking.send(new PunchRequestPacketC2S(false, AffixConstantsPunch.NORMAL_PUNCH));
                    timer2 = 4;
                }


            }

        });




    }

}

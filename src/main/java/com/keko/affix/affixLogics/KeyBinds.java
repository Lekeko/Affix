package com.keko.affix.affixLogics;

import com.keko.affix.AffixClient;
import com.keko.affix.effects.ModStatusEffects;
import com.keko.affix.packet.SendPlayerToDodgeC2S;
import com.keko.affix.sounds.ModSounds;
import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.player.Input;
import net.minecraft.sounds.SoundSource;
import org.lwjgl.glfw.GLFW;

public class KeyBinds {

    public static KeyMapping dodgeKeyBind = KeyBindingHelper.registerKeyBinding(new KeyMapping(
            "key.affix.dodge",
            InputConstants.Type.MOUSE,
            GLFW.GLFW_MOUSE_BUTTON_5,
            "key.affix.affixmod"
    ));

    public static void registerKeyInputs() {


        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (dodgeKeyBind.consumeClick() && client.player != null){
                if(client.player.hasEffect(ModStatusEffects.ACCELERATED)){
                    Input input = client.player.input;
                    float forwardImpulse = input.forwardImpulse;
                    float leftImpulse = input.leftImpulse;
                    boolean jump = input.jumping;
                    assert client.level != null;
                    client.player.level().playLocalSound(client.player, ModSounds.DASH, SoundSource.PLAYERS, 1, 0.5f + client.level.random.nextFloat());
                    AffixClient.addGlyph();
                    ClientPlayNetworking.send(new SendPlayerToDodgeC2S(forwardImpulse, leftImpulse, jump));
                }
            }
        });




    }

}

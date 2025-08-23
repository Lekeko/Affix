package com.keko.affix.mixin;

import com.keko.affix.effects.ModStatusEffects;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public class YouKnowWhoThisIsForLol { //yellow and purple may ring some bells
    @Unique
    Player player = (Player) (Object) this;

    @Inject(method = "causeFallDamage", at = @At("HEAD"), cancellable = true)
    private void MacelVanitate(float f, float g, DamageSource damageSource, CallbackInfoReturnable<Boolean> cir){
        if (player.hasEffect(ModStatusEffects.OTHERWORDLY))
            cir.cancel();
    }
}

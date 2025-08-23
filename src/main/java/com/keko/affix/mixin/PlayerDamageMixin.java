package com.keko.affix.mixin;

import com.keko.affix.packet.AwardForKillingS2C;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public class PlayerDamageMixin {
    @Unique
    Player player = (Player) (Object) this;

    @Inject(method = "hurt", at = @At("RETURN"))
    private void lowerStyle(DamageSource damageSource, float f, CallbackInfoReturnable<Boolean> cir){
        if (cir.getReturnValue() == true)
            ServerPlayNetworking.send((ServerPlayer) player, new AwardForKillingS2C(0, -0.3f));

    }
}

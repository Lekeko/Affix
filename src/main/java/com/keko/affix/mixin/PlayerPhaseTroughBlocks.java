package com.keko.affix.mixin;

import com.keko.affix.Affix;
import com.keko.affix.AffixClient;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Player.class)
public abstract class PlayerPhaseTroughBlocks extends LivingEntity {
    protected PlayerPhaseTroughBlocks(EntityType<? extends LivingEntity> type, Level world) {
        super(type, world);
    }

    private boolean shouldPhase(Player player) {
        boolean ok = player.isSpectator() || Affix.isPLayer(player);

        if (this.level().isClientSide()) {
            return ok || AffixClient.isPLayerClient(player);
        }

        return ok;
    }

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;isSpectator()Z"))
    private boolean canClipTroughWorld(Player player) {
        return this.shouldPhase(player);
    }

    @Redirect(method = "aiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;isSpectator()Z"))
    private boolean collidesWithEntities(Player player) {
        return this.shouldPhase(player);
    }

    @Redirect(method = "updatePlayerPose", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;isSpectator()Z"))
    private boolean spectatorsDontPose(Player player) {
        return this.shouldPhase(player);
    }
}
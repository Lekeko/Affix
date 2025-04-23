package com.keko.mixin;

import com.keko.Affix;
import com.keko.AffixClient;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Player.class)
public abstract class PlayerPhaseTroughBlocks extends LivingEntity{
    protected PlayerPhaseTroughBlocks(EntityType<? extends LivingEntity> type, Level world)
    {
        super(type, world);
    }

    @Redirect(method = "tick", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/player/Player;isSpectator()Z")
    )
    private boolean canClipTroughWorld(Player player)
    {
        return player.isSpectator() || Affix.isPLayer(player) || AffixClient.isPLayerClient(player);
    }

    @Redirect(method = "aiStep", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/player/Player;isSpectator()Z")
    )
    private boolean collidesWithEntities(Player player)
    {
        return player.isSpectator() || Affix.isPLayer(player) || AffixClient.isPLayerClient(player);
    }

    @Redirect(method = "updatePlayerPose", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/player/Player;isSpectator()Z")
    )
    private boolean spectatorsDontPose(Player player)
    {
        return player.isSpectator() || Affix.isPLayer(player) || AffixClient.isPLayerClient(player);
    }
}

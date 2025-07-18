package com.keko.affix.items.custom;

import com.keko.affix.Affix;
import com.keko.affix.AffixClient;
import com.keko.affix.affixLogics.PlayerPhase;
import com.keko.affix.sounds.ModSounds;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.dimension.LevelStem;

public class Refractor extends Item {
    public Refractor(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext useOnContext) {
        if (!useOnContext.getLevel().isClientSide && useOnContext.getPlayer() != null){
            if (useOnContext.getPlayer().level().dimension().equals(LevelStem.OVERWORLD)){
                if (!useOnContext.getPlayer().isCreative())
                    useOnContext.getPlayer().getCooldowns().addCooldown(this, 20 * 25);
                PlayerPhase playerPhase = new PlayerPhase(useOnContext.getPlayer(), useOnContext.getPlayer().getViewVector(1.0f).add(0,5.2f, 0));
                Affix.addPlayer(playerPhase);
                playerPhase.getPlayer().level().playSound(null, playerPhase.getPlayer().getOnPos(), ModSounds.REFRACTOR_PHASE, SoundSource.PLAYERS, 10F, (float) (0.5 + (playerPhase.getPlayer().level().random.nextFloat() / 2f)));
            }else {
                useOnContext.getPlayer().sendSystemMessage(Component.translatable("This dimension is too unstable!"));
            }

        }else {
            AffixClient.transition = 1;
        }
        return super.useOn(useOnContext);
    }
}

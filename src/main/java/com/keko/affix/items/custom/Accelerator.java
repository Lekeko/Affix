package com.keko.affix.items.custom;

import com.keko.affix.AffixClient;
import com.keko.affix.affixLogics.HandsAnimationSystem;
import com.keko.affix.effects.ModStatusEffects;
import com.keko.affix.sounds.ModSounds;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class Accelerator extends Item {
    public static final int TRANZITION_TIMER = 130;
    public static final float TRANZITIONER = 20;
    public Accelerator(Properties properties) {
        super(properties);
    }


    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        if (level.isClientSide){
            AffixClient.armTranzition = TRANZITION_TIMER;
            AffixClient.acceleratorShaderTranzition = TRANZITIONER;
            HandsAnimationSystem.resetTickerAccelerator();
        }
        else {
            player.getItemInHand(interactionHand).shrink(1);
            player.addEffect(new MobEffectInstance(ModStatusEffects.ACCELERATED, 20 * 60, 0));

            level.playSound(null, player.getOnPos(), ModSounds.ACCELERATOR, SoundSource.PLAYERS, 10F, (float) (0.5 + (level.random.nextFloat() / 2f)));
        }
        return super.use(level, player, interactionHand);
    }
}

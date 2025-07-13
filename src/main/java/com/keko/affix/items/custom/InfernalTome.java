package com.keko.affix.items.custom;

import com.keko.affix.entity.ModEntities;
import com.keko.affix.helpers.Directional;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

public class InfernalTome extends Item {
    private final int CAST_TIME = 10;
    public InfernalTome(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        player.startUsingItem(hand);
        player.addDeltaMovement(new Vec3(0,player.onGround() ?  0.1f : 0,0));
        player.hurtMarked = true;

        if (!level.isClientSide) {
            Vec3 castPosition = Directional.rayCast(level, player, player.getViewVector(1.0f), 50);
            castPosition = new Vec3(
                    Math.floor(castPosition.x) + .5f,
                    Math.floor(castPosition.y) + .5f,
                    Math.floor(castPosition.z) + .5f
            );
        }
        return InteractionResultHolder.consume(player.getItemInHand(hand));
    }


    @Override
    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack itemStack, int i) {
        if (!level.isClientSide){
            livingEntity.addDeltaMovement(new Vec3(livingEntity.getDeltaMovement().x / 12,0.08f,livingEntity.getDeltaMovement().z / 12));
            livingEntity.hurtMarked = true;
        }
        super.onUseTick(level, livingEntity, itemStack, i);

    }

    @Override
    public int getUseDuration(ItemStack itemStack, LivingEntity livingEntity) {
        return 20 * CAST_TIME;
    }
}

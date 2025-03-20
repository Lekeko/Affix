package com.keko.items.custom;

import com.keko.AffixClient;
import com.keko.items.ModItems;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class HeavyMirror extends Item {
    public HeavyMirror(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        if (level.isClientSide){

        }else {

        }
        return super.use(level, player, interactionHand);
    }

    @Override
    public void inventoryTick(ItemStack itemStack, Level level, Entity entity, int i, boolean bl) {
        if (level.isClientSide && entity instanceof Player){
            AffixClient.renderMirror = ((Player) entity).getMainHandItem().is(ModItems.HEAVY_MIRROR) || (((Player) entity).getOffhandItem().is(ModItems.HEAVY_MIRROR));
        }
        super.inventoryTick(itemStack, level, entity, i, bl);
    }
}

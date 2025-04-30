package com.keko.items.custom;

import com.keko.AffixClient;
import com.keko.items.ModItems;
import com.keko.modComponents.ModComponents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

public class HeavyMirror extends Item {

    public static final float LIMIT = 6;

    public HeavyMirror(Properties properties) {
        super(properties);
        this.getDefaultInstance().set(ModComponents.MIRROR_POSITION_TRANSITION, 2.0f);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack itemStack) {
        return UseAnim.SPYGLASS;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        ItemStack stack = player.getItemInHand(interactionHand);


        getUseAnimation(Items.SPYGLASS.getDefaultInstance());
        player.startUsingItem(interactionHand);

        return super.use(level, player, interactionHand);
    }

    @Override
    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack itemStack, int i) {
        if (level.isClientSide){
            float value = itemStack.get(ModComponents.MIRROR_POSITION_TRANSITION);
            itemStack.set(ModComponents.MIRROR_POSITION_TRANSITION, value <= LIMIT ? value + 0.25f : LIMIT);
        }else {
            //TODO idk
        }
        super.onUseTick(level, livingEntity, itemStack, i);
    }

    @Override
    public int getUseDuration(ItemStack itemStack, LivingEntity livingEntity) {
        return 100000;
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int i, boolean bl) {
        initStack(stack, level, entity);
        if (level.isClientSide && entity instanceof Player){
                float value = stack.get(ModComponents.MIRROR_POSITION_TRANSITION);
                AffixClient.renderMirror = ((Player) entity).getMainHandItem().is(ModItems.HEAVY_MIRROR) || (((Player) entity).getOffhandItem().is(ModItems.HEAVY_MIRROR));
                AffixClient.mirrorOffset = stack.get(ModComponents.MIRROR_POSITION_TRANSITION);
                if (value >= 0)
                    stack.set(ModComponents.MIRROR_POSITION_TRANSITION,
                            value / 1.25f < 0.001 ? 0f : value / 1.25f // I hate it
                    );
        }
        super.inventoryTick(stack, level, entity, i, bl);
    }

    private void initStack(ItemStack stack, Level level, Entity entity) {
        if (stack.get(ModComponents.MIRROR_POSITION_TRANSITION) == null)
            stack.set(ModComponents.MIRROR_POSITION_TRANSITION, 2.0f);
    }
}

package com.keko.affix.items.custom;

import com.keko.affix.entity.ModEntities;
import com.keko.affix.entity.infernalArrow.InfernalArrow;
import com.keko.affix.entity.infernalExplosion.InfernalExplosion;
import com.keko.affix.helpers.Directional;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.awt.*;
import java.util.List;

public class QuasarCaller extends Item {
    public QuasarCaller(Properties properties) {
        super(properties);
    }

    public float lerp (float start, float finish, float t){
        return start * (1-t) + finish * t;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, TooltipContext tooltipContext, List<Component> list, TooltipFlag tooltipFlag) {
        list.add(Component.translatable("Recommended to use with high entity render distance!!!").withColor(Color.GRAY.getRGB()));
    }

    @Override
    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack itemStack, int i) {
        super.onUseTick(level, livingEntity, itemStack, i);
        if (level.isClientSide) return;

        if (!(livingEntity instanceof Player)) return;

        int tickPassed = 100 - i;

        Player player = (Player) livingEntity;

        final int DISTANCE_TO_SPAWN = 20;
        final int SPREAD = 0;


        Vec3 playerDirection = player.getViewVector(1.0F);
        Vec3 spawnCoordinates = player.getPosition(1.0f).subtract(playerDirection.multiply(
                DISTANCE_TO_SPAWN,
                0,
                DISTANCE_TO_SPAWN)).multiply(1, 0, 1);
        InfernalArrow infernalArrow = new InfernalArrow(ModEntities.INFERNAL_ARROW, level);
        infernalArrow.setPos(spawnCoordinates.add(new Vec3(
                level.random.nextIntBetweenInclusive(-SPREAD, SPREAD),
                level.random.nextIntBetweenInclusive(-SPREAD, SPREAD) + player.getPosition(1.0f).y + 50,
                level.random.nextIntBetweenInclusive(-SPREAD, SPREAD)

        )));

        float speed = 8.3456f;
        infernalArrow.setSoundEvent(SoundEvents.EMPTY);
        Vec3 playerLookPos = Directional.rayCast(level, player, playerDirection, 200);
        Vec3 hitPos;
        if (tickPassed <= 20) {
            Vec3 directionToGo = getDirectionToGo(tickPassed, playerDirection);
            int stripLength = 70;
            if (tickPassed >= 10) tickPassed-=10;
            float progress = lerp(stripLength, -stripLength, tickPassed / 10f);
            hitPos = new Vec3(
                    playerLookPos.x + progress * directionToGo.x,
                    playerLookPos.y + progress * directionToGo.y,
                    playerLookPos.z + progress * directionToGo.z
            );

        } else {
            hitPos = playerLookPos.add(new Vec3(
                    level.random.nextIntBetweenInclusive(-9,9),
                    level.random.nextIntBetweenInclusive(-9,9),
                    level.random.nextIntBetweenInclusive(-9,9)
            ));
        }

        InfernalExplosion infernalExplosion = new InfernalExplosion(ModEntities.INFERNAL_EXPLOSION, level);
        infernalExplosion.setPos(spawnCoordinates.add(new Vec3(
                level.random.nextIntBetweenInclusive(-SPREAD, SPREAD),
                level.random.nextIntBetweenInclusive(-SPREAD, SPREAD) + player.getPosition(1.0f).y + 50,
                level.random.nextIntBetweenInclusive(-SPREAD, SPREAD))));
        level.addFreshEntity(infernalExplosion);
        infernalArrow.setDeltaMovement(hitPos.subtract(infernalArrow.getPosition(1.0f)).normalize().multiply(speed, speed, speed));
        level.addFreshEntity(infernalArrow);

        player.addDeltaMovement(new Vec3(player.getDeltaMovement().x / 12,0.08f,player.getDeltaMovement().z / 12));
        player.hurtMarked = true;
    }

    private Vec3 getDirectionToGo(int i, Vec3 playerDirection) {
        if (i < 10){
            double x = playerDirection.x * Mth.cos(48) - playerDirection.z * Mth.sin(48);
            double z = playerDirection.x * Mth.sin(48) + playerDirection.z * Mth.cos(48);
            return new Vec3(x, 0, z);
        }else {
            double x = playerDirection.x * Mth.cos(-48) - playerDirection.z * Mth.sin(-48);
            double z = playerDirection.x * Mth.sin(-48) + playerDirection.z * Mth.cos(-48);
            return new Vec3(x, 0, z);
        }
    }

    @Override
    public ItemStack finishUsingItem(ItemStack itemStack, Level level, LivingEntity livingEntity) {

        if (livingEntity instanceof Player){
            Player player = (Player) livingEntity;
            if (!player.isCreative())
                player.getCooldowns().addCooldown(this, 20 * 30);
        }
        return super.finishUsingItem(itemStack, level, livingEntity);
    }

    @Override
    public void releaseUsing(ItemStack itemStack, Level level, LivingEntity livingEntity, int i) {
        if (livingEntity instanceof Player){
            Player player = (Player) livingEntity;
            if (!player.isCreative())
                player.getCooldowns().addCooldown(this, 20 * 20);//intended
        }
        super.releaseUsing(itemStack, level, livingEntity, i);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
        user.startUsingItem(hand);
        return InteractionResultHolder.consume(user.getItemInHand(hand));
    }

    @Override
    public int getUseDuration(ItemStack itemStack, LivingEntity livingEntity) {
        return 5 * 20;
    }
}

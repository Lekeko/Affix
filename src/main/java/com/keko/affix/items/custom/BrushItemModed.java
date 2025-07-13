package com.keko.affix.items.custom;

import com.keko.affix.Affix;
import com.keko.affix.entity.ModEntities;
import com.keko.affix.entity.infernalArrow.InfernalArrow;
import com.keko.affix.helpers.Directional;
import foundry.veil.api.client.render.VeilRenderSystem;
import foundry.veil.api.quasar.particle.ParticleEmitter;
import foundry.veil.api.quasar.particle.ParticleSystemManager;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

public class BrushItemModed extends Item {
    public BrushItemModed(Properties properties) {
        super(properties);
    }

    @Override
    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack itemStack, int i) {
        super.onUseTick(level, livingEntity, itemStack, i);

        if (!(livingEntity instanceof Player)) return;

        Player player = (Player) livingEntity;
        Vec3 position = new Vec3(Directional.rayCast(level, player, player.getViewVector(1.0f), 100).toVector3f());
        if (level.isClientSide)
            spawnParticle(position, ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, "fire_smoke"));


    }

    public static void spawnParticle(Vec3 position, ResourceLocation id){
        try {
            ParticleSystemManager manager = VeilRenderSystem.renderer().getParticleManager();
            ParticleEmitter emitter = manager.createEmitter(id);
            emitter.setPosition(position);
            manager.addParticleSystem(emitter);
        } catch (Exception ignored) {

        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
        user.startUsingItem(hand);
        return InteractionResultHolder.consume(user.getItemInHand(hand));
    }

    @Override
    public int getUseDuration(ItemStack itemStack, LivingEntity livingEntity) {
        return 10000;
    }
}

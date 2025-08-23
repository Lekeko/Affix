package com.keko.affix.stylePointsManager.rockets;

import com.keko.affix.affixLogics.BoxHelper;
import com.keko.affix.sounds.ModSounds;
import foundry.veil.api.client.render.CullFrustum;
import foundry.veil.api.client.render.VeilRenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RocketScreenManager {
    public static float ticks = 0;
    public static List<LivingEntity> hitmarkPositions = new ArrayList<>();
    public static final int MAX_CROSSHAIRS = 10;

    public static void updateTicks(){
        ticks += Minecraft.getInstance().getTimer().getGameTimeDeltaTicks() * 60;
        if (ticks > 2){
            ticks = 0;
            if (hitmarkPositions.size() < MAX_CROSSHAIRS)
                tryAddingTarget();
        }

        removeInvisible();

    }

    public static List<LivingEntity> getHitmarkPositions() {
        return hitmarkPositions;
    }

    private static void removeInvisible() {
        Frustum frustum = VeilRenderSystem.getCullingFrustum().toFrustum();
        Iterator<LivingEntity> iterator = hitmarkPositions.iterator();
        while (iterator.hasNext()) {
            LivingEntity livingEntity = iterator.next();

            AABB entityBox = livingEntity.getBoundingBoxForCulling().inflate(0.5F);
            if (!frustum.isVisible(entityBox))

                if (!frustum.isVisible(entityBox))
                    iterator.remove();
        }

    }

    private static void tryAddingTarget() {
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;

        CullFrustum frustum = VeilRenderSystem.getCullingFrustum();
        Vec3 posToScan = player.getPosition(1.0f).add(player.getViewVector(1.0f).multiply(6,6,6));
        AABB aabb = BoxHelper.createEvenBox(new BlockPos((int)posToScan.x, (int)posToScan.y, (int)posToScan.z), 20);

        float distance = 69420.0f;
        LivingEntity holder = null;

        for (LivingEntity livingEntity : player.level().getEntitiesOfClass(LivingEntity.class, aabb,  LivingEntity::isAlive))
            if (livingEntity != player){
                AABB entityBox = livingEntity.getBoundingBoxForCulling().inflate(0.5F);
                if (frustum.testAab(entityBox) && !hitmarkPositions.contains(livingEntity) && livingEntity.distanceTo(player) < distance) {
                    holder = livingEntity;
                    distance = livingEntity.distanceTo(player);
                }
            }

        if (holder!= null){
            hitmarkPositions.add(holder);
            player.level().playSound(player, player.getOnPos(), ModSounds.ROCKET_LAUNCH, SoundSource.PLAYERS, 1, 1.5f + player.level().random.nextFloat());
        }
    }


    public static void reset() {
        ticks = 0;
        hitmarkPositions.clear();
    }
}

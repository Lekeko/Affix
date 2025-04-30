package com.keko.affixLogics;

import com.keko.packet.GenericSpawnerParticleS2C;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

public class DodgeHandler {

    public static void dodgePlayer(Player player, float forwardImpulse, float leftImpulse, boolean jump){
        float yawRad = (float) Math.toRadians(player.getYRot());

        double deltaX_forward = -Mth.sin(yawRad) * forwardImpulse;
        double deltaZ_forward =  Mth.cos(yawRad) * forwardImpulse;

        double deltaX_left = -Mth.cos(yawRad) * -leftImpulse;
        double deltaZ_left = -Mth.sin(yawRad) * -leftImpulse; //BLJ

        double deltaX = deltaX_forward + deltaX_left;
        double deltaZ = deltaZ_forward + deltaZ_left;

        double deltaY = jump && player.onGround() ? 1 : 0;
        player.push(deltaX, deltaY, deltaZ);
        player.hurtMarked = true;

        spawnCoolAssParticles(player, -deltaX, -deltaY, -deltaZ);
    }

    private static void spawnCoolAssParticles(Player player, double deltaX, double deltaY, double deltaZ) {
        float minimizer = 2f;

        deltaX /= minimizer;
        deltaY /= minimizer;
        deltaZ /= minimizer;

        ServerLevel serverWorld = (ServerLevel) player.level();
        double x = player.getX();
        double y = player.getY() + 1.23f;
        double z = player.getZ();
        int numberOfParticles = 22;

        AABB aabb = new AABB(player.getX() + 30, player.getY() + 30, player.getZ() + 30,
                             player.getX() - 30, player.getY() - 30, player.getZ() - 30);
        for (Player players : serverWorld.getEntitiesOfClass(Player.class, aabb, Player::isAlive))
            for (int i = 0; i++ < numberOfParticles;)
                ServerPlayNetworking.send((ServerPlayer) players, new GenericSpawnerParticleS2C(
                        x + (serverWorld.random.nextDouble() - 0.5f) * 1,
                        y + (serverWorld.random.nextDouble() - 0.5f) * 1,
                        z + (serverWorld.random.nextDouble() - 0.5f) * 1,
                        deltaX / (1 + serverWorld.random.nextDouble()),
                        deltaY / (1 + serverWorld.random.nextDouble()),
                        deltaZ / (1 + serverWorld.random.nextDouble())
                ));
    }
}

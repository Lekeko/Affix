package com.keko.affixLogics;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

public class PhasePlayers {
    public static void phase(PlayerPhase playerPhase){
        Player player = playerPhase.getPlayer();
        Vec3 phaseVelocity = playerPhase.getPhaseVelocity();
        player.push(phaseVelocity.multiply(0.3, 0.02, 0.3));
        player.setSwimming(true);
        player.hurtMarked = true;
        if (player.getY() < -63)
            player.push(phaseVelocity.multiply(0.4, 1,0.4));
    }

    public static boolean canStopPhasing(PlayerPhase playerPhase){
        Player player = playerPhase.getPlayer();
        int ok = 0;
        int searchArea = 2;
        for (int i = -searchArea; i <= searchArea; i++)
            for (int j = -searchArea; j <= searchArea; j++)
                for (int k = -searchArea; k <= searchArea; k++){
                    Vec3 posToCheck = new Vec3(player.getX() + i, player.getY() + j, player.getZ() + k);

                    if (player.level().getBlockState(BlockPos.containing(posToCheck)).is(Blocks.AIR))
                         ok++;
                }

        return ok == Math.pow(searchArea * 2 + 1, 3);
    }
}

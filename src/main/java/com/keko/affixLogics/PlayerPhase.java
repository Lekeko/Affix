package com.keko.affixLogics;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3d;

public class PlayerPhase {
    private final Player player;
    private Vec3 phaseVelocity;
    private final float BOYANT_MODIFIER = 0.1f;

    public PlayerPhase(Player player , Vec3 phaseVelocity){
        this.phaseVelocity = phaseVelocity;
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public Vec3 getPhaseVelocity() {
        return phaseVelocity;
    }

    public void up(){
        this.phaseVelocity = this.phaseVelocity.add(0,BOYANT_MODIFIER,0);
        this.phaseVelocity = this.phaseVelocity.lerp(new Vec3(player.getViewVector(1.0f).x, this.phaseVelocity.y, player.getViewVector(1.0f).z),1);
    }
}

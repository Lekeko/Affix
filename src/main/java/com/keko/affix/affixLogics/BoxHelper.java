package com.keko.affix.affixLogics;

import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;

public class BoxHelper {

    public static AABB createEvenBox(BlockPos origin, float size){
        return new AABB(origin.getX() + size, origin.getY() + size, origin.getZ() + size,
                        origin.getX() - size, origin.getY() - size, origin.getZ() - size);
    }

    public static AABB createBox(BlockPos origin, float x, float y, float z){
        return new AABB(origin.getX() + x, origin.getY() + y, origin.getZ() + z,
                        origin.getX() - x, origin.getY() - y, origin.getZ() - z);
    }
}

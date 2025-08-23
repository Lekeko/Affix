package com.keko.affix.affixLogics;

import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class BoxHelper {

    public static AABB createEvenBox(BlockPos origin, float size){
        return new AABB(origin.getX() + size, origin.getY() + size, origin.getZ() + size,
                        origin.getX() - size, origin.getY() - size, origin.getZ() - size);
    }

    public static AABB createEvenVec3Box(Vec3 origin, float size){
        return new AABB(origin.x() + size, origin.y() + size, origin.z() + size,
                origin.x() - size, origin.y() - size, origin.z() - size);
    }

    public static AABB createBox(BlockPos origin, float x, float y, float z){
        return new AABB(origin.getX() + x, origin.getY() + y, origin.getZ() + z,
                        origin.getX() - x, origin.getY() - y, origin.getZ() - z);
    }

    public static AABB createVec3Box(Vec3 origin, float x, float y, float z){
        return new AABB(origin.x() + x, origin.y() + y, origin.z() + z,
                origin.x() - x, origin.y() - y, origin.z() - z);
    }
}

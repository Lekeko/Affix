package com.keko.affix.helpers;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public abstract class Directional {

		public static Vec3 rayCast(Level world, Entity player, Vec3 direction, double distance) {
			Vec3 startPos = player.getEyePosition();
			Vec3 endPos = startPos.add(direction.normalize().scale(distance));

			EntityHitResult entityHitResult = ProjectileUtil.getEntityHitResult(
					player,
					startPos,
					endPos,
					player.getBoundingBox().expandTowards(direction.scale(distance)).inflate(1.0),
					(target) -> !target.isSpectator() && target.isPickable(),
					distance * distance
			);

			ClipContext context = new ClipContext(
					startPos,
					endPos,
					ClipContext.Block.OUTLINE,
					ClipContext.Fluid.NONE,
					player
			);
			BlockHitResult blockHitResult = world.clip(context);

			if (entityHitResult != null) {
				Vec3 entityHitPos = entityHitResult.getLocation();
				double entityDist = startPos.distanceToSqr(entityHitPos);

				if (blockHitResult.getType() == HitResult.Type.MISS || entityDist <= startPos.distanceToSqr(blockHitResult.getLocation())) {
					return entityHitPos;
				}
			}

			if (blockHitResult.getType() != HitResult.Type.MISS)
				return blockHitResult.getLocation();

			return endPos;
		}
	}


package com.keko;

import com.keko.entity.ModEntities;
import com.keko.items.ModItems;
import com.keko.modComponents.ModComponents;
import com.keko.particle.ModParticles;
import com.keko.particle.ParticleRegistrerMod;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.world.level.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class Affix implements ModInitializer {
	public static final String MOD_ID = "affix";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Affixing......");

		registration();




	}

	private void registration() {
		ModItems.registerModItems();
		ModComponents.registerDataComponents();
		ModParticles.register();
		ParticleRegistrerMod.register();
		ModEntities.registerEntities();
	}
}
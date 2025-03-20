package com.keko;

import com.keko.items.ModItems;
import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Affix implements ModInitializer {
	public static final String MOD_ID = "affix";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Affixing......");

		ModItems.registerModItems();


	}
}
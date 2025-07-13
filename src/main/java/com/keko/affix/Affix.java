package com.keko.affix;

import com.keko.affix.affixLogics.PhasePlayers;
import com.keko.affix.affixLogics.PlayerPhase;
import com.keko.affix.effects.ModStatusEffects;
import com.keko.affix.entity.ModBlockEntityTypes;
import com.keko.affix.entity.ModEntities;
import com.keko.affix.items.ModGroupItem;
import com.keko.affix.items.ModItems;
import com.keko.affix.midLib.AffixConfigs;
import com.keko.affix.modComponents.ModComponents;
import com.keko.affix.packet.SyncPhaserRemoverS2C;
import com.keko.affix.packet.SyncPhaserS2C;
import com.keko.affix.packet.networking.ModMessagesClient;
import com.keko.affix.sounds.ModSounds;
import com.keko.affix.util.ModLootTableModif;
import eu.midnightdust.lib.config.MidnightConfig;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Affix implements ModInitializer {
	public static final String MOD_ID = "affix";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	private static ArrayList<PlayerPhase> phasingPlayers = new ArrayList<>();

	public static void addPlayer(PlayerPhase playerPhase) {
		for (PlayerPhase playerPhase1 : phasingPlayers)
			if (playerPhase1.getPlayer() == playerPhase.getPlayer())
				return;
		phasingPlayers.add(playerPhase);
		ServerPlayNetworking.send((ServerPlayer) playerPhase.getPlayer(), new SyncPhaserS2C(playerPhase.getPlayer().getId()));
	}

	public static boolean isPLayer(Player player) {
			for (PlayerPhase playerPhase1 : phasingPlayers)
				if (playerPhase1.getPlayer().getUUID().equals(player.getUUID()))
					return true;
			return false;
	}

    public static ResourceLocation id(String name) {
		return ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, name);
    }


    @Override
	public void onInitialize() {
		LOGGER.info("Affixing......");

		registration();
		events();
		ResourceManagerHelper.registerBuiltinResourcePack(ResourceLocation.parse("affix_particles"),
				FabricLoader.getInstance().getModContainer(MOD_ID).orElseThrow(), ResourcePackActivationType.ALWAYS_ENABLED);

	}

	private void events() {
		ServerTickEvents.END_SERVER_TICK.register((minecraftServer -> {
			if (!phasingPlayers.isEmpty()) {
				Iterator<PlayerPhase> iterator = phasingPlayers.iterator();
				while (iterator.hasNext()) {
					PlayerPhase playerPhase = iterator.next();
					PhasePlayers.phase(playerPhase);
					playerPhase.up();

					if (PhasePlayers.canStopPhasing(playerPhase) || !playerPhase.getPlayer().isAlive()) {
						iterator.remove();
						ServerPlayNetworking.send((ServerPlayer) playerPhase.getPlayer(), new SyncPhaserRemoverS2C(playerPhase.getPlayer().getId()));

					}
				}
			}

			scarePlayers(minecraftServer);
		}));

	}

	private void scarePlayers(MinecraftServer minecraftServer) {
		List<ServerPlayer> playersToScare = minecraftServer.getPlayerList().getPlayers();
		for (Player player : playersToScare){
			if (player.hasEffect(ModStatusEffects.UNSTABLE)){
				RandomSource random = player.level().random;
				int choice = random.nextIntBetweenInclusive(1, 1000);
				if (random.nextIntBetweenInclusive(1, 111) < 1) choice = 50;
				if (isBetween(choice, 1, 10))
					player.level().playSound(null, player.getOnPos(), SoundEvents.GRASS_BREAK, SoundSource.PLAYERS ,0.3f,  0.3f);
				if (isBetween(choice, 11, 20))
					player.level().playSound(null, player.getOnPos(), SoundEvents.GLASS_BREAK, SoundSource.PLAYERS ,0.3f,  0.3f);
				if (isBetween(choice, 21, 30))
					player.level().playSound(null, player.getOnPos(), SoundEvents.GRASS_STEP, SoundSource.PLAYERS ,0.3f,  0.3f);
				if (isBetween(choice, 31, 40))
					player.level().playSound(null, player.getOnPos(), ModSounds.WHISPER, SoundSource.PLAYERS ,0.3f,  0.3f);
				if (isBetween(choice, 41, 42))
					player.hurt(player.level().damageSources().generic(), 4);
			}
		}
	}

	public static boolean isBetween(int x, int lower, int upper) {
		return lower <= x && x <= upper;
	}

	private void registration() {
		ModItems.registerModItems();
		ModComponents.registerDataComponents();
		ModEntities.registerEntities();
		ModGroupItem.register();
		ModStatusEffects.registerStatusEffects();
		ModSounds.registerSounds();
		MidnightConfig.init(MOD_ID, AffixConfigs.class);
		ModMessagesClient.registerC2SPacket();
		ModLootTableModif.modifu();
		ModBlockEntityTypes.registerBlockEnt();
	}
}
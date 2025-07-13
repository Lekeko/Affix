package com.keko.affix.packet.networking;

import com.keko.affix.Affix;
import com.keko.affix.affixLogics.DodgeHandler;
import com.keko.affix.effects.ModStatusEffects;
import com.keko.affix.entity.ModEntities;
import com.keko.affix.entity.fabricPocket.FabricPocket;
import com.keko.affix.entity.infernalBeacon.InfernalBeacon;
import com.keko.affix.entity.infernalDragon.InfernalDragon;
import com.keko.affix.entity.infernalPortal.InfernalPortal;
import com.keko.affix.entity.infernalPrison.InfernalPrison;
import com.keko.affix.packet.*;
import com.keko.affix.sounds.ModSounds;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import java.awt.*;
import java.util.ArrayList;


public class ModMessagesClient {
    public static final ResourceLocation RENDER_MIRROR_SHADER = ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, "render_mirror_shader_tp");
    public static final ResourceLocation SEND_PLAYER_FOR_DODGE = ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, "SEND_PLAYER_FOR_DODGE_TP".toLowerCase());
    public static final ResourceLocation SPAWN_GENERIC_PARTICLE = ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, "SPAWN_GENERIC_PARTICLE_TP".toLowerCase());
    public static final ResourceLocation CRAAZY_STATUS = ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, "CRAAZY_STATUS_TP".toLowerCase());
    public static final ResourceLocation ANOMALY_ID = ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, "ANOMALY_ID_TP".toLowerCase());
    public static final ResourceLocation SYNC_PHASERS_ID = ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, "SYNC_PHASERS_ID_TP".toLowerCase());
    public static final ResourceLocation DRAGON_SUMMON_ID = ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, "DRAGON_SUMMON_ID_TP".toLowerCase());
    public static final ResourceLocation SPEAR_SUMMON_ID = ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, "SPEAR_SUMMON_ID_TP".toLowerCase());
    public static final ResourceLocation SYNC_PHASERS_REMOVER_ID = ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, "SYNC_PHASERS_REMOVER_ID_TP".toLowerCase());
    public static final ResourceLocation BEACON_SUMMON_ID = ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, "BEACON_SUMMON_ID_TP".toLowerCase());


    public static void registerC2SPacket(){
        PayloadTypeRegistry.playS2C().register(GenericSpawnerParticleS2C.ID, GenericSpawnerParticleS2C.CODEC);
        PayloadTypeRegistry.playS2C().register(RenderHeavyMirrorShaderS2C.ID, RenderHeavyMirrorShaderS2C.CODEC);
        PayloadTypeRegistry.playC2S().register(SendPlayerToDodgeC2S.ID, SendPlayerToDodgeC2S.CODEC);
        PayloadTypeRegistry.playC2S().register(CrazyStatusC2S.ID, CrazyStatusC2S.CODEC);
        PayloadTypeRegistry.playC2S().register(AnomalyPacketC2S.ID, AnomalyPacketC2S.CODEC);
        PayloadTypeRegistry.playS2C().register(SyncPhaserS2C.ID, SyncPhaserS2C.CODEC);
        PayloadTypeRegistry.playS2C().register(SyncPhaserRemoverS2C.ID, SyncPhaserRemoverS2C.CODEC);
        PayloadTypeRegistry.playC2S().register(DragonSummonPacketC2S.ID, DragonSummonPacketC2S.CODEC);
        PayloadTypeRegistry.playC2S().register(SpearSummonPacketC2S.ID, SpearSummonPacketC2S.CODEC);
        PayloadTypeRegistry.playC2S().register(BeaconSummonPacketC2S.ID, BeaconSummonPacketC2S.CODEC);

        ServerPlayNetworking.registerGlobalReceiver(SendPlayerToDodgeC2S.ID, (payload, context) -> {
            context.server().execute(() -> {
                DodgeHandler.dodgePlayer(context.player(), payload.forwardImpulse(), payload.leftImpulse(), payload.jump());
            });
        });

        ServerPlayNetworking.registerGlobalReceiver(CrazyStatusC2S.ID, (payload, context) -> {
            context.server().execute(() -> {
                context.player().addEffect(new MobEffectInstance(ModStatusEffects.UNSTABLE, 30 * 20,0));
            });
        });

        ServerPlayNetworking.registerGlobalReceiver(AnomalyPacketC2S.ID, (payload, context) -> {
            context.server().execute(() -> {
                FabricPocket fabricPocket = new FabricPocket(ModEntities.FABRIC_POCKET_ENTITY_TYPE, context.player().level());
                fabricPocket.setPos(
                        payload.x(),
                        payload.y(),
                        payload.z()
                );
                context.player().level().addFreshEntity(fabricPocket);
            });
        });

        ServerPlayNetworking.registerGlobalReceiver(DragonSummonPacketC2S.ID, (payload, context) -> {
            context.server().execute(() -> {
                Level level = context.player().level();
                InfernalDragon infernalDragon = new InfernalDragon(ModEntities.INFERNAL_DRAGON, level);

                BlockPos pos = new BlockPos(payload.x(), payload.y(), payload.z());
                while (level.getBlockState(pos).is(Blocks.AIR))
                    pos = pos.below();

                Vec3 positionToSummon = new Vec3(
                        pos.getX() + (level.random.nextIntBetweenInclusive(40, 50) * (level.random.nextBoolean() ? -1 : 1)),
                        pos.getY() + 250,
                        pos.getZ() + (level.random.nextIntBetweenInclusive(40, 50) * (level.random.nextBoolean() ? -1 : 1))
                );
                infernalDragon.setPos(positionToSummon.x, positionToSummon.y, positionToSummon.z);
                infernalDragon.setAttackPosition(new Vector3f(pos.getX(),pos.getY(), pos.getZ()));
                infernalDragon.setDeltaMovement(new Vec3(pos.getX(), pos.getY(), pos.getZ()).subtract(positionToSummon).normalize().multiply(2,2,2));
                level.addFreshEntity(infernalDragon);
                if (!context.player().isCreative())
                    context.player().getCooldowns().addCooldown(context.player().getUseItem().getItem(), 20 * 120);
                InfernalPortal infernalPortal = new InfernalPortal(ModEntities.INFERNAL_PORTAL, level);
                infernalPortal.setPos(positionToSummon.x, positionToSummon.y, positionToSummon.z);
                infernalPortal.setDeltaMovement(new Vec3(pos.getX(), pos.getY(), pos.getZ()).subtract(positionToSummon).normalize().multiply(-1,-1,-1));
                level.addFreshEntity(infernalPortal);

                level.playSound(null, new BlockPos(pos.getX(), pos.getY(), pos.getZ()),ModSounds.DRAGON_SUMMON, SoundSource.PLAYERS, 10, 0.7f);

            });
        });

        ServerPlayNetworking.registerGlobalReceiver(SpearSummonPacketC2S.ID, (payload, context) -> {
            context.server().execute(() -> {
                Level level = context.player().level();
                AABB aabb = new AABB(payload.x() + 20, payload.y() + 5, payload.z() + 20,payload.x() - 20, payload.y() - 5, payload.z() - 20);
                int i = 0;
                level.playSound(null, payload.x(), payload.y(), payload.z(), ModSounds.DRAGON_SUMMON, SoundSource.PLAYERS, 12, 27f);
                for (LivingEntity entity : context.player().level().getEntitiesOfClass(LivingEntity.class, aabb, LivingEntity::isAlive)){
                    if (!(entity instanceof Player)){
                        InfernalPrison infernalPrison = new InfernalPrison(ModEntities.INFERNAL_PRISON, level);
                        entity.setPos(entity.getX(), entity.getY() + 2, entity.getZ());
                        infernalPrison.setPos(entity.getX(), entity.getY(), entity.getZ());
                        infernalPrison.setOrder(i);
                        infernalPrison.setTrappedMob(entity.getId());
                        infernalPrison.setOwner(context.player());
                        infernalPrison.addDeltaMovement(new Vec3(0,1,0));
                        DamageSource damageSource = infernalPrison.level().damageSources().indirectMagic(infernalPrison, context.player());
                        entity.hurt(damageSource, entity.getMaxHealth() / 2);
                        entity.level().addFreshEntity(infernalPrison);
                        i+=5;
                        if (i > 50) i = 0;
                        if (!context.player().isCreative())
                            context.player().getCooldowns().addCooldown(context.player().getUseItem().getItem(), 20 * 30);

                    }

                }
            });
        });

        ServerPlayNetworking.registerGlobalReceiver(BeaconSummonPacketC2S.ID, (payload, context) -> {
            context.server().execute(() -> {
                Level level = context.player().level();
                InfernalBeacon infernalBeacon = new InfernalBeacon(ModEntities.INFERNAL_BEACON, level);
                infernalBeacon.setPos(new Vec3(payload.x(), payload.y()+2, payload.z()));
                infernalBeacon.setType(payload.btype());
                ArrayList<Color> colors = new ArrayList<>();
                colors.add(new Color(202, 69, 255));
                colors.add(new Color(255, 237, 124));
                colors.add(new Color(158, 255, 128));
                colors.add(new Color(98, 245, 255));
                infernalBeacon.setColor(new Vector3f(
                        colors.get(payload.btype()-1).getRed(),
                        colors.get(payload.btype()-1).getGreen(),
                        colors.get(payload.btype()-1).getBlue()
                ));
                if (!context.player().isCreative())
                    context.player().getCooldowns().addCooldown(context.player().getUseItem().getItem(), 20 * 90);
                level.addFreshEntity(infernalBeacon);
            });
        });
    }
}

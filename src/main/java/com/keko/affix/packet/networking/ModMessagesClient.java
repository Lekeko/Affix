package com.keko.affix.packet.networking;

import com.keko.affix.Affix;
import com.keko.affix.affixLogics.AffixConstantsPunch;
import com.keko.affix.affixLogics.AffixRenderingHandlers;
import com.keko.affix.affixLogics.BoxHelper;
import com.keko.affix.affixLogics.DodgeHandler;
import com.keko.affix.effects.ModStatusEffects;
import com.keko.affix.entity.ModEntities;
import com.keko.affix.entity.enderExplosion.EnderExplosion;
import com.keko.affix.entity.enderFingers.EnderFingers;
import com.keko.affix.entity.enderFist.EnderFist;
import com.keko.affix.entity.fabricPocket.FabricPocket;
import com.keko.affix.entity.infernalBeacon.InfernalBeacon;
import com.keko.affix.entity.infernalDragon.InfernalDragon;
import com.keko.affix.entity.infernalPortal.InfernalPortal;
import com.keko.affix.entity.infernalPrison.InfernalPrison;
import com.keko.affix.packet.*;
import com.keko.affix.sounds.ModSounds;
import com.keko.affix.util.cc.MyComponents;
import com.keko.affix.util.cc.ScoreComponent;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import java.awt.*;
import java.util.ArrayList;


public class ModMessagesClient {
    public static final ResourceLocation RENDER_MIRROR_SHADER = ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, "render_mirror_shader_tp");
    public static final ResourceLocation SEND_PLAYER_FOR_DODGE = ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, "sen_player_for_dodge_tp");
    public static final ResourceLocation SPAWN_GENERIC_PARTICLE = ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, "spawn_generic_particle_tp");
    public static final ResourceLocation CRAAZY_STATUS = ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, "craazy_status_tp"); //ikik
    public static final ResourceLocation ANOMALY_ID = ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, "anomaly_id_tp");
    public static final ResourceLocation SYNC_PHASERS_ID = ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, "sync_phasers_id");
    public static final ResourceLocation DRAGON_SUMMON_ID = ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, "dragon_summon_id_tp");
    public static final ResourceLocation SPEAR_SUMMON_ID = ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, "spear_summon_id_tp");
    public static final ResourceLocation SYNC_PHASERS_REMOVER_ID = ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, "sync_phasers_remover_id");
    public static final ResourceLocation BEACON_SUMMON_ID = ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, "beacon_summon_id");
    public static final ResourceLocation JAM_REQUEST = ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, "jam_request_id");
    public static final ResourceLocation R_REQUEST = ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, "r_request_id");
    public static final ResourceLocation PUNCH_REQUEST = ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, "punch_request_id");
    public static final ResourceLocation AWARD_FOR_KILL = ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, "award_for_kill_id");
    public static final ResourceLocation AWARD_FOR_JAMMING = ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, "award_for_jamming_id");
    public static final ResourceLocation PURCHASE = ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, "purchase_id");


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
        PayloadTypeRegistry.playC2S().register(JamRequestPacketC2S.ID, JamRequestPacketC2S.CODEC);
        PayloadTypeRegistry.playC2S().register(RocketSpawnRequestPacketC2S.ID, RocketSpawnRequestPacketC2S.CODEC);
        PayloadTypeRegistry.playC2S().register(PunchRequestPacketC2S.ID, PunchRequestPacketC2S.CODEC);
        PayloadTypeRegistry.playS2C().register(AwardForKillingS2C.ID, AwardForKillingS2C.CODEC);
        PayloadTypeRegistry.playC2S().register(AwardForJammingC2S.ID, AwardForJammingC2S.CODEC);
        PayloadTypeRegistry.playC2S().register(ConfirmPurchaseC2S.ID, ConfirmPurchaseC2S.CODEC);

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

        ServerPlayNetworking.registerGlobalReceiver(RocketSpawnRequestPacketC2S.ID, (payload, context) -> {
            context.server().execute(() -> {

                ServerPlayer player = context.player();
                Entity target = player.level().getEntity(payload.id());
                EnderFingers enderFingers = new EnderFingers(ModEntities.ENDER_FINGERS, player.level());
                enderFingers.setPos(player.getPosition(1.0f).add(0,2,0));
                enderFingers.setDeltaMovement((player.level().random.nextFloat()- 0.5f),1.2f,(player.level().random.nextFloat()- 0.5f));
                enderFingers.setOwner(player);
                enderFingers.setTarget((LivingEntity) target);
                player.level().addFreshEntity(enderFingers);
                player.level().playSound(enderFingers, enderFingers.getOnPos(), ModSounds.ROCKET_LAUNCH, SoundSource.PLAYERS, 1.4f, 0.5f + player.level().random.nextFloat());
            });
        });

        ServerPlayNetworking.registerGlobalReceiver(JamRequestPacketC2S.ID, (payload, context) -> {
            context.server().execute(() -> {
                if (payload.id() == 10){
                    context.player().removeEffect(ModStatusEffects.OTHERWORDLY);

                    AABB aabb = BoxHelper.createBox(context.player().getOnPos(), 5,5,5);
                    for (EnderFist enderFist : context.player().level().getEntitiesOfClass(EnderFist.class, aabb, EnderFist::isNoGravity)){
                        if (enderFist.getOwner() == context.player()){
                            enderFist.discard();
                        }
                    }

                    return;
                }

                Player player = context.player();
                player.addEffect(new MobEffectInstance(ModStatusEffects.OTHERWORDLY, 1000000,0));

                ServerLevel serverLevel = (ServerLevel) player.level();

                long currentTime = serverLevel.getDayTime();
                long nightTime = 14000;
                long ticksToAdd = nightTime - (currentTime % 24000);
                serverLevel.setDayTime(currentTime + ticksToAdd);

                EnderFist enderFistLeft = new EnderFist(ModEntities.ENDER_FIST, player.level());
                EnderFist enderFistRight = new EnderFist(ModEntities.ENDER_FIST, player.level());

                Vec3 playerDirection = player.getViewVector(1.0f);
                Vec3 sideVector = playerDirection.cross(new Vec3(0,1,0));

                enderFistLeft.setPos(player.getPosition(1.0f).subtract(sideVector.multiply(2,2,2)));
                enderFistRight.setPos(player.getPosition(1.0f).add(sideVector.multiply(2,2,2)));

                enderFistLeft.setSleevePosition(player.getPosition(1.0f).add(sideVector.multiply(2,2,2)).toVector3f());
                enderFistRight.setSleevePosition(player.getPosition(1.0f).subtract(sideVector.multiply(2,2,2)).toVector3f());

                enderFistLeft.setLeft(true);
                enderFistRight.setLeft(false);

                enderFistLeft.setOwner(player);
                enderFistRight.setOwner(player);

                player.level().addFreshEntity(enderFistLeft);
                player.level().addFreshEntity(enderFistRight);

                Affix.addFist(enderFistLeft);
                Affix.addFist(enderFistRight);

            });
        });

        ServerPlayNetworking.registerGlobalReceiver(PunchRequestPacketC2S.ID, (payload, context) -> {
            context.server().execute(() -> {
                AABB aabb = BoxHelper.createBox(context.player().getOnPos(), 10,10,10);

                if (payload.superP() == AffixConstantsPunch.REMAKE_FISTS){

                    Player player = context.player();

                    EnderFist enderFistLeft = getNearFist(player, true);
                    EnderFist enderFistRight = getNearFist(player, false);

                    Vec3 playerDirection = player.getViewVector(1.0f);
                    Vec3 sideVector = playerDirection.cross(new Vec3(0,1,0));

                    if (enderFistLeft == null){
                        enderFistLeft = new EnderFist(ModEntities.ENDER_FIST, player.level());
                        enderFistLeft.setPos(player.getPosition(1.0f).subtract(sideVector.multiply(2,2,2)));
                        enderFistLeft.setSleevePosition(player.getPosition(1.0f).add(sideVector.multiply(2,2,2)).toVector3f());
                        enderFistLeft.setLeft(true);
                        enderFistLeft.setOwner(player);
                        player.level().addFreshEntity(enderFistLeft);
                        Affix.addFist(enderFistLeft);
                    }

                    if (enderFistRight == null){
                        enderFistRight = new EnderFist(ModEntities.ENDER_FIST, player.level());
                        enderFistRight.setPos(player.getPosition(1.0f).add(sideVector.multiply(2, 2, 2)));
                        enderFistRight.setSleevePosition(player.getPosition(1.0f).subtract(sideVector.multiply(2, 2, 2)).toVector3f());
                        enderFistRight.setLeft(false);
                        enderFistRight.setOwner(player);
                        player.level().addFreshEntity(enderFistRight);
                        Affix.addFist(enderFistRight);

                    }

                    return;
                }

                if (payload.superP() == AffixConstantsPunch.SLAM_GROUND_PUNCH){
                    Vec3 direction = context.player().getViewVector(1.0f).normalize();
                    context.player().setDeltaMovement(direction.multiply(8,8,8));
                    context.player().hurtMarked = true;
                    for (EnderFist enderFist : context.player().level().getEntitiesOfClass(EnderFist.class, aabb, EnderFist::isNoGravity)){
                        if (enderFist.getOwner() == context.player()){
                            if (payload.left() == enderFist.getLeft()) {
                                enderFist.setWantsToExplode(true);
                            }
                        }
                    }

                    return;
                }

                if (payload.superP() == AffixConstantsPunch.DOUBLE_BLAST_PUNCH){



                    for (EnderFist enderFist : context.player().level().getEntitiesOfClass(EnderFist.class, aabb, EnderFist::isNoGravity)){
                        enderFist.punch();
                    }

                    Vec3 dir = context.player().getViewVector(1.0f).normalize();

                    Vec3 explodeLoc = context.player().getEyePosition().add(dir.multiply(6,6,6));

                    context.player().level().explode(context.player(), explodeLoc.x, explodeLoc.y,explodeLoc.z, 2, Level.ExplosionInteraction.NONE);
                    context.player().addDeltaMovement(dir.multiply(-3,-4,-3));
                    context.player().hurtMarked = true;


                    EnderExplosion enderExplosion = new EnderExplosion(ModEntities.ENDER_EXPLOSION, context.player().level());
                    enderExplosion.setPos(context.player().getPosition(1.0f));
                    enderExplosion.setSmall(true);
                    context.player().level().addFreshEntity(enderExplosion);

                }

                if (payload.superP() == AffixConstantsPunch.NORMAL_PUNCH && !context.player().isFallFlying() && context.player().isSprinting()) {
                    Vec3 push = context.player().getViewVector(1.0f).normalize().multiply(0.6f,0.6f,0.6f);
                    push = new Vec3(push.x, 0, push.z);
                    context.player().addDeltaMovement(push);
                    context.player().hurtMarked = true;
                }

                for (EnderFist enderFist : context.player().level().getEntitiesOfClass(EnderFist.class, aabb, EnderFist::isNoGravity)){
                    if (enderFist.getOwner() == context.player()){
                        if (payload.left() == enderFist.getLeft())
                            enderFist.punch();
                    }
                }

            });
        });


        ServerPlayNetworking.registerGlobalReceiver(AwardForJammingC2S.ID, (payload, context) -> {
            context.server().execute(() -> {
                ScoreComponent component = MyComponents.SCORE.get(context.player());
                component.setScore(component.getScore() + (int) payload.score());
                MyComponents.SCORE.sync(context.player());
            });
        });

        ServerPlayNetworking.registerGlobalReceiver(ConfirmPurchaseC2S.ID, (payload, context) -> {
            context.server().execute(() -> {
                for (int i = 0; i < payload.ga(); i++){
                    ItemEntity ga = new ItemEntity(context.player().level(), context.player().getX(), context.player().getY(), context.player().getZ(), Items.ENCHANTED_GOLDEN_APPLE.getDefaultInstance());
                    context.player().level().addFreshEntity(ga);
                }
                for (int i = 0; i < payload.ns(); i++){
                    ItemEntity ga = new ItemEntity(context.player().level(), context.player().getX(), context.player().getY(), context.player().getZ(), Items.NETHERITE_SCRAP.getDefaultInstance());
                    context.player().level().addFreshEntity(ga);
                }
                for (int i = 0; i < payload.di(); i++){
                    ItemEntity ga = new ItemEntity(context.player().level(), context.player().getX(), context.player().getY(), context.player().getZ(), Items.DIAMOND.getDefaultInstance());
                    context.player().level().addFreshEntity(ga);
                }
                for (int i = 0; i < payload.em(); i++){
                    ItemEntity ga = new ItemEntity(context.player().level(), context.player().getX(), context.player().getY(), context.player().getZ(), Items.EMERALD.getDefaultInstance());
                    context.player().level().addFreshEntity(ga);
                }
                for (int i = 0; i < payload.tk(); i++){
                    ItemEntity ga = new ItemEntity(context.player().level(), context.player().getX(), context.player().getY(), context.player().getZ(), Items.OMINOUS_TRIAL_KEY.getDefaultInstance());
                    context.player().level().addFreshEntity(ga);
                }
            });
        });

    }

    private static EnderFist getNearFist(Player player, boolean left) {
        AABB aabb = BoxHelper.createEvenVec3Box(player.getPosition(1.0f), 10);
        for (EnderFist enderFist : player.level().getEntitiesOfClass(EnderFist.class, aabb)){
            if (enderFist.getOwner() == player && enderFist.getLeft() == left)
                return enderFist;
        }
        return null;
    }
}

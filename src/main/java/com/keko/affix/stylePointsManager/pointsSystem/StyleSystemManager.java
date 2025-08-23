package com.keko.affix.stylePointsManager.pointsSystem;

import com.keko.affix.AffixClient;
import com.keko.affix.entity.enderFist.EnderFist;
import com.keko.affix.packet.AwardForJammingC2S;
import com.keko.affix.packet.SendPlayerToDodgeC2S;
import com.keko.affix.sounds.ModSounds;
import com.keko.affix.util.cc.MyComponents;
import com.keko.affix.util.cc.ScoreComponent;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;

import static com.keko.affix.util.cc.MyComponents.SCORE;

public class StyleSystemManager {
    float style;
    float score;
    int tick;
    int rockets;
    float dashes;

    public StyleSystemManager() {
        this.style = 1;
        this.score = 0;
        tick = 0;
        rockets = 20;
        dashes = 3;
    }

    public void awardKill(float amount, float style) {
        this.style += style;
        this.style = Mth.clamp(this.style, 1, 7.5f);
        score += amount * this.style;
    }

    public float getScore() {
        return score;
    }

    public int getRockets() {
        return rockets;
    }

    public void addRockets(int rockets) {
        this.rockets += rockets;
        if (this.rockets > 50)
            this.rockets = 50;
    }

    public void subRockets(int rockets){
        this.rockets -= rockets;
    }

    public float getStyle() {
        return style;
    }

    public void decreaseStyle(float ammount){
        style -= ammount;
        if (style < 1)
            style = 1;
    }

    public void reset(){
        try {
            ClientPlayNetworking.send(new AwardForJammingC2S(score, style));
        }catch (Exception ignored){}
        style = 1;
        score = 0;
    }


    public void tick() {
        tick++;
        if (tick == 20 * 4){
            tick = 0;
            decreaseStyle(0.2f);
        }
        if (tick % 40 == 0)
            addRockets(1);

        if (dashes < 3){
            dashes+= 0.025f;
        }
    }

    public float getDashes() {
        return dashes;
    }

    public void dashed(float forwardImpulse, float leftImpulse, boolean jump) {
        if (dashes > 1) {
            Minecraft client = Minecraft.getInstance();
            ClientPlayNetworking.send(new SendPlayerToDodgeC2S(forwardImpulse, leftImpulse, jump));
            client.player.level().playLocalSound(client.player, ModSounds.JAM_DASH, SoundSource.PLAYERS, 0.5f, 1);
            dashes -= 1;
        }
    }
}

package com.keko.affix.entity;

import com.keko.affix.entity.fabricPocket.FabricPocketRenderer;
import com.keko.affix.entity.infernalArrow.InfernalArrowRenderer;
import com.keko.affix.entity.infernalBeacon.InfernalBeaconRenderer;
import com.keko.affix.entity.infernalDragon.InfernalDragonRenderer;
import com.keko.affix.entity.infernalExplosion.InfernalExplosionRenderer;
import com.keko.affix.entity.infernalPortal.InfernalPortalRenderer;
import com.keko.affix.entity.infernalPrison.InfernalPrisonRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public class ModEntitiesRenderer {
    public static void register(){
        EntityRendererRegistry.register(ModEntities.FABRIC_POCKET_ENTITY_TYPE, FabricPocketRenderer::new);
        EntityRendererRegistry.register(ModEntities.INFERNAL_ARROW, InfernalArrowRenderer::new);
        EntityRendererRegistry.register(ModEntities.INFERNAL_EXPLOSION, InfernalExplosionRenderer::new);
        EntityRendererRegistry.register(ModEntities.INFERNAL_DRAGON, InfernalDragonRenderer::new);
        EntityRendererRegistry.register(ModEntities.INFERNAL_PORTAL, InfernalPortalRenderer::new);
        EntityRendererRegistry.register(ModEntities.INFERNAL_PRISON, InfernalPrisonRenderer::new);
        EntityRendererRegistry.register(ModEntities.INFERNAL_BEACON, InfernalBeaconRenderer::new);
    }
}

package com.keko.entity;

import com.keko.entity.fabricPocket.FabricPocket;
import com.keko.entity.fabricPocket.FabricPocketRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public class ModEntitiesRenderer {
    public static void register(){
        EntityRendererRegistry.register(ModEntities.FABRIC_POCKET_ENTITY_TYPE, FabricPocketRenderer::new);
    }
}

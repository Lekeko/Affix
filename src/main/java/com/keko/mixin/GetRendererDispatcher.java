package com.keko.mixin;


import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ItemInHandRenderer.class)
public interface GetRendererDispatcher {
    @Accessor("entityRenderDispatcher")
    EntityRenderDispatcher affix$getEntityRendererDispatcher();

}

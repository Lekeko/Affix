package com.keko.affix.entity.enderFingers;

import com.keko.affix.Affix;
import com.keko.affix.midLib.AffixConfigs;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EnderFingersRenderer extends GeoEntityRenderer<EnderFingers> {

    public EnderFingersRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new EnderFingersModel());
    }
    private final int STRIP_LENGTH = 20;
    private final Map<UUID, ArrayList<Vec3>> entityPositions = new HashMap<>();


    @Override
    public @NotNull ResourceLocation getTextureLocation(EnderFingers enderFingers) {
        return ResourceLocation.fromNamespaceAndPath(Affix.MOD_ID, "textures/entity/ender_fingers.png");
    }

    public float lerp (float start, float finish, float t){
        return start * (1-t) + finish * t;
    }


    @Override
    public void render(EnderFingers enderFingers, float f, float g, PoseStack poseStack, MultiBufferSource multiBufferSource, int i){
        Color primaryColor;
        Color secondaryColor;
        try {
            primaryColor = Color.decode(AffixConfigs.jamPrimaryColor);
            secondaryColor = Color.decode(AffixConfigs.jamSecondaryColor);
        }catch (Exception e){
            Affix.LOGGER.warn("Please provide correct color input :3 (or imma send ur pc in space lol)");
            primaryColor = Color.MAGENTA;
            secondaryColor = Color.BLACK;
        }


        poseStack.pushPose();
        poseStack.last().pose().identity();

        renderTrail(enderFingers, f, g, poseStack, multiBufferSource, i, primaryColor, secondaryColor);

        poseStack.popPose();


        double speed = Math.sqrt(Math.pow(enderFingers.getDeltaMovement().x, 2) + Math.pow(enderFingers.getDeltaMovement().y, 2) + Math.pow(enderFingers.getDeltaMovement().z, 2))/3;

        poseStack.pushPose();
        poseStack.translate(0,0.5f,0);
        poseStack.scale(1.2f, (float) (1.2f + speed),1.2f);
        if (!enderFingers.isWantToDiscard()){
            poseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(g, enderFingers.yRotO, enderFingers.getYRot()) - 90.0F));
            poseStack.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(g, enderFingers.xRotO, enderFingers.getXRot()) + 90.0F));
            super.actuallyRender(poseStack, enderFingers, this.model.getBakedModel(this.model.getModelResource(enderFingers)),
                    RenderType.eyes(getTextureLocation(enderFingers)), multiBufferSource, multiBufferSource.getBuffer(RenderType.eyes(getTextureLocation(enderFingers))),
                    false, g, 255, OverlayTexture.NO_OVERLAY, secondaryColor.getRGB());
            poseStack.scale(-2, 2, 2);
            super.actuallyRender(poseStack, enderFingers, this.model.getBakedModel(this.model.getModelResource(enderFingers)),
                    RenderType.eyes(getTextureLocation(enderFingers)), multiBufferSource, multiBufferSource.getBuffer(RenderType.eyes(getTextureLocation(enderFingers))),
                    true, g, 255, OverlayTexture.NO_OVERLAY, primaryColor.getRGB());
        }
        poseStack.popPose();
    }

    public static Vec3 catmullRom(float t, Vec3 p0, Vec3 p1, Vec3 p2, Vec3 p3) {

        double x = 0.5 * ((2 * p1.x) + (-p0.x + p2.x) * t + (2 * p0.x - 5 * p1.x + 4 * p2.x - p3.x) * (t * t) + (-p0.x + 3 * p1.x - 3 * p2.x + p3.x) * (t * t * t));
        double y = 0.5 * ((2 * p1.y) + (-p0.y + p2.y) * t + (2 * p0.y - 5 * p1.y + 4 * p2.y - p3.y) * (t * t) + (-p0.y + 3 * p1.y - 3 * p2.y + p3.y) * (t * t * t));
        double z = 0.5 * ((2 * p1.z) + (-p0.z + p2.z) * t + (2 * p0.z - 5 * p1.z + 4 * p2.z - p3.z) * (t * t) + (-p0.z + 3 * p1.z - 3 * p2.z + p3.z) * (t * t * t));

        return new Vec3(x, y, z);
    }

    private void renderTrail(EnderFingers enderFingers, float f, float g, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, Color primaryColor,  Color secondaryColor) {
        UUID entityUUID = enderFingers.getUUID();
        updateArrayList(enderFingers, entityUUID, g);


        Camera camera = Minecraft.getInstance().gameRenderer.getMainCamera();
        Vec3 cameraPos = camera.getPosition();

        Tesselator tessellator = Tesselator.getInstance();
        BufferBuilder buffer = tessellator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);


        float maxTrailWidth = 0.3f;
        float trailWidth;

        ArrayList<Vec3> positions = entityPositions.get(entityUUID);


        if (positions != null && positions.size() >=2) {

            ArrayList<Vec3> smootherPositions = new ArrayList<>();
            final int SUBDIVISIONS = 20;

            for (int j = 0; j < positions.size() - 1; j++) {
                Vec3 p0 = positions.get(Math.max(0, j - 1));
                Vec3 p1 = positions.get(j);
                Vec3 p2 = positions.get(j + 1);
                Vec3 p3 = positions.get(Math.min(positions.size() - 1, j + 2));

                for (int k = 0; k < SUBDIVISIONS; k++) {
                    float t = (float) k / SUBDIVISIONS;
                    smootherPositions.add(catmullRom(t, p0, p1, p2, p3));
                }
            }
            smootherPositions.add(positions.getLast());
            for (int pose = 0; pose < smootherPositions.size() - 1; pose++) {
                trailWidth = maxTrailWidth * (1.0f - (float)pose / (float)smootherPositions.size());

                Vec3 pos1 = smootherPositions.get(pose);
                Vec3 pos2 = smootherPositions.get(pose + 1);

                Vec3 dir = pos2.subtract(pos1).normalize();
                Vec3 cameraLookVec = new Vec3(camera.getUpVector());
                Vec3 side1 = dir.cross(cameraLookVec).normalize().scale(trailWidth / 2);
                Vec3 side2 = side1.scale(-1);


                Vec3 v1 = pos1.add(side1);
                Vec3 v2 = pos1.add(side2);
                Vec3 v3 = pos2.add(side2);
                Vec3 v4 = pos2.add(side1);

                v1 = v1.subtract(cameraPos);
                v2 = v2.subtract(cameraPos);
                v3 = v3.subtract(cameraPos);
                v4 = v4.subtract(cameraPos);

                int newStripLength = smootherPositions.size();

                int red = (int) lerp(primaryColor.getRed(), secondaryColor.getRed(), ((float) pose )/newStripLength);
                int green = (int) lerp(primaryColor.getGreen(), secondaryColor.getGreen(), ((float) pose )/newStripLength);
                int blue = (int) lerp(primaryColor.getBlue(), secondaryColor.getBlue(), ((float) pose )/newStripLength);
                int alpha = (int) lerp(255, 0, ((float) pose )/newStripLength);


                buffer.addVertex(poseStack.last().pose(), (float) v1.x, (float) (v1.y + 1.2), (float) v1.z).setColor(red, green, blue, 255);
                buffer.addVertex(poseStack.last().pose(), (float) v2.x, (float) (v2.y + 1.2), (float) v2.z).setColor(red, green, blue, 255);
                buffer.addVertex(poseStack.last().pose(), (float) v3.x, (float) (v3.y + 1.2), (float) v3.z).setColor(red, green, blue, 255);
                buffer.addVertex(poseStack.last().pose(), (float) v4.x, (float) (v4.y + 1.2), (float) v4.z).setColor(red, green, blue, 255);
            }
            RenderSystem.disableCull();
            RenderSystem.enableDepthTest();

            RenderSystem.setShader(GameRenderer::getPositionColorShader);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            BufferUploader.drawWithShader(buffer.buildOrThrow());

            RenderSystem.disableDepthTest();
            RenderSystem.enableCull();
        }
    }

    @Override
    public @Nullable RenderType getRenderType(EnderFingers enderFingers, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderType.eyes(getTextureLocation(enderFingers));
    }

    @Override
    public boolean shouldRender(EnderFingers enderFingers, Frustum frustum, double d, double e, double f) {
        return enderFingers.tickCount > 1;
    }


    private void updateArrayList(EnderFingers enderFingers, UUID entityUUID, float f) {
        ArrayList<Vec3> positions = entityPositions.computeIfAbsent(entityUUID, k -> new ArrayList<>());
        Vec3 currentTickPos = enderFingers.getPosition(f);

            positions.addFirst(currentTickPos);

            if (positions.size() > STRIP_LENGTH) {
                positions.removeLast();
            }
    }
}

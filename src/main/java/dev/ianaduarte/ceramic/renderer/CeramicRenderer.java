package dev.ianaduarte.ceramic.renderer;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.ianaduarte.ceramic.layers.CeramicRenderLayer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Leashable;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.PlayerModelPart;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.phys.AABB;

import java.util.List;

public abstract class CeramicRenderer<E extends Entity, S extends EntityRenderState> extends EntityRenderer<E, S> {
	protected final List<CeramicRenderLayer<S, CeramicRenderer<E, S>>> renderLayers;
	
	protected CeramicRenderer(EntityRendererProvider.Context context) {
		super(context);
		this.renderLayers = Lists.newArrayList();
	}
	
	public abstract ResourceLocation getTextureLocation(S entity);
	
	public void render(S entity, float tickDelta, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
		for(var layer : this.renderLayers) {
			layer.render(
				entity,
				tickDelta, entity.ageInTicks,
				poseStack, bufferSource, packedLight
			);
		}
	}
	protected void setupRotations(E entity, PoseStack poseStack, float tickDelta, float entityYaw, float entityScale) {
		if(!entity.hasPose(Pose.SLEEPING)) poseStack.mulPose(Axis.YP.rotationDegrees(180 - entityYaw));
		if(entity instanceof LivingEntity livingEntity) {
			//if(livingEntity.isFullyFrozen()) entityYaw += Mth.cos(entity.tickCount * 3.25f) * 23.9f;
			if(livingEntity.deathTime > 0) {
				float f = (livingEntity.deathTime + tickDelta - 1) / 20 * 1.6f;
				f = Mth.sqrt(f);
				if(f > 1) f = 1;
				
				poseStack.mulPose(Axis.ZP.rotationDegrees(f * 90));
			} else if(isEntityUpsideDown(entity)) {
				poseStack.translate(0.0F, (entity.getBbHeight() + 0.1F) / entityScale, 0.0F);
				poseStack.mulPose(Axis.ZP.rotationDegrees(180.0F));
			}
		}
	}
	
	final public void renderExtras(S renderState, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
		super.render(renderState, poseStack, bufferSource, packedLight);
	}
	
	protected static <E extends Entity> boolean isEntityUpsideDown(E entity) {
		if(entity instanceof Player || entity.hasCustomName()) {
			String string = ChatFormatting.stripFormatting(entity.getName().getString());
			
			if("Dinnerbone".equals(string) || "Grumm".equals(string)) {
				return !(entity instanceof Player) || ((Player)entity).isModelPartShown(PlayerModelPart.CAPE);
			}
		}
		return false;
	}
	
	final protected int getSkyLightLevel(E entity, BlockPos pos) {
		return entity.level().getBrightness(LightLayer.SKY, pos);
	}
	final protected int getBlockLightLevel(E entity, BlockPos pos) {
		return entity.isOnFire()? Math.round(14 + Mth.sin(entity.tickCount / 20f)) : entity.level().getBrightness(LightLayer.BLOCK, pos);
	}
	
	final public boolean shouldRender(E entity, Frustum cameraFrustum, double camX, double camY, double camZ) {
		if(!entity.shouldRender(camX, camY, camZ)) return false;
		if(!this.affectedByCulling(entity)) return true;
		
		AABB boundingBox = this.getBoundingBoxForCulling(entity).inflate(0.5);
		if(boundingBox.hasNaN() || boundingBox.getSize() == 0.0) {
			boundingBox = new AABB(
				entity.getX() - 2.0,
				entity.getY() - 2.0,
				entity.getZ() - 2.0,
				entity.getX() + 2.0,
				entity.getY() + 2.0,
				entity.getZ() + 2.0
			);
		}
		if(cameraFrustum.isVisible(boundingBox)) return true;
		
		if(entity instanceof Leashable leashable) {
			Entity leashHolder = leashable.getLeashHolder();
			if(leashHolder != null) return cameraFrustum.isVisible(entity.getBoundingBox());
		}
		return false;
	}
	final public void render(S entity, float entityYaw, float tickDelta, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
		this.render(entity, tickDelta, poseStack, bufferSource, packedLight);
	}
}

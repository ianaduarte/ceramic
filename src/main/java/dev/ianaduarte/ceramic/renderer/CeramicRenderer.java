package dev.ianaduarte.ceramic.renderer;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.ianaduarte.ceramic.layers.CeramicLayerParent;
import dev.ianaduarte.ceramic.layers.CeramicRenderLayer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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

public abstract class CeramicRenderer<E extends Entity, S extends EntityRenderState> extends EntityRenderer<E, S> implements CeramicLayerParent<S> {
	protected final List<CeramicRenderLayer<S>> renderLayers;
	
	protected CeramicRenderer(EntityRendererProvider.Context context) {
		super(context);
		this.renderLayers = Lists.newArrayList();
	}
	
	public abstract ResourceLocation getTextureLocation(S entity);
	
	@Override
	public void render(S renderState, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
		poseStack.pushPose();
			float g = renderState instanceof LivingEntityRenderState lrs? lrs.scale : 1;
			poseStack.scale(g, g, g);
			this.setupRotations(renderState, poseStack);
			poseStack.scale(-1, -1, 1);
			this.scale(renderState, poseStack);
			poseStack.translate(0.0F, -1.501F, 0.0F);
			this.renderLayers(renderState, Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaPartialTick(true), poseStack, bufferSource, packedLight);
		poseStack.popPose();
		this.renderExtras(renderState, poseStack, bufferSource, packedLight);
	}
	
	public void renderLayers(S entity, float tickDelta, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
		for(var layer : this.renderLayers) {
			layer.render(
				entity,
				tickDelta, entity.ageInTicks,
				poseStack, bufferSource, packedLight
			);
		}
	}
	
	protected void scale(S renderState, PoseStack poseStack) {}
	protected void setupRotations(S renderState, PoseStack poseStack) {
		if(renderState instanceof LivingEntityRenderState lrs) this.setupRotations(renderState, poseStack, lrs.bodyRot, lrs.scale, 90);
	}
	protected void setupRotations(S renderState, PoseStack poseStack, float bodyRot, float scale, float flipDeg) {
		if(renderState instanceof LivingEntityRenderState lrs) {
			if(lrs.isFullyFrozen) bodyRot += (float)(Math.cos(Mth.floor(renderState.ageInTicks) * 3.25F) * Math.PI * 0.4F);
			if(!lrs.hasPose(Pose.SLEEPING)) poseStack.mulPose(Axis.YP.rotationDegrees(180.0F - bodyRot));
			
			if(lrs.deathTime > 0) {
				float f = Mth.sqrt((lrs.deathTime - 1f) / 20f * 1.6f);
				if(f > 1.0F) f = 1.0F;
				
				poseStack.mulPose(Axis.ZP.rotationDegrees(f * flipDeg));
			} else if(lrs.isAutoSpinAttack) {
				poseStack.mulPose(Axis.XP.rotationDegrees(-90.0F - lrs.xRot));
				poseStack.mulPose(Axis.YP.rotationDegrees(renderState.ageInTicks * -75.0F));
			} else if (lrs.hasPose(Pose.SLEEPING)) {
				Direction direction = lrs.bedOrientation;
				float g = direction != null ? Direction.getYRot(direction) : bodyRot;
				poseStack.mulPose(Axis.YP.rotationDegrees(g));
				poseStack.mulPose(Axis.ZP.rotationDegrees(flipDeg));
				poseStack.mulPose(Axis.YP.rotationDegrees(270.0F));
			} else if (lrs.isUpsideDown) {
				poseStack.translate(0.0F, (renderState.boundingBoxHeight + 0.1F) / scale, 0.0F);
				poseStack.mulPose(Axis.ZP.rotationDegrees(180.0F));
			}
		}
	}
	
	final public void renderExtras(S renderState, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
		super.render(renderState, poseStack, bufferSource, packedLight);
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
}

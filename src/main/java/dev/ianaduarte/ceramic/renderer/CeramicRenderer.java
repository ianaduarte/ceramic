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
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.PlayerModelPart;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ResolvableProfile;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.AbstractSkullBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.AABB;

import java.util.List;

public abstract class CeramicRenderer<E extends Entity, S extends EntityRenderState> extends EntityRenderer<E, S> implements CeramicLayerParent<S> {
	protected final List<CeramicRenderLayer<S>> renderLayers;
	protected final ItemModelResolver itemModelResolver;
	
	protected CeramicRenderer(EntityRendererProvider.Context context) {
		super(context);
		this.renderLayers = Lists.newArrayList();
		this.itemModelResolver = context.getItemModelResolver();
	}
	
	public abstract ResourceLocation getTextureLocation(S entity);
	
	@Override
	public void render(S renderState, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
		poseStack.pushPose();
			float g = renderState instanceof LivingEntityRenderState lrs? lrs.scale : 1;
			poseStack.scale(g, g, g);
			this.setupRotations(renderState, poseStack);
			this.scale(renderState, poseStack);
			this.renderLayers(renderState, Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaPartialTick(true), poseStack, bufferSource, packedLight);
		poseStack.popPose();
		this.renderExtras(renderState, poseStack, bufferSource, packedLight);
	}
	
	public static boolean isEntityUpsideDown(LivingEntity entity) {
		if (entity instanceof Player || entity.hasCustomName()) {
			String string = ChatFormatting.stripFormatting(entity.getName().getString());
			if ("Dinnerbone".equals(string) || "Grumm".equals(string)) {
				return !(entity instanceof Player) || ((Player)entity).isModelPartShown(PlayerModelPart.CAPE);
			}
		}
		
		return false;
	}
	protected static float solveBodyRot(LivingEntity entity, float yHeadRot, float partialTick) {
		Entity f = entity.getVehicle();
		
		if (f instanceof LivingEntity livingEntity) {
			float r = Mth.rotLerp(partialTick, livingEntity.yBodyRotO, livingEntity.yBodyRot);
			float g = 85.0F;
			float h = Mth.clamp(Mth.wrapDegrees(yHeadRot - r), -85.0F, 85.0F);
			r = yHeadRot - h;
			if (Math.abs(h) > 50.0F) r += h * 0.2F;
			
			return r;
		} else {
			return Mth.rotLerp(partialTick, entity.yBodyRotO, entity.yBodyRot);
		}
	}
	
	@Override
	public void extractRenderState(E entity, S reusedState, float partialTick) {
		super.extractRenderState(entity, reusedState, partialTick);
		if(entity instanceof LivingEntity le && reusedState instanceof LivingEntityRenderState les) {
			float g = Mth.rotLerp(partialTick, le.yHeadRotO, le.yHeadRot);
			les.bodyRot = solveBodyRot(le, g, partialTick);
			les.yRot = Mth.wrapDegrees(g - les.bodyRot);
			les.xRot = le.getXRot(partialTick);
			les.customName = le.getCustomName();
			les.isUpsideDown = isEntityUpsideDown(le);
			if (les.isUpsideDown) {
				les.xRot *= -1.0F;
				les.yRot *= -1.0F;
			}
			
			if (!le.isPassenger() && le.isAlive()) {
				les.walkAnimationPos = le.walkAnimation.position(partialTick);
				les.walkAnimationSpeed = le.walkAnimation.speed(partialTick);
			} else {
				les.walkAnimationPos = 0.0F;
				les.walkAnimationSpeed = 0.0F;
			}
			
			if (le.getVehicle() instanceof LivingEntity livingEntity2) les.wornHeadAnimationPos = livingEntity2.walkAnimation.position(partialTick);
			else les.wornHeadAnimationPos = les.walkAnimationPos;
			
			les.scale = le.getScale();
			les.ageScale = le.getAgeScale();
			les.pose = le.getPose();
			les.bedOrientation = le.getBedOrientation();
			if (les.bedOrientation != null) {
				les.eyeHeight = le.getEyeHeight(Pose.STANDING);
			}

label48: {
				les.isFullyFrozen = le.isFullyFrozen();
				les.isBaby = le.isBaby();
				les.isInWater = le.isInWater();
				les.isAutoSpinAttack = le.isAutoSpinAttack();
				les.hasRedOverlay = le.hurtTime > 0 || le.deathTime > 0;
				ItemStack itemStack = le.getItemBySlot(EquipmentSlot.HEAD);
				Item var8 = itemStack.getItem();
				if (var8 instanceof BlockItem blockItem) {
					Block var12 = blockItem.getBlock();
					
					if (var12 instanceof AbstractSkullBlock abstractSkullBlock) {
						les.wornHeadType = abstractSkullBlock.getType();
						les.wornHeadProfile = itemStack.get(DataComponents.PROFILE);
						les.headItem.clear();
						break label48;
					}
				}
				
				les.wornHeadType = null;
				les.wornHeadProfile = null;
				if (!HumanoidArmorLayer.shouldRender(itemStack, EquipmentSlot.HEAD)) {
					this.itemModelResolver.updateForLiving(les.headItem, itemStack, ItemDisplayContext.HEAD, false, le);
				} else {
					les.headItem.clear();
				}
			}

			
			les.deathTime = le.deathTime > 0 ? le.deathTime + partialTick : 0.0F;
			Minecraft minecraft = Minecraft.getInstance();
			les.isInvisibleToPlayer = les.isInvisible && le.isInvisibleTo(minecraft.player);
			les.appearsGlowing = minecraft.shouldEntityAppearGlowing(le);
		}
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

package dev.ianaduarte.ceramic.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.ianaduarte.ceramic.renderer.CeramicRenderer;
import dev.ianaduarte.ceramic.util.RotationOrder;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.function.Function;

public abstract class CeramicRenderLayer<S extends EntityRenderState, R extends CeramicRenderer<?, S>> {
	public boolean active;
	protected final R renderer;
	protected final Function<ResourceLocation, RenderType> renderType;
	
	protected CeramicRenderLayer(R renderer, Function<ResourceLocation, RenderType> renderType) {
		this.renderer = renderer;
		this.renderType = renderType;
		this.active = true;
	}
	
	public final void render(
		S entity,
		float tickDelta, float currentTick,
		PoseStack poseStack, MultiBufferSource bufferSource,
		int packedLight
	) {
		if(!this.active) return;
		this.renderLayer(entity, tickDelta, currentTick, poseStack, bufferSource, packedLight);
	}
	public abstract void renderLayer(
		S entity,
		float tickDelta, float currentTick,
		PoseStack poseStack, MultiBufferSource bufferSource,
		int packedLight
	);

	public static void applyPartTransform(ModelPart part, PoseStack poseStack) {
		poseStack.translate(part.x / 16, -part.y / 16, part.z / 16);
		poseStack.mulPose(RotationOrder.XZY.getQuaternion(part.xRot, part.yRot, part.zRot));
		poseStack.scale(part.xScale, part.yScale, part.zScale);
	}
	public static int getOverlayCoords(Entity entity, float u) {
		return OverlayTexture.pack(
			OverlayTexture.u(u),
			entity instanceof LivingEntity livingEntity? OverlayTexture.v(livingEntity.hurtTime > 0 || livingEntity.deathTime > 0) : OverlayTexture.v(false)
		);
	}
	
	final public RenderType renderType(ResourceLocation location) {
		return this.renderType.apply(location);
	}
	final public RenderType renderType(S entity) {
		return this.renderType.apply(this.getTextureLocation(entity));
	}
	
	final protected ResourceLocation getTextureLocation(S entity) {
		return this.renderer.getTextureLocation(entity);
	}
}

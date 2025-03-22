package dev.ianaduarte.ceramic.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.ianaduarte.ceramic.geom.CeramicModel;
import dev.ianaduarte.ceramic.renderer.CeramicRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Function;

public abstract class CeramicModelLayer<S extends EntityRenderState, R extends CeramicRenderer<?, S>> extends CeramicRenderLayer<S, R>  {
	protected CeramicModel model;
	
	protected CeramicModelLayer(R renderer, Function<ResourceLocation, RenderType> renderType, CeramicModel model) {
		super(renderer, renderType);
	}
	public final CeramicModel getModel() {
		return model;
	}
	
	protected abstract void poseModel(S entity, float tickDelta, float currentTick);
	
	@Override
	public void renderLayer(S entity, float tickDelta, float currentTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
		this.poseModel(entity, tickDelta, currentTick);
		model.render(poseStack, bufferSource.getBuffer(this.renderType.apply(this.getTextureLocation(entity))), packedLight, OverlayTexture.NO_OVERLAY, -1);
	}
}

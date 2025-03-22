package dev.ianaduarte.ceramic.layers;

import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.resources.ResourceLocation;

public interface CeramicLayerParent<S extends EntityRenderState> {
	ResourceLocation getTextureLocation(S entity);
}

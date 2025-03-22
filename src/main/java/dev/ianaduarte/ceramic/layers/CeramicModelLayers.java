package dev.ianaduarte.ceramic.layers;

import com.google.common.collect.ImmutableMap;
import dev.ianaduarte.ceramic.Ceramic;
import dev.ianaduarte.ceramic.geom.CeramicModel;
import dev.ianaduarte.ceramic.geom.definitions.CeramicModelDefinition;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;

import java.util.HashMap;
import java.util.Map;

public class CeramicModelLayers implements ResourceManagerReloadListener, IdentifiableResourceReloadListener {
	private static final Map<ModelLayerLocation, ModelProvider> ALL_PROVIDERS = new HashMap<>();
	private static Map<ModelLayerLocation, CeramicModelDefinition> MODEL_DEFINITIONS = Map.of();
	
	public static ModelLayerLocation registerModelDefinition(ResourceLocation model, String layer, ModelProvider provider) {
		ModelLayerLocation location = new ModelLayerLocation(model, layer);
		
		ALL_PROVIDERS.put(location, provider);
		return location;
	}
	public static CeramicModel bakeModelDefinition(ModelLayerLocation layerLocation) {
		if(!MODEL_DEFINITIONS.containsKey(layerLocation)) throw new IllegalArgumentException("Inexistent model " + layerLocation);
		return MODEL_DEFINITIONS.get(layerLocation).bake();
	}
	
	public static void registerDefaultLayers() {
		Ceramic.LOGGER.info("Registering default layers");
	}
	
	@Override
	public void onResourceManagerReload(ResourceManager resourceManager) {
		ImmutableMap.Builder<ModelLayerLocation, CeramicModelDefinition> builder = ImmutableMap.builder();
		
		ALL_PROVIDERS.forEach((layerLocation, provider) -> {
			try { builder.put(layerLocation, provider.get()); }
			catch(Exception e) { Ceramic.LOGGER.error("Failed to create model {}", layerLocation); throw e; }
		});
		MODEL_DEFINITIONS = builder.build();
		Ceramic.LOGGER.info("Baked models layers");
	}
	
	@Override
	public ResourceLocation getFabricId() {
		return Ceramic.getLocation("model_layers");
	}
	
	public interface ModelProvider {
		CeramicModelDefinition get();
	}
}

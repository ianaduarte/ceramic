package dev.ianaduarte.ceramic;

import dev.ianaduarte.ceramic.layers.CeramicModelLayers;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.server.packs.PackType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.fabricmc.api.ModInitializer;

import net.minecraft.resources.ResourceLocation;

public class Ceramic implements ClientModInitializer {
	public static final String MOD_ID = "ceramic";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	
	public static ResourceLocation getLocation(String path) {
		return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
	}
	
	
	@Override
	public void onInitializeClient() {
		ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(new CeramicModelLayers());
	}
}